package com.litti.ml.model.predictor;

import com.litti.ml.entities.model.BatchPredictionRequest;
import com.litti.ml.entities.model.BatchPredictionResponse;
import com.litti.ml.entities.model.PredictionResponse;
import com.litti.ml.feature.FeatureFetchRouter;
import com.litti.ml.model.logger.ModelLogger;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ModelPredictionManager {
  private final FeatureFetchRouter featureFetchRouter;

  private final AbstractPredictor modelPredictor;

  private final ModelLogger modelLogger;

  public ModelPredictionManager(
      FeatureFetchRouter featureFetchRouter,
      AbstractPredictor modelPredictor,
      ModelLogger modelLogger) {
    this.featureFetchRouter = featureFetchRouter;
    this.modelPredictor = modelPredictor;
    this.modelLogger = modelLogger;
  }

  public BatchPredictionResponse predictSet(BatchPredictionRequest batchPredictionRequest) {
    final Map<String, Map<String, ?>> predictSetMap =
        batchPredictionRequest.getPredictionRequests().stream()
            .map(
                input -> {
                  //                  final Map<String, ?> featureStoreFeatures =
                  //                      this.featureFetchRouter.fetchFeatures(
                  //                          modelPredictor.getModelMetadata().getFeatures(),
                  // input.getInputs());
                  return Map.entry(input.getId(), input.getInputs());
                })
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    final Set<PredictionResponse> predictionResponseSet =
        this.modelPredictor.predictSet(predictSetMap);
    //    predictionResponseSet.forEach(
    //        predictionResponse -> {
    //          final ModelLogRecord modelLogRecord =
    //              new ModelLogRecord(
    //                  batchPredictionRequest.getBatchPredictionId(),
    //                  predictionResponse.getId(),
    //                  predictSetMap.get(predictionResponse.getId()),
    //                  predictionResponse.getOutputs());
    //          this.modelLogger.logModelPrediction(modelLogRecord);
    //        });
    return new BatchPredictionResponse(
        batchPredictionRequest.getBatchPredictionId(), predictionResponseSet);
  }
}
