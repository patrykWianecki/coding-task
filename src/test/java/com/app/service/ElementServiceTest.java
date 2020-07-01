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
import com.app.model.ElementsResponse;
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
  public void shouldAddValidElements() throws IOException {
    // given
    when(fileMock.getBytes()).thenReturn(VALID_FILE_BYTES);

    // when
    final ResponseEntity<ElementsResponse> response = elementService.addElements(fileMock);

    // then
    assertNotNull(response);
    assertEquals(OK, response.getStatusCode());

    ElementsResponse elementsResponse = response.getBody();
    assertNotNull(elementsResponse);

    assertNull(elementsResponse.getErrors());

    List<ElementDto> elements = elementsResponse.getElements();
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
  public void shouldAddValidElementsAndSkipInvalid() throws IOException {
    // given
    when(fileMock.getBytes()).thenReturn(MIXED_FILE_BYTES);

    // when
    final ResponseEntity<ElementsResponse> response = elementService.addElements(fileMock);

    // then
    assertNotNull(response);
    assertEquals(OK, response.getStatusCode());

    ElementsResponse elementsResponse = response.getBody();
    assertNotNull(elementsResponse);

    assertNull(elementsResponse.getErrors());

    List<ElementDto> elements = elementsResponse.getElements();
    assertNotNull(elements);
    assertEquals(1, elements.size());

    ElementDto firstElement = elements.get(0);
    assertNotNull(firstElement);
    assertEquals(FIRST_ELEMENT_PRIMARY_KEY, firstElement.getPrimaryKey());
    assertEquals(FIRST_ELEMENT_NAME, firstElement.getName());
    assertEquals(FIRST_ELEMENT_DESCRIPTION, firstElement.getDescription());
    assertEquals(FIRST_ELEMENT_UPDATED_TIMESTAMP, firstElement.getUpdatedTimestamp());
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