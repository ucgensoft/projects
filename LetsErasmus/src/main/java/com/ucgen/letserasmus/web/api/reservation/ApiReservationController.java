package com.ucgen.letserasmus.web.api.reservation;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ucgen.common.operationresult.EnmResultCode;
import com.ucgen.common.operationresult.OperationResult;
import com.ucgen.common.operationresult.ValueOperationResult;
import com.ucgen.common.util.CommonUtil;
import com.ucgen.common.util.DateUtil;
import com.ucgen.common.util.FileLogger;
import com.ucgen.common.util.WebUtil;
import com.ucgen.letserasmus.library.common.enumeration.EnmBoolStatus;
import com.ucgen.letserasmus.library.common.enumeration.EnmCurrency;
import com.ucgen.letserasmus.library.common.enumeration.EnmEntityType;
import com.ucgen.letserasmus.library.common.enumeration.EnmErrorCode;
import com.ucgen.letserasmus.library.common.enumeration.EnmSize;
import com.ucgen.letserasmus.library.location.model.Location;
import com.ucgen.letserasmus.library.location.service.ILocationService;
import com.ucgen.letserasmus.library.log.enumeration.EnmExternalSystem;
import com.ucgen.letserasmus.library.log.enumeration.EnmOperation;
import com.ucgen.letserasmus.library.log.enumeration.EnmTransaction;
import com.ucgen.letserasmus.library.log.model.TransactionLog;
import com.ucgen.letserasmus.library.message.enumeration.EnmMessageStatus;
import com.ucgen.letserasmus.library.message.model.Message;
import com.ucgen.letserasmus.library.message.model.MessageThread;
import com.ucgen.letserasmus.library.parameter.enumeration.EnmParameter;
import com.ucgen.letserasmus.library.payment.model.Payment;
import com.ucgen.letserasmus.library.payment.model.PaymentMethod;
import com.ucgen.letserasmus.library.payment.model.PayoutMethod;
import com.ucgen.letserasmus.library.payment.service.IPaymentService;
import com.ucgen.letserasmus.library.place.model.Place;
import com.ucgen.letserasmus.library.place.service.IPlaceService;
import com.ucgen.letserasmus.library.reservation.enumeration.EnmReservationStatus;
import com.ucgen.letserasmus.library.reservation.model.Reservation;
import com.ucgen.letserasmus.library.reservation.service.IReservationService;
import com.ucgen.letserasmus.library.review.model.Review;
import com.ucgen.letserasmus.library.review.service.IReviewService;
import com.ucgen.letserasmus.library.user.model.User;
import com.ucgen.letserasmus.library.user.service.IUserService;
import com.ucgen.letserasmus.web.api.BaseApiController;
import com.ucgen.letserasmus.web.api.payment.UiPaymentMethod;
import com.ucgen.letserasmus.web.enumeration.EnmUriParameter;
import com.ucgen.letserasmus.web.view.application.AppConstants;
import com.ucgen.letserasmus.web.view.application.AppUtil;
import com.ucgen.letserasmus.web.view.application.EnmSession;
import com.ucgen.letserasmus.web.view.application.WebApplication;

@RestController
public class ApiReservationController extends BaseApiController {

	private IReservationService reservationService;
	private IPlaceService placeService;
	private WebApplication webApplication;
	private IReviewService reviewService;
	private IUserService userService;
	private IPaymentService paymentService;
	private ILocationService locationService;
	
	@Autowired
	public void setReviewService(IReviewService reviewService) {
		this.reviewService = reviewService;
	}
	
	@Autowired
	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	@Autowired
	public void setReservationService(IReservationService reservationService) {
		this.reservationService = reservationService;
	}
		
	@Autowired
	public void setPlaceService(IPlaceService placeService) {
		this.placeService = placeService;
	}
	
	@Autowired
	public void setPaymentService(IPaymentService paymentService) {
		this.paymentService = paymentService;
	}

	@Autowired
	public void setWebApplication(WebApplication webApplication) {
		this.webApplication = webApplication;
	}

	@Autowired
	public void setLocationService(ILocationService locationService) {
		this.locationService = locationService;
	}

