package com.atg.catalog.restcontroller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.atg.catalog.exception.CustomException;
import com.atg.catalog.exception.CustomExceptionHandler;
import com.atg.catalog.model.CatalogRequest;
import com.atg.catalog.model.ItemRequest;
import com.atg.catalog.service.CatalogService;
import com.atg.catalog.utils.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
@DisplayName("Catalog Controller Test")
 class CatalogControllerTest {

  private final String path = Constants.API_ROOT_V1 + "/catalogs";
  private MockMvc mockMvc;
  @InjectMocks private CatalogController subject;
  @Mock private CatalogService catalogService;
  private HttpHeaders httpHeaders;
  private ObjectMapper mapper;

  @BeforeEach
  void setup() {
    MockitoAnnotations.initMocks(this);
    mockMvc =
        MockMvcBuilders.standaloneSetup(subject)
            .setControllerAdvice(CustomExceptionHandler.class)
            .build();
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    httpHeaders.setContentType(MediaType.APPLICATION_JSON);
    ReflectionTestUtils.setField(this, "httpHeaders", httpHeaders);
    ReflectionTestUtils.setField(this, "mapper", new ObjectMapper());
  }

  @Test
  void test_getCatalogById() throws Exception {

    // When
    MockHttpServletResponse response =
        mockMvc.perform(get(path + "/{id}", "1").headers(httpHeaders)).andReturn().getResponse();

    // Then
    assertThat(response).isNotNull();
    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
  }

  @Test
  void test_getCatalogById_notFound() throws Exception {
    //
    // Given
    //
    when(catalogService.getCatalogById(any()))
        .thenThrow(new CustomException("Error", HttpStatus.NOT_FOUND));

    // When
    MockHttpServletResponse response =
        mockMvc.perform(get(path + "/{id}", "1").headers(httpHeaders)).andReturn().getResponse();

    // Then
    assertThat(response).isNotNull();
    assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
  }

  @Test
  void test_createItem_noBody() throws Exception {

    // When
    MockHttpServletResponse response =
        mockMvc
            .perform(post(path + "/{id}/items", "1").headers(httpHeaders))
            .andReturn()
            .getResponse();

    // Then
    assertThat(response).isNotNull();
    assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
  }

  @Test
  void test_createItem_emptyBody() throws Exception {

    // When
    MockHttpServletResponse response =
        mockMvc
            .perform(
                post(path + "/{id}/items", "1")
                    .headers(httpHeaders)
                    .content(mapper.writeValueAsString(new ItemRequest())))
            .andReturn()
            .getResponse();

    // Then
    assertThat(response).isNotNull();
    assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
  }

  @Test
  void test_createItem() throws Exception {
    //
    // Given
    //
    String text = "test";

    // When
    MockHttpServletResponse response =
        mockMvc
            .perform(
                post(path + "/{id}/items", "1")
                    .headers(httpHeaders)
                    .content(
                        mapper.writeValueAsString(TestData.buildItemRequest(text, BigDecimal.TEN))))
            .andReturn()
            .getResponse();

    // Then
    assertThat(response).isNotNull();
    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
  }

  @Test
  void test_createItem_notFound() throws Exception {
    //
    // Given
    //
    String text = "test";
    when(catalogService.createItem(any(), any()))
        .thenThrow(new CustomException("Error", HttpStatus.NOT_FOUND));

    // When
    MockHttpServletResponse response =
        mockMvc
            .perform(
                post(path + "/{id}/items", "1")
                    .headers(httpHeaders)
                    .content(
                        mapper.writeValueAsString(TestData.buildItemRequest(text, BigDecimal.TEN))))
            .andReturn()
            .getResponse();

    // Then
    assertThat(response).isNotNull();
    assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
  }

  @Test
  void test_getCatalogs() throws Exception {

    // When
    MockHttpServletResponse response =
        mockMvc.perform(get(path).headers(httpHeaders)).andReturn().getResponse();

    // Then
    assertThat(response).isNotNull();
    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
  }

  @Test
  void test_createCatalog_noBody() throws Exception {

    // When
    MockHttpServletResponse response =
        mockMvc.perform(post(path).headers(httpHeaders)).andReturn().getResponse();

    // Then
    assertThat(response).isNotNull();
    assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
  }

  @Test
  void test_createCatalog_emptyBody() throws Exception {

    // When
    MockHttpServletResponse response =
        mockMvc
            .perform(
                post(path)
                    .headers(httpHeaders)
                    .content(mapper.writeValueAsString(new ItemRequest())))
            .andReturn()
            .getResponse();

    // Then
    assertThat(response).isNotNull();
    assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
  }

  @Test
  void test_createCatalog() throws Exception {
    //
    // Given
    //
    String text = "test";

    // When
    MockHttpServletResponse response =
        mockMvc
            .perform(
                post(path)
                    .headers(httpHeaders)
                    .content(mapper.writeValueAsString(TestData.buildCatalogRequest(text))))
            .andReturn()
            .getResponse();

    // Then
    assertThat(response).isNotNull();
    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
  }

  private static class TestData {
    public static ItemRequest buildItemRequest(String text, BigDecimal price) {
      ItemRequest request = new ItemRequest();
      request.setName(text);
      request.setDescription(text);
      request.setReferenceId(text);
      request.setPrice(price);
      return request;
    }

    public static CatalogRequest buildCatalogRequest(String text) {
      CatalogRequest request = new CatalogRequest();
      request.setName(text);
      request.setDescription(text);
      request.setReferenceId(text);
      return request;
    }
  }
}
