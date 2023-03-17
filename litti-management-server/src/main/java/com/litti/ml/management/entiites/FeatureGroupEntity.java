package com.litti.ml.management.entiites;

import com.google.common.collect.ImmutableList;
import com.litti.ml.entities.feature.FeatureGroup;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.util.UUID;

@Entity
public class FeatureGroupEntity {

  @Id @GeneratedValue private UUID id;
  private String name;
  private String dimensions;
  private UUID featureStoreId;

  public FeatureGroupEntity() {}

  public FeatureGroupEntity(String name) {
    this.name = name;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDimensions() {
    return dimensions;
  }

  public void setDimensions(String dimensions) {
    this.dimensions = dimensions;
  }

  public UUID getFeatureStoreId() {
    return featureStoreId;
  }

  public void setFeatureStoreId(UUID featureStoreId) {
    this.featureStoreId = featureStoreId;
  }

  public FeatureGroup toFeatureGroupMetadata() {
    return FeatureGroup.builder()
        .name(this.name)
        .dimensions(ImmutableList.copyOf(this.dimensions.split(",")))
        .build();
  }
}
