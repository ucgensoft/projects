package com.ucgen.letserasmus.library.payment.dao;

import java.util.List;

import com.ucgen.common.operationresult.OperationResult;
import com.ucgen.letserasmus.library.payment.model.PaymentMethod;

public interface IPaymentDao {

	OperationResult insertPaymentMethod(PaymentMethod paymentMethod);

	PaymentMethod getPaymentMethod(PaymentMethod paymentMethod);
	
	List<PaymentMethod> listPaymentMethod(PaymentMethod paymentMethod);
	
}
