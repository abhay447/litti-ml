package com.litti.ml.management.dto;

import com.litti.ml.management.entiites.ModelEntity;

import java.util.List;

public class CreateModelRequest {

  private ModelEntity modelEntity;
  private List<String> featureIds;

  public CreateModelRequest() {}

  public CreateModelRequest(ModelEntity modelEntity, List<String> featureIds) {
    this.modelEntity = modelEntity;
    this.featureIds = featureIds;
  }

  public ModelEntity getModelEntity() {
    return modelEntity;
  }

  public void setModelEntity(ModelEntity modelEntity) {
    this.modelEntity = modelEntity;
  }

  public List<String> getFeatureIds() {
    return featureIds;
  }

  public void setFeatureIds(List<String> featureIds) {
    this.featureIds = featureIds;
  }

  @Override
  public String toString() {
    return "CreateModelRequest{"
        + "modelEntity="
        + modelEntity
        + ", featureIds="
        + featureIds
        + '}';
  }
}
