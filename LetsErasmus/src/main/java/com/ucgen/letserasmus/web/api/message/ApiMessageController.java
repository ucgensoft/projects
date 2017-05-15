package com.ucgen.letserasmus.web.api.message;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
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
import com.ucgen.common.util.FileLogger;
import com.ucgen.common.util.NumberUtil;
import com.ucgen.letserasmus.library.common.enumeration.EnmEntityType;
import com.ucgen.letserasmus.library.common.enumeration.EnmErrorCode;
import com.ucgen.letserasmus.library.common.enumeration.EnmSize;
import com.ucgen.letserasmus.library.log.enumeration.EnmTransaction;
import com.ucgen.letserasmus.library.log.model.TransactionLog;
import com.ucgen.letserasmus.library.log.service.ILogService;
import com.ucgen.letserasmus.library.message.enumeration.EnmMessageStatus;
import com.ucgen.letserasmus.library.message.model.Message;
import com.ucgen.letserasmus.library.message.model.MessageThread;
import com.ucgen.letserasmus.library.message.service.IMessageService;
import com.ucgen.letserasmus.library.place.model.Place;
import com.ucgen.letserasmus.library.reservation.model.Reservation;
import com.ucgen.letserasmus.library.reservation.service.IReservationService;
import com.ucgen.letserasmus.library.user.model.User;
import com.ucgen.letserasmus.web.api.BaseApiController;
import com.ucgen.letserasmus.web.view.application.AppConstants;
import com.ucgen.letserasmus.web.view.application.WebApplication;

@RestController
public class ApiMessageController extends BaseApiController {

	private IMessageService messageService;
	private IReservationService reservationService;
	private ILogService logService;
	
	private WebApplication webApplication;
	
	@Autowired
	public void setMessageService(IMessageService messageService) {
		this.messageService = messageService;
	}
	
	@Autowired
	public void setReservationService(IReservationService reservationService) {
		this.reservationService = reservationService;
	}

	@Autowired
	public void setLogService(ILogService logService) {
		this.logService = logService;
	}

	@Autowired
	public void setWebApplication(WebApplication webApplication) {
		this.webApplication = webApplication;
	}
	
