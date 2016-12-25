package com.ucgen.letserasmus.web.api;

import java.math.BigDecimal;

import javax.servlet.http.HttpSession;

import com.ucgen.letserasmus.library.user.model.User;

public abstract class BaseApiController {

	public User getSessionUser(HttpSession session) {
		Object user = session.getAttribute("appUser");
		if (user != null && user instanceof User) {
			return (User) user;
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
	
}
