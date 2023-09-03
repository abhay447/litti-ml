package com.litti.ml.runtime;

import com.litti.ml.entities.dtypes.JsonDataReader;
import com.litti.ml.entities.management.client.LittiManagementClient;
import com.litti.ml.feature.FeatureFetchRouter;
import com.litti.ml.model.ModelRegistry;
import com.litti.ml.model.loader.LittiManagementModelLoader;
import com.litti.ml.model.loader.ModelLoader;
import java.io.IOException;
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
    final LittiManagementClient littiManagementClient = new LittiManagementClient(MGMT_SERVER_URL);
    final ModelRegistry modelRegistry = new ModelRegistry();
    final ModelLoader modelLoader = new LittiManagementModelLoader(littiManagementClient);
    modelLoader.loadAllModels().getModelsLoaded().forEach(modelRegistry::addModelForPrediction);
    RuntimeHTTPServer runtimeHTTPServer =
        new RuntimeHTTPServer(SERVER_HOST, SERVER_PORT, modelRegistry);
  }
}
