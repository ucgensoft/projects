package com.ucgen.letserasmus.library.bluesnap.service;

import com.ucgen.common.operationresult.ValueOperationResult;
import com.ucgen.letserasmus.library.bluesnap.model.ExtVendor;

public interface IExtPaymentService {

	ValueOperationResult<Long> createVendor(ExtVendor extVendor);
	
}
