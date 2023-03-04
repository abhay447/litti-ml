package com.litti.ml.model.entities;

import com.google.common.collect.ImmutableList;
import com.litti.ml.feature.entities.FeatureMetadata;
import javax.annotation.processing.Generated;

@Generated("com.google.auto.value.processor.AutoValueProcessor")
final class AutoValue_ModelMetadata extends ModelMetadata {

  private final String name;

  private final String version;

  private final String modelLocation;

  private final String modelFramework;

  private final ImmutableList<FeatureMetadata> features;

  private final ImmutableList<ModelOutputMetadata> outputs;

  private AutoValue_ModelMetadata(
      String name,
      String version,
      String modelLocation,
      String modelFramework,
      ImmutableList<FeatureMetadata> features,
      ImmutableList<ModelOutputMetadata> outputs) {
    this.name = name;
    this.version = version;
    this.modelLocation = modelLocation;
    this.modelFramework = modelFramework;
    this.features = features;
    this.outputs = outputs;
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
  public String modelLocation() {
    return modelLocation;
  }

  @Override
  public String modelFramework() {
    return modelFramework;
  }

  @Override
  public ImmutableList<FeatureMetadata> features() {
    return features;
  }

  @Override
  public ImmutableList<ModelOutputMetadata> outputs() {
    return outputs;
  }

  @Override
  public String toString() {
    return "ModelMetadata{"
        + "name=" + name + ", "
        + "version=" + version + ", "
        + "modelLocation=" + modelLocation + ", "
        + "modelFramework=" + modelFramework + ", "
        + "features=" + features + ", "
        + "outputs=" + outputs
        + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof ModelMetadata) {
      ModelMetadata that = (ModelMetadata) o;
      return this.name.equals(that.name())
          && this.version.equals(that.version())
          && this.modelLocation.equals(that.modelLocation())
          && this.modelFramework.equals(that.modelFramework())
          && this.features.equals(that.features())
          && this.outputs.equals(that.outputs());
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
    h$ ^= modelLocation.hashCode();
    h$ *= 1000003;
    h$ ^= modelFramework.hashCode();
    h$ *= 1000003;
    h$ ^= features.hashCode();
    h$ *= 1000003;
    h$ ^= outputs.hashCode();
    return h$;
  }

  static final class Builder extends ModelMetadata.Builder {
    private String name;
    private String version;
    private String modelLocation;
    private String modelFramework;
    private ImmutableList.Builder<FeatureMetadata> featuresBuilder$;
    private ImmutableList<FeatureMetadata> features;
    private ImmutableList.Builder<ModelOutputMetadata> outputsBuilder$;
    private ImmutableList<ModelOutputMetadata> outputs;
    Builder() {
    }
    @Override
    public ModelMetadata.Builder name(String name) {
      if (name == null) {
        throw new NullPointerException("Null name");
      }
      this.name = name;
      return this;
    }
    @Override
    public ModelMetadata.Builder version(String version) {
      if (version == null) {
        throw new NullPointerException("Null version");
      }
      this.version = version;
      return this;
    }
    @Override
    public ModelMetadata.Builder modelLocation(String modelLocation) {
      if (modelLocation == null) {
        throw new NullPointerException("Null modelLocation");
      }
      this.modelLocation = modelLocation;
      return this;
    }
    @Override
    public ModelMetadata.Builder modelFramework(String modelFramework) {
      if (modelFramework == null) {
        throw new NullPointerException("Null modelFramework");
      }
      this.modelFramework = modelFramework;
      return this;
    }
    @Override
    public ModelMetadata.Builder features(ImmutableList<FeatureMetadata> features) {
      if (features == null) {
        throw new NullPointerException("Null features");
      }
      if (featuresBuilder$ != null) {
        throw new IllegalStateException("Cannot set features after calling featuresBuilder()");
      }
      this.features = features;
      return this;
    }
    @Override
    public ImmutableList.Builder<FeatureMetadata> featuresBuilder() {
      if (featuresBuilder$ == null) {
        if (features == null) {
          featuresBuilder$ = ImmutableList.builder();
        } else {
          featuresBuilder$ = ImmutableList.builder();
          featuresBuilder$.addAll(features);
          features = null;
        }
      }
      return featuresBuilder$;
    }
    @Override
    public ModelMetadata.Builder outputs(ImmutableList<ModelOutputMetadata> outputs) {
      if (outputs == null) {
        throw new NullPointerException("Null outputs");
      }
      if (outputsBuilder$ != null) {
        throw new IllegalStateException("Cannot set outputs after calling outputsBuilder()");
      }
      this.outputs = outputs;
      return this;
    }
    @Override
    public ImmutableList.Builder<ModelOutputMetadata> outputsBuilder() {
      if (outputsBuilder$ == null) {
        if (outputs == null) {
          outputsBuilder$ = ImmutableList.builder();
        } else {
          outputsBuilder$ = ImmutableList.builder();
          outputsBuilder$.addAll(outputs);
          outputs = null;
        }
      }
      return outputsBuilder$;
    }
    @Override
    public ModelMetadata build() {
      if (featuresBuilder$ != null) {
        this.features = featuresBuilder$.build();
      } else if (this.features == null) {
        this.features = ImmutableList.of();
      }
      if (outputsBuilder$ != null) {
        this.outputs = outputsBuilder$.build();
      } else if (this.outputs == null) {
        this.outputs = ImmutableList.of();
      }
      if (this.name == null
          || this.version == null
          || this.modelLocation == null
          || this.modelFramework == null) {
        StringBuilder missing = new StringBuilder();
        if (this.name == null) {
          missing.append(" name");
        }
        if (this.version == null) {
          missing.append(" version");
        }
        if (this.modelLocation == null) {
          missing.append(" modelLocation");
        }
        if (this.modelFramework == null) {
          missing.append(" modelFramework");
        }
        throw new IllegalStateException("Missing required properties:" + missing);
      }
      return new AutoValue_ModelMetadata(
          this.name,
          this.version,
          this.modelLocation,
          this.modelFramework,
          this.features,
          this.outputs);
    }
  }

}
