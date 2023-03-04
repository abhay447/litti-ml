package com.litti.ml.entities.dtypes;

import static com.litti.ml.entities.dtypes.LogicalDataTypes.*;

import java.util.List;
import java.util.stream.Collectors;

public class JsonDataReader {

  public Object read(Object obj, String dataType) {
    switch (dataType) {
      case PRIMITIVE_DATA_TYPE_STRING:
        return readString(obj);
      case PRIMITIVE_DATA_TYPE_BOOLEAN:
        return readBool(obj);
      case PRIMITIVE_DATA_TYPE_DOUBLE:
        return readDouble(obj);
      case PRIMITIVE_DATA_TYPE_LONG:
        return readLong(obj);

      case ARRAY_DATA_TYPE_STRING:
        return readStringArray(obj);
      case ARRAY_DATA_TYPE_BOOLEAN:
        return readBooleanArray(obj);
      case ARRAY_DATA_TYPE_DOUBLE:
        return readDoubleArray(obj);
      case ARRAY_DATA_TYPE_LONG:
        return readLongArray(obj);

      case MATRIX_DATA_TYPE_STRING:
        return readStringMatrix(obj);
      case MATRIX_DATA_TYPE_BOOLEAN:
        return readBooleanMatrix(obj);
      case MATRIX_DATA_TYPE_DOUBLE:
        return readDoubleMatrix(obj);
      case MATRIX_DATA_TYPE_LONG:
        return readLongMatrix(obj);
      default:
        throw new RuntimeException("unknown feature data type: " + dataType);
    }
  }

  private Double readDouble(Object obj) {
    return Double.valueOf(obj.toString());
  }

  private Long readLong(Object obj) {
    return Double.valueOf(obj.toString()).longValue();
  }

  private Boolean readBool(Object obj) {
    return (Boolean) obj;
  }

  private String readString(Object obj) {
    return obj.toString();
  }

  private List<Double> readDoubleArray(Object obj) {
    List<Object> raw_inputs = (List<Object>) obj;
    return raw_inputs.stream().map(x -> Double.valueOf(x.toString())).collect(Collectors.toList());
  }

  private List<Long> readLongArray(Object obj) {
    List<Object> raw_inputs = (List<Object>) obj;
    return raw_inputs.stream()
        .map(x -> Double.valueOf(x.toString()).longValue())
        .collect(Collectors.toList());
  }

  private List<Boolean> readBooleanArray(Object obj) {
    List<Object> raw_inputs = (List<Object>) obj;
    return raw_inputs.stream().map(x -> (Boolean) x).collect(Collectors.toList());
  }

  private List<String> readStringArray(Object obj) {
    List<Object> raw_inputs = (List<Object>) obj;
    return raw_inputs.stream().map(Object::toString).collect(Collectors.toList());
  }

  private List<List<Double>> readDoubleMatrix(Object obj) {
    List<List<Object>> raw_inputs = (List<List<Object>>) obj;
    return raw_inputs.stream()
        .map(
            row -> row.stream().map(x -> Double.valueOf(x.toString())).collect(Collectors.toList()))
        .collect(Collectors.toList());
  }

  private List<List<Long>> readLongMatrix(Object obj) {
    List<List<Object>> raw_inputs = (List<List<Object>>) obj;
    return raw_inputs.stream()
        .map(
            row ->
                row.stream()
                    .map(x -> Double.valueOf(x.toString()).longValue())
                    .collect(Collectors.toList()))
        .collect(Collectors.toList());
  }

  private List<List<Boolean>> readBooleanMatrix(Object obj) {
    List<List<Object>> raw_inputs = (List<List<Object>>) obj;
    return raw_inputs.stream()
        .map(row -> row.stream().map(x -> (Boolean) x).collect(Collectors.toList()))
        .collect(Collectors.toList());
  }

  private List<List<String>> readStringMatrix(Object obj) {
    List<List<Object>> raw_inputs = (List<List<Object>>) obj;
    return raw_inputs.stream()
        .map(row -> row.stream().map(Object::toString).collect(Collectors.toList()))
        .collect(Collectors.toList());
  }
}
