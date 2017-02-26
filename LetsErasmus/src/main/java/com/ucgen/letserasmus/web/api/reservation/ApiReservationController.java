package com.ucgen.letserasmus.web.api.reservation;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.ucgen.common.operationresult.EnmResultCode;
import com.ucgen.common.operationresult.OperationResult;
import com.ucgen.common.operationresult.ValueOperationResult;
import com.ucgen.letserasmus.library.common.enumeration.EnmBoolStatus;
import com.ucgen.letserasmus.library.common.enumeration.EnmEntityType;
import com.ucgen.letserasmus.library.message.enumeration.EnmMessageStatus;
import com.ucgen.letserasmus.library.message.model.Message;
import com.ucgen.letserasmus.library.message.model.MessageThread;
import com.ucgen.letserasmus.library.place.model.Place;
import com.ucgen.letserasmus.library.place.service.IPlaceService;
import com.ucgen.letserasmus.library.reservation.enumeration.EnmReservationStatus;
import com.ucgen.letserasmus.library.reservation.model.Reservation;
import com.ucgen.letserasmus.library.reservation.service.IReservationService;
import com.ucgen.letserasmus.library.user.model.User;
import com.ucgen.letserasmus.web.api.BaseApiController;
import com.ucgen.letserasmus.web.view.application.EnmOperation;
import com.ucgen.letserasmus.web.view.application.EnmSession;
import com.ucgen.letserasmus.web.view.application.WebApplication;

@RestController
public class ApiReservationController extends BaseApiController {

	private IReservationService reservationService;
	private IPlaceService placeService;
	private WebApplication webApplication;
	
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

	@RequestMapping(value = "/api/reservation/finish", method = RequestMethod.POST)
    public ResponseEntity<OperationResult> finishReservation(@RequestBody UiReservation uiReservation, HttpSession session) {
		OperationResult operationResult = new OperationResult();
		
		try {
			User user = super.getSessionUser(session);
			if (user != null) {
				Object activeOperation = super.getSession().getAttribute(EnmSession.ACTIVE_OPERATION.getId());
				if (activeOperation != null && activeOperation.equals(EnmOperation.FINISH_RESERVATION)) {
					Object objReservation = super.getSession().getAttribute(EnmSession.ACTIVE_RESERVATION.getId());
					if (objReservation != null) {
						
						Reservation reservation = (Reservation) objReservation;
						
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
						
						OperationResult createResult = this.reservationService.insert(reservation);
						
						if (!OperationResult.isResultSucces(createResult)) {
							// TODO: Write log
							createResult.setResultDesc("Reservation could not be saved. Please try again later!");
						}
						operationResult = createResult;
					} else {
						operationResult.setResultCode(EnmResultCode.ERROR.getValue());
						operationResult.setResultDesc("Place record could not be found!");
					}	
				} else {
					operationResult.setResultCode(EnmResultCode.ERROR.getValue());
					operationResult.setResultDesc("You are not authorized for this operation!");
				}
			} else {
				operationResult.setResultCode(EnmResultCode.ERROR.getValue());
				operationResult.setResultDesc("You are not logged in or session is expired. Please login first.");
			}
		} catch (Exception e) {
			operationResult.setResultCode(EnmResultCode.EXCEPTION.getValue());
			operationResult.setResultDesc("Create operation could not be completed. Please try again later!");
		}
		return new ResponseEntity<OperationResult>(operationResult, HttpStatus.OK);
    }
	
