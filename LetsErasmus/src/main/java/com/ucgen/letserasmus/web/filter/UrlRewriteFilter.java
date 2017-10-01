package com.ucgen.letserasmus.web.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.ocpsoft.rewrite.servlet.RewriteFilter;

public class UrlRewriteFilter extends RewriteFilter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		String url = httpRequest.getRequestURL().toString();
		if (url.contains("/static/") 
				|| url.contains("/place/")
				|| url.contains("/user/")
				|| url.contains("sitemap.xml")
				|| url.contains("robots.txt")) {
			super.doFilter(request, response, chain);
		} else {
			super.doFilter(request, response, chain);
		}
	}
	
}
