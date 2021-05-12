package com.atg.catalog.model;

import java.math.BigDecimal;
import javax.validation.constraints.NotNull;

public class Item {
  @NotNull private String name;
  private String description;
  @NotNull private String referenceId;
  @NotNull private BigDecimal price;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getReferenceId() {
    return referenceId;
  }

  public void setReferenceId(String referenceId) {
    this.referenceId = referenceId;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }
}
