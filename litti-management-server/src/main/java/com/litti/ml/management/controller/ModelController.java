package com.litti.ml.management.controller;

import com.litti.ml.entities.model.ModelMetadata;
import com.litti.ml.management.dto.CreateModelRequest;
import com.litti.ml.management.entiites.ModelEntity;
import com.litti.ml.management.service.ModelManagementService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin(originPatterns = "*")
public class ModelController {

  private final ModelManagementService modelManagementService;

  public ModelController(ModelManagementService modelManagementService) {
    this.modelManagementService = modelManagementService;
  }

  @GetMapping(value = "/models", produces = "application/json")
  public List<ModelEntity> list() {
    return this.modelManagementService.findAll();
  }

  @PostMapping(value = "/models", produces = "application/json", consumes = "application/json")
  public ModelEntity add(@RequestBody CreateModelRequest createModelRequest) {
    return this.modelManagementService.addModel(createModelRequest);
  }

  @GetMapping(value = "/models/{modelId}", produces = "application/json")
  public ModelEntity get(@PathVariable String modelId) {
    return this.modelManagementService.findById(UUID.fromString(modelId));
  }

  @GetMapping(value = "/model-deployment-meta/{modelId}", produces = "application/json")
  public ModelMetadata getModelDeploymentMetadata(@PathVariable String modelId) {
    return this.modelManagementService.getModelDeploymentMetadata(UUID.fromString(modelId));
  }
}
