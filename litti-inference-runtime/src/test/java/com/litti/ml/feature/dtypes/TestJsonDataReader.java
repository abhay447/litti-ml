package com.litti.ml.feature.dtypes;

import static org.junit.Assert.assertTrue;

import com.google.gson.Gson;
import com.litti.ml.model.entities.PredictionRequest;
import java.util.List;
import java.util.Map;
import org.junit.Test;

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
        "PRIMITIVE#STRING",
        jsonDataReader.read(inputs.get("PRIMITIVE#STRING"), "PRIMITIVE#STRING") instanceof String);

    assertTrue(
        "PRIMITIVE#LONG",
        jsonDataReader.read(inputs.get("PRIMITIVE#LONG"), "PRIMITIVE#LONG") instanceof Long);

    assertTrue(
        "PRIMITIVE#DOUBLE",
        jsonDataReader.read(inputs.get("PRIMITIVE#DOUBLE"), "PRIMITIVE#DOUBLE") instanceof Double);

    assertTrue(
        "PRIMITIVE#BOOLEAN",
        jsonDataReader.read(inputs.get("PRIMITIVE#BOOLEAN"), "PRIMITIVE#BOOLEAN")
            instanceof Boolean);

    assertTrue(
        "ARRAY#STRING",
        ((List) jsonDataReader.read(inputs.get("ARRAY#STRING"), "ARRAY#STRING")).get(0)
            instanceof String);

    assertTrue(
        "ARRAY#DOUBLE",
        ((List) jsonDataReader.read(inputs.get("ARRAY#DOUBLE"), "ARRAY#DOUBLE")).get(0)
            instanceof Double);

    assertTrue(
        "ARRAY#LONG",
        ((List) jsonDataReader.read(inputs.get("ARRAY#LONG"), "ARRAY#LONG")).get(0)
            instanceof Long);

    assertTrue(
        "ARRAY#BOOLEAN",
        ((List) jsonDataReader.read(inputs.get("ARRAY#BOOLEAN"), "ARRAY#BOOLEAN")).get(0)
            instanceof Boolean);

    assertTrue(
        "MATRIX#STRING",
        ((List) ((List) jsonDataReader.read(inputs.get("MATRIX#STRING"), "MATRIX#STRING")).get(0))
                .get(0)
            instanceof String);

    assertTrue(
        "MATRIX#DOUBLE",
        ((List) ((List) jsonDataReader.read(inputs.get("MATRIX#DOUBLE"), "MATRIX#DOUBLE")).get(0))
                .get(0)
            instanceof Double);

    assertTrue(
        "MATRIX#LONG",
        ((List) ((List) jsonDataReader.read(inputs.get("MATRIX#LONG"), "MATRIX#LONG")).get(0))
                .get(0)
            instanceof Long);

    assertTrue(
        "MATRIX#BOOLEAN",
        ((List) ((List) jsonDataReader.read(inputs.get("MATRIX#BOOLEAN"), "MATRIX#BOOLEAN")).get(0))
                .get(0)
            instanceof Boolean);
  }
}
