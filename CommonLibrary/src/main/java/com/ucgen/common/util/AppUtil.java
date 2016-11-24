package com.ucgen.common.util;

import java.io.Closeable;
import java.io.File;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class AppUtil {

	private ApplicationContext solrbContext;
	private ApplicationContext bscsContext;
	private boolean isSolrbContextLocal;
	private boolean isBscsContextLocal;
	private String configPath;
	private String test;
	
	private static AppUtil singleInstance;
	
	public void setSolrbContext(ApplicationContext solrbContext) {
		this.solrbContext = solrbContext;
	}

	public void setBscsContext(ApplicationContext bscsContext) {
		this.bscsContext = bscsContext;
	}
	
	public String getConfigPath() {
		return configPath;
	}

	public void setConfigPath(String configPath) {
		this.configPath = configPath;
	}
	
	public void setSolrbContextLocal(boolean isSolrbContextLocal) {
		this.isSolrbContextLocal = isSolrbContextLocal;
	}

	public void setBscsContextLocal(boolean isBscsContextLocal) {
		this.isBscsContextLocal = isBscsContextLocal;
	}

	private AppUtil() {
	}
	
	public static AppUtil createInstance(ApplicationContext applicationContext, String configPath, boolean createBscsContext) throws Exception {
		ApplicationContext tmpSolrbContext = applicationContext;
		ApplicationContext tmpBscsContext = null;
		boolean solrbContextLocal = false;
		boolean bscsContextLocal = false;
		if (applicationContext == null) {
			
			String solrbContextFileName = "solrbContext.xml";
			String bscsContextFileName = "bscsContext.xml";
			
			String solrbConfigPath = configPath + File.separatorChar + solrbContextFileName;
			File solrbConfigFile = new File(solrbConfigPath);
			
			if (solrbConfigFile.exists()) {
				System.out.println("Loading:" + solrbConfigPath);
				tmpSolrbContext = new FileSystemXmlApplicationContext(solrbConfigPath);
			} else {
				System.out.println("Loading classpath config");
				tmpSolrbContext = new ClassPathXmlApplicationContext("/spring/common/" + solrbContextFileName);
			}
			
			if (createBscsContext) {
				String bscsConfigPath = configPath + File.separatorChar + bscsContextFileName;
				File bscsConfigFile = new File(solrbConfigPath);
				
				if (bscsConfigFile.exists()) {
					tmpBscsContext = new FileSystemXmlApplicationContext(bscsConfigPath);
				} else {
					tmpBscsContext = new ClassPathXmlApplicationContext("/spring/common/" + bscsContextFileName);
				}
				bscsContextLocal = true;
			}
			
		}
		
		singleInstance = new AppUtil();
		singleInstance.setSolrbContextLocal(solrbContextLocal);
		singleInstance.setBscsContextLocal(bscsContextLocal);
		singleInstance.setSolrbContext(tmpSolrbContext);
		singleInstance.setBscsContext(tmpBscsContext);
		singleInstance.setConfigPath(configPath);
		return singleInstance;
	}
	
	public static AppUtil createInstance(ApplicationContext applicationContext, String configPath) throws Exception {
		return AppUtil.createInstance(applicationContext, configPath, false);
	}
	
	public static AppUtil getInstance() {
		return singleInstance;
	}
	
	public Object getContextObject(String beanName) {
		try {
			return solrbContext.getBean(beanName);
		} catch (NoSuchBeanDefinitionException e) {
			return null;
		}
	}
	
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		if (this.solrbContext != null && isSolrbContextLocal) {
			((Closeable) this.solrbContext).close();
		}
		if (this.bscsContext != null && isBscsContextLocal) {
			((Closeable) this.bscsContext).close();
		}
	}
	
}
