package com.github.kszuba1.jsonplaceholderclient.service;

import com.fasterxml.jackson.databind.ObjectMapper;
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

  private static final String OUTPUT_DIR = "./posts";

  private final ObjectMapper mapper = new ObjectMapper();

  public void savePostToJsonFile(Post post) throws IOException {
    final var outputDir = new File(OUTPUT_DIR);
    outputDir.mkdirs();

    final var fileName = OUTPUT_DIR + "/" + post.getId() + ".json";
    mapper.writerWithDefaultPrettyPrinter().writeValue(new File(fileName), post);

    log.debug("Saved post {} -> {}", post.getId(), fileName);
  }
}
