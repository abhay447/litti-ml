package com.litti.ml.runtime;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.litti.ml.entities.model.BatchPredictionRequest;
import com.litti.ml.entities.model.BatchPredictionResponse;
import com.litti.ml.model.ModelRegistry;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MyHttpHandler implements HttpHandler {
  static Logger logger = LogManager.getLogger(MyHttpHandler.class);

  private final ModelRegistry modelRegistry;

  public MyHttpHandler(ModelRegistry modelRegistry) {
    this.modelRegistry = modelRegistry;
  }

  @Override
  public void handle(HttpExchange httpExchange) throws IOException {
    final Gson gson = new Gson();
    try {
      logger.trace("Received request {}", httpExchange);
      if (httpExchange.getRequestMethod().equals("POST")) {
        final String requestBody = new String(httpExchange.getRequestBody().readAllBytes());
        final BatchPredictionRequest batchPredictionRequest =
            gson.fromJson(requestBody, BatchPredictionRequest.class);
        logger.debug("prediction request: {}", batchPredictionRequest);
        final String predictionRoute = extractModelRoute(httpExchange);
        logger.info("received request for prediction route: {}", predictionRoute);
        final BatchPredictionResponse predictionResponses =
            this.modelRegistry.forwardToRouter(predictionRoute, batchPredictionRequest);
        logger.debug("prediction response {}", predictionResponses);
        handleResponse(httpExchange, 200, gson.toJsonTree(predictionResponses));
      } else {
        final Map<String, ?> responseMap =
            Map.ofEntries(Map.entry("error", "only post method is supported"));
        handleResponse(httpExchange, 400, gson.toJsonTree(responseMap));
      }
    } catch (Exception e) {
      logger.info("error occurred in handling request", e);
      final Map<String, ?> responseMap =
          Map.ofEntries(
              Map.entry("error", "Internal server error"), Map.entry("message", e.getMessage()));
      handleResponse(httpExchange, 503, gson.toJsonTree(responseMap));
    }
  }

  private void handleResponse(
      HttpExchange httpExchange, int responseStatus, JsonElement responseJson) throws IOException {
    final OutputStream outputStream = httpExchange.getResponseBody();
    final Gson gson = new Gson();
    final String htmlResponse = gson.toJson(responseJson);
    httpExchange.getResponseHeaders().set("Content-Type", "application/json");
    httpExchange.sendResponseHeaders(responseStatus, htmlResponse.length());
    outputStream.write(htmlResponse.getBytes());
    outputStream.flush();
    outputStream.close();
  }

  private String extractModelRoute(HttpExchange httpExchange) {
    logger.info("received request for path {}", httpExchange.getRequestURI().getPath());
    final String[] uriTokens = httpExchange.getRequestURI().getPath().split("/predict/");
    if (uriTokens.length != 2) {
      throw new RuntimeException(
          "invalid url spec, use <base_url>/predict/<model_name>/<model_version>");
    }
    final String[] predictionTokens = uriTokens[1].split("/");
    if (predictionTokens.length != 2) {
      throw new RuntimeException(
          "invalid url spec, use <base_url>/predict/<model_name>/<model_version>");
    }
    return String.format("%s#%s", predictionTokens[0], predictionTokens[1]);
  }
}
