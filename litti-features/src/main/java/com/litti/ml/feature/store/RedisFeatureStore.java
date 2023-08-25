package com.litti.ml.feature.store;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.litti.ml.entities.dtypes.JsonDataReader;
import com.litti.ml.entities.feature.FeatureGroup;
import com.litti.ml.entities.feature.FeatureMetadata;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import java.util.*;
import java.util.stream.Collectors;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RedisFeatureStore extends AbstractFeatureStore {

  private static final Logger logger = LogManager.getLogger(RedisFeatureStore.class);

  private final GenericObjectPool<StatefulRedisConnection<String, String>> redisPool;

  public RedisFeatureStore(
      JsonDataReader jsonDataReader,
      GenericObjectPool<StatefulRedisConnection<String, String>> redisPool) {
    super(jsonDataReader);
    this.redisPool = redisPool;
  }

  @Override
  public String name() {
    return "redis";
  }

  @Override
  Optional<Map<String, ?>> fetchFeatureFromStore(
      Set<FeatureMetadata> featureMetadataList,
      FeatureGroup featureGroup,
      Map<String, String> dimensions) {
    final Set<String> acceptableFeatures =
        featureMetadataList.stream()
            .map(x -> String.format("%s#%s", x.getName(), x.getVersion()))
            .collect(Collectors.toSet());
    final Optional<Map<String, FeatureStoreRecord>> featureStoreRecords =
        readFeatureRecords(featureMetadataList, featureGroup, dimensions);
    return featureStoreRecords.map(
        records ->
            records.entrySet().stream()
                .filter(entry -> acceptableFeatures.contains(entry.getKey()))
                .filter(entry -> entry.getValue().getValidTo() > System.currentTimeMillis() / 1000)
                .map(
                    entry ->
                        Map.entry(entry.getKey().split("#")[0], entry.getValue().getRawValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
  }

  private Optional<Map<String, FeatureStoreRecord>> readFeatureRecords(
      Set<FeatureMetadata> featureMetadataList,
      FeatureGroup featureGroup,
      Map<String, String> dimensions) {

    final Gson gson = new Gson();
    final String featureGroupKey = this.createFeatureGroupRedisKey(dimensions, featureGroup);
    try (StatefulRedisConnection<String, String> redisConnection = redisPool.borrowObject()) {
      final RedisCommands<String, String> syncCommands = redisConnection.sync();
      final String rawRedisValue = syncCommands.get(featureGroupKey);
      if (rawRedisValue == null || rawRedisValue.equalsIgnoreCase("nil")) {
        return Optional.empty();
      }
      return Optional.of(
          gson.fromJson(
              rawRedisValue, new TypeToken<Map<String, FeatureStoreRecord>>() {}.getType()));
    } catch (Exception e) {
      logger.error("Error occured in fetching features from Redis", e);
      throw new RuntimeException("Error occured in fetching features from Redis");
    }
  }

  @Override
  public void writeFeaturesToStore(
      List<Map<String, ?>> featureRows,
      List<FeatureMetadata> featureMetadataList,
      FeatureGroup featureGroup,
      Boolean merge) {
    featureMetadataList.forEach(
        featureMetadata -> {
          if (!featureMetadata.getFeatureGroup().equalsIgnoreCase(featureGroup.getName())) {
            logger.error("feature:{} has invalid feature group: {}", featureMetadata, featureGroup);
            throw new RuntimeException(
                ("feature group for all features should match supplied feature group"));
          }
        });
    final Map<String, FeatureMetadata> featureMetadataMap =
        featureMetadataList.stream()
            .map(f -> Map.entry(f.getName() + "#" + f.getVersion(), f))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    featureRows.stream()
        .forEach(
            featureRow -> {
              try {
                writeRow(featureRow, featureGroup, featureMetadataMap, merge);
              } catch (Exception e) {
                logger.error("Error occured in writing row {}", featureRow, e);
              }
            });
  }

  private void writeRow(
      Map<String, ?> featureRow,
      FeatureGroup featureGroup,
      Map<String, FeatureMetadata> featureMetadataMap,
      Boolean merge) {
    final Gson gson = new Gson();
    final String featureGroupKey = this.createFeatureGroupRedisKey(featureRow, featureGroup);
    final Map<String, FeatureStoreRecord> inputRow =
        featureRow.entrySet().stream()
            .filter(f -> !featureGroup.getDimensions().contains(f.getKey()))
            .filter(f -> featureMetadataMap.containsKey(f.getKey()))
            .map(
                entry ->
                    Map.entry(
                        entry.getKey(),
                        FeatureStoreRecord.fromValueAndFeatureMetadata(
                            entry.getValue(), featureMetadataMap.get(entry.getKey()))))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    final Map<String, FeatureStoreRecord> redisWriteRow = new HashMap<>();
    if (merge) {
      Optional<Map<String, FeatureStoreRecord>> featureStoreValues =
          this.readFeatureRecords(
              featureMetadataMap.values().stream().collect(Collectors.toSet()),
              featureGroup,
              featureRow.entrySet().stream()
                  .map(e -> Map.entry(e.getKey(), e.getValue().toString()))
                  .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
      if (featureStoreValues.isPresent()) {
        redisWriteRow.putAll(featureStoreValues.get());
      }
    }
    redisWriteRow.putAll(inputRow);
    final Long maxExpiry =
        redisWriteRow.values().stream()
            .map(FeatureStoreRecord::getValidTo)
            .max(Long::compare)
            .get();
    final Long ttl = maxExpiry - System.currentTimeMillis() / 1000;
    try (StatefulRedisConnection<String, String> redisConnection = redisPool.borrowObject()) {
      final RedisCommands<String, String> syncCommands = redisConnection.sync();
      syncCommands.setex(featureGroupKey, ttl, gson.toJson(redisWriteRow));
    } catch (Exception e) {
      logger.error("Error occured in writing features to Redis", e);
      throw new RuntimeException("Error occured in writing features to Redis");
    }
  }

  private String createFeatureGroupRedisKey(Map<String, ?> featureRow, FeatureGroup featureGroup) {
    final List<String> sortedDims = featureGroup.getDimensions().stream().sorted().toList();
    final List<String> sortedDimVals =
        sortedDims.stream().map(x -> featureRow.get(x).toString()).collect(Collectors.toList());
    return String.format("%s|%s", String.join("#", sortedDims), String.join("#", sortedDimVals));
  }
}
