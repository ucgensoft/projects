package com.ucgen.letserasmus.library.mail.service.impl;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;

import org.apache.log4j.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailParseException;
import org.springframework.stereotype.Service;

import com.ucgen.common.operationresult.EnmResultCode;
import com.ucgen.common.operationresult.OperationResult;
import com.ucgen.common.util.CommonUtil;
import com.ucgen.common.util.DateUtil;
import com.ucgen.common.util.FileLogger;
import com.ucgen.common.util.FileUtil;
import com.ucgen.common.util.MailUtil;
import com.ucgen.letserasmus.library.batch.EmailSender;
import com.ucgen.letserasmus.library.common.enumeration.EnmCurrency;
import com.ucgen.letserasmus.library.location.model.Location;
import com.ucgen.letserasmus.library.mail.constants.IMailConstants;
import com.ucgen.letserasmus.library.mail.model.Email;
import com.ucgen.letserasmus.library.mail.service.IMailService;
import com.ucgen.letserasmus.library.parameter.enumeration.EnmParameter;
import com.ucgen.letserasmus.library.parameter.service.IParameterService;
import com.ucgen.letserasmus.library.place.model.Place;
import com.ucgen.letserasmus.library.reservation.model.Reservation;
import com.ucgen.letserasmus.library.user.model.User;

@Service
public class MailService implements IMailService, IMailConstants {

	private String urlPrefix;
	private String localAppPath;
	private String emailVerificationUrl;
	private String resetPasswordUrl;
	
	private EmailSender emailSender;
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

	@Autowired
	public void setEmailSender(EmailSender emailSender) {
		this.emailSender = emailSender;
	}

	@PostConstruct
	public void initialize() {
		this.localAppPath = this.parameterService.getParameterValue(EnmParameter.LOCAL_APP_ROOT_PATH.getId());
		urlPrefix = this.parameterService.getParameterValue(EnmParameter.LETSERASMUS_URL_PREFIX.getId());
		
		emailVerificationUrl = urlPrefix + "/pages/Main.html?user=#paramUserId#&code=#paramCode#&op=10";
		resetPasswordUrl = urlPrefix + "/pages/Main.html?user=#paramUserId#&code=#paramCode#&op=12";
	}
	
	private String getMailTemplatePath(String templateName) {
		return FileUtil.concatPath(this.localAppPath, "static", "html", "mail", templateName);	
	}
	
	public void sendMailFromTemplate(String templateFilePath, Map<String, String> paramMap, List<String> toList, List<String> ccList, String subject, File file, boolean directSend) throws MailParseException {
		String emailContent = FileUtil.readFileAsString(templateFilePath);
		
		if (paramMap != null && paramMap.size() >0) {
			for (Entry<String, String> paramEntry : paramMap.entrySet()) {
				emailContent = emailContent.replaceAll(paramEntry.getKey(), paramEntry.getValue());
			}
		}
		if (directSend) {
			this.mailUtil.sendMail(emailContent, toList, ccList, subject, file);
		} else {
			Email email = new Email(subject, emailContent, toList, ccList, file);
			this.emailSender.addEmail(email);
		}
		try {
			String paramErasmusRespEmail = this.parameterService.getParameterValue(EnmParameter.LETSERASMUS_RESPONSIBLE_EMAIL.getId());
			if (paramErasmusRespEmail != null && !paramErasmusRespEmail.trim().isEmpty()) {
				List<String> emailList = Arrays.asList(paramErasmusRespEmail.split(","));
				Email tmpEmail = new Email(subject, emailContent, emailList, null, file);
				this.emailSender.addEmail(tmpEmail);
			}
		} catch (Exception e) {
			FileLogger.log(Level.ERROR, "MessageService-insertMessage() - Error: " + CommonUtil.getExceptionMessage(e));
		}
	}
	
