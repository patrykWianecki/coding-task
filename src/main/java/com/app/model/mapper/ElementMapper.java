package com.app.model.mapper;

import com.app.model.Element;
import com.app.model.dto.ElementDto;

public interface ElementMapper {

  static Element fromElementDtoToElement(ElementDto elementDto) {
    return Element.builder()
        .primaryKey(elementDto.getPrimaryKey())
        .name(elementDto.getName())
        .description(elementDto.getDescription())
        .updatedTimestamp(elementDto.getUpdatedTimestamp())
        .build();
  }

  static ElementDto fromElementToElementDto(Element element) {
    return ElementDto.builder()
        .primaryKey(element.getPrimaryKey())
        .name(element.getName())
        .description(element.getDescription())
        .updatedTimestamp(element.getUpdatedTimestamp())
        .build();
  }
}
