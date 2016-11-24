package com.ucgen.common.util;

import java.net.InetAddress;

import org.apache.commons.lang3.exception.ExceptionUtils;

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
		return "Hata Mesaj�:" + e.getMessage() + ", Hata Yeri:" + ExceptionUtils.getStackTrace(e); 
	}
	
}
