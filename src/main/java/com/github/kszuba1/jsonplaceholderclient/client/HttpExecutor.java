package com.github.kszuba1.jsonplaceholderclient.client;

import com.github.kszuba1.jsonplaceholderclient.exception.ApiClientException;
import com.github.kszuba1.jsonplaceholderclient.exception.ApiServerException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@RequiredArgsConstructor
public class HttpExecutor {

  private static final String HTTP_ERROR_MSG = "%s error when calling: %s";

  private final RestTemplate restTemplate;

  public <T> T execute(String url, HttpHeaders headers, HttpMethod method, ParameterizedTypeReference<T> responseType) {
    return execute(url, headers, method, null, responseType);
  }

  public <T> T execute(String url, HttpHeaders headers, HttpMethod method, Object body, ParameterizedTypeReference<T> responseType) {
    try {
      log.info("Sending {} request to {}", method, url);

      final var response = restTemplate.exchange(url, method, new HttpEntity<>(body, headers), responseType);

      log.info("Successfully received response from {}", url);

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
