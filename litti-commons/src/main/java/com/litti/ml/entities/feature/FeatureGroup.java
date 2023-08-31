package com.litti.ml.entities.feature;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import java.util.UUID;

@AutoValue
@JsonDeserialize(builder = AutoValue_FeatureGroup.Builder.class)
public abstract class FeatureGroup {

  public static Builder builder() {
    return new AutoValue_FeatureGroup.Builder();
  }

  public abstract UUID getId();

  public abstract String getName();

  public abstract ImmutableList<String> getDimensions();

  @JsonPOJOBuilder(buildMethodName = "build", withPrefix = "")
  @AutoValue.Builder
  public abstract static class Builder {

    public abstract FeatureGroup.Builder id(UUID value);

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
