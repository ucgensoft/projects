package com.ucgen.letserasmus.web.api.message;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

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
import com.ucgen.common.util.NumberUtil;
import com.ucgen.letserasmus.library.common.enumeration.EnmEntityType;
import com.ucgen.letserasmus.library.common.enumeration.EnmSize;
import com.ucgen.letserasmus.library.message.enumeration.EnmMessageStatus;
import com.ucgen.letserasmus.library.message.model.Message;
import com.ucgen.letserasmus.library.message.model.MessageThread;
import com.ucgen.letserasmus.library.message.service.IMessageService;
import com.ucgen.letserasmus.library.reservation.model.Reservation;
import com.ucgen.letserasmus.library.reservation.service.IReservationService;
import com.ucgen.letserasmus.library.user.model.User;
import com.ucgen.letserasmus.web.api.BaseApiController;
import com.ucgen.letserasmus.web.view.application.EnmSession;
import com.ucgen.letserasmus.web.view.application.WebApplication;

@RestController
public class ApiMessageController extends BaseApiController {

	private IMessageService messageService;
	private IReservationService reservationService;
	
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
	public void setWebApplication(WebApplication webApplication) {
		this.webApplication = webApplication;
	}
	
	@RequestMapping(value = "/api/message/list", method = RequestMethod.GET)
    public ResponseEntity<ValueOperationResult<String>> listMessage(@RequestParam Integer messageType, HttpSession session) {
		ValueOperationResult<String> operationResult = new ValueOperationResult<String>();
		
		try {
			User user = super.getSessionUser(session);
			if (user != null) {
				//Object activeOperation = super.getSession().getAttribute(EnmSession.ACTIVE_OPERATION.getId());
				
				if (messageType != null && (messageType.equals(1) || messageType.equals(2))) {
					
					
					operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());;
				} else {
					operationResult.setResultCode(EnmResultCode.ERROR.getValue());
					operationResult.setResultDesc("Mandatory parameters are missing or invalid!");
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
	
	@RequestMapping(value = "/api/message/listthread", method = RequestMethod.GET)
    public ResponseEntity<ValueOperationResult<Map<String, List<UiMessageThread>>>> listMessageThread(@RequestParam("entityType") Integer entityType, 
    		@RequestParam("hostFlag") Integer hostFlag, @RequestParam("clientFlag") Integer clientFlag, HttpSession session) {
		ValueOperationResult<Map<String, List<UiMessageThread>>> operationResult = new ValueOperationResult<Map<String, List<UiMessageThread>>>();
		
		try {
			User user = super.getSessionUser(session);
			if (user != null) {
				Object activeOperation = super.getSession().getAttribute(EnmSession.ACTIVE_OPERATION.getId());
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
				operationResult.setResultCode(EnmResultCode.ERROR.getValue());
				operationResult.setResultDesc("You are not logged in or session is expired. Please login first.");
			}
		} catch (Exception e) {
			operationResult.setResultCode(EnmResultCode.EXCEPTION.getValue());
			operationResult.setResultDesc("Create operation could not be completed. Please try again later!");
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
				messageThread.setEntityType(EnmEntityType.PLACE.getId());
				
				MessageThread dbMessageThread = this.messageService.getMessageThread(messageThread, true, true, true);
				
				if (dbMessageThread != null) {
					if (user.getId().equals(dbMessageThread.getHostUserId()) 
							|| user.getId().equals(dbMessageThread.getClientUserId())) {
						boolean isHost = (user.getId().equals(dbMessageThread.getHostUserId()));
						UiMessageThread uiMessageThread = this.convertToUiMessageThread(user, dbMessageThread, isHost, true);
						
						Message message = new Message();
						message.setMessageThreadId(threadId);
												
						List<Message> messageList = this.messageService.listMessage(message, false, false);
						uiMessageThread.setMessageList(messageList);
						
						operationResult.setResultValue(uiMessageThread);
						operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
					} else {
						operationResult.setResultCode(EnmResultCode.ERROR.getValue());
						operationResult.setResultDesc("Unauthorized operation!");
					}
				} else {
					operationResult.setResultCode(EnmResultCode.ERROR.getValue());
					operationResult.setResultDesc("Message thread is not found!");
				}			
			} else {
				operationResult.setResultCode(EnmResultCode.ERROR.getValue());
				operationResult.setResultDesc("You are not logged in or session is expired. Please login first.");
			}
		} catch (Exception e) {
			operationResult.setResultCode(EnmResultCode.EXCEPTION.getValue());
			operationResult.setResultDesc("Create operation could not be completed. Please try again later!");
		}
		return new ResponseEntity<ValueOperationResult<UiMessageThread>>(operationResult, HttpStatus.OK);
    }
	
	@RequestMapping(value = "/api/message/send", method = RequestMethod.POST)
    public ResponseEntity<ValueOperationResult<Message>> sendMessage(@RequestBody Message uiMessage, HttpSession session) {
		ValueOperationResult<Message> operationResult = new ValueOperationResult<Message>();
		
		try {
			User user = super.getSessionUser(session);
			if (user != null) {
				if (uiMessage.getMessageThreadId() != null 
						&& uiMessage.getMessageText() != null 
						&& !uiMessage.getMessageText().trim().isEmpty()) {
					MessageThread messageThread = new MessageThread();
					
					messageThread.setId(uiMessage.getMessageThreadId());
					messageThread.setEntityType(EnmEntityType.PLACE.getId());
					
					MessageThread dbMessageThread = this.messageService.getMessageThread(messageThread, false, false, false);
					
					if (dbMessageThread != null) {
						if (user.getId().equals(dbMessageThread.getHostUserId()) 
								|| user.getId().equals(dbMessageThread.getClientUserId())) {
							
							Message message = new Message();
							message.setMessageThreadId(dbMessageThread.getId());
							message.setSenderUserId(user.getId());
							if (user.getId().equals(dbMessageThread.getHostUserId())) {
								message.setReceiverUserId(dbMessageThread.getClientUserId());
							} else {
								message.setReceiverUserId(dbMessageThread.getHostUserId());
							}
							message.setMessageText(uiMessage.getMessageText());
							message.setStatus(EnmMessageStatus.NOT_READ.getId());
							message.setCreatedDate(new Date());
							message.setCreatedBy(user.getFullName());
													
							OperationResult insertMessageResult = this.messageService.insertMessage(message);
							if (OperationResult.isResultSucces(insertMessageResult)) {
								operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
								operationResult.setResultValue(message);
							} else {
								operationResult.setResultCode(EnmResultCode.ERROR.getValue());
								operationResult.setResultDesc("Message could not be saved. Please try again later!");
							}
						} else {
							operationResult.setResultCode(EnmResultCode.ERROR.getValue());
							operationResult.setResultDesc("Unauthorized operation!");
						}
					} else {
						operationResult.setResultCode(EnmResultCode.ERROR.getValue());
						operationResult.setResultDesc("Message thread is not found!");
					}
				} else {
					operationResult.setResultCode(EnmResultCode.ERROR.getValue());
					operationResult.setResultDesc("Missing mandatory parameters !");
				}
			} else {
				operationResult.setResultCode(EnmResultCode.ERROR.getValue());
				operationResult.setResultDesc("You are not logged in or session is expired. Please login first.");
			}
		} catch (Exception e) {
			operationResult.setResultCode(EnmResultCode.EXCEPTION.getValue());
			operationResult.setResultDesc("Create operation could not be completed. Please try again later!");
		}
		return new ResponseEntity<ValueOperationResult<Message>>(operationResult, HttpStatus.OK);
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
		
		String clientUserProfilePhotoUrl = this.webApplication.getUserProfilePhotoUrl(clientUser.getId(), clientUser.getProfilePhotoId(), EnmSize.SMALL.getValue());
		String clientUserProfilePhotoUrlMedium = clientUserProfilePhotoUrl.replace("_small_", "_large_");
		
		newUiMessageThread.setClientUserId(clientUser.getId());
		newUiMessageThread.setClientUserFirstName(clientUser.getFirstName());
		newUiMessageThread.setClientUserProfilePhotoUrl(clientUserProfilePhotoUrl);
		newUiMessageThread.setClientUserProfilePhotoUrlMedium(clientUserProfilePhotoUrlMedium);
		
		String hostUserProfilePhotoUrl = this.webApplication.getUserProfilePhotoUrl(hostUser.getId(), hostUser.getProfilePhotoId(), EnmSize.SMALL.getValue());
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
			List<Reservation> reservationList = this.reservationService.list(reservation, false, false, false);
			if (reservationList != null && reservationList.size() > 0) {
				reservation = reservationList.get(0);
				newUiMessageThread.setReservation(reservation);
				newUiMessageThread.setReservationPriceText(NumberUtil.format(reservation.getPlacePrice(), ',', '.', 2));
			}
		}
				
		return newUiMessageThread;
	}
	
}
