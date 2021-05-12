package com.atg.catalog.service;

import com.atg.catalog.exception.CustomException;
import com.atg.catalog.model.CatalogRequest;
import com.atg.catalog.model.CatalogResponse;
import com.atg.catalog.model.ItemRequest;
import com.atg.catalog.model.ItemResponse;
import com.atg.catalog.repository.CatalogRepository;
import com.atg.catalog.repository.model.Catalog;
import com.atg.catalog.repository.model.Item;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
public class CatalogService {

  private CatalogRepository repository;

  public CatalogService(CatalogRepository repository) {
    this.repository = repository;
  }

  public CatalogResponse createCatalog(CatalogRequest request) {
    Catalog catalog = this.buildCatalog(request);
    catalog = repository.save(catalog);
    return buildCatalogResponse(catalog);
  }

  public Collection<CatalogResponse> getCatalogs() {
    List<CatalogResponse> result = new ArrayList<>();
    Iterable<Catalog> iterable = repository.findAll();
    iterable.forEach(catalog -> result.add(this.buildCatalogResponse(catalog)));
    return result;
  }

  public CatalogResponse getCatalogById(Long id) {
    Catalog catalog =
        repository
            .findById(id)
            .orElseThrow(() -> new CustomException("Item not found", HttpStatus.NOT_FOUND));
    return this.buildCatalogResponse(catalog);
  }

  public ItemResponse createItem(Long id, ItemRequest request) {
    Catalog catalog =
        repository
            .findById(id)
            .orElseThrow(() -> new CustomException("Item not found", HttpStatus.NOT_FOUND));

    catalog.getItems().add(this.buildItem(catalog, request));
    catalog = repository.save(catalog);

    Item item =
        catalog.getItems().stream()
            .filter(i -> i.getReferenceId().equals(request.getReferenceId()))
            .findFirst()
            .orElseThrow(() -> new CustomException("Something is wrong!"));

    return this.buildItemResponse(item);
  }

  private Catalog buildCatalog(CatalogRequest request) {
    Catalog catalog = new Catalog();
    catalog.setName(request.getName());
    catalog.setDescription(request.getDescription());
    catalog.setReferenceId(request.getReferenceId());
    return catalog;
  }

  private CatalogResponse buildCatalogResponse(Catalog catalog) {
    CatalogResponse response = new CatalogResponse();
    response.setName(catalog.getName());
    response.setDescription(catalog.getDescription());
    response.setReferenceId(catalog.getReferenceId());
    response.setId(catalog.getId());
    if (!CollectionUtils.isEmpty(catalog.getItems())) {
      response.setItems(
          catalog.getItems().stream().map(this::buildItemResponse).collect(Collectors.toList()));
    }
    return response;
  }

  private ItemResponse buildItemResponse(Item item) {
    ItemResponse response = new ItemResponse();
    response.setName(item.getName());
    response.setDescription(item.getDescription());
    response.setReferenceId(item.getReferenceId());
    response.setId(item.getId());
    response.setPrice(item.getPrice());
    return response;
  }

  private Item buildItem(Catalog catalog, ItemRequest request) {
    Item item = new Item();
    item.setName(request.getName());
    item.setDescription(request.getDescription());
    item.setReferenceId(request.getReferenceId());
    item.setPrice(request.getPrice());
    item.setCatalog(catalog);
    return item;
  }
}
