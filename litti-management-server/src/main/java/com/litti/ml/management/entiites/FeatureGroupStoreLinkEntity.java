package com.litti.ml.management.entiites;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.util.Set;
import java.util.UUID;

@Entity
public class FeatureGroupStoreLinkEntity {

  public static final String FEATURE_GROUP_PRIMARY_STORE = "PRIMARY";

  public static final String FEATURE_GROUP_WRITE_REPLICA_STORE = "WRITE_REPLICA";

  private static final Set<String> validLinkModes =
      Set.of(FEATURE_GROUP_PRIMARY_STORE, FEATURE_GROUP_WRITE_REPLICA_STORE);

  @Id @GeneratedValue private UUID id;

  private UUID featureStoreId;

  private UUID featureGroupId;

  private String mode;

  public FeatureGroupStoreLinkEntity() {}

  public FeatureGroupStoreLinkEntity(UUID featureGroupId, UUID featureStoreId) {
    this.featureStoreId = featureStoreId;
    this.featureGroupId = featureGroupId;
  }

  public static FeatureGroupStoreLinkEntity fromFeatureGroupId(UUID featureGroupId) {
    FeatureGroupStoreLinkEntity featureGroupStoreLinkEntity = new FeatureGroupStoreLinkEntity();
    featureGroupStoreLinkEntity.setFeatureGroupId(featureGroupId);
    return featureGroupStoreLinkEntity;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public UUID getFeatureStoreId() {
    return featureStoreId;
  }

  public void setFeatureStoreId(UUID featureStoreId) {
    if (null == featureStoreId) {
      throw new RuntimeException(("featureStoreId cannot be null"));
    }
    this.featureStoreId = featureStoreId;
  }

  public UUID getFeatureGroupId() {
    return featureGroupId;
  }

  public void setFeatureGroupId(UUID featureGroupId) {
    if (null == featureGroupId) {
      throw new RuntimeException(("featureStoreId cannot be null"));
    }
    this.featureGroupId = featureGroupId;
  }

  public String getMode() {
    return mode;
  }

  public void setMode(String mode) {
    if (null == mode) {
      throw new RuntimeException(("feature group store link mode cannot be null"));
    } else if (!validLinkModes.contains(mode)) {
      throw new RuntimeException(
          "invalid feature group store link mode: " + mode + ", valid choices" + validLinkModes);
    }
    this.mode = mode;
  }

  @Override
  public String toString() {
    return "FeatureGroupStoreLinkEntity{"
        + "id="
        + id
        + ", featureStoreId="
        + featureStoreId
        + ", featureGroupId="
        + featureGroupId
        + ", mode='"
        + mode
        + '\''
        + '}';
  }
}
