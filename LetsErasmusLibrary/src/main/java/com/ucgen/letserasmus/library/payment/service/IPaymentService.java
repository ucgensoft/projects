package com.ucgen.letserasmus.library.payment.service;

import java.util.List;

import com.ucgen.common.operationresult.OperationResult;
import com.ucgen.letserasmus.library.payment.model.PaymentMethod;
import com.ucgen.letserasmus.library.payment.model.PayoutMethod;

public interface IPaymentService {

	OperationResult insertPaymentMethod(PaymentMethod paymentMethod);
	
	PaymentMethod getPaymentMethod(PaymentMethod paymentMethod);
	
	List<PaymentMethod> listPaymentMethod(PaymentMethod paymentMethod);
	
	List<PayoutMethod> listPayoutMethod(PayoutMethod payoutMethod);
	
	PayoutMethod getPayoutMethod(PayoutMethod payoutMethod);
	
	OperationResult createPayoutMethodDraft(PayoutMethod payoutMethod);
	
}
