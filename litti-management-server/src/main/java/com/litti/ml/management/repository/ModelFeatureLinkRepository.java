package com.litti.ml.management.repository;

import com.litti.ml.management.entiites.ModelFeatureLinkEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ModelFeatureLinkRepository extends JpaRepository<ModelFeatureLinkEntity, UUID> {}
