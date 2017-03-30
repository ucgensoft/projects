package com.ucgen.letserasmus.library.reservation.service;

import java.util.List;

import com.ucgen.common.exception.operation.OperationResultException;
import com.ucgen.common.operationresult.OperationResult;
import com.ucgen.letserasmus.library.message.model.Message;
import com.ucgen.letserasmus.library.reservation.model.Reservation;
import com.ucgen.letserasmus.library.transactionlog.model.TransactionLog;

public interface IReservationService {

	OperationResult insert(Reservation reservation) throws OperationResultException;
	
	List<Reservation> list(Reservation reservation, boolean placeFlag, boolean hostUserFlag, boolean clientUserFlag);
	
	OperationResult update(Reservation reservation, Message message, TransactionLog transactionLog) throws OperationResultException;
	
}
