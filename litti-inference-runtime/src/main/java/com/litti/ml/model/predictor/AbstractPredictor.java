package com.litti.ml.model.predictor;

import com.litti.ml.feature.FeatureFetchRouter;
import com.litti.ml.model.entities.ModelMetadata;
import com.litti.ml.model.entities.PredictionResponse;
import java.util.Map;
import java.util.Set;

public abstract class AbstractPredictor {
  private final ModelMetadata modelMetadata;
  private final FeatureFetchRouter featureFetchRouter;

  protected AbstractPredictor(ModelMetadata modelMetadata, FeatureFetchRouter featureFetchRouter) {
    this.modelMetadata = modelMetadata;
    this.featureFetchRouter = featureFetchRouter;
  }

  public abstract PredictionResponse predictSingle(String predictionId, Map<String, ?> features);

  public abstract Set<PredictionResponse> predictSet(
      Map<String, Map<String, ?>> predictionFeaturesMap);

  public ModelMetadata getModelMetadata() {
    return modelMetadata;
  }
}
