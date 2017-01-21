package com.ucgen.letserasmus.web.view;

import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


public abstract class BaseController {

	public static final String PAGING_TEMPLATE = "{CurrentPageReport}  {FirstPageLink} {PreviousPageLink} {JumpToPageDropdown} {NextPageLink} {LastPageLink} {RowsPerPageDropdown}";
	public static final String PAGING_REPORT_TEMPLATE = "( {currentPage} / {totalPages} )";
	public static final String ROWS_PER_PAGE = "10,20,30,50";

	public static final String UI_SHORT_DATE_FORMAT = "dd.MM.yyyy";
	public static final String UI_DATE_TIME_FORMAT = "dd.MM.yyyy HH:mm:ss";
	public static final String UI_DECIMAL_LOCALE = "tr-TR";

	public HttpSession getSession() {
		Object session = FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		if (session != null) {
			return (HttpSession) session;
		} else {
			return null;
		}
	}
	
	public Object getSessionAttribute(String attributeName) {
		HttpSession session = this.getSession();
		if (session != null) {
			return session.getAttribute(attributeName);
		} else {
			return null;
		}
	}

	public HttpServletRequest getRequest() {
		Object request = FacesContext.getCurrentInstance().getExternalContext().getRequest();
		if (request != null) {
			return (HttpServletRequest) request;
		} else {
			return null;
		}
	}

	public String getPagingTemplate() {
		return PAGING_TEMPLATE;
	}

	public String getPagingReportTemplate() {
		return PAGING_REPORT_TEMPLATE;
	}
	
	public String getRowsPerPageTemplate() {
		return ROWS_PER_PAGE;
	}

	public static String getRequestParameter(String paramName) {
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		return params.get(paramName);
	}

	public static String getClientIp(FacesContext faces) {
		return ((HttpServletRequest) faces.getExternalContext().getRequest()).getRemoteAddr();
	}

	public static String getClientHost(FacesContext faces) {
		return ((HttpServletRequest) faces.getExternalContext().getRequest()).getRemoteHost();
	}

	public static String getClientName(FacesContext faces) {
		return ((HttpServletRequest) faces.getExternalContext().getRequest()).getRemoteUser();
	}

	public static void addMessage(FacesMessage.Severity severity, String summary, String detail) {
		FacesMessage message = new FacesMessage(severity, summary, detail);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public static void addMessage(String summary) {
		FacesMessage message = new FacesMessage(summary);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public static void addMessage(String summary, String detail) {
		FacesMessage message = new FacesMessage(summary, detail);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public static void error(String summary) {
		addMessage(FacesMessage.SEVERITY_ERROR, summary, null);
	}

	public static void error(String summary, String detail) {
		addMessage(FacesMessage.SEVERITY_ERROR, summary, detail);
	}

	public static void fatal(String summary) {
		addMessage(FacesMessage.SEVERITY_FATAL, summary, null);
	}

	public static void fatal(String summary, String detail) {
		addMessage(FacesMessage.SEVERITY_FATAL, summary, detail);
	}

	public static void info(String summary) {
		addMessage(summary);
	}

	public static void info(String summary, String detail) {
		addMessage(summary, detail);
	}

	public static void warn(String summary) {
		addMessage(FacesMessage.SEVERITY_WARN, summary, null);
	}

	public static void warn(String summary, String detail) {
		addMessage(FacesMessage.SEVERITY_WARN, summary, detail);
	}

	public static boolean isEmptyString(String str) {
		return (str == null || str.trim().isEmpty());
	}
}
