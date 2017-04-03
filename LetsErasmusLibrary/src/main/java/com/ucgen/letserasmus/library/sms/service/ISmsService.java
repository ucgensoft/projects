package com.ucgen.letserasmus.library.sms.service;

import com.ucgen.common.operationresult.OperationResult;

public interface ISmsService {

	public OperationResult sendSms(Long userId, String messageText, String toPhoneNumber, String createdBy);
	
}
