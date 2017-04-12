package com.ucgen.letserasmus.library.bluesnap.service;

import com.ucgen.common.operationresult.ValueOperationResult;
import com.ucgen.letserasmus.library.bluesnap.model.ExtVendor;

public interface IExtPaymentService {

	ValueOperationResult<Long> createVendorDraft(Long userId, String email, String countryCode, String operationBy);
	
	ValueOperationResult<Long> createVendor(Long userId, ExtVendor extVendor);
	
	ValueOperationResult<String> getPaymentFieldToken(Long userId, String operationBy);
	
}
