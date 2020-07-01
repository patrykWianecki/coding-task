package com.app.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.app.model.Element;
import com.app.model.ElementsResponse;
import com.app.model.dto.ElementDto;
import com.app.model.mapper.ElementMapper;
import com.app.repository.ElementRepository;

import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;

import static java.nio.charset.StandardCharsets.*;
import static java.time.format.DateTimeFormatter.*;

@Service
@Transactional
@RequiredArgsConstructor
public class ElementService {

  private final ElementRepository elementRepository;

  public ResponseEntity<ElementsResponse> addElements(MultipartFile file) throws IOException {
    String fileContent = new String(file.getBytes(), UTF_8);

    final List<ElementDto> elementDtos = new BufferedReader(new StringReader(fileContent))
        .lines()
        .skip(1)
        .map(ElementService::createElement)
        .filter(Objects::nonNull)
        .collect(Collectors.toList());

    saveElements(elementDtos);

    return ResponseEntity.ok(
        ElementsResponse.builder()
            .elements(elementDtos)
            .build()
    );
  }

  private void saveElements(List<ElementDto> elementsDtos) {
    final List<Element> elements = elementsDtos
        .stream()
        .map(elementDto -> {
          Element element = ElementMapper.fromElementDtoToElement(elementDto);
          element.setPrimaryKey(elementDto.getPrimaryKey());
          return element;
        })
        .collect(Collectors.toList());
    elementRepository.saveAll(elements);
  }

  private static ElementDto createElement(String line) {
    String[] lines = line.split(",");
    if (lines.length != 4) {
      return null;
    }
    String primaryKey = convertStringNull(lines[0]);
    String name = convertStringNull(lines[1]);
    String description = convertStringNull(lines[2]);
    String updatedTimestamp = convertStringNull(lines[3]);
    if (isElementNotValid(primaryKey, name, description, updatedTimestamp)) {
      return null;
    }

    return ElementDto.builder()
        .primaryKey(primaryKey)
        .name(name)
        .description(description)
        .updatedTimestamp(parseDate(updatedTimestamp))
        .build();
  }

  private static String convertStringNull(String value) {
    return "null".equals(value) ? null : value;
  }

  private static boolean isElementNotValid(String primaryKey, String name, String description,
      String updatedTimestamp) {
    return StringUtils.isBlank(primaryKey) ||
        StringUtils.isBlank(name) ||
        StringUtils.isBlank(description) ||
        StringUtils.isBlank(updatedTimestamp);
  }

  private static LocalDateTime parseDate(String updatedTimestamp) {
    return LocalDateTime.parse(updatedTimestamp, ISO_LOCAL_DATE_TIME);
  }

  public ResponseEntity<ElementDto> findElementByPrimaryKey(
      String primaryKey) throws NotFoundException {

    final Optional<Element> element = elementRepository.findById(primaryKey);
    if (element.isPresent()) {
      return ResponseEntity.ok(ElementMapper.fromElementToElementDto(element.get()));
    } else {
      throw new NotFoundException(
          MessageFormat.format("Element with primary key {0} does not exist", primaryKey)
      );
    }
  }

  public ResponseEntity<String> deleteElementByPrimaryKey(
      String primaryKey) throws NotFoundException {

    elementRepository.deleteById(primaryKey);
    final Optional<Element> element = elementRepository.findById(primaryKey);
    if (element.isPresent()) {
      throw new NotFoundException("Failed to delete element with primary key " + primaryKey);
    } else {
      return ResponseEntity.ok(
          "Successfully removed element with primary key " + primaryKey
      );
    }
  }
}
