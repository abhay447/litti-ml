package com.litti.ml.feature.store;

import com.litti.ml.entities.feature.FeatureMetadata;

public class FeatureStoreRecord {

  private final String featureName;

  private final String featureVersion;

  private final Object rawValue;

  private final Long validFrom;

  private final Long validTo;

  public FeatureStoreRecord(
      String featureName, String featureVersion, Object rawValue, long validFrom, long validTo) {
    this.featureName = featureName;
    this.featureVersion = featureVersion;
    this.rawValue = rawValue;
    this.validFrom = validFrom;
    this.validTo = validTo;
  }

  public static FeatureStoreRecord fromValueAndFeatureMetadata(
      Object value, FeatureMetadata featureMetadata) {
    return new FeatureStoreRecord(
        featureMetadata.name(),
        featureMetadata.version(),
        value,
        System.currentTimeMillis() / 1000,
        System.currentTimeMillis() / 1000 + 86400 // TODO: track ttl in feature meta
        );
  }

  public String getFeatureName() {
    return featureName;
  }

  public String getFeatureVersion() {
    return featureVersion;
  }

  public Object getRawValue() {
    return rawValue;
  }

  public Long getValidFrom() {
    return validFrom;
  }

  public Long getValidTo() {
    return validTo;
  }
}
