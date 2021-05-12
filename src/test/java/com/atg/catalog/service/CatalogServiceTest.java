package com.atg.catalog.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.atg.catalog.exception.CustomException;
import com.atg.catalog.model.CatalogRequest;
import com.atg.catalog.model.CatalogResponse;
import com.atg.catalog.model.ItemRequest;
import com.atg.catalog.model.ItemResponse;
import com.atg.catalog.repository.CatalogRepository;
import com.atg.catalog.repository.model.Catalog;
import com.atg.catalog.repository.model.Item;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("Catalog Service Test")
class CatalogServiceTest {

  @InjectMocks private CatalogService subject;
  @Mock private CatalogRepository repository;

  @Test
  void test_createCatalog_exception() {
    //
    // Given
    //
    String text = "test01";
    CatalogRequest request = TestData.buildCatalogRequest(text);
    when(repository.save(any())).thenThrow(new NullPointerException("NPE"));

    // When / Then
    Assertions.assertThrows(NullPointerException.class, () -> subject.createCatalog(request));
  }

  @Test
  void test_createCatalog() {
    //
    // Given
    //
    String text = "test01";
    CatalogRequest request = TestData.buildCatalogRequest(text);
    when(repository.save(any())).thenReturn(TestData.buildCatalog(text));

    // When
    CatalogResponse response = subject.createCatalog(request);

    // Then
    assertThat(response).isNotNull();
    assertThat(response.getId()).isEqualTo(1L);
    assertThat(response.getName()).isEqualTo(text);
    assertThat(response.getDescription()).isEqualTo(text);
    assertThat(response.getReferenceId()).isEqualTo(text);
  }

  @Test
  void test_getCatalogs() {
    //
    // Given
    //
    String text = "test01";
    when(repository.findAll()).thenReturn(Collections.singletonList(TestData.buildCatalog(text)));

    // When
    Collection<CatalogResponse> response = subject.getCatalogs();

    // Then
    assertThat(response).isNotNull().hasSize(1);
  }

  @Test
  void test_getCatalogById_notFound() {
    //
    // Given
    //
    when(repository.findById(any())).thenReturn(Optional.empty());

    // When / Then
    Assertions.assertThrows(CustomException.class, () -> subject.getCatalogById(1L));
  }

  @Test
  void test_getCatalogById() {
    //
    // Given
    //
    String text = "test01";
    when(repository.findById(any())).thenReturn(Optional.of(TestData.buildCatalog(text)));

    // When
    CatalogResponse response = subject.getCatalogById(1L);

    // Then
    assertThat(response).isNotNull();
    assertThat(response.getId()).isEqualTo(1L);
    assertThat(response.getName()).isEqualTo(text);
    assertThat(response.getDescription()).isEqualTo(text);
    assertThat(response.getReferenceId()).isEqualTo(text);
  }

  @Test
  void test_createItem_notFound() {
    //
    // Given
    //
    String text = "test01";
    ItemRequest request = TestData.buildItemRequest(text, BigDecimal.TEN);
    when(repository.findById(any())).thenReturn(Optional.empty());

    // When / Then
    Assertions.assertThrows(CustomException.class, () -> subject.createItem(1L, request));
  }

  @Test
  void test_createItem_somethingWrong() {
    //
    // Given
    //
    String text = "test01";
    ItemRequest request = TestData.buildItemRequest(text, BigDecimal.valueOf(56));
    when(repository.findById(any())).thenReturn(Optional.of(TestData.buildCatalog(text)));
    when(repository.save(any())).thenReturn(TestData.buildCatalog(text));

    // When / Then
    Assertions.assertThrows(CustomException.class, () -> subject.createItem(1L, request));
  }

  @Test
  void test_createItem() {
    //
    // Given
    //
    String text = "test01";
    ItemRequest request = TestData.buildItemRequest(text, BigDecimal.valueOf(56));
    when(repository.findById(any())).thenReturn(Optional.of(TestData.buildCatalog(text)));
    when(repository.save(any()))
        .thenReturn(TestData.buildCatalogWithItem(text, BigDecimal.valueOf(56)));

    // When
    ItemResponse response = subject.createItem(1L, request);

    // Then
    assertThat(response).isNotNull();
    assertThat(response.getId()).isEqualTo(1L);
    assertThat(response.getName()).isEqualTo(text);
    assertThat(response.getDescription()).isEqualTo(text);
    assertThat(response.getReferenceId()).isEqualTo(text);
    assertThat(response.getPrice()).isEqualTo(BigDecimal.valueOf(56));
  }

  private static class TestData {

    public static CatalogRequest buildCatalogRequest(String text) {
      CatalogRequest request = new CatalogRequest();
      request.setName(text);
      request.setDescription(text);
      request.setReferenceId(text);
      return request;
    }

    public static Catalog buildCatalog(String text) {
      Catalog catalog = new Catalog();
      catalog.setName(text);
      catalog.setDescription(text);
      catalog.setReferenceId(text);
      catalog.setId(1L);
      return catalog;
    }

    public static ItemRequest buildItemRequest(String text, BigDecimal price) {
      ItemRequest request = new ItemRequest();
      request.setName(text);
      request.setDescription(text);
      request.setReferenceId(text);
      request.setPrice(price);
      return request;
    }

    public static Object buildCatalogWithItem(String text, BigDecimal value) {
      Catalog catalog = buildCatalog(text);
      catalog.getItems().add(buildItem(text, value));
      return catalog;
    }

    private static Item buildItem(String text, BigDecimal value) {
      Item item = new Item();
      item.setName(text);
      item.setDescription(text);
      item.setReferenceId(text);
      item.setPrice(value);
      item.setId(1L);
      return item;
    }
  }
}
