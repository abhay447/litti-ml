package com.litti.ml.management.controller;

import com.litti.ml.entities.model.ModelMetadata;
import com.litti.ml.management.dto.CreateModelRequest;
import com.litti.ml.management.entiites.ModelEntity;
import com.litti.ml.management.service.ModelManagementService;
import java.util.List;
import java.util.UUID;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(originPatterns = "*")
public class ModelController {

  private final ModelManagementService modelManagementService;

  public ModelController(ModelManagementService modelManagementService) {
    this.modelManagementService = modelManagementService;
  }

  @GetMapping(value = "/models", produces = "application/json")
  public List<ModelMetadata> list() {
    return this.modelManagementService.findAll().stream()
        .map(modelEntity -> this.modelManagementService.getModelDeploymentMetadata(modelEntity))
        .toList();
  }

  @PostMapping(value = "/models", produces = "application/json", consumes = "application/json")
  public ModelEntity add(@RequestBody CreateModelRequest createModelRequest) {
    return this.modelManagementService.addModel(createModelRequest);
  }

  @GetMapping(value = "/models/{modelId}", produces = "application/json")
  public ModelMetadata get(@PathVariable String modelId) {
    return this.modelManagementService.getModelDeploymentMetadata(
        this.modelManagementService.findById(UUID.fromString(modelId)));
  }
}
