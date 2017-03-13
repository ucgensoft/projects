package com.ucgen.letserasmus.library.common;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ucgen.common.util.CommonUtil;
import com.ucgen.common.util.FileLogger;
import com.ucgen.letserasmus.library.parameter.enumeration.EnmParameter;
import com.ucgen.letserasmus.library.parameter.service.IParameterService;

@Service
public class AppInitializer {

	private IParameterService parameterService;

	@Autowired
	public void setParameterService(IParameterService parameterService) {
		this.parameterService = parameterService;
	}
	
	@PostConstruct
	public void initialize() {
		try {
			String log4jConfigFilePath = this.parameterService.getParameterValue(EnmParameter.LOG_CONFIG_FILE_PATH.getId());
			FileLogger.getInstance().initializeLogger(log4jConfigFilePath);
		} catch (Exception e) {
			System.out.println("Application initialization is failed. Error: " + CommonUtil.getExceptionMessage(e));
		}
	}
	
}
