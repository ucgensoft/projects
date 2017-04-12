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
import com.ucgen.letserasmus.library.payment.model.PaymentMethod;
import com.ucgen.letserasmus.library.payment.model.PayoutMethod;
import com.ucgen.letserasmus.library.payment.service.IPaymentService;
import com.ucgen.letserasmus.library.user.model.User;
import com.ucgen.letserasmus.web.api.BaseApiController;
import com.ucgen.letserasmus.web.view.application.AppConstants;

@RestController
public class ApiPaymentController extends BaseApiController {

	private IPaymentService paymentService;
	private IExtPaymentService extPaymentService;
	
	@Autowired
	public void setPaymentService(IPaymentService paymentService) {
		this.paymentService = paymentService;
	}

	@Autowired
	public void setExtPaymentService(IExtPaymentService extPaymentService) {
		this.extPaymentService = extPaymentService;
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
	
	@RequestMapping(value = "/api/payout/createdraft", method = RequestMethod.GET)
    public ResponseEntity<OperationResult> createDraftPayoutMethod(@RequestParam("countryCode") String countryCode, HttpSession session) {
		OperationResult operationResult = new OperationResult();
		
		try {
			User user = super.getSessionUser(session);
			if (user != null) {
				if (countryCode != null && !countryCode.trim().isEmpty()) {
					PayoutMethod payoutMethod = this.paymentService.getPayoutMethod(new PayoutMethod(user.getId()));
					if (payoutMethod == null) {
						payoutMethod = new PayoutMethod();
						payoutMethod.setUserId(user.getId());
						payoutMethod.setEmail(user.getEmail());
						payoutMethod.setBlueSnapCountryCode(countryCode);
						payoutMethod.setCreatedBy(user.getFullName());
						payoutMethod.setCreatedDate(new Date());
						OperationResult createPayoutResult = this.paymentService.createPayoutMethodDraft(payoutMethod);
						if (OperationResult.isResultSucces(createPayoutResult)) {
							operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
						} else {
							operationResult.setResultCode(EnmResultCode.ERROR.getValue());
							operationResult.setResultDesc(AppConstants.BLUESNAP_CREATE_VENDOR_FAILES);
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
	
}
