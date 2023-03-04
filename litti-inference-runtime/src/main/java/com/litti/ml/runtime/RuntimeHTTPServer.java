package com.litti.ml.runtime;

import com.litti.ml.model.ModelRegistry;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RuntimeHTTPServer {

  private static final Logger logger = LogManager.getLogger(RuntimeHTTPServer.class);

  private final HttpServer server;

  private final ThreadPoolExecutor threadPoolExecutor;

  private final ModelRegistry modelRegistry;

  public RuntimeHTTPServer(ModelRegistry modelRegistry) throws IOException {
    this.modelRegistry = modelRegistry;
    server = HttpServer.create(new InetSocketAddress("localhost", 8001), 0);
    threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
    server.createContext("/predict", new MyHttpHandler(modelRegistry));
    server.setExecutor(threadPoolExecutor);
    server.start();
    logger.info(" Server started on port 8001");
  }
}
