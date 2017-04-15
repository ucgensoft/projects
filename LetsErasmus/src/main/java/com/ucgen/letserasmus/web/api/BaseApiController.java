package com.ucgen.letserasmus.web.api;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.ucgen.common.util.SecurityUtil;
import com.ucgen.letserasmus.library.log.enumeration.EnmOperation;
import com.ucgen.letserasmus.library.parameter.enumeration.EnmParameter;
import com.ucgen.letserasmus.library.parameter.service.IParameterService;
import com.ucgen.letserasmus.library.user.model.User;
import com.ucgen.letserasmus.web.view.application.EnmSession;

public abstract class BaseApiController {

	protected IParameterService parameterService;
	
	@Autowired
	public void setParameterService(IParameterService parameterService) {
		this.parameterService = parameterService;
	}
	
	public HttpSession getSession() {
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		return attr.getRequest().getSession();
	}
	
	public User getSessionUser(HttpSession session) {
		Object user = session.getAttribute(EnmSession.USER.getId());
		if (user != null && user instanceof User) {
			return (User) user;
		} else {
			return null;
		}
	}
	
	public boolean setSessionAttribute(String attributeName, Object attributeValue) {
		HttpSession session = this.getSession();
		if (session != null) {
			session.removeAttribute(attributeName);
			session.setAttribute(attributeName, attributeValue);
			return true;
		} else {
			return false;
		}
	}
	
	public <T> void saveOperationToken(String tokenId, EnmOperation operation, T object) {
		HttpSession session = this.getSession();
		if (session != null) {
			Map<Integer, Map<String, Object>> sessionOperationTokenMap = null;
			Object operationTokenMap = session.getAttribute(EnmSession.OPERATION_TOKEN.getId());
			if (operationTokenMap == null) { 
				Map<Integer, Map<String, Object>> newOperationTokenMap = new HashMap<Integer, Map<String, Object>>();
				session.setAttribute(EnmSession.OPERATION_TOKEN.getId(), newOperationTokenMap);
				sessionOperationTokenMap = newOperationTokenMap;
			} else {
				sessionOperationTokenMap = (HashMap<Integer, Map<String, Object>>) operationTokenMap;
			}
			
			Map<String, Object> tokenMap = null;
			if (!sessionOperationTokenMap.containsKey(operation.getId())) {
				tokenMap = new HashMap<String, Object>();
				tokenMap.put(tokenId, object);
				sessionOperationTokenMap.put(operation.getId(), tokenMap);
			} else {
				tokenMap = sessionOperationTokenMap.get(operation.getId());
				tokenMap.put(tokenId, object);
			}
		}
	}
	
	public <T> T getObjectForToken(String tokenId, Integer operationId) {
		HttpSession session = this.getSession();
		if (session != null) {
			Object operationTokenMap = session.getAttribute(EnmSession.OPERATION_TOKEN.getId());
			if (operationTokenMap != null) {
				Map<Integer, Map<String, Object>> sessionOperationTokenMap = (HashMap<Integer, Map<String, Object>>) operationTokenMap;
				if (sessionOperationTokenMap.containsKey(operationId) 
						&& sessionOperationTokenMap.get(operationId).containsKey(tokenId)) {
					return (T) sessionOperationTokenMap.get(operationId).get(tokenId);
				} else {
					return null;
				}
			} else {
				return null;
			}
		} else {
			return null;
		}
	}
	
	public void savePaymentToken(String operationTokenId, String paymentToken) {
		HttpSession session = this.getSession();
		if (session != null) {
			Map<String, String> sessionPaymentTokenMap = null;
			Object paymentTokenMap = session.getAttribute(EnmSession.PAYMENT_TOKEN.getId());
			if (paymentTokenMap == null) { 
				Map<String, String> newPaymentTokenMap = new HashMap<String, String>();
				session.setAttribute(EnmSession.PAYMENT_TOKEN.getId(), newPaymentTokenMap);
				sessionPaymentTokenMap = newPaymentTokenMap;
			} else {
				sessionPaymentTokenMap = (HashMap<String, String>) paymentTokenMap;
			}
			if (sessionPaymentTokenMap.containsKey(operationTokenId)) {
				sessionPaymentTokenMap.remove(operationTokenId);
			}
			sessionPaymentTokenMap.put(operationTokenId, paymentToken);
		}
	}
	
	public String getPaymentToken(String operationTokenId) {
		HttpSession session = this.getSession();
		if (session != null) {
			Map<String, String> sessionPaymentTokenMap = null;
			Object paymentTokenMap = session.getAttribute(EnmSession.PAYMENT_TOKEN.getId());
			if (paymentTokenMap != null) { 
				return ((Map<String, String>) paymentTokenMap).get(operationTokenId);
			} else {
				return null;
			}
		} else {
			return null;
		}
	}
	
	public String getString(Object jsonValue) {
		if (jsonValue != null) {
			if (jsonValue instanceof String) {
				return (String) jsonValue;
			} else {
				return jsonValue.toString();
			}
		} else {
			return null;
		}
	}
	
	public Integer getInteger(Object jsonValue) {
		if (jsonValue != null) {
			if (jsonValue instanceof Integer) {
				return (Integer) jsonValue;
			} else {
				return Integer.valueOf(jsonValue.toString());
			}
		} else {
			return null;
		}
	}
	
	public Long getLong(Object jsonValue) {
		if (jsonValue != null) {
			if (jsonValue instanceof Long) {
				return (Long) jsonValue;
			} else {
				return Long.valueOf(jsonValue.toString());
			}
		} else {
			return null;
		}
	}
	
	public BigDecimal getBigDecimal(Object jsonValue) {
		if (jsonValue != null) {
			if (jsonValue instanceof BigDecimal) {
				return (BigDecimal) jsonValue;
			} else {
				return new BigDecimal(jsonValue.toString());
			}
		} else {
			return null;
		}
	}
	
	public String generateOperationToken() {
		String tokenLength = this.parameterService.getParameterValue(EnmParameter.OPERATIN_TOKEN_LENGTH.getId());
		return SecurityUtil.generateAlphaNumericCode(Integer.valueOf(tokenLength));
	}
	
}