	@RequestMapping(value = "/api/reservation/start", method = RequestMethod.POST)
    public ResponseEntity<ValueOperationResult<String>> startReservation(@RequestBody UiReservation uiReservation, HttpSession session) {
		ValueOperationResult<String> operationResult = new ValueOperationResult<String>();
		
		try {
			User user = super.getSessionUser(session);
			if (user != null) {				
				if (uiReservation.getPlaceId() != null && uiReservation.getStartDate() != null 
						&& uiReservation.getEndDate() != null && uiReservation.getGuestNumber() != null) {
					
					String paramServiceFeeRate = this.parameterService.getParameterValue(EnmParameter.PLACE_SERVICE_FEE_RATE.getId());
					String paramCommissionFeeRate = this.parameterService.getParameterValue(EnmParameter.PLACE_COMMISSION_FEE_RATE.getId());
					
					BigDecimal serviceFeeRate = (new BigDecimal(paramServiceFeeRate)).divide(new BigDecimal(100));
					BigDecimal commissionFeeRate = (new BigDecimal(paramCommissionFeeRate)).divide(new BigDecimal(100));
					
					Reservation listReservation = new Reservation();
					listReservation.setPlaceId(uiReservation.getPlaceId());
					listReservation.setClientUserId(user.getId());
					
					List<Reservation> currentReserList = this.reservationService.list(listReservation, false, false, false);
					boolean userHasActiveReservation = false;
					if (currentReserList != null && currentReserList.size() > 0) {
						for (Reservation tmpReservation : currentReserList) {
							if (tmpReservation.getStatus().equals(EnmReservationStatus.PENDING.getId()) 
									|| tmpReservation.getStatus().equals(EnmReservationStatus.ACCEPTED.getId())
									|| tmpReservation.getStatus().equals(EnmReservationStatus.WAITING_PAYMENT.getId())) {
								userHasActiveReservation = true;
							}
						}
					}
					if (!userHasActiveReservation) {
						ValueOperationResult<Place> getOperationResult = this.placeService.getPlace(uiReservation.getPlaceId());
						
						Place place = getOperationResult.getResultValue();
						
						if (!place.getHostUserId().equals(user.getId())) {
							Reservation reservation = new Reservation();
							
							reservation.setClientUserId(user.getId());
							
							reservation.setPlace(place);
							
							reservation.setPlaceId(place.getId());
							reservation.setPlacePrice(place.getPrice());
							reservation.setHostUserId(place.getHostUserId());
							
							reservation.setServiceRate(serviceFeeRate);
							reservation.setServiceFee(serviceFeeRate.multiply(place.getPrice()));
							
							reservation.setCommissionRate(commissionFeeRate);
							reservation.setCommissionFee(commissionFeeRate.multiply(place.getPrice()));
							
							reservation.setGuestNumber(uiReservation.getGuestNumber());
							reservation.setCurrencyId(place.getCurrencyId());
							reservation.setStartDate(uiReservation.getStartDate());
							reservation.setEndDate(uiReservation.getEndDate());
							
							reservation.setStatus(EnmReservationStatus.PENDING.getId());
							reservation.setCancellationPolicyId(place.getCancellationPolicyId());
							
							MessageThread messageThread = new MessageThread();
							messageThread.setHostUserId(place.getHostUserId());
							messageThread.setClientUserId(user.getId());
							messageThread.setEntityType(EnmEntityType.RESERVATION.getId());
							messageThread.setEntityId(place.getId());
							messageThread.setThreadTitle(place.getTitle());
							
							Message message = new Message();
							
							message.setSenderUserId(user.getId());
							message.setReceiverUserId(place.getHostUserId());
							message.setStatus(EnmMessageStatus.NOT_READ.getId());
							//message.setMessageText(uiReservation.getMessageText());
							
							messageThread.addMessage(message);
							
							reservation.setMessageThread(messageThread);
							
							String operationToken = this.generateOperationToken();
							
							this.saveOperationToken(operationToken, EnmOperation.CREATE_RESERVATION, reservation);
							
							operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
							String nextUrl = null;
							
							String uriParams = EnmUriParameter.OPERATION_TOKEN.getName() + "=" + operationToken 
									+ "&" + EnmUriParameter.OPERATION.getName() + "=" + EnmOperation.CREATE_RESERVATION.getId();
							
							if (user.getMsisdnVerified().equals(EnmBoolStatus.YES.getId()) 
									&& user.getEmailVerified().equals(EnmBoolStatus.YES.getId())) {
								nextUrl = this.webApplication.getUrlPrefix() + "/pages/Payment.html";
							} else {
								nextUrl = this.webApplication.getUrlPrefix() + "/pages/Verification.html";
							}
							nextUrl = nextUrl + "?" + uriParams;
							operationResult.setResultValue(nextUrl);
						} else {
							operationResult.setResultCode(EnmResultCode.ERROR.getValue());
							operationResult.setResultDesc("Come on, do you really want to reserve your own place :)");
						}
					} else {
						operationResult.setResultCode(EnmResultCode.ERROR.getValue());
						operationResult.setResultDesc("You already have an active reservation for this place!");
					}
				} else {
					operationResult.setResultCode(EnmResultCode.ERROR.getValue());
					operationResult.setResultDesc(AppConstants.MISSING_MANDATORY_PARAM);
				}	
			} else {
				operationResult.setErrorCode(EnmErrorCode.USER_NOT_LOGGED_IN.getId());
				operationResult.setResultCode(EnmResultCode.ERROR.getValue());
				operationResult.setResultDesc(AppConstants.USER_NOT_LOGGED_IN);
			}
		} catch (Exception e) {
			FileLogger.log(Level.ERROR, "ApiReservationController-startReservation()-Error: " + CommonUtil.getExceptionMessage(e));
			operationResult.setResultCode(EnmResultCode.EXCEPTION.getValue());
			operationResult.setResultDesc(AppConstants.CREATE_OPERATION_FAIL);
		}
		return new ResponseEntity<ValueOperationResult<String>>(operationResult, HttpStatus.OK);
    }
	
