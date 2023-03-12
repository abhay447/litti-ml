package com.litti.ml.management.dto;

import com.litti.ml.management.entiites.ModelEntity;

import java.util.List;

public class CreateModelRequest {

  private final ModelEntity modelEntity;
  private final List<String> featureIds;

  public CreateModelRequest(ModelEntity modelEntity, List<String> featureIds) {
    this.modelEntity = modelEntity;
    this.featureIds = featureIds;
  }

  public ModelEntity getModelEntity() {
    return modelEntity;
  }

  public List<String> getFeatureIds() {
    return featureIds;
  }
}
