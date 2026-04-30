package com.da.dg2526.api.models.dao;

import com.da.dg2526.api.models.entities.LendingEntity;
import org.springframework.data.repository.CrudRepository;

public interface ILendingEntityDAO extends CrudRepository<LendingEntity, Integer> {
}
