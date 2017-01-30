package com.ucgen.letserasmus.web.view.application;

import com.ucgen.letserasmus.library.file.enumeration.EnmFileType;

public class AppUtil {

	public static final String URL_SEPARATOR = "/";
	
	public static String getSmallUserPhotoName(Long userId, Long fileId, EnmFileType fileType) {
		return String.format(AppConstants.USER_FILE_NAME_TEMPLATE, userId, fileId, fileType.getFileSuffix());
	}
	
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
