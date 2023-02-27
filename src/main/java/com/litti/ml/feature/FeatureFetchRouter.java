package com.litti.ml.feature;

import com.litti.ml.feature.entities.FeatureGroup;
import com.litti.ml.feature.entities.FeatureMetadata;
import com.litti.ml.feature.store.AbstractFeatureStore;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    Map<String, List<FeatureMetadata>> groupedFeatures =
        featureMetadataList.stream()
            .collect(Collectors.groupingBy(FeatureMetadata::featureGroup, Collectors.toList()));
    Map<String, ?> fetchedFeatures =
        groupedFeatures.entrySet().stream()
            .flatMap(
                groupedEntry -> {
                  final FeatureGroup featureGroup =
                      this.inferenceFeatureGroups.get(groupedEntry.getKey());
                  final AbstractFeatureStore featureStore =
                      this.inferenceFeatureStores.get(
                          this.featureGroupStoreMap.get(featureGroup.name()));
                  return featureStore
                      .fetchFeatures(groupedEntry.getValue(), featureGroup, requestInputs)
                      .getFeatures()
                      .entrySet()
                      .stream();
                })
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    return fetchedFeatures;
  }
}
