package com.ucgen.letserasmus.web.api.payment;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ucgen.common.operationresult.EnmResultCode;
import com.ucgen.common.operationresult.ListOperationResult;
import com.ucgen.common.operationresult.OperationResult;
import com.ucgen.common.operationresult.ValueOperationResult;
import com.ucgen.common.util.CommonUtil;
import com.ucgen.common.util.FileLogger;
import com.ucgen.letserasmus.library.bluesnap.service.IExtPaymentService;
import com.ucgen.letserasmus.library.common.enumeration.EnmErrorCode;
import com.ucgen.letserasmus.library.log.enumeration.EnmDirection;
import com.ucgen.letserasmus.library.log.enumeration.EnmExternalSystem;
import com.ucgen.letserasmus.library.log.enumeration.EnmOperation;
import com.ucgen.letserasmus.library.log.model.IntegrationLog;
import com.ucgen.letserasmus.library.log.service.ILogService;
import com.ucgen.letserasmus.library.payment.enumeration.EnmVendorEntityType;
import com.ucgen.letserasmus.library.payment.model.PaymentMethod;
import com.ucgen.letserasmus.library.payment.model.PayoutMethod;
import com.ucgen.letserasmus.library.payment.service.IPaymentService;
import com.ucgen.letserasmus.library.stripe.service.IStripePaymentService;
import com.ucgen.letserasmus.library.user.model.User;
import com.ucgen.letserasmus.web.api.BaseApiController;
import com.ucgen.letserasmus.web.view.application.AppConstants;

@RestController
public class ApiPaymentController extends BaseApiController {

	private IPaymentService paymentService;
	private IExtPaymentService extPaymentService;
	private IStripePaymentService stripePaymentService;
	private ILogService logService;
	
	@Autowired
	public void setLogService(ILogService logService) {
		this.logService = logService;
	}

	@Autowired
	public void setPaymentService(IPaymentService paymentService) {
		this.paymentService = paymentService;
	}

	@Autowired
	public void setExtPaymentService(IExtPaymentService extPaymentService) {
		this.extPaymentService = extPaymentService;
	}

	@Autowired
	public void setStripePaymentService(IStripePaymentService stripePaymentService) {
		this.stripePaymentService = stripePaymentService;
	}

	@RequestMapping(value = "/api/payment/gettoken", method = RequestMethod.GET)
    public ResponseEntity<ValueOperationResult<String>> getPaymentToken(@RequestParam("operationToken") String operationToken,
    		@RequestParam("operationId") Integer operationId, HttpSession session) {
		ValueOperationResult<String> operationResult = new ValueOperationResult<String>();
		
		try {
			User user = super.getSessionUser(session);
			if (user != null) {
				if (operationToken != null && !operationToken.trim().isEmpty()
						&& operationId != null) {
					Object tokenObject = this.getObjectForToken(operationToken, operationId);
					if (tokenObject != null) {
						ValueOperationResult<String> getTokenResult = this.extPaymentService.getPaymentFieldToken(user.getId(), "ApiPaymentController");
						if (OperationResult.isResultSucces(getTokenResult)) {
							super.savePaymentToken(operationToken, getTokenResult.getResultValue());
							operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
							operationResult.setResultValue(getTokenResult.getResultValue());
						} else {
							operationResult.setResultCode(EnmResultCode.ERROR.getValue());
							operationResult.setResultDesc(AppConstants.OPERATION_FAIL);
						}
					} else {
						operationResult.setResultCode(EnmResultCode.ERROR.getValue());
						operationResult.setResultDesc(AppConstants.INVALID_OPERATION_TOKEN);
					}
				} else {
					operationResult.setResultCode(EnmResultCode.ERROR.getValue());
					operationResult.setResultDesc(AppConstants.MISSING_MANDATORY_PARAM);
				}				
			} else {
				operationResult.setErrorCode(EnmErrorCode.USER_NOT_LOGGED_IN.getId());
				operationResult.setResultCode(EnmResultCode.ERROR.getValue());
				operationResult.setResultDesc(AppConstants.USER_NOT_LOGGED_IN);
			}
		} catch (Exception e) {
			operationResult.setResultCode(EnmResultCode.EXCEPTION.getValue());
			operationResult.setResultDesc(AppConstants.OPERATION_FAIL);
			FileLogger.log(Level.ERROR, "ApiPaymentController-getPaymentToken()-Error: " + CommonUtil.getExceptionMessage(e));
		}
		return new ResponseEntity<ValueOperationResult<String>>(operationResult, HttpStatus.OK);
    }
	
