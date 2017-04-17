package com.ucgen.letserasmus.library.bluesnap.service;

import com.ucgen.common.operationresult.OperationResult;
import com.ucgen.common.operationresult.ValueOperationResult;
import com.ucgen.letserasmus.library.bluesnap.model.ExtVendorInfo;
import com.ucgen.letserasmus.library.payment.model.PaymentMethod;
import com.ucgen.letserasmus.library.payment.model.PayoutMethod;

public interface IExtPaymentService {

	ValueOperationResult<Long> createVendorDraft(Long userId, String email, String countryCode, String operationBy);
	
	ValueOperationResult<Long> createVendor(Long userId, ExtVendorInfo extVendor);
	
	ValueOperationResult<String> getPaymentFieldToken(Long userId, String operationBy);
	
	ValueOperationResult<String> paymentAuth(Long userId, PaymentMethod paymentMethod, PayoutMethod payoutMethod, String operationBy);

	OperationResult paymentAuthReversal(Long userId, String blueSnapTransactionId, String operationBy);
	
	OperationResult paymentCapture(Long userId, String blueSnapTransactionId, String operationBy);
	
}
