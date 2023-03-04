package com.litti.ml.entities.model;

import java.util.Collections;
import java.util.Map;

public class PredictionResponse {
  private final String id;
  private final Map<String, ?> outputs;

  private final String errorMessage;

  public PredictionResponse(String id, Map<String, ?> outputs, String errorMessage) {
    this.id = id;
    this.outputs = Collections.unmodifiableMap(outputs);
    this.errorMessage = errorMessage;
  }

  public String getId() {
    return id;
  }

  public Map<String, ?> getOutputs() {
    return outputs;
  }

  public String getErrorMessage() {
    return errorMessage;
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
