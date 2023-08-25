package com.litti.ml.management.repository;

import com.litti.ml.management.entiites.FeatureGroupEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeatureGroupRepository extends JpaRepository<FeatureGroupEntity, UUID> {}
