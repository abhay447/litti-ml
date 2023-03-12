package com.litti.ml.management.service;

import com.litti.ml.management.entiites.ModelEntity;
import com.litti.ml.management.repository.FeatureRepository;
import com.litti.ml.management.repository.ModelFeatureLinkRepository;
import com.litti.ml.management.repository.ModelRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;

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

  public ModelEntity addModel(ModelEntity modelEntity) {
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
    ModelEntity savedEntity = this.modelRepository.save(modelEntity);
    return savedEntity;
  }
}
