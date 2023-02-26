package com.litti.ml.model.entities;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.google.auto.value.AutoValue;

@AutoValue
@JsonDeserialize(builder = AutoValue_ModelOutputMetadata.Builder.class)
public abstract class ModelOutputMetadata {
  public static ModelOutputMetadata.Builder builder() {
    return new AutoValue_ModelOutputMetadata.Builder();
  }

  public abstract String name();

  public abstract String dataType();

  @JsonPOJOBuilder(buildMethodName = "build", withPrefix = "")
  @AutoValue.Builder
  public abstract static class Builder {
    public abstract ModelOutputMetadata.Builder name(String value);

    public abstract ModelOutputMetadata.Builder dataType(String value);

    public abstract ModelOutputMetadata build();
  }
}
