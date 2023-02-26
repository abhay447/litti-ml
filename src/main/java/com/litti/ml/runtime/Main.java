package com.litti.ml.runtime;

import com.litti.ml.model.loader.ModelLoader;
import com.litti.ml.model.loader.StaticResourcesModelLoader;
import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {

  private static final Logger logger = LogManager.getLogger(Main.class);

  public static void main(String[] args) throws IOException {

    final ModelRegistry modelRegistry = new ModelRegistry();
    final ModelLoader staticModelLoader = new StaticResourcesModelLoader();
    staticModelLoader
        .loadAllModels()
        .getModelsLoaded()
        .forEach(modelRegistry::addModelForPrediction);
    RuntimeHTTPServer runtimeHTTPServer = new RuntimeHTTPServer(modelRegistry);
  }
}
