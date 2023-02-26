package com.litti.ml.feature.loader;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.google.common.io.Resources;
import com.litti.ml.feature.entities.FeatureGroup;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.AbstractMap.SimpleEntry;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class StaticResourcesFGLoader implements FeatureGroupLoader {

  private static final Logger logger = LogManager.getLogger(StaticResourcesFGLoader.class);

  private static final List<String> fgFileNames =
      List.of(
          "feature-groups/dream11_fg_player.json",
          "feature-groups/dream11_fg_player_date.json",
          "feature-groups/dream11_fg_player_date_venue.json");
  final ObjectMapper objectMapper = JsonMapper.builder().addModule(new GuavaModule()).build();

  @Override
  public FeatureGroupLoadingResult loadAllFeatureGroups() {
    Map<Boolean, Set<SimpleEntry<String, Optional<FeatureGroup>>>> featureGroupLoadingMap =
        fgFileNames.stream()
            .map(
                featureGroupFile -> {
                  try {
                    final String featureGroupJson =
                        Files.readString(
                            Paths.get(Resources.getResource(featureGroupFile).toURI()));
                    final FeatureGroup featureGroup =
                        objectMapper.readValue(featureGroupJson, FeatureGroup.class);
                    logger.info(
                        "successfully loaded featureGroup from source {}", featureGroupFile);
                    return new SimpleEntry<>(featureGroupFile, Optional.of(featureGroup));
                  } catch (Exception e) {
                    logger.error(
                        "failed to load featureGroup from source {} ", featureGroupFile, e);
                    return new SimpleEntry<String, Optional<FeatureGroup>>(
                        featureGroupFile, Optional.empty());
                  }
                })
            .collect(
                Collectors.partitioningBy(
                    entry -> entry.getValue().isPresent(), Collectors.toSet()));
    final Set<FeatureGroup> fgLoadSuccess =
        featureGroupLoadingMap.get(true).stream()
            .map(entry -> entry.getValue().get())
            .collect(Collectors.toSet());
    final Set<String> fgLoadFailSources =
        featureGroupLoadingMap.get(false).stream()
            .map(SimpleEntry::getKey)
            .collect(Collectors.toSet());
    return new FeatureGroupLoadingResult(fgLoadSuccess, fgLoadFailSources);
  }
}
