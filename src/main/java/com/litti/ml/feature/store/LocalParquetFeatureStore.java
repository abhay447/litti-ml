package com.litti.ml.feature.store;

import com.google.common.io.Resources;
import com.litti.ml.feature.dtypes.JsonDataReader;
import com.litti.ml.feature.entities.FeatureGroup;
import com.litti.ml.feature.entities.FeatureMetadata;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.apache.avro.generic.GenericRecord;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.parquet.avro.AvroParquetReader;
import org.apache.parquet.hadoop.ParquetReader;
import org.apache.parquet.hadoop.util.HadoopInputFile;
import org.apache.parquet.io.InputFile;

public class LocalParquetFeatureStore extends AbstractFeatureStore {

  private final String name = "LocalParquet";
  private final List<GenericRecord> records;
  private final JsonDataReader jsonDataReader;

  public LocalParquetFeatureStore(JsonDataReader jsonDataReader) {
    this.jsonDataReader = jsonDataReader;
    this.records = readAllLocalRecords();
  }

  @Override
  public String name() {
    return this.name;
  }

  @Override
  public FeatureFetchResult fetchFeatureFromStore(
      List<FeatureMetadata> featureMetadataList,
      FeatureGroup featureGroup,
      Map<String, String> dimensions) {

    Predicate<GenericRecord> dimensionFilterQuery =
        generateDimensionFilterQuery(dimensions, featureGroup);

    Optional<FeatureFetchResult> featureFetchResult =
        queryLocalFile(featureMetadataList, dimensionFilterQuery);

    if (!featureFetchResult.isEmpty()) {
      return featureFetchResult.get();
    }
    Map<String, ?> defaultFeatures = createDefaultFeatures(featureMetadataList);

    return new FeatureFetchResult(
        defaultFeatures,
        featureMetadataList.stream().map(FeatureMetadata::name).collect(Collectors.toSet()),
        Collections.emptySet());
  }

  private Optional<FeatureFetchResult> queryLocalFile(
      List<FeatureMetadata> featureMetadataList, Predicate<GenericRecord> dimensionFilterQuery) {
    Set<String> featureStoreMisses = new HashSet<>();
    List<Map<String, Object>> featureValues =
        this.records.stream()
            .filter(dimensionFilterQuery)
            .map(
                record -> {
                  Map<String, Object> features = new HashMap<>();
                  featureMetadataList.forEach(
                      f -> {
                        if (record.hasField(f.name()) && record.get(f.name()) != null) {
                          features.put(
                              f.name(), jsonDataReader.read(record.get(f.name()), f.dataType()));
                        } else {
                          features.put(
                              f.name(), jsonDataReader.read(f.defaultValue(), f.dataType()));
                          featureStoreMisses.add(f.name());
                        }
                      });
                  return features;
                })
            .collect(Collectors.toList());
    if (!featureValues.isEmpty()) {
      return Optional.of(
          new FeatureFetchResult(featureValues.get(0), featureStoreMisses, Collections.emptySet()));
    }
    return Optional.empty();
  }

  private Predicate<GenericRecord> generateDimensionFilterQuery(
      Map<String, String> requestInputs, FeatureGroup featureGroup) {
    final Map<String, String> queryDimensions =
        requestInputs.entrySet().stream()
            .filter(x -> featureGroup.dimensions().contains(x.getKey()))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    if (queryDimensions.containsKey("dt")) {
      // HACK : cast dt to epoch days since our test parquet files have dt in avro DaysSinceEPoch
      // format
      queryDimensions.put(
          "dt", String.valueOf(LocalDate.parse(queryDimensions.get("dt")).toEpochDay()));
    }
    final Predicate<GenericRecord> baseFilterQuery = Objects::nonNull;
    return queryDimensions.entrySet().stream()
        .map(
            entry ->
                baseFilterQuery.and(
                    record -> record.get(entry.getKey()).toString().equals(entry.getValue())))
        .reduce(baseFilterQuery, Predicate::and);
  }

  private Map<String, ?> createDefaultFeatures(List<FeatureMetadata> featureMetadataList) {
    return featureMetadataList.stream()
        .map(f -> Map.entry(f.name(), jsonDataReader.read(f.defaultValue(), f.dataType())))
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
  }

  private List<GenericRecord> readAllLocalRecords() {
    List<GenericRecord> allRecords = new ArrayList<>();
    try {
      final Path path =
          new Path(Resources.getResource("feature-store-data/local-parquet/data.parquet").toURI());
      final InputFile inputFile = HadoopInputFile.fromPath(path, new Configuration());
      ParquetReader<GenericRecord> reader =
          AvroParquetReader.<GenericRecord>builder(inputFile).build();
      GenericRecord record;
      while ((record = reader.read()) != null) {
        allRecords.add(record);
      }
    } catch (IOException | URISyntaxException e) {
      throw new RuntimeException(e);
    }
    return allRecords;
  }
}
