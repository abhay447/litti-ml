package com.litti.ml.feature.store;

import com.litti.ml.entities.feature.FeatureGroup;
import com.litti.ml.entities.feature.FeatureMetadata;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class AbstractFeatureStore {

  public abstract String name();

  abstract FeatureFetchResult fetchFeatureFromStore(
      List<FeatureMetadata> featureMetadataList,
      FeatureGroup featureGroup,
      Map<String, String> dimensions);

  public FeatureFetchResult fetchFeatures(
      List<FeatureMetadata> featureMetadataList,
      FeatureGroup featureGroup,
      Map<String, ?> requestInputs) {
    FeatureFetchResult featureStoreFeatures =
        fetchFeatureFromStore(featureMetadataList, featureGroup, extractDimensions(requestInputs));
    return overrideFeaturesFromInput(featureStoreFeatures, requestInputs);
  }

  private FeatureFetchResult overrideFeaturesFromInput(
      FeatureFetchResult featureStoreFeatures, Map<String, ?> requestInputs) {
    Set<String> featuresOverriden = new HashSet<>();
    Map<String, ?> enrichedFeatures =
        featureStoreFeatures.getFeatures().entrySet().stream()
            .map(
                entry -> {
                  if (requestInputs.containsKey(entry.getKey())) {
                    featuresOverriden.add(entry.getKey());
                    return Map.entry(entry.getKey(), requestInputs.get(entry.getKey()));
                  }
                  return Map.entry(entry.getKey(), entry.getValue());
                })
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    return new FeatureFetchResult(
        enrichedFeatures, featureStoreFeatures.getFeatureStoreMisses(), featuresOverriden);
  }

  private Map<String, String> extractDimensions(Map<String, ?> requestInputs) {
    return requestInputs.entrySet().stream()
        .map(entry -> Map.entry(entry.getKey(), entry.getValue().toString()))
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
  }
}
