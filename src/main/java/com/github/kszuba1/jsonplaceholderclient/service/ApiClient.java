package com.github.kszuba1.jsonplaceholderclient.service;

import com.github.kszuba1.jsonplaceholderclient.config.JsonPlaceholderProperties;
import com.github.kszuba1.jsonplaceholderclient.exception.ApiClientException;
import com.github.kszuba1.jsonplaceholderclient.exception.ApiServerException;
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
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
@EnableConfigurationProperties(JsonPlaceholderProperties.class)
@Slf4j
public class ApiClient {

  private static final String HTTP_ERROR_MSG = "%s error when calling: %s";

  private final RestTemplate restTemplate;
  private final JsonPlaceholderProperties properties;

  public List<Post> getPosts() {
    final var headers = new HttpHeaders();
    final var url = properties.getBaseUri() + "/posts";
    log.info("Sending GET request to {}", url);

    try {
      final var response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), new ParameterizedTypeReference<List<Post>>(){});

      log.info("Successfully fetched posts from {}", url);
      return response.getBody();
    } catch (HttpClientErrorException e) {
      String errorMsg = String.format(HTTP_ERROR_MSG, e.getStatusCode(), url);
      log.error(errorMsg, e);
      throw new ApiClientException(errorMsg, e);
    } catch (HttpServerErrorException e) {
      String errorMsg = String.format(HTTP_ERROR_MSG, e.getStatusCode(), url);
      log.error(errorMsg, e);
      throw new ApiServerException(errorMsg, e);
    } catch (RestClientException e) {
      String errorMsg = String.format(HTTP_ERROR_MSG, "Unexpected", url);
      log.error(errorMsg, e);
      throw new ApiClientException(errorMsg, e);
    }
  }

}
