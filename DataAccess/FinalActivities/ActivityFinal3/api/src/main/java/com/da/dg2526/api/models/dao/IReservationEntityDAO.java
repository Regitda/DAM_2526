package com.da.dg2526.api.models.dao;

import com.da.dg2526.api.models.entities.ReservationEntity;
import org.springframework.data.repository.CrudRepository;

public interface IReservationEntityDAO extends CrudRepository<ReservationEntity, Integer> {
}
