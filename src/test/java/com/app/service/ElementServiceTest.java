package com.app.service;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.app.model.Element;
import com.app.model.dto.ElementDto;
import com.app.repository.ElementRepository;

import javassist.NotFoundException;

import static com.app.data.MockDataForTests.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;

@ExtendWith(MockitoExtension.class)
class ElementServiceTest {

  @Mock
  private ElementRepository elementRepository;
  @Mock
  private MultipartFile fileMock;

  @InjectMocks
  private ElementService elementService;

  private final Element elementMock = createElement();

  @Test
  public void shouldThrowExceptionWhenFileDoesNotExist() {
    // when
    final IllegalArgumentException illegalArgumentException = assertThrows(
        IllegalArgumentException.class, () -> elementService.addElements(null)
    );

    // then
    assertEquals("File does not exist!", illegalArgumentException.getMessage());
  }

  @Test
  public void shouldThrowExceptionWhenUploadedFileIsEmpty() throws IOException {
    // given
    when(fileMock.getBytes()).thenReturn(new byte[]{});

    // when
    final IllegalArgumentException illegalArgumentException = assertThrows(
        IllegalArgumentException.class, () -> elementService.addElements(fileMock)
    );

    // then
    assertEquals("Uploaded file is empty!", illegalArgumentException.getMessage());
  }

  @Test
  public void shouldAddValidElements() throws IOException {
    // given
    byte[] bytes = {
        80, 82, 73, 77, 65, 82, 89, 95, 75, 69, 89, 44, 78, 65, 77, 69, 44, 68, 69, 83, 67, 82, 73,
        80, 84, 73, 79, 78, 44, 85, 80, 68, 65, 84, 69, 68, 95, 84, 73, 77, 69, 83, 84, 65, 77, 80,
        10, 49, 44, 102, 105, 114, 115, 116, 78, 97, 109, 101, 44, 102, 105, 114, 115, 116, 68, 101,
        115, 99, 114, 105, 112, 116, 105, 111, 110, 44, 50, 48, 49, 55, 45, 48, 56, 45, 48, 52, 84,
        49, 48, 58, 49, 49, 58, 51, 48, 10, 50, 44, 115, 101, 99, 111, 110, 100, 78, 97, 109, 101,
        44, 115, 101, 99, 111, 110, 100, 68, 101, 115, 99, 114, 105, 112, 116, 105, 111, 110, 44,
        50, 48, 49, 56, 45, 48, 57, 45, 49, 52, 84, 49, 49, 58, 49, 50, 58, 52, 48, 10, 51, 44, 116,
        104, 105, 114, 100, 78, 97, 109, 101, 44, 116, 104, 105, 114, 100, 68, 101, 115, 99, 114,
        105, 112, 116, 105, 111, 110, 44, 50, 48, 49, 57, 45, 49, 48, 45, 50, 52, 84, 49, 50, 58,
        49, 51, 58, 53, 48, 10
    };
    when(fileMock.getBytes()).thenReturn(bytes);

    // when
    final ResponseEntity<List<ElementDto>> response = elementService.addElements(fileMock);

    // then
    assertNotNull(response);
    assertEquals(OK, response.getStatusCode());

    List<ElementDto> elements = response.getBody();
    assertNotNull(elements);
    assertEquals(3, elements.size());

    ElementDto firstElement = elements.get(0);
    assertNotNull(firstElement);
    assertEquals(FIRST_ELEMENT_PRIMARY_KEY, firstElement.getPrimaryKey());
    assertEquals(FIRST_ELEMENT_NAME, firstElement.getName());
    assertEquals(FIRST_ELEMENT_DESCRIPTION, firstElement.getDescription());
    assertEquals(FIRST_ELEMENT_UPDATED_TIMESTAMP, firstElement.getUpdatedTimestamp());

    ElementDto secondElement = elements.get(1);
    assertNotNull(secondElement);
    assertEquals(SECOND_ELEMENT_PRIMARY_KEY, secondElement.getPrimaryKey());
    assertEquals(SECOND_ELEMENT_NAME, secondElement.getName());
    assertEquals(SECOND_ELEMENT_DESCRIPTION, secondElement.getDescription());
    assertEquals(SECOND_ELEMENT_UPDATED_TIMESTAMP, secondElement.getUpdatedTimestamp());

    ElementDto thirdElement = elements.get(2);
    assertNotNull(thirdElement);
    assertEquals(THIRD_ELEMENT_PRIMARY_KEY, thirdElement.getPrimaryKey());
    assertEquals(THIRD_ELEMENT_NAME, thirdElement.getName());
    assertEquals(THIRD_ELEMENT_DESCRIPTION, thirdElement.getDescription());
    assertEquals(THIRD_ELEMENT_UPDATED_TIMESTAMP, thirdElement.getUpdatedTimestamp());
  }

  @Test
  public void shouldFindFileWithExistingId() throws NotFoundException {
    // given
    when(elementRepository.findById(anyString())).thenReturn(Optional.ofNullable(elementMock));

    // when
    final ResponseEntity<ElementDto> response = elementService
        .findElementByPrimaryKey(ELEMENT_PRIMARY_KEY);

    // then
    assertNotNull(response);
    assertEquals(OK, response.getStatusCode());

    ElementDto elementDto = response.getBody();
    assertNotNull(elementDto);
    assertEquals(ELEMENT_PRIMARY_KEY, elementDto.getPrimaryKey());
    assertEquals(ELEMENT_NAME, elementDto.getName());
    assertEquals(ELEMENT_DESCRIPTION, elementDto.getDescription());
    assertEquals(ELEMENT_UPDATED_TIMESTAMP, elementDto.getUpdatedTimestamp());
  }

  @Test
  public void shouldThrowExceptionWhenElementWithGivenIdDoesNotExist() {
    // when + then
    final NotFoundException notFoundException = assertThrows(
        NotFoundException.class,
        () -> elementService.findElementByPrimaryKey(INCORRECT_ELEMENT_PRIMARY_KEY)
    );

    assertEquals(
        MessageFormat.format(
            "Element with primary key {0} does not exist", INCORRECT_ELEMENT_PRIMARY_KEY
        ),
        notFoundException.getMessage()
    );
  }

  @Test
  public void shouldDeleteFileWithExistingId() throws NotFoundException {
    // given
    doNothing().when(elementRepository).deleteById(anyString());

    // when
    final ResponseEntity<String> response = elementService
        .deleteElementByPrimaryKey(ELEMENT_PRIMARY_KEY);

    // then
    assertNotNull(response);
    assertEquals(OK, response.getStatusCode());
    assertEquals(
        "Successfully removed element with primary key " + ELEMENT_PRIMARY_KEY,
        response.getBody()
    );
  }

  @Test
  public void shouldThrowExceptionWhenElementHasNotBeenSuccessfullyDeleted() {
    // given
    when(elementRepository.findById(anyString()))
        .thenReturn(Optional.ofNullable(elementMock));

    // when + then
    final NotFoundException notFoundException = assertThrows(
        NotFoundException.class,
        () -> elementService.deleteElementByPrimaryKey(ELEMENT_PRIMARY_KEY)
    );

    assertEquals(
        "Failed to delete element with primary key " + ELEMENT_PRIMARY_KEY,
        notFoundException.getMessage()
    );
  }
}