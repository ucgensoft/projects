package com.ucgen.letserasmus.library.reservation.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ucgen.common.exception.operation.OperationResultException;
import com.ucgen.common.operationresult.EnmResultCode;
import com.ucgen.common.operationresult.OperationResult;
import com.ucgen.common.operationresult.ValueOperationResult;
import com.ucgen.letserasmus.library.bluesnap.service.IExtPaymentService;
import com.ucgen.letserasmus.library.common.enumeration.EnmCurrency;
import com.ucgen.letserasmus.library.common.enumeration.EnmEntityType;
import com.ucgen.letserasmus.library.log.dao.ILogDao;
import com.ucgen.letserasmus.library.log.enumeration.EnmTransaction;
import com.ucgen.letserasmus.library.log.model.TransactionLog;
import com.ucgen.letserasmus.library.message.dao.IMessageDao;
import com.ucgen.letserasmus.library.message.model.Message;
import com.ucgen.letserasmus.library.message.model.MessageThread;
import com.ucgen.letserasmus.library.payment.dao.IPaymentDao;
import com.ucgen.letserasmus.library.payment.enumeration.EnmPaymentStatus;
import com.ucgen.letserasmus.library.payment.model.PaymentMethod;
import com.ucgen.letserasmus.library.payment.model.PayoutMethod;
import com.ucgen.letserasmus.library.reservation.dao.IReservationDao;
import com.ucgen.letserasmus.library.reservation.enumeration.EnmReservationStatus;
import com.ucgen.letserasmus.library.reservation.model.Reservation;
import com.ucgen.letserasmus.library.reservation.service.IReservationService;
import com.ucgen.letserasmus.library.simpleobject.service.ISimpleObjectService;

@Service
public class ReservationService implements IReservationService {

	private IReservationDao reservationDao;
	private IMessageDao messageDao;
	private ILogDao logDao;
	private IPaymentDao paymentDao;
	private IExtPaymentService extPaymentService;
	private ISimpleObjectService simpleObjectService;
	
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

	@Autowired
	public void setPaymentDao(IPaymentDao paymentDao) {
		this.paymentDao = paymentDao;
	}

	@Autowired
	public void setExtPaymentService(IExtPaymentService extPaymentService) {
		this.extPaymentService = extPaymentService;
	}

	@Autowired
	public void setSimpleObjectService(ISimpleObjectService simpleObjectService) {
		this.simpleObjectService = simpleObjectService;
	}

