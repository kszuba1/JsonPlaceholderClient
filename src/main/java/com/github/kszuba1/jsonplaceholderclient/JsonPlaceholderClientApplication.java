package com.github.kszuba1.jsonplaceholderclient;

import com.github.kszuba1.jsonplaceholderclient.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class JsonPlaceholderClientApplication implements CommandLineRunner {

	private final PostService postService;

	public static void main(String[] args) {
		SpringApplication.run(JsonPlaceholderClientApplication.class, args).close();
	}

	@Override
	public void run(String... args) {
		postService.downloadAndSavePosts();
	}
}