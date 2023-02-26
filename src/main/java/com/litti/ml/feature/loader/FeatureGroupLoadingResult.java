package com.litti.ml.feature.loader;

import com.litti.ml.feature.entities.FeatureGroup;

import java.util.Collections;
import java.util.Set;

public class FeatureGroupLoadingResult {

  private final Set<FeatureGroup> featureGroupsLoaded;
  private final Set<String> featureGroupSourcesFailed;

  public FeatureGroupLoadingResult(
      Set<FeatureGroup> featureGroupsLoaded, Set<String> featureGroupSourcesFailed) {
    this.featureGroupsLoaded = Collections.unmodifiableSet(featureGroupsLoaded);
    this.featureGroupSourcesFailed = Collections.unmodifiableSet(featureGroupSourcesFailed);
  }

  public Set<FeatureGroup> getFeatureGroupsLoaded() {
    return featureGroupsLoaded;
  }

  public Set<String> getFeatureGroupSourcesFailed() {
    return featureGroupSourcesFailed;
  }
}
