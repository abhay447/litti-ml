package com.litti.ml.management;

import com.litti.ml.management.config.ArtifactStorageConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableConfigurationProperties(ArtifactStorageConfig.class)
public class Application {

  private static final Logger logger = LogManager.getLogger(Application.class);

  public static void main(String[] args) {
    logger.info("starting app");
    SpringApplication.run(Application.class, args);
  }

  @Bean
  public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
    return args -> {};
  }
}
