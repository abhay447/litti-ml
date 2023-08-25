package com.litti.ml.entities.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import com.litti.ml.entities.feature.FeatureMetadata;
import java.util.UUID;

@AutoValue
@JsonDeserialize(builder = AutoValue_ModelMetadata.Builder.class)
public abstract class ModelMetadata {

  public static Builder builder() {
    return new AutoValue_ModelMetadata.Builder();
  }

  public abstract UUID getId();

  public abstract String getName();

  public abstract String getVersion();

  public abstract String getModelLocation();

  public abstract String getModelFramework();

  public abstract ImmutableList<FeatureMetadata> getFeatures();

  public abstract ImmutableList<ModelOutputMetadata> getOutputs();

  public abstract String getDomain();

  @JsonPOJOBuilder(buildMethodName = "build", withPrefix = "")
  @AutoValue.Builder
  public abstract static class Builder {

    public abstract Builder id(UUID value);

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

    public abstract Builder domain(String value);

    public final Builder addOutput(ModelOutputMetadata output) {
      outputsBuilder().add(output);
      return this;
    }

    public abstract ModelMetadata build();
  }
}
