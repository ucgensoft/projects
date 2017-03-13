package com.ucgen.common.util;

import java.io.IOException;
import java.util.Properties;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

public class ResourceUtil {
	
	public static Properties loadResourceFile(String filePath) throws IOException {
		System.out.println("Konfigürasyon dosyasý yüklendi. Path:" + filePath);
		Resource resource = new FileSystemResource(filePath);
		return PropertiesLoaderUtils.loadProperties(resource);
	}
		
}
