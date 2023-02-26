package com.litti.ml.runtime;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.google.common.io.Resources;
import com.litti.ml.entities.ModelMetadata;
import com.litti.ml.model.loader.ModelLoader;
import com.litti.ml.model.loader.StaticResourcesModelLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {

  private final static Logger logger = LogManager.getLogger(Main.class);

  public static void main(String[] args) throws IOException {

    final ModelRegistry modelRegistry = new ModelRegistry();
    final ModelLoader staticModelLoader = new StaticResourcesModelLoader();
    staticModelLoader.loadAllModels()
                    .getModelsLoaded()
                            .forEach(modelRegistry::addModelForPrediction);
    RuntimeHTTPServer runtimeHTTPServer = new RuntimeHTTPServer(modelRegistry);
  }
}
