package com.ucgen.letserasmus.library.mail.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ucgen.common.operationresult.EnmResultCode;
import com.ucgen.common.operationresult.OperationResult;
import com.ucgen.common.util.CommonUtil;
import com.ucgen.common.util.DateUtil;
import com.ucgen.common.util.FileUtil;
import com.ucgen.common.util.MailUtil;
import com.ucgen.letserasmus.library.mail.constants.IMailConstants;
import com.ucgen.letserasmus.library.mail.service.IMailService;
import com.ucgen.letserasmus.library.parameter.enumeration.EnmParameter;
import com.ucgen.letserasmus.library.parameter.service.IParameterService;
import com.ucgen.letserasmus.library.user.model.User;

@Service
public class MailService implements IMailService, IMailConstants {

	private String urlPrefix;
	private String localAppPath;
	private String emailVerificationUrl;
	private String resetPasswordUrl;
	
	private MailUtil mailUtil;
	private IParameterService parameterService;
	
	@Autowired
	public void setMailUtil(MailUtil mailUtil) {
		this.mailUtil = mailUtil;
	}
	
	@Autowired
	public void setParameterService(IParameterService parameterService) {
		this.parameterService = parameterService;
	}

	@PostConstruct
	public void initialize() {
		this.localAppPath = this.parameterService.getParameterValue(EnmParameter.LOCAL_APP_ROOT_PATH.getId());
		urlPrefix = this.parameterService.getParameterValue(EnmParameter.LETSERASMUS_URL_PREFIX.getId());
		
		emailVerificationUrl = urlPrefix + "/pages/Main.xhtml?user=#paramUserId#&code=#paramCode#&op=10";
		resetPasswordUrl = urlPrefix + "/pages/Main.xhtml?user=#paramUserId#&code=#paramCode#&op=12";
	}
	
	public OperationResult sendNewMessageMail(String email, String placeTitle, String messageText) {
		OperationResult operationResult = new OperationResult();
		try {
			String htmlFilePath = this.getMailTemplatePath("NewMessage.html");	
			
			List<String> toList = new ArrayList<String>();
			toList.add(email);
			
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("#paramPlaceDetailTitle#", placeTitle);
			paramMap.put("#paramMessageText#", messageText);
			paramMap.put("#paramTimeStamp#", DateUtil.format(new Date(), DateUtil.READABLE_DATE_FORMAT));
			
			String subject = MAIL_SUBJECT_NEW_MESSAGE.replace("#paramPlaceDetailTitle#", placeTitle);
			
			this.mailUtil.sendMailFromTemplate(htmlFilePath, paramMap, toList, null, subject, null);
			operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
		} catch (Exception e) {
			operationResult.setResultCode(EnmResultCode.EXCEPTION.getValue());
			operationResult.setResultDesc("MailService-sendNewMessageMail() Error: " + CommonUtil.getExceptionMessage(e));
		}
		return operationResult;
	}
	
	public OperationResult sendEmailVerificationMail(User user, String email) {
		OperationResult operationResult = new OperationResult();
		try {
			String htmlFilePath = this.getMailTemplatePath("VerifyEmail.html");	
			
			
			List<String> toList = new ArrayList<String>();
			toList.add(email);
			
			String tmpEmailVerificationUrl = this.emailVerificationUrl;
			tmpEmailVerificationUrl = tmpEmailVerificationUrl.replaceAll("#paramUserId#", user.getId().toString());
			tmpEmailVerificationUrl = tmpEmailVerificationUrl.replaceAll("#paramCode#", user.getUserActivationKeyEmail());
			
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("#paramUserName#", user.getFirstName());
			paramMap.put("#paramEmailVerificationUrl#", tmpEmailVerificationUrl);
			paramMap.put("#paramTimeStamp#", DateUtil.format(new Date(), DateUtil.READABLE_DATE_FORMAT));
			this.mailUtil.sendMailFromTemplate(htmlFilePath, paramMap, toList, null, MAIL_SUBJECT_EMAIL_VERIFICATION, null);
			
			operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
		} catch (Exception e) {
			operationResult.setResultCode(EnmResultCode.EXCEPTION.getValue());
			operationResult.setResultDesc("MailService-sendNewMessageMail() Error: " + CommonUtil.getExceptionMessage(e));
		}
		return operationResult;
	}
	
