package com.litti.ml.management.repository;

import com.litti.ml.management.entiites.FeatureEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FeatureRepository extends JpaRepository<FeatureEntity, UUID> {}
