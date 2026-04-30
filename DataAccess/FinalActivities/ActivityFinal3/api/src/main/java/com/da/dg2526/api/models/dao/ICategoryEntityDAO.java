package com.da.dg2526.api.models.dao;

import com.da.dg2526.api.models.entities.CategoryEntity;
import org.springframework.data.repository.CrudRepository;

public interface ICategoryEntityDAO extends CrudRepository<CategoryEntity, String> {
}
