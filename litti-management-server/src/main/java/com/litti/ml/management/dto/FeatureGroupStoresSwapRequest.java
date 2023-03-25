package com.litti.ml.management.dto;

public class FeatureGroupStoresSwapRequest {

  private String featureGroupId;

  public FeatureGroupStoresSwapRequest() {}

  public FeatureGroupStoresSwapRequest(String featureGroupId) {
    this.featureGroupId = featureGroupId;
  }

  public String getFeatureGroupId() {
    return featureGroupId;
  }

  public void setFeatureGroupId(String featureGroupId) {
    this.featureGroupId = featureGroupId;
  }

  @Override
  public String toString() {
    return "FeatureGroupStoreSwapRequest{" + "featureGroupId='" + featureGroupId + '\'' + '}';
  }
}
