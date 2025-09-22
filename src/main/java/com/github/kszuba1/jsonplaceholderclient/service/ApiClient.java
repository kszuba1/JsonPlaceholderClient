package com.github.kszuba1.jsonplaceholderclient.service;

import com.github.kszuba1.jsonplaceholderclient.config.JsonPlaceholderProperties;
import com.github.kszuba1.jsonplaceholderclient.model.Post;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
@EnableConfigurationProperties(JsonPlaceholderProperties.class)
@Slf4j
public class ApiClient {

  private final RestTemplate restTemplate;
  private final JsonPlaceholderProperties properties;

  public List<Post> getPosts() {
    final var headers = new HttpHeaders();
    final var url = properties.getBaseUri() + "/posts";
    log.info("Sending GET request to {}", url);
    return restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), new ParameterizedTypeReference<List<Post>>(){}).getBody();
  }

}
