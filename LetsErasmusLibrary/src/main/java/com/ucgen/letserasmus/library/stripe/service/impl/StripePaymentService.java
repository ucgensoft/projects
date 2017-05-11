package com.ucgen.letserasmus.library.stripe.service.impl;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stripe.Stripe;
import com.stripe.model.Account;
import com.stripe.model.Charge;
import com.stripe.model.Refund;
import com.ucgen.common.operationresult.EnmResultCode;
import com.ucgen.common.operationresult.OperationResult;
import com.ucgen.common.operationresult.ValueOperationResult;
import com.ucgen.common.util.CommonUtil;
import com.ucgen.common.util.DateUtil;
import com.ucgen.letserasmus.library.log.enumeration.EnmExternalSystem;
import com.ucgen.letserasmus.library.log.enumeration.EnmOperation;
import com.ucgen.letserasmus.library.log.model.IntegrationLog;
import com.ucgen.letserasmus.library.log.service.ILogService;
import com.ucgen.letserasmus.library.parameter.enumeration.EnmParameter;
import com.ucgen.letserasmus.library.parameter.service.IParameterService;
import com.ucgen.letserasmus.library.payment.enumeration.EnmVendorEntityType;
import com.ucgen.letserasmus.library.payment.model.PaymentMethod;
import com.ucgen.letserasmus.library.payment.model.PayoutMethod;
import com.ucgen.letserasmus.library.stripe.service.IStripePaymentService;

@Service
public class StripePaymentService implements IStripePaymentService {

	private IParameterService parameterService;
	private ILogService logService;
	
	@Autowired
	public void setParameterService(IParameterService parameterService) {
		this.parameterService = parameterService;
	}

	@Autowired
	public void setLogService(ILogService logService) {
		this.logService = logService;
	}

	@Override
	public ValueOperationResult<String> createManagedAccount(PayoutMethod payoutMethod) {
		ValueOperationResult<String> operationResult = new ValueOperationResult<String>();
		
		StringBuilder request = new StringBuilder();
		StringBuilder response = new StringBuilder();
		String responseCode = null;
		Date startDate = null;
		Date endDate = null;
		
		try {
			String stripePrivateKey = this.parameterService.getParameterValue(EnmParameter.STRIPE_PRIVATE_KEY.getId());
			Stripe.apiKey = stripePrivateKey;
			
			Map<String, Object> dateOfBirthParams = new HashMap<String, Object>();
			dateOfBirthParams.put("day", DateUtil.getDatePart(payoutMethod.getVendorBirthDate(), Calendar.DATE));
			dateOfBirthParams.put("month", DateUtil.getDatePart(payoutMethod.getVendorBirthDate(), Calendar.MONTH));
			dateOfBirthParams.put("year", DateUtil.getDatePart(payoutMethod.getVendorBirthDate(), Calendar.YEAR));
			
			Map<String, Object> addressParams = new HashMap<String, Object>();
			addressParams.put("country", payoutMethod.getVendorCountry());
			addressParams.put("city", payoutMethod.getVendorCity());
			addressParams.put("line1", payoutMethod.getVendorAddress());
			addressParams.put("line2", payoutMethod.getVendorAddress2());
			addressParams.put("postal_code", payoutMethod.getVendorZip());
			
			Map<String, Object> legalEntityParams = new HashMap<String, Object>();
			legalEntityParams.put("first_name", payoutMethod.getVendorFirstName());
			legalEntityParams.put("last_name", payoutMethod.getVendorLastName());
			legalEntityParams.put("dob", dateOfBirthParams);
			legalEntityParams.put("address", addressParams);
			if (payoutMethod.getVendorEntityType().equals(EnmVendorEntityType.PERSONAL.getId())) {
				legalEntityParams.put("type", "individual");
			} else {
				legalEntityParams.put("type", "company");
			}
			
			Map<String, Object> payoutScheduleParams = new HashMap<String, Object>();
			payoutScheduleParams.put("interval", "manual");
			
			Map<String, Object> accountParams = new HashMap<String, Object>();
			accountParams.put("country", payoutMethod.getVendorCountry());
			accountParams.put("managed", true);
			accountParams.put("legal_entity", legalEntityParams);
			accountParams.put("payout_schedule", payoutScheduleParams);

			ObjectMapper objectMapper = new ObjectMapper();
		    
		    String strAccountParameters = objectMapper.writeValueAsString(accountParams);
		    		     
		    request.append("private key : " + stripePrivateKey);
		    request.append(System.lineSeparator());
		    request.append("request : " + strAccountParameters);
		    
		    startDate = new Date();
			Account newAccount = Account.create(accountParams);
			responseCode = "0";
			operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
			operationResult.setResultValue(newAccount.getId());
			endDate = new Date();
			
			response.append(objectMapper.writeValueAsString(newAccount)); 
			
		} catch (Exception e) {
			operationResult.setResultCode(EnmResultCode.EXCEPTION.getValue());
			operationResult.setResultDesc(CommonUtil.getExceptionMessage(e));
			responseCode = "-1";
			response.append(CommonUtil.getExceptionMessage(e));
		}  finally {
			if (startDate != null && request != null) {
				if (endDate == null) {
					endDate = new Date();
				}
				IntegrationLog integrationLog = new IntegrationLog();
				integrationLog.setUserId(payoutMethod.getUserId());
				integrationLog.setExtSystemId(EnmExternalSystem.STRIPE.getId());
				integrationLog.setOperationId(EnmOperation.STRIPE_CREATE_ACCOUNT.getId());
				integrationLog.setOperationDate(startDate);
				integrationLog.setDuration(endDate.getTime() - startDate.getTime());
				integrationLog.setRequest(request.toString());
				integrationLog.setResponse(response.toString());
				integrationLog.setResponseCode(responseCode);
				integrationLog.setCreatedBy(payoutMethod.getCreatedBy());
				integrationLog.setCreatedDate(new Date());
				this.logService.insertIntegrationLog(integrationLog);
			}
		}
		
		return operationResult;
	}
	
