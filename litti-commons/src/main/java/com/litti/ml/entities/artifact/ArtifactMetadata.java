package com.litti.ml.entities.artifact;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.google.auto.value.AutoValue;

import java.util.UUID;

@AutoValue
@JsonDeserialize(builder = AutoValue_ArtifactMetadata.Builder.class)
public abstract class ArtifactMetadata {
    public static ArtifactMetadata.Builder builder() {
        return new AutoValue_ArtifactMetadata.Builder();
    }

    public abstract UUID getId();

    public abstract String getStorageType();

    public abstract String getStorageLocation();

  @JsonPOJOBuilder(buildMethodName = "build", withPrefix = "")
  @AutoValue.Builder
  public abstract static class Builder {

    public abstract ArtifactMetadata.Builder id(UUID value);

    public abstract ArtifactMetadata.Builder storageType(String value);

    public abstract ArtifactMetadata.Builder storageLocation(String value);

    public abstract ArtifactMetadata build();
  }

}
