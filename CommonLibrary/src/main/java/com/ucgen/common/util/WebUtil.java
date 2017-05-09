package com.ucgen.common.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;

public class WebUtil {

	public static final String URL_SEPERATOR = "/";
	
	public static final int HTTP_SUCCESS = 200;
	public static final int HTTP_CREATED = 201;
	public static final int HTTP_ACCEPTED = 202;
	
	public static String addUriParam(String uri, String paramName, Object paramValue) {
		StringBuilder uriBuilder = new StringBuilder(uri);
		if (!uri.contains("?")) {
			uriBuilder.append("?");
			uriBuilder.append(paramName);
			uriBuilder.append("=");
		} else {
			uriBuilder.append("&");
			uriBuilder.append(paramName);
			uriBuilder.append("=");
		}
		String strParamValue = paramValue.toString();
		try {
			uriBuilder.append(URLEncoder.encode(strParamValue, Charset.defaultCharset().name()));
		} catch (UnsupportedEncodingException e) {
			
		}
		
		return uriBuilder.toString();
	}
	
	public static String concatUrl(String url, String... subUrlArr) {
		for (String subPath : subUrlArr) {
			if (!url.endsWith(URL_SEPERATOR)) {
				url += URL_SEPERATOR;
			}
			url += subPath;
		}
		
		return url;
	}
	
}
