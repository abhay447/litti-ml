package com.litti.ml.model.logger;

import java.util.Collections;
import java.util.Map;

public class ModelLogRecord {

  private final String batchPredictionId;

  private final String predictionId;

  private final Map<String, ?> inputs;

  private final Map<String, ?> outputs;

  public ModelLogRecord(
      String batchPredictionId,
      String predictionId,
      Map<String, ?> inputs,
      Map<String, ?> outputs) {
    this.batchPredictionId = batchPredictionId;
    this.predictionId = predictionId;
    this.inputs = Collections.unmodifiableMap(inputs);
    this.outputs = Collections.unmodifiableMap(outputs);
  }

  public String getBatchPredictionId() {
    return batchPredictionId;
  }

  public String getPredictionId() {
    return predictionId;
  }

  public Map<String, ?> getInputs() {
    return inputs;
  }

  public Map<String, ?> getOutputs() {
    return outputs;
  }
}
