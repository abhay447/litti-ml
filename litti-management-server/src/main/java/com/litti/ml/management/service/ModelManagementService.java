package com.litti.ml.management.service;

import com.google.common.collect.Sets;
import com.litti.ml.entities.feature.FeatureGroup;
import com.litti.ml.entities.feature.FeatureMetadata;
import com.litti.ml.entities.model.ModelMetadata;
import com.litti.ml.management.dto.CreateModelRequest;
import com.litti.ml.management.entiites.FeatureEntity;
import com.litti.ml.management.entiites.ModelEntity;
import com.litti.ml.management.entiites.ModelFeatureLinkEntity;
import com.litti.ml.management.repository.FeatureGroupRepository;
import com.litti.ml.management.repository.FeatureRepository;
import com.litti.ml.management.repository.ModelFeatureLinkRepository;
import com.litti.ml.management.repository.ModelRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ModelManagementService {

  private static final Logger logger = LogManager.getLogger(ModelManagementService.class);

  // TODO : add more null field checks before saving

  private final ModelRepository modelRepository;

  private final ModelFeatureLinkRepository modelFeatureLinkRepository;

  private final FeatureRepository featureRepository;

  private final FeatureGroupRepository featureGroupRepository;

  public ModelManagementService(
      ModelRepository modelRepository,
      ModelFeatureLinkRepository modelFeatureLinkRepository,
      FeatureRepository featureRepository,
      FeatureGroupRepository featureGroupRepository) {
    this.modelRepository = modelRepository;
    this.modelFeatureLinkRepository = modelFeatureLinkRepository;
    this.featureRepository = featureRepository;
    this.featureGroupRepository = featureGroupRepository;
  }

  public ModelEntity addModel(CreateModelRequest createModelRequest) {
    logger.info("received request: {}", createModelRequest);
    final ModelEntity modelEntity = createModelRequest.getModelEntity();
    final Set<UUID> featureUUIDs =
        createModelRequest.getFeatureIds().stream()
            .map(UUID::fromString)
            .collect(Collectors.toSet());
    final ExampleMatcher caseInsensitiveExampleMatcher =
        ExampleMatcher.matchingAll().withIgnoreCase();
    if (modelRepository
        .findOne(
            Example.of(
                new ModelEntity(modelEntity.getName(), modelEntity.getVersion()),
                caseInsensitiveExampleMatcher))
        .isPresent()) {
      throw new RuntimeException("model already exists with name and version");
    }
    List<FeatureEntity> existingFeatures = this.featureRepository.findAllById(featureUUIDs);
    Set<UUID> existingFeatureIds =
        existingFeatures.stream().map(FeatureEntity::getId).collect(Collectors.toSet());
    if (!existingFeatureIds.equals(featureUUIDs)) {
      throw new RuntimeException(
          "feature ids not found: " + Sets.difference(Set.of(featureUUIDs), existingFeatureIds));
    }
    Map<String, Long> featureNameFrequency =
        existingFeatures.stream()
            .collect(Collectors.groupingBy(FeatureEntity::getName, Collectors.counting()));
    for (Map.Entry<String, Long> entry : featureNameFrequency.entrySet()) {
      if (entry.getValue() > 1) {
        throw new RuntimeException("more than one feature found with same name: " + entry.getKey());
      }
    }
    final ModelEntity savedModelEntity = modelRepository.save(modelEntity);
    final List<ModelFeatureLinkEntity> modelFeatureLinks =
        featureUUIDs.stream()
            .map(featureId -> new ModelFeatureLinkEntity(savedModelEntity.getId(), featureId))
            .toList();
    this.modelFeatureLinkRepository.saveAll(modelFeatureLinks);
    return savedModelEntity;
  }

  public ModelEntity findById(UUID modelId) {
    final Optional<ModelEntity> dbModelEntity = modelRepository.findById(modelId);
    if (dbModelEntity.isEmpty()) {
      throw new RuntimeException("modelId not found");
    }
    return dbModelEntity.get();
  }

  public List<ModelEntity> findAll() {
    return modelRepository.findAll();
  }

  public ModelMetadata getModelDeploymentMetadata(UUID modelId) {
    final ModelEntity modelEntity = modelRepository.findById(modelId).get();
    final List<ModelFeatureLinkEntity> modelFeatureLinkEntities =
        this.modelFeatureLinkRepository.findAll(Example.of(new ModelFeatureLinkEntity(modelId)));
    final List<UUID> featureIds =
        modelFeatureLinkEntities.stream().map(ModelFeatureLinkEntity::getFeatureId).toList();
    final List<FeatureEntity> featureEntities = this.featureRepository.findAllById(featureIds);
    final List<UUID> featureGroupIds =
        featureEntities.stream().map(FeatureEntity::getFeatureGroupId).toList();
    final Map<UUID, FeatureGroup> featureGroupMap =
        this.featureGroupRepository.findAllById(featureGroupIds).stream()
            .map(
                featureGroupEntity ->
                    Map.entry(
                        featureGroupEntity.getId(), featureGroupEntity.toFeatureGroupMetadata()))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    final List<FeatureMetadata> featureMetadataList =
        featureEntities.stream()
            .map(
                featureEntity ->
                    featureEntity.toFeatureDeploymentMetadata(
                        featureGroupMap.get(featureEntity.getFeatureGroupId())))
            .toList();
    return modelEntity.toModelDeploymentMetadata(featureMetadataList);
  }
}
