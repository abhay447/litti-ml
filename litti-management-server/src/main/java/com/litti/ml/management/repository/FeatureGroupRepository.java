package com.litti.ml.management.repository;

import com.litti.ml.management.entiites.FeatureGroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FeatureGroupRepository extends JpaRepository<FeatureGroupEntity, UUID> {}
