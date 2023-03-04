package com.litti.ml.model.predictor;

import com.litti.ml.entities.model.BatchPredictionRequest;
import com.litti.ml.entities.model.PredictionResponse;
import com.litti.ml.feature.FeatureFetchRouter;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ModelPredictionManager {
  private final FeatureFetchRouter featureFetchRouter;

  private final AbstractPredictor modelPredictor;

  public ModelPredictionManager(
      FeatureFetchRouter featureFetchRouter, AbstractPredictor modelPredictor) {
    this.featureFetchRouter = featureFetchRouter;
    this.modelPredictor = modelPredictor;
  }

  public Set<PredictionResponse> predictSet(BatchPredictionRequest batchPredictionRequest) {
    Map<String, Map<String, ?>> predictSetMap =
        batchPredictionRequest.getPredictionRequests().stream()
            .map(
                input -> {
                  final Map<String, ?> featureStoreFeatures =
                      this.featureFetchRouter.fetchFeatures(
                          modelPredictor.getModelMetadata().features(), input.getInputs());
                  return Map.entry(input.getId(), featureStoreFeatures);
                })
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    return this.modelPredictor.predictSet(predictSetMap);
  }
}
