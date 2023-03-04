package com.litti.ml.entities.feature;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;

@AutoValue
@JsonDeserialize(builder = AutoValue_FeatureGroup.Builder.class)
public abstract class FeatureGroup {

  public static Builder builder() {
    return new AutoValue_FeatureGroup.Builder();
  }

  public abstract String name();

  public abstract ImmutableList<String> dimensions();

  @JsonPOJOBuilder(buildMethodName = "build", withPrefix = "")
  @AutoValue.Builder
  public abstract static class Builder {

    public abstract FeatureGroup.Builder name(String value);

    public abstract Builder dimensions(ImmutableList<String> value);

    public abstract ImmutableList.Builder<String> dimensionsBuilder();

    public final Builder addDimension(String output) {
      dimensionsBuilder().add(output);
      return this;
    }

    public abstract FeatureGroup build();
  }
}