	@RequestMapping(value = "/api/reservation/get", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
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
						operationResult.setResultDesc("Reservation not found!");
					}
				} else {
					operationResult.setResultCode(EnmResultCode.ERROR.getValue());
					operationResult.setResultDesc("You are not authorized for this operation!");
				}
			} else {
				operationResult.setResultCode(EnmResultCode.ERROR.getValue());
				operationResult.setResultDesc("You are not logged in or session is expired. Please login first.");
			}
		} catch (Exception e) {
			operationResult.setResultCode(EnmResultCode.EXCEPTION.getValue());
			operationResult.setResultDesc("Create reservation could not be completed. Please try again later!");
		}
		return new ResponseEntity<ValueOperationResult<Reservation>>(operationResult, HttpStatus.OK);
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
						if (dbReservationList.get(0).getHostUserId().equals(user.getId())) {
							boolean statusSuitable = false;
							
							if (dbReservationList.get(0).getStatus().equals(EnmReservationStatus.PENDING.getId()) 
									&& (uiReservation.getStatus().equals(EnmReservationStatus.CONFIRMED.getId()) 
											|| uiReservation.getStatus().equals(EnmReservationStatus.DECLINED.getId()))) {
								statusSuitable = true;
							}
							
							if (statusSuitable) {
								Date createdDate = new Date();
								String createdBy = user.getFullName();
								
								reservation.setStatus(uiReservation.getStatus());
								
								reservation.setModifiedBy(createdBy);
								reservation.setModifiedDate(createdDate);
								
								Message message = new Message();
								message.setMessageThreadId(dbReservationList.get(0).getMessageThreadId());
								message.setSenderUserId(user.getId());
								message.setReceiverUserId(dbReservationList.get(0).getClientUserId());
								message.setMessageText(uiReservation.getMessageText());
								message.setCreatedBy(createdBy);
								message.setCreatedDate(createdDate);
								message.setStatus(EnmMessageStatus.NOT_READ.getId());
								
								OperationResult createResult = this.reservationService.update(reservation, message);
								
								if (!OperationResult.isResultSucces(createResult)) {
									// TODO: Write log
									createResult.setResultDesc("Reservation could not be updated. Please try again later!");
								}
								operationResult = createResult;
							} else {
								operationResult.setResultCode(EnmResultCode.ERROR.getValue());
								operationResult.setResultDesc("Reservation status is not suitable for this operation!");
							}
						} else {
							operationResult.setResultCode(EnmResultCode.ERROR.getValue());
							operationResult.setResultDesc("You are not authorized for this operation!");
						}
					} else {
						operationResult.setResultCode(EnmResultCode.ERROR.getValue());
						operationResult.setResultDesc("Reservation record could not be found!");
					}	
				} else {
					operationResult.setResultCode(EnmResultCode.ERROR.getValue());
					operationResult.setResultDesc("Missing mandatory parameter!");
				}
			} else {
				operationResult.setResultCode(EnmResultCode.ERROR.getValue());
				operationResult.setResultDesc("You are not logged in or session is expired. Please login first.");
			}
		} catch (Exception e) {
			operationResult.setResultCode(EnmResultCode.EXCEPTION.getValue());
			operationResult.setResultDesc("Create operation could not be completed. Please try again later!");
		}
		return new ResponseEntity<OperationResult>(operationResult, HttpStatus.OK);
    }
	
	@RequestMapping(value = "/api/reservation/start", method = RequestMethod.POST)
    public ResponseEntity<ValueOperationResult<String>> startReservation(@RequestBody UiReservation uiReservation, HttpSession session) {
		ValueOperationResult<String> operationResult = new ValueOperationResult<String>();
		
		try {
			User user = super.getSessionUser(session);
			if (user != null) {
				Object activeOperation = super.getSession().getAttribute(EnmSession.ACTIVE_OPERATION.getId());
				
				if (uiReservation.getPlaceId() != null && uiReservation.getStartDate() != null 
						&& uiReservation.getEndDate() != null && uiReservation.getGuestNumber() != null) {
					
					BigDecimal serviceFeeRate = new BigDecimal(0.1);
					BigDecimal commissionFeeRate = new BigDecimal(0.1);
					
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
					
					MessageThread messageThread = new MessageThread();
					messageThread.setHostUserId(place.getHostUserId());
					messageThread.setClientUserId(user.getId());
					messageThread.setEntityType(EnmEntityType.PLACE.getId());
					messageThread.setEntityId(place.getId());
					messageThread.setThreadTitle(place.getTitle());
					
					Message message = new Message();
					
					message.setSenderUserId(user.getId());
					message.setReceiverUserId(place.getHostUserId());
					message.setStatus(EnmMessageStatus.NOT_READ.getId());
					//message.setMessageText(uiReservation.getMessageText());
					
					messageThread.addMessage(message);
					
					reservation.setMessageThread(messageThread);
					
					super.getSession().removeAttribute(EnmSession.ACTIVE_RESERVATION.getId());
					super.getSession().setAttribute(EnmSession.ACTIVE_RESERVATION.getId(), reservation);
					
					operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
					String nextUrl = null;
							
					if (user.getMsisdnVerified().equals(EnmBoolStatus.YES.getId()) 
							&& user.getEmailVerified().equals(EnmBoolStatus.YES.getId())) {
						nextUrl = this.webApplication.getUrlPrefix() + "/pages/Reservation.xhtml";
					} else {
						nextUrl = this.webApplication.getUrlPrefix() + "/pages/Verification.xhtml";
					}
					operationResult.setResultValue(nextUrl);
				} else {
					operationResult.setResultCode(EnmResultCode.ERROR.getValue());
					operationResult.setResultDesc("Mandatory parameters are missing!");
				}	
			} else {
				operationResult.setResultCode(EnmResultCode.ERROR.getValue());
				operationResult.setResultDesc("You are not logged in or session is expired. Please login first.");
			}
		} catch (Exception e) {
			operationResult.setResultCode(EnmResultCode.EXCEPTION.getValue());
			operationResult.setResultDesc("Create operation could not be completed. Please try again later!");
		}
		return new ResponseEntity<ValueOperationResult<String>>(operationResult, HttpStatus.OK);
    }
	
}