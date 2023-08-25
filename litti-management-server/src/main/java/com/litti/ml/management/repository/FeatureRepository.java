package com.litti.ml.management.repository;

import com.litti.ml.management.entiites.FeatureEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeatureRepository extends JpaRepository<FeatureEntity, UUID> {}
