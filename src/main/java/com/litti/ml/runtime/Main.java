package com.litti.ml.runtime;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.google.common.io.Resources;
import com.litti.ml.entities.ModelMetadata;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {

  public static void main(String[] args) throws IOException {
    ModelMetadata sampleModelMetadata = null;
    ModelMetadata dream11ModelMetadata = null;
    ObjectMapper objectMapper = JsonMapper.builder().addModule(new GuavaModule()).build();
    try {
      final String modelJson =
          Files.readString(Paths.get(Resources.getResource("sample-model-meta.json").toURI()));
      sampleModelMetadata = objectMapper.readValue(modelJson, ModelMetadata.class);
    } catch (Exception e) {
      e.printStackTrace();
    }
    try {
      final String modelJson =
          Files.readString(Paths.get(Resources.getResource("dream11-model-meta.json").toURI()));
      dream11ModelMetadata = objectMapper.readValue(modelJson, ModelMetadata.class);
    } catch (Exception e) {
      e.printStackTrace();
    }

    final ModelRegistry modelRegistry = new ModelRegistry();
    modelRegistry.addModelForPrediction(sampleModelMetadata);
    modelRegistry.addModelForPrediction(dream11ModelMetadata);

    RuntimeHTTPServer runtimeHTTPServer = new RuntimeHTTPServer(modelRegistry);
  }
}
