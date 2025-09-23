package com.github.kszuba1.jsonplaceholderclient.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.github.kszuba1.jsonplaceholderclient.client.ApiClient;
import com.github.kszuba1.jsonplaceholderclient.exception.ApiClientException;
import com.github.kszuba1.jsonplaceholderclient.exception.ApiServerException;
import com.github.kszuba1.jsonplaceholderclient.model.Post;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

@ExtendWith({MockitoExtension.class, OutputCaptureExtension.class})
class PostServiceTest {

  @Mock
  private ApiClient apiClient;

  @Mock
  private FileService fileService;

  private PostService postService;

  @BeforeEach
  void setUp() {
    postService = new PostService(apiClient, fileService);
  }

  @Test
  @DisplayName("should save all posts")
  void shouldSaveAllPosts() throws IOException {
    // given
    final var post1 = Post.builder().id(1L).userId(1L).title("Test Title1").body("Test Body1").build();
    final var post2 = Post.builder().id(2L).userId(2L).title("Test Title2").body("Test Body2").build();
    when(apiClient.getPosts()).thenReturn(List.of(post1, post2));

    // when
    postService.fetchAndSavePosts();

    // then
    verify(fileService).savePostToJsonFile(post1);
    verify(fileService).savePostToJsonFile(post2);
  }

  @Test
  @DisplayName("should log error when client exception occurs")
  void shouldLogErrorWhenClientException(CapturedOutput output) throws IOException {
    // given
    when(apiClient.getPosts()).thenThrow(new ApiClientException("Client error", null));

    // when
    postService.fetchAndSavePosts();

    // then
    verify(fileService, never()).savePostToJsonFile(any());
    assertThat(output).contains("Could not fetch posts: Client error");
  }

  @Test
  @DisplayName("should log error when server exception occurs")
  void shouldLogErrorWhenServerException(CapturedOutput output) throws IOException {
    // given
    when(apiClient.getPosts()).thenThrow(new ApiServerException("Server error", null));

    // when
    postService.fetchAndSavePosts();

    // then
    verify(fileService, never()).savePostToJsonFile(any());
    assertThat(output).contains("Could not fetch posts: Server error");
  }

  @Test
  @DisplayName("should log error when file save failed")
  void shouldLogErrorWhenFileSaveFailed(CapturedOutput output) throws IOException {
    // given
    final var post1 = Post.builder().id(1L).userId(1L).title("Test Title1").body("Test Body1").build();
    final var post2 = Post.builder().id(2L).userId(2L).title("Test Title2").body("Test Body2").build();
    when(apiClient.getPosts()).thenReturn(List.of(post1, post2));

    doThrow(new IOException("Disk full")).when(fileService).savePostToJsonFile(post1);

    // when
    postService.fetchAndSavePosts();

    // then
    verify(fileService).savePostToJsonFile(post1);
    verify(fileService).savePostToJsonFile(post2);
    assertThat(output)
        .contains("Unable to save post-1 to file. Skipping...")
        .doesNotContain("Unable to save post-2 to file. Skipping...");
  }
}
