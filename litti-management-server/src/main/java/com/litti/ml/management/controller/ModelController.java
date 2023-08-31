package com.litti.ml.management.controller;

import com.litti.ml.entities.model.ModelMetadata;
import com.litti.ml.management.dto.CreateModelRequest;
import com.litti.ml.management.entiites.ArtifactEntity;
import com.litti.ml.management.entiites.ModelEntity;
import com.litti.ml.management.service.ArtifactStorageService;
import com.litti.ml.management.service.ModelManagementService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin(originPatterns = "*")
public class ModelController {

  private final ModelManagementService modelManagementService;

  private final ArtifactStorageService artifactStorageService;

  public ModelController(
      ModelManagementService modelManagementService,
      ArtifactStorageService artifactStorageService) {
    this.modelManagementService = modelManagementService;
    this.artifactStorageService = artifactStorageService;
  }

  @GetMapping(value = "/models", produces = "application/json")
  public List<ModelMetadata> list() {
    return this.modelManagementService.findAll().stream()
        .map(
            modelEntity -> {
              final ArtifactEntity artifactEntity =
                  this.artifactStorageService.get(modelEntity.getModelArtifactId()).get();
              return this.modelManagementService.getModelDeploymentMetadata(
                  modelEntity, artifactEntity);
            })
        .toList();
  }

  @PostMapping(value = "/models", produces = "application/json", consumes = "application/json")
  public ModelEntity add(@RequestBody CreateModelRequest createModelRequest) {
    return this.modelManagementService.addModel(createModelRequest);
  }

  @GetMapping(value = "/models/{modelId}", produces = "application/json")
  public ModelMetadata get(@PathVariable String modelId) {
    final ModelEntity modelEntity = this.modelManagementService.findById(UUID.fromString(modelId));
    final ArtifactEntity artifactEntity =
        this.artifactStorageService.get(modelEntity.getModelArtifactId()).get();
    return this.modelManagementService.getModelDeploymentMetadata(modelEntity, artifactEntity);
  }

  @PostMapping("/litti-artifacts")
  public ArtifactEntity handleFileUpload(
      @RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
    return artifactStorageService.store(file);
  }
}
