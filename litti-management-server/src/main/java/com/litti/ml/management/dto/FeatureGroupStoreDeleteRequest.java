package com.litti.ml.management.dto;

public class FeatureGroupStoreDeleteRequest {

  private String featureGroupStoreLinkId;

  public FeatureGroupStoreDeleteRequest() {}

  public String getFeatureGroupStoreLinkId() {
    return featureGroupStoreLinkId;
  }

  public void setFeatureGroupStoreLinkId(String featureGroupStoreLinkId) {
    this.featureGroupStoreLinkId = featureGroupStoreLinkId;
  }

  @Override
  public String toString() {
    return "FeatureGroupStoreDeleteRequest{"
        + "featureGroupStoreLinkId='"
        + featureGroupStoreLinkId
        + '\''
        + '}';
  }
}
