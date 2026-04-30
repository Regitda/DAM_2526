package com.da.dg2526.api.models.dao;

import com.da.dg2526.api.models.entities.UserEntity;
import org.springframework.data.repository.CrudRepository;

public interface IUserEntityDAO extends CrudRepository<UserEntity, String> {
}
