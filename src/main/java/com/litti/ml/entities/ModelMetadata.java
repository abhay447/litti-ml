package com.litti.ml.entities;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;

@AutoValue
@JsonDeserialize(builder = AutoValue_ModelMetadata.Builder.class)
public abstract class ModelMetadata {

  public static Builder builder() {
    return new AutoValue_ModelMetadata.Builder();
  }

  public abstract String name();

  public abstract String version();

  public abstract String modelLocation();

  public abstract String modelFramework();

  public abstract ImmutableList<FeatureMetadata> features();

  public abstract ImmutableList<ModelOutputMetadata> outputs();

  @JsonPOJOBuilder(buildMethodName = "build", withPrefix = "")
  @AutoValue.Builder
  public abstract static class Builder {

    public abstract Builder name(String value);

    public abstract Builder version(String value);

    public abstract Builder modelLocation(String value);

    public abstract Builder modelFramework(String value);

    // convenience method for json parsing
    public abstract Builder features(ImmutableList<FeatureMetadata> value);

    public abstract ImmutableList.Builder<FeatureMetadata> featuresBuilder();

    public final Builder addFeature(FeatureMetadata feature) {
      featuresBuilder().add(feature);
      return this;
    }

    // convenience method for json parsing
    public abstract Builder outputs(ImmutableList<ModelOutputMetadata> value);

    public abstract ImmutableList.Builder<ModelOutputMetadata> outputsBuilder();

    public final Builder addOutput(ModelOutputMetadata output) {
      outputsBuilder().add(output);
      return this;
    }

    public abstract ModelMetadata build();
  }
}
