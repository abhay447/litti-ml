package com.litti.ml.management.entiites;

import com.litti.ml.entities.artifact.ArtifactMetadata;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.nio.file.Path;
import java.util.UUID;

@Entity
public class ArtifactEntity {

  @Id private UUID id;
  private String storageType;
  private String storageLocation;

  public ArtifactEntity() {}

  public ArtifactEntity(UUID artifactId, String storageTypeVolumeMount, Path destinationFile) {
    this.id = artifactId;
    this.storageType = storageTypeVolumeMount;
    this.storageLocation = destinationFile.toAbsolutePath().toString();
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getStorageType() {
    return storageType;
  }

  public void setStorageType(String storageType) {
    this.storageType = storageType;
  }

  public String getStorageLocation() {
    return storageLocation;
  }

  public void setStorageLocation(String storageLocation) {
    this.storageLocation = storageLocation;
  }

  public ArtifactMetadata toArtifactMetadata() {
    return ArtifactMetadata.builder()
        .id(this.id)
        .storageType(this.storageType)
        .storageLocation(this.storageLocation)
        .build();
  }
}
