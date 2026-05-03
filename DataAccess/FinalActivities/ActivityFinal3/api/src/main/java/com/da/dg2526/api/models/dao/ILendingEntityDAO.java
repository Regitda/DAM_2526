package com.da.dg2526.api.models.dao;

import com.da.dg2526.api.models.entities.BookEntity;
import com.da.dg2526.api.models.entities.LendingEntity;
import com.da.dg2526.api.models.entities.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ILendingEntityDAO extends CrudRepository<LendingEntity, Integer> {

    Integer countAllByBookEntityAndReturningdateIsNull(BookEntity book);

    Integer countAllByBorrowerAndReturningdateIsNull(UserEntity user);

    Optional<LendingEntity> findFirstByBookEntityAndBorrowerAndReturningdateIsNullOrderByIdDesc(BookEntity bookEntity, UserEntity borrower);


}
