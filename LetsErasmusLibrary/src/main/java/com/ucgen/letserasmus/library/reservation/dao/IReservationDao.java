package com.ucgen.letserasmus.library.reservation.dao;

import java.util.List;

import com.ucgen.common.exception.operation.OperationResultException;
import com.ucgen.common.operationresult.OperationResult;
import com.ucgen.letserasmus.library.reservation.model.Reservation;

public interface IReservationDao {

	OperationResult insert(Reservation reservation);
	
	List<Reservation> list(Reservation reservation, boolean placeFlag, boolean hostUserFlag, boolean clientUserFlag);

	OperationResult update(Reservation reservation) throws OperationResultException;
	
}
