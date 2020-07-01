package com.app.exception;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.app.controller.CustomErrorResponse;

import javassist.NotFoundException;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class ExceptionAdvice {

  @ExceptionHandler(value = NotFoundException.class)
  public ResponseEntity<CustomErrorResponse> handleGenericNotFoundException(NotFoundException e) {
    return new ResponseEntity<>(createCustomErrorResponse(e, "ELEMENT_NOT_FOUND"), NOT_FOUND);
  }

  @ExceptionHandler(value = EmptyResultDataAccessException.class)
  public ResponseEntity<CustomErrorResponse> handleGenericNotFoundException(
      EmptyResultDataAccessException e) {
    return new ResponseEntity<>(createCustomErrorResponse(e, "CLASS_NOT_FOUND"), NOT_FOUND);
  }

  @ExceptionHandler(value = IOException.class)
  public ResponseEntity<CustomErrorResponse> handleGenericNotFoundException(IOException e) {
    return new ResponseEntity<>(createCustomErrorResponse(e, "INCORRECT_FILE"), NOT_FOUND);
  }

  @ExceptionHandler(value = ElementException.class)
  public ResponseEntity<CustomErrorResponse> handleValidationException(ElementException e) {
    return new ResponseEntity<>(createCustomErrorResponse(e, "FILE_VALIDATION_ERROR"), NOT_FOUND);
  }

  private static CustomErrorResponse createCustomErrorResponse(Exception e, String errorCode) {
    return CustomErrorResponse.builder()
        .errorCode(errorCode)
        .errorMessage(e.getMessage())
        .timestamp(LocalDateTime.now())
        .status(NOT_FOUND.value())
        .build();
  }
}
