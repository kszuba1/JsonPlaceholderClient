package com.github.kszuba1.jsonplaceholderclient.service;

import com.github.kszuba1.jsonplaceholderclient.config.JsonPlaceholderProperties;
import com.github.kszuba1.jsonplaceholderclient.model.Post;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@EnableConfigurationProperties(JsonPlaceholderProperties.class)
@Slf4j
public class ApiClient {

  private final JsonPlaceholderProperties properties;
  private final HttpExecutor httpExecutor;

  public List<Post> getPosts() {
    final var headers = new HttpHeaders();
    final var postsPath = "/posts";
    final var url = properties.getBaseUri() + postsPath;

    return httpExecutor.execute(url, headers, HttpMethod.GET, null, new ParameterizedTypeReference<>(){});
  }
}
