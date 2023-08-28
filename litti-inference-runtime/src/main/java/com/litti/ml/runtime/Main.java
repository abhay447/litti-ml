package com.litti.ml.runtime;

import com.litti.ml.entities.dtypes.JsonDataReader;
import com.litti.ml.entities.management.client.LittiManagementClient;
import com.litti.ml.feature.FeatureFetchRouter;
import com.litti.ml.feature.loader.FeatureGroupLoader;
import com.litti.ml.feature.loader.StaticResourcesFGLoader;
import com.litti.ml.feature.store.AbstractFeatureStore;
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

  public static final String MGMT_SERVER_URL =
      System.getenv().getOrDefault("MGMT_SERVER_URL", "http://localhost:8081");
  public static final String REDIS_SERVER_URL =
      System.getenv().getOrDefault("REDIS_SERVER_URL", "redis://localhost:6379/0");
  public static final String SERVER_HOST = System.getenv().getOrDefault("SERVER_HOST", "localhost");
  public static final Integer SERVER_PORT =
      Integer.valueOf(System.getenv().getOrDefault("SERVER_PORT", "8001"));
  private static final Logger logger = LogManager.getLogger(Main.class);

  public static void main(String[] args) throws IOException {

    final JsonDataReader jsonDataReader = new JsonDataReader();
    final FeatureFetchRouter featureFetchRouter = new FeatureFetchRouter();
    //    final LocalParquetFeatureStore localParquetStore = new
    // LocalParquetFeatureStore(jsonDataReader);
    final RedisClient redisClient = RedisClient.create(REDIS_SERVER_URL);
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
            new LittiManagementClient(MGMT_SERVER_URL)); // new StaticResourcesModelLoader();
    modelLoader.loadAllModels().getModelsLoaded().forEach(modelRegistry::addModelForPrediction);
    RuntimeHTTPServer runtimeHTTPServer =
        new RuntimeHTTPServer(SERVER_HOST, SERVER_PORT, modelRegistry);
  }
}
