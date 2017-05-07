package com.ucgen.letserasmus.library.sms.service.impl;

import java.util.Date;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.api.v2010.account.Message.Status;
import com.twilio.type.PhoneNumber;
import com.ucgen.common.operationresult.EnmResultCode;
import com.ucgen.common.operationresult.OperationResult;
import com.ucgen.common.util.CommonUtil;
import com.ucgen.letserasmus.library.log.enumeration.EnmExternalSystem;
import com.ucgen.letserasmus.library.log.enumeration.EnmOperation;
import com.ucgen.letserasmus.library.log.model.IntegrationLog;
import com.ucgen.letserasmus.library.log.service.ILogService;
import com.ucgen.letserasmus.library.parameter.enumeration.EnmParameter;
import com.ucgen.letserasmus.library.parameter.service.IParameterService;
import com.ucgen.letserasmus.library.sms.service.ISmsService;

@Service
public class SmsService implements ISmsService {
	
	private String accountSid = null;
	private String authToken = null;
	
	private IParameterService parameterService;
	private ILogService logService;
	
	@Autowired
	public void setParameterService(IParameterService parameterService) {
		this.parameterService = parameterService;
	}
	
	@Autowired
	public void setLogService(ILogService logService) {
		this.logService = logService;
	}

	@PostConstruct
	public void initialize() {
		accountSid = this.parameterService.getParameterValue(EnmParameter.TWILIO_ACCOUNT_SID.getId());
		authToken = this.parameterService.getParameterValue(EnmParameter.TWILIO_AUTH_TOKEN.getId());
		Twilio.init(accountSid, authToken);
	}


	@Override
	public OperationResult sendSms(Long userId, String messageText, String toPhoneNumber, String createdBy) {
		OperationResult operationResult = new OperationResult();
		Date sendStartDate = null;
		Date sendEndDate = null;
		String request = null;
		String response = null;
		String responseCode = null; 
		try {
			String fromPhoneNumber = this.parameterService.getParameterValue(EnmParameter.TWILIO_FROM_PHONE_NUMBER.getId());
			
			request = "AccountSid: " + this.accountSid + ", authToken: " + this.authToken + ", From: " 
					+ fromPhoneNumber + ", To: " + toPhoneNumber + ", Message:" + messageText;
			
			sendStartDate = new Date();
			Message message = Message.creator(new PhoneNumber(toPhoneNumber), new PhoneNumber(fromPhoneNumber), messageText).create();
			sendEndDate = new Date();
			
			Status messageStatus = message.getStatus();
			
			response = message.toString();
			responseCode = messageStatus.name();
			
			if (messageStatus == Status.DELIVERED || messageStatus == Status.SENT || messageStatus == Status.QUEUED) {
				operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
			} else {
				operationResult.setResultCode(EnmResultCode.ERROR.getValue());
			}
		} catch (Exception e) {
			response = CommonUtil.getExceptionMessage(e);
			responseCode = String.valueOf(EnmResultCode.EXCEPTION.getValue());
			
			operationResult.setResultCode(EnmResultCode.EXCEPTION.getValue());
			operationResult.setResultDesc(CommonUtil.getExceptionMessage(e));
		} finally {
			if (sendStartDate != null && request != null) {
				if (sendEndDate == null) {
					sendEndDate = new Date();
				}
				IntegrationLog integrationLog = new IntegrationLog();
				integrationLog.setUserId(userId);
				integrationLog.setExtSystemId(EnmExternalSystem.TWILIO.getId());
				integrationLog.setOperationId(EnmOperation.SEND_VERIFICATION_SMS.getId());
				integrationLog.setOperationDate(sendStartDate);
				integrationLog.setDuration(sendEndDate.getTime() - sendStartDate.getTime());
				integrationLog.setRequest(request);
				integrationLog.setResponse(response);
				integrationLog.setResponse(response);
				integrationLog.setResponseCode(responseCode);
				integrationLog.setCreatedBy(createdBy);
				integrationLog.setCreatedDate(new Date());
				this.logService.insertIntegrationLog(integrationLog);
			}
		}
		return operationResult;
	}

}
