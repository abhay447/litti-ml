package com.litti.ml.runtime;

import com.litti.ml.entities.dtypes.JsonDataReader;
import com.litti.ml.entities.management.client.LittiManagementClient;
import com.litti.ml.feature.FeatureFetchRouter;
import com.litti.ml.feature.loader.FeatureGroupLoader;
import com.litti.ml.feature.loader.StaticResourcesFGLoader;
import com.litti.ml.feature.store.AbstractFeatureStore;
import com.litti.ml.feature.store.LocalParquetFeatureStore;
import com.litti.ml.feature.store.RedisFeatureStore;
import com.litti.ml.model.ModelRegistry;
import com.litti.ml.model.loader.LittiManagementModelLoader;
import com.litti.ml.model.loader.ModelLoader;
import com.litti.ml.model.logger.ModelConsoleLogger;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.support.ConnectionPoolSupport;
import java.io.IOException;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {

  private static final Logger logger = LogManager.getLogger(Main.class);

  public static void main(String[] args) throws IOException {

    final JsonDataReader jsonDataReader = new JsonDataReader();
    final FeatureFetchRouter featureFetchRouter = new FeatureFetchRouter();
    final LocalParquetFeatureStore localParquetStore = new LocalParquetFeatureStore(jsonDataReader);
    final RedisClient redisClient = RedisClient.create("redis://localhost:6379/0");
    final GenericObjectPool<StatefulRedisConnection<String, String>> redisPool =
        ConnectionPoolSupport.createGenericObjectPool(
            redisClient::connect, new GenericObjectPoolConfig());
    final AbstractFeatureStore redisFeatureStore = new RedisFeatureStore(jsonDataReader, redisPool);
    final FeatureGroupLoader featureGroupLoader = new StaticResourcesFGLoader();
    featureGroupLoader
        .loadAllFeatureGroups()
        .getFeatureGroupsLoaded()
        .forEach(fg -> featureFetchRouter.registerFeatureGroup(fg, redisFeatureStore));
    final ModelRegistry modelRegistry =
        new ModelRegistry(new ModelConsoleLogger(), featureFetchRouter);
    final ModelLoader modelLoader =
        new LittiManagementModelLoader(
            new LittiManagementClient(
                "http://localhost:8081")); // new StaticResourcesModelLoader();
    modelLoader.loadAllModels().getModelsLoaded().forEach(modelRegistry::addModelForPrediction);
    RuntimeHTTPServer runtimeHTTPServer = new RuntimeHTTPServer(modelRegistry);
  }
}
