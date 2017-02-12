package com.ucgen.common.util;

import java.util.UUID;

public class SecurityUtil {

	public static String generateUUID() {
		return UUID.randomUUID().toString();
	}
	
}
