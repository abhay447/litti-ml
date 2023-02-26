package com.litti.ml.model;

import com.litti.ml.feature.FeatureFetchRouter;
import com.litti.ml.model.entities.ModelMetadata;
import com.litti.ml.model.entities.PredictionRequest;
import com.litti.ml.model.entities.PredictionResponse;
import com.litti.ml.model.predictor.AbstractPredictor;
import com.litti.ml.model.predictor.PMMLPredictor;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ModelRegistry {

  static Logger logger = LogManager.getLogger(ModelRegistry.class);
  private final Map<String, AbstractPredictor> predictionRegistry;

  private final FeatureFetchRouter featureFetchRouter;

  public ModelRegistry(FeatureFetchRouter featureFetchRouter) {
    this.featureFetchRouter = featureFetchRouter;
    predictionRegistry = new HashMap<>();
  }

  public void addModelForPrediction(ModelMetadata modelMetadata) {
    try {
      switch (modelMetadata.modelFramework()) {
        case "PMML":
          predictionRegistry.put(
              String.format("%s#%s", modelMetadata.name(), modelMetadata.version()),
              new PMMLPredictor(modelMetadata, featureFetchRouter));
          break;
        default:
          logger.info("predictor not found for model framework {}", modelMetadata.modelFramework());
          break;
      }
    } catch (Exception e) {
      logger.error("Error occurred in predictor registration model {}", modelMetadata, e);
    }
  }

  public Set<PredictionResponse> forwardToPredictor(String route, Set<PredictionRequest> request) {
    if (!this.predictionRegistry.containsKey(route)) {
      logger.info("registered routes {}", this.predictionRegistry.keySet());
      throw new RuntimeException("Model/version not found : " + Arrays.asList(route.split("#")));
    }
    return predictionRegistry.get(route).predictSet(request);
  }
}
