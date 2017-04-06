package com.ucgen.letserasmus.library.payment.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ucgen.common.operationresult.OperationResult;
import com.ucgen.letserasmus.library.payment.dao.IPaymentDao;
import com.ucgen.letserasmus.library.payment.model.PaymentMethod;
import com.ucgen.letserasmus.library.payment.service.IPaymentService;

@Service
public class PaymentService implements IPaymentService {

	private IPaymentDao paymentDao;
	
	@Autowired
	public void setPaymentDao(IPaymentDao paymentDao) {
		this.paymentDao = paymentDao;
	}

	@Override
	public OperationResult insertPaymentMethod(PaymentMethod paymentMethod) {
		return this.paymentDao.insertPaymentMethod(paymentMethod);
	}

	@Override
	public PaymentMethod getPaymentMethod(PaymentMethod paymentMethod) {
		return this.paymentDao.getPaymentMethod(paymentMethod);
	}

	@Override
	public List<PaymentMethod> listPaymentMethod(PaymentMethod paymentMethod) {
		return this.paymentDao.listPaymentMethod(paymentMethod);
	}

}
