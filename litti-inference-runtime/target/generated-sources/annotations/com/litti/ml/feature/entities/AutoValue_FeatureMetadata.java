package com.litti.ml.feature.entities;

import autovalue.shaded.org.checkerframework.checker.nullness.qual.Nullable;
import javax.annotation.processing.Generated;

@Generated("com.google.auto.value.processor.AutoValueProcessor")
final class AutoValue_FeatureMetadata extends FeatureMetadata {

  private final String name;

  private final String version;

  private final String dataType;

  private final String defaultValue;

  private final @Nullable String featureGroup;

  private AutoValue_FeatureMetadata(
      String name,
      String version,
      String dataType,
      String defaultValue,
      @Nullable String featureGroup) {
    this.name = name;
    this.version = version;
    this.dataType = dataType;
    this.defaultValue = defaultValue;
    this.featureGroup = featureGroup;
  }

  @Override
  public String name() {
    return name;
  }

  @Override
  public String version() {
    return version;
  }

  @Override
  public String dataType() {
    return dataType;
  }

  @Override
  public String defaultValue() {
    return defaultValue;
  }

  @Override
  public @Nullable String featureGroup() {
    return featureGroup;
  }

  @Override
  public String toString() {
    return "FeatureMetadata{"
        + "name=" + name + ", "
        + "version=" + version + ", "
        + "dataType=" + dataType + ", "
        + "defaultValue=" + defaultValue + ", "
        + "featureGroup=" + featureGroup
        + "}";
  }

  @Override
  public boolean equals(@Nullable Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof FeatureMetadata) {
      FeatureMetadata that = (FeatureMetadata) o;
      return this.name.equals(that.name())
          && this.version.equals(that.version())
          && this.dataType.equals(that.dataType())
          && this.defaultValue.equals(that.defaultValue())
          && (this.featureGroup == null ? that.featureGroup() == null : this.featureGroup.equals(that.featureGroup()));
    }
    return false;
  }

  @Override
  public int hashCode() {
    int h$ = 1;
    h$ *= 1000003;
    h$ ^= name.hashCode();
    h$ *= 1000003;
    h$ ^= version.hashCode();
    h$ *= 1000003;
    h$ ^= dataType.hashCode();
    h$ *= 1000003;
    h$ ^= defaultValue.hashCode();
    h$ *= 1000003;
    h$ ^= (featureGroup == null) ? 0 : featureGroup.hashCode();
    return h$;
  }

  static final class Builder extends FeatureMetadata.Builder {
    private String name;
    private String version;
    private String dataType;
    private String defaultValue;
    private @Nullable String featureGroup;
    Builder() {
    }
    @Override
    public FeatureMetadata.Builder name(String name) {
      if (name == null) {
        throw new NullPointerException("Null name");
      }
      this.name = name;
      return this;
    }
    @Override
    public FeatureMetadata.Builder version(String version) {
      if (version == null) {
        throw new NullPointerException("Null version");
      }
      this.version = version;
      return this;
    }
    @Override
    public FeatureMetadata.Builder dataType(String dataType) {
      if (dataType == null) {
        throw new NullPointerException("Null dataType");
      }
      this.dataType = dataType;
      return this;
    }
    @Override
    public FeatureMetadata.Builder defaultValue(String defaultValue) {
      if (defaultValue == null) {
        throw new NullPointerException("Null defaultValue");
      }
      this.defaultValue = defaultValue;
      return this;
    }
    @Override
    public FeatureMetadata.Builder featureGroup(@Nullable String featureGroup) {
      this.featureGroup = featureGroup;
      return this;
    }
    @Override
    public FeatureMetadata build() {
      if (this.name == null
          || this.version == null
          || this.dataType == null
          || this.defaultValue == null) {
        StringBuilder missing = new StringBuilder();
        if (this.name == null) {
          missing.append(" name");
        }
        if (this.version == null) {
          missing.append(" version");
        }
        if (this.dataType == null) {
          missing.append(" dataType");
        }
        if (this.defaultValue == null) {
          missing.append(" defaultValue");
        }
        throw new IllegalStateException("Missing required properties:" + missing);
      }
      return new AutoValue_FeatureMetadata(
          this.name,
          this.version,
          this.dataType,
          this.defaultValue,
          this.featureGroup);
    }
  }

}
