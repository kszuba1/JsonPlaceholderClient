package com.github.kszuba1.jsonplaceholderclient.service;

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
    final var posts = apiClient.getPosts();

    posts.forEach(this::savePostToFile);
  }

  private void savePostToFile(Post post) {
    try {
      fileService.savePostToJsonFile(post);
    } catch (IOException e) {
      log.error("Unable to save post-{} to file. Skipping...", post.getId(), e);
    }
  }
}
