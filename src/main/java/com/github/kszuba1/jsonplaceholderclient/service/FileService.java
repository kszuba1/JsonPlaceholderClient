package com.github.kszuba1.jsonplaceholderclient.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kszuba1.jsonplaceholderclient.config.JsonPlaceholderProperties;
import com.github.kszuba1.jsonplaceholderclient.model.Post;
import java.io.File;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileService {

  private final JsonPlaceholderProperties properties;
  private final ObjectMapper mapper = new ObjectMapper();

  public void savePostToJsonFile(Post post) throws IOException {
    final var outputDir = new File(properties.getOutputDir());
    outputDir.mkdirs();

    final var fileName = properties.getOutputDir() + "/" + post.getId() + ".json";
    mapper.writerWithDefaultPrettyPrinter().writeValue(new File(fileName), post);

    log.debug("Saved post {} -> {}", post.getId(), fileName);
  }
}