	@RequestMapping(value = "/api/paymentmethod/create", method = RequestMethod.POST)
    public ResponseEntity<ValueOperationResult<PaymentMethod>> createPaymentMethod(@RequestBody PaymentMethod paymentMethod, HttpSession session) {
		ValueOperationResult<PaymentMethod> operationResult = new ValueOperationResult<PaymentMethod>();
		
		try {
			User user = super.getSessionUser(session);
			if (user != null) {
				if (paymentMethod.getBlueSnapId() != null 
						&& paymentMethod.getCardNumber() != null && paymentMethod.getCardNumber().trim().length() == 4
						&& paymentMethod.getCardType() != null 
						&& paymentMethod.getExpDate() != null &&  paymentMethod.getExpDate().trim().length() == 7) {
					
					PaymentMethod newPaymentMethod  = new PaymentMethod();
					newPaymentMethod.setUserId(user.getId());
					newPaymentMethod.setCardNumber(paymentMethod.getCardNumber());
					newPaymentMethod.setCardType(paymentMethod.getCardType());
					newPaymentMethod.setExpDate(paymentMethod.getExpDate());
					newPaymentMethod.setBlueSnapId(paymentMethod.getBlueSnapId());
					newPaymentMethod.setCreatedBy(user.getFullName());
					newPaymentMethod.setCreatedDate(new Date());
					
					PaymentMethod dbPaymentMethod = this.paymentService.getPaymentMethod(newPaymentMethod);
					if (dbPaymentMethod == null) {
						OperationResult createPaymentMethodResult = this.paymentService.insertPaymentMethod(newPaymentMethod);
						if (OperationResult.isResultSucces(createPaymentMethodResult)) {
							operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
							operationResult.setResultValue(newPaymentMethod);
						} else {
							operationResult.setResultCode(EnmResultCode.ERROR.getValue());
							operationResult.setResultDesc(AppConstants.CREATE_OPERATION_FAIL);
						}
					} else {
						operationResult.setResultCode(EnmResultCode.ERROR.getValue());
						operationResult.setResultDesc(AppConstants.PAYMENT_METHOD_DOUBLE);
					}
				} else {
					operationResult.setResultCode(EnmResultCode.ERROR.getValue());
					operationResult.setResultDesc(AppConstants.MISSING_MANDATORY_PARAM);
				}
			} else {
				operationResult.setErrorCode(EnmErrorCode.USER_NOT_LOGGED_IN.getId());
				operationResult.setResultCode(EnmResultCode.ERROR.getValue());
				operationResult.setResultDesc(AppConstants.USER_NOT_LOGGED_IN);
			}
		} catch (Exception e) {
			operationResult.setResultCode(EnmResultCode.EXCEPTION.getValue());
			operationResult.setResultDesc(AppConstants.CREATE_OPERATION_FAIL);
			FileLogger.log(Level.ERROR, "ApiPaymentController-createPaymentMethod()-Error: " + CommonUtil.getExceptionMessage(e));
		}
		return new ResponseEntity<ValueOperationResult<PaymentMethod>>(operationResult, HttpStatus.OK);
    }
	
	@RequestMapping(value = "/api/paymentmethod/list", method = RequestMethod.GET)
    public ResponseEntity<ListOperationResult<PaymentMethod>> listPaymentMethod(HttpSession session) {
		ListOperationResult<PaymentMethod> operationResult = new ListOperationResult<PaymentMethod>();
		
		try {
			User user = super.getSessionUser(session);
			if (user != null) {
				PaymentMethod newPaymentMethod  = new PaymentMethod();
				newPaymentMethod.setUserId(user.getId());
				
				List<PaymentMethod> paymentMethodList = this.paymentService.listPaymentMethod(newPaymentMethod);
				operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
				operationResult.setObjectList(paymentMethodList);
			} else {
				operationResult.setErrorCode(EnmErrorCode.USER_NOT_LOGGED_IN.getId());
				operationResult.setResultCode(EnmResultCode.ERROR.getValue());
				operationResult.setResultDesc(AppConstants.USER_NOT_LOGGED_IN);
			}
		} catch (Exception e) {
			operationResult.setResultCode(EnmResultCode.EXCEPTION.getValue());
			operationResult.setResultDesc(AppConstants.CREATE_OPERATION_FAIL);
			FileLogger.log(Level.ERROR, "ApiPaymentController-listPaymentMethod()-Error: " + CommonUtil.getExceptionMessage(e));
		}
		return new ResponseEntity<ListOperationResult<PaymentMethod>>(operationResult, HttpStatus.OK);
    }
	
