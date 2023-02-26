package com.litti.ml.feature;

import com.litti.ml.feature.entities.FeatureGroup;
import com.litti.ml.feature.store.FeatureStore;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class FeatureFetchRouter {
  private static final Logger logger = LogManager.getLogger(FeatureFetchRouter.class);

  private final Map<String, FeatureGroup> inferenceFeatureGroups;
  private final Map<String, FeatureStore> inferenceFeatureStores;
  private final Map<String, String> featureGroupStoreMap;

  public FeatureFetchRouter() {
    this.inferenceFeatureGroups = new HashMap<>();
    this.inferenceFeatureStores = new HashMap<>();
    this.featureGroupStoreMap = new HashMap<>();
  }

  public void registerFeatureGroup(FeatureGroup featureGroup, FeatureStore featureStore) {
    this.inferenceFeatureGroups.put(featureGroup.name(), featureGroup);
    this.inferenceFeatureStores.put(featureStore.name(), featureStore);
    this.featureGroupStoreMap.put(featureGroup.name(), featureStore.name());
  }
}
