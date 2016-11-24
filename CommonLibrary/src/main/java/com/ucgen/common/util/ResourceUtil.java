package com.ucgen.common.util;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Level;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

public class ResourceUtil {

	private static Object moduleConfigLock = new String();
	private static Properties moduleConfigProperties;
	private static Map<String, Long> lastModifiedDateMap = null;
	
	private static String CONFIG_FILE_NAME = "AppConfig.properties";

	public static void loadConfigResources() throws IOException {
		try {
			String configPath = AppUtil.getInstance().getConfigPath();
			
			String fullModuleConfigPath = configPath + File.separatorChar + "AppConfig.properties";
			if (isConfigFileModified(fullModuleConfigPath)) {
				File moduleConfigFile = new File(fullModuleConfigPath);
				moduleConfigProperties = new Properties();
				if (moduleConfigFile.exists()) {
					Resource resource = new FileSystemResource(moduleConfigFile);	
					moduleConfigProperties = PropertiesLoaderUtils.loadProperties(resource);
					addFileModifyDate(fullModuleConfigPath, moduleConfigFile.lastModified());
					FileLogger.logOperation(Level.INFO, "Konfig�rasyon bilgileri okundu. Path:" + fullModuleConfigPath);
				} else {
					String cpFilePath = "/config/AppConfig.properties";
					Resource resource = new ClassPathResource(cpFilePath);			
					moduleConfigProperties = PropertiesLoaderUtils.loadProperties(resource);
					FileLogger.logOperation(Level.INFO, "Konfig�rasyon bilgileri  classpath'ten okundu. path: classpath:" + cpFilePath);
				}
			}
			
		} catch (IOException e) {
			FileLogger.logOperation(Level.ERROR, CONFIG_FILE_NAME + " dosyas� y�klenemedi. ResourceUtil-loadModuleConfigResources(), Hata:" + e.getMessage());
			throw e;
		}
	}
	
	public static Properties loadResourceFile(String filePath) throws IOException {
		System.out.println("Konfig�rasyon dosyas� y�klendi. Path:" + filePath);
		Resource resource = new FileSystemResource(filePath);
		return PropertiesLoaderUtils.loadProperties(resource);
	}
	
	public static String getConfigValue(String configParamName) {
		try {
			synchronized (moduleConfigLock) {
				loadConfigResources();
			}
			return moduleConfigProperties.getProperty(configParamName);
		} catch (IOException e) {
			FileLogger.logOperation(Level.ERROR, "ResourceUtil - getConfigValue(), Config de�eri okunurken hata olu�tu. Hata:" + e.getMessage());
			return null;
		}
	}
	
	private static boolean isConfigFileModified(String fileName) {
		File configFile = new File(fileName);
		if (configFile.exists()) {
			if (lastModifiedDateMap == null || !lastModifiedDateMap.containsKey(fileName) || lastModifiedDateMap.get(fileName) != configFile.lastModified()) {
				return true;
			} else {
				return false;
			}
		} else {
			return true;
		}
	}
	
	private static void addFileModifyDate(String fileName, Long modifiedDate) {
		if (lastModifiedDateMap == null) {
			lastModifiedDateMap = new HashMap<String, Long>();
		}
		if (lastModifiedDateMap.containsKey(fileName)) {
			lastModifiedDateMap.remove(fileName);
		}
		lastModifiedDateMap.put(fileName, modifiedDate);
	}
		
}
