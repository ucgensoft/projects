package com.ucgen.letserasmus.library.reservation.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ucgen.common.exception.operation.OperationResultException;
import com.ucgen.common.operationresult.OperationResult;
import com.ucgen.letserasmus.library.common.enumeration.EnmEntityType;
import com.ucgen.letserasmus.library.log.dao.ILogDao;
import com.ucgen.letserasmus.library.log.enumeration.EnmTransaction;
import com.ucgen.letserasmus.library.log.model.TransactionLog;
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
	private ILogDao logDao;
	
	@Autowired
	public void setReservationDao(IReservationDao reservationDao) {
		this.reservationDao = reservationDao;
	}
	
	@Autowired
	public void setMessageDao(IMessageDao messageDao) {
		this.messageDao = messageDao;
	}

	@Autowired
	public void setLogDao(ILogDao logDao) {
		this.logDao = logDao;
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
			TransactionLog tLog = new TransactionLog();
			tLog.setUserId(reservation.getClientUserId());
			tLog.setOperationId(EnmTransaction.RESERVATION_SEND_INQUIRY.getId());
			tLog.setOperationDate(reservation.getCreatedDate());
			tLog.setEntityType(EnmEntityType.RESERVATION.getId());
			tLog.setEntityId(reservation.getId());
			
			tLog.setCreatedBy(reservation.getCreatedBy());
			tLog.setCreatedDate(reservation.getCreatedDate());
			
			OperationResult createLogResult = this.logDao.insertTransactionLog(tLog);
			if (OperationResult.isResultSucces(createLogResult)) {
				if (reservation.getMessageThread() != null) {
					MessageThread messageThread = reservation.getMessageThread();
					messageThread.setEntityId(reservation.getId());
					OperationResult updateMessageThreadResult = this.messageDao.updateMessageThread(reservation.getMessageThread());
					if (OperationResult.isResultSucces(updateMessageThreadResult)) {
						return createReservationResult;
					} else {
						throw new OperationResultException(updateMessageThreadResult);
					}
				} else {
					return createReservationResult;
				}
			} else {
				throw new OperationResultException(createLogResult);
			}
		} else {
			throw new OperationResultException(createReservationResult);
		}
	}

	@Override
	public List<Reservation> list(Reservation reservation, boolean placeFlag, boolean hostUserFlag, boolean clientUserFlag) {
		return this.reservationDao.list(reservation, placeFlag, hostUserFlag, clientUserFlag);
	}

	@Override
	@Transactional
	public OperationResult update(Reservation reservation, Message message, TransactionLog transactionLog) throws OperationResultException {
		OperationResult updateReservationResult = this.reservationDao.update(reservation, message);
		if (OperationResult.isResultSucces(updateReservationResult)) {
			if (message != null) {
				OperationResult insertMessageresult = this.messageDao.insertMessage(message);
				if (OperationResult.isResultSucces(insertMessageresult)) {
					OperationResult createLogResult = this.logDao.insertTransactionLog(transactionLog);
					if (OperationResult.isResultSucces(createLogResult)) {
						return updateReservationResult;
					} else {
						throw new OperationResultException(createLogResult);
					}
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