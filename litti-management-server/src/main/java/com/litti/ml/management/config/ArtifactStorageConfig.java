package com.litti.ml.management.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "artifact-storage")
public class ArtifactStorageConfig {
  private String rootLocation;

  public String getRootLocation() {
    return rootLocation;
  }

  public void setRootLocation(String rootLocation) {
    this.rootLocation = rootLocation;
  }
}
