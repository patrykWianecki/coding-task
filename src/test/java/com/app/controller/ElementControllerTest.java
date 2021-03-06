package com.app.controller;

import java.util.List;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.app.data.MockDataForTests;
import com.app.model.ElementsResponse;
import com.app.model.dto.ElementDto;
import com.app.service.ElementService;
import com.app.validator.ElementValidator;
import com.fasterxml.jackson.databind.ObjectMapper;

import static com.app.data.MockDataForTests.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
class ElementControllerTest {

  private static final String ELEMENTS_MAPPING = "/elements";
  private static final String PRIMARY_KEY = "primaryKey";

  @MockBean
  private ElementService elementService;
  @MockBean
  private ElementValidator elementValidator;

  @Autowired
  private MockMvc mockMvc;

  private final ElementDto elementDto = MockDataForTests.createElementDto();

  @Test
  void shouldAddElements() throws Exception {
    // given
    ElementsResponse elementsResponse = ElementsResponse.builder()
        .elements(List.of(elementDto))
        .build();
    when(elementService.addElements(any())).thenReturn(ResponseEntity.ok(elementsResponse));
    doNothing().when(elementValidator).validate(any(), anyMap());

    // when
    mockMvc
        .perform(
            post(ELEMENTS_MAPPING)
                .content(toJson(elementDto))
                .contentType(APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.handler().methodName("addElements"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andReturn();

    // then
    verify(elementService, times(1)).addElements(any());
  }

  @Test
  void shouldFindElementWithPrimaryKey() throws Exception {
    // given
    when(elementService.findElementByPrimaryKey(anyString()))
        .thenReturn(ResponseEntity.ok(elementDto));

    // when
    mockMvc
        .perform(
            get(ELEMENTS_MAPPING)
                .queryParam(PRIMARY_KEY, ELEMENT_PRIMARY_KEY)
                .content(toJson(elementDto))
                .contentType(APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.handler().methodName("findElementByPrimaryKey"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(jsonPath("$.primaryKey", Matchers.equalTo(ELEMENT_PRIMARY_KEY)))
        .andExpect(jsonPath("$.name", Matchers.equalTo(ELEMENT_NAME)))
        .andExpect(jsonPath("$.description", Matchers.equalTo(ELEMENT_DESCRIPTION)))
        .andExpect(
            jsonPath("$.updatedTimestamp", Matchers.is(ELEMENT_UPDATED_TIMESTAMP.toString()))
        )
        .andReturn();

    // then
    verify(elementService, times(1)).findElementByPrimaryKey(anyString());
  }

  @Test
  void shouldDeleteElementWithPrimaryKey() throws Exception {
    // given
    when(elementService.deleteElementByPrimaryKey(anyString())).thenReturn(ResponseEntity.ok(""));

    // when
    mockMvc
        .perform(
            delete(ELEMENTS_MAPPING)
                .queryParam(PRIMARY_KEY, ELEMENT_PRIMARY_KEY)
                .content(toJson(elementDto))
                .contentType(APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andExpect(handler().methodName("deleteElementByPrimaryKey"))
        .andExpect(status().isOk())
        .andReturn();

    // then
    verify(elementService, times(1)).deleteElementByPrimaryKey(anyString());
  }

  private static String toJson(ElementDto elementDto) {
    try {
      return new ObjectMapper().writeValueAsString(elementDto);
    } catch (Exception e) {
      throw new IllegalStateException("Failed to create json object");
    }
  }
}