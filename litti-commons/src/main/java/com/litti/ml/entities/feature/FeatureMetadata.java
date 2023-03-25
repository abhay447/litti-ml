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

  public abstract String getName();

  public abstract String getVersion();

  public abstract String getDataType();

  @Nullable
  public abstract String getDefaultValue();

  @Nullable
  public abstract String getFeatureGroup();

  @Nullable
  public abstract Long getTtlSeconds();

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
