package com.ucgen.letserasmus.library.common;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ucgen.common.util.CommonUtil;
import com.ucgen.common.util.FileLogger;
import com.ucgen.common.util.FileUtil;
import com.ucgen.common.util.aws.AwsS3Util;
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
			System.out.println("Config file initialization is failed. Error: " + CommonUtil.getExceptionMessage(e));
		}
		try {
			String awsCredentials = this.parameterService.getParameterValue(EnmParameter.AWS_CREDENTIALS.getId());
			String[] credentialParamList = awsCredentials.split(",");
			//String awsUserFilesBucketName = this.parameterService.getParameterValue(EnmParameter.AWS_USER_FILES_BUCKET_NAME.getId());
			
			List<String> awsFileContent = new ArrayList<String>();
			awsFileContent.add("[" + credentialParamList[0] + "]");
			awsFileContent.add("aws_access_key_id=" + credentialParamList[1]);
			awsFileContent.add("aws_secret_access_key=" + credentialParamList[2]);
			
			String rootAppFolder = this.parameterService.getParameterValue(EnmParameter.LOCAL_APP_ROOT_PATH.getId());
			String awsFilePath = FileUtil.concatPath(rootAppFolder, "WEB-INF", "aws_credentials");
			
			FileUtil.writeToFile(awsFilePath, awsFileContent, false);
			
			
			AwsS3Util.initialize(awsFilePath, "erasmus_admin");
			
		} catch (Exception e) {
			System.out.println("AWS object initialization failed. Error: " + CommonUtil.getExceptionMessage(e));
		}
	}
	
}
