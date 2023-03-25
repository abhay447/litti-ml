package com.litti.ml.management.service;

import com.litti.ml.management.entiites.FeatureEntity;
import com.litti.ml.management.entiites.FeatureGroupEntity;
import com.litti.ml.management.entiites.FeatureGroupStoreLinkEntity;
import com.litti.ml.management.entiites.FeatureStoreEntity;
import com.litti.ml.management.repository.FeatureGroupRepository;
import com.litti.ml.management.repository.FeatureGroupStoreLinkRepository;
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

  private final FeatureGroupStoreLinkRepository featureGroupStoreLinkRepository;

  public FeatureManagementService(
      FeatureRepository featureRepository,
      FeatureGroupRepository featureGroupRepository,
      FeatureStoreRepository featureStoreRepository,
      FeatureGroupStoreLinkRepository featureGroupStoreLinkRepository) {
    this.featureRepository = featureRepository;
    this.featureGroupRepository = featureGroupRepository;
    this.featureStoreRepository = featureStoreRepository;
    this.featureGroupStoreLinkRepository = featureGroupStoreLinkRepository;
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

    if (featureEntity.getFeatureGroupId() != null
        && this.featureGroupRepository.findById(featureEntity.getFeatureGroupId()).isEmpty()) {
      throw new RuntimeException(
          "feature group not found with id: " + featureEntity.getFeatureGroupId());
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

  public FeatureGroupStoreLinkEntity addFeatureGroupStoreLink(
      FeatureGroupStoreLinkEntity featureGroupStoreLinkEntity) {
    final ExampleMatcher caseInsensitiveExampleMatcher =
        ExampleMatcher.matchingAll().withIgnoreCase();
    final List<FeatureGroupStoreLinkEntity> existingFeatureGroupStoreLinks =
        this.featureGroupStoreLinkRepository.findAll(
            Example.of(
                new FeatureGroupStoreLinkEntity(
                    featureGroupStoreLinkEntity.getFeatureGroupId(),
                    featureGroupStoreLinkEntity.getFeatureStoreId()),
                caseInsensitiveExampleMatcher));

    if (existingFeatureGroupStoreLinks.size() > 1) {
      throw new RuntimeException(
          "More than 1 feature group-store mappings already exist for pair: "
              + featureGroupStoreLinkEntity);
    } else if (existingFeatureGroupStoreLinks.size() == 1) {
      if (existingFeatureGroupStoreLinks
          .get(0)
          .getMode()
          .equals(featureGroupStoreLinkEntity.getMode())) {
        throw new RuntimeException(
            "Feature group-store mappings already linked with same mode: "
                + featureGroupStoreLinkEntity);
      }
    }
    return this.featureGroupStoreLinkRepository.save(featureGroupStoreLinkEntity);
  }

  public List<FeatureGroupStoreLinkEntity> findFeatureGroupStoreLinks(UUID featureGroupId) {
    final ExampleMatcher caseInsensitiveExampleMatcher =
        ExampleMatcher.matchingAll().withIgnoreCase();
    return this.featureGroupStoreLinkRepository.findAll(
        Example.of(FeatureGroupStoreLinkEntity.fromFeatureGroupId(featureGroupId)));
  }

  public Optional<FeatureGroupStoreLinkEntity> findPrimaryFeatureGroupStoreLink(
      UUID featureGroupId) {
    return this.findFeatureGroupStoreLinks(featureGroupId).stream()
        .filter(x -> x.getMode().equals(FeatureGroupStoreLinkEntity.FEATURE_GROUP_PRIMARY_STORE))
        .findFirst();
  }

  public List<FeatureGroupStoreLinkEntity> swapFeatureGroupLinks(UUID featureGroupId) {
    List<FeatureGroupStoreLinkEntity> featureGroupStoreLinks =
        this.findFeatureGroupStoreLinks(featureGroupId);
    if (featureGroupStoreLinks.size() > 2) {
      throw new RuntimeException(
          "More than 2 feature stores linked to feature group Id: "
              + featureGroupId
              + ", cannot swap");
    }
    Optional<FeatureGroupStoreLinkEntity> primary =
        this.findFeatureGroupStoreLinks(featureGroupId).stream()
            .filter(
                x -> x.getMode().equals(FeatureGroupStoreLinkEntity.FEATURE_GROUP_PRIMARY_STORE))
            .findFirst();
    Optional<FeatureGroupStoreLinkEntity> secondary =
        this.findFeatureGroupStoreLinks(featureGroupId).stream()
            .filter(
                x ->
                    x.getMode()
                        .equals(FeatureGroupStoreLinkEntity.FEATURE_GROUP_WRITE_REPLICA_STORE))
            .findFirst();

    if (secondary.isEmpty()) {
      throw new RuntimeException(
          "no write replica feature group store link to swap for featureGroupId: "
              + featureGroupId);
    } else {
      secondary.get().setMode(FeatureGroupStoreLinkEntity.FEATURE_GROUP_PRIMARY_STORE);
      this.featureGroupStoreLinkRepository.save(secondary.get());
    }

    if (primary.isPresent()) {
      primary.get().setMode(FeatureGroupStoreLinkEntity.FEATURE_GROUP_WRITE_REPLICA_STORE);
      this.featureGroupStoreLinkRepository.save(primary.get());
    }
    return this.findFeatureGroupStoreLinks(featureGroupId);
  }

  public FeatureGroupStoreLinkEntity deleteFeatureGroupStoreLink(UUID featureGroupStoreLinkId) {
    final Optional<FeatureGroupStoreLinkEntity> featureGroupStoreLinkEntity =
        this.featureGroupStoreLinkRepository.findById(featureGroupStoreLinkId);
    if (featureGroupStoreLinkEntity.isEmpty()) {
      throw new RuntimeException(
          "Feature Group Store links not found with ID: " + featureGroupStoreLinkId);
    } else if (featureGroupStoreLinkEntity
        .get()
        .getMode()
        .equals(FeatureGroupStoreLinkEntity.FEATURE_GROUP_PRIMARY_STORE)) {
      throw new RuntimeException(
          "Cannot delete primary feature store link, please swap before delete");
    }
    this.featureGroupStoreLinkRepository.deleteById(featureGroupStoreLinkId);
    return featureGroupStoreLinkEntity.get();
  }
}
