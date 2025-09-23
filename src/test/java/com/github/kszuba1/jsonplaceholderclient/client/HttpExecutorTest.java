package com.github.kszuba1.jsonplaceholderclient.client;

import com.github.kszuba1.jsonplaceholderclient.exception.ApiClientException;
import com.github.kszuba1.jsonplaceholderclient.exception.ApiServerException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HttpExecutorTest {

  @Mock
  private RestTemplate restTemplate;

  private HttpExecutor httpExecutor;

  private static final String TEST_URL = "https://api.example.com/test";
  private static final HttpHeaders TEST_HEADERS = new HttpHeaders();
  private static final ParameterizedTypeReference<String> RESPONSE_TYPE = new ParameterizedTypeReference<String>() {};

  @BeforeEach
  void setUp() {
    httpExecutor = new HttpExecutor(restTemplate);
  }

  @Test
  @DisplayName("should return response body when no request body provided")
  void shouldReturnResponseBodyWhenNoRequestBodyProvided() {
    // given
    final var mockResponse = ResponseEntity.ok("Success response");
    when(restTemplate.exchange(eq(TEST_URL), eq(HttpMethod.GET), any(HttpEntity.class), eq(RESPONSE_TYPE))).thenReturn(mockResponse);

    // when
    final var result = httpExecutor.execute(TEST_URL, TEST_HEADERS, HttpMethod.GET, RESPONSE_TYPE);

    // then
    assertThat(result).isEqualTo("Success response");
  }

  @Test
  @DisplayName("should return response body when request body provided")
  void shouldReturnResponseBodyWhenRequestBodyProvided() {
    // given
    final var requestBody = "Request body";
    final var mockResponse = ResponseEntity.ok("Success response");

    when(restTemplate.exchange(eq(TEST_URL), eq(HttpMethod.POST), any(HttpEntity.class), eq(RESPONSE_TYPE))).thenReturn(mockResponse);

    // when
    final var result = httpExecutor.execute(TEST_URL, TEST_HEADERS, HttpMethod.POST, requestBody, RESPONSE_TYPE);

    // then
    assertThat(result).isEqualTo("Success response");
  }

  @Test
  @DisplayName("should throw exception when client error")
  void shouldExceptionWhenClientError() {
    // given
    final var clientException = new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Bad Request");

    when(restTemplate.exchange(eq(TEST_URL), eq(HttpMethod.GET), any(HttpEntity.class), eq(RESPONSE_TYPE))).thenThrow(clientException);

    // when & then
    assertThatThrownBy(() -> httpExecutor.execute(TEST_URL, TEST_HEADERS, HttpMethod.GET, RESPONSE_TYPE))
        .isInstanceOf(ApiClientException.class)
        .hasMessage("400 BAD_REQUEST error when calling: " + TEST_URL)
        .hasCause(clientException);
  }

  @Test
  @DisplayName("should throw exception when server error")
  void shouldExceptionWhenServerError() {
    // given
    final var serverException = new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");

    when(restTemplate.exchange(eq(TEST_URL), eq(HttpMethod.GET), any(HttpEntity.class), eq(RESPONSE_TYPE))).thenThrow(serverException);

    // when & then
    assertThatThrownBy(() -> httpExecutor.execute(TEST_URL, TEST_HEADERS, HttpMethod.GET, RESPONSE_TYPE))
        .isInstanceOf(ApiServerException.class)
        .hasMessage("500 INTERNAL_SERVER_ERROR error when calling: " + TEST_URL)
        .hasCause(serverException);
  }

  @Test
  @DisplayName("should throw exception when rest client exception")
  void shouldExceptionWhenRestClientException() {
    // given
    final var restClientException = new RestClientException("Connection failed");

    when(restTemplate.exchange(eq(TEST_URL), eq(HttpMethod.GET), any(HttpEntity.class), eq(RESPONSE_TYPE))).thenThrow(restClientException);

    // when & then
    assertThatThrownBy(() -> httpExecutor.execute(TEST_URL, TEST_HEADERS, HttpMethod.GET, RESPONSE_TYPE))
        .isInstanceOf(ApiClientException.class)
        .hasMessage("Unexpected error when calling: " + TEST_URL)
        .hasCause(restClientException);
  }
}