	@Override
	public ValueOperationResult<String> charge(PayoutMethod payoutMethod, PaymentMethod paymentMethod, String operationBy) {
		ValueOperationResult<String> operationResult = new ValueOperationResult<String>();
		
		StringBuilder request = new StringBuilder();
		StringBuilder response = new StringBuilder();
		String responseCode = null;
		Date startDate = null;
		Date endDate = null;
		
		try {
			String stripePrivateKey = this.parameterService.getParameterValue(EnmParameter.STRIPE_PRIVATE_KEY.getId());
			Stripe.apiKey = stripePrivateKey;
			
			BigDecimal centMultiplier = new BigDecimal(100);
			
			Integer chargeAmount = paymentMethod.getPayment().getEntityPrice().add(paymentMethod.getPayment().getServiceFee()).multiply(centMultiplier).intValue();
			Integer destinationAmount = paymentMethod.getPayment().getEntityPrice().subtract(paymentMethod.getPayment().getCommissionFee()).multiply(centMultiplier).intValue();
			
			Map<String, Object> destinationParams = new HashMap<String, Object>();
			destinationParams.put("account", payoutMethod.getStripeAccountId());
			destinationParams.put("amount", destinationAmount);
			
			Map<String, Object> chargeParams = new HashMap<String, Object>();
			chargeParams.put("amount", chargeAmount);
			chargeParams.put("source", paymentMethod.getPayment().getCardInfoToken());
			chargeParams.put("currency", paymentMethod.getPayment().getCurrencyCode());
			chargeParams.put("capture", false);
			chargeParams.put("description", "Charge for LetsErasmus reservation.");
			chargeParams.put("destination", destinationParams);
									
			ObjectMapper objectMapper = new ObjectMapper();
		    
		    String strChargeParameters = objectMapper.writeValueAsString(chargeParams);
		    		     
		    request.append("private key : " + stripePrivateKey);
		    request.append(System.lineSeparator());
		    request.append("request : " + strChargeParameters);
		    
		    startDate = new Date();
			Charge newCharge = Charge.create(chargeParams);
			responseCode = "0";
			operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
			operationResult.setResultValue(newCharge.getId());
			endDate = new Date();
			
			response.append(objectMapper.writeValueAsString(newCharge)); 
			
		} catch (Exception e) {
			operationResult.setResultCode(EnmResultCode.EXCEPTION.getValue());
			operationResult.setResultDesc(CommonUtil.getExceptionMessage(e));
			responseCode = "-1";
			response.append(CommonUtil.getExceptionMessage(e));
		}  finally {
			if (startDate != null && request != null) {
				if (endDate == null) {
					endDate = new Date();
				}
				IntegrationLog integrationLog = new IntegrationLog();
				integrationLog.setUserId(paymentMethod.getUserId());
				integrationLog.setExtSystemId(EnmExternalSystem.STRIPE.getId());
				integrationLog.setOperationId(EnmOperation.STRIPE_CHARGE.getId());
				integrationLog.setOperationDate(startDate);
				integrationLog.setDuration(endDate.getTime() - startDate.getTime());
				integrationLog.setRequest(request.toString());
				integrationLog.setResponse(response.toString());
				integrationLog.setResponseCode(responseCode);
				integrationLog.setCreatedBy(operationBy);
				integrationLog.setCreatedDate(new Date());
				this.logService.insertIntegrationLog(integrationLog);
			}
		}
		
		return operationResult;
	}
	