	public OperationResult sendNewMessageMail(String email, String placeTitle, String messageText) {
		OperationResult operationResult = new OperationResult();
		try {
			String htmlFilePath = this.getMailTemplatePath("NewMessage.html");	
			
			List<String> toList = new ArrayList<String>();
			for (String tmpEmail : email.split(",")) {
				toList.add(tmpEmail);
			}
			
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("#paramPlaceDetailTitle#", placeTitle);
			paramMap.put("#paramMessageText#", messageText);
			paramMap.put("#paramTimeStamp#", DateUtil.format(new Date(), DateUtil.READABLE_DATE_FORMAT));
			
			String subject = MAIL_SUBJECT_NEW_MESSAGE.replace("#paramPlaceDetailTitle#", placeTitle);
			
			this.sendMailFromTemplate(htmlFilePath, paramMap, toList, null, subject, null, false);
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
			this.sendMailFromTemplate(htmlFilePath, paramMap, toList, null, MAIL_SUBJECT_EMAIL_VERIFICATION, null, false);
			
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
			this.sendMailFromTemplate(htmlFilePath, paramMap, toList, null, MAIL_SUBJECT_RESET_PASSWORD, null, false);
			
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
			this.sendMailFromTemplate(htmlFilePath, paramMap, toList, null, MAIL_SUBJECT_NEW_PASSWORD, null, false);
			
			operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
		} catch (Exception e) {
			operationResult.setResultCode(EnmResultCode.EXCEPTION.getValue());
			operationResult.setResultDesc("MailService-sendNewPasswordMail() Error: " + CommonUtil.getExceptionMessage(e));
		}
		return operationResult;
	}
	
