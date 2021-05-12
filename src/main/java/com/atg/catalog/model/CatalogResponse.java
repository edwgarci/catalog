package com.atg.catalog.model;

import java.util.Collection;

public class CatalogResponse extends Catalog {
  private Long id;
  private Collection<ItemResponse> items;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Collection<ItemResponse> getItems() {
    return items;
  }

  public void setItems(Collection<ItemResponse> items) {
    this.items = items;
  }
}
