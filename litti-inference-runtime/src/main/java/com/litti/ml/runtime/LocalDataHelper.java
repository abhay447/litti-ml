package com.litti.ml.runtime;

import com.litti.ml.entities.dtypes.JsonDataReader;
import com.litti.ml.entities.feature.FeatureGroup;
import com.litti.ml.entities.feature.FeatureMetadata;
import com.litti.ml.entities.model.ModelMetadata;
import com.litti.ml.feature.loader.FeatureGroupLoader;
import com.litti.ml.feature.loader.StaticResourcesFGLoader;
import com.litti.ml.feature.store.AbstractFeatureStore;
import com.litti.ml.feature.store.LocalParquetFeatureStore;
import com.litti.ml.feature.store.RedisFeatureStore;
import com.litti.ml.model.loader.StaticResourcesModelLoader;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class LocalDataHelper {

  // class to used in local deployments to load data to redis from parquet
  public static void main(String[] args) {

    final JsonDataReader jsonDataReader = new JsonDataReader();
    final LocalParquetFeatureStore localParquetStore = new LocalParquetFeatureStore(jsonDataReader);
    final RedisClient redisClient = RedisClient.create("redis://localhost:6379/0");
    final StatefulRedisConnection<String, String> redisConnection = redisClient.connect();
    final AbstractFeatureStore redisFeatureStore =
        new RedisFeatureStore(jsonDataReader, redisConnection);

    final FeatureGroupLoader featureGroupLoader = new StaticResourcesFGLoader();
    final Map<String, FeatureGroup> fgMap =
        featureGroupLoader.loadAllFeatureGroups().getFeatureGroupsLoaded().stream()
            .map(fg -> Map.entry(fg.name(), fg))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    final StaticResourcesModelLoader staticModelLoader = new StaticResourcesModelLoader();
    final Map<String, ModelMetadata> modelsMap =
        staticModelLoader.loadAllModels().getModelsLoaded().stream()
            .map(model -> Map.entry(model.name() + "#" + model.version(), model))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    final ModelMetadata modelMetadata = modelsMap.get("dream11_model#v1");
    final Set<String> allFeatureNames =
        modelMetadata.features().stream().map(FeatureMetadata::name).collect(Collectors.toSet());
    final Map<String, Set<FeatureMetadata>> groupedFeatures =
        modelMetadata.features().stream()
            .collect(Collectors.groupingBy(FeatureMetadata::featureGroup, Collectors.toSet()));
    List<Map<String, ?>> rawRows =
        localParquetStore.readAllLocalRecords().stream()
            .map(
                record ->
                    record.getSchema().getFields().stream()
                        .filter(f -> record.get(f.name()) != null)
                        .map(
                            f -> {
                              if (allFeatureNames.contains(f.name())) {
                                return Map.entry(f.name() + "#v1", record.get(f.name())); // feature
                              } else {
                                Object value =
                                    f.name().equals("dt")
                                        ? LocalDate.ofEpochDay((Integer) record.get("dt"))
                                            .toString()
                                        : record.get(f.name());
                                return Map.entry(f.name(), value); // dimension
                              }
                            })
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)))
            .collect(Collectors.toList());
    groupedFeatures
        .entrySet()
        .forEach(
            groupedFeature -> {
              final FeatureGroup featureGroup = fgMap.get(groupedFeature.getKey());
              redisFeatureStore.writeFeaturesToStore(
                  rawRows, groupedFeature.getValue().stream().toList(), featureGroup, true);
            });
  }
}
