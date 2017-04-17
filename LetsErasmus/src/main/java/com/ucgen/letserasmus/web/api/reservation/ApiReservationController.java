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
import com.ucgen.letserasmus.library.common.enumeration.EnmBoolStatus;
import com.ucgen.letserasmus.library.common.enumeration.EnmCurrency;
import com.ucgen.letserasmus.library.common.enumeration.EnmEntityType;
import com.ucgen.letserasmus.library.common.enumeration.EnmErrorCode;
import com.ucgen.letserasmus.library.common.enumeration.EnmSize;
import com.ucgen.letserasmus.library.location.model.Location;
import com.ucgen.letserasmus.library.log.enumeration.EnmOperation;
import com.ucgen.letserasmus.library.log.enumeration.EnmTransaction;
import com.ucgen.letserasmus.library.log.model.TransactionLog;
import com.ucgen.letserasmus.library.message.enumeration.EnmMessageStatus;
import com.ucgen.letserasmus.library.message.model.Message;
import com.ucgen.letserasmus.library.message.model.MessageThread;
import com.ucgen.letserasmus.library.parameter.enumeration.EnmParameter;
import com.ucgen.letserasmus.library.payment.model.Payment;
import com.ucgen.letserasmus.library.payment.model.PaymentMethod;
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
	public void setWebApplication(WebApplication webApplication) {
		this.webApplication = webApplication;
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
					
					ValueOperationResult<Place> getOperationResult = this.placeService.getPlace(uiReservation.getPlaceId());
					
					Place place = getOperationResult.getResultValue();
					
					Reservation reservation = new Reservation();
					
					reservation.setClientUserId(user.getId());
					
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
						nextUrl = this.webApplication.getUrlPrefix() + "/pages/Payment.xhtml";
					} else {
						nextUrl = this.webApplication.getUrlPrefix() + "/pages/Verification.xhtml";
					}
					nextUrl = nextUrl + "?" + uriParams;
					operationResult.setResultValue(nextUrl);
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
						&& uiPaymentMethod.getCardHolderLastName() != null && !uiPaymentMethod.getCardHolderLastName().trim().isEmpty()) {
					String paymentToken = this.getPaymentToken(uiReservation.getOperationToken());
					if (paymentToken != null) {
						Reservation reservation = this.getObjectForToken(uiReservation.getOperationToken(), EnmOperation.CREATE_RESERVATION.getId());
						
						Date createdDate = new Date();
						String createdBy = user.getFullName();
						
						reservation.setStatus(EnmReservationStatus.PENDING.getId());
						
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
						payment.setTotalPrice(reservation.getPlacePrice().add(reservation.getCommissionFee().add(reservation.getServiceFee())));
						payment.setBlueSnapHostedFieldToken(paymentToken);
						payment.setBlueSnapCurrencyCode(EnmCurrency.getCurrency(reservation.getCurrencyId()).getBlueSnapCode());
						
						PaymentMethod paymentMethod = new PaymentMethod();
						paymentMethod.setCardHolderFirstName(uiPaymentMethod.getCardHolderFirstName());
						paymentMethod.setCardHolderLastName(uiPaymentMethod.getCardHolderLastName());
						paymentMethod.setCardHolderZipCode(uiPaymentMethod.getZipCode());
						paymentMethod.setPayment(payment);
						
						OperationResult createResult = this.reservationService.insert(user.getId(), reservation, paymentMethod);
						
						if (!OperationResult.isResultSucces(createResult)) {
							FileLogger.log(Level.ERROR, "Reservation could not be saved. UserId:" + user.getId() + ", Error:" + OperationResult.getResultDesc(createResult));
							createResult.setResultDesc("Reservation could not be saved. Please try again later!");
						}
						operationResult = createResult;
					} else {
						operationResult.setResultCode(EnmResultCode.ERROR.getValue());
						operationResult.setResultDesc(AppConstants.OPERATION_FAIL);
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
				if (uiReservation.getId() != null 
						&& uiReservation.getStatus() != null && EnmReservationStatus.getReservationStatus(uiReservation.getStatus()) != null
						&& uiReservation.getMessageText() != null && !uiReservation.getMessageText().trim().isEmpty()) {
					
					Reservation reservation = new Reservation();
					reservation.setId(uiReservation.getId());
					
					List<Reservation> dbReservationList = this.reservationService.list(reservation, false, false, false);
					
					if (dbReservationList != null && dbReservationList.size() > 0) {
						reservation = dbReservationList.get(0);
						boolean isUserAutorized = false;
						Integer oldReservationStatus = reservation.getStatus();
						
						if (uiReservation.getStatus().equals(EnmReservationStatus.DECLINED.getId()) 
								|| uiReservation.getStatus().equals(EnmReservationStatus.CONFIRMED.getId()) 
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
									|| uiReservation.getStatus().equals(EnmReservationStatus.CONFIRMED.getId())) {
								if (dbReservationList.get(0).getStatus().equals(EnmReservationStatus.PENDING.getId())) {
									statusSuitable = true;
								}
							}
							
							if (uiReservation.getStatus().equals(EnmReservationStatus.HOST_CANCELLED.getId()) 
									|| uiReservation.getStatus().equals(EnmReservationStatus.CLIENT_CANCELLED.getId())) {
								if (dbReservationList.get(0).getStatus().equals(EnmReservationStatus.CONFIRMED.getId())) {
									statusSuitable = true;
								}
							}
							
							if (uiReservation.getStatus().equals(EnmReservationStatus.RECALLED.getId())) {
								if (dbReservationList.get(0).getStatus().equals(EnmReservationStatus.PENDING.getId())) {
									statusSuitable = true;
								}
							}
							
							if (uiReservation.getStatus().equals(EnmReservationStatus.PENDING.getId())) {
								if (dbReservationList.get(0).getStatus().equals(EnmReservationStatus.INQUIRY.getId())) {
									statusSuitable = true;
								}
							}
							
							if (statusSuitable) {
								Date operationDate = new Date();
								String createdBy = user.getFullName();
								
								reservation.setStatus(uiReservation.getStatus());
								
								reservation.setModifiedBy(createdBy);
								reservation.setModifiedDate(operationDate);
								
								Message message = null;
								
								if (!dbReservationList.get(0).getStatus().equals(EnmReservationStatus.PENDING.getId())) {
									message = new Message();
									message.setMessageThreadId(dbReservationList.get(0).getMessageThreadId());
									message.setSenderUserId(user.getId());
									message.setReceiverUserId(dbReservationList.get(0).getClientUserId());
									message.setMessageText(uiReservation.getMessageText());
									message.setCreatedBy(createdBy);
									message.setCreatedDate(operationDate);
									message.setStatus(EnmMessageStatus.NOT_READ.getId());
								}
								
								TransactionLog tLog = new TransactionLog();
								tLog.setUserId(reservation.getClientUserId());
								tLog.setOperationDate(operationDate);
								tLog.setEntityType(EnmEntityType.RESERVATION.getId());
								tLog.setEntityId(reservation.getId());
								tLog.setCreatedBy(createdBy);
								tLog.setCreatedDate(operationDate);
								
								if (uiReservation.getStatus().equals(EnmReservationStatus.CONFIRMED.getId())) {
									tLog.setOperationId(EnmTransaction.RESERVATION_ACCEPT.getId());
								} else if (uiReservation.getStatus().equals(EnmReservationStatus.DECLINED.getId())) {
									tLog.setOperationId(EnmTransaction.RESERVATION_DECLINE.getId());
								} else if (uiReservation.getStatus().equals(EnmReservationStatus.CLIENT_CANCELLED.getId()) 
										|| uiReservation.getStatus().equals(EnmReservationStatus.HOST_CANCELLED.getId())) {
									tLog.setOperationId(EnmTransaction.RESERVATION_CANCEL.getId());
								} else if (uiReservation.getStatus().equals(EnmReservationStatus.RECALLED.getId())) {
									tLog.setOperationId(EnmTransaction.RESERVATION_RECALL.getId());
								} else if (uiReservation.getStatus().equals(EnmReservationStatus.PENDING.getId())) {
									tLog.setOperationId(EnmTransaction.RESERVATION_SEND_REQUEST.getId());
								}
								
								if (dbReservationList.get(0).getStatus().equals(EnmReservationStatus.INQUIRY.getId()) 
										&& operationDate.getTime() > reservation.getStartDate().getTime()) {
									operationResult.setResultCode(EnmResultCode.WARNING.getValue());
									operationResult.setResultDesc(AppConstants.RESERV_REQUEST_OUTDATED);
								} else {
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
	
	//@RequestMapping(value = "/api/reservation/get", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResponseEntity<ValueOperationResult<Reservation>> getReservation(@RequestParam("reservationId") Long reservationId, HttpSession session) {
		ValueOperationResult<Reservation> operationResult = new ValueOperationResult<Reservation>();		
		try {
			User user = super.getSessionUser(session);
			if (user != null) {
				Object activeOperation = super.getSession().getAttribute(EnmSession.ACTIVE_OPERATION.getId());
				if (activeOperation != null && activeOperation.equals(EnmOperation.CREATE_PLACE)) {
					Reservation reservation = new Reservation();
					reservation.setId(reservationId);
					
					List<Reservation> reservationList = this.reservationService.list(reservation, true, true, true);
					
					operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
					if (reservationList != null && reservationList.size() > 0) {
						operationResult.setResultValue(reservationList.get(0));
					} else {
						operationResult.setResultDesc(AppConstants.RESERV_NOT_FOUND);
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
			FileLogger.log(Level.ERROR, "ApiReservationController-getReservation()-Error: " + CommonUtil.getExceptionMessage(e));
			operationResult.setResultCode(EnmResultCode.EXCEPTION.getValue());
			operationResult.setResultDesc(AppConstants.GET_RESERV_NOT_COMPLETED);
		}
		return new ResponseEntity<ValueOperationResult<Reservation>>(operationResult, HttpStatus.OK);
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
								|| EnmReservationStatus.CONFIRMED.getId().equals(tmpReservation.getStatus())
								|| EnmReservationStatus.CLOSED.getId().equals(tmpReservation.getStatus())
								|| EnmReservationStatus.WAITING_PAYMENT.getId().equals(tmpReservation.getStatus())) {
							/*
							Review review = new Review();
				
							review.setEntityType(EnmEntityType.RESERVATION.getId());
							review.setEntityId(tmpReservation.getId());
							
							List<Review> reviewList = this.reviewService.listReview(review, null, false, false, false);
							tmpReservation.setReviewList(reviewList);
							*/
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
								|| EnmReservationStatus.CONFIRMED.getId().equals(tmpReservation.getStatus())
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
						
						Reservation reservation = new Reservation();
						
						reservation.setClientUserId(user.getId());
						
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
						
						OperationResult createResult = this.reservationService.insert(user.getId(), reservation, null);
						
						if (!OperationResult.isResultSucces(createResult)) {
							FileLogger.log(Level.ERROR, "Contact host operation could not be completed. UserId:" + user.getId() + ", Error:" + OperationResult.getResultDesc(createResult));
							createResult.setResultDesc("Reservation could not be saved. Please try again later!");
						}
						
						operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
					} else {
						operationResult.setResultCode(EnmResultCode.WARNING.getValue());
						operationResult.setErrorCode(EnmErrorCode.ALREADY_CONTACTED.getId());
						operationResult.setResultDesc(AppConstants.MESSAGE_DOUBLE);
						String conversationPageUrl = AppUtil.concatPath(this.webApplication.getUrlPrefix(), "/pages/dashboard/Conversation.xhtml?threadId="+ dbReservationList.get(0).getMessageThreadId());
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
						
						User responseHostUser = new User();
						String profileImageUrl = this.webApplication.getUserPhotoUrl(hostUser.getId(), hostUser.getProfilePhotoId(), EnmSize.SMALL.getValue());
						
						responseHostUser.setFirstName(hostUser.getFirstName());
						responseHostUser.setProfileImageUrl(profileImageUrl);
						
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