	@Override
	public OperationResult updateCharge(Long userId, String chargeId, BigDecimal newAmount, BigDecimal newVendorAmount, String operationBy) {
		OperationResult operationResult = new OperationResult();
		
		StringBuilder request = new StringBuilder();
		StringBuilder response = new StringBuilder();
		String responseCode = null;
		Date startDate = null;
		Date endDate = null;
		
		try {
			String stripePrivateKey = this.parameterService.getParameterValue(EnmParameter.STRIPE_PRIVATE_KEY.getId());
			Stripe.apiKey = stripePrivateKey;
			
			BigDecimal centMultiplier = new BigDecimal(100);
			
			ValueOperationResult<Charge> getChargeResult = this.getCharge(userId, chargeId, operationBy);
			
			if (OperationResult.isResultSucces(getChargeResult)) {
				Charge charge = getChargeResult.getResultValue();
				Map<String, Object> updateParams = new HashMap<String, Object>();
				updateParams.put("description", "Charge for andrew.miller@example.com");

				request.append("private key : " + stripePrivateKey);
			    request.append(System.lineSeparator());
			    request.append("request : " + null);
				
				startDate = new Date();
				Charge responseCharge = charge.update(updateParams);
				
				ObjectMapper objectMapper = new ObjectMapper();
			    
			    String strChargeParameters = objectMapper.writeValueAsString(updateParams);
			    
				responseCode = "0";
				operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
				endDate = new Date();
				
				response.append(objectMapper.writeValueAsString(responseCharge));
			} else {
				operationResult.setResultCode(EnmResultCode.ERROR.getValue());
				operationResult.setResultDesc("Failed to retrieve charge information from stripe system. chargeId: " + chargeId);
				return operationResult;
			}
		} catch (Exception e) {
			operationResult.setResultCode(EnmResultCode.EXCEPTION.getValue());
			operationResult.setResultDesc(CommonUtil.getExceptionMessage(e));
			responseCode = "-1";
			response.append(CommonUtil.getExceptionMessage(e));
		}  finally {
			if (startDate != null && request != null) {
				if (endDate == null) {
					endDate = new Date();
				}
				IntegrationLog integrationLog = new IntegrationLog();
				integrationLog.setUserId(userId);
				integrationLog.setExtSystemId(EnmExternalSystem.STRIPE.getId());
				integrationLog.setOperationId(EnmOperation.STRIPE_CHARGE.getId());
				integrationLog.setOperationDate(startDate);
				integrationLog.setDuration(endDate.getTime() - startDate.getTime());
				integrationLog.setRequest(request.toString());
				integrationLog.setResponse(response.toString());
				integrationLog.setResponseCode(responseCode);
				integrationLog.setCreatedBy(operationBy);
				integrationLog.setCreatedDate(new Date());
				this.logService.insertIntegrationLog(integrationLog);
			}
		}
		
		return operationResult;
	}
	
	@Override
	public ValueOperationResult<Charge> getCharge(Long userId, String chargeId, String operationBy) {
		ValueOperationResult<Charge> operationResult = new ValueOperationResult<Charge>();
		
		StringBuilder request = new StringBuilder();
		StringBuilder response = new StringBuilder();
		String responseCode = null;
		Date startDate = null;
		Date endDate = null;
		
		try {
			String stripePrivateKey = this.parameterService.getParameterValue(EnmParameter.STRIPE_PRIVATE_KEY.getId());
			Stripe.apiKey = stripePrivateKey;
			
			request.append("private key : " + stripePrivateKey);
		    request.append(System.lineSeparator());
		    request.append("request : " + chargeId);
		    
		    startDate = new Date();
			Charge charge = Charge.retrieve(chargeId);

			ObjectMapper objectMapper = new ObjectMapper();
			
			responseCode = "0";
			operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
			operationResult.setResultValue(charge);
			endDate = new Date();
			
			response.append(objectMapper.writeValueAsString(charge)); 
			
		} catch (Exception e) {
			operationResult.setResultCode(EnmResultCode.EXCEPTION.getValue());
			operationResult.setResultDesc(CommonUtil.getExceptionMessage(e));
			responseCode = "-1";
			response.append(CommonUtil.getExceptionMessage(e));
		}  finally {
			if (startDate != null && request != null) {
				if (endDate == null) {
					endDate = new Date();
				}
				IntegrationLog integrationLog = new IntegrationLog();
				integrationLog.setUserId(userId);
				integrationLog.setExtSystemId(EnmExternalSystem.STRIPE.getId());
				integrationLog.setOperationId(EnmOperation.STRIPE_GET_CHARGE.getId());
				integrationLog.setOperationDate(startDate);
				integrationLog.setDuration(endDate.getTime() - startDate.getTime());
				integrationLog.setRequest(request.toString());
				integrationLog.setResponse(response.toString());
				integrationLog.setResponseCode(responseCode);
				integrationLog.setCreatedBy(operationBy);
				integrationLog.setCreatedDate(new Date());
				this.logService.insertIntegrationLog(integrationLog);
			}
		}
		
		return operationResult;
	}
	
