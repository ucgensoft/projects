package com.ucgen.letserasmus.library.payment.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ucgen.common.operationresult.EnmResultCode;
import com.ucgen.common.operationresult.OperationResult;
import com.ucgen.common.operationresult.ValueOperationResult;
import com.ucgen.letserasmus.library.bluesnap.service.IExtPaymentService;
import com.ucgen.letserasmus.library.common.enumeration.EnmBoolStatus;
import com.ucgen.letserasmus.library.log.enumeration.EnmExternalSystem;
import com.ucgen.letserasmus.library.payment.dao.IPaymentDao;
import com.ucgen.letserasmus.library.payment.model.PaymentMethod;
import com.ucgen.letserasmus.library.payment.model.PayoutMethod;
import com.ucgen.letserasmus.library.payment.service.IPaymentService;
import com.ucgen.letserasmus.library.simpleobject.model.Country;
import com.ucgen.letserasmus.library.simpleobject.service.ISimpleObjectService;
import com.ucgen.letserasmus.library.stripe.service.IStripePaymentService;

@Service
public class PaymentService implements IPaymentService {

	private IExtPaymentService extPaymentService;
	private IPaymentDao paymentDao;
	private IStripePaymentService stripePaymentService;
	private ISimpleObjectService simpleObjectService;
	
	@Autowired
	public void setPaymentDao(IPaymentDao paymentDao) {
		this.paymentDao = paymentDao;
	}
	
	@Autowired
	public void setExtPaymentService(IExtPaymentService extPaymentService) {
		this.extPaymentService = extPaymentService;
	}

	@Autowired
	public void setStripePaymentService(IStripePaymentService stripePaymentService) {
		this.stripePaymentService = stripePaymentService;
	}

	@Autowired
	public void setSimpleObjectService(ISimpleObjectService simpleObjectService) {
		this.simpleObjectService = simpleObjectService;
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

	@Override
	public List<PayoutMethod> listPayoutMethod(PayoutMethod payoutMethod) {
		return this.paymentDao.listPayoutMethod(payoutMethod);
	}

	@Override
	public PayoutMethod getPayoutMethod(PayoutMethod payoutMethod) {
		return this.paymentDao.getPayoutMethod(payoutMethod);
	}

	@Override
	@Transactional
	public OperationResult createPayoutMethodDraft(PayoutMethod payoutMethod) {
		OperationResult operationResult = new OperationResult();
		
		String countryIsoCode2 = payoutMethod.getVendorCountry().toUpperCase();
		
		Country vendorCountry = this.simpleObjectService.getCountryWithIsoCode2(countryIsoCode2);
		
		if (vendorCountry != null) {
			if (vendorCountry.getStripeSupportFlag().equals(EnmBoolStatus.YES.getId())) {
				ValueOperationResult<String> createAccountResult = this.stripePaymentService.createManagedAccount(payoutMethod);
				if (OperationResult.isResultSucces(createAccountResult)) {
					payoutMethod.setExternalSystemId(EnmExternalSystem.STRIPE.getId());
					payoutMethod.setStripeAccountId(createAccountResult.getResultValue());
					OperationResult createPayoutResult = this.paymentDao.insertPayoutMethod(payoutMethod);
					if (OperationResult.isResultSucces(createPayoutResult)) {
						operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
					} else {
						operationResult.setResultCode(EnmResultCode.ERROR.getValue());
						operationResult.setResultDesc("Stripe account created, payout method creation failed. Error: " + OperationResult.getResultDesc(createPayoutResult));
					}
				} else {
					operationResult.setResultCode(EnmResultCode.ERROR.getValue());
					operationResult.setResultDesc("Stripe account creation failed. Error: " + OperationResult.getResultDesc(createAccountResult));
				}
			} else {
				ValueOperationResult<Long> createVendorResult = this.extPaymentService.createVendorDraft(payoutMethod.getUserId(), payoutMethod.getVendorEmail(), payoutMethod.getBankCountry(), payoutMethod.getCreatedBy());
				if (OperationResult.isResultSucces(createVendorResult)) {
					payoutMethod.setExternalSystemId(EnmExternalSystem.BLUESNAP.getId());
					payoutMethod.setBlueSnapVendorId(createVendorResult.getResultValue());
					OperationResult createPayoutResult = this.paymentDao.insertPayoutMethod(payoutMethod);
					if (OperationResult.isResultSucces(createPayoutResult)) {
						operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
					} else {
						operationResult.setResultCode(EnmResultCode.ERROR.getValue());
						operationResult.setResultDesc("Bluesnap vendor created, payout method creation failed. Error: " + OperationResult.getResultDesc(createPayoutResult));
					}
				} else {
					operationResult.setResultCode(EnmResultCode.ERROR.getValue());
					operationResult.setResultDesc("Bluesnap vendor creation failed. Error: " + OperationResult.getResultDesc(createVendorResult));
				}
			}
		} else {
			operationResult.setResultCode(EnmResultCode.ERROR.getValue());
			operationResult.setResultDesc("Country information not found for isoCode2: " + countryIsoCode2);
		}
		return operationResult;
	}

	@Override
	public OperationResult updatePayoutMethod(PayoutMethod payoutMethod) {
		return this.paymentDao.updatePayoutMethod(payoutMethod);
	}

}
