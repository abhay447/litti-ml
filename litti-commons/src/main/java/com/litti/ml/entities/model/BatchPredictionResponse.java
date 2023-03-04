package com.litti.ml.entities.model;

import java.util.Collections;
import java.util.Set;

public class BatchPredictionResponse {
  private final String batchPredictionId;

  private final Set<PredictionResponse> predictionResponses;

  public BatchPredictionResponse(String id, Set<PredictionResponse> predictionResponses) {
    this.batchPredictionId = id;
    this.predictionResponses = Collections.unmodifiableSet(predictionResponses);
  }

  public String getBatchPredictionId() {
    return batchPredictionId;
  }

  public Set<PredictionResponse> getPredictionResponses() {
    return predictionResponses;
  }
}