	@RequestMapping(value = "/api/payout/haspayout", method = RequestMethod.GET)
    public ResponseEntity<ValueOperationResult<Boolean>> hasPayoutMethod(HttpSession session) {
		ValueOperationResult<Boolean> operationResult = new ValueOperationResult<Boolean>();
		
		try {
			User user = super.getSessionUser(session);
			if (user != null) {
				PayoutMethod payoutMethod = this.paymentService.getPayoutMethod(new PayoutMethod(user.getId()));
				operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
				if (payoutMethod != null) {
					operationResult.setResultValue(true);
				} else {
					operationResult.setResultValue(false);
				}				
			} else {
				operationResult.setErrorCode(EnmErrorCode.USER_NOT_LOGGED_IN.getId());
				operationResult.setResultCode(EnmResultCode.ERROR.getValue());
				operationResult.setResultDesc(AppConstants.USER_NOT_LOGGED_IN);
			}
		} catch (Exception e) {
			operationResult.setResultCode(EnmResultCode.EXCEPTION.getValue());
			operationResult.setResultDesc(AppConstants.OPERATION_FAIL);
			FileLogger.log(Level.ERROR, "ApiPaymentController-hasPayoutMethod()-Error: " + CommonUtil.getExceptionMessage(e));
		}
		return new ResponseEntity<ValueOperationResult<Boolean>>(operationResult, HttpStatus.OK);
    }
	
	@RequestMapping(value = "/api/payout/createdraft", method = RequestMethod.POST)
    public ResponseEntity<OperationResult> createDraftPayoutMethod(@RequestBody PayoutMethod uiPayoutMethod, HttpSession session) {
		OperationResult operationResult = new OperationResult();
		
		try {
			User user = super.getSessionUser(session);
			if (user != null) {
				if (uiPayoutMethod != null 
						&& uiPayoutMethod.getVendorEntityType() != null && !uiPayoutMethod.getVendorEntityType().trim().isEmpty()
						&& EnmVendorEntityType.getEntityType(uiPayoutMethod.getVendorEntityType()) != null
						&& uiPayoutMethod.getVendorLastName() != null && !uiPayoutMethod.getVendorLastName().trim().isEmpty()
						&& uiPayoutMethod.getVendorFirstName() != null && !uiPayoutMethod.getVendorFirstName().trim().isEmpty()
						&& uiPayoutMethod.getVendorBirthDate() != null
						&& uiPayoutMethod.getVendorCountry() != null && !uiPayoutMethod.getVendorCountry().trim().isEmpty()
						&& uiPayoutMethod.getVendorCity() != null && !uiPayoutMethod.getVendorCity().trim().isEmpty()
						&& uiPayoutMethod.getVendorZip() != null && !uiPayoutMethod.getVendorZip().trim().isEmpty()
						&& uiPayoutMethod.getVendorAddress() != null && !uiPayoutMethod.getVendorAddress().trim().isEmpty()) {
					PayoutMethod payoutMethod = this.paymentService.getPayoutMethod(new PayoutMethod(user.getId()));
					if (payoutMethod == null) {
						payoutMethod = new PayoutMethod();
						payoutMethod.setVendorEntityType(uiPayoutMethod.getVendorEntityType().toUpperCase());
						payoutMethod.setUserId(user.getId());
						payoutMethod.setVendorEmail(user.getEmail());
						payoutMethod.setVendorFirstName(uiPayoutMethod.getVendorFirstName());
						payoutMethod.setVendorLastName(uiPayoutMethod.getVendorLastName());
						payoutMethod.setVendorBirthDate(uiPayoutMethod.getVendorBirthDate());
						payoutMethod.setVendorCountry(uiPayoutMethod.getVendorCountry());
						payoutMethod.setVendorCity(uiPayoutMethod.getVendorCity());
						payoutMethod.setVendorZip(uiPayoutMethod.getVendorZip());
						payoutMethod.setVendorAddress(uiPayoutMethod.getVendorAddress());
						if (uiPayoutMethod.getVendorAddress2() != null && !uiPayoutMethod.getVendorAddress2().trim().isEmpty()) {
							payoutMethod.setVendorAddress2(uiPayoutMethod.getVendorAddress2());
						}
						
						payoutMethod.setCreatedBy(user.getFullName());
						payoutMethod.setCreatedDate(new Date());
						OperationResult createPayoutResult = this.paymentService.createPayoutMethodDraft(payoutMethod);
						if (OperationResult.isResultSucces(createPayoutResult)) {
							user.setPayoutMethod(payoutMethod);
							operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
						} else {
							operationResult.setResultCode(EnmResultCode.ERROR.getValue());
							operationResult.setResultDesc(AppConstants.CREATE_PAYMENT_ACCOUNT_FAILES);
						}
					} else {
						operationResult.setResultCode(EnmResultCode.ERROR.getValue());
						operationResult.setResultDesc(AppConstants.USER_ALREADY_HAVE_PAYOUT_METHOD);
					}		
				} else {
					operationResult.setResultCode(EnmResultCode.ERROR.getValue());
					operationResult.setResultDesc(AppConstants.MISSING_MANDATORY_PARAM);
				}				
			} else {
				operationResult.setErrorCode(EnmErrorCode.USER_NOT_LOGGED_IN.getId());
				operationResult.setResultCode(EnmResultCode.ERROR.getValue());
				operationResult.setResultDesc(AppConstants.USER_NOT_LOGGED_IN);
			}
		} catch (Exception e) {
			operationResult.setResultCode(EnmResultCode.EXCEPTION.getValue());
			operationResult.setResultDesc(AppConstants.OPERATION_FAIL);
			FileLogger.log(Level.ERROR, "ApiPaymentController-createDraftPayoutMethod()-Error: " + CommonUtil.getExceptionMessage(e));
		}
		return new ResponseEntity<OperationResult>(operationResult, HttpStatus.OK);
    }
	
