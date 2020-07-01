package com.app.controller;

import java.io.IOException;
import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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

  @InitBinder
  private void initBinder(WebDataBinder webDataBinder) {
    webDataBinder.addValidators(elementValidator);
  }

  @PostMapping
  public ResponseEntity<List<ElementDto>> addElements(
      @RequestBody @Valid MultipartFile file) throws IOException {
    return elementService.addElements(file);
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
