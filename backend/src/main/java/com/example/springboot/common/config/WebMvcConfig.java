package com.example.springboot.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

  @Override
  public void configurePathMatch(PathMatchConfigurer configurer) {
    // This automatically prepends "/api/v1" to any controller annotated with @RestController
    configurer.addPathPrefix("/api/v1", c -> c.isAnnotationPresent(RestController.class));
  }
}
