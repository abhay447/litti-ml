package com.litti.ml.entities.model;

public class ModelOutputMetadata {

  private String name;

  private String dataType;

  public ModelOutputMetadata(String name, String dataType) {
    this.name = name;
    this.dataType = dataType;
  }

  public ModelOutputMetadata() {}

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDataType() {
    return dataType;
  }

  public void setDataType(String dataType) {
    this.dataType = dataType;
  }
}
