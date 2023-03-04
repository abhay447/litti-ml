package com.litti.ml.model.entities;

import javax.annotation.processing.Generated;

@Generated("com.google.auto.value.processor.AutoValueProcessor")
final class AutoValue_ModelOutputMetadata extends ModelOutputMetadata {

  private final String name;

  private final String dataType;

  private AutoValue_ModelOutputMetadata(
      String name,
      String dataType) {
    this.name = name;
    this.dataType = dataType;
  }

  @Override
  public String name() {
    return name;
  }

  @Override
  public String dataType() {
    return dataType;
  }

  @Override
  public String toString() {
    return "ModelOutputMetadata{"
        + "name=" + name + ", "
        + "dataType=" + dataType
        + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof ModelOutputMetadata) {
      ModelOutputMetadata that = (ModelOutputMetadata) o;
      return this.name.equals(that.name())
          && this.dataType.equals(that.dataType());
    }
    return false;
  }

  @Override
  public int hashCode() {
    int h$ = 1;
    h$ *= 1000003;
    h$ ^= name.hashCode();
    h$ *= 1000003;
    h$ ^= dataType.hashCode();
    return h$;
  }

  static final class Builder extends ModelOutputMetadata.Builder {
    private String name;
    private String dataType;
    Builder() {
    }
    @Override
    public ModelOutputMetadata.Builder name(String name) {
      if (name == null) {
        throw new NullPointerException("Null name");
      }
      this.name = name;
      return this;
    }
    @Override
    public ModelOutputMetadata.Builder dataType(String dataType) {
      if (dataType == null) {
        throw new NullPointerException("Null dataType");
      }
      this.dataType = dataType;
      return this;
    }
    @Override
    public ModelOutputMetadata build() {
      if (this.name == null
          || this.dataType == null) {
        StringBuilder missing = new StringBuilder();
        if (this.name == null) {
          missing.append(" name");
        }
        if (this.dataType == null) {
          missing.append(" dataType");
        }
        throw new IllegalStateException("Missing required properties:" + missing);
      }
      return new AutoValue_ModelOutputMetadata(
          this.name,
          this.dataType);
    }
  }

}
