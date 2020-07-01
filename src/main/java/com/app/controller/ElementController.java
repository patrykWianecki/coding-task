package com.app.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.app.model.ElementsResponse;
import com.app.model.dto.ElementDto;
import com.app.service.ElementService;
import com.app.validator.ElementValidator;

import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/elements")
public class ElementController {

  private final ElementService elementService;
  private final ElementValidator elementValidator;

  @PostMapping
  public ResponseEntity<ElementsResponse> addElements(
      @RequestBody MultipartFile file) throws IOException {
    Map<String, String> errors = new HashMap<>();
    elementValidator.validate(file, errors);

    final ResponseEntity<ElementsResponse> responseEntity = elementService.addElements(file);

    if (!errors.isEmpty()) {
      final ElementsResponse elementsResponse = responseEntity.getBody();
      elementsResponse.setErrors(errors);
      return ResponseEntity.ok(elementsResponse);
    }

    return responseEntity;
  }

  @GetMapping
  public ResponseEntity<ElementDto> findElementByPrimaryKey(
      @RequestParam String primaryKey) throws NotFoundException {
    return elementService.findElementByPrimaryKey(primaryKey);
  }

  @DeleteMapping
  public ResponseEntity<String> deleteElementByPrimaryKey(
      @RequestParam String primaryKey) throws NotFoundException {
    return elementService.deleteElementByPrimaryKey(primaryKey);
  }
}
