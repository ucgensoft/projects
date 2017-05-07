package com.ucgen.letserasmus.library.stripe.service;

import java.math.BigDecimal;

import com.stripe.model.Charge;
import com.ucgen.common.operationresult.OperationResult;
import com.ucgen.common.operationresult.ValueOperationResult;
import com.ucgen.letserasmus.library.payment.model.PaymentMethod;
import com.ucgen.letserasmus.library.payment.model.PayoutMethod;

public interface IStripePaymentService {

	ValueOperationResult<String> createManagedAccount(PayoutMethod payoutMethod);
	
	ValueOperationResult<String> charge(PayoutMethod payoutMethod, PaymentMethod paymentMethod, String operationBy);
	
	OperationResult updateCharge(Long userId, String chargeId, BigDecimal newAmount, BigDecimal newVendorAmount, String operationBy);
	
	ValueOperationResult<Charge> getCharge(Long userId, String chargeId, String operationBy);
	
	OperationResult capture(Long userId, String chargeId, String operationBy);
	
	OperationResult refund(Long userId, String chargeId, BigDecimal refundAmount, BigDecimal vendorAmount, String operationBy);
	
}
