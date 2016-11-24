package com.ucgen.common.util;

import java.io.File;
import java.util.Properties;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class FileLogger {

	private static Logger ucgenOperationLogger;
	
	private static String LOG4J_UCGEN_OPERATION_LOG_CAT = "UcgenOperationLogger";

	public static void logOperation(Level priority, String message) {
		try {
			if (ucgenOperationLogger == null) {
				initializeLogger();
			}
			ucgenOperationLogger.log(priority, message);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public static void initializeLogger() {
		try {
			String propFileName = "log4j.appender.UcgenOperationAppender.File";
			String configPath = AppUtil.getInstance().getConfigPath();
			String appName = "LetsErasmus";//AppUtil.getInstance().getAppModule().getName();
			
			String log4jConfigFilePath = configPath + File.separatorChar + "Log4j.properties";
			
			Properties log4jProperties = ResourceUtil.loadResourceFile(log4jConfigFilePath);
			
			String logFilePath = log4jProperties.get(propFileName).toString();
			String newLogFilePath = logFilePath.replace("\\\\", File.separator); 
			newLogFilePath = newLogFilePath.replace("/", File.separator);
			newLogFilePath = FileUtil.concatPath(newLogFilePath.substring(0, newLogFilePath.lastIndexOf(File.separator) + 1), appName, appName + ".log"); 
			
			log4jProperties.remove(propFileName);
			log4jProperties.put(propFileName, newLogFilePath);
			
			PropertyConfigurator.configure(log4jProperties);	
			ucgenOperationLogger = Logger.getLogger(LOG4J_UCGEN_OPERATION_LOG_CAT);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
}
