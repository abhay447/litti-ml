package com.litti.ml.management.entiites;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.util.UUID;

@Entity
public class FeatureStoreEntity {

  @Id @GeneratedValue private UUID id;
  private String name;
  private String storageEngine;

  public FeatureStoreEntity() {}

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

  public String getStorageEngine() {
    return storageEngine;
  }

  public void setStorageEngine(String storageEngine) {
    this.storageEngine = storageEngine;
  }
}
