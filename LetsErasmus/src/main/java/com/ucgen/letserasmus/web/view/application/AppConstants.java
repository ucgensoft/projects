package com.ucgen.letserasmus.web.view.application;

public interface AppConstants {

	String USER_NOT_LOGGED_IN = "You are not logged in or session is expired. Please login first.";
	
	String MISSING_MANDATORY_PARAM = "Request could not be completed because of missing or invalid parameters";
	
	String UNAUTHORIZED_OPERATION = "You are not authorized for this operation!";
	
	String UNEXPECTED_ERROR = "Operation failed because of an unexpected error. Please try again later!";
	
	String USER_NOT_VERIFIED = "Please verify your email and msisdn using our 'Trust And Verification' page.";
	
	String LIST_OPERATION_FAIL = "List operation could not be completed. Please try again later!";
	
	String CREATE_OPERATION_FAIL = "Create operation could not be completed. Please try again later!";	
	
	String COMPLAINT_DOUBLE = "You already have an active complaint record, necessary action will be taken as soon as possible. Thanks for reporting.";
	
	String COMPLAINT_NOT_FOUND = "Complaint record not found. You may have previously removed it.";
	
	String OPERATION_FAIL = "Operation could not be completed. Please try again later!";
	
	String ITEM_NOT_FOUND = "Item is not found in system!";
	
	String WISH_LIST_DOUBLE = "You already have this listing in your wish list!";
	
	String WISH_LIST_MAX = "Maximum 20 items can be added in wish list!";
	
	String WISH_LIST_NOT_FOUND = "Favorite record not found. You may have previously removed it.";

	String MESSAGE_NOT_FOUND = "Message thread is not found!";
	
	String MESSAGE_NOT_SAVED = "Message could not be saved. Please try again later!";
	
	String MESSAGE_DOUBLE = "You have sent message to this user before. Please go to INBOX to send a new message!";
	
	String PHOTO_MIN_NUM = "At least one photo should be added!";
	
	String PLACE_LIST_NOT_FOUND = "Place listing could not be found!";

	String LIST_STATUS_FAIL = "Existing status of list is not suitable for this operation.";
	
	String PLACE_LIST_MANDATORY_PARAM = "PlaceId and Status parameters are mandatory!";
	
	String RESERV_REQUEST_OUTDATED = "Your reservation dates are outdated. Please send a new booking request!";
	
	String RESERV_STATUS_FAIL = "Reservation status is not suitable for this operation!";
	
	String RESERV_NOT_FOUND = "Reservation record could not be found!";

	String GET_RESERV_NOT_COMPLETED = "Reservation could not be completed. Please try again later!";
	
	String LIST_RESERV_NOT_COMPLETED = "List reservation could not be completed. Please try again later!";
	
	String LIST_TRIP_NOT_COMPLETED = "List trip could not be completed. Please try again later!";
	
	String REVIEW_NOT_SAVED = "Your review could not be saved. Please try again later!";
	
	String REVIEW_DOUBLE = "You have already reviewed this reservation!";
	
	String LOGIN_TYPE_NOT_FOUND = "Login type is not supported!";
	
	String LOGIN_NOT_COMPLETED = "Login could not be completed. Please try again later!";
	
	String ACCOUNT_REACTIVATE = "Your account is deactive. Do you want to reactivate ?";
	
	String LOGIN_FAIL = "Email or password is incorrect.";
	
	String LOGIN_MANDATORY_PARAM = "Email and password fields are mandatory.";
	
	String MAIL_IN_USE_FAIL = "This email is in use! If you forgot your password click 'Forgot password' link in login page.";
	
	String GOOGLE_IN_USE_FAIL = "This google account is used by another user!";

	String GOOGLE_MANDATORY_PARAM = "GoogleId and email parameters are mandatory!";
	
	String FACEBOOK_MANDATORY_PARAM = "facebookId and email parameters are mandatory!";
	
	String USER_UPDATE_FAIL = "User update could not be completed. Please try again later!";
	
	String VERIFICATION_CODE_FAIL = "Verification code is incorrect!";
	
	String USER_VERIFICATION_DOUBLE = "User is already verified!";

	String USER_FOUND_FAIL = "User not found!";
	
	String VERIFICATION_PARAM_FAIL = "Phone number and country code parameters are required!";

	String MSISDN_REQUIRED = "Phone number is required!";
	
	String MSISDN_DOUBLE_VERIFICATION = "Phone number is already verified!";
	
	String MSISDN_VERIFICATION_FAIL = "System is not in phone number verification state!";
	
	String GOOGLE_BINDING_FAIL = "Google is the only account binded to your user record!";
	
	String LOGIN_WITH_G_OR_FB = "User record does not have a password. Please login with your Google or Facebook account!";
	
	String LOGIN_WITH_GOOGLE = "User record does not have a password. Please login with your Google account!";
	
	String LOGIN_WITH_FB = "User record does not have a password. Please login with your Facebook account!";
	
	String MAIL_NOT_VERIFIED = "Your email is not verified. Please click the verification link which is sent to your email first!";

	String PASS_RESET_FAIL = "Reset password code is incorrect!";
	
	String PAYMENT_METHOD_DOUBLE = "You have already defined this payment method!";
	
	String INVALID_OPERATION_TOKEN = "Invalid operation token!";
	
	String USER_ALREADY_HAVE_PAYOUT_METHOD = "User payout method already defined!";
	
	String USER_DO_NOT_HAVE_PAYOUT_METHOD = "User does not have a payout method!";
	
	String BLUESNAP_CREATE_VENDOR_FAILES = "Bluesnap vendor creation failed!";
	
	String CREATE_PAYMENT_ACCOUNT_FAILES = "Payment account creation failed!";
	
	String COMMUNITY_CREATE_TOPIC_SUCCESS = "Your topic is created successfully!";
	
	String COMMUNITY_CREATE_TOPIC_FAIL = "Topic create operation failed!";
	
	String COMMUNITY_CREATE_TOPIC_NOT_FOUND = "Topic record not found!";
	
	String COMMUNITY_CREATE_TOPIC_ = "Topic record not found!";
	
}

