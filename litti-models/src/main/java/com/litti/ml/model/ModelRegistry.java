package com.litti.ml.model;

import com.litti.ml.entities.model.BatchPredictionRequest;
import com.litti.ml.entities.model.BatchPredictionResponse;
import com.litti.ml.entities.model.ModelMetadata;
import com.litti.ml.feature.FeatureFetchRouter;
import com.litti.ml.model.logger.ModelLogger;
import com.litti.ml.model.predictor.AbstractPredictor;
import com.litti.ml.model.predictor.ModelPredictionManager;
import com.litti.ml.model.predictor.PMMLPredictor;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ModelRegistry {

  static Logger logger = LogManager.getLogger(ModelRegistry.class);
  private final Map<String, ModelPredictionManager> predictionRegistry;

  private final ModelLogger modelLogger;

  private final FeatureFetchRouter featureFetchRouter;

  public ModelRegistry(ModelLogger modelLogger, FeatureFetchRouter featureFetchRouter) {
    this.modelLogger = modelLogger;
    this.featureFetchRouter = featureFetchRouter;
    predictionRegistry = new HashMap<>();
  }

  public void addModelForPrediction(ModelMetadata modelMetadata) {
    try {
      switch (modelMetadata.modelFramework()) {
        case "PMML":
          final AbstractPredictor modelPredictor =
              new PMMLPredictor(modelMetadata, featureFetchRouter);
          final ModelPredictionManager modelPredictionManager =
              new ModelPredictionManager(featureFetchRouter, modelPredictor, modelLogger);
          predictionRegistry.put(
              String.format("%s#%s", modelMetadata.name(), modelMetadata.version()),
              modelPredictionManager);
          break;
        default:
          logger.info("predictor not found for model framework {}", modelMetadata.modelFramework());
          break;
      }
    } catch (Exception e) {
      logger.error("Error occurred in predictor registration model {}", modelMetadata, e);
    }
  }

  public BatchPredictionResponse forwardToRouter(
      String route, BatchPredictionRequest batchPredictionRequest) {
    if (!this.predictionRegistry.containsKey(route)) {
      logger.info("registered routes {}", this.predictionRegistry.keySet());
      throw new RuntimeException("Model/version not found : " + Arrays.asList(route.split("#")));
    }
    return predictionRegistry.get(route).predictSet(batchPredictionRequest);
  }
}
