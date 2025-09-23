package com.github.kszuba1.jsonplaceholderclient.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class  Post {

  private Long id;
  private Long userId;
  private String title;
  private String body;
}
