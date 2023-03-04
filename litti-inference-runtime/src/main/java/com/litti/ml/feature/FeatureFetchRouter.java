package com.litti.ml.feature;

import com.litti.ml.entities.feature.FeatureGroup;
import com.litti.ml.entities.feature.FeatureMetadata;
import com.litti.ml.feature.store.AbstractFeatureStore;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FeatureFetchRouter {
  private static final Logger logger = LogManager.getLogger(FeatureFetchRouter.class);

  private final Map<String, FeatureGroup> inferenceFeatureGroups;
  private final Map<String, AbstractFeatureStore> inferenceFeatureStores;
  private final Map<String, String> featureGroupStoreMap;

  public FeatureFetchRouter() {
    this.inferenceFeatureGroups = new HashMap<>();
    this.inferenceFeatureStores = new HashMap<>();
    this.featureGroupStoreMap = new HashMap<>();
  }

  public void registerFeatureGroup(FeatureGroup featureGroup, AbstractFeatureStore featureStore) {
    this.inferenceFeatureGroups.put(featureGroup.name(), featureGroup);
    this.inferenceFeatureStores.put(featureStore.name(), featureStore);
    this.featureGroupStoreMap.put(featureGroup.name(), featureStore.name());
  }

  public Map<String, ?> fetchFeatures(
      List<FeatureMetadata> featureMetadataList, Map<String, ?> requestInputs) {
    Map<String, Set<FeatureMetadata>> groupedFeatures =
        featureMetadataList.stream()
            .collect(Collectors.groupingBy(FeatureMetadata::featureGroup, Collectors.toSet()));
    return groupedFeatures.entrySet().stream()
        .flatMap(
            groupedEntry ->
                fetchFeatureGroup(groupedEntry.getKey(), groupedEntry.getValue(), requestInputs)
                    .entrySet()
                    .stream())
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
  }

  private Map<String, ?> fetchFeatureGroup(
      String featureGroupName,
      Set<FeatureMetadata> featureMetadataList,
      Map<String, ?> requestInputs) {
    final FeatureGroup featureGroup = this.inferenceFeatureGroups.get(featureGroupName);
    final AbstractFeatureStore featureStore =
        this.inferenceFeatureStores.get(this.featureGroupStoreMap.get(featureGroup.name()));
    return featureStore
        .fetchFeatures(featureMetadataList, featureGroup, requestInputs)
        .getFeatures();
  }
}
