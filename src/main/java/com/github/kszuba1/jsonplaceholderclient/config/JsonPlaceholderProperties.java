package com.github.kszuba1.jsonplaceholderclient.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "json-placeholder")
@Getter
@Setter
public class JsonPlaceholderProperties {
  private String baseUri;
}
