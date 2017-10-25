package com.ucgen.common.util;

import java.io.File;
import java.util.Properties;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class FileLogger {
	
	private Logger letsErasmusOperationLogger;
	
	private static String LOG4J_LETSERASMUS_LOG_CAT = "LetsErasmusLogger";
	
	private static final String LOG_LINE_TEMPLATE = "user: %s | operation:%s | ip: %s | message: %s";
	
	private static FileLogger instance;
	
	private ThreadLocal<String> ip = new ThreadLocal<String>();
	private ThreadLocal<String> user = new ThreadLocal<String>();
	
	public void setIp(String ip) {
		this.ip.set(ip);
	}
	
	public void setUser(String user) {
		this.user.set(user);
	}
	
	private FileLogger() {
		
	}
	
	public static FileLogger getInstance() {
		if (instance == null) {
			instance = new FileLogger();
		}
		return instance;
	}
	
	public static void log(Level priority, String message) {
		try {
			log(priority, null, null, message);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public static void log(Level priority, String paramUser, String operationId, String message) {
		try {
			FileLogger.getInstance().logOperation(priority, paramUser, operationId, message);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
		
	public void logOperation(Level priority, String paramUser, String operationId, String message) {
		try {
			String tmpUser = (paramUser != null ? paramUser : this.user.get());
			String logMessage = String.format(LOG_LINE_TEMPLATE, tmpUser, operationId, this.ip.get(), message);
			if (this.letsErasmusOperationLogger != null) {
				this.letsErasmusOperationLogger.log(priority, logMessage);
			} else {
				System.out.println(priority + " - " + logMessage);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void initializeLogger(String log4jConfigFilePath) {
		try {
			//String propFileName = "log4j.appender.LetsErasmusOperationAppender.File";
			//String appName = "LetsErasmus";
			
			File configFile = new File(log4jConfigFilePath);
			if (configFile.exists()) {
				Properties log4jProperties = ResourceUtil.loadResourceFile(log4jConfigFilePath);
				PropertyConfigurator.configure(log4jProperties);
				letsErasmusOperationLogger = Logger.getLogger(LOG4J_LETSERASMUS_LOG_CAT);
			}
			
			/*
			String logFilePath = log4jProperties.get(propFileName).toString();
			String newLogFilePath = logFilePath.replace("\\\\", File.separator); 
			newLogFilePath = newLogFilePath.replace("/", File.separator);
			newLogFilePath = FileUtil.concatPath(newLogFilePath.substring(0, newLogFilePath.lastIndexOf(File.separator) + 1), appName, appName + ".log"); 
			
			log4jProperties.remove(propFileName);
			log4jProperties.put(propFileName, newLogFilePath);
			*/
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
}
