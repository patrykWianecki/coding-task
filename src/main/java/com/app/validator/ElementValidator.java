package com.app.validator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import static java.nio.charset.StandardCharsets.*;

@Component
public class ElementValidator {

  private static final String HEADER = "Header";
  private static final String PRIMARY_KEY = "PRIMARY_KEY";
  private static final String NAME = "NAME";
  private static final String DESCRIPTION = "DESCRIPTION";
  private static final String UPDATED_TIMESTAMP = "UPDATED_TIMESTAMP";

  public void validate(final MultipartFile file, final Map<String, String> errors) {
    if (Objects.isNull(file)) {
      throw new IllegalArgumentException("File does not exist");
    }

    String fileName = file.getName();
    String fileContent;
    try {
      fileContent = new String(file.getBytes(), UTF_8);
    } catch (IOException e) {
      throw new IllegalArgumentException("Failed to read file " + fileName);
    }

    if (StringUtils.isBlank(fileContent)) {
      throw new IllegalArgumentException("Uploaded file has not content");
    } else {
      final List<String> lines = new BufferedReader(new StringReader(fileContent))
          .lines()
          .collect(Collectors.toList());

      if (CollectionUtils.isNotEmpty(lines)) {

        validateFirstLine(lines.get(0), errors);

        AtomicInteger elementNumber = new AtomicInteger(1);
        lines
            .stream()
            .skip(1)
            .forEach(line -> validateElement(line, errors, elementNumber));
      }
    }
  }

  private static void validateFirstLine(String firstLine, Map<String, String> errors) {
    String[] headers = firstLine.split(",");
    if (headers.length != 4) {
      errors.put(HEADER, "Invalid number of headers - " + Arrays.toString(headers));
    } else {
      String primaryKey = headers[0];
      String name = headers[1];
      String description = headers[2];
      String updatedTimestamp = headers[3];
      if (!PRIMARY_KEY.equals(primaryKey)) {
        errors.put(createHeaderKey(PRIMARY_KEY), createHeaderValue(PRIMARY_KEY, primaryKey));
      }
      if (!NAME.equals(name)) {
        errors.put(createHeaderKey(NAME), createHeaderValue(NAME, name));
      }
      if (!DESCRIPTION.equals(description)) {
        errors.put(createHeaderKey(DESCRIPTION), createHeaderValue(DESCRIPTION, description));
      }
      if (!UPDATED_TIMESTAMP.equals(updatedTimestamp)) {
        errors.put(
            createHeaderKey(UPDATED_TIMESTAMP),
            createHeaderValue(UPDATED_TIMESTAMP, updatedTimestamp)
        );
      }
    }
  }

  private static void validateElement(String line, final Map<String, String> errors,
      AtomicInteger elementNumber) {
    String[] lines = line.split(",");

    if (lines.length != 4) {
      errors.put(
          createElementKey(elementNumber), "Invalid number of elements - " + Arrays.toString(lines)
      );
    } else {
      String primaryKey = convertStringNull(lines[0]);
      String name = convertStringNull(lines[1]);
      String description = convertStringNull(lines[2]);
      String updatedTimestamp = convertStringNull(lines[3]);

      if (StringUtils.isEmpty(primaryKey)) {
        errors.put(createElementKey(elementNumber) + " primaryKey", primaryKey);
      }
      if (StringUtils.isEmpty(name)) {
        errors.put(createElementKey(elementNumber) + " name", name);
      }
      if (StringUtils.isEmpty(description)) {
        errors.put(createElementKey(elementNumber) + " description", description);
      }
      if (StringUtils.isEmpty(updatedTimestamp)) {
        errors.put(createElementKey(elementNumber) + " updatedTimestamp", updatedTimestamp);
      }
    }
  }

  private static String convertStringNull(String elementValue) {
    return "null".equals(elementValue) ? null : elementValue;
  }

  private static String createElementKey(AtomicInteger elementNumber) {
    return "Element no. " + elementNumber.getAndIncrement();
  }

  private static String createHeaderKey(String headerName) {
    return HEADER + " " + headerName;
  }

  private static String createHeaderValue(String headerName, String headerValue) {
    return headerName + " has incorrect value " + headerValue;
  }
}
