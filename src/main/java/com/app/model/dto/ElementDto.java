package com.app.model.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ElementDto {

  private final String primaryKey;
  private final String name;
  private final String description;
  private final LocalDateTime updatedTimestamp;
}