	@RequestMapping(value = "/api/message/list", method = RequestMethod.GET)
    public ResponseEntity<ValueOperationResult<String>> listMessage(@RequestParam Integer messageType, HttpSession session) {
		ValueOperationResult<String> operationResult = new ValueOperationResult<String>();
		
		try {
			User user = super.getSessionUser(session);
			if (user != null) {				
				if (messageType != null && (messageType.equals(1) || messageType.equals(2))) {
					
					
					operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());;
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
			FileLogger.log(Level.ERROR, "ApiMessageController-listMessage()-Error: " + CommonUtil.getExceptionMessage(e));
			operationResult.setResultCode(EnmResultCode.EXCEPTION.getValue());
			operationResult.setResultDesc(AppConstants.CREATE_OPERATION_FAIL);
		}
		return new ResponseEntity<ValueOperationResult<String>>(operationResult, HttpStatus.OK);
    }
	
	@RequestMapping(value = "/api/message/listthread", method = RequestMethod.GET)
    public ResponseEntity<ValueOperationResult<Map<String, List<UiMessageThread>>>> listMessageThread(@RequestParam("entityType") Integer entityType, 
    		@RequestParam("hostFlag") Integer hostFlag, @RequestParam("clientFlag") Integer clientFlag, HttpSession session) {
		ValueOperationResult<Map<String, List<UiMessageThread>>> operationResult = new ValueOperationResult<Map<String, List<UiMessageThread>>>();
		
		try {
			User user = super.getSessionUser(session);
			if (user != null) {
				Map<String, List<UiMessageThread>> messageThreadMap = new HashMap<String, List<UiMessageThread>>();
				
				if (hostFlag != null && hostFlag.intValue() == 1) {
					MessageThread hostMessageThread = new MessageThread();
					hostMessageThread.setEntityType(entityType);
					hostMessageThread.setHostUserId(user.getId());
					
					List<MessageThread> hostMessageThreadList = this.messageService.listMessageThread(hostMessageThread, true, false, true);
					List<UiMessageThread> uiMessageThreadList = this.convertToUiMessageThreadList(user, hostMessageThreadList, true, true);
					messageThreadMap.put("hostThreadList", uiMessageThreadList);
				}
				
				if (clientFlag != null && clientFlag.intValue() == 1) {
					MessageThread clientMessageThread = new MessageThread();
					clientMessageThread.setEntityType(entityType);
					clientMessageThread.setClientUserId(user.getId());
					
					List<MessageThread> clientMessageThreadList = this.messageService.listMessageThread(clientMessageThread, true, true, false);
					
					List<UiMessageThread> uiMessageThreadList = this.convertToUiMessageThreadList(user, clientMessageThreadList, false, true);
					messageThreadMap.put("clientThreadList", uiMessageThreadList);
				}
				
				operationResult.setResultValue(messageThreadMap);
				operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());;				
			} else {
				operationResult.setErrorCode(EnmErrorCode.USER_NOT_LOGGED_IN.getId());
				operationResult.setResultCode(EnmResultCode.ERROR.getValue());
				operationResult.setResultDesc(AppConstants.USER_NOT_LOGGED_IN);
			}
		} catch (Exception e) {
			FileLogger.log(Level.ERROR, "ApiMessageController-listMessageThread()-Error: " + CommonUtil.getExceptionMessage(e));
			operationResult.setResultCode(EnmResultCode.EXCEPTION.getValue());
			operationResult.setResultDesc(AppConstants.CREATE_OPERATION_FAIL);
		}
		return new ResponseEntity<ValueOperationResult<Map<String, List<UiMessageThread>>>>(operationResult, HttpStatus.OK);
    }
	
