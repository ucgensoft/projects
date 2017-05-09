package com.ucgen.letserasmus.library.mail.service;

import java.util.Date;

import com.ucgen.common.operationresult.OperationResult;
import com.ucgen.letserasmus.library.user.model.User;

public interface IMailService {

	OperationResult sendNewMessageMail(String email, String placeTitle, String messageText);
	
	OperationResult sendEmailVerificationMail(User user, String email);
	
	OperationResult sendResetPasswordMail(User user);
	
	OperationResult sendNewPasswordMail(User user);
	
	OperationResult sendBokingRequestMail(String guestEmail, String hostEmail, String placeTitle, String placeDetailUrl, 
			String placeImageUrl, String homeType, String placeType, Date checkinDate, Date checkoutDate);
	
}