	public OperationResult sendResetPasswordMail(User user) {
		OperationResult operationResult = new OperationResult();
		try {
			
			String htmlFilePath = this.getMailTemplatePath("ResetPassword.html");	
			
			List<String> toList = new ArrayList<String>();
			toList.add(user.getEmail());
			
			String tmpResetPasswordUrl = this.resetPasswordUrl;
			tmpResetPasswordUrl = tmpResetPasswordUrl.replaceAll("#paramUserId#", user.getId().toString());
			tmpResetPasswordUrl = tmpResetPasswordUrl.replaceAll("#paramCode#", user.getResetPasswordToken());
			
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("#paramUserName#", user.getFirstName());
			paramMap.put("#paramResetPasswordUrl#", tmpResetPasswordUrl);
			paramMap.put("#paramTimeStamp#", DateUtil.format(new Date(), DateUtil.READABLE_DATE_FORMAT));
			this.mailUtil.sendMailFromTemplate(htmlFilePath, paramMap, toList, null, MAIL_SUBJECT_RESET_PASSWORD, null);
			
			operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
		} catch (Exception e) {
			operationResult.setResultCode(EnmResultCode.EXCEPTION.getValue());
			operationResult.setResultDesc("MailService-sendNewMessageMail() Error: " + CommonUtil.getExceptionMessage(e));
		}
		return operationResult;
	}
	
	public OperationResult sendNewPasswordMail(User user) {
		OperationResult operationResult = new OperationResult();
		try {
			String htmlFilePath = this.getMailTemplatePath("NewPassword.html");	
			
			List<String> toList = new ArrayList<String>();
			toList.add(user.getEmail());
			
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("#paramUserName#", user.getFirstName());
			paramMap.put("#paramNewPassword#", user.getPassword());
			paramMap.put("#paramTimeStamp#", DateUtil.format(new Date(), DateUtil.READABLE_DATE_FORMAT));
			this.mailUtil.sendMailFromTemplate(htmlFilePath, paramMap, toList, null, MAIL_SUBJECT_NEW_PASSWORD, null);
			
			operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
		} catch (Exception e) {
			operationResult.setResultCode(EnmResultCode.EXCEPTION.getValue());
			operationResult.setResultDesc("MailService-sendNewMessageMail() Error: " + CommonUtil.getExceptionMessage(e));
		}
		return operationResult;
	}
	
	public OperationResult sendBokingRequestMail(String guestEmail, String hostEmail, String placeTitle, String placeDetailUrl, 
			String placeImageUrl, String homeType, String placeType, Date checkinDate, Date checkoutDate) {
				
		OperationResult operationResult = new OperationResult();
		try {
						
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("#paramPlaceDetailUrl#", placeDetailUrl);
			paramMap.put("#paramPlaceImageUrl#", placeImageUrl);
			paramMap.put("#paramPlaceDetailHomeType#", homeType);
			paramMap.put("#paramPlaceDetailPlaceType#", placeType);
			paramMap.put("#paramRequestMoveInDate#", DateUtil.format(checkinDate, DateUtil.READABLE_DATE_FORMAT));
			paramMap.put("#paramRequestMoveOutDate#", DateUtil.format(checkoutDate, DateUtil.READABLE_DATE_FORMAT));
			paramMap.put("#paramTimeStamp#", DateUtil.format(new Date(), DateUtil.READABLE_DATE_FORMAT));
			
			String guestSubject = MAIL_SUBJECT_GUEST_BOOKING_REQUEST.replace("#paramPlaceDetailTitle#", placeTitle);
			String guestHtmlFilePath = this.getMailTemplatePath("GuestBookingRequest.html");
			List<String> guestToList = new ArrayList<String>();
			guestToList.add(guestEmail);
			this.mailUtil.sendMailFromTemplate(guestHtmlFilePath, paramMap, guestToList, null, guestSubject, null);
			
			String hostSubject = MAIL_SUBJECT_HOST_BOOKING_REQUEST.replace("#paramPlaceDetailTitle#", placeTitle);
			String hostHtmlFilePath = this.getMailTemplatePath("HostBookingRequest.html");
			List<String> hostToList = new ArrayList<String>();
			hostToList.add(hostEmail);
			this.mailUtil.sendMailFromTemplate(hostHtmlFilePath, paramMap, hostToList, null, hostSubject, null);
			
			operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
		} catch (Exception e) {
			operationResult.setResultCode(EnmResultCode.EXCEPTION.getValue());
			operationResult.setResultDesc("MailService-sendNewMessageMail() Error: " + CommonUtil.getExceptionMessage(e));
		}
		return operationResult;
	}
	
	private String getMailTemplatePath(String templateName) {
		return FileUtil.concatPath(this.localAppPath, "static", "html", "mail", templateName);	
	}
	
}
