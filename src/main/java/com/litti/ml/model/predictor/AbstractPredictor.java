package com.litti.ml.model.predictor;

import com.litti.ml.feature.FeatureFetchRouter;
import com.litti.ml.model.entities.ModelMetadata;
import com.litti.ml.model.entities.PredictionRequest;
import com.litti.ml.model.entities.PredictionResponse;
import java.util.Set;

public abstract class AbstractPredictor {
  private final ModelMetadata modelMetadata;
  private final FeatureFetchRouter featureFetchRouter;

  protected AbstractPredictor(ModelMetadata modelMetadata, FeatureFetchRouter featureFetchRouter) {
    this.modelMetadata = modelMetadata;
    this.featureFetchRouter = featureFetchRouter;
  }

  public abstract PredictionResponse predictSingle(PredictionRequest inputs);

  public abstract Set<PredictionResponse> predictSet(Set<PredictionRequest> inputs);
}
