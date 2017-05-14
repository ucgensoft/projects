package com.ucgen.letserasmus.library.stripe.service;

import java.math.BigDecimal;
import java.util.Date;

import com.stripe.model.Charge;
import com.stripe.model.Reversal;
import com.stripe.model.Transfer;
import com.ucgen.common.operationresult.OperationResult;
import com.ucgen.common.operationresult.ValueOperationResult;
import com.ucgen.letserasmus.library.payment.model.PaymentMethod;
import com.ucgen.letserasmus.library.payment.model.PayoutMethod;

public interface IStripePaymentService {

	ValueOperationResult<String> createManagedAccount(PayoutMethod payoutMethod, String tosAcceptIp, Date tosAcceptDate);
	
	ValueOperationResult<Charge> createCharge(PayoutMethod payoutMethod, PaymentMethod paymentMethod, String operationBy);
	
	OperationResult updateCharge(Long userId, String chargeId, BigDecimal newAmount, BigDecimal newVendorAmount, String operationBy);
	
	ValueOperationResult<Charge> getCharge(Long userId, String chargeId, String operationBy);
	
	ValueOperationResult<String> capture(Long userId, String chargeId, String operationBy);
	
	OperationResult refund(Long userId, String chargeId, String transferId, BigDecimal clientRefundAmount, BigDecimal vendorRefundAmount, String operationBy);
	
	OperationResult createPayout(Long userId, BigDecimal payoutAmount, String currencyCode, String operationBy);
	
	ValueOperationResult<Transfer> getTransfer(Long userId, String transferId, String operationBy);
	
	ValueOperationResult<Reversal> createReverseTransfer(Long userId, String transferId, BigDecimal amount, String operationBy);
	
}
