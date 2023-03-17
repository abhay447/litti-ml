package com.litti.ml.management.controller;

import com.litti.ml.management.entiites.FeatureEntity;
import com.litti.ml.management.service.FeatureManagementService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class FeatureController {

  private final FeatureManagementService featureManagementService;

  public FeatureController(FeatureManagementService featureManagementService) {
    this.featureManagementService = featureManagementService;
  }

  @GetMapping(value = "/features", produces = "application/json")
  public List<FeatureEntity> list() {
    return this.featureManagementService.findAll();
  }

  @PostMapping(value = "/features", produces = "application/json", consumes = "application/json")
  public FeatureEntity add(@RequestBody FeatureEntity featureEntity) {
    return this.featureManagementService.addFeature(featureEntity);
  }

  @GetMapping(value = "/features/{featureId}", produces = "application/json")
  public FeatureEntity get(@PathVariable String featureId) {
    return this.featureManagementService.findById(UUID.fromString(featureId));
  }
}
