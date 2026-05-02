package com.da.dg2526.api.models.dao;

import com.da.dg2526.api.models.entities.BookEntity;
import com.da.dg2526.api.models.entities.ReservationEntity;
import com.da.dg2526.api.models.entities.UserEntity;
import org.springframework.data.repository.CrudRepository;

public interface IReservationEntityDAO extends CrudRepository<ReservationEntity, Integer> {


    Integer countAllByBorrower(UserEntity user);

    Integer countAllByBookEntityAndLendingEntityIsNull(BookEntity book);

}