	@RequestMapping(value = "/api/reservation/finish", method = RequestMethod.POST)
    public ResponseEntity<OperationResult> finishReservation(@RequestBody UiReservation uiReservation, HttpSession session) {
		OperationResult operationResult = new OperationResult();
		
		try {
			User user = super.getSessionUser(session);
			if (user != null) {
				UiPaymentMethod uiPaymentMethod = uiReservation.getUiPaymentMethod();
				
				if (uiReservation != null && uiReservation.getOperationToken() != null && uiPaymentMethod != null 
						&& uiPaymentMethod.getCardHolderFirstName() != null && !uiPaymentMethod.getCardHolderFirstName().trim().isEmpty()
						&& uiPaymentMethod.getCardHolderLastName() != null && !uiPaymentMethod.getCardHolderLastName().trim().isEmpty()
						&& uiPaymentMethod.getZipCode() != null && !uiPaymentMethod.getZipCode().trim().isEmpty()) {
					
					Reservation reservation = this.getObjectForToken(uiReservation.getOperationToken(), EnmOperation.CREATE_RESERVATION.getId());
					
					Reservation listReservation = new Reservation();
					listReservation.setPlaceId(reservation.getPlaceId());
					listReservation.setClientUserId(user.getId());
					
					List<Reservation> currentReserList = this.reservationService.list(listReservation, false, false, false);
					boolean userHasActiveReservation = false;
					if (currentReserList != null && currentReserList.size() > 0) {
						for (Reservation tmpReservation : currentReserList) {
							if (tmpReservation.getStatus().equals(EnmReservationStatus.PENDING.getId()) 
									|| tmpReservation.getStatus().equals(EnmReservationStatus.ACCEPTED.getId())
									|| tmpReservation.getStatus().equals(EnmReservationStatus.WAITING_PAYMENT.getId())) {
								userHasActiveReservation = true;
							}
						}
					}
					
					if (!userHasActiveReservation) {
						PayoutMethod hostPayoutMethod = this.paymentService.getPayoutMethod(new PayoutMethod(reservation.getHostUserId()));
						
						String paymentToken = null;
						if (hostPayoutMethod.getExternalSystemId().equals(EnmExternalSystem.BLUESNAP.getId())) {
							paymentToken = this.getPaymentToken(uiReservation.getOperationToken());
						} else if (hostPayoutMethod.getExternalSystemId().equals(EnmExternalSystem.STRIPE.getId())) {
							paymentToken = uiReservation.getUiPaymentMethod().getCardInfoToken();
						}
						if (paymentToken != null) {
							
							Integer parameterReserPendingDuration = Integer.valueOf(this.parameterService.getParameterValue(EnmParameter.RESERVATION_PENDING_DURATION.getId()));
							
							Date createdDate = new Date();
							String createdBy = user.getFullName();
							
							reservation.setStatus(EnmReservationStatus.PENDING.getId());
							reservation.setExpireDate(new Date(createdDate.getTime() + parameterReserPendingDuration * 60 * 60 * 1000));
							reservation.setCreatedBy(createdBy);
							reservation.setCreatedDate(createdDate);
							
							MessageThread messageThread = reservation.getMessageThread();
							
							messageThread.setCreatedBy(createdBy);
							messageThread.setCreatedDate(createdDate);
							
							if (reservation.getMessageThread().getMessageList() != null 
									&& reservation.getMessageThread().getMessageList().size() > 0) {
								Message message = reservation.getMessageThread().getMessageList().get(0);
								message.setCreatedDate(createdDate);
								message.setMessageText(uiReservation.getMessageText());
							}
							
							Payment payment = new Payment();
							payment.setCommissionFee(reservation.getCommissionFee());
							payment.setServiceFee(reservation.getServiceFee());
							payment.setEntityPrice(reservation.getPlacePrice());
							payment.setCardInfoToken(paymentToken);
							payment.setCurrencyCode(EnmCurrency.getCurrency(reservation.getCurrencyId()).getBlueSnapCode());
							
							PaymentMethod paymentMethod = new PaymentMethod();
							paymentMethod.setUserId(reservation.getClientUserId());
							paymentMethod.setCardHolderFirstName(uiPaymentMethod.getCardHolderFirstName());
							paymentMethod.setCardHolderLastName(uiPaymentMethod.getCardHolderLastName());
							paymentMethod.setCardHolderZipCode(uiPaymentMethod.getZipCode());
							paymentMethod.setPayment(payment);
							
							//Place place = this.placeService.getPlace(reservation.getPlaceId()).getResultValue();
							Place place = reservation.getPlace();
							String placeCoverPhotoUrl = this.webApplication.getPlacePhotoUrl(place.getId(), place.getCoverPhotoId(), EnmSize.SMALL.getValue());

							String placeUrl = WebUtil.concatUrl(this.webApplication.getUrlPrefix(), "/pages/PlaceDetail.html");
							placeUrl = WebUtil.addUriParam(placeUrl, EnmUriParameter.PLACE_ID.getName(), place.getId());
							
							place.setCoverPhotoUrl(placeCoverPhotoUrl);
							place.setUrl(placeUrl);
							
							OperationResult createResult = this.reservationService.insert(user, reservation, paymentMethod, hostPayoutMethod);
							
							if (!OperationResult.isResultSucces(createResult)) {
								FileLogger.log(Level.ERROR, "Reservation could not be saved. UserId:" + user.getId() + ", Error:" + OperationResult.getResultDesc(createResult));
								createResult.setResultDesc("Reservation could not be saved. Please try again later!");
							} else {
								super.removeOperationToken(uiReservation.getOperationToken(), EnmOperation.CREATE_RESERVATION);
							}
							operationResult = createResult;
						} else {
							operationResult.setResultCode(EnmResultCode.ERROR.getValue());
							operationResult.setResultDesc(AppConstants.OPERATION_FAIL);
						}
					} else {
						operationResult.setResultCode(EnmResultCode.ERROR.getValue());
						operationResult.setResultDesc("You already have an active reservation for this place!");
					}
				} else {
					operationResult.setResultCode(EnmResultCode.ERROR.getValue());
					operationResult.setResultDesc(AppConstants.UNAUTHORIZED_OPERATION);
				}	
			} else {
				operationResult.setErrorCode(EnmErrorCode.USER_NOT_LOGGED_IN.getId());
				operationResult.setResultCode(EnmResultCode.ERROR.getValue());
				operationResult.setResultDesc(AppConstants.USER_NOT_LOGGED_IN);
			}
		} catch (Exception e) {
			FileLogger.log(Level.ERROR, "ApiReservationController-finishReservation()-Error: " + CommonUtil.getExceptionMessage(e));
			operationResult.setResultCode(EnmResultCode.EXCEPTION.getValue());
			operationResult.setResultDesc(AppConstants.CREATE_OPERATION_FAIL);
		}
		return new ResponseEntity<OperationResult>(operationResult, HttpStatus.OK);
    }
	
