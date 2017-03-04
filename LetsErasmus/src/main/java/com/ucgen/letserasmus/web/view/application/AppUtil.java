package com.ucgen.letserasmus.web.view.application;

public class AppUtil {

	public static final String URL_SEPARATOR = "/";
	
	public static String concatPath(String path, String... subPathArr) {
		for (String subPath : subPathArr) {
			if (!path.endsWith(URL_SEPARATOR) 
					&& !subPath.startsWith(URL_SEPARATOR)) {
				path += URL_SEPARATOR;
			} else if(path.endsWith(URL_SEPARATOR) 
					&& subPath.startsWith(URL_SEPARATOR)) {
				if (path.length() > 1) {
					path.substring(0, path.length() - 1);
				} else {
					path = "";
				}
			}
			path += subPath;
		}
		
		return path;
	}
	
}
