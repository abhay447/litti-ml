package com.litti.ml.management.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/models")
public class HelloController {

  @GetMapping("/")
  public String list() {
    return "Greetings from Spring Boot!";
  }

  @PostMapping("/")
  public String add() {
    return "Greetings from Spring Boot!";
  }

  @GetMapping("/{modelId")
  public String get(@PathVariable String modelId) {
    return "Greetings from Spring Boot!";
  }

  @DeleteMapping("/{modelId}")
  public String delete(@PathVariable String modelId) {
    return "Greetings from Spring Boot!";
  }
}
