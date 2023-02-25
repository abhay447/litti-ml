package com.litti.ml.runtime;

import com.litti.ml.entities.ModelMetadata;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.google.common.io.Resources;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {

  public static void main(String[] args) throws IOException {
    ModelMetadata modelMetadata = null;
    ObjectMapper objectMapper = JsonMapper.builder().addModule(new GuavaModule()).build();
    try {
      final String modelJson =
          Files.readString(Paths.get(Resources.getResource("sample-model-meta.json").toURI()));
      modelMetadata = objectMapper.readValue(modelJson, ModelMetadata.class);
    } catch (Exception e) {
      e.printStackTrace();
    }

    final ModelRegistry modelRegistry = new ModelRegistry();
    modelRegistry.addModelForPrediction(modelMetadata);

    RuntimeHTTPServer runtimeHTTPServer = new RuntimeHTTPServer(modelRegistry);
  }
}
