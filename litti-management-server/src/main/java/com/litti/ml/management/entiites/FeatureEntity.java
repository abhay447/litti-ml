package com.litti.ml.management.entiites;

import com.litti.ml.entities.feature.FeatureMetadata;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.util.UUID;

@Entity
public class FeatureEntity {

  @Id @GeneratedValue private UUID id;
  private String name;
  private String version;
  private String dataType;
  private String defaultValue;
  private String featureGroupName;

  public FeatureEntity() {}

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

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public String getDataType() {
    return dataType;
  }

  public void setDataType(String dataType) {
    this.dataType = dataType;
  }

  public String getDefaultValue() {
    return defaultValue;
  }

  public void setDefaultValue(String defaultValue) {
    this.defaultValue = defaultValue;
  }

  public String getFeatureGroupName() {
    return featureGroupName;
  }

  public void setFeatureGroupName(String featureGroupName) {
    this.featureGroupName = featureGroupName;
  }

  public FeatureMetadata toFeatureDeploymentMetadata() {
    return FeatureMetadata.builder()
        .name(this.name)
        .version(this.version)
        .dataType(this.dataType)
        .defaultValue(this.defaultValue)
        .featureGroup(this.featureGroupName)
        .build();
  }
}
