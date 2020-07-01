package com.app.validator;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import static com.app.data.MockDataForTests.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class ElementValidatorTest {

  @Mock
  private MultipartFile fileMock;

  @InjectMocks
  private ElementValidator elementValidator;

  private final Map<String, String> errors = new HashMap<>();

  @Test
  void shouldThrowExceptionWhenFileDoesNotExist() {
    // when
    final IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class, () -> elementValidator.validate(null, null));

    // then
    assertEquals("File does not exist", exception.getMessage());
  }

  @Test
  void shouldThrowExceptionWhenFileFailsToRead() throws IOException {
    // given
    when(fileMock.getBytes()).thenThrow(new IOException());
    when(fileMock.getName()).thenReturn(FILE_NAME);

    // when
    final IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class, () -> elementValidator.validate(fileMock, null)
    );

    // then
    assertEquals("Failed to read file " + FILE_NAME, exception.getMessage());
  }

  @Test
  void shouldThrowExceptionWhenFileHasNoContent() throws IOException {
    // given
    when(fileMock.getBytes()).thenReturn(new byte[0]);

    // when
    final IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class, () -> elementValidator.validate(fileMock, null)
    );

    // then
    assertEquals("Uploaded file has not content", exception.getMessage());
  }

  @Test
  void shouldValidateCorrectFile() throws IOException {
    // given
    when(fileMock.getBytes()).thenReturn(VALID_FILE_BYTES);

    // when
    elementValidator.validate(fileMock, errors);

    // then
    assertEquals(0, errors.size());
  }

  @Test
  void shouldValidateFileWithoutElements() throws IOException {
    // given
    when(fileMock.getBytes()).thenReturn(FILE_WITHOUT_ELEMENTS_BYTES);

    // when
    elementValidator.validate(fileMock, errors);

    // then
    assertEquals(0, errors.size());
  }

  @Test
  void shouldValidateFileWithIncorrectHeader() throws IOException {
    // given
    when(fileMock.getBytes()).thenReturn(FILE_WITH_INVALID_HEADER_BYTES);

    // when
    elementValidator.validate(fileMock, errors);

    // then
    assertEquals(1, errors.size());
    assertTrue(errors.containsKey("Header"));
    assertTrue(errors.containsValue("Invalid number of headers - [PRIMARYKEY, DESC, TITLE]"));
  }

  @Test
  void shouldValidateFileWithIncorrectHeaderValues() throws IOException {
    // given
    when(fileMock.getBytes()).thenReturn(FILE_WITH_INVALID_HEADER_VALUES_BYTES);

    // when
    elementValidator.validate(fileMock, errors);

    // then
    assertEquals(4, errors.size());
    assertTrue(errors.containsKey("Header DESCRIPTION"));
    assertTrue(errors.containsValue("DESCRIPTION has incorrect value TITLE"));
    assertTrue(errors.containsKey("Header UPDATED_TIMESTAMP"));
    assertTrue(errors.containsValue("UPDATED_TIMESTAMP has incorrect value UPDATED_TIME"));
    assertTrue(errors.containsKey("Header PRIMARY_KEY"));
    assertTrue(errors.containsValue("PRIMARY_KEY has incorrect value PRIMARYKEY"));
    assertTrue(errors.containsKey("Header NAME"));
    assertTrue(errors.containsValue("NAME has incorrect value DESC"));
  }

  @Test
  void shouldValidateFileWithIncorrectElementValues() throws IOException {
    // given
    when(fileMock.getBytes()).thenReturn(FILE_WITH_INVALID_ELEMENTS_BYTES);

    // when
    elementValidator.validate(fileMock, errors);

    // then
    assertEquals(5, errors.size());
    assertTrue(errors.containsKey("Element no. 1 name"));
    assertTrue(errors.containsValue(""));
    assertTrue(errors.containsKey("Element no. 2 description"));
    assertTrue(errors.containsValue(null));
    assertTrue(errors.containsKey("Element no. 3"));
    assertTrue(errors.containsValue("Invalid number of elements - [3, 2019-10-24T12:13:50]"));
    assertTrue(errors.containsKey("Element no. 4 primaryKey"));
    assertTrue(errors.containsKey("Element no. 5 updatedTimestamp"));
  }
}