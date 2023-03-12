package com.litti.ml.management.entiites;

import com.google.common.collect.ImmutableList;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.litti.ml.entities.feature.FeatureMetadata;
import com.litti.ml.entities.model.ModelMetadata;
import com.litti.ml.entities.model.ModelOutputMetadata;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.util.List;
import java.util.UUID;

@Entity
public class ModelEntity {

  @Id @GeneratedValue private UUID id;
  private String name;
  private String version;
  private String domain;
  private String modelLocation;
  private String modelFramework;
  private String outputs;

  public ModelEntity() {}

  public ModelEntity(String name, String version) {
    this.name = name;
    this.version = version;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public String getDomain() {
    return domain;
  }

  public void setDomain(String domain) {
    this.domain = domain;
  }

  public String getModelLocation() {
    return modelLocation;
  }

  public void setModelLocation(String modelLocation) {
    this.modelLocation = modelLocation;
  }

  public String getModelFramework() {
    return modelFramework;
  }

  public void setModelFramework(String modelFramework) {
    this.modelFramework = modelFramework;
  }

  public String getOutputs() {
    return outputs;
  }

  public void setOutputs(String outputs) {
    this.outputs = outputs;
  }

  public ModelMetadata toModelDeploymentMetadata(List<FeatureMetadata> features) {
    final Gson gson = new Gson();
    final List<ModelOutputMetadata> outputs =
        gson.fromJson(this.outputs, new TypeToken<List<ModelOutputMetadata>>() {}.getType());
    return ModelMetadata.builder()
        .name(this.name)
        .version(this.version)
        .modelLocation(this.modelLocation)
        .modelFramework(this.modelFramework)
        .features(ImmutableList.copyOf(features))
        .outputs(ImmutableList.copyOf(outputs))
        .build();
  }
}
