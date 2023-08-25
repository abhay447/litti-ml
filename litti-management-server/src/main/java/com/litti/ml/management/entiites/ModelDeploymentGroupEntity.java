package com.litti.ml.management.entiites;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.util.UUID;

@Entity
public class ModelDeploymentGroupEntity {

  @Id @GeneratedValue private UUID id;
  private String name;

  public ModelDeploymentGroupEntity() {}

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
}
