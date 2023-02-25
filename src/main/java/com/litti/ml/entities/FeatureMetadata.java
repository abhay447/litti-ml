package com.litti.ml.entities;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;

@AutoValue
@JsonDeserialize(builder = AutoValue_FeatureMetadata.Builder.class)
public abstract class FeatureMetadata {

  public abstract String name();

  public abstract String version();

  public abstract String dataType();

  public abstract String defaultValue();

  public abstract ImmutableList<String> dimensions();

  public abstract boolean isFetchedFromStore();

  public static Builder builder() {
    return new AutoValue_FeatureMetadata.Builder();
  }

  @JsonPOJOBuilder(buildMethodName = "build", withPrefix = "")
  @AutoValue.Builder
  public abstract static class Builder {

    public abstract FeatureMetadata.Builder name(String value);

    public abstract FeatureMetadata.Builder version(String value);

    public abstract FeatureMetadata.Builder dataType(String value);

    public abstract FeatureMetadata.Builder defaultValue(String value);

    public abstract ImmutableList.Builder<String> dimensionsBuilder();

    public final Builder addDimension(String output) {
      dimensionsBuilder().add(output);
      return this;
    }

    public abstract FeatureMetadata.Builder isFetchedFromStore(boolean value);

    public abstract FeatureMetadata build();
  }
}
