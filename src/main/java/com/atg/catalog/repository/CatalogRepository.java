package com.atg.catalog.repository;

import com.atg.catalog.repository.model.Catalog;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CatalogRepository extends CrudRepository<Catalog, Long> {}