	@RequestMapping(value = "/api/message/getthread", method = RequestMethod.GET)
    public ResponseEntity<ValueOperationResult<UiMessageThread>> getMessageThread(@RequestParam("threadId") Long threadId, HttpSession session) {
		ValueOperationResult<UiMessageThread> operationResult = new ValueOperationResult<UiMessageThread>();
		
		try {
			User user = super.getSessionUser(session);
			if (user != null) {
				
				MessageThread messageThread = new MessageThread();
				
				messageThread.setId(threadId);
				messageThread.setEntityType(EnmEntityType.RESERVATION.getId());
				
				MessageThread dbMessageThread = this.messageService.getMessageThread(messageThread, true, true, true);
				if (dbMessageThread != null) {
					if (user.getId().equals(dbMessageThread.getHostUserId()) 
							|| user.getId().equals(dbMessageThread.getClientUserId())) {
						Reservation reservation = (Reservation) dbMessageThread.getEntity();
						
						boolean isHost = (user.getId().equals(dbMessageThread.getHostUserId()));
						UiMessageThread uiMessageThread = this.convertToUiMessageThread(user, dbMessageThread, isHost, true);
						
						Message message = new Message();
						message.setMessageThreadId(threadId);
												
						List<Message> messageList = this.messageService.listMessage(message, false, false);
						
						TransactionLog transactionLog = new TransactionLog();
						transactionLog.setEntityType(EnmEntityType.RESERVATION.getId());
						transactionLog.setEntityId(uiMessageThread.getReservation().getId());
						List<TransactionLog> transactionLogList = this.logService.listTransactionLog(transactionLog, false, false);
						
						List<UiMessage> uiMessageList = new ArrayList<UiMessage>();
												
						Integer totalMessageCount = (messageList == null ? 0 : messageList.size()) 
								+ (transactionLogList == null ? 0 : transactionLogList.size());
						
						Integer messageListIndex = 0;
						Integer logListIndex = 0;
						
						for (int i = 0; i < totalMessageCount; i++) {
							TransactionLog currentLog = null;
							if (transactionLogList != null && transactionLogList.size() > logListIndex) {
								currentLog = transactionLogList.get(logListIndex);
							}
							Message currentMessage = null;
							if (messageList != null && messageList.size() > messageListIndex) {
								currentMessage = messageList.get(messageListIndex);
							}
							UiMessage newUiMessage = new UiMessage();
							if (i == 0) {
								newUiMessage.setMessageType(UiMessage.MESSAGE_TYPE_TRANSACTION);
							} else {
								if (currentMessage != null && currentLog != null) {
									if (currentMessage.getCreatedDate().getTime() < currentLog.getOperationDate().getTime()) {
										newUiMessage.setMessageType(UiMessage.MESSAGE_TYPE_TEXT);
									} else {
										newUiMessage.setMessageType(UiMessage.MESSAGE_TYPE_TRANSACTION);
									}
								} else if (currentMessage != null) {
									newUiMessage.setMessageType(UiMessage.MESSAGE_TYPE_TEXT);
								} else {
									newUiMessage.setMessageType(UiMessage.MESSAGE_TYPE_TRANSACTION);
								}
							}
							if (newUiMessage.getMessageType().equals(UiMessage.MESSAGE_TYPE_TRANSACTION)) {
								newUiMessage.setSenderUserId(currentLog.getUserId());
								if (currentLog.getOperationId().equals(EnmTransaction.RESERVATION_SEND_INQUIRY.getId())) {
									newUiMessage.setMessageText("Client contacted host");
								} else if (currentLog.getOperationId().equals(EnmTransaction.RESERVATION_SEND_REQUEST.getId())) {
									newUiMessage.setMessageText("Booking request sent");
								} else if (currentLog.getOperationId().equals(EnmTransaction.RESERVATION_ACCEPT.getId())) {
									newUiMessage.setMessageText("Booking request accepted");
								} else if (currentLog.getOperationId().equals(EnmTransaction.RESERVATION_DECLINE.getId())) {
									newUiMessage.setMessageText("Booking request declined");
								} else if (currentLog.getOperationId().equals(EnmTransaction.RESERVATION_RECALL.getId())) {
									newUiMessage.setMessageText("Booking request recalled");
								} else if (currentLog.getOperationId().equals(EnmTransaction.RESERVATION_CLIENT_CANCELLED.getId())) {
									newUiMessage.setMessageText("Booking request cancelled by client");
								} else if (currentLog.getOperationId().equals(EnmTransaction.RESERVATION_HOST_CANCELLED.getId())) {
									newUiMessage.setMessageText("Booking request cancelled by host");
								}
								newUiMessage.setCreatedDate(currentLog.getOperationDate());
								logListIndex += 1;
							} else {
								newUiMessage.setId(currentMessage.getId());
								newUiMessage.setSenderUserId(currentMessage.getSenderUserId());
								newUiMessage.setReceiverUserId(currentMessage.getReceiverUserId());
								newUiMessage.setMessageText(currentMessage.getMessageText());
								newUiMessage.setCreatedDate(currentMessage.getCreatedDate());
								messageListIndex += 1;
							}
							
							uiMessageList.add(newUiMessage);
						}
						
						Collections.reverse(uiMessageList);
						
						uiMessageThread.setMessageList(uiMessageList);
												
						uiMessageThread.setCurrencyId(reservation.getCurrencyId());
						uiMessageThread.setEntityPrice(reservation.getPlacePrice());
						
						if (isHost) {
							uiMessageThread.setServiceFee(reservation.getCommissionFee().multiply(new BigDecimal(-1)));
						} else {
							uiMessageThread.setServiceFee(reservation.getServiceFee());
						}
						
						operationResult.setResultValue(uiMessageThread);
						operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
					} else {
						operationResult.setResultCode(EnmResultCode.ERROR.getValue());
						operationResult.setResultDesc(AppConstants.UNAUTHORIZED_OPERATION);
					}
				} else {
					operationResult.setResultCode(EnmResultCode.ERROR.getValue());
					operationResult.setResultDesc(AppConstants.MESSAGE_NOT_FOUND);
				}			
			} else {
				operationResult.setErrorCode(EnmErrorCode.USER_NOT_LOGGED_IN.getId());
				operationResult.setResultCode(EnmResultCode.ERROR.getValue());
				operationResult.setResultDesc(AppConstants.USER_NOT_LOGGED_IN);
			}
		} catch (Exception e) {
			FileLogger.log(Level.ERROR, "ApiMessageController-getMessageThread()-Error: " + CommonUtil.getExceptionMessage(e));
			operationResult.setResultCode(EnmResultCode.EXCEPTION.getValue());
			operationResult.setResultDesc(AppConstants.CREATE_OPERATION_FAIL);
		}
		return new ResponseEntity<ValueOperationResult<UiMessageThread>>(operationResult, HttpStatus.OK);
    }
	
