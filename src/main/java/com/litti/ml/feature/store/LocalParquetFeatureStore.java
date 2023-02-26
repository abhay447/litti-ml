package com.litti.ml.feature.store;

import com.google.common.io.Resources;
import com.litti.ml.feature.entities.FeatureGroup;
import com.litti.ml.feature.entities.FeatureMetadata;
import org.apache.avro.generic.GenericRecord;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.parquet.avro.AvroParquetReader;
import org.apache.parquet.hadoop.ParquetReader;
import org.apache.parquet.hadoop.util.HadoopInputFile;
import org.apache.parquet.io.InputFile;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class LocalParquetFeatureStore implements FeatureStore {

  private final String name = "LocalParquet";
  private final List<GenericRecord> records;

  public LocalParquetFeatureStore() {
    this.records = readAllLocalRecords();
  }

  @Override
  public String name() {
    return this.name;
  }

  @Override
  public Map<String, Object> fetchFeatures(
      List<FeatureMetadata> featureMetadataList,
      FeatureGroup featureGroup,
      Map<String, String> inputDimensions) {
    final Map<String, String> queryDimensions =
        inputDimensions.entrySet().stream()
            .filter(x -> featureGroup.dimensions().contains(x.getKey()))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    if (queryDimensions.containsKey("dt")) {
      // HACK : cast dt to epoch days since our test parquet files have dt in avro DaysSinceEPoch
      // format
      queryDimensions.put(
          "dt", String.valueOf(LocalDate.parse(queryDimensions.get("dt")).toEpochDay()));
    }
    final Predicate<GenericRecord> baseFilterQuery = Objects::nonNull;
    Predicate<GenericRecord> dimensionFilterQuery =
        queryDimensions.entrySet().stream()
            .map(
                entry ->
                    baseFilterQuery.and(
                        record -> record.get(entry.getKey()).toString().equals(entry.getValue())))
            .reduce(baseFilterQuery, Predicate::and);
    List<Map<String, Object>> featureValues =
        this.records.stream()
            .filter(dimensionFilterQuery)
            .map(
                record -> {
                  Map<String, Object> features = new HashMap<>();
                  featureMetadataList.forEach(
                      f -> {
                        if (record.hasField(f.name()) && record.get(f.name()) != null) {
                          features.put(f.name(), record.get(f.name()));
                        } else {
                          features.put(f.name(), Double.valueOf(f.defaultValue()));
                        }
                      });
                  return features;
                })
            .collect(Collectors.toList());
    return featureValues.get(0);
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
