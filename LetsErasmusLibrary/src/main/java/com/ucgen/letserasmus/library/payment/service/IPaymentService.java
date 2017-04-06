package com.ucgen.letserasmus.library.payment.service;

import java.util.List;

import com.ucgen.common.operationresult.OperationResult;
import com.ucgen.letserasmus.library.payment.model.PaymentMethod;

public interface IPaymentService {

	OperationResult insertPaymentMethod(PaymentMethod paymentMethod);
	
	PaymentMethod getPaymentMethod(PaymentMethod paymentMethod);
	
	List<PaymentMethod> listPaymentMethod(PaymentMethod paymentMethod);
	
}
