package com.da.dg2526.api.models.dao;

import com.da.dg2526.api.models.entities.BookEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IBookEntityDAO extends CrudRepository<BookEntity,String> {}
