package com.ucgen.letserasmus.web.view.application;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.ucgen.common.model.Size;
import com.ucgen.common.util.FileUtil;
import com.ucgen.letserasmus.library.common.enumeration.EnmBoolStatus;
import com.ucgen.letserasmus.library.common.enumeration.EnmSize;
import com.ucgen.letserasmus.library.log.enumeration.EnmOperation;
import com.ucgen.letserasmus.library.parameter.enumeration.EnmParameter;
import com.ucgen.letserasmus.library.parameter.service.IParameterService;
import com.ucgen.letserasmus.library.user.model.User;
import com.ucgen.letserasmus.web.view.BaseController;

@Service
@Scope("singleton")
public class WebApplication extends BaseController {
	
	public String facebookAppId;
	public String googleAppId;
	public String googleApiKey;
	
	public String localAppPath;

	private String urlPrefix;
	
	//private String emailVerificationUrl;
	//private String resetPasswordUrl;
	
	private String rootUserPhotoPath;
	private String rootPlacePhotoPath;
	
	private String userPhotoUrlTemplate;
	private String placePhotoUrlTemplate;
	private String rootPlaceImageUrl;
	private String rootUserImageUrl;
	private String defaultUserPhotoUrl;
	
	private String userPhotoNameTemplate;
	private String placePhotoNameTemplate;
	
	private String placePhotoPath;
	private String userPhotoPath;
	
	private Size smallUserPhotoSize;
	private Size mediumUserPhotoSize;
	
	private Size smallPlacePhotoSize;
	private Size mediumPlacePhotoSize;
	
	private Integer staticFileVersion;
		
	private IParameterService parameterService;
	
	@Autowired
	public void setParameterService(IParameterService parameterService) {
		this.parameterService = parameterService;
	}

