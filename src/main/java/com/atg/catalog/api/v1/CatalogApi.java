package com.atg.catalog.api.v1;

import com.atg.catalog.api.v1.resources.CatalogResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CatalogApi {

  @GetMapping("/v1/catalogs/{id}")
  public CatalogResource getCatalogById(@PathVariable long id) {
    return new CatalogResource()
      .setId(id)
      .setName("Catalog Name")
      .setDescription("Catalog Description");
  }
}
