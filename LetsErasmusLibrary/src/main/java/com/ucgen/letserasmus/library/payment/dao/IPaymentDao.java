package com.ucgen.letserasmus.library.payment.dao;

import java.util.List;

import com.ucgen.common.operationresult.OperationResult;
import com.ucgen.letserasmus.library.payment.model.PaymentMethod;
import com.ucgen.letserasmus.library.payment.model.PayoutMethod;

public interface IPaymentDao {

	OperationResult insertPaymentMethod(PaymentMethod paymentMethod);

	PaymentMethod getPaymentMethod(PaymentMethod paymentMethod);
	
	List<PaymentMethod> listPaymentMethod(PaymentMethod paymentMethod);
	
	List<PayoutMethod> listPayoutMethod(PayoutMethod payoutMethod);
	
	PayoutMethod getPayoutMethod(PayoutMethod payoutMethod);
	
	OperationResult insertPayoutMethod(PayoutMethod payoutMethod);
	
}
