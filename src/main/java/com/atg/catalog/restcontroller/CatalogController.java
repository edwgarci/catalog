package com.atg.catalog.restcontroller;

import com.atg.catalog.model.CatalogRequest;
import com.atg.catalog.model.CatalogResponse;
import com.atg.catalog.model.ItemRequest;
import com.atg.catalog.model.ItemResponse;
import com.atg.catalog.service.CatalogService;
import com.atg.catalog.utils.Constants;
import java.util.Collection;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constants.API_ROOT_V1 + "/catalogs")
public class CatalogController {

  private CatalogService service;

  public CatalogController(CatalogService service) {
    this.service = service;
  }

  @GetMapping("/{id}")
  public CatalogResponse getCatalogById(@PathVariable Long id) {
    return service.getCatalogById(id);
  }

  @PostMapping("/{id}/items")
  public ItemResponse createItem(@PathVariable Long id, @Valid @RequestBody ItemRequest request) {
    return service.createItem(id, request);
  }

  @GetMapping
  public Collection<CatalogResponse> getCatalogs() {
    return service.getCatalogs();
  }

  @PostMapping
  public CatalogResponse createCatalog(@Valid @RequestBody CatalogRequest request) {
    return service.createCatalog(request);
  }
}
