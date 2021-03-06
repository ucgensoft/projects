package com.ucgen.letserasmus.web.filter;

import java.io.IOException;

import javax.servlet.DispatcherType;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Level;
import org.ocpsoft.rewrite.servlet.RewriteFilter;

import com.ucgen.common.util.CommonUtil;
import com.ucgen.common.util.FileLogger;
import com.ucgen.common.util.WebUtil;
import com.ucgen.letserasmus.web.view.application.WebApplication;

public class UrlRewriteFilter extends RewriteFilter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		String url = httpRequest.getRequestURL().toString();

		HttpServletRequest httpServletRequest = (HttpServletRequest)request;
		HttpServletResponse httpServletResponse = (HttpServletResponse)response;
		
		if (url.contains("/pages/PlaceDetail.html") && httpServletRequest.getDispatcherType() != DispatcherType.FORWARD) {
			String placeId = ((HttpServletRequest) request).getParameter("placeId");			
			if (placeId != null && !placeId.trim().isEmpty()) {
				String newUrl = WebUtil.concatUrl(WebApplication.getInstance().getUrlPrefix(), "/room/" + placeId);
				httpServletResponse.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
				httpServletResponse.setHeader("Location", newUrl);				
			} else {
				//String newUrl = WebUtil.concatUrl(WebApplication.getInstance().getUrlPrefix(), "/Notfound.html");
				httpServletResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
				//httpServletResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
				//httpServletResponse.setHeader("Location", newUrl);
				//((HttpServletResponse)response).sendRedirect(newUrl);
			}
		} else if (url.contains("DisplayUser.html") && httpServletRequest.getDispatcherType() != DispatcherType.FORWARD) {
			String userId = ((HttpServletRequest) request).getParameter("userId");			
			if (userId != null && !userId.trim().isEmpty()) {
				String newUrl = WebUtil.concatUrl(WebApplication.getInstance().getUrlPrefix(), "/profile/" + userId);
				httpServletResponse.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
				httpServletResponse.setHeader("Location", newUrl);				
			} else {
				httpServletResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
			}
		} else {
			try {
				super.doFilter(request, response, chain);
			} catch (Throwable e) {
				String message = "UrlRewriteFilter - " + CommonUtil.getExceptionMessage(e);
				FileLogger.log(Level.ERROR, message);
				System.out.println(message);
				//httpServletResponse.sendRedirect("/pages/Notfound.html");
			}
		}
	}
	
}