	@Override
	public ValueOperationResult<String> capture(Long userId, String chargeId, String operationBy) {
		ValueOperationResult<String> operationResult = new ValueOperationResult<String>();
		
		StringBuilder request = new StringBuilder();
		StringBuilder response = new StringBuilder();
		String responseCode = null;
		Date startDate = null;
		Date endDate = null;
		
		try {
			String stripePrivateKey = this.parameterService.getParameterValue(EnmParameter.STRIPE_PRIVATE_KEY.getId());
			Stripe.apiKey = stripePrivateKey;
															
			ObjectMapper objectMapper = new ObjectMapper();
		    
		    		     
		    request.append("private key : " + stripePrivateKey);
		    request.append(System.lineSeparator());
		    request.append("request : " + chargeId);
		    
		    startDate = new Date();
		    
		    Charge charge = Charge.retrieve(chargeId);
		    charge = charge.capture();
		    
			responseCode = "0";
			endDate = new Date();
			response.append(objectMapper.writeValueAsString(charge)); 
			
			operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
			operationResult.setResultValue(charge.getTransfer());
			
		} catch (Exception e) {
			operationResult.setResultCode(EnmResultCode.EXCEPTION.getValue());
			operationResult.setResultDesc(CommonUtil.getExceptionMessage(e));
			responseCode = "-1";
			response.append(CommonUtil.getExceptionMessage(e));
		}  finally {
			if (startDate != null && request != null) {
				if (endDate == null) {
					endDate = new Date();
				}
				IntegrationLog integrationLog = new IntegrationLog();
				integrationLog.setUserId(userId);
				integrationLog.setExtSystemId(EnmExternalSystem.STRIPE.getId());
				integrationLog.setOperationId(EnmOperation.STRIPE_CAPTURE.getId());
				integrationLog.setOperationDate(startDate);
				integrationLog.setDuration(endDate.getTime() - startDate.getTime());
				integrationLog.setRequest(request.toString());
				integrationLog.setResponse(response.toString());
				integrationLog.setResponseCode(responseCode);
				integrationLog.setCreatedBy(operationBy);
				integrationLog.setCreatedDate(new Date());
				this.logService.insertIntegrationLog(integrationLog);
			}
		}
		
		return operationResult;
	}
	
	@Override
	public OperationResult refund(Long userId, String chargeId, BigDecimal refundAmount, BigDecimal vendorAmount, String operationBy) {
		OperationResult operationResult = new OperationResult();
		
		StringBuilder request = new StringBuilder();
		StringBuilder response = new StringBuilder();
		String responseCode = null;
		Date startDate = null;
		Date endDate = null;
		
		try {
			String stripePrivateKey = this.parameterService.getParameterValue(EnmParameter.STRIPE_PRIVATE_KEY.getId());
			Stripe.apiKey = stripePrivateKey;
															
			ObjectMapper objectMapper = new ObjectMapper();

			Map<String, Object> refundParams = new HashMap<String, Object>();
			refundParams.put("charge", chargeId);
			refundParams.put("reverse_transfer", true);
			
			if (refundAmount != null) {
				refundParams.put("amount", refundAmount);
			}
		    		     
		    request.append("private key : " + stripePrivateKey);
		    request.append(System.lineSeparator());
		    request.append("request : " + chargeId);
		    
		    startDate = new Date();
		    
		    Refund newRefund = Refund.create(refundParams);
		    
			responseCode = "0";
			operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
			endDate = new Date();
			
			response.append(objectMapper.writeValueAsString(newRefund)); 
			
		} catch (Exception e) {
			operationResult.setResultCode(EnmResultCode.EXCEPTION.getValue());
			operationResult.setResultDesc(CommonUtil.getExceptionMessage(e));
			responseCode = "-1";
			response.append(CommonUtil.getExceptionMessage(e));
		}  finally {
			if (startDate != null && request != null) {
				if (endDate == null) {
					endDate = new Date();
				}
				IntegrationLog integrationLog = new IntegrationLog();
				integrationLog.setUserId(userId);
				integrationLog.setExtSystemId(EnmExternalSystem.STRIPE.getId());
				integrationLog.setOperationId(EnmOperation.STRIPE_REFUND.getId());
				integrationLog.setOperationDate(startDate);
				integrationLog.setDuration(endDate.getTime() - startDate.getTime());
				integrationLog.setRequest(request.toString());
				integrationLog.setResponse(response.toString());
				integrationLog.setResponseCode(responseCode);
				integrationLog.setCreatedBy(operationBy);
				integrationLog.setCreatedDate(new Date());
				this.logService.insertIntegrationLog(integrationLog);
			}
		}
		
		return operationResult;
	}

}
