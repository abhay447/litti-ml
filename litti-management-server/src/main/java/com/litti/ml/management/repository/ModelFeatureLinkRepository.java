package com.litti.ml.management.repository;

import com.litti.ml.management.entiites.ModelFeatureLinkEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ModelFeatureLinkRepository extends JpaRepository<ModelFeatureLinkEntity, UUID> {}