	@RequestMapping(value = "/api/reservation/update", method = RequestMethod.POST)
    public ResponseEntity<OperationResult> updateReservation(@RequestBody UiReservation uiReservation, HttpSession session) {
		OperationResult operationResult = new OperationResult();
		
		try {
			User user = super.getSessionUser(session);
			if (user != null) {
				// inquiry statüdeki rezervasyon güncellenemez, þimdilik
				if (uiReservation.getId() != null 
						&& !uiReservation.getStatus().equals(EnmReservationStatus.INQUIRY.getId())
						&& uiReservation.getStatus() != null && EnmReservationStatus.getReservationStatus(uiReservation.getStatus()) != null
						&& uiReservation.getMessageText() != null && !uiReservation.getMessageText().trim().isEmpty()) {
					
					Reservation reservation = new Reservation();
					reservation.setId(uiReservation.getId());
					
					List<Reservation> dbReservationList = this.reservationService.list(reservation, true, true, true);
					
					if (dbReservationList != null && dbReservationList.size() > 0) {
						reservation = dbReservationList.get(0);
						boolean isUserAutorized = false;
						Integer oldReservationStatus = reservation.getStatus();
						
						if (uiReservation.getStatus().equals(EnmReservationStatus.DECLINED.getId()) 
								|| uiReservation.getStatus().equals(EnmReservationStatus.ACCEPTED.getId()) 
								|| uiReservation.getStatus().equals(EnmReservationStatus.HOST_CANCELLED.getId())) {
							if (reservation.getHostUserId().equals(user.getId())) {
								isUserAutorized = true;
							}
						}
												
						if (uiReservation.getStatus().equals(EnmReservationStatus.RECALLED.getId()) 
								|| uiReservation.getStatus().equals(EnmReservationStatus.CLIENT_CANCELLED.getId())) {
							if (reservation.getClientUserId().equals(user.getId())) {
								isUserAutorized = true;
							}
						}
						
						if (uiReservation.getStatus().equals(EnmReservationStatus.PENDING.getId())) {
							if (reservation.getClientUserId().equals(user.getId())) {
								isUserAutorized = true;
							}
						}
						
						if (isUserAutorized) {
							boolean statusSuitable = false;
							
							if (uiReservation.getStatus().equals(EnmReservationStatus.DECLINED.getId()) 
									|| uiReservation.getStatus().equals(EnmReservationStatus.ACCEPTED.getId())) {
								if (reservation.getStatus().equals(EnmReservationStatus.PENDING.getId())) {
									statusSuitable = true;
								}
							}
							
							if (uiReservation.getStatus().equals(EnmReservationStatus.HOST_CANCELLED.getId()) 
									|| uiReservation.getStatus().equals(EnmReservationStatus.CLIENT_CANCELLED.getId())) {
								if (reservation.getStatus().equals(EnmReservationStatus.ACCEPTED.getId())) {
									statusSuitable = true;
								}
							}
							
							if (uiReservation.getStatus().equals(EnmReservationStatus.RECALLED.getId())) {
								if (reservation.getStatus().equals(EnmReservationStatus.PENDING.getId())) {
									statusSuitable = true;
								}
							}
							
							if (uiReservation.getStatus().equals(EnmReservationStatus.PENDING.getId())) {
								if (reservation.getStatus().equals(EnmReservationStatus.INQUIRY.getId())) {
									statusSuitable = true;
								}
							}
							
							if (statusSuitable) {
								Date operationDate = new Date();
								String createdBy = user.getFullName();
								
								Message message = null;
								
								if (uiReservation.getMessageText() != null 
										&& !uiReservation.getMessageText().trim().isEmpty()) {
									
									Long receiverUserId = null;
									if (reservation.getClientUserId().equals(user.getId())) {
										receiverUserId = reservation.getHostUserId();
									} else {
										receiverUserId = reservation.getClientUserId();
									}
									
									message = new Message();
									message.setMessageThreadId(reservation.getMessageThreadId());
									message.setSenderUserId(user.getId());
									message.setReceiverUserId(receiverUserId);
									message.setMessageText(uiReservation.getMessageText());
									message.setCreatedBy(createdBy);
									message.setCreatedDate(operationDate);
									message.setStatus(EnmMessageStatus.NOT_READ.getId());
								}
								
								TransactionLog tLog = null;
								
								if (!reservation.getStatus().equals(uiReservation.getStatus())) {
									tLog = new TransactionLog();
									
									tLog.setUserId(reservation.getClientUserId());
									tLog.setOperationDate(operationDate);
									tLog.setEntityType(EnmEntityType.RESERVATION.getId());
									tLog.setEntityId(reservation.getId());
									tLog.setCreatedBy(createdBy);
									tLog.setCreatedDate(operationDate);
									
									if (uiReservation.getStatus().equals(EnmReservationStatus.ACCEPTED.getId())) {
										tLog.setOperationId(EnmTransaction.RESERVATION_ACCEPT.getId());
									} else if (uiReservation.getStatus().equals(EnmReservationStatus.DECLINED.getId())) {
										tLog.setOperationId(EnmTransaction.RESERVATION_DECLINE.getId());
									} else if (uiReservation.getStatus().equals(EnmReservationStatus.CLIENT_CANCELLED.getId())) {
										tLog.setOperationId(EnmTransaction.RESERVATION_CLIENT_CANCELLED.getId());
									} else if ( uiReservation.getStatus().equals(EnmReservationStatus.HOST_CANCELLED.getId())) {
										tLog.setOperationId(EnmTransaction.RESERVATION_HOST_CANCELLED.getId());
									} else if (uiReservation.getStatus().equals(EnmReservationStatus.RECALLED.getId())) {
										tLog.setOperationId(EnmTransaction.RESERVATION_RECALL.getId());
									} else if (uiReservation.getStatus().equals(EnmReservationStatus.PENDING.getId())) {
										tLog.setOperationId(EnmTransaction.RESERVATION_SEND_REQUEST.getId());
									}
								}
								
								reservation.setStatus(uiReservation.getStatus());
								
								reservation.setModifiedBy(createdBy);
								reservation.setModifiedDate(operationDate);
								
								if (dbReservationList.get(0).getStatus().equals(EnmReservationStatus.INQUIRY.getId()) 
										&& operationDate.getTime() > reservation.getStartDate().getTime()) {
									operationResult.setResultCode(EnmResultCode.WARNING.getValue());
									operationResult.setResultDesc(AppConstants.RESERV_REQUEST_OUTDATED);
								} else {
									if ((reservation.getStatus().equals(EnmReservationStatus.ACCEPTED.getId()) 
											&& !oldReservationStatus.equals(EnmReservationStatus.ACCEPTED.getId()))
											|| (reservation.getStatus().equals(EnmReservationStatus.HOST_CANCELLED.getId()) 
													&& !oldReservationStatus.equals(EnmReservationStatus.HOST_CANCELLED.getId()))) {
										Place place = reservation.getPlace();
										String placeCoverPhotoUrl = this.webApplication.getPlacePhotoUrl(place.getId(), place.getCoverPhotoId(), EnmSize.SMALL.getValue());

										String placeUrl = WebUtil.concatUrl(this.webApplication.getUrlPrefix(), "pages/PlaceDetail.html");
										placeUrl = WebUtil.addUriParam(placeUrl, EnmUriParameter.PLACE_ID.getName(), place.getId());
										
										place.setCoverPhotoUrl(placeCoverPhotoUrl);
										place.setUrl(placeUrl);
										
										Location location = this.locationService.get(place.getLocationId());
										place.setLocation(location);
										
										User clientUser = reservation.getClientUser();
										String clientUserUrl = WebUtil.concatUrl(this.webApplication.getUrlPrefix(), "pages/dashboard/DisplayUser.html?userId=" + clientUser.getId());
										String clientProfilePhotoUrl = this.webApplication.getUserPhotoUrl(clientUser.getId(), clientUser.getProfilePhotoId(), EnmSize.SMALL.getValue());
										clientUser.setUrl(clientUserUrl);
										clientUser.setProfileImageUrl(clientProfilePhotoUrl);
										
										User hostUser = reservation.getHostUser();
										String hostUserUrl = WebUtil.concatUrl(this.webApplication.getUrlPrefix(), "pages/dashboard/DisplayUser.html?userId=" + hostUser.getId());
										String hostProfilePhotoUrl = this.webApplication.getUserPhotoUrl(hostUser.getId(), hostUser.getProfilePhotoId(), EnmSize.SMALL.getValue());
										hostUser.setUrl(hostUserUrl);
										hostUser.setProfileImageUrl(hostProfilePhotoUrl);
										
										String conversationUrl = WebUtil.concatUrl(this.webApplication.getUrlPrefix(), "pages/dashboard/Conversation.html?threadId=" + reservation.getMessageThreadId());
										MessageThread messageThread = new MessageThread();
										messageThread.setUrl(conversationUrl);
										reservation.setMessageThread(messageThread);
									}
									OperationResult createResult = this.reservationService.update(reservation, message, tLog, oldReservationStatus);
									
									if (!OperationResult.isResultSucces(createResult)) {
										// TODO: Write log
										createResult.setResultDesc("Reservation could not be updated. Please try again later!");
									}
									operationResult = createResult;
								}
						
							} else {
								operationResult.setResultCode(EnmResultCode.ERROR.getValue());
								operationResult.setResultDesc(AppConstants.RESERV_STATUS_FAIL);
							}
						} else {
							operationResult.setResultCode(EnmResultCode.ERROR.getValue());
							operationResult.setResultDesc(AppConstants.UNAUTHORIZED_OPERATION);
						}
					} else {
						operationResult.setResultCode(EnmResultCode.ERROR.getValue());
						operationResult.setResultDesc(AppConstants.RESERV_NOT_FOUND);
					}	
				} else {
					operationResult.setResultCode(EnmResultCode.ERROR.getValue());
					operationResult.setResultDesc(AppConstants.MISSING_MANDATORY_PARAM);
				}
			} else {
				operationResult.setErrorCode(EnmErrorCode.USER_NOT_LOGGED_IN.getId());
				operationResult.setResultCode(EnmResultCode.ERROR.getValue());
				operationResult.setResultDesc(AppConstants.USER_NOT_LOGGED_IN);
			}
		} catch (Exception e) {
			FileLogger.log(Level.ERROR, "ApiReservationController-updateReservation()-Error: " + CommonUtil.getExceptionMessage(e));
			operationResult.setResultCode(EnmResultCode.EXCEPTION.getValue());
			operationResult.setResultDesc(AppConstants.CREATE_OPERATION_FAIL);
		}
		return new ResponseEntity<OperationResult>(operationResult, HttpStatus.OK);
    }
	
