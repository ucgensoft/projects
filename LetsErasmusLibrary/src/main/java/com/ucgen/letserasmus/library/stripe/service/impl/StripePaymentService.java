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
import com.stripe.model.Payout;
import com.stripe.model.Refund;
import com.stripe.model.Reversal;
import com.stripe.model.Transfer;
import com.ucgen.common.operationresult.EnmResultCode;
import com.ucgen.common.operationresult.OperationResult;
import com.ucgen.common.operationresult.ValueOperationResult;
import com.ucgen.common.util.CommonUtil;
import com.ucgen.common.util.DateUtil;
import com.ucgen.letserasmus.library.log.enumeration.EnmDirection;
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

	private static final BigDecimal CENT_MULTIPLIER = new BigDecimal(100);
	
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
	public ValueOperationResult<String> createManagedAccount(PayoutMethod payoutMethod, String tosAcceptIp, Date tosAcceptDate) {
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
			
			Map<String, Object> tosParams = new HashMap<String, Object>();
			tosParams.put("ip", tosAcceptIp);
			tosParams.put("date", Math.abs(tosAcceptDate.getTime() / 1000));
			
			Map<String, Object> accountParams = new HashMap<String, Object>();
			accountParams.put("country", payoutMethod.getVendorCountry());
			accountParams.put("managed", true);
			accountParams.put("legal_entity", legalEntityParams);
			accountParams.put("payout_schedule", payoutScheduleParams);
			accountParams.put("tos_acceptance", tosParams);

			ObjectMapper objectMapper = new ObjectMapper();
		    
		    String strAccountParameters = objectMapper.writeValueAsString(accountParams);
		    		     
		    //request.append("private key : " + stripePrivateKey);
		    //request.append(System.lineSeparator());
		    request.append(strAccountParameters);
		    
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
				integrationLog.setDirection(EnmDirection.OUTGOING.getId());
				this.logService.insertIntegrationLog(integrationLog);
			}
		}
		
		return operationResult;
	}
	
	@Override
	public ValueOperationResult<Charge> createCharge(PayoutMethod payoutMethod, PaymentMethod paymentMethod, String operationBy) {
		ValueOperationResult<Charge> operationResult = new ValueOperationResult<Charge>();
		
		StringBuilder request = new StringBuilder();
		StringBuilder response = new StringBuilder();
		String responseCode = null;
		Date startDate = null;
		Date endDate = null;
		
		try {
			String stripePrivateKey = this.parameterService.getParameterValue(EnmParameter.STRIPE_PRIVATE_KEY.getId());
			Stripe.apiKey = stripePrivateKey;
			
			Long chargeAmount = this.getAbsAmount(paymentMethod.getPayment().getEntityPrice().add(paymentMethod.getPayment().getServiceFee()));
			Long destinationAmount = this.getAbsAmount(paymentMethod.getPayment().getEntityPrice().subtract(paymentMethod.getPayment().getCommissionFee()));
			
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
		    		     
		    //request.append("private key : " + stripePrivateKey);
		    //request.append(System.lineSeparator());
		    request.append("request : " + strChargeParameters);
		    
		    startDate = new Date();
			Charge newCharge = Charge.create(chargeParams);
			responseCode = "0";
			
			operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
			operationResult.setResultValue(newCharge);
			
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
				integrationLog.setOperationId(EnmOperation.STRIPE_CREATE_CHARGE.getId());
				integrationLog.setOperationDate(startDate);
				integrationLog.setDuration(endDate.getTime() - startDate.getTime());
				integrationLog.setRequest(request.toString());
				integrationLog.setResponse(response.toString());
				integrationLog.setResponseCode(responseCode);
				integrationLog.setCreatedBy(operationBy);
				integrationLog.setCreatedDate(new Date());
				integrationLog.setDirection(EnmDirection.OUTGOING.getId());
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
			
			ValueOperationResult<Charge> getChargeResult = this.getCharge(userId, chargeId, operationBy);
			
			if (OperationResult.isResultSucces(getChargeResult)) {
				Charge charge = getChargeResult.getResultValue();
				Map<String, Object> updateParams = new HashMap<String, Object>();
				updateParams.put("description", "Charge for andrew.miller@example.com");

				//request.append("private key : " + stripePrivateKey);
			    //request.append(System.lineSeparator());
			    request.append("request : " + null);
				
				startDate = new Date();
				Charge responseCharge = charge.update(updateParams);
				
				ObjectMapper objectMapper = new ObjectMapper();
			    
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
				integrationLog.setOperationId(EnmOperation.STRIPE_UPDATE_CHARGE.getId());
				integrationLog.setOperationDate(startDate);
				integrationLog.setDuration(endDate.getTime() - startDate.getTime());
				integrationLog.setRequest(request.toString());
				integrationLog.setResponse(response.toString());
				integrationLog.setResponseCode(responseCode);
				integrationLog.setCreatedBy(operationBy);
				integrationLog.setCreatedDate(new Date());
				integrationLog.setDirection(EnmDirection.OUTGOING.getId());
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
			
			//request.append("private key : " + stripePrivateKey);
		    //request.append(System.lineSeparator());
		    request.append(chargeId);
		    
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
				integrationLog.setDirection(EnmDirection.OUTGOING.getId());
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
		    
		    		     
		    //request.append("private key : " + stripePrivateKey);
		    //request.append(System.lineSeparator());
		    request.append(chargeId);
		    
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
				integrationLog.setDirection(EnmDirection.OUTGOING.getId());
				this.logService.insertIntegrationLog(integrationLog);
			}
		}
		
		return operationResult;
	}
	
	@Override
	public OperationResult refund(Long userId, String chargeId, String transferId, BigDecimal clientRefundAmount, BigDecimal vendorRefundAmount, String operationBy) {
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
			
			if (clientRefundAmount != null) {
				refundParams.put("amount", this.getAbsAmount(clientRefundAmount));
			}
			
			Charge stripeCharge = Charge.retrieve(chargeId);
			if (stripeCharge.getCaptured()) {
				ValueOperationResult<Reversal> reversalResult = this.createReverseTransfer(userId, transferId, vendorRefundAmount, operationBy);
				if (!OperationResult.isResultSucces(reversalResult)) {
					operationResult.setResultCode(EnmResultCode.ERROR.getValue());
					operationResult.setResultDesc("Reverse transfer operation failed. Error: " + reversalResult.getResultDesc());
					return operationResult;
				}
				refundParams.put("reverse_transfer", false);
			} else {
				refundParams.put("reverse_transfer", true);
			}
			
		    //request.append("private key : " + stripePrivateKey);
		    //request.append(System.lineSeparator());
		    request.append(objectMapper.writeValueAsString(refundParams));
		    
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
				integrationLog.setDirection(EnmDirection.OUTGOING.getId());
				this.logService.insertIntegrationLog(integrationLog);
			}
		}
		
		return operationResult;
	}
	
	@Override
	public OperationResult createPayout(Long userId, BigDecimal payoutAmount, String currencyCode, String operationBy) {
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

			Map<String, Object> payoutParams = new HashMap<String, Object>();
			payoutParams.put("amount", this.getAbsAmount(payoutAmount));
			payoutParams.put("currency", currencyCode);

			//request.append("private key : " + stripePrivateKey);
		    //request.append(System.lineSeparator());
		    request.append("request : " + objectMapper.writeValueAsString(payoutParams));
			
			startDate = new Date();
			Payout newPayout = Payout.create(payoutParams);
		    
			responseCode = "0";
			operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
			endDate = new Date();
			
			response.append(objectMapper.writeValueAsString(newPayout)); 
			
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
				integrationLog.setOperationId(EnmOperation.STRIPE_CREATE_PAYOUT.getId());
				integrationLog.setOperationDate(startDate);
				integrationLog.setDuration(endDate.getTime() - startDate.getTime());
				integrationLog.setRequest(request.toString());
				integrationLog.setResponse(response.toString());
				integrationLog.setResponseCode(responseCode);
				integrationLog.setCreatedBy(operationBy);
				integrationLog.setCreatedDate(new Date());
				integrationLog.setDirection(EnmDirection.OUTGOING.getId());
				this.logService.insertIntegrationLog(integrationLog);
			}
		}
		
		return operationResult;
	}

	
	@Override
	public ValueOperationResult<Transfer> getTransfer(Long userId, String transferId, String operationBy) {
		ValueOperationResult<Transfer> operationResult = new ValueOperationResult<Transfer>();
		
		StringBuilder request = new StringBuilder();
		StringBuilder response = new StringBuilder();
		String responseCode = null;
		Date startDate = null;
		Date endDate = null;
		
		try {
			String stripePrivateKey = this.parameterService.getParameterValue(EnmParameter.STRIPE_PRIVATE_KEY.getId());
			Stripe.apiKey = stripePrivateKey;
			
			//request.append("private key : " + stripePrivateKey);
		    //request.append(System.lineSeparator());
		    request.append(transferId);
		    
		    startDate = new Date();
			Transfer transfer = Transfer.retrieve(transferId);

			ObjectMapper objectMapper = new ObjectMapper();
			
			responseCode = "0";
			operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
			operationResult.setResultValue(transfer);
			endDate = new Date();
			
			response.append(objectMapper.writeValueAsString(transfer)); 
			
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
				integrationLog.setDirection(EnmDirection.OUTGOING.getId());
				this.logService.insertIntegrationLog(integrationLog);
			}
		}
		
		return operationResult;
	}

	
	@Override
	public ValueOperationResult<Reversal> createReverseTransfer(Long userId, String transferId, BigDecimal amount,
			String operationBy) {
		ValueOperationResult<Reversal> operationResult = new ValueOperationResult<Reversal>();
		
		StringBuilder request = new StringBuilder();
		StringBuilder response = new StringBuilder();
		String responseCode = null;
		Date startDate = null;
		Date endDate = null;
		
		try {
			String stripePrivateKey = this.parameterService.getParameterValue(EnmParameter.STRIPE_PRIVATE_KEY.getId());
			Stripe.apiKey = stripePrivateKey;
			
			//request.append("private key : " + stripePrivateKey);
		    //request.append(System.lineSeparator());
		    request.append(transferId);
		    
		    startDate = new Date();
		    
		    Map<String, Object> reverseParams = new HashMap<String, Object>();
		    reverseParams.put("amount", this.getAbsAmount(amount));
			Transfer transfer = this.getTransfer(userId, transferId, operationBy).getResultValue();
			Reversal reversal = transfer.getReversals().create(reverseParams);

			ObjectMapper objectMapper = new ObjectMapper();
			
			responseCode = "0";
			operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
			operationResult.setResultValue(reversal);
			endDate = new Date();
			
			response.append(objectMapper.writeValueAsString(reversal)); 
			
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
				integrationLog.setOperationId(EnmOperation.STRIPE_CREATE_REVERSE_TRANSFER.getId());
				integrationLog.setOperationDate(startDate);
				integrationLog.setDuration(endDate.getTime() - startDate.getTime());
				integrationLog.setRequest(request.toString());
				integrationLog.setResponse(response.toString());
				integrationLog.setResponseCode(responseCode);
				integrationLog.setCreatedBy(operationBy);
				integrationLog.setCreatedDate(new Date());
				integrationLog.setDirection(EnmDirection.OUTGOING.getId());
				this.logService.insertIntegrationLog(integrationLog);
			}
		}
		
		return operationResult;
	}
	
	private Long getAbsAmount(BigDecimal amount) {
		return amount.multiply(CENT_MULTIPLIER).abs().longValue();
	}

}
