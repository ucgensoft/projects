package com.ucgen.letserasmus.util;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

public class WebUtil {
	
	public static <T> T getSessionAttribute(String attributeName, Class<T> clazz) {
		HttpSession httpSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		return WebUtil.getSessionAttribute(httpSession, attributeName, clazz);
	}

	@SuppressWarnings("unchecked")
	public static <T> T getSessionAttribute(HttpSession httpSession, String attributeName, Class<T> clazz) {
		if (httpSession != null) {
			Object sessionValue = ((HttpSession) httpSession).getAttribute(attributeName);
			if (sessionValue != null && clazz.isInstance(sessionValue)) {
				return (T) sessionValue;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

}
