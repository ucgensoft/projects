package com.ucgen.common.util;

import java.security.SecureRandom;
import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;

public class SecurityUtil {

	private static final String ALPHA_NUMERIC_COMPLEX_CHAR_SET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%&*()-_=+[{]}\\|;:\'\",<.>/?";
	private static final String ALPHA_NUMERIC_SIMPLE_CHAR_SET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	private static final String NUMERIC_CHAR_SET = "0123456789";
	
	private static char[] alphaNumericComplexCharacters;
	private static char[] alphaNumericSimpleCharacters;
	private static char[] numericCharacters;
	
	static {
		alphaNumericSimpleCharacters = ALPHA_NUMERIC_SIMPLE_CHAR_SET.toCharArray();
		alphaNumericComplexCharacters = ALPHA_NUMERIC_COMPLEX_CHAR_SET.toCharArray();
		numericCharacters = NUMERIC_CHAR_SET.toCharArray();
	}
	
	public static String generateUUID() {
		return UUID.randomUUID().toString().replace("-", "");
	}
	
	public static String generateToken(int length) {
		return RandomStringUtils.random( length, 0, alphaNumericSimpleCharacters.length-1, false, false, alphaNumericSimpleCharacters, new SecureRandom());
	}
	
	public static String generateAlphaNumericCode(int length) {
		return RandomStringUtils.random( length, 0, alphaNumericComplexCharacters.length-1, false, false, alphaNumericComplexCharacters, new SecureRandom());
	}
	
	public static String generateNumericCode(int length) {
		return RandomStringUtils.random( length, 0, numericCharacters.length-1, false, false, numericCharacters, new SecureRandom());
	}
	
}
