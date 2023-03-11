package com.litti.ml.management.controller;

import com.litti.ml.management.entiites.ModelEntity;
import com.litti.ml.management.repository.ModelRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class ModelController {

  private final ModelRepository modelRepository;

  public ModelController(ModelRepository modelRepository) {
    this.modelRepository = modelRepository;
  }

  @GetMapping(value = "/models", produces = "application/json")
  public List<ModelEntity> list() {
    return this.modelRepository.findAll();
  }

  @PostMapping(value = "/models", consumes = "application/json")
  public String add(@RequestBody ModelEntity modelEntity) {
    this.modelRepository.save(modelEntity);
    return "Model Saved!";
  }

  @GetMapping(value = "/models/{modelId}", produces = "application/json")
  public ModelEntity get(@PathVariable String modelId) {
    return this.modelRepository.findById(UUID.fromString(modelId)).get();
  }

  @DeleteMapping(value = "/models/{modelId}", produces = "application/json")
  public String delete(@PathVariable String modelId) {
    this.modelRepository.deleteById(UUID.fromString(modelId));
    return "Model Deleted!";
  }
}
