package com.ucgen.letserasmus.web.view.application;

public class AppUtil {

	public static final String URL_SEPARATOR = "/";
	
	public static String concatPath(String path, String... subPathArr) {
		for (String subPath : subPathArr) {
			if (!path.endsWith(URL_SEPARATOR)) {
				path += URL_SEPARATOR;
			}
			path += subPath;
		}
		
		return path;
	}
	
}
