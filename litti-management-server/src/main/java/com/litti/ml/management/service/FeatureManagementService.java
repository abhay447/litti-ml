package com.litti.ml.management.service;

import com.litti.ml.management.entiites.FeatureEntity;
import com.litti.ml.management.entiites.FeatureGroupEntity;
import com.litti.ml.management.entiites.FeatureStoreEntity;
import com.litti.ml.management.repository.FeatureGroupRepository;
import com.litti.ml.management.repository.FeatureRepository;
import com.litti.ml.management.repository.FeatureStoreRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class FeatureManagementService {

  // TODO : add more null field checks before saving

  private final FeatureRepository featureRepository;
  private final FeatureGroupRepository featureGroupRepository;

  private final FeatureStoreRepository featureStoreRepository;

  public FeatureManagementService(
      FeatureRepository featureRepository,
      FeatureGroupRepository featureGroupRepository,
      FeatureStoreRepository featureStoreRepository) {
    this.featureRepository = featureRepository;
    this.featureGroupRepository = featureGroupRepository;
    this.featureStoreRepository = featureStoreRepository;
  }

  public FeatureEntity addFeature(FeatureEntity featureEntity) {
    final ExampleMatcher caseInsensitiveExampleMatcher =
        ExampleMatcher.matchingAll().withIgnoreCase();
    if (this.featureRepository
        .findOne(
            Example.of(
                new FeatureEntity(featureEntity.getName(), featureEntity.getVersion()),
                caseInsensitiveExampleMatcher))
        .isPresent()) {
      throw new RuntimeException("feature already exists with name and version");
    }

    return this.featureRepository.save(featureEntity);
  }

  public FeatureEntity findFeatureById(UUID featureId) {
    final Optional<FeatureEntity> dbFeatureEntity = this.featureRepository.findById(featureId);
    if (dbFeatureEntity.isEmpty()) {
      throw new RuntimeException("feature not found with id: " + featureId);
    }
    return dbFeatureEntity.get();
  }

  public List<FeatureEntity> findAllFeatures() {
    return this.featureRepository.findAll();
  }

  public List<FeatureGroupEntity> findAllFeatureGroups() {
    return this.featureGroupRepository.findAll();
  }

  public FeatureGroupEntity addFeatureGroup(FeatureGroupEntity featureGroupEntity) {
    final ExampleMatcher caseInsensitiveExampleMatcher =
        ExampleMatcher.matchingAll().withIgnoreCase();
    if (this.featureGroupRepository
        .findOne(
            Example.of(
                new FeatureGroupEntity(featureGroupEntity.getName()),
                caseInsensitiveExampleMatcher))
        .isPresent()) {
      throw new RuntimeException("feature already exists with name and version");
    }

    return this.featureGroupRepository.save(featureGroupEntity);
  }

  public FeatureGroupEntity findFeatureGroupById(UUID featureGroupId) {
    final Optional<FeatureGroupEntity> dbFeatureEntity =
        this.featureGroupRepository.findById(featureGroupId);
    if (dbFeatureEntity.isEmpty()) {
      throw new RuntimeException("feature group not found with id: " + featureGroupId);
    }
    return dbFeatureEntity.get();
  }

  public List<FeatureStoreEntity> findAllFeatureStores() {
    return this.featureStoreRepository.findAll();
  }

  public FeatureStoreEntity addFeatureStore(FeatureStoreEntity featureStoreEntity) {
    final ExampleMatcher caseInsensitiveExampleMatcher =
        ExampleMatcher.matchingAll().withIgnoreCase();
    if (this.featureStoreRepository
        .findOne(
            Example.of(
                new FeatureStoreEntity(featureStoreEntity.getName()),
                caseInsensitiveExampleMatcher))
        .isPresent()) {
      throw new RuntimeException("feature already exists with name and version");
    }

    return this.featureStoreRepository.save(featureStoreEntity);
  }

  public FeatureStoreEntity findFeatureStoreById(UUID featureStoreId) {
    final Optional<FeatureStoreEntity> dbFeatureStoreEntity =
        this.featureStoreRepository.findById(featureStoreId);
    if (dbFeatureStoreEntity.isEmpty()) {
      throw new RuntimeException("feature store not found with id: " + featureStoreId);
    }
    return dbFeatureStoreEntity.get();
  }
}
