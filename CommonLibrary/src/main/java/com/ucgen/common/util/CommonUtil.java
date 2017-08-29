package com.ucgen.common.util;

import java.net.InetAddress;

import org.apache.commons.lang3.exception.ExceptionUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CommonUtil {

	public static String getIp() {
		try {
			InetAddress ip = InetAddress.getLocalHost();
			return ip.getHostAddress();
		} catch (Exception e) {
			return "";
		}
	}
	
	public static String getExceptionMessage(Exception e) {
		return "Hata Mesajı:" + e.getMessage() + ", Hata Yeri:" + ExceptionUtils.getStackTrace(e); 
	}
	
	public static String toJson(Object obj) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		String objJson = mapper.writeValueAsString(obj);
		return objJson;
	}
	
}
