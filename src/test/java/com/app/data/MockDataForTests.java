package com.app.data;

import java.time.LocalDateTime;

import com.app.model.Element;
import com.app.model.dto.ElementDto;

public interface MockDataForTests {

  String ELEMENT_PRIMARY_KEY = "10";
  String FIRST_ELEMENT_PRIMARY_KEY = "1";
  String SECOND_ELEMENT_PRIMARY_KEY = "2";
  String THIRD_ELEMENT_PRIMARY_KEY = "3";
  String INCORRECT_ELEMENT_PRIMARY_KEY = "0";
  String ELEMENT_NAME = "name";
  String FIRST_ELEMENT_NAME = "firstName";
  String SECOND_ELEMENT_NAME = "secondName";
  String THIRD_ELEMENT_NAME = "thirdName";
  String ELEMENT_DESCRIPTION = "description";
  String FIRST_ELEMENT_DESCRIPTION = "firstDescription";
  String SECOND_ELEMENT_DESCRIPTION = "secondDescription";
  String THIRD_ELEMENT_DESCRIPTION = "thirdDescription";

  LocalDateTime ELEMENT_UPDATED_TIMESTAMP = LocalDateTime.of(2020, 2, 11, 7, 13, 30);
  LocalDateTime FIRST_ELEMENT_UPDATED_TIMESTAMP = LocalDateTime.of(2017, 8, 4, 10, 11, 30);
  LocalDateTime SECOND_ELEMENT_UPDATED_TIMESTAMP = LocalDateTime.of(2018, 9, 14, 11, 12, 40);
  LocalDateTime THIRD_ELEMENT_UPDATED_TIMESTAMP = LocalDateTime.of(2019, 10, 24, 12, 13, 50);

  static Element createElement() {
    return Element.builder()
        .primaryKey(ELEMENT_PRIMARY_KEY)
        .name(ELEMENT_NAME)
        .description(ELEMENT_DESCRIPTION)
        .updatedTimestamp(ELEMENT_UPDATED_TIMESTAMP)
        .build();
  }

  static ElementDto createElementDto() {
    return ElementDto.builder()
        .primaryKey(ELEMENT_PRIMARY_KEY)
        .name(ELEMENT_NAME)
        .description(ELEMENT_DESCRIPTION)
        .updatedTimestamp(ELEMENT_UPDATED_TIMESTAMP)
        .build();
  }
}
