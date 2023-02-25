package com.litti.ml.entities;

import java.util.Map;

public class PredictionResponse {
  private final String id;
  private final Map<String, ?> outputs;

  private final String errorMessage;

  public PredictionResponse(String id, Map<String, ?> outputs, String errorMessage) {
    this.id = id;
    this.outputs = outputs;
    this.errorMessage = errorMessage;
  }

  @Override
  public String toString() {
    return "PredictionResponse{"
        + "id='"
        + id
        + '\''
        + ", outputs="
        + outputs
        + ", errorMessage='"
        + errorMessage
        + '\''
        + '}';
  }
}
