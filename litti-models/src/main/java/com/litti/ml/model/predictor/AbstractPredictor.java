package com.litti.ml.model.predictor;

import com.litti.ml.entities.model.ModelMetadata;
import com.litti.ml.entities.model.PredictionResponse;
import com.litti.ml.feature.FeatureFetchRouter;
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
