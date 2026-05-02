package com.da.dg2526.api.models.dao;

import com.da.dg2526.api.models.entities.BookEntity;
import com.da.dg2526.api.models.entities.LendingEntity;
import com.da.dg2526.api.models.entities.UserEntity;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;

public interface ILendingEntityDAO extends CrudRepository<LendingEntity, Integer> {

    Integer countAllByBookEntity(BookEntity book);

    LendingEntity findFirstByBookEntityAndBorrowerAndReturningdateIsNullOrderByIdDesc(BookEntity bookEntity, UserEntity borrower);

}
