package com.litti.ml.runtime;

import static com.litti.ml.runtime.Main.MGMT_SERVER_URL;

import com.litti.ml.entities.dtypes.JsonDataReader;
import com.litti.ml.entities.feature.FeatureGroup;
import com.litti.ml.entities.feature.FeatureMetadata;
import com.litti.ml.entities.management.client.LittiManagementClient;
import com.litti.ml.entities.model.ModelMetadata;
import com.litti.ml.feature.loader.FeatureGroupLoader;
import com.litti.ml.feature.loader.LittiManagementFGLoader;
import com.litti.ml.feature.store.AbstractFeatureStore;
import com.litti.ml.feature.store.LocalParquetFeatureStore;
import com.litti.ml.feature.store.RedisFeatureStore;
import com.litti.ml.model.loader.LittiManagementModelLoader;
import com.litti.ml.model.loader.ModelLoader;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.support.ConnectionPoolSupport;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

public class LocalDataHelper {

  // class to used in local deployments to load data to redis from parquet
  public static void main(String[] args) throws IOException {

    final LittiManagementClient littiManagementClient = new LittiManagementClient(MGMT_SERVER_URL);
    final JsonDataReader jsonDataReader = new JsonDataReader();
    final LocalParquetFeatureStore localParquetStore = new LocalParquetFeatureStore(jsonDataReader);
    final RedisClient redisClient = RedisClient.create("redis://localhost:6379/0");
    final GenericObjectPool<StatefulRedisConnection<String, String>> redisPool =
        ConnectionPoolSupport.createGenericObjectPool(
            () -> redisClient.connect(), new GenericObjectPoolConfig());
    final AbstractFeatureStore redisFeatureStore = new RedisFeatureStore(jsonDataReader, redisPool);
    final FeatureGroupLoader featureGroupLoader =
        new LittiManagementFGLoader(littiManagementClient);
    final Map<String, FeatureGroup> fgMap =
        featureGroupLoader.loadAllFeatureGroups().getFeatureGroupsLoaded().stream()
            .map(fg -> Map.entry(fg.getName(), fg))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    final ModelLoader modelLoader = new LittiManagementModelLoader(littiManagementClient);
    final Map<String, ModelMetadata> modelsMap =
        modelLoader.loadAllModels().getModelsLoaded().stream()
            .map(model -> Map.entry(model.getName() + "#" + model.getVersion(), model))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    final ModelMetadata modelMetadata = modelsMap.get("dream11_model#v1");
    final Set<String> allFeatureNames =
        modelMetadata.getFeatures().stream()
            .map(FeatureMetadata::getName)
            .collect(Collectors.toSet());
    final Map<String, Set<FeatureMetadata>> groupedFeatures =
        modelMetadata.getFeatures().stream()
            .collect(Collectors.groupingBy(FeatureMetadata::getFeatureGroup, Collectors.toSet()));
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
