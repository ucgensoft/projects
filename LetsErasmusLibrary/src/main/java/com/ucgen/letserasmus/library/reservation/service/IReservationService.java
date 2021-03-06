package com.ucgen.letserasmus.library.reservation.service;

import java.util.List;

import com.ucgen.common.exception.operation.OperationResultException;
import com.ucgen.common.operationresult.OperationResult;
import com.ucgen.letserasmus.library.log.model.TransactionLog;
import com.ucgen.letserasmus.library.message.model.Message;
import com.ucgen.letserasmus.library.payment.model.PaymentMethod;
import com.ucgen.letserasmus.library.payment.model.PayoutMethod;
import com.ucgen.letserasmus.library.reservation.model.Reservation;
import com.ucgen.letserasmus.library.user.model.User;

public interface IReservationService {

	OperationResult insert(User user, Reservation reservation, PaymentMethod paymentMethod, PayoutMethod payoutMethod) throws OperationResultException;
	
	List<Reservation> list(Reservation reservation, boolean placeFlag, boolean hostUserFlag, boolean clientUserFlag);
	
	OperationResult update(Reservation reservation, Message message, TransactionLog transactionLog, Integer reservationOldStatus) throws OperationResultException;
	
}
