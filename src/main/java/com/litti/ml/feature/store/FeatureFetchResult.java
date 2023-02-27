package com.litti.ml.feature.store;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import java.util.Map;
import java.util.Set;

public class FeatureFetchResult {

  private final Map<String, ?> features;
  private final Set<String> featureStoreMisses;

  private final Set<String> featuresOverriden;

  public FeatureFetchResult(
      Map<String, ?> features, Set<String> featureStoreMisses, Set<String> featuresOverriden) {
    this.features = ImmutableMap.copyOf(features);
    this.featureStoreMisses = ImmutableSet.copyOf(featureStoreMisses);
    this.featuresOverriden = ImmutableSet.copyOf(featuresOverriden);
  }

  public Map<String, ?> getFeatures() {
    return features;
  }

  public Set<String> getFeatureStoreMisses() {
    return featureStoreMisses;
  }

  public Set<String> getFeaturesOverriden() {
    return featuresOverriden;
  }
}
