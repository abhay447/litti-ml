package com.litti.ml.management.entiites;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.util.UUID;

@Entity
public class ModelFeatureLinkEntity {

  @Id @GeneratedValue private UUID id;
  private UUID modelId;
  private UUID featureId;

  public ModelFeatureLinkEntity() {}

  public ModelFeatureLinkEntity(UUID modelId) {
    this.modelId = modelId;
  }

  public ModelFeatureLinkEntity(UUID modelId, UUID featureId) {
    this.modelId = modelId;
    this.featureId = featureId;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public UUID getModelId() {
    return modelId;
  }

  public void setModelId(UUID modelId) {
    this.modelId = modelId;
  }

  public UUID getFeatureId() {
    return featureId;
  }

  public void setFeatureId(UUID featureId) {
    this.featureId = featureId;
  }
}
