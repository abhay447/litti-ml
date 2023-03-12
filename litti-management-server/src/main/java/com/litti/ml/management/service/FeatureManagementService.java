package com.litti.ml.management.service;

import com.litti.ml.management.entiites.FeatureEntity;
import com.litti.ml.management.repository.FeatureRepository;
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

  public FeatureManagementService(FeatureRepository featureRepository) {
    this.featureRepository = featureRepository;
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

  public FeatureEntity findById(UUID featureId) {
    final Optional<FeatureEntity> dbFeatureEntity = this.featureRepository.findById(featureId);
    if (dbFeatureEntity.isEmpty()) {
      throw new RuntimeException("feature not found with id: " + featureId);
    }
    return dbFeatureEntity.get();
  }

  public List<FeatureEntity> findAll() {
    return this.featureRepository.findAll();
  }
}
