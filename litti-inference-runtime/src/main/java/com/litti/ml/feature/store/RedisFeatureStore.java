package com.litti.ml.feature.store;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.litti.ml.entities.dtypes.JsonDataReader;
import com.litti.ml.entities.feature.FeatureGroup;
import com.litti.ml.entities.feature.FeatureMetadata;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class RedisFeatureStore extends AbstractFeatureStore {

  private final StatefulRedisConnection redisConnection; // TODO: replace with connection pool

  protected RedisFeatureStore(
      JsonDataReader jsonDataReader, StatefulRedisConnection redisConnection) {
    super(jsonDataReader);
    this.redisConnection = redisConnection;
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
            .map(x -> String.format("%s#%s", x.name(), x.version()))
            .collect(Collectors.toSet());
    final Gson gson = new Gson();
    final String featureGroupKey = this.createFeatureGroupRedisKey(dimensions, featureGroup);
    final RedisCommands<String, String> syncCommands = this.redisConnection.sync();
    final String rawRedisValue = syncCommands.get(featureGroupKey);
    if (rawRedisValue.equalsIgnoreCase("nil")) {
      return Optional.empty();
    }
    final Map<String, FeatureStoreRecord> featureStoreRecords =
        gson.fromJson(rawRedisValue, new TypeToken<Map<String, FeatureStoreRecord>>() {}.getType());
    final Map<String, ?> features =
        featureStoreRecords.entrySet().stream()
            .filter(entry -> acceptableFeatures.contains(entry.getKey()))
            .filter(entry -> entry.getValue().getValidTo() > System.currentTimeMillis() / 1000)
            .map(entry -> Map.entry(entry.getKey().split("#")[0], entry.getValue().getRawValue()))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    return Optional.of(features);
  }

  void writeFeaturesToStore(
      List<Map<String, ?>> featureRows,
      Map<String, FeatureMetadata> featureMetadataMap,
      FeatureGroup featureGroup) {
    final Gson gson = new Gson();
    final long ttl = System.currentTimeMillis() + 86400; // use this from feature group
    featureRows.stream()
        .forEach(
            featureRow -> {
              final String featureGroupKey =
                  this.createFeatureGroupRedisKey(featureRow, featureGroup);
              final Map<String, FeatureStoreRecord> recordsMap =
                  featureRow.entrySet().stream()
                      .filter(f -> !featureGroup.dimensions().contains(f.getKey()))
                      .filter(f -> featureMetadataMap.containsKey(f.getKey()))
                      .map(
                          entry ->
                              Map.entry(
                                  entry.getKey(),
                                  FeatureStoreRecord.fromValueAndFeatureMetadata(
                                      entry.getValue(), featureMetadataMap.get(entry.getKey()))))
                      .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

              // TODO: merge records incase key already exists, overwrite features in new recordsMap
              final RedisCommands<String, String> syncCommands = this.redisConnection.sync();
              syncCommands.setex(featureGroupKey, ttl, gson.toJson(recordsMap));
            });
  }

  private String createFeatureGroupRedisKey(Map<String, ?> featureRow, FeatureGroup featureGroup) {
    final List<String> sortedDims = featureGroup.dimensions().stream().sorted().toList();
    final List<String> sortedDimVals =
        sortedDims.stream().map(x -> featureRow.get(x).toString()).collect(Collectors.toList());
    return String.format("%s-%s", String.join("#", sortedDims), String.join("#", sortedDimVals));
  }
}
