package com.litti.ml.model.loader;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.google.common.io.Resources;
import com.litti.ml.entities.ModelMetadata;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.AbstractMap.SimpleEntry;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class StaticResourcesModelLoader implements ModelLoader {

  private static final Logger logger = LogManager.getLogger(StaticResourcesModelLoader.class);

  private static final List<String> modelFileNames =
      List.of("sample-model-meta.json", "dream11-model-meta.json");
  final ObjectMapper objectMapper = JsonMapper.builder().addModule(new GuavaModule()).build();

  @Override
  public ModelLoadingResult loadAllModels() {
    Map<Boolean, Set<SimpleEntry<String, Optional<ModelMetadata>>>> modelLoadingMap =
        modelFileNames.stream()
            .map(
                modelFile -> {
                  try {
                    final String modelJson =
                        Files.readString(Paths.get(Resources.getResource(modelFile).toURI()));
                    final ModelMetadata modelMetadata =
                        objectMapper.readValue(modelJson, ModelMetadata.class);
                    logger.info("successfully loaded model from source {}", modelFile);
                    return new SimpleEntry<>(modelFile, Optional.of(modelMetadata));
                  } catch (Exception e) {
                    logger.error("failed to load model from source {} ", modelFile, e);
                    return new SimpleEntry<String, Optional<ModelMetadata>>(
                        modelFile, Optional.empty());
                  }
                })
            .collect(
                Collectors.partitioningBy(
                    entry -> entry.getValue().isPresent(), Collectors.toSet()));
    final Set<ModelMetadata> successfullyLoadedModels =
        modelLoadingMap.get(true).stream()
            .map(entry -> entry.getValue().get())
            .collect(Collectors.toSet());
    final Set<String> failedModelLoadSources =
        modelLoadingMap.get(false).stream().map(SimpleEntry::getKey).collect(Collectors.toSet());
    return new ModelLoadingResult(successfullyLoadedModels, failedModelLoadSources);
  }
}
