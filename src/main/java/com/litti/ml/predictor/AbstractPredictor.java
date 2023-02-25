package com.litti.ml.predictor;

import com.litti.ml.entities.ModelMetadata;
import com.litti.ml.entities.PredictionRequest;
import com.litti.ml.entities.PredictionResponse;

import java.util.Set;

public abstract class AbstractPredictor {
  private final ModelMetadata modelMetadata;

  protected AbstractPredictor(ModelMetadata modelMetadata) {
    this.modelMetadata = modelMetadata;
  }

  public abstract PredictionResponse predictSingle(PredictionRequest inputs);

  public abstract Set<PredictionResponse> predictSet(Set<PredictionRequest> inputs);
}
