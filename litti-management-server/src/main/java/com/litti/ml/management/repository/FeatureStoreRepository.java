package com.litti.ml.management.repository;

import com.litti.ml.management.entiites.FeatureStoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FeatureStoreRepository extends JpaRepository<FeatureStoreEntity, UUID> {}
