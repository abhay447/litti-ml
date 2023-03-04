package com.litti.ml.feature.entities;

import com.google.common.collect.ImmutableList;
import javax.annotation.processing.Generated;

@Generated("com.google.auto.value.processor.AutoValueProcessor")
final class AutoValue_FeatureGroup extends FeatureGroup {

  private final String name;

  private final ImmutableList<String> dimensions;

  private AutoValue_FeatureGroup(
      String name,
      ImmutableList<String> dimensions) {
    this.name = name;
    this.dimensions = dimensions;
  }

  @Override
  public String name() {
    return name;
  }

  @Override
  public ImmutableList<String> dimensions() {
    return dimensions;
  }

  @Override
  public String toString() {
    return "FeatureGroup{"
        + "name=" + name + ", "
        + "dimensions=" + dimensions
        + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof FeatureGroup) {
      FeatureGroup that = (FeatureGroup) o;
      return this.name.equals(that.name())
          && this.dimensions.equals(that.dimensions());
    }
    return false;
  }

  @Override
  public int hashCode() {
    int h$ = 1;
    h$ *= 1000003;
    h$ ^= name.hashCode();
    h$ *= 1000003;
    h$ ^= dimensions.hashCode();
    return h$;
  }

  static final class Builder extends FeatureGroup.Builder {
    private String name;
    private ImmutableList.Builder<String> dimensionsBuilder$;
    private ImmutableList<String> dimensions;
    Builder() {
    }
    @Override
    public FeatureGroup.Builder name(String name) {
      if (name == null) {
        throw new NullPointerException("Null name");
      }
      this.name = name;
      return this;
    }
    @Override
    public FeatureGroup.Builder dimensions(ImmutableList<String> dimensions) {
      if (dimensions == null) {
        throw new NullPointerException("Null dimensions");
      }
      if (dimensionsBuilder$ != null) {
        throw new IllegalStateException("Cannot set dimensions after calling dimensionsBuilder()");
      }
      this.dimensions = dimensions;
      return this;
    }
    @Override
    public ImmutableList.Builder<String> dimensionsBuilder() {
      if (dimensionsBuilder$ == null) {
        if (dimensions == null) {
          dimensionsBuilder$ = ImmutableList.builder();
        } else {
          dimensionsBuilder$ = ImmutableList.builder();
          dimensionsBuilder$.addAll(dimensions);
          dimensions = null;
        }
      }
      return dimensionsBuilder$;
    }
    @Override
    public FeatureGroup build() {
      if (dimensionsBuilder$ != null) {
        this.dimensions = dimensionsBuilder$.build();
      } else if (this.dimensions == null) {
        this.dimensions = ImmutableList.of();
      }
      if (this.name == null) {
        String missing = " name";
        throw new IllegalStateException("Missing required properties:" + missing);
      }
      return new AutoValue_FeatureGroup(
          this.name,
          this.dimensions);
    }
  }

}