	@PostConstruct
	public void initialize() {
		try {
			localAppPath = this.parameterService.getParameterValue(EnmParameter.LOCAL_APP_ROOT_PATH.getId());
			
			facebookAppId = this.parameterService.getParameterValue(EnmParameter.FACEBOOK_APP_ID.getId());
			googleAppId = this.parameterService.getParameterValue(EnmParameter.GOOGLE_APP_ID.getId());
			googleApiKey = this.parameterService.getParameterValue(EnmParameter.GOOGLE_API_KEY.getId());
			
			urlPrefix = this.parameterService.getParameterValue(EnmParameter.LETSERASMUS_URL_PREFIX.getId());
			staticFileVersion = Integer.valueOf(this.parameterService.getParameterValue(EnmParameter.STATIC_FILE_VERSION.getId()));
			
			//emailVerificationUrl = urlPrefix + "//PAGES/Main.html?user=#paramUserId#&code=#paramCode#&op=10";
			//resetPasswordUrl = urlPrefix + "//PAGES/Main.html?user=#paramUserId#&code=#paramCode#&op=12";
			
			String placeImageSubUrl = this.parameterService.getParameterValue(EnmParameter.PLACE_IMAGE_SUB_URL.getId());
			String userImageSubUrl = this.parameterService.getParameterValue(EnmParameter.USER_IMAGE_SUB_URL.getId());
			
			rootPlaceImageUrl = AppUtil.concatPath(urlPrefix, placeImageSubUrl);
			rootUserImageUrl = AppUtil.concatPath(urlPrefix, userImageSubUrl);
			
			rootPlacePhotoPath = localAppPath + placeImageSubUrl.replace("/", File.separator);
			rootUserPhotoPath = localAppPath + userImageSubUrl.replace("/", File.separator);
			
			String imageSufix = this.parameterService.getParameterValue(EnmParameter.IMAGE_SUFFIX.getId());
			
			userPhotoNameTemplate = this.parameterService.getParameterValue(EnmParameter.USER_PHOTO_NAME_TEMPLATE.getId()) + "." + imageSufix; 
			
			placePhotoNameTemplate = this.parameterService.getParameterValue(EnmParameter.PLACE_PHOTO_NAME_TEMPLATE.getId()) + "." + imageSufix;
			
			String defaultUserImageName = this.parameterService.getParameterValue(EnmParameter.DEFAULT_USER_IMAGE_NAME_TEMPLATE.getId()) + "." + imageSufix;
			
			defaultUserPhotoUrl = AppUtil.concatPath(rootUserImageUrl, defaultUserImageName);
			userPhotoUrlTemplate = AppUtil.concatPath(rootUserImageUrl, "#userId#", userPhotoNameTemplate);
			placePhotoUrlTemplate = AppUtil.concatPath(rootPlaceImageUrl, "#placeId#", placePhotoNameTemplate);
			
			placePhotoPath = FileUtil.concatPath(rootPlacePhotoPath, "#placeId#", placePhotoNameTemplate);
			userPhotoPath = FileUtil.concatPath(rootUserPhotoPath, "#userId#", userPhotoNameTemplate);
			
			String smallUserImageDimensions[] = this.parameterService.getParameterValue(EnmParameter.SMALL_USER_PHOTO_SIZE.getId()).split(":");
			String mediumUserImageDimensions[] = this.parameterService.getParameterValue(EnmParameter.MEDIUM_USER_PHOTO_SIZE.getId()).split(":");
			
			smallUserPhotoSize = new Size(new Float(smallUserImageDimensions[0]), new Float(smallUserImageDimensions[1]));
			mediumUserPhotoSize = new Size(new Float(mediumUserImageDimensions[0]), new Float(mediumUserImageDimensions[1]));
			
			String smallPlaceImageDimensions[] = this.parameterService.getParameterValue(EnmParameter.SMALL_PLACE_PHOTO_SIZE.getId()).split(":");
			String mediumPlaceImageDimensions[] = this.parameterService.getParameterValue(EnmParameter.MEDIUM_PLACE_PHOTO_SIZE.getId()).split(":");
			
			smallPlacePhotoSize = new Size(new Float(smallPlaceImageDimensions[0]), new Float(smallPlaceImageDimensions[1]));
			mediumPlacePhotoSize = new Size(new Float(mediumPlaceImageDimensions[0]), new Float(mediumPlaceImageDimensions[1]));
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public String getLocalAppPath() {
		return localAppPath;
	}

	public String getFacebookAppId() {
		return facebookAppId;
	}
		
	public String getGoogleApiKey() {
		return googleApiKey;
	}

	public String getUrlPrefix() {
		return urlPrefix;
	}
	
	public String getEncodedUrlPrefix() throws UnsupportedEncodingException {
		return URLEncoder.encode(this.getUrlPrefix(), "UTF-8");
	}
	
	public String getRootPlaceImageUrl() {
		return rootPlaceImageUrl;
	}

	/*
	public String getEmailVerificationUrl() {
		return emailVerificationUrl;
	}

	public void setEmailVerificationUrl(String emailVerificationUrl) {
		this.emailVerificationUrl = emailVerificationUrl;
	}
	
	public String getResetPasswordUrl() {
		return resetPasswordUrl;
	}
	*/

	public String getRootUserPhotoPath() {
		return this.rootUserPhotoPath;
	}
	
	public String getRootPlacePhotoPath() {
		return this.rootPlacePhotoPath;
	}

	public String getUserPhotoUrlTemplate() {
		return userPhotoUrlTemplate;
	}

	public String getDefaultUserPhotoUrl() {
		return defaultUserPhotoUrl;
	}

	public String getRootUserImageUrl() {
		return rootUserImageUrl;
	}

	public String getUserPhotoNameTemplate() {
		return userPhotoNameTemplate;
	}

	public String getPlacePhotoNameTemplate() {
		return placePhotoNameTemplate;
	}

	public Size getSmallUserPhotoSize() {
		return smallUserPhotoSize;
	}

	public Size getMediumUserPhotoSize() {
		return mediumUserPhotoSize;
	}

	public Size getSmallPlacePhotoSize() {
		return smallPlacePhotoSize;
	}

	public Size getMediumPlacePhotoSize() {
		return mediumPlacePhotoSize;
	}

	public String getPlacePhotoUrlTemplate() {
		return placePhotoUrlTemplate;
	}

	public Integer getStaticFileVersion() {
		return staticFileVersion;
	}

	public WebApplication() {
		
	}
	
	public String getNgController() {
		String requestUrl = super.getRequest().getRequestURL().toString();
		requestUrl = requestUrl.trim().replace(this.urlPrefix, "");
		if (requestUrl.isEmpty() || requestUrl.equals("/")) {
			return "mainCtrl";
		} else {
			requestUrl = requestUrl.toUpperCase();
			if (requestUrl.contains("PAGES/MAIN.HTML")) {
				return "mainCtrl";
			} else if (requestUrl.contains("PAGES/SEARCHRESULT.HTML")) {
				return "searchResultCtrl";
			} else if (requestUrl.contains("PAGES/PLACE.HTML")) {
				return "placeCtrl";
			} else if (requestUrl.contains("PAGES/PLACEDETAIL.HTML")) {
				return "placeDetailCtrl";
			} else if (requestUrl.contains("PAGES/DASHBOARD/EDITUSER.HTML")) {
				return "editUserCtrl";
			} else if (requestUrl.contains("PAGES/DASHBOARD/DISPLAYUSER.HTML")) {
				return "displayUserCtrl";
			} else if (requestUrl.contains("PAGES/DASHBOARD/LISTINGS.HTML")) {
				return "listingsCtrl";
			} else if (requestUrl.contains("PAGES/DASHBOARD/TRUSTANDVERIFICATION.HTML")) {
				return "trustAndverificationCtrl";
			} else if (requestUrl.contains("PAGES/VERIFICATION.HTML")) {
				return "verificationCtrl";
			} else if (requestUrl.contains("PAGES/PAYMENT.HTML")) {
				return "paymentCtrl";
			} else if (requestUrl.contains("PAGES/DASHBOARD/MESSAGELIST.HTML")) {
				return "messageListCtrl";
			} else if (requestUrl.contains("PAGES/DASHBOARD/CONVERSATION.HTML")) {
				return "conversationCtrl";
			} else if (requestUrl.contains("PAGES/DASHBOARD/RESERVATIONLIST.HTML")) {
				return "reservationListCtrl";
			} else if (requestUrl.contains("PAGES/DASHBOARD/TRIPLIST.HTML")) {
				return "tripListCtrl";
			} else if (requestUrl.contains("PAGES/DASHBOARD/WISHLIST.HTML")) {
				return "wishListCtrl";
			} else if (requestUrl.contains("PAGES/HELP/HELP.HTML")) {
				return "helpCtrl";
			} else if (requestUrl.contains("PAGES/DASHBOARD/PAYMENTMETHODS.HTML")) {
				return "paymentMethodCtrl";
			} else if (requestUrl.contains("PAGES/DASHBOARD/PAYOUTMETHODS.HTML")) {
				return "payoutMethodCtrl";
			} else {
				return "";
			}	
		}
	}
	
	public User getUser() {
		Object objUser = super.getSessionAttribute(EnmSession.USER.getId());
		if (objUser != null) {
			return (User) objUser;
		} else {
			return null;
		}
	}
	
	public String getGoogleAppId() {
		return this.googleAppId;
	}
		
	public String getLoginType() {
		String loginType = "";
		User user = (User) super.getSessionAttribute(EnmSession.USER.getId()); 
		if (user != null && user.getLoginType() != null) {
			loginType = user.getLoginType().toString();
		}
		return loginType;
	}
	
	public String getFacebookTokenId() {
		String facebookTokenId = "";
		User user = (User) super.getSessionAttribute(EnmSession.USER.getId()); 
		if (user != null && user.getFacebookTokenId() != null) {
			facebookTokenId = user.getFacebookTokenId();
		}
		return facebookTokenId;
	}
	
	public String setActiveOperation(int operationId) {
		if (super.getSession() != null) {
			super.getSession().removeAttribute(EnmSession.ACTIVE_OPERATION.getId());;
			super.getSession().setAttribute(EnmSession.ACTIVE_OPERATION.getId(), EnmOperation.getOperation(operationId));;
		}
		return "";
	}

	public String getUserPhotoName(Long userId, Long photoId, EnmSize size) {
		String photoName = this.userPhotoNameTemplate.replace("#userId#", userId.toString());
		photoName = photoName.replace("#photoId#", photoId.toString());
		photoName = photoName.replace("#size#", size.getValue());
		return photoName;
	}
	
	public String getPlacePhotoName(Long placeId, Long photoId, EnmSize size) {
		String photoName = this.placePhotoNameTemplate.replace("#placeId#", placeId.toString());
		photoName = photoName.replace("#photoId#", photoId.toString());
		photoName = photoName.replace("#size#", size.getValue());
		return photoName;
	}
	
	public String getActiveUserPhotoUrl(String size) {
		String photoUrl = null;
		User user = this.getUser();
		if (user != null) {
			if (user.getProfilePhotoId() != null) {
				photoUrl = this.getUserPhotoUrl(user.getId(), user.getProfilePhotoId(), size);
			} else if (user.getProfileImageUrl() != null) {
				photoUrl = user.getProfileImageUrl();
			}
		}
		return photoUrl;
	}
	
	public String getUserPhotoUrl(Long userId, Long photoId, String size) {
		String photoUrl = null;
		if (photoId != null && photoId > 0) {
			photoUrl = this.userPhotoUrlTemplate.replaceAll("#userId#", userId.toString());
			photoUrl = photoUrl.replaceAll("#photoId#", photoId.toString());
		} else {
			photoUrl = this.defaultUserPhotoUrl;
		}
		photoUrl = photoUrl.replace("#size#", size);
		return photoUrl;
	}
	
	public String getUserPhotoPath(Long userId, Long photoId, String size) {
		String photoPath = this.userPhotoPath.replaceAll("#userId#", userId.toString());
		photoPath = photoPath.replaceAll("#photoId#", photoId.toString());
		photoPath = photoPath.replaceAll("#size#", EnmSize.getSize(size).getValue());
		return photoPath;
	}
	
	public String getPlacePhotoUrl(Long placeId, Long photoId, String size) {
		String photoPath = this.placePhotoUrlTemplate.replaceAll("#placeId#", placeId.toString());
		photoPath = photoPath.replaceAll("#photoId#", photoId.toString());
		photoPath = photoPath.replaceAll("#size#", EnmSize.getSize(size).getValue());
		return photoPath;
	}
	
	public String getPlacePhotoPath(Long placeId, Long photoId, String size) {
		String photoPath = this.placePhotoPath.replaceAll("#placeId#", placeId.toString());
		photoPath = photoPath.replaceAll("#photoId#", photoId.toString());
		photoPath = photoPath.replaceAll("#size#", EnmSize.getSize(size).getValue());
		return photoPath;
	}
	
	public boolean isFooterVisible() {
		String requestUrl = super.getRequest().getRequestURL().toString();
		requestUrl = requestUrl.trim().replace(this.urlPrefix, "").toUpperCase();
		if (requestUrl.contains("PAGES/PLACE.HTML") 
				|| requestUrl.contains("PAGES/SEARCHRESULT.HTML")) {
			return false;
		} else {
			return true;
		}		
	}
	
	public String getParameterValue(String parameterName) {
		try {
			EnmParameter parameter = EnmParameter.getParameter(parameterName);
			if (parameter != null) {
				return this.parameterService.getParameterValue(parameter.getId());
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	public Boolean isActiveUserVerified() {
		User user = this.getUser();
		return this.isUserVerified(user);
	}
	
	public Boolean isUserVerified(User user) {
		return (user != null 
				&& user.getEmailVerified() != null && user.getEmailVerified().equals(EnmBoolStatus.YES.getId())
				&& user.getMsisdnVerified() != null && user.getMsisdnVerified().equals(EnmBoolStatus.YES.getId()));
	}
	
}
