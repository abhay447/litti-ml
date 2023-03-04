package com.litti.ml.runtime;

import com.litti.ml.entities.dtypes.JsonDataReader;
import com.litti.ml.feature.FeatureFetchRouter;
import com.litti.ml.feature.loader.FeatureGroupLoader;
import com.litti.ml.feature.loader.StaticResourcesFGLoader;
import com.litti.ml.feature.store.AbstractFeatureStore;
import com.litti.ml.feature.store.LocalParquetFeatureStore;
import com.litti.ml.feature.store.RedisFeatureStore;
import com.litti.ml.model.ModelRegistry;
import com.litti.ml.model.loader.ModelLoader;
import com.litti.ml.model.loader.StaticResourcesModelLoader;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {

  private static final Logger logger = LogManager.getLogger(Main.class);

  public static void main(String[] args) throws IOException {

    final JsonDataReader jsonDataReader = new JsonDataReader();
    final FeatureFetchRouter featureFetchRouter = new FeatureFetchRouter();
    final LocalParquetFeatureStore localParquetStore = new LocalParquetFeatureStore(jsonDataReader);
    final RedisClient redisClient = RedisClient.create("redis://localhost:6379/0");
    final StatefulRedisConnection<String, String> redisConnection = redisClient.connect();
    logger.info(redisConnection.sync().get("1"));
    final AbstractFeatureStore redisFeatureStore =
        new RedisFeatureStore(jsonDataReader, redisConnection);
    final FeatureGroupLoader featureGroupLoader = new StaticResourcesFGLoader();
    featureGroupLoader
        .loadAllFeatureGroups()
        .getFeatureGroupsLoaded()
        .forEach(fg -> featureFetchRouter.registerFeatureGroup(fg, redisFeatureStore));
    final ModelRegistry modelRegistry = new ModelRegistry(featureFetchRouter);
    final ModelLoader staticModelLoader = new StaticResourcesModelLoader();
    staticModelLoader
        .loadAllModels()
        .getModelsLoaded()
        .forEach(modelRegistry::addModelForPrediction);
    RuntimeHTTPServer runtimeHTTPServer = new RuntimeHTTPServer(modelRegistry);
  }
}
