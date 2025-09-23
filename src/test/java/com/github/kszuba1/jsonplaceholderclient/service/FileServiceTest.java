package com.github.kszuba1.jsonplaceholderclient.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kszuba1.jsonplaceholderclient.config.JsonPlaceholderProperties;
import com.github.kszuba1.jsonplaceholderclient.model.Post;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class FileServiceTest {

  private FileService fileService;
  private ObjectMapper mapper;

  @TempDir
  Path tempDir;

  @BeforeEach
  void setUp() {
    final var properties = new JsonPlaceholderProperties();
    properties.setOutputDir(tempDir.toString());
    fileService = new FileService(properties);
    mapper = new ObjectMapper();
  }

  @Test
  @DisplayName("should save json to file")
  void shouldSaveJsonToFile() throws Exception {
    // given
    final var post = Post.builder().id(123L).userId(1L).title("Test Title").body("Test Body").build();

    // when
    fileService.savePostToJsonFile(post);

    // then
    final var savedFile = tempDir.resolve("123.json").toFile();
    assertThat(savedFile).exists();

    final var fileContent = Files.readString(savedFile.toPath());
    final var result = mapper.readValue(fileContent, Post.class);

    assertThat(result.getId()).isEqualTo(123);
    assertThat(result.getTitle()).isEqualTo("Test Title");
    assertThat(result.getBody()).isEqualTo("Test Body");
  }
}
