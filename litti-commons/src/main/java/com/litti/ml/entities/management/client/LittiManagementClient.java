package com.litti.ml.entities.management.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.litti.ml.entities.feature.FeatureGroup;
import com.litti.ml.entities.model.ModelMetadata;
import java.io.IOException;
import java.util.List;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.http.io.entity.EntityUtils;

public class LittiManagementClient {

  private final String managementServerUrl;

  public LittiManagementClient(String managementServerUrl) {
    this.managementServerUrl = managementServerUrl;
  }

  public List<ModelMetadata> listModels() throws IOException {
    try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
      HttpGet httpGet = new HttpGet(String.format("%s/models", managementServerUrl));

      HttpClientResponseHandler<List<ModelMetadata>> responseHandler =
          httpResponse -> {
            int statusCode = httpResponse.getCode();
            if (statusCode >= 200 && statusCode < 300) {
              // Successful response, process the entity
              String body = EntityUtils.toString(httpResponse.getEntity());
              final ObjectMapper objectMapper = new ObjectMapper();
              objectMapper.registerModule(new GuavaModule());
              objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
              return objectMapper.readValue(body, new TypeReference<List<ModelMetadata>>() {});
            } else {
              // Handle error responses
              throw new IOException("Request failed with status code: " + statusCode);
            }
          };
      return httpClient.execute(httpGet, responseHandler);
    }
  }

  public List<FeatureGroup> listFeatureGroups() throws IOException {
    try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
      HttpGet httpGet = new HttpGet(String.format("%s/feature-groups", managementServerUrl));

      HttpClientResponseHandler<List<FeatureGroup>> responseHandler =
          httpResponse -> {
            int statusCode = httpResponse.getCode();
            if (statusCode >= 200 && statusCode < 300) {
              // Successful response, process the entity
              String body = EntityUtils.toString(httpResponse.getEntity());
              final ObjectMapper objectMapper = new ObjectMapper();
              objectMapper.registerModule(new GuavaModule());
              objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
              return objectMapper.readValue(body, new TypeReference<List<FeatureGroup>>() {});
            } else {
              // Handle error responses
              throw new IOException("Request failed with status code: " + statusCode);
            }
          };
      return httpClient.execute(httpGet, responseHandler);
    }
  }
}
