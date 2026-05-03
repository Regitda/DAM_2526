package com.da.dg2526.api.models.dao;

import com.da.dg2526.api.models.entities.BookEntity;
import com.da.dg2526.api.models.entities.ReservationEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IReservationEntityDAO extends CrudRepository<ReservationEntity, Integer> {

    Integer countAllByBookEntityAndLendingEntityIsNull(BookEntity book);

    Optional<ReservationEntity> findFirstByBookEntityAndLendingEntityIsNullOrderByDateAsc(BookEntity book);

}

