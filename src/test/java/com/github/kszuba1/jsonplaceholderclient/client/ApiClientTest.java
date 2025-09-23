package com.github.kszuba1.jsonplaceholderclient.client;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.github.kszuba1.jsonplaceholderclient.config.JsonPlaceholderProperties;
import com.github.kszuba1.jsonplaceholderclient.model.Post;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

@ExtendWith(MockitoExtension.class)
public class ApiClientTest {

  @Mock
  private JsonPlaceholderProperties properties;

  @Mock
  private HttpExecutor httpExecutor;

  private ApiClient apiClient;

  private static final String BASE_URI = "https://jsonplaceholder.typicode.com";
  private static final List<Post> EXPECTED_POSTS = List.of(
      new Post(1L, 1L, "Test Title 1", "Test Body 1"),
      new Post(2L, 1L, "Test Title 2", "Test Body 2")
  );

  @BeforeEach
  void setUp() {
    when(properties.getBaseUri()).thenReturn(BASE_URI);
    apiClient = new ApiClient(properties, httpExecutor);
  }

  @Test
  @DisplayName("should return posts")
  void shouldReturnPosts() {
    // given
    final var expectedUrl = BASE_URI + "/posts";

    when(httpExecutor.execute(eq(expectedUrl), any(HttpHeaders.class), eq(HttpMethod.GET), any(ParameterizedTypeReference.class)))
        .thenReturn(EXPECTED_POSTS);

    // when
    final var result = apiClient.getPosts();

    // then
    assertThat(result).isEqualTo(EXPECTED_POSTS);
  }
}
