package com.atg.catalog.api.v1.resources;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CatalogResource {

  private Long id;
  private String name;
  private String description;
  private String referenceId;
}