	@RequestMapping(value = "/api/payout/update", method = RequestMethod.POST)
    public ResponseEntity<OperationResult> updatePayoutMethod(@RequestBody PayoutMethod uiPayoutMethod, HttpSession session) {
		OperationResult operationResult = new OperationResult();
		
		try {
			User user = super.getSessionUser(session);
			if (user != null) {
				if (uiPayoutMethod != null) {
					
					PayoutMethod dbPayoutMethod = this.paymentService.getPayoutMethod(new PayoutMethod(user.getId()));
					if (dbPayoutMethod != null) {		
						
						dbPayoutMethod.setVendorNationalId(uiPayoutMethod.getVendorNationalId());
						dbPayoutMethod.setVendorTaxId(uiPayoutMethod.getVendorTaxId());
						dbPayoutMethod.setVendorFirstName(uiPayoutMethod.getVendorFirstName());
						dbPayoutMethod.setVendorLastName(uiPayoutMethod.getVendorLastName());
						dbPayoutMethod.setVendorBirthDate(uiPayoutMethod.getVendorBirthDate());
						dbPayoutMethod.setVendorCountry(uiPayoutMethod.getVendorCountry());
						dbPayoutMethod.setVendorCity(uiPayoutMethod.getVendorCity());
						dbPayoutMethod.setVendorZip(uiPayoutMethod.getVendorZip());
						dbPayoutMethod.setVendorAddress(uiPayoutMethod.getVendorAddress());
						dbPayoutMethod.setVendorAddress2(uiPayoutMethod.getVendorAddress2());
						
						dbPayoutMethod.setBankAccountClass(uiPayoutMethod.getBankAccountClass());
						dbPayoutMethod.setBankAccountIban(uiPayoutMethod.getBankAccountIban());
						dbPayoutMethod.setBankAccountHolderName(uiPayoutMethod.getBankAccountHolderName());
						dbPayoutMethod.setBankCountry(uiPayoutMethod.getBankCountry());
						dbPayoutMethod.setBankSwiftBic(uiPayoutMethod.getBankSwiftBic());
						
						dbPayoutMethod.setModifiedBy(user.getFullName());
						dbPayoutMethod.setCreatedDate(new Date());
						OperationResult updatePayoutResult = this.paymentService.updatePayoutMethod(dbPayoutMethod);
						if (OperationResult.isResultSucces(updatePayoutResult)) {
							user.setPayoutMethod(dbPayoutMethod);
							operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
						} else {
							operationResult.setResultCode(EnmResultCode.ERROR.getValue());
							operationResult.setResultDesc(AppConstants.OPERATION_FAIL);
						}
					} else {
						operationResult.setResultCode(EnmResultCode.ERROR.getValue());
						operationResult.setResultDesc(AppConstants.USER_DO_NOT_HAVE_PAYOUT_METHOD);
					}	
					
				} else {
					operationResult.setResultCode(EnmResultCode.ERROR.getValue());
					operationResult.setResultDesc(AppConstants.MISSING_MANDATORY_PARAM);
				}				
			} else {
				operationResult.setErrorCode(EnmErrorCode.USER_NOT_LOGGED_IN.getId());
				operationResult.setResultCode(EnmResultCode.ERROR.getValue());
				operationResult.setResultDesc(AppConstants.USER_NOT_LOGGED_IN);
			}
		} catch (Exception e) {
			operationResult.setResultCode(EnmResultCode.EXCEPTION.getValue());
			operationResult.setResultDesc(AppConstants.OPERATION_FAIL);
			FileLogger.log(Level.ERROR, "ApiPaymentController-createDraftPayoutMethod()-Error: " + CommonUtil.getExceptionMessage(e));
		}
		return new ResponseEntity<OperationResult>(operationResult, HttpStatus.OK);
    }
	