	@Override
	@Transactional
	public OperationResult insert(Long userId, Reservation reservation, PaymentMethod paymentMethod) throws OperationResultException {
		OperationResult operationResult = new OperationResult();
		Long hostUserId = reservation.getHostUserId();
		PayoutMethod payoutMethod = new PayoutMethod(hostUserId);
		
		payoutMethod = this.paymentDao.getPayoutMethod(payoutMethod);
		if (payoutMethod != null && payoutMethod.getBlueSnapVendorId() != null) {
			if (reservation.getMessageThread() != null) {
				MessageThread messageThread = reservation.getMessageThread();
				OperationResult createMessageThreadResult = this.messageDao.insertMessageThread(reservation.getMessageThread());
				if (OperationResult.isResultSucces(createMessageThreadResult)) {
					reservation.setMessageThreadId(messageThread.getId());
				} else {
					throw new OperationResultException(createMessageThreadResult);
				}
			}
			
			String transactionId = this.simpleObjectService.generateTransactionId();
			
			reservation.setTransactionId(transactionId);
			reservation.setPaymentStatus(EnmPaymentStatus.INITIAL.getId());
			
			paymentMethod.getPayment().setMerchantTransactionId(transactionId);
			
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
						if (!OperationResult.isResultSucces(updateMessageThreadResult)) {
							throw new OperationResultException(updateMessageThreadResult);
						}
					}
					
					// Send Inquiry 'de ödeme bilgileri girilmiyor.
					if (paymentMethod != null) {
						
						ValueOperationResult<String> sendPaymentResult = this.extPaymentService.paymentAuth(userId, paymentMethod, payoutMethod, 
								reservation.getCreatedBy());
						if (OperationResult.isResultSucces(sendPaymentResult)) {
							String blueSnapTransactionId = sendPaymentResult.getResultValue();
							reservation.setBlueSnapTransactionId(blueSnapTransactionId);
							reservation.setPaymentStatus(EnmPaymentStatus.AUTH.getId());
							this.reservationDao.update(reservation);
						} else {
							sendPaymentResult.setResultDesc("Payment information could not be sent to BlueSnap system. Error:" + OperationResult.getResultDesc(sendPaymentResult));
							throw new OperationResultException(sendPaymentResult);
						}
					}
					
					operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
				} else {
					throw new OperationResultException(createLogResult);
				}
			} else {
				throw new OperationResultException(createReservationResult);
			}
		} else {
			operationResult.setResultCode(EnmResultCode.ERROR.getValue());
			operationResult.setResultDesc("Host does not have a payment method! UserId: " + reservation.getHostUserId());
		}
		return operationResult;
	}

	@Override
	public List<Reservation> list(Reservation reservation, boolean placeFlag, boolean hostUserFlag, boolean clientUserFlag) {
		return this.reservationDao.list(reservation, placeFlag, hostUserFlag, clientUserFlag);
	}

	@Override
	@Transactional
	public OperationResult update(Reservation reservation, Message message, TransactionLog transactionLog, Integer reservationOldStatus) throws OperationResultException {
		if (reservation.getStatus() != null && reservationOldStatus != null 
				&& !reservation.getStatus().equals(reservationOldStatus)) {
			if ((reservation.getStatus().equals(EnmReservationStatus.DECLINED.getId()) 
					&& !reservationOldStatus.equals(EnmReservationStatus.DECLINED.getId()))
					|| (reservation.getStatus().equals(EnmReservationStatus.RECALLED.getId()) 
							&& !reservationOldStatus.equals(EnmReservationStatus.RECALLED.getId()))
					|| (reservation.getStatus().equals(EnmReservationStatus.EXPIRED.getId()) 
							&& !reservationOldStatus.equals(EnmReservationStatus.EXPIRED.getId()))) {
				reservation.setPaymentStatus(EnmPaymentStatus.AUTH_REVERSAL.getId());
			}
			
			if ((reservation.getStatus().equals(EnmReservationStatus.ACCEPTED.getId()) 
					&& !reservationOldStatus.equals(EnmReservationStatus.ACCEPTED.getId()))) {
				reservation.setPaymentStatus(EnmPaymentStatus.CAPTURE.getId());
			}
		}
		OperationResult updateReservationResult = this.reservationDao.update(reservation);
		if (OperationResult.isResultSucces(updateReservationResult)) {
			if (message != null) {
				OperationResult insertMessageresult = this.messageDao.insertMessage(message);
				if (OperationResult.isResultSucces(insertMessageresult)) {
					OperationResult createLogResult = this.logDao.insertTransactionLog(transactionLog);
					if (OperationResult.isResultSucces(createLogResult)) {
						if (reservation.getStatus() != null && reservationOldStatus != null 
								&& !reservation.getStatus().equals(reservationOldStatus)) {
							if (reservation.getStatus().equals(EnmReservationStatus.DECLINED.getId()) 
									&& !reservationOldStatus.equals(EnmReservationStatus.DECLINED.getId())) {
								OperationResult authPaymentReverseResult = this.extPaymentService.paymentAuthReversal(reservation.getClientUserId(), 
										reservation.getBlueSnapTransactionId(), reservation.getModifiedBy());
								if (!OperationResult.isResultSucces(authPaymentReverseResult)) {
									updateReservationResult.setResultDesc("Payment auth reversal failed(BlueSnap). Error:" + OperationResult.getResultDesc(authPaymentReverseResult));
									throw new OperationResultException(updateReservationResult);
								}
							}
							if (reservation.getStatus().equals(EnmReservationStatus.ACCEPTED.getId()) 
									&& !reservationOldStatus.equals(EnmReservationStatus.ACCEPTED.getId())) {
								OperationResult capturePaymentResult = this.extPaymentService.paymentCapture(reservation.getClientUserId(), 
										reservation.getBlueSnapTransactionId(), reservation.getModifiedBy());
								if (!OperationResult.isResultSucces(capturePaymentResult)) {
									updateReservationResult.setResultDesc("Payment capture operation failed(BlueSnap). Error:" + OperationResult.getResultDesc(capturePaymentResult));
									throw new OperationResultException(updateReservationResult);
								}
							}
						}
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