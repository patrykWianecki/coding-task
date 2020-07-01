package com.app.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ElementException extends RuntimeException {

  private final String message;
}
