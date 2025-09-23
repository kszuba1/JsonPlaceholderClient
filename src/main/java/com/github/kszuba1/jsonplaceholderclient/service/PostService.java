package com.github.kszuba1.jsonplaceholderclient.service;

import com.github.kszuba1.jsonplaceholderclient.client.ApiClient;
import com.github.kszuba1.jsonplaceholderclient.exception.ApiClientException;
import com.github.kszuba1.jsonplaceholderclient.exception.ApiServerException;
import com.github.kszuba1.jsonplaceholderclient.model.Post;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostService {

  private final ApiClient apiClient;
  private final FileService fileService;

  public void downloadAndSavePosts() {
    try {
      final var posts = apiClient.getPosts();
      posts.forEach(this::savePostToFile);
    } catch (ApiClientException | ApiServerException e) {
      log.error("Could not fetch posts: {}", e.getMessage());
    }
  }

  private void savePostToFile(Post post) {
    try {
      fileService.savePostToJsonFile(post);
    } catch (IOException e) {
      log.error("Unable to save post-{} to file. Skipping...", post.getId(), e);
    }
  }
}
