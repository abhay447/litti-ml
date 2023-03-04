package com.litti.ml.feature.store;

import com.google.common.collect.Sets;
import com.litti.ml.entities.dtypes.JsonDataReader;
import com.litti.ml.entities.feature.FeatureGroup;
import com.litti.ml.entities.feature.FeatureMetadata;
import java.util.*;
import java.util.stream.Collectors;

public abstract class AbstractFeatureStore {

  private final JsonDataReader jsonDataReader;

  protected AbstractFeatureStore(JsonDataReader jsonDataReader) {
    this.jsonDataReader = jsonDataReader;
  }

  public abstract String name();

  abstract Optional<Map<String, ?>> fetchFeatureFromStore(
      Set<FeatureMetadata> featureMetadataList,
      FeatureGroup featureGroup,
      Map<String, String> dimensions);

  public abstract void writeFeaturesToStore(
      List<Map<String, ?>> featureRows,
      List<FeatureMetadata> featureMetadataList,
      FeatureGroup featureGroup,
      Boolean merge);

  public final FeatureFetchResult fetchFeatures(
      Set<FeatureMetadata> featureMetadataList,
      FeatureGroup featureGroup,
      Map<String, ?> requestInputs) {
    final Optional<Map<String, ?>> featureStoreResult =
        fetchFeatureFromStore(featureMetadataList, featureGroup, extractDimensions(requestInputs));
    final Map<String, ?> featureStoreFeatures =
        featureStoreResult.isPresent() ? featureStoreResult.get() : Collections.emptyMap();
    return overrideFeaturesFromInputAndDefaults(
        featureStoreFeatures, requestInputs, featureMetadataList);
  }

  private FeatureFetchResult overrideFeaturesFromInputAndDefaults(
      Map<String, ?> featureStoreFeatures,
      Map<String, ?> requestInputs,
      Set<FeatureMetadata> featureMetadataSet) {
    Set<String> acceptableFeatureNames =
        featureMetadataSet.stream().map(FeatureMetadata::name).collect(Collectors.toSet());
    Map<String, ?> inputOverrideFeatures =
        requestInputs.entrySet().stream()
            .filter(acceptableFeatureNames::contains)
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    final Map<String, ?> defaultFeatures = createDefaultFeaturesRow(featureMetadataSet);
    Map<String, Object> mergedFeatures = new HashMap<>();
    // start with defaults
    mergedFeatures.putAll(defaultFeatures);
    // top up with feature store result
    mergedFeatures.putAll(featureStoreFeatures);
    // override with request inputs
    mergedFeatures.putAll(inputOverrideFeatures);
    // features defaulted
    final Set<String> featuresCacheMissed =
        Sets.difference(acceptableFeatureNames, featureStoreFeatures.keySet());
    // features overridden
    final Set<String> featuresOverriden = inputOverrideFeatures.keySet();
    return new FeatureFetchResult(mergedFeatures, featuresCacheMissed, featuresOverriden);
  }

  private Map<String, String> extractDimensions(Map<String, ?> requestInputs) {
    return requestInputs.entrySet().stream()
        .map(entry -> Map.entry(entry.getKey(), entry.getValue().toString()))
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
  }

  private Map<String, ?> createDefaultFeaturesRow(Set<FeatureMetadata> featureMetadataList) {
    return featureMetadataList.stream()
        .map(f -> Map.entry(f.name(), jsonDataReader.read(f.defaultValue(), f.dataType())))
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
  }
}
