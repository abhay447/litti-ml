package com.litti.ml.management.service;

import com.google.common.collect.Sets;
import com.litti.ml.management.dto.CreateModelRequest;
import com.litti.ml.management.entiites.FeatureEntity;
import com.litti.ml.management.entiites.ModelEntity;
import com.litti.ml.management.entiites.ModelFeatureLinkEntity;
import com.litti.ml.management.repository.FeatureRepository;
import com.litti.ml.management.repository.ModelFeatureLinkRepository;
import com.litti.ml.management.repository.ModelRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class ModelManagementService {

  private final ModelRepository modelRepository;

  private final ModelFeatureLinkRepository modelFeatureLinkRepository;

  private final FeatureRepository featureRepository;

  public ModelManagementService(
      ModelRepository modelRepository,
      ModelFeatureLinkRepository modelFeatureLinkRepository,
      FeatureRepository featureRepository) {
    this.modelRepository = modelRepository;
    this.modelFeatureLinkRepository = modelFeatureLinkRepository;
    this.featureRepository = featureRepository;
  }

  public ModelEntity addModel(CreateModelRequest createModelRequest) {
    final ModelEntity modelEntity = createModelRequest.getModelEntity();
    final List<UUID> featureUUIDs =
        createModelRequest.getFeatureIds().stream().map(UUID::fromString).toList();
    final ExampleMatcher caseInsensitiveExampleMatcher =
        ExampleMatcher.matchingAll().withIgnoreCase();
    if (this.modelRepository
        .findOne(
            Example.of(
                new ModelEntity(modelEntity.getName(), modelEntity.getVersion()),
                caseInsensitiveExampleMatcher))
        .isPresent()) {
      throw new RuntimeException("model already exists with name and version");
    }

    Set<UUID> existingFeatureIds =
        this.featureRepository.findAllById(featureUUIDs).stream()
            .map(FeatureEntity::getId)
            .collect(Collectors.toSet());
    if (!existingFeatureIds.equals(featureUUIDs)) {
      throw new RuntimeException(
          "feature ids not found: " + Sets.difference(Set.of(featureUUIDs), existingFeatureIds));
    }
    final ModelEntity savedModelEntity = this.modelRepository.save(modelEntity);
    final List<ModelFeatureLinkEntity> modelFeatureLinks =
        featureUUIDs.stream()
            .map(featureId -> new ModelFeatureLinkEntity(savedModelEntity.getId(), featureId))
            .toList();
    this.modelFeatureLinkRepository.saveAll(modelFeatureLinks);
    return savedModelEntity;
  }

  public ModelEntity findById(UUID modelId) {
    final Optional<ModelEntity> dbModelEntity = this.modelRepository.findById(modelId);
    if (dbModelEntity.isEmpty()) {
      throw new RuntimeException("modelId not found");
    }
    return dbModelEntity.get();
  }

  public List<ModelEntity> findAll() {
    return this.modelRepository.findAll();
  }
}
