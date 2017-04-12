package com.ucgen.letserasmus.library.bluesnap.service.impl;

import java.util.Arrays;
import java.util.Date;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ucgen.common.operationresult.EnmResultCode;
import com.ucgen.common.operationresult.ValueOperationResult;
import com.ucgen.common.util.CommonUtil;
import com.ucgen.letserasmus.library.bluesnap.model.ExtVendor;
import com.ucgen.letserasmus.library.bluesnap.service.IExtPaymentService;
import com.ucgen.letserasmus.library.log.enumeration.EnmExternalSystem;
import com.ucgen.letserasmus.library.log.enumeration.EnmOperation;
import com.ucgen.letserasmus.library.log.model.IntegrationLog;
import com.ucgen.letserasmus.library.log.service.ILogService;
import com.ucgen.letserasmus.library.parameter.enumeration.EnmParameter;
import com.ucgen.letserasmus.library.parameter.service.IParameterService;

@Service
public class ExtPaymentService implements IExtPaymentService {

	private IParameterService parameterService;
	private ILogService logService;
	
	private RestTemplate restTemplate;
	
	@Autowired
	public void setParameterService(IParameterService parameterService) {
		this.parameterService = parameterService;
	}
	
	@Autowired
	public void setLogService(ILogService logService) {
		this.logService = logService;
	}

	@PostConstruct
	public void initialize() {
		String userName = this.parameterService.getParameterValue(EnmParameter.BLUESNAP_USER.getId());		
		String password = this.parameterService.getParameterValue(EnmParameter.BLUESNAP_PASSWORD.getId());

		restTemplate = new RestTemplate();
		restTemplate.getInterceptors().add(new BasicAuthorizationInterceptor(userName, password));
	}

	@Override
	public ValueOperationResult<Long> createVendor(Long userId, ExtVendor extVendor) {
		
		return null;
	}

	@Override
	public ValueOperationResult<String> getPaymentFieldToken(Long userId, String operationBy) {
		StringBuilder request = new StringBuilder();
		StringBuilder response = new StringBuilder();
		String responseCode = null;
		Date startDate = null;
		Date endDate = null;
		
		ValueOperationResult<String> operationResult = new ValueOperationResult<String>();
		String blueSnapDomain = this.parameterService.getParameterValue(EnmParameter.BLUESNAP_DOMAIN.getId());
		final String uri =  blueSnapDomain + "/services/2/payment-fields-tokens";
		try {
			RestTemplate restTemplate = this.createRestTemplate();
		     
		    HttpHeaders headers = new HttpHeaders();
		    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		    HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
		     
		    request.append("URL : " + uri);
		    request.append(System.lineSeparator());
		    request.append("user : " + this.parameterService.getParameterValue(EnmParameter.BLUESNAP_USER.getId()));
		    request.append(System.lineSeparator());
		    request.append("pass : " + this.parameterService.getParameterValue(EnmParameter.BLUESNAP_PASSWORD.getId()));
		    
		    startDate = new Date();
		    ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.POST, entity, String.class);
		    endDate = new Date();
		    
		    responseCode = String.valueOf(result.getStatusCode().value());
		    
		    if (result.getStatusCode() == HttpStatus.CREATED) {
		    	String responseUrl = result.getHeaders().getLocation().toString();
		    	response.append(System.lineSeparator());
		    	response.append("response url : " + responseUrl);
		    	String[] responseUriParts = responseUrl.split("/");
			    operationResult.setResultValue(responseUriParts[responseUriParts.length - 1]);
			    operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
		    } else {
		    	operationResult.setResultCode(EnmResultCode.ERROR.getValue());
		    }
		} catch (Exception e) {
			operationResult.setResultCode(EnmResultCode.EXCEPTION.getValue());
			operationResult.setResultDesc(CommonUtil.getExceptionMessage(e));
			responseCode = "-1";
		}  finally {
			if (startDate != null && request != null) {
				if (endDate == null) {
					endDate = new Date();
				}
				IntegrationLog integrationLog = new IntegrationLog();
				integrationLog.setUserId(userId);
				integrationLog.setExtSystemId(EnmExternalSystem.BLUESNAP.getId());
				integrationLog.setOperationId(EnmOperation.BLUESNAP_GET_PAYMENT_FIELD_TOKEN.getId());
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

	private RestTemplate createRestTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		String userName = this.parameterService.getParameterValue(EnmParameter.BLUESNAP_USER.getId());		
		String password = this.parameterService.getParameterValue(EnmParameter.BLUESNAP_PASSWORD.getId());

		restTemplate.getInterceptors().add(new BasicAuthorizationInterceptor(userName, password));
		return restTemplate;
	}

	@Override
	public ValueOperationResult<Long> createVendorDraft(Long userId, String email, String countryCode, String operationBy) {
		StringBuilder request = new StringBuilder();
		StringBuilder response = new StringBuilder();
		String responseCode = null;
		Date startDate = null;
		Date endDate = null;
		
		ValueOperationResult<Long> operationResult = new ValueOperationResult<Long>();
		String blueSnapDomain = this.parameterService.getParameterValue(EnmParameter.BLUESNAP_DOMAIN.getId());
		final String uri =  blueSnapDomain + "/services/2/vendors";
		try {
			RestTemplate restTemplate = this.createRestTemplate();
		     
		    HttpHeaders headers = new HttpHeaders();
		    headers.setContentType(MediaType.APPLICATION_JSON);
		    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		    
		    ExtVendor vendor = new ExtVendor();
		    vendor.setEmail(email);
		    vendor.setCountry(countryCode.toUpperCase());

		    ObjectMapper objectMapper = new ObjectMapper();
		    
		    String strVendor = objectMapper.writeValueAsString(vendor);
		    
		    HttpEntity<String> entity = new HttpEntity<String>(strVendor, headers);
		     
		    request.append("URL : " + uri);
		    request.append(System.lineSeparator());
		    request.append("user : " + this.parameterService.getParameterValue(EnmParameter.BLUESNAP_USER.getId()));
		    request.append(System.lineSeparator());
		    request.append("pass : " + this.parameterService.getParameterValue(EnmParameter.BLUESNAP_PASSWORD.getId()));
		    request.append(System.lineSeparator());
		    request.append("request : " + strVendor);
		    
		    startDate = new Date();
		    ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.POST, entity, String.class);
		    endDate = new Date();
		    
		    responseCode = String.valueOf(result.getStatusCode().value());
		    
		    if (result.getStatusCode() == HttpStatus.CREATED) {
		    	String responseUrl = result.getHeaders().getLocation().toString();
		    	response.append(System.lineSeparator());
		    	response.append("response url : " + responseUrl);
		    	String[] responseUriParts = responseUrl.split("/");
			    operationResult.setResultValue(Long.valueOf(responseUriParts[responseUriParts.length - 1]));
			    operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
		    } else {
		    	operationResult.setResultCode(EnmResultCode.ERROR.getValue());
		    }
		} catch (Exception e) {
			operationResult.setResultCode(EnmResultCode.EXCEPTION.getValue());
			operationResult.setResultDesc(CommonUtil.getExceptionMessage(e));
			responseCode = "-1";
		}  finally {
			if (startDate != null && request != null) {
				if (endDate == null) {
					endDate = new Date();
				}
				IntegrationLog integrationLog = new IntegrationLog();
				integrationLog.setUserId(userId);
				integrationLog.setExtSystemId(EnmExternalSystem.BLUESNAP.getId());
				integrationLog.setOperationId(EnmOperation.BLUESNAP_CREATE_VENDOR_DRAFT.getId());
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
