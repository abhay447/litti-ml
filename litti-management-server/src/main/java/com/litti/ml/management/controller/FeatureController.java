package com.litti.ml.management.controller;

import com.litti.ml.management.dto.FeatureGroupStoreDeleteRequest;
import com.litti.ml.management.dto.FeatureGroupStoresSwapRequest;
import com.litti.ml.management.entiites.FeatureEntity;
import com.litti.ml.management.entiites.FeatureGroupEntity;
import com.litti.ml.management.entiites.FeatureGroupStoreLinkEntity;
import com.litti.ml.management.entiites.FeatureStoreEntity;
import com.litti.ml.management.service.FeatureManagementService;
import java.util.List;
import java.util.UUID;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(originPatterns = "*")
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

  @PostMapping(
      value = "/feature-group-stores",
      produces = "application/json",
      consumes = "application/json")
  public FeatureGroupStoreLinkEntity addFeatureGroupStore(
      @RequestBody FeatureGroupStoreLinkEntity featureGroupStoreLinkEntity) {
    return this.featureManagementService.addFeatureGroupStoreLink(featureGroupStoreLinkEntity);
  }

  @PostMapping(
      value = "/feature-group-stores/swap",
      produces = "application/json",
      consumes = "application/json")
  public List<FeatureGroupStoreLinkEntity> swapFeatureGroupStore(
      @RequestBody FeatureGroupStoresSwapRequest featureGroupStoresSwapRequest) {
    return this.featureManagementService.swapFeatureGroupLinks(
        UUID.fromString(featureGroupStoresSwapRequest.getFeatureGroupId()));
  }

  @DeleteMapping(
      value = "/feature-group-stores",
      produces = "application/json",
      consumes = "application/json")
  public FeatureGroupStoreLinkEntity swapFeatureGroupStore(
      @RequestBody FeatureGroupStoreDeleteRequest featureGroupStoreDeleteRequest) {
    return this.featureManagementService.deleteFeatureGroupStoreLink(
        UUID.fromString(featureGroupStoreDeleteRequest.getFeatureGroupStoreLinkId()));
  }

  @GetMapping(
      value = "/feature-group-stores/{featureGroupStoreLinkId}",
      produces = "application/json")
  public FeatureGroupStoreLinkEntity getFeatureGroupStoreLink(
      @PathVariable String featureGroupStoreLinkId) {
    return this.featureManagementService.findFeatureGroupStoreLink(
        UUID.fromString(featureGroupStoreLinkId));
  }

  @GetMapping(value = "/feature-group-stores", produces = "application/json")
  public List<FeatureGroupStoreLinkEntity> getFeatureGroupStoreLinks() {
    return this.featureManagementService.findAllFeatureGroupStoreLinks();
  }

  @GetMapping(value = "/feature-group-stores-find/{featureGroupId}", produces = "application/json")
  public List<FeatureGroupStoreLinkEntity> findFeatureGroupStoreLinks(
      @PathVariable String featureGroupId) {
    return this.featureManagementService.findFeatureGroupStoreLinksByGroupId(
        UUID.fromString(featureGroupId));
  }
}
