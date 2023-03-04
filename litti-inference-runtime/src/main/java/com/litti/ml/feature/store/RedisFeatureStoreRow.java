package com.litti.ml.feature.store;

import java.util.Map;

public class RedisFeatureStoreRow {

  private final String key;
  private final Map<String, FeatureStoreRecord> featureStoreRecords;

  public RedisFeatureStoreRow(String key, Map<String, FeatureStoreRecord> featureStoreRecords) {
    this.key = key;
    this.featureStoreRecords = featureStoreRecords;
  }
}
