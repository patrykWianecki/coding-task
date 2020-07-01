package com.app.data;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;

import org.springframework.util.ResourceUtils;

import com.app.model.Element;
import com.app.model.dto.ElementDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MockDataForTests {

  public static final byte[] FILE_WITHOUT_ELEMENTS_BYTES =
      convertTextFileToBytes("fileWithoutElements.txt");
  public static final byte[] FILE_WITH_INVALID_HEADER_BYTES =
      convertTextFileToBytes("fileWithInvalidHeader.txt");
  public static final byte[] FILE_WITH_INVALID_HEADER_VALUES_BYTES =
      convertTextFileToBytes("fileWithInvalidHeaderValues.txt");
  public static final byte[] FILE_WITH_INVALID_ELEMENTS_BYTES =
      convertTextFileToBytes("fileWithInvalidElements.txt");
  public static final byte[] MIXED_FILE_BYTES = convertTextFileToBytes("mixedFile.txt");
  public static final byte[] VALID_FILE_BYTES = convertTextFileToBytes("validFile.txt");

  public static final String ELEMENT_PRIMARY_KEY = "10";
  public static final String FIRST_ELEMENT_PRIMARY_KEY = "1";
  public static final String SECOND_ELEMENT_PRIMARY_KEY = "2";
  public static final String THIRD_ELEMENT_PRIMARY_KEY = "3";
  public static final String INCORRECT_ELEMENT_PRIMARY_KEY = "0";
  public static final String ELEMENT_NAME = "name";
  public static final String FIRST_ELEMENT_NAME = "firstName";
  public static final String SECOND_ELEMENT_NAME = "secondName";
  public static final String THIRD_ELEMENT_NAME = "thirdName";
  public static final String ELEMENT_DESCRIPTION = "description";
  public static final String FIRST_ELEMENT_DESCRIPTION = "firstDescription";
  public static final String SECOND_ELEMENT_DESCRIPTION = "secondDescription";
  public static final String THIRD_ELEMENT_DESCRIPTION = "thirdDescription";
  public static final String FILE_NAME = "fileName";
  public static final String RESOURCE_FILE_PATH = "src/test/resources/";

  public static final LocalDateTime ELEMENT_UPDATED_TIMESTAMP =
      LocalDateTime.of(2020, 2, 11, 7, 13, 30);
  public static final LocalDateTime FIRST_ELEMENT_UPDATED_TIMESTAMP =
      LocalDateTime.of(2017, 8, 4, 10, 11, 30);
  public static final LocalDateTime SECOND_ELEMENT_UPDATED_TIMESTAMP =
      LocalDateTime.of(2018, 9, 14, 11, 12, 40);
  public static final LocalDateTime THIRD_ELEMENT_UPDATED_TIMESTAMP =
      LocalDateTime.of(2019, 10, 24, 12, 13, 50);

  public static Element createElement() {
    return Element.builder()
        .primaryKey(ELEMENT_PRIMARY_KEY)
        .name(ELEMENT_NAME)
        .description(ELEMENT_DESCRIPTION)
        .updatedTimestamp(ELEMENT_UPDATED_TIMESTAMP)
        .build();
  }

  public static ElementDto createElementDto() {
    return ElementDto.builder()
        .primaryKey(ELEMENT_PRIMARY_KEY)
        .name(ELEMENT_NAME)
        .description(ELEMENT_DESCRIPTION)
        .updatedTimestamp(ELEMENT_UPDATED_TIMESTAMP)
        .build();
  }

  static byte[] convertTextFileToBytes(String fileName) {
    try {
      File file = ResourceUtils.getFile(RESOURCE_FILE_PATH + fileName);
      return Files.readAllBytes(Paths.get(file.getAbsolutePath()));
    } catch (IOException e) {
      log.error("Failed to convert file to bytes", e);
      return new byte[0];
    }
  }
}
