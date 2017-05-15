package com.ucgen.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationUtil {

	private static Pattern pattern;
	private static Matcher matcher;

	private static final String EMAIL_PATTERN =
		"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
		+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	static {
		pattern = Pattern.compile(EMAIL_PATTERN);
	}
	
	public static boolean isEmailValid(String email) {
		matcher = pattern.matcher(email);
		return matcher.matches();
	}
	
	public static boolean isPasswordValid(String password, Integer length) {
		String pattern = "(?=.*[0-9])(?=.*[a-zA-Z]).{" + length + ",}";
		return password.matches(pattern);
	}
	
}