	@RequestMapping(value = "/api/payout/get", method = RequestMethod.GET)
    public ResponseEntity<ValueOperationResult<PayoutMethod>> getPayoutMethod(HttpSession session) {
		ValueOperationResult<PayoutMethod> operationResult = new ValueOperationResult<PayoutMethod>();
		
		try {
			User user = super.getSessionUser(session);
			if (user != null) {
				PayoutMethod payoutMethod = this.paymentService.getPayoutMethod(new PayoutMethod(user.getId()));
				if (payoutMethod != null) {
					//payoutMethod = new PayoutMethod();
					if (payoutMethod.getVendorFirstName() == null) {
						payoutMethod.setVendorFirstName(user.getFirstName());
					}
					if (payoutMethod.getVendorLastName() == null) {
						payoutMethod.setVendorLastName(user.getLastName());
					}
					if (payoutMethod.getVendorBirthDate() == null) {
						payoutMethod.setVendorBirthDate(user.getBirthDate());
					}
					if (payoutMethod.getVendorCountry() == null) {
						payoutMethod.setVendorCountry(payoutMethod.getBankCountry());
					}
				}
				
				operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
				operationResult.setResultValue(payoutMethod);			
			} else {
				operationResult.setErrorCode(EnmErrorCode.USER_NOT_LOGGED_IN.getId());
				operationResult.setResultCode(EnmResultCode.ERROR.getValue());
				operationResult.setResultDesc(AppConstants.USER_NOT_LOGGED_IN);
			}
		} catch (Exception e) {
			operationResult.setResultCode(EnmResultCode.EXCEPTION.getValue());
			operationResult.setResultDesc(AppConstants.OPERATION_FAIL);
			FileLogger.log(Level.ERROR, "ApiPaymentController-getPayoutMethod()-Error: " + CommonUtil.getExceptionMessage(e));
		}
		return new ResponseEntity<ValueOperationResult<PayoutMethod>>(operationResult, HttpStatus.OK);
    }
	
	@RequestMapping(value = "/api/payment/bluesnap/ipn", method = RequestMethod.POST)
    public ResponseEntity<OperationResult> insertBlueSnapIpn(@RequestBody String requestBody, HttpSession session) {
		OperationResult operationResult = new OperationResult();
		
		try {
			Date operationDate = new Date();
			IntegrationLog integrationLog = new IntegrationLog();
			integrationLog.setUserId(0l);
			integrationLog.setExtSystemId(EnmExternalSystem.BLUESNAP.getId());
			integrationLog.setOperationId(EnmOperation.BLUESNAP_WEBHOOK.getId());
			integrationLog.setOperationDate(operationDate);
			integrationLog.setDuration(0l);
			integrationLog.setRequest(requestBody);
			integrationLog.setResponse("Success");
			integrationLog.setResponseCode("0");
			integrationLog.setCreatedBy("BlueSnap");
			integrationLog.setCreatedDate(operationDate);
			integrationLog.setDirection(EnmDirection.INCOMING.getId());
			this.logService.insertIntegrationLog(integrationLog);
			
			operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
		} catch (Exception e) {
			operationResult.setResultCode(EnmResultCode.EXCEPTION.getValue());
			operationResult.setResultDesc(AppConstants.OPERATION_FAIL);
			FileLogger.log(Level.ERROR, "ApiPaymentController-createDraftPayoutMethod()-Error: " + CommonUtil.getExceptionMessage(e));
		}
		return new ResponseEntity<OperationResult>(operationResult, HttpStatus.OK);
    }
	
}
