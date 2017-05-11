package com.ucgen.letserasmus.library.mail.service;

import java.math.BigDecimal;
import java.util.Date;

import com.ucgen.common.operationresult.OperationResult;
import com.ucgen.letserasmus.library.place.model.Place;
import com.ucgen.letserasmus.library.reservation.model.Reservation;
import com.ucgen.letserasmus.library.user.model.User;

public interface IMailService {

	OperationResult sendNewMessageMail(String email, String placeTitle, String messageText);
	
	OperationResult sendEmailVerificationMail(User user, String email);
	
	OperationResult sendResetPasswordMail(User user);
	
	OperationResult sendNewPasswordMail(User user);
	
	OperationResult sendBookingRequestMail(String guestEmail, String hostEmail, String placeTitle, String placeDetailUrl, 
			String placeImageUrl, String homeType, String placeType, Date checkinDate, Date checkoutDate);

	OperationResult sendBookingRequestDeclineMail(String email, String guestFirstName, String placeTitle);
	
	OperationResult sendBookingRequestExpiredMail(String guestEmail, String guestFirstName, String hostEmail, String hostFirstName, String placeTitle);
	
	OperationResult sendBookingRequestRecallMail(String hostEmail, String hostFirstName, String placeTitle);
	/*
	OperationResult sendBokingRequestMail(String hostEmail, String placeTitle, String placeDetailUrl, 
			String placeImageUrl, String homeType, String placeType, String placeAddress, Date checkinDate, Date checkoutDate, String placePrice, 
			String reservationReceipt, String reservationCode, String hostProfileUrl, String hostName, String hostPhoneNumber, 
			String conversationUrl);
	*/
	
	OperationResult sendBookingRequestAcceptMail(User guestUser, User hostUser, Place place,  
			String homeType, String placeType, Reservation reservation, 
			String reservationReceipt, String reservationCode);

	OperationResult sendBookingRequestCancelMail(String guestEmail, String placeTitle, String placeDetailUrl, 
			String placeImageUrl, String homeType, String placeType, Date checkinDate, Date checkoutDate, BigDecimal placePrice, Integer currencyId);
	
}
