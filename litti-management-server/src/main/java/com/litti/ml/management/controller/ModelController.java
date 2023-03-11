package com.litti.ml.management.controller;

import com.litti.ml.entities.model.ModelMetadata;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping(value = "/models")
public class ModelController {

  @GetMapping(value = "/", produces = "application/json")
  public Set<ModelMetadata> list() {
    return null;
  }

  @PostMapping(value = "/", produces = "application/json", consumes = "application/json")
  public String add(@RequestBody ModelMetadata modelMetadata) {
    return "Greetings from Spring Boot!";
  }

  @GetMapping(value = "/{modelId}", produces = "application/json")
  public ModelMetadata get(@PathVariable String modelId) {
    return null;
  }

  @DeleteMapping(value = "/{modelId}", produces = "application/json")
  public String delete(@PathVariable String modelId) {
    return "Greetings from Spring Boot!";
  }
}
