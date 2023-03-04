package com.litti.ml.feature.dtypes;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.gson.Gson;
import com.litti.ml.model.entities.PredictionRequest;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class TestJsonDataReader {
  @Test
  public void testDataTypeCOnversions() throws Exception {
    String json =
        new String(
            getClass()
                .getClassLoader()
                .getResourceAsStream("json_reader_test.json")
                .readAllBytes());
    final Gson gson = new Gson();
    final PredictionRequest predictionRequest = gson.fromJson(json, PredictionRequest.class);
    final Map<String, ?> inputs = predictionRequest.getInputs();
    final JsonDataReader jsonDataReader = new JsonDataReader();
    assertTrue(
        jsonDataReader.read(inputs.get("PRIMITIVE#STRING"), "PRIMITIVE#STRING") instanceof String,
        "PRIMITIVE#STRING");

    assertTrue(
        jsonDataReader.read(inputs.get("PRIMITIVE#LONG"), "PRIMITIVE#LONG") instanceof Long,
        "PRIMITIVE#LONG");

    assertTrue(
        jsonDataReader.read(inputs.get("PRIMITIVE#DOUBLE"), "PRIMITIVE#DOUBLE") instanceof Double,
        "PRIMITIVE#DOUBLE");

    assertTrue(
        jsonDataReader.read(inputs.get("PRIMITIVE#BOOLEAN"), "PRIMITIVE#BOOLEAN")
            instanceof Boolean,
        "PRIMITIVE#BOOLEAN");

    assertTrue(
        ((List) jsonDataReader.read(inputs.get("ARRAY#STRING"), "ARRAY#STRING")).get(0)
            instanceof String,
        "ARRAY#STRING");

    assertTrue(
        ((List) jsonDataReader.read(inputs.get("ARRAY#DOUBLE"), "ARRAY#DOUBLE")).get(0)
            instanceof Double,
        "ARRAY#DOUBLE");

    assertTrue(
        ((List) jsonDataReader.read(inputs.get("ARRAY#LONG"), "ARRAY#LONG")).get(0) instanceof Long,
        "ARRAY#LONG");

    assertTrue(
        ((List) jsonDataReader.read(inputs.get("ARRAY#BOOLEAN"), "ARRAY#BOOLEAN")).get(0)
            instanceof Boolean,
        "ARRAY#BOOLEAN");

    assertTrue(
        ((List) ((List) jsonDataReader.read(inputs.get("MATRIX#STRING"), "MATRIX#STRING")).get(0))
                .get(0)
            instanceof String,
        "MATRIX#STRING");

    assertTrue(
        ((List) ((List) jsonDataReader.read(inputs.get("MATRIX#DOUBLE"), "MATRIX#DOUBLE")).get(0))
                .get(0)
            instanceof Double,
        "MATRIX#DOUBLE");

    assertTrue(
        ((List) ((List) jsonDataReader.read(inputs.get("MATRIX#LONG"), "MATRIX#LONG")).get(0))
                .get(0)
            instanceof Long,
        "MATRIX#LONG");

    assertTrue(
        ((List) ((List) jsonDataReader.read(inputs.get("MATRIX#BOOLEAN"), "MATRIX#BOOLEAN")).get(0))
                .get(0)
            instanceof Boolean,
        "MATRIX#BOOLEAN");
  }
}