	@RequestMapping(value = "/api/reservation/list", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResponseEntity<ValueOperationResult<Map<String, List<Reservation>>>> listReservation(HttpSession session) {
		ValueOperationResult<Map<String, List<Reservation>>> operationResult = new ValueOperationResult<Map<String, List<Reservation>>>();		
		try {
			User user = super.getSessionUser(session);
			if (user != null) {
				Date currentDate = DateUtil.truncate(new Date());
				Reservation reservation = new Reservation();
				reservation.setHostUserId(user.getId());
				
				List<Reservation> reservationList = this.reservationService.list(reservation, true, true, true);
				
				Map<String, List<Reservation>> reservationMap = new HashMap<String, List<Reservation>>();
				List<Reservation> upcomingList = new ArrayList<Reservation>();
				List<Reservation> ongoingList = new ArrayList<Reservation>();
				List<Reservation> oldList = new ArrayList<Reservation>();
				reservationMap.put("upcomingList", upcomingList);
				reservationMap.put("ongoingList", ongoingList);
				reservationMap.put("oldList", oldList);
				
				if (reservationList != null && reservationList.size() > 0) {
					for (Reservation tmpReservation : reservationList) {
						if (EnmReservationStatus.PENDING.getId().equals(tmpReservation.getStatus())
								|| EnmReservationStatus.ACCEPTED.getId().equals(tmpReservation.getStatus())
								|| EnmReservationStatus.CLOSED.getId().equals(tmpReservation.getStatus())
								|| EnmReservationStatus.WAITING_PAYMENT.getId().equals(tmpReservation.getStatus())) {
							/*
							Review review = new Review();
				
							review.setEntityType(EnmEntityType.RESERVATION.getId());
							review.setEntityId(tmpReservation.getId());
							
							List<Review> reviewList = this.reviewService.listReview(review, null, false, false, false);
							tmpReservation.setReviewList(reviewList);
							*/
							if (tmpReservation.getStatus().equals(EnmReservationStatus.PENDING.getId()) 
									|| DateUtil.truncate(tmpReservation.getStartDate()).getTime() > currentDate.getTime()) {
								upcomingList.add(tmpReservation);
							} else if (DateUtil.truncate(tmpReservation.getEndDate()).getTime() < currentDate.getTime()) {
								oldList.add(tmpReservation);
							} else {
								ongoingList.add(tmpReservation);
							}
						}
					}
				}
				
				operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
				operationResult.setResultValue(reservationMap);
			} else {
				operationResult.setErrorCode(EnmErrorCode.USER_NOT_LOGGED_IN.getId());
				operationResult.setResultCode(EnmResultCode.ERROR.getValue());
				operationResult.setResultDesc(AppConstants.USER_NOT_LOGGED_IN);
			}
		} catch (Exception e) {
			FileLogger.log(Level.ERROR, "ApiReservationController-listReservation()-Error: " + CommonUtil.getExceptionMessage(e));
			operationResult.setResultCode(EnmResultCode.EXCEPTION.getValue());
			operationResult.setResultDesc(AppConstants.LIST_RESERV_NOT_COMPLETED);
		}
		return new ResponseEntity<ValueOperationResult<Map<String, List<Reservation>>>>(operationResult, HttpStatus.OK);
    }
	
	@RequestMapping(value = "/api/reservation/listtrips", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResponseEntity<ValueOperationResult<Map<String, List<Reservation>>>> listTrips(HttpSession session) {
		ValueOperationResult<Map<String, List<Reservation>>> operationResult = new ValueOperationResult<Map<String, List<Reservation>>>();		
		try {
			User user = super.getSessionUser(session);
			if (user != null) {
				Date currentDate = DateUtil.truncate(new Date());
				Reservation reservation = new Reservation();
				reservation.setClientUserId(user.getId());
				
				List<Reservation> reservationList = this.reservationService.list(reservation, true, true, true);
				
				Map<String, List<Reservation>> reservationMap = new HashMap<String, List<Reservation>>();
				List<Reservation> upcomingList = new ArrayList<Reservation>();
				List<Reservation> ongoingList = new ArrayList<Reservation>();
				List<Reservation> oldList = new ArrayList<Reservation>();
				reservationMap.put("upcomingList", upcomingList);
				reservationMap.put("ongoingList", ongoingList);
				reservationMap.put("oldList", oldList);
				
				if (reservationList != null && reservationList.size() > 0) {
					for (Reservation tmpReservation : reservationList) {
						if (EnmReservationStatus.PENDING.getId().equals(tmpReservation.getStatus())
								|| EnmReservationStatus.ACCEPTED.getId().equals(tmpReservation.getStatus())
								|| EnmReservationStatus.CLOSED.getId().equals(tmpReservation.getStatus())
								|| EnmReservationStatus.WAITING_PAYMENT.getId().equals(tmpReservation.getStatus())) {

							Review review = new Review();
							review.setEntityType(EnmEntityType.RESERVATION.getId());
							review.setEntityId(tmpReservation.getId());
							
							List<Review> reviewList = this.reviewService.listReview(review, null, false, false, false);
							tmpReservation.setReviewList(reviewList);
							
							if (DateUtil.truncate(tmpReservation.getStartDate()).getTime() > currentDate.getTime()) {
								upcomingList.add(tmpReservation);
							} else if (DateUtil.truncate(tmpReservation.getEndDate()).getTime() < currentDate.getTime()) {
								oldList.add(tmpReservation);
							} else {
								ongoingList.add(tmpReservation);
							}
						}
					}
				}
				
				operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
				operationResult.setResultValue(reservationMap);
			} else {
				operationResult.setErrorCode(EnmErrorCode.USER_NOT_LOGGED_IN.getId());
				operationResult.setResultCode(EnmResultCode.ERROR.getValue());
				operationResult.setResultDesc(AppConstants.USER_NOT_LOGGED_IN);
			}
		} catch (Exception e) {
			FileLogger.log(Level.ERROR, "ApiReservationController-listTrips()-Error: " + CommonUtil.getExceptionMessage(e));
			operationResult.setResultCode(EnmResultCode.EXCEPTION.getValue());
			operationResult.setResultDesc(AppConstants.LIST_TRIP_NOT_COMPLETED);
		}
		return new ResponseEntity<ValueOperationResult<Map<String, List<Reservation>>>>(operationResult, HttpStatus.OK);
    }

	@RequestMapping(value = "/api/reservation/createinquiry", method = RequestMethod.POST)
    public ResponseEntity<ValueOperationResult<String>> createInquiry(@RequestBody UiReservation uiReservation, HttpSession session) {
		ValueOperationResult<String> operationResult = new ValueOperationResult<String>();
		
		try {
			User user = super.getSessionUser(session);
			if (user != null) {				
				if (uiReservation.getPlaceId() != null && uiReservation.getStartDate() != null 
						&& uiReservation.getEndDate() != null && uiReservation.getGuestNumber() != null
						&& uiReservation.getMessageText() != null && uiReservation.getMessageText().trim().length() > 0) {
					Reservation dbReservation = new Reservation();
					
					dbReservation.setClientUserId(user.getId());
					dbReservation.setPlaceId(uiReservation.getPlaceId());
					dbReservation.setStatus(EnmReservationStatus.INQUIRY.getId());
					
					List<Reservation> dbReservationList = this.reservationService.list(dbReservation, false, false, false);
					
					if (dbReservationList == null || dbReservationList.size() == 0) {
						Date operationDate = new Date();
						String createdBy = user.getFullName();
						
						String paramServiceFeeRate = this.parameterService.getParameterValue(EnmParameter.PLACE_SERVICE_FEE_RATE.getId());
						String paramCommissionFeeRate = this.parameterService.getParameterValue(EnmParameter.PLACE_COMMISSION_FEE_RATE.getId());
						
						BigDecimal serviceFeeRate = (new BigDecimal(paramServiceFeeRate)).divide(new BigDecimal(100));
						BigDecimal commissionFeeRate = (new BigDecimal(paramCommissionFeeRate)).divide(new BigDecimal(100));
						
						ValueOperationResult<Place> getOperationResult = this.placeService.getPlace(uiReservation.getPlaceId());
						
						Place place = getOperationResult.getResultValue();
						
						if (!place.getHostUserId().equals(user.getId())) {
							Reservation reservation = new Reservation();
							
							reservation.setClientUserId(user.getId());
							
							reservation.setPlaceId(place.getId());
							reservation.setPlacePrice(place.getPrice());
							reservation.setHostUserId(place.getHostUserId());
							reservation.setHostUser(place.getUser());
							
							reservation.setServiceRate(serviceFeeRate);
							reservation.setServiceFee(serviceFeeRate.multiply(place.getPrice()));
							
							reservation.setCommissionRate(commissionFeeRate);
							reservation.setCommissionFee(commissionFeeRate.multiply(place.getPrice()));
							
							reservation.setGuestNumber(uiReservation.getGuestNumber());
							reservation.setCurrencyId(place.getCurrencyId());
							reservation.setStartDate(uiReservation.getStartDate());
							reservation.setEndDate(uiReservation.getEndDate());
							
							reservation.setStatus(EnmReservationStatus.INQUIRY.getId());
							reservation.setCreatedDate(operationDate);
							reservation.setCreatedBy(createdBy);
							
							MessageThread messageThread = new MessageThread();
							messageThread.setHostUserId(place.getHostUserId());
							messageThread.setClientUserId(user.getId());
							messageThread.setEntityType(EnmEntityType.RESERVATION.getId());
							messageThread.setEntityId(-1l);
							messageThread.setThreadTitle(place.getTitle());
							messageThread.setCreatedDate(operationDate);
							messageThread.setCreatedBy(createdBy);
							
							Message message = new Message();
							
							message.setSenderUserId(user.getId());
							message.setReceiverUserId(place.getHostUserId());
							message.setMessageText(uiReservation.getMessageText());
							message.setStatus(EnmMessageStatus.NOT_READ.getId());
							message.setCreatedDate(operationDate);
							message.setCreatedBy(createdBy);
							
							messageThread.addMessage(message);
							
							reservation.setMessageThread(messageThread);
							
							OperationResult createResult = this.reservationService.insert(user, reservation, null, null);
							
							if (!OperationResult.isResultSucces(createResult)) {
								FileLogger.log(Level.ERROR, "Contact host operation could not be completed. UserId:" + user.getId() + ", Error:" + OperationResult.getResultDesc(createResult));
								createResult.setResultDesc("Reservation could not be saved. Please try again later!");
							}
							
							operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
						} else {
							operationResult.setResultCode(EnmResultCode.ERROR.getValue());
							operationResult.setResultDesc("Come on, do you really want to send message to yourself :)");
						}
					} else {
						operationResult.setResultCode(EnmResultCode.WARNING.getValue());
						operationResult.setErrorCode(EnmErrorCode.ALREADY_CONTACTED.getId());
						operationResult.setResultDesc(AppConstants.MESSAGE_DOUBLE);
						String conversationPageUrl = AppUtil.concatPath(this.webApplication.getUrlPrefix(), "/pages/dashboard/Conversation.html?threadId="+ dbReservationList.get(0).getMessageThreadId());
						operationResult.setResultValue(conversationPageUrl);
					}
				} else {
					operationResult.setResultCode(EnmResultCode.ERROR.getValue());
					operationResult.setResultDesc(AppConstants.MISSING_MANDATORY_PARAM);
				}	
			} else {
				operationResult.setErrorCode(EnmErrorCode.USER_NOT_LOGGED_IN.getId());
				operationResult.setResultCode(EnmResultCode.ERROR.getValue());
				operationResult.setResultDesc(AppConstants.USER_NOT_LOGGED_IN);
			}
		} catch (Exception e) {
			FileLogger.log(Level.ERROR, "ApiReservationController-startReservation()-Error: " + CommonUtil.getExceptionMessage(e));
			operationResult.setResultCode(EnmResultCode.EXCEPTION.getValue());
			operationResult.setResultDesc(AppConstants.CREATE_OPERATION_FAIL);
		}
		return new ResponseEntity<ValueOperationResult<String>>(operationResult, HttpStatus.OK);
    }
	
	@RequestMapping(value = "/api/reservation/gettokenobject", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResponseEntity<ValueOperationResult<Reservation>> getTokenObject(@RequestParam("operationToken") String operationToken,
    		@RequestParam("operationId") Integer operationId, HttpSession session) {
		ValueOperationResult<Reservation> operationResult = new ValueOperationResult<Reservation>();		
		try {
			User user = this.getSessionUser(session);
			if (user != null) {
				if (operationToken != null && !operationToken.trim().isEmpty()
						&& operationId != null) {
					Reservation reservation = this.getObjectForToken(operationToken, operationId);
					if (reservation != null) {
						User hostUser = this.userService.getUser(new User(reservation.getHostUserId()));
						
						PayoutMethod responsePayoutMethod = new PayoutMethod(hostUser.getId());
						
						PayoutMethod hostUserPayoutMethod = this.paymentService.getPayoutMethod(responsePayoutMethod);
						
						responsePayoutMethod.setExternalSystemId(hostUserPayoutMethod.getExternalSystemId());
						
						hostUser.setPayoutMethod(responsePayoutMethod);
						
						User responseHostUser = new User();
						String profileImageUrl = this.webApplication.getUserPhotoUrl(hostUser.getId(), hostUser.getProfilePhotoId(), EnmSize.SMALL.getValue());
						
						responseHostUser.setFirstName(hostUser.getFirstName());
						responseHostUser.setProfileImageUrl(profileImageUrl);
						responseHostUser.setPayoutMethod(responsePayoutMethod);
						
						User responseClientUser = new User();
						responseClientUser.setFirstName(user.getFirstName());
						responseClientUser.setLastName(user.getLastName());
						responseClientUser.setMsisdnCountryCode(user.getMsisdnCountryCode());
						responseClientUser.setMsisdn(user.getMsisdn());
						
						ValueOperationResult<Place> placeResult = this.placeService.getPlace(reservation.getPlaceId());
						
						Place place = placeResult.getResultValue();
						String coverPhotoUrl = this.webApplication.getPlacePhotoUrl(place.getId(), place.getCoverPhotoId(), EnmSize.SMALL.getValue());
						
						Place responsePlace = new Place();
						responsePlace.setTitle(place.getTitle());
						responsePlace.setPlaceTypeId(place.getPlaceTypeId());
						responsePlace.setCoverPhotoUrl(coverPhotoUrl);
						
						Location responseLocation = new Location();
						responseLocation.setCountry(place.getLocation().getCountry());
						responseLocation.setState(place.getLocation().getState());
												
						responsePlace.setLocation(responseLocation);
						
						Reservation responseReservation = new Reservation();
						responseReservation.setPlacePrice(reservation.getPlacePrice());
						responseReservation.setServiceFee(reservation.getServiceFee());
						responseReservation.setStartDate(reservation.getStartDate());
						responseReservation.setEndDate(reservation.getEndDate());
						responseReservation.setCurrencyId(reservation.getCurrencyId());
						
						responseReservation.setPlace(responsePlace);
						responseReservation.setHostUser(responseHostUser);
						responseReservation.setClientUser(responseClientUser);
						
						operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
						operationResult.setResultValue(responseReservation);
					} else {
						operationResult.setResultCode(EnmResultCode.ERROR.getValue());
						operationResult.setResultDesc(AppConstants.INVALID_OPERATION_TOKEN);
					}
				} else {
					operationResult.setResultCode(EnmResultCode.ERROR.getValue());
					operationResult.setResultDesc(AppConstants.MISSING_MANDATORY_PARAM);
				}
			} else {
				operationResult.setErrorCode(EnmErrorCode.USER_NOT_LOGGED_IN.getId());
				operationResult.setResultCode(EnmResultCode.ERROR.getValue());
				operationResult.setResultDesc(AppConstants.USER_NOT_LOGGED_IN);
			}
		} catch (Exception e) {
			FileLogger.log(Level.ERROR, "BaseApiController-getTokenObject()-Error: " + CommonUtil.getExceptionMessage(e));
			operationResult.setResultCode(EnmResultCode.EXCEPTION.getValue());
			operationResult.setResultDesc(AppConstants.OPERATION_FAIL);
		}
		return new ResponseEntity<ValueOperationResult<Reservation>>(operationResult, HttpStatus.OK);
    }
	
}
