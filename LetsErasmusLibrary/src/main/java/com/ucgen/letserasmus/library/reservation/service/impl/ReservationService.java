package com.ucgen.letserasmus.library.reservation.service.impl;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ucgen.common.exception.operation.OperationResultException;
import com.ucgen.common.operationresult.EnmResultCode;
import com.ucgen.common.operationresult.OperationResult;
import com.ucgen.common.operationresult.ValueOperationResult;
import com.ucgen.common.util.DateUtil;
import com.ucgen.letserasmus.library.bluesnap.service.IExtPaymentService;
import com.ucgen.letserasmus.library.common.enumeration.EnmEntityType;
import com.ucgen.letserasmus.library.log.dao.ILogDao;
import com.ucgen.letserasmus.library.log.enumeration.EnmExternalSystem;
import com.ucgen.letserasmus.library.log.enumeration.EnmTransaction;
import com.ucgen.letserasmus.library.log.model.TransactionLog;
import com.ucgen.letserasmus.library.mail.service.IMailService;
import com.ucgen.letserasmus.library.message.dao.IMessageDao;
import com.ucgen.letserasmus.library.message.model.Message;
import com.ucgen.letserasmus.library.message.model.MessageThread;
import com.ucgen.letserasmus.library.payment.enumeration.EnmPaymentStatus;
import com.ucgen.letserasmus.library.payment.model.PaymentMethod;
import com.ucgen.letserasmus.library.payment.model.PayoutMethod;
import com.ucgen.letserasmus.library.payment.service.IPaymentService;
import com.ucgen.letserasmus.library.place.enumeration.EnmHomeType;
import com.ucgen.letserasmus.library.place.enumeration.EnmPlaceType;
import com.ucgen.letserasmus.library.place.model.Place;
import com.ucgen.letserasmus.library.reservation.dao.IReservationDao;
import com.ucgen.letserasmus.library.reservation.enumeration.EnmReservationStatus;
import com.ucgen.letserasmus.library.reservation.model.Reservation;
import com.ucgen.letserasmus.library.reservation.service.IReservationService;
import com.ucgen.letserasmus.library.simpleobject.model.CancelPolicyRule;
import com.ucgen.letserasmus.library.simpleobject.service.ISimpleObjectService;
import com.ucgen.letserasmus.library.stripe.service.IStripePaymentService;
import com.ucgen.letserasmus.library.user.model.User;
import com.ucgen.letserasmus.library.user.service.IUserService;

@Service
public class ReservationService implements IReservationService {

	private IReservationDao reservationDao;
	private IMessageDao messageDao;
	private ILogDao logDao;
	private IExtPaymentService extPaymentService;
	private ISimpleObjectService simpleObjectService;
	private IStripePaymentService stripePaymentService;
	private IPaymentService paymentService;
	private IMailService mailService;
	private IUserService userService;
	
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
	public void setExtPaymentService(IExtPaymentService extPaymentService) {
		this.extPaymentService = extPaymentService;
	}

	@Autowired
	public void setSimpleObjectService(ISimpleObjectService simpleObjectService) {
		this.simpleObjectService = simpleObjectService;
	}

	@Autowired
	public void setStripePaymentService(IStripePaymentService stripePaymentService) {
		this.stripePaymentService = stripePaymentService;
	}

	@Autowired
	public void setPaymentService(IPaymentService paymentService) {
		this.paymentService = paymentService;
	}
	
	@Autowired
	public void setMailService(IMailService mailService) {
		this.mailService = mailService;
	}

