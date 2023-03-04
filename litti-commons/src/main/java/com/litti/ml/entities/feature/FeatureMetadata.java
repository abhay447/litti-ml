package com.litti.ml.entities.feature;

import autovalue.shaded.org.checkerframework.checker.nullness.qual.Nullable;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.google.auto.value.AutoValue;

@AutoValue
@JsonDeserialize(builder = AutoValue_FeatureMetadata.Builder.class)
public abstract class FeatureMetadata {

  public static Builder builder() {
    return new AutoValue_FeatureMetadata.Builder();
  }

  public abstract String name();

  public abstract String version();

  public abstract String dataType();

  @Nullable
  public abstract String defaultValue();

  @Nullable
  public abstract String featureGroup();

  @Nullable
  public abstract Long ttlSeconds();

  @JsonPOJOBuilder(buildMethodName = "build", withPrefix = "")
  @AutoValue.Builder
  public abstract static class Builder {

    public abstract FeatureMetadata.Builder name(String value);

    public abstract FeatureMetadata.Builder version(String value);

    public abstract FeatureMetadata.Builder dataType(String value);

    public abstract FeatureMetadata.Builder defaultValue(@Nullable String value);

    public abstract FeatureMetadata.Builder featureGroup(@Nullable String value);

    public abstract FeatureMetadata build();

    public abstract Builder ttlSeconds(@Nullable Long value);
  }
}