	@RequestMapping(value = "/api/message/send", method = RequestMethod.POST)
    public ResponseEntity<ValueOperationResult<UiMessage>> sendMessage(@RequestBody Message uiMessage, HttpSession session) {
		ValueOperationResult<UiMessage> operationResult = new ValueOperationResult<UiMessage>();
		
		try {
			User user = super.getSessionUser(session);
			if (user != null) {
				if (uiMessage.getMessageThreadId() != null 
						&& uiMessage.getMessageText() != null 
						&& !uiMessage.getMessageText().trim().isEmpty()) {
					MessageThread messageThread = new MessageThread();
					
					messageThread.setId(uiMessage.getMessageThreadId());
					messageThread.setEntityType(EnmEntityType.RESERVATION.getId());
					
					MessageThread dbMessageThread = this.messageService.getMessageThread(messageThread, false, true, true);
					
					if (dbMessageThread != null) {
						if (user.getId().equals(dbMessageThread.getHostUserId()) 
								|| user.getId().equals(dbMessageThread.getClientUserId())) {
							
							Message message = new Message();
							message.setMessageThreadId(dbMessageThread.getId());
							message.setSenderUserId(user.getId());
							if (user.getId().equals(dbMessageThread.getHostUserId())) {
								message.setReceiverUserId(dbMessageThread.getClientUserId());
								message.setReceiverUser(dbMessageThread.getClientUser());
							} else {
								message.setReceiverUserId(dbMessageThread.getHostUserId());
								message.setReceiverUser(dbMessageThread.getHostUser());
							}
							message.setMessageText(uiMessage.getMessageText());
							message.setStatus(EnmMessageStatus.NOT_READ.getId());
							message.setCreatedDate(new Date());
							message.setCreatedBy(user.getFullName());
							
							message.setMessageThread(dbMessageThread);
													
							OperationResult insertMessageResult = this.messageService.insertMessage(message, true);
							if (OperationResult.isResultSucces(insertMessageResult)) {
								
								operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
								
								UiMessage createdMessage = new UiMessage();
								createdMessage.setMessageType(UiMessage.MESSAGE_TYPE_TEXT);
								createdMessage.setSenderUserId(message.getSenderUserId());
								createdMessage.setReceiverUserId(message.getReceiverUserId());
								createdMessage.setMessageText(message.getMessageText());
								createdMessage.setCreatedDate(message.getCreatedDate());
								
								operationResult.setResultValue(createdMessage);
							} else {
								operationResult.setResultCode(EnmResultCode.ERROR.getValue());
								operationResult.setResultDesc(AppConstants.MESSAGE_NOT_SAVED);
							}
						} else {
							operationResult.setResultCode(EnmResultCode.ERROR.getValue());
							operationResult.setResultDesc(AppConstants.UNAUTHORIZED_OPERATION);
						}
					} else {
						operationResult.setResultCode(EnmResultCode.ERROR.getValue());
						operationResult.setResultDesc(AppConstants.MESSAGE_NOT_FOUND);
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
			FileLogger.log(Level.ERROR, "ApiMessageController-sendMessage()-Error: " + CommonUtil.getExceptionMessage(e));
			operationResult.setResultCode(EnmResultCode.EXCEPTION.getValue());
			operationResult.setResultDesc(AppConstants.CREATE_OPERATION_FAIL);
		}
		return new ResponseEntity<ValueOperationResult<UiMessage>>(operationResult, HttpStatus.OK);
    }
	
	private List<UiMessageThread> convertToUiMessageThreadList(User activeUser, List<MessageThread> messageThreadList, boolean isHost, boolean getReservation) {
		List<UiMessageThread> uiMessageThreadList = new ArrayList<UiMessageThread>();
		if (messageThreadList != null) {
			for (MessageThread tmpMessageThread : messageThreadList) {
				uiMessageThreadList.add(this.convertToUiMessageThread(activeUser, tmpMessageThread, isHost, getReservation));
			}
		}
		return uiMessageThreadList;
	}
	
	private UiMessageThread convertToUiMessageThread(User activeUser, MessageThread messageThread, boolean isHost, boolean getReservation) {
		
		User clientUser = null;
		User hostUser = null;
		
		if (isHost) {
			clientUser = messageThread.getClientUser();
			hostUser = activeUser;
		} else {
			clientUser = activeUser;
			hostUser = messageThread.getHostUser();
		}
		
		UiMessageThread newUiMessageThread = new UiMessageThread();
		
		newUiMessageThread.setId(messageThread.getId());
		newUiMessageThread.setThreadTitle(messageThread.getThreadTitle());
		
		newUiMessageThread.setActiveUserId(activeUser.getId());
		
		String clientUserProfilePhotoUrl = this.webApplication.getUserPhotoUrl(clientUser.getId(), clientUser.getProfilePhotoId(), EnmSize.SMALL.getValue());
		String clientUserProfilePhotoUrlMedium = clientUserProfilePhotoUrl.replace("_small_", "_large_");
		
		newUiMessageThread.setClientUserId(clientUser.getId());
		newUiMessageThread.setClientUserFirstName(clientUser.getFirstName());
		newUiMessageThread.setClientUserProfilePhotoUrl(clientUserProfilePhotoUrl);
		newUiMessageThread.setClientUserProfilePhotoUrlMedium(clientUserProfilePhotoUrlMedium);
		
		String hostUserProfilePhotoUrl = this.webApplication.getUserPhotoUrl(hostUser.getId(), hostUser.getProfilePhotoId(), EnmSize.SMALL.getValue());
		String hostUserProfilePhotoUrlMedium = hostUserProfilePhotoUrl.replace("_small_", "_large_");
		
		newUiMessageThread.setHostUserId(hostUser.getId());
		newUiMessageThread.setHostUserFirstName(hostUser.getFirstName());
		newUiMessageThread.setHostUserProfilePhotoUrl(hostUserProfilePhotoUrl);
		newUiMessageThread.setHostUserProfilePhotoUrlMedium(hostUserProfilePhotoUrlMedium);
						
		newUiMessageThread.setMessageText("This is a test");
		newUiMessageThread.setMessageDate(new Date());
		
		if (getReservation) {
			Reservation reservation = new Reservation();
			reservation.setMessageThreadId(messageThread.getId());
			List<Reservation> reservationList = this.reservationService.list(reservation, true, false, false);
			if (reservationList != null && reservationList.size() > 0) {
				reservation = reservationList.get(0);
				Place uiPlace = new Place();
				String coverPhotoUrl = this.webApplication.getPlacePhotoUrl(reservation.getPlace().getId(), reservation.getPlace().getCoverPhotoId(), EnmSize.SMALL.getValue());
				uiPlace.setCoverPhotoUrl(coverPhotoUrl);
				reservation.setPlace(uiPlace);
				newUiMessageThread.setReservation(reservation);
				newUiMessageThread.setReservationPriceText(NumberUtil.format(reservation.getPlacePrice(), ",", ".", 2));
			}
		}
				
		return newUiMessageThread;
	}
	
}
