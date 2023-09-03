package com.litti.ml.model.predictor;

import com.litti.ml.entities.model.BatchPredictionRequest;
import com.litti.ml.entities.model.BatchPredictionResponse;
import com.litti.ml.entities.model.PredictionResponse;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ModelPredictionManager {

  private final AbstractPredictor modelPredictor;

  public ModelPredictionManager(AbstractPredictor modelPredictor) {
    this.modelPredictor = modelPredictor;
  }

  public BatchPredictionResponse predictSet(BatchPredictionRequest batchPredictionRequest) {
    final Map<String, Map<String, ?>> predictSetMap =
        batchPredictionRequest.getPredictionRequests().stream()
            .map(input -> Map.entry(input.getId(), input.getInputs()))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    final Set<PredictionResponse> predictionResponseSet =
        this.modelPredictor.predictSet(predictSetMap);
    return new BatchPredictionResponse(
        batchPredictionRequest.getBatchPredictionId(), predictionResponseSet);
  }
}
