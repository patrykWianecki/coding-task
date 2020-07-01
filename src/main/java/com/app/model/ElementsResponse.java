package com.app.model;

import java.util.List;
import java.util.Map;

import com.app.model.dto.ElementDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
public class ElementsResponse {

  private final List<ElementDto> elements;
  @Setter
  @JsonInclude(Include.NON_NULL)
  private Map<String, String> errors;
}
