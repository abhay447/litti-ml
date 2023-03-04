package com.litti.ml.entities.model;

import java.util.Map;

public class PredictionRequest {
  private final String id;
  private final Map<String, ?> inputs;

  public PredictionRequest(String id, Map<String, ?> inputs) {
    this.id = id;
    this.inputs = inputs;
  }

  public String getId() {
    return id;
  }

  public Map<String, ?> getInputs() {
    return inputs;
  }

  @Override
  public String toString() {
    return "PredictionRequest{" + "id='" + id + '\'' + ", inputs=" + inputs + '}';
  }
}
