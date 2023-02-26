package com.litti.ml.runtime;

import com.litti.ml.feature.FeatureFetchRouter;
import com.litti.ml.feature.loader.FeatureGroupLoader;
import com.litti.ml.feature.loader.StaticResourcesFGLoader;
import com.litti.ml.feature.store.FeatureStore;
import com.litti.ml.feature.store.LocalParquetFeatureStore;
import com.litti.ml.model.ModelRegistry;
import com.litti.ml.model.loader.ModelLoader;
import com.litti.ml.model.loader.StaticResourcesModelLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class Main {

  private static final Logger logger = LogManager.getLogger(Main.class);

  public static void main(String[] args) throws IOException {

    final FeatureFetchRouter featureFetchRouter = new FeatureFetchRouter();
    final FeatureStore localParquetStore = new LocalParquetFeatureStore();
    final FeatureGroupLoader featureGroupLoader = new StaticResourcesFGLoader();
    featureGroupLoader
        .loadAllFeatureGroups()
        .getFeatureGroupsLoaded()
        .forEach(fg -> featureFetchRouter.registerFeatureGroup(fg, localParquetStore));
    final ModelRegistry modelRegistry = new ModelRegistry(featureFetchRouter);
    final ModelLoader staticModelLoader = new StaticResourcesModelLoader();
    staticModelLoader
        .loadAllModels()
        .getModelsLoaded()
        .forEach(modelRegistry::addModelForPrediction);
    RuntimeHTTPServer runtimeHTTPServer = new RuntimeHTTPServer(modelRegistry);
  }
}
