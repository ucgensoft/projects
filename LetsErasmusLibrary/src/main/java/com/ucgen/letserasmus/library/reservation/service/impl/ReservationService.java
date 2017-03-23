package com.ucgen.letserasmus.library.reservation.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ucgen.common.exception.operation.OperationResultException;
import com.ucgen.common.operationresult.OperationResult;
import com.ucgen.letserasmus.library.message.dao.IMessageDao;
import com.ucgen.letserasmus.library.message.model.Message;
import com.ucgen.letserasmus.library.message.model.MessageThread;
import com.ucgen.letserasmus.library.reservation.dao.IReservationDao;
import com.ucgen.letserasmus.library.reservation.model.Reservation;
import com.ucgen.letserasmus.library.reservation.service.IReservationService;

@Service
public class ReservationService implements IReservationService {

	private IReservationDao reservationDao;
	private IMessageDao messageDao;
	
	public ReservationService() {
		String a = "";
		System.out.println(a);
	}
	
	@Autowired
	public void setReservationDao(IReservationDao reservationDao) {
		this.reservationDao = reservationDao;
	}
	
	@Autowired
	public void setMessageDao(IMessageDao messageDao) {
		this.messageDao = messageDao;
	}

	@Override
	@Transactional
	public OperationResult insert(Reservation reservation) throws OperationResultException {
		if (reservation.getMessageThread() != null) {
			MessageThread messageThread = reservation.getMessageThread();
			OperationResult createMessageThreadResult = this.messageDao.insertMessageThread(reservation.getMessageThread());
			if (OperationResult.isResultSucces(createMessageThreadResult)) {
				reservation.setMessageThreadId(messageThread.getId());
			} else {
				throw new OperationResultException(createMessageThreadResult);
			}
		}
		
		OperationResult createReservationResult = this.reservationDao.insert(reservation);
		if (OperationResult.isResultSucces(createReservationResult)) {
			return createReservationResult;
		} else {
			throw new OperationResultException(createReservationResult);
		}
	}

	@Override
	public List<Reservation> list(Reservation reservation, boolean placeFlag, boolean hostUserFlag, boolean clientUserFlag) {
		return this.reservationDao.list(reservation, placeFlag, hostUserFlag, clientUserFlag);
	}

	@Override
	public OperationResult update(Reservation reservation, Message message) throws OperationResultException {
		OperationResult updateReservationResult = this.reservationDao.update(reservation, message);
		if (OperationResult.isResultSucces(updateReservationResult)) {
			if (message != null) {
				OperationResult insertMessageresult = this.messageDao.insertMessage(message);
				if (OperationResult.isResultSucces(insertMessageresult)) {
					return updateReservationResult;
				} else {
					throw new OperationResultException(insertMessageresult);
				}
			} else {
				return updateReservationResult;
			}
		} else {
			return updateReservationResult;
		}
	}

}