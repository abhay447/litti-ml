package com.litti.ml.management.entiites;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.util.UUID;

@Entity
public class ModelDeploymentGroupLinkEntity {

  @Id @GeneratedValue private UUID id;
  private UUID modelId;
  private UUID modelDeploymentGroupId;
  private String deploymentStatus;

  public ModelDeploymentGroupLinkEntity() {}

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

  public UUID getModelDeploymentGroupId() {
    return modelDeploymentGroupId;
  }

  public void setModelDeploymentGroupId(UUID modelDeploymentGroupId) {
    this.modelDeploymentGroupId = modelDeploymentGroupId;
  }

  public String getDeploymentStatus() {
    return deploymentStatus;
  }

  public void setDeploymentStatus(String deploymentStatus) {
    this.deploymentStatus = deploymentStatus;
  }
}
