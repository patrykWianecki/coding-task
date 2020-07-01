package com.app.validator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.regex.PatternSyntaxException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

import static java.nio.charset.StandardCharsets.*;

@Component
public class ElementValidator implements Validator {

  @Override
  public boolean supports(final Class<?> clazz) {
    return MultipartFile.class.equals(clazz);
  }

  @Override
  public void validate(final Object target, final Errors errors) {
    MultipartFile file = (MultipartFile) target;

    String fileContent;
    try {
      fileContent = new String(file.getBytes(), UTF_8);
    } catch (IOException e) {
      errors.rejectValue("file", "Failed to read file");
      throw new IllegalArgumentException("Failed to read file");
    }

    if (StringUtils.isBlank(fileContent)) {
      errors.rejectValue("file", "Uploaded file is empty");
      throw new IllegalArgumentException("Uploaded file is empty!");
    } else {
      new BufferedReader(new StringReader(fileContent))
          .lines()
          .skip(1)
          .forEach(s -> validateElement(s, errors));
    }
  }

  private static void validateElement(String line, Errors errors) {
    String[] lines;
    try {
      lines = line.split(",");
    } catch (PatternSyntaxException e) {
      errors.rejectValue("test", "Tu sie wyjebalo");
      throw new IllegalArgumentException("asd sss");
    }

    if (lines.length != 4) {
      errors.rejectValue("test", Arrays.toString(lines));
    } else {
      String primaryKey = lines[0];
      String name = lines[1];
      String description = lines[2];
      String updatedTimestamp = lines[3];

      if (StringUtils.isBlank(primaryKey)) {
        errors.rejectValue("primaryKey", Arrays.toString(lines));
      } else if (StringUtils.isBlank(name)) {
        errors.rejectValue("name", Arrays.toString(lines));
      } else if (StringUtils.isBlank(description)) {
        errors.rejectValue("description", Arrays.toString(lines));
      } else if (StringUtils.isBlank(updatedTimestamp)) {
        errors.rejectValue("updatedTimestamp", Arrays.toString(lines));
      }
    }
  }
}
