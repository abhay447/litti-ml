package com.litti.ml.entities.model;

import java.util.Collections;
import java.util.Set;

public class BatchPredictionRequest {
  private final String batchPredictionId;

  private final Set<PredictionRequest> predictionRequests;

  public BatchPredictionRequest(String id, Set<PredictionRequest> predictionRequests) {
    this.batchPredictionId = id;
    this.predictionRequests = Collections.unmodifiableSet(predictionRequests);
  }

  public String getBatchPredictionId() {
    return batchPredictionId;
  }

  public Set<PredictionRequest> getPredictionRequests() {
    return predictionRequests;
  }
}
