package com.litti.ml.model.predictor;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.litti.ml.entities.model.ModelMetadata;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.apache.curator.shaded.com.google.common.io.Resources;
import org.junit.jupiter.api.Test;

public class TestModelLoad {

  final ObjectMapper objectMapper = JsonMapper.builder().addModule(new GuavaModule()).build();

  @Test
  public void testPmmlLoad() throws Exception {
    String json =
        Files.readString(
            Paths.get(Resources.getResource("model-metadata/dream11-model-meta.json").toURI()));
    final ModelMetadata modelMetadata = objectMapper.readValue(json, ModelMetadata.class);
    assertDoesNotThrow(() -> new PMMLPredictor(modelMetadata));
  }
}
