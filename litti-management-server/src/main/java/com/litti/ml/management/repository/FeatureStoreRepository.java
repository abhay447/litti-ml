package com.litti.ml.management.repository;

import com.litti.ml.management.entiites.FeatureStoreEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeatureStoreRepository extends JpaRepository<FeatureStoreEntity, UUID> {}
