package com.litti.ml.management.controller;

import com.litti.ml.management.entiites.FeatureEntity;
import com.litti.ml.management.entiites.FeatureGroupEntity;
import com.litti.ml.management.entiites.FeatureStoreEntity;
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
  public List<FeatureEntity> listFeatures() {
    return this.featureManagementService.findAllFeatures();
  }

  @PostMapping(value = "/features", produces = "application/json", consumes = "application/json")
  public FeatureEntity addFeature(@RequestBody FeatureEntity featureEntity) {
    return this.featureManagementService.addFeature(featureEntity);
  }

  @GetMapping(value = "/features/{featureId}", produces = "application/json")
  public FeatureEntity getFeature(@PathVariable String featureId) {
    return this.featureManagementService.findFeatureById(UUID.fromString(featureId));
  }

  @GetMapping(value = "/feature-groups", produces = "application/json")
  public List<FeatureGroupEntity> listFeatureGroups() {
    return this.featureManagementService.findAllFeatureGroups();
  }

  @PostMapping(
      value = "/feature-groups",
      produces = "application/json",
      consumes = "application/json")
  public FeatureGroupEntity addFeatureGroup(@RequestBody FeatureGroupEntity featureGroupEntity) {
    return this.featureManagementService.addFeatureGroup(featureGroupEntity);
  }

  @GetMapping(value = "/feature-groups/{featureGroupId}", produces = "application/json")
  public FeatureGroupEntity getFeatureGroup(@PathVariable String featureGroupId) {
    return this.featureManagementService.findFeatureGroupById(UUID.fromString(featureGroupId));
  }

  @GetMapping(value = "/feature-stores", produces = "application/json")
  public List<FeatureStoreEntity> listFeatureStores() {
    return this.featureManagementService.findAllFeatureStores();
  }

  @PostMapping(
      value = "/feature-stores",
      produces = "application/json",
      consumes = "application/json")
  public FeatureStoreEntity addFeatureStore(@RequestBody FeatureStoreEntity featureStoreEntity) {
    return this.featureManagementService.addFeatureStore(featureStoreEntity);
  }

  @GetMapping(value = "/feature-stores/{featureStoreId}", produces = "application/json")
  public FeatureStoreEntity getFeatureStore(@PathVariable String featureStoreId) {
    return this.featureManagementService.findFeatureStoreById(UUID.fromString(featureStoreId));
  }
}