	public OperationResult sendBookingRequestMail(String guestEmail, String hostEmail, String placeTitle, String placeDetailUrl, 
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
			this.sendMailFromTemplate(guestHtmlFilePath, paramMap, guestToList, null, guestSubject, null, false);
			
			String hostSubject = MAIL_SUBJECT_HOST_BOOKING_REQUEST.replace("#paramPlaceDetailTitle#", placeTitle);
			String hostHtmlFilePath = this.getMailTemplatePath("HostBookingRequest.html");
			List<String> hostToList = new ArrayList<String>();
			hostToList.add(hostEmail);
			this.sendMailFromTemplate(hostHtmlFilePath, paramMap, hostToList, null, hostSubject, null, false);
			
			operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
		} catch (Exception e) {
			operationResult.setResultCode(EnmResultCode.EXCEPTION.getValue());
			operationResult.setResultDesc("MailService-sendNewMessageMail() Error: " + CommonUtil.getExceptionMessage(e));
		}
		return operationResult;
	}
	
	public OperationResult sendBookingRequestDeclineMail(String email, String guestFirstName, String placeTitle) {
		OperationResult operationResult = new OperationResult();
		try {
			String htmlFilePath = this.getMailTemplatePath("RequestDeclined.html");	
			
			List<String> toList = new ArrayList<String>();
			toList.add(email);
			
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("#paramGuestName#", guestFirstName);
			paramMap.put("#paramTimeStamp#", DateUtil.format(new Date(), DateUtil.READABLE_DATE_FORMAT));
			
			String subject = MAIL_SUBJECT_GUEST_RESERVATION_DECLINE.replace("#paramPlaceDetailTitle#", placeTitle);
			
			this.sendMailFromTemplate(htmlFilePath, paramMap, toList, null, subject, null, false);
			
			operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
		} catch (Exception e) {
			operationResult.setResultCode(EnmResultCode.EXCEPTION.getValue());
			operationResult.setResultDesc("MailService-sendBookingRequestDeclineMail() Error: " + CommonUtil.getExceptionMessage(e));
		}
		return operationResult;
	}
	
	@Override
	public OperationResult sendBookingRequestExpiredMail(String guestEmail, String guestFirstName, String hostEmail,
			String hostFirstName, String placeTitle) {
		OperationResult operationResult = new OperationResult();
		try {
			Date currentDate = new Date();
						
			Map<String, String> guestParamMap = new HashMap<String, String>();
			guestParamMap.put("#paramUserName#", guestFirstName);
			guestParamMap.put("#paramTimeStamp#", DateUtil.format(currentDate, DateUtil.READABLE_DATE_FORMAT));
			
			String guestSubject = MAIL_SUBJECT_GUEST_RESERVATION_EXPIRE.replace("#paramPlaceDetailTitle#", placeTitle);
			String guestHtmlFilePath = this.getMailTemplatePath("RequestExpiredForGuest.html");
			List<String> guestToList = new ArrayList<String>();
			guestToList.add(guestEmail);
			this.sendMailFromTemplate(guestHtmlFilePath, guestParamMap, guestToList, null, guestSubject, null, false);
			
			Map<String, String> hostParamMap = new HashMap<String, String>();
			guestParamMap.put("#paramUserName#", hostFirstName);
			guestParamMap.put("#paramTimeStamp#", DateUtil.format(currentDate, DateUtil.READABLE_DATE_FORMAT));
			
			String hostSubject = MAIL_SUBJECT_HOST_RESERVATION_EXPIRE.replace("#paramPlaceDetailTitle#", placeTitle);
			String hostHtmlFilePath = this.getMailTemplatePath("RequestExpiredForHost.html");
			List<String> hostToList = new ArrayList<String>();
			hostToList.add(hostEmail);
			this.sendMailFromTemplate(hostHtmlFilePath, hostParamMap, hostToList, null, hostSubject, null, false);
			
			operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
		} catch (Exception e) {
			operationResult.setResultCode(EnmResultCode.EXCEPTION.getValue());
			operationResult.setResultDesc("MailService-sendBookingRequestExpiredMail() Error: " + CommonUtil.getExceptionMessage(e));
		}
		return operationResult;
	}

	@Override
	public OperationResult sendBookingRequestRecallMail(String hostEmail, String hostFirstName, String placeTitle) {
		OperationResult operationResult = new OperationResult();
		try {
			String htmlFilePath = this.getMailTemplatePath("ReservationRecalled.html");	
			
			List<String> toList = new ArrayList<String>();
			toList.add(hostEmail);
			
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("#paramUserName#", hostFirstName);
			paramMap.put("#paramTimeStamp#", DateUtil.format(new Date(), DateUtil.READABLE_DATE_FORMAT));
			
			String subject = MAIL_SUBJECT_HOST_RESERVATION_RECALL.replace("#paramPlaceDetailTitle#", placeTitle);
			
			this.sendMailFromTemplate(htmlFilePath, paramMap, toList, null, subject, null, false);
			
			operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
		} catch (Exception e) {
			operationResult.setResultCode(EnmResultCode.EXCEPTION.getValue());
			operationResult.setResultDesc("MailService-sendBookingRequestRecallMail() Error: " + CommonUtil.getExceptionMessage(e));
		}
		return operationResult;
	}

	@Override
	public OperationResult sendBookingRequestAcceptMail(User guestUser, User hostUser, Place place,  
			String homeType, String placeType, Reservation reservation, 
			String reservationReceipt, String reservationCode) {
		OperationResult operationResult = new OperationResult();
		try {
			String guestHtmlFilePath = this.getMailTemplatePath("RequestAcceptedForGuest.html");	
			
			List<String> toList = new ArrayList<String>();
			toList.add(guestUser.getEmail());
			
			Location location = place.getLocation();
			
			String placeAddress = location.getStreet() + " " + location.getUserAddress() + " " + location.getPostalCode() + " " + location.getLocality() + "/" + location.getState() + "/" + location.getCountry() ;
			
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("#paramPlaceDetailUrl#", place.getUrl());
			paramMap.put("#paramPlaceImageUrl#", place.getCoverPhotoUrl());
			paramMap.put("#paramPlaceDetailHomeType#", homeType);
			paramMap.put("#paramPlaceDetailPlaceType#", placeType);
			paramMap.put("#paramPlaceDetailAddress#", placeAddress);
			paramMap.put("#paramPlaceCoordicates#", place.getLocation().getLatitude() + "," + place.getLocation().getLongitude());
			paramMap.put("#paramRequestMoveInDate#", DateUtil.format(reservation.getStartDate(), DateUtil.READABLE_DATE_FORMAT));
			paramMap.put("#paramRequestMoveOutDate#", DateUtil.format(reservation.getEndDate(), DateUtil.READABLE_DATE_FORMAT));
			paramMap.put("#paramPlaceDetailPrice#", reservation.getPlacePrice().add(reservation.getServiceFee()).toString() + " " + EnmCurrency.getCurrency(reservation.getCurrencyId()).getBlueSnapCode());
			//paramMap.put("#paramReservationReceipt#", null);
			//paramMap.put("#paramReservationCode#", null);
			paramMap.put("#paramHostDisplayProfileUrl#", hostUser.getUrl());
			paramMap.put("#paramHostProfilePictureUrl#", hostUser.getProfileImageUrl());
			paramMap.put("#paramHostName#", hostUser.getFullName());
			paramMap.put("#paramReservationMessageUrl#", reservation.getMessageThread().getUrl());
			paramMap.put("#paramHostPhoneNumber#", hostUser.getMsisdnCountryCode() + hostUser.getMsisdn());
			paramMap.put("#paramTimeStamp#", DateUtil.format(new Date(), DateUtil.READABLE_DATE_FORMAT));
			
			String guestSubject = MAIL_SUBJECT_GUEST_RESERVATION_ACCEPT.replace("#paramPlaceDetailTitle#", place.getTitle());
			
			this.sendMailFromTemplate(guestHtmlFilePath, paramMap, toList, null, guestSubject, null, false);
			
			
			String hostHtmlFilePath = this.getMailTemplatePath("RequestAcceptedForHost.html");	
			
			List<String> hostToList = new ArrayList<String>();
			hostToList .add(hostUser.getEmail());
			
			String hostSubject = MAIL_SUBJECT_HOST_RESERVATION_ACCEPT.replace("#paramPlaceDetailTitle#", place.getTitle());
			
			paramMap.put("#paramPlaceDetailHostPrice#", paramMap.put("#paramPlaceDetailPrice#", reservation.getPlacePrice().subtract(reservation.getCommissionFee()).toString() + " " + EnmCurrency.getCurrency(reservation.getCurrencyId()).getBlueSnapCode()));
			paramMap.put("#paramGuestName#", guestUser.getFullName());
			paramMap.put("#paramGuestDisplayProfileUrl#", guestUser.getUrl());
			paramMap.put("#paramGuestProfilePictureUrl#", guestUser.getProfileImageUrl());
			paramMap.put("#paramGuestPhoneNumber#", guestUser.getMsisdnCountryCode() + guestUser.getMsisdn());
			
			this.sendMailFromTemplate(hostHtmlFilePath, paramMap, hostToList, null, hostSubject, null, false);
			
			operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
		} catch (Exception e) {
			operationResult.setResultCode(EnmResultCode.EXCEPTION.getValue());
			operationResult.setResultDesc("MailService-sendBookingRequestAcceptMail() Error: " + CommonUtil.getExceptionMessage(e));
		}
		return operationResult;
	}

	@Override
	public OperationResult sendBookingRequestCancelMail(String guestEmail, String placeTitle, String placeDetailUrl,
			String placeImageUrl, String homeType, String placeType, Date checkinDate, Date checkoutDate, BigDecimal placePrice, Integer currencyId) {
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
			paramMap.put("#paramPlaceDetailPrice#", placePrice.toString() + " " + EnmCurrency.getCurrency(currencyId).getBlueSnapCode());
			
			String guestSubject = MAIL_SUBJECT_GUEST_RESERVATION_CANCEL.replace("#paramPlaceDetailTitle#", placeTitle);
			String guestHtmlFilePath = this.getMailTemplatePath("ReservationCancelledForGuest.html");
			List<String> guestToList = new ArrayList<String>();
			guestToList.add(guestEmail);
			this.sendMailFromTemplate(guestHtmlFilePath, paramMap, guestToList, null, guestSubject, null, false);
			
			operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
		} catch (Exception e) {
			operationResult.setResultCode(EnmResultCode.EXCEPTION.getValue());
			operationResult.setResultDesc("MailService-sendBookingRequestCancelMail() Error: " + CommonUtil.getExceptionMessage(e));
		}
		return operationResult;
	}
	
	
}
