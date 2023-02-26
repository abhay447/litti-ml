package com.litti.ml.model.predictor;

import com.litti.ml.model.entities.ModelMetadata;
import com.litti.ml.model.entities.PredictionRequest;
import com.litti.ml.model.entities.PredictionResponse;
import java.util.Set;

public abstract class AbstractPredictor {
  private final ModelMetadata modelMetadata;

  protected AbstractPredictor(ModelMetadata modelMetadata) {
    this.modelMetadata = modelMetadata;
  }

  public abstract PredictionResponse predictSingle(PredictionRequest inputs);

  public abstract Set<PredictionResponse> predictSet(Set<PredictionRequest> inputs);
}
