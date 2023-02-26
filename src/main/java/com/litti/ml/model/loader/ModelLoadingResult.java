package com.litti.ml.model.loader;

import com.litti.ml.entities.ModelMetadata;

import java.util.Collections;
import java.util.Set;

public class ModelLoadingResult {

  private final Set<ModelMetadata> modelsLoaded;
  private final Set<String> modelSourcesFailed;

  public ModelLoadingResult(Set<ModelMetadata> modelsLoaded, Set<String> modelSourcesFailed) {
    this.modelsLoaded = Collections.unmodifiableSet(modelsLoaded);
    this.modelSourcesFailed = Collections.unmodifiableSet(modelSourcesFailed);
    ;
  }

  public Set<ModelMetadata> getModelsLoaded() {
    return modelsLoaded;
  }

  public Set<String> getModelSourcesFailed() {
    return modelSourcesFailed;
  }
}