	@Autowired
	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	@Override
	@Transactional
	public OperationResult insert(User user, Reservation reservation, PaymentMethod paymentMethod, PayoutMethod payoutMethod) throws OperationResultException {
		OperationResult operationResult = new OperationResult();
		if (reservation.getMessageThread() != null) {
			MessageThread messageThread = reservation.getMessageThread();
			OperationResult createMessageThreadResult = this.messageDao.insertMessageThread(reservation.getMessageThread());
			if (OperationResult.isResultSucces(createMessageThreadResult)) {
				reservation.setMessageThreadId(messageThread.getId());
			} else {
				throw new OperationResultException(createMessageThreadResult);
			}
		}
		
		if (!reservation.getStatus().equals(EnmReservationStatus.INQUIRY.getId())) {
			String transactionId = this.simpleObjectService.generateTransactionId();
			reservation.setTransactionId(transactionId);
			reservation.setPaymentStatus(EnmPaymentStatus.INITIAL.getId());
			paymentMethod.getPayment().setMerchantTransactionId(transactionId);
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
					if (!OperationResult.isResultSucces(updateMessageThreadResult)) {
						throw new OperationResultException(updateMessageThreadResult);
					}
				}
				
				// Send Inquiry 'de ödeme bilgileri girilmiyor.
				if (!reservation.getStatus().equals(EnmReservationStatus.INQUIRY.getId())) {
					
					ValueOperationResult<String> sendPaymentResult = null;
					
					if (payoutMethod.getExternalSystemId().equals(EnmExternalSystem.BLUESNAP.getId())) {
						sendPaymentResult = this.extPaymentService.paymentAuth(user.getId(), paymentMethod, payoutMethod, reservation.getCreatedBy());
					} else if (payoutMethod.getExternalSystemId().equals(EnmExternalSystem.STRIPE.getId())) {
						sendPaymentResult = this.stripePaymentService.charge(payoutMethod, paymentMethod, reservation.getCreatedBy());
					}
							
					if (OperationResult.isResultSucces(sendPaymentResult)) {
						String paymentTransactionId = sendPaymentResult.getResultValue();
						reservation.setPaymentTransactionId(paymentTransactionId);
						reservation.setPaymentStatus(EnmPaymentStatus.AUTH.getId());
						this.reservationDao.update(reservation);
						
						Place place = reservation.getPlace();
						
						this.mailService.sendBokingRequestMail(user.getEmail(), place.getUser().getEmail(), place.getTitle(), place.getUrl(), 
								place.getCoverPhotoUrl(), EnmHomeType.getHomeType(place.getHomeTypeId()).getText(), EnmPlaceType.getPlaceType(place.getPlaceTypeId()).getText(), 
								reservation.getStartDate(), reservation.getEndDate());
						
					} else {
						sendPaymentResult.setResultDesc("Payment information could not be sent to BlueSnap system. Error:" + OperationResult.getResultDesc(sendPaymentResult));
						throw new OperationResultException(sendPaymentResult);
					}
				} else {
					User hostUser = this.userService.getUser(new User(reservation.getHostUserId()));
					this.mailService.sendNewMessageMail(hostUser.getEmail(), reservation.getMessageThread().getThreadTitle(), reservation.getMessageThread().getMessageList().get(0).getMessageText());
				}
				
				operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
			} else {
				throw new OperationResultException(createLogResult);
			}
		} else {
			throw new OperationResultException(createReservationResult);
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
			
			if ((reservation.getStatus().equals(EnmReservationStatus.HOST_CANCELLED.getId()) && !reservationOldStatus.equals(EnmReservationStatus.HOST_CANCELLED.getId()))
					|| (reservation.getStatus().equals(EnmReservationStatus.CLIENT_CANCELLED.getId()) && !reservationOldStatus.equals(EnmReservationStatus.CLIENT_CANCELLED.getId()))) {
				reservation.setPaymentStatus(EnmPaymentStatus.REFUND.getId());
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
							PayoutMethod hostPayoutMethod = this.paymentService.getPayoutMethod(new PayoutMethod(reservation.getHostUserId()));
							if ((reservation.getStatus().equals(EnmReservationStatus.DECLINED.getId()) && !reservationOldStatus.equals(EnmReservationStatus.DECLINED.getId()))
									|| (reservation.getStatus().equals(EnmReservationStatus.EXPIRED.getId()) && !reservationOldStatus.equals(EnmReservationStatus.EXPIRED.getId()))
									|| (reservation.getStatus().equals(EnmReservationStatus.RECALLED) && !reservationOldStatus.equals(EnmReservationStatus.RECALLED.getId()))) {
								OperationResult authPaymentReverseResult = null;
								if (hostPayoutMethod.getExternalSystemId().equals(EnmExternalSystem.BLUESNAP.getId())) {
									authPaymentReverseResult = this.extPaymentService.paymentAuthReversal(reservation.getClientUserId(), 
											reservation.getPaymentTransactionId(), reservation.getModifiedBy());
								} else if (hostPayoutMethod.getExternalSystemId().equals(EnmExternalSystem.STRIPE.getId())) {
									authPaymentReverseResult = this.stripePaymentService.refund(reservation.getClientUserId(), reservation.getPaymentTransactionId(), null, null, reservation.getModifiedBy());
								}
								if (!OperationResult.isResultSucces(authPaymentReverseResult)) {
									updateReservationResult.setResultDesc("Payment auth reversal failed. Error:" + OperationResult.getResultDesc(authPaymentReverseResult));
									throw new OperationResultException(updateReservationResult);
								}
							}
							if (reservation.getStatus().equals(EnmReservationStatus.ACCEPTED.getId()) 
									&& !reservationOldStatus.equals(EnmReservationStatus.ACCEPTED.getId())) {
								OperationResult capturePaymentResult = null;
								if (hostPayoutMethod.getExternalSystemId().equals(EnmExternalSystem.BLUESNAP.getId())) {
									capturePaymentResult = this.extPaymentService.paymentCapture(reservation.getClientUserId(), 
											reservation.getPaymentTransactionId(), reservation.getModifiedBy());
								} else if (hostPayoutMethod.getExternalSystemId().equals(EnmExternalSystem.STRIPE.getId())) {
									capturePaymentResult = this.stripePaymentService.capture(reservation.getClientUserId(), reservation.getPaymentTransactionId(), reservation.getModifiedBy());
								}
								if (!OperationResult.isResultSucces(capturePaymentResult)) {
									updateReservationResult.setResultDesc("Payment capture operation failed. Error:" + OperationResult.getResultDesc(capturePaymentResult));
									throw new OperationResultException(updateReservationResult);
								}
							}
							if (reservation.getStatus().equals(EnmReservationStatus.HOST_CANCELLED) && !reservationOldStatus.equals(EnmReservationStatus.HOST_CANCELLED.getId())) {
								OperationResult authPaymentReverseResult = null;
								if (hostPayoutMethod.getExternalSystemId().equals(EnmExternalSystem.BLUESNAP.getId())) {
									authPaymentReverseResult = this.extPaymentService.refund(reservation.getClientUserId(), 
											reservation.getPaymentTransactionId(), null, null, reservation.getModifiedBy());
								} else if (hostPayoutMethod.getExternalSystemId().equals(EnmExternalSystem.STRIPE.getId())) {
									authPaymentReverseResult = this.stripePaymentService.refund(reservation.getClientUserId(), reservation.getPaymentTransactionId(), null, null, reservation.getModifiedBy());
								}
								if (!OperationResult.isResultSucces(authPaymentReverseResult)) {
									updateReservationResult.setResultDesc("Payment auth reversal failed. Error:" + OperationResult.getResultDesc(authPaymentReverseResult));
									throw new OperationResultException(updateReservationResult);
								}
							}
							if (reservation.getStatus().equals(EnmReservationStatus.CLIENT_CANCELLED) && !reservationOldStatus.equals(EnmReservationStatus.CLIENT_CANCELLED.getId())) {
								Long resRemainingDays = DateUtil.dateDiff(reservation.getStartDate(), new Date(), Calendar.DATE);
								TreeMap<Integer, CancelPolicyRule> entityTypeCancelRuleMap = this.simpleObjectService.listCancelPolicyRule(EnmEntityType.RESERVATION.getId());
								CancelPolicyRule cancelPolicyRule = null;
								for (Integer ruleRemainingDays : entityTypeCancelRuleMap.keySet()) {
									cancelPolicyRule = entityTypeCancelRuleMap.get(ruleRemainingDays);
									if (ruleRemainingDays > resRemainingDays) {
										break;
									}
								}
								
								BigDecimal clientRefundAmount = reservation.getPlacePrice().multiply(cancelPolicyRule.getRefundRate());
								BigDecimal vendorAmount = reservation.getPlacePrice().subtract(clientRefundAmount).subtract(reservation.getCommissionFee());
								
								OperationResult authPaymentReverseResult = null;
								if (hostPayoutMethod.getExternalSystemId().equals(EnmExternalSystem.BLUESNAP.getId())) {
									authPaymentReverseResult = this.extPaymentService.refund(reservation.getClientUserId(), 
											reservation.getPaymentTransactionId(), clientRefundAmount, vendorAmount, reservation.getModifiedBy());
								} else if (hostPayoutMethod.getExternalSystemId().equals(EnmExternalSystem.STRIPE.getId())) {
									authPaymentReverseResult = this.stripePaymentService.refund(reservation.getClientUserId(), reservation.getPaymentTransactionId(), null, null, reservation.getModifiedBy());
								}
								if (!OperationResult.isResultSucces(authPaymentReverseResult)) {
									updateReservationResult.setResultDesc("Payment auth reversal failed. Error:" + OperationResult.getResultDesc(authPaymentReverseResult));
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