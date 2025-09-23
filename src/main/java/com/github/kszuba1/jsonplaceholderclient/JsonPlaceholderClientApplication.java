package com.github.kszuba1.jsonplaceholderclient;

import com.github.kszuba1.jsonplaceholderclient.config.JsonPlaceholderProperties;
import com.github.kszuba1.jsonplaceholderclient.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@RequiredArgsConstructor
@EnableConfigurationProperties(JsonPlaceholderProperties.class)
public class JsonPlaceholderClientApplication implements CommandLineRunner {

	private final PostService postService;

	public static void main(String[] args) {
		SpringApplication.run(JsonPlaceholderClientApplication.class, args).close();
	}

	@Override
	public void run(String... args) {
		postService.fetchAndSavePosts();
	}
}