package com.ucgen.letserasmus.web.api.user;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.ucgen.common.exception.operation.OperationResultException;
import com.ucgen.common.operationresult.EnmResultCode;
import com.ucgen.common.operationresult.ListOperationResult;
import com.ucgen.common.operationresult.OperationResult;
import com.ucgen.common.operationresult.ValueOperationResult;
import com.ucgen.common.util.CommonUtil;
import com.ucgen.common.util.FileUtil;
import com.ucgen.common.util.ImageUtil;
import com.ucgen.common.util.MailUtil;
import com.ucgen.common.util.SecurityUtil;
import com.ucgen.letserasmus.library.common.enumeration.EnmBoolStatus;
import com.ucgen.letserasmus.library.common.enumeration.EnmEntityType;
import com.ucgen.letserasmus.library.common.enumeration.EnmErrorCode;
import com.ucgen.letserasmus.library.common.enumeration.EnmGender;
import com.ucgen.letserasmus.library.common.enumeration.EnmSize;
import com.ucgen.letserasmus.library.common.model.BaseModel;
import com.ucgen.letserasmus.library.favorite.model.Favorite;
import com.ucgen.letserasmus.library.favorite.service.IFavoriteService;
import com.ucgen.letserasmus.library.file.enumeration.EnmFileType;
import com.ucgen.letserasmus.library.file.model.FileModel;
import com.ucgen.letserasmus.library.file.model.Photo;
import com.ucgen.letserasmus.library.place.model.Place;
import com.ucgen.letserasmus.library.place.service.IPlaceService;
import com.ucgen.letserasmus.library.user.enumeration.EnmLoginType;
import com.ucgen.letserasmus.library.user.enumeration.EnmUserStatus;
import com.ucgen.letserasmus.library.user.model.User;
import com.ucgen.letserasmus.library.user.service.IUserService;
import com.ucgen.letserasmus.web.api.BaseApiController;
import com.ucgen.letserasmus.web.view.application.AppConstants;
import com.ucgen.letserasmus.web.view.application.EnmOperation;
import com.ucgen.letserasmus.web.view.application.EnmSession;
import com.ucgen.letserasmus.web.view.application.WebApplication;

@RestController
public class ApiUserController extends BaseApiController {

	private MailUtil mailUtil;
	private IUserService userService;
	private IPlaceService placeService;
	private WebApplication webApplication;
	private IFavoriteService favoriteService;
	
	@Autowired
	public void setFavoriteService(IFavoriteService favoriteService) {
		this.favoriteService = favoriteService;
	}

	@Autowired
	public void setWebApplication(WebApplication webApplication) {
		this.webApplication = webApplication;
	}
	
	@Autowired
	public void setUserService(IUserService userService) {
		this.userService = userService;
	}
	
	@Autowired
	public void setPlaceService(IPlaceService placeService) {
		this.placeService = placeService;
	}
	
	@Autowired
	public void setMailUtil(MailUtil mailUtil) {
		this.mailUtil = mailUtil;
	}

	@RequestMapping(value = "/api/user/signup", method = RequestMethod.POST)
    public ResponseEntity<OperationResult> signup(@RequestBody User user, HttpSession session) {
		HttpStatus httpStatus = null;
		OperationResult operationResult = new OperationResult();
		
		try {
			EnmLoginType loginType = EnmLoginType.getLoginType(user.getLoginType());
			if (loginType != null) {
				if (loginType == EnmLoginType.LOCAL_ACCOUNT) {
					operationResult = this.signupWithLocalAccount(user, session);	
				} else if (loginType == EnmLoginType.GOOGLE) {
					operationResult = this.signupWithGoogleAccount(user, session);
				} else if (loginType == EnmLoginType.FACEBOOK) {
					operationResult = this.signupWithFacebookAccount(user, session);
				}
			} else {
				operationResult.setResultCode(EnmResultCode.WARNING.getValue());
				operationResult.setResultDesc("Login type is not supported!");
			}
			httpStatus = HttpStatus.OK;			
		} catch (Exception e) {
			operationResult.setResultCode(EnmResultCode.EXCEPTION.getValue());
			operationResult.setResultDesc("Create operation could not be completed. Please try again later!");
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<OperationResult>(operationResult, httpStatus);
    }
	
	@RequestMapping(value = "/api/user/login", method = RequestMethod.POST)
    public ResponseEntity<OperationResult> login(@RequestBody User user, HttpSession session) {
		HttpStatus httpStatus = null;
		OperationResult operationResult = new OperationResult();
		
		try {
			EnmLoginType loginType = EnmLoginType.getLoginType(user.getLoginType());
			if (loginType != null && loginType == EnmLoginType.LOCAL_ACCOUNT) {
				operationResult = this.loginWithLocalAccount(user, session);
			} else {
				operationResult.setResultCode(EnmResultCode.WARNING.getValue());
				operationResult.setResultDesc("Login type is not supported!");
			}
			httpStatus = HttpStatus.OK;			
		} catch (Exception e) {
			operationResult.setResultCode(EnmResultCode.EXCEPTION.getValue());
			operationResult.setResultDesc("Login operation could not be completed. Please try again later!");
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<OperationResult>(operationResult, httpStatus);
    }
	
	@RequestMapping(value = "/api/user/logout", method = RequestMethod.POST)
    public ResponseEntity<OperationResult> logout(HttpSession session) {
		HttpStatus httpStatus = null;
		OperationResult operationResult = new OperationResult();
		
		try {
			
			session.invalidate();
			operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
			
			httpStatus = HttpStatus.OK;			
		} catch (Exception e) {
			operationResult.setResultCode(EnmResultCode.EXCEPTION.getValue());
			operationResult.setResultDesc("Create operation could not be completed. Please try again later!");
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<OperationResult>(operationResult, httpStatus);
    }
	
    public OperationResult loginWithLocalAccount(User user, HttpSession session) {
		OperationResult operationResult = new OperationResult();
		
		try {
			if (user != null && user.getEmail() != null && user.getPassword() != null) {
				User dbUser = new User();
				dbUser.setEmail(user.getEmail());
				dbUser.setPassword(user.getPassword());
				dbUser = this.userService.getUser(dbUser);
				
				if(dbUser != null) {
					operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
					this.processLogin(session, dbUser, EnmLoginType.LOCAL_ACCOUNT);
				} else {
					operationResult.setResultCode(EnmResultCode.WARNING.getValue());
					operationResult.setResultDesc("Email or password is incorrect.");
				}
			} else {
				operationResult.setResultCode(EnmResultCode.WARNING.getValue());
				operationResult.setResultDesc("Email and password fields are mandatory.");
			}	
		} catch (Exception e) {
			operationResult.setResultCode(EnmResultCode.EXCEPTION.getValue());
			operationResult.setResultDesc("Create operation could not be completed. Please try again later!");
		}
		return operationResult;
    }
	
    public OperationResult signupWithLocalAccount(User uiUser, HttpSession session) {
		OperationResult operationResult = new OperationResult();
		
		try {
			if (uiUser != null && uiUser.getEmail() != null 
					&& uiUser.getEmail().trim().length() > 0
					&& uiUser.getPassword() != null
					&& uiUser.getPassword().trim().length() > 0) {
				User dbUser = new User();
				dbUser.setEmail(uiUser.getEmail());
				dbUser.setPassword(uiUser.getPassword());
				
				User registeredUser = this.userService.getUser(dbUser);
				if(registeredUser == null) {
					dbUser.setPassword(null);
					dbUser.setGoogleEmail(uiUser.getEmail());
					dbUser.setFacebookEmail(uiUser.getEmail());
					
					registeredUser = this.userService.getUserForLogin(dbUser);
					if(registeredUser == null) {
						String verificationCode = SecurityUtil.generateUUID().replace("-", "");
						dbUser = new User();
						
						dbUser.setEmail(uiUser.getEmail());
						dbUser.setPassword(uiUser.getPassword());
						dbUser.setFirstName(uiUser.getFirstName());
						dbUser.setLastName(uiUser.getLastName());
						
						dbUser.setEmailVerified(EnmBoolStatus.NO.getId());
						dbUser.setMsisdnVerified(EnmBoolStatus.NO.getId());
						dbUser.setStatus(EnmUserStatus.ACTIVE.getValue());
						dbUser.setUserActivationKeyEmail(verificationCode);
						
						OperationResult createUserResult = this.userService.insertUser(dbUser);
						
						if (OperationResult.isResultSucces(createUserResult)) {
							operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
							this.processLogin(session, dbUser, EnmLoginType.LOCAL_ACCOUNT);
							this.sendWelcomeMail(dbUser, EnmLoginType.LOCAL_ACCOUNT);
						} else {
							operationResult = createUserResult;
						}
					} else {
						String message = null;
						if (registeredUser.getEmail() != null) {
							message = "Email address or password is incorrect!";
						} else {
							if (registeredUser.getGoogleEmail() != null) {
								message = "This email address is in use and associated with google account. You can use 'Login With Google' option to sign in.";
							} else {
								message = "This email address is in use and associated with facebook account. You can use 'Login With Facebook' option to sign in.";
							}
						}
						operationResult.setResultCode(EnmResultCode.WARNING.getValue());
						operationResult.setResultDesc(message);
					}
				} else {
					operationResult.setResultCode(EnmResultCode.WARNING.getValue());
					operationResult.setResultDesc("This email is in use! If you forgot your password click 'Forgot password' link in login page.");
				}
			} else {
				operationResult.setResultCode(EnmResultCode.WARNING.getValue());
				operationResult.setResultDesc("Email and password parameters are mandatory!");
			}
		} catch (Exception e) {
			operationResult.setResultCode(EnmResultCode.EXCEPTION.getValue());
			operationResult.setResultDesc("Create operation could not be completed. Please try again later!");
		}
		return operationResult;
    }
	
    public OperationResult signupWithGoogleAccount(User uiUser, HttpSession session) {
		OperationResult operationResult = new OperationResult();
		
		try {
			if (uiUser.getGoogleId() != null && uiUser.getGoogleEmail() != null) {
				User dbUser = new User();
				dbUser.setGoogleEmail(uiUser.getGoogleEmail());
				dbUser.setGoogleId(uiUser.getGoogleId());
				
				User registeredUser = this.userService.getUser(dbUser);
				
				if(registeredUser == null) {
					dbUser.setGoogleId(null);
					dbUser.setEmail(uiUser.getGoogleEmail());
					dbUser.setGoogleEmail(uiUser.getGoogleEmail());
					dbUser.setFacebookEmail(uiUser.getGoogleEmail());
					
					registeredUser = this.userService.getUserForLogin(dbUser);
					if(registeredUser == null) {
						String verificationCode = SecurityUtil.generateUUID().replace("-", "");
						
						dbUser = new User();

						dbUser.setEmail(uiUser.getGoogleEmail());
						dbUser.setGoogleId(uiUser.getGoogleId());
						dbUser.setGoogleEmail(uiUser.getGoogleEmail());
						dbUser.setFirstName(uiUser.getFirstName());
						dbUser.setLastName(uiUser.getLastName());
						if (uiUser.getGender() != null) {
							EnmGender gender = EnmGender.getGender(uiUser.getGender());
							if (gender != null) {
								dbUser.setGender(uiUser.getGender());
							}
						}
						
						dbUser.setProfileImageUrl(uiUser.getProfileImageUrl());
						dbUser.setLoginType(uiUser.getLoginType());
						
						dbUser.setEmailVerified(EnmBoolStatus.NO.getId());
						dbUser.setMsisdnVerified(EnmBoolStatus.NO.getId());
						dbUser.setStatus(EnmUserStatus.ACTIVE.getValue());
						
						dbUser.setUserActivationKeyEmail(verificationCode);
						
						OperationResult createUserResult = this.userService.insertUser(dbUser);
						
						if (OperationResult.isResultSucces(createUserResult)) {
							operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
							this.processLogin(session, dbUser, EnmLoginType.GOOGLE);
							this.sendWelcomeMail(dbUser, EnmLoginType.GOOGLE);
						} else {
							operationResult = createUserResult;
						}
					} else {
						String message = null;
						if (registeredUser.getGoogleEmail() != null) {
							message = "Email address or google id is incorrect!";
						} else {
							if (registeredUser.getEmail() != null) {
								message = "This email address is in use and associated with a local account. You can login with email and password.";
							} else {
								message = "This email address is in use and associated with facebook account. You can use 'Login With Facebook' option to sign in.";
							}
						}
						operationResult.setResultCode(EnmResultCode.WARNING.getValue());
						operationResult.setResultDesc(message);
					}
				} else {
					registeredUser.setLoginType(EnmLoginType.GOOGLE.getId());
					registeredUser.setProfileImageUrl(uiUser.getProfileImageUrl());
					this.processLogin(session, registeredUser, EnmLoginType.GOOGLE);
					
					operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
				}	
			} else {
				operationResult.setResultCode(EnmResultCode.ERROR.getValue());
				operationResult.setResultDesc("googleId and email parameters are mandatory!");
			}
		} catch (Exception e) {
			operationResult.setResultCode(EnmResultCode.EXCEPTION.getValue());
			operationResult.setResultDesc("Create operation could not be completed. Please try again later!");
		}
		return operationResult;
    }
    
    public OperationResult signupWithFacebookAccount(User uiUser, HttpSession session) {
		OperationResult operationResult = new OperationResult();
		
		try {
			if (uiUser.getFacebookId() != null && uiUser.getFacebookEmail() != null) {
				User dbUser = new User();
				dbUser.setFacebookEmail(uiUser.getFacebookEmail());
				dbUser.setFacebookId(uiUser.getFacebookId());
				
				User registeredUser = this.userService.getUser(dbUser);
				
				if(registeredUser == null) {
					dbUser.setFacebookId(null);
					dbUser.setEmail(uiUser.getFacebookEmail());
					dbUser.setGoogleEmail(uiUser.getFacebookEmail());
					dbUser.setFacebookEmail(uiUser.getFacebookEmail());
					
					registeredUser = this.userService.getUserForLogin(dbUser);
					
					if(registeredUser == null) {
						String verificationCode = SecurityUtil.generateUUID().replace("-", "");
						dbUser = new User();
						
						dbUser.setEmail(uiUser.getFacebookEmail());
						dbUser.setFacebookId(uiUser.getFacebookId());
						dbUser.setFacebookTokenId(uiUser.getFacebookTokenId());
						dbUser.setEmail(uiUser.getFacebookEmail());
						dbUser.setFirstName(uiUser.getFirstName());
						dbUser.setLastName(uiUser.getLastName());
						if (uiUser.getGender() != null) {
							EnmGender gender = EnmGender.getGender(uiUser.getGender());
							if (gender != null) {
								dbUser.setGender(uiUser.getGender());
							}
						}
						
						dbUser.setProfileImageUrl(uiUser.getProfileImageUrl());
						dbUser.setLoginType(EnmLoginType.FACEBOOK.getId());
						
						dbUser.setEmailVerified(EnmBoolStatus.NO.getId());
						dbUser.setMsisdnVerified(EnmBoolStatus.NO.getId());
						dbUser.setStatus(EnmUserStatus.ACTIVE.getValue());
						
						dbUser.setUserActivationKeyEmail(verificationCode);
						
						OperationResult createUserResult = this.userService.insertUser(dbUser);
						
						if (OperationResult.isResultSucces(createUserResult)) {
							operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
							this.processLogin(session, dbUser, EnmLoginType.FACEBOOK);
							this.sendWelcomeMail(dbUser, EnmLoginType.FACEBOOK);
						} else {
							operationResult = createUserResult;
						}
					} else {
						String message = null;
						if (registeredUser.getFacebookEmail() != null) {
							message = "Email address or facebook id is incorrect!";
						} else {
							if (registeredUser.getEmail() != null) {
								message = "This email address is in use and associated with a local account. You can login with email and password.";
							} else {
								message = "This email address is in use and associated with google account. You can use 'Login With Google' option to sign in.";
							}
						}
						operationResult.setResultCode(EnmResultCode.WARNING.getValue());
						operationResult.setResultDesc(message);
					}
				} else {
					registeredUser.setLoginType(EnmLoginType.FACEBOOK.getId());
					registeredUser.setProfileImageUrl(uiUser.getProfileImageUrl());
					this.processLogin(session, registeredUser, EnmLoginType.FACEBOOK);
					
					operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
				}
			} else {
				operationResult.setResultCode(EnmResultCode.ERROR.getValue());
				operationResult.setResultDesc("facebookId and email parameters are mandatory!");
			}
		} catch (Exception e) {
			operationResult.setResultCode(EnmResultCode.EXCEPTION.getValue());
			operationResult.setResultDesc("Create operation could not be completed. Please try again later!");
		}
		return operationResult;
    }
	
    @RequestMapping(value = "/api/user/update", method = RequestMethod.POST)
    public ResponseEntity<OperationResult> updateUser(@RequestBody User uiUser, HttpSession session) throws JsonParseException, JsonMappingException, IOException, ParseException {
		HttpStatus httpStatus = null;
		OperationResult operationResult = new OperationResult();
		try {			
			Object activeOperation = super.getSession().getAttribute(EnmSession.ACTIVE_OPERATION.getId());
			if (activeOperation != null && activeOperation.equals(EnmOperation.UPDATE_USER)) {
				User sessionUser = super.getSessionUser(session);
				if (sessionUser != null) {
					boolean isValid = true;
					if (!sessionUser.getEmail().equals(uiUser.getEmail())) {
						User tmpUser = new User();
						tmpUser.setEmail(uiUser.getEmail());
						tmpUser = this.userService.getUser(tmpUser);
					}
					if (isValid) {
						MultipartFile profilePhoto = (MultipartFile) session.getAttribute(EnmSession.USER_PHOTO.getId());
						session.removeAttribute(EnmSession.USER_PHOTO.getId());
						
						boolean profilePhotoChanged = false;
						boolean profilePhotoDeleted = false;
						
						if (profilePhoto != null) {
							if (profilePhoto.getOriginalFilename().equalsIgnoreCase("deleted")) {
								profilePhotoDeleted = true;
							} else if (!profilePhoto.getOriginalFilename().equalsIgnoreCase("dummy")) {
								profilePhotoChanged = true;
							}
						}
						
						String modifiedBy = sessionUser.getFullName();
						Long oldProfilePhotoId = sessionUser.getProfilePhotoId();
						Long deleteProfilePhotoId = null;
						
						if (profilePhotoDeleted) {
							deleteProfilePhotoId = oldProfilePhotoId; 
						}
						
						User user = this.userService.getUser(sessionUser);
						user.setProfilePhoto(null);
						
						if (uiUser.getFirstName() != null && uiUser.getFirstName().trim().length() > 0) {
							user.setFirstName(uiUser.getFirstName());
						}
						if (uiUser.getLastName() != null && uiUser.getLastName().trim().length() > 0) {
							user.setLastName(uiUser.getLastName());
						}
						if (uiUser.getGender() != null && EnmGender.getGender(uiUser.getGender()) != null) {
							user.setGender(EnmGender.getGender(uiUser.getGender()).getId());
						}
						if (uiUser.getPassword() != null && uiUser.getPassword().trim().length() > 0) {
							user.setPassword(uiUser.getPassword());
						}
						if (user.getGoogleId() != null || user.getFacebookId() != null) {
							user.setEmail(uiUser.getEmail());
							
							if (sessionUser.getEmail() == null || !sessionUser.getEmail().equals(user.getEmail())) {
								String verificationCode = SecurityUtil.generateUUID().replace("-", "");
								
								user.setEmailVerified(EnmBoolStatus.NO.getId());
								user.setUserActivationKeyEmail(verificationCode);
							}
						}
						user.setResidenceLocationName(uiUser.getResidenceLocationName());
						user.setDescription(uiUser.getDescription());
						user.setJobTitle(uiUser.getJobTitle());
						user.setSchoolName(uiUser.getSchoolName());
						user.setLanguages(uiUser.getLanguages());
						//user.setMsisdn(uiUser.getMsisdn());
						user.setBirthDate(uiUser.getBirthDate());
											
						if (profilePhotoDeleted) {
							user.setProfilePhotoId(null);
						}
						
						if (profilePhotoChanged) {
							String fileSuffix = profilePhoto.getContentType().split("/")[1];
							String fileName = profilePhoto.getOriginalFilename().split("\\.")[0];
							Photo photo = new Photo();
							photo.setFileName(fileName);
							photo.setFileSize(profilePhoto.getSize());
							photo.setEntityType(EnmEntityType.USER.getId());
							photo.setEntityId(user.getId());
							photo.setCreatedBy(modifiedBy);
							photo.setFileType(EnmFileType.getFileTypeWithSuffix(fileSuffix).getValue());
							
							user.setProfilePhoto(photo);
						}
						
						OperationResult updateResult = this.userService.updateUser(user, true, deleteProfilePhotoId);
						if (OperationResult.isResultSucces(updateResult)) {
							user = this.userService.getUser(user);
							this.processLogin(session, user, EnmLoginType.getLoginType(sessionUser.getLoginType()));
							if (!sessionUser.getEmail().equals(user.getEmail())) {
								this.sendEmailVerificationMail(user);
							}
							if (profilePhotoDeleted || profilePhotoChanged) {
								try {
									
									String rootPhotoFolder = this.webApplication.getRootUserPhotoPath();
									String userPhotoFolder = FileUtil.concatPath(rootPhotoFolder, user.getId().toString()); 
									
									FileModel photo = user.getProfilePhoto();
									
									String fileName = profilePhoto.getOriginalFilename();
									
									if (oldProfilePhotoId != null) {
										FileUtil.cleanDirectory(userPhotoFolder, false);
									}
									
									if (!profilePhotoDeleted) {
										String tmpPhotoPath = FileUtil.concatPath(rootPhotoFolder, user.getId().toString(), "tmp", fileName);
										
										String newSmallFilePath = this.webApplication.getUserPhotoPath(user.getId(), photo.getId(), EnmSize.SMALL.getValue());
										String newMediumFilePath = this.webApplication.getUserPhotoPath(user.getId(), photo.getId(), EnmSize.MEDIUM.getValue());
										
										File tmpFile = new File(tmpPhotoPath);

										ImageUtil.resizeImage(tmpFile, newMediumFilePath, this.webApplication.getMediumUserPhotoSize());
										ImageUtil.resizeImage(tmpFile, newSmallFilePath, this.webApplication.getSmallUserPhotoSize());
										
										String tmpFolder = tmpPhotoPath.substring(0, tmpPhotoPath.lastIndexOf(File.separator));
										
										FileUtil.cleanDirectory(tmpFolder, true);
									}
									
								} catch (Exception e) {
									System.out.println(CommonUtil.getExceptionMessage(e));
								}
							}
						}
						operationResult = updateResult;
					} else {
						operationResult.setResultCode(EnmResultCode.WARNING.getValue());
						operationResult.setResultDesc("This email is used by another user!");
					}
				} else {
					operationResult.setErrorCode(EnmErrorCode.USER_NOT_LOGGED_IN.getId());
					operationResult.setResultCode(EnmResultCode.ERROR.getValue());
					operationResult.setResultDesc(AppConstants.USER_NOT_LOGGED_IN);
				}
			} else {
				operationResult.setResultCode(EnmResultCode.ERROR.getValue());
				operationResult.setResultDesc("You are not authorized for this operation!");
			}
			
			httpStatus = HttpStatus.OK;			
		} catch (Exception e) {
			operationResult.setResultCode(EnmResultCode.EXCEPTION.getValue());
			operationResult.setResultDesc("Create operation could not be completed. Please try again later!");
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<OperationResult>(operationResult, httpStatus);
    }
    
    @RequestMapping(value = "/api/user/confirmemail", method = RequestMethod.POST)
    public ResponseEntity<OperationResult> confirmEmail(@RequestParam("id") Long id, @RequestParam("code") String code, HttpSession session) throws JsonParseException, JsonMappingException, IOException, ParseException {
		HttpStatus httpStatus = null;
		OperationResult operationResult = new OperationResult();
		try {			
			User user = new User();
			user.setId(id);
			
			User dbUser = this.userService.getUser(user);
			if (dbUser != null) {
				if (dbUser.getEmailVerified().equals(EnmBoolStatus.NO.getId())) {
					if (dbUser.getUserActivationKeyEmail() != null && dbUser.getUserActivationKeyEmail().equals(code)) {
						String modifiedBy = dbUser.getFullName();
						
						user.setEmailVerified(EnmBoolStatus.YES.getId());;
						user.setModifiedBy(modifiedBy);
						
						OperationResult createResult = this.userService.updateUser(user, false, null);
						if (OperationResult.isResultSucces(createResult)) {
							User sessionUser = this.getSessionUser(session);
							if (sessionUser != null) {
								sessionUser.setEmailVerified(EnmBoolStatus.YES.getId());
							}
							operationResult = createResult;
						} else {
							operationResult.setResultCode(EnmResultCode.ERROR.getValue());
							operationResult.setResultDesc("User update could not be completed!");
							operationResult.setErrorCode(EnmErrorCode.UNDEFINED_ERROR.getId());
						}
					} else {
						operationResult.setResultCode(EnmResultCode.ERROR.getValue());
						operationResult.setResultDesc("Email verification code is incorrect!");
						operationResult.setErrorCode(EnmErrorCode.USER_NOT_FOUND.getId());
					}
				} else {
					operationResult.setResultCode(EnmResultCode.ERROR.getValue());
					operationResult.setResultDesc("User is already verified!");
					operationResult.setErrorCode(EnmErrorCode.UNDEFINED_ERROR.getId());
				}
			} else {
				operationResult.setResultCode(EnmResultCode.ERROR.getValue());
				operationResult.setResultDesc("User not found!");
				operationResult.setErrorCode(EnmErrorCode.USER_NOT_FOUND.getId());
			}
			
			httpStatus = HttpStatus.OK;			
		} catch (Exception e) {
			operationResult.setResultCode(EnmResultCode.EXCEPTION.getValue());
			operationResult.setResultDesc("Create operation could not be completed. Please try again later!");
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<OperationResult>(operationResult, httpStatus);
    }
    
    @RequestMapping(value = "/api/user/savephoto", method = RequestMethod.POST)
    public ResponseEntity<OperationResult> savePhoto(@RequestParam("profilePhoto") MultipartFile prifilePhoto, HttpSession session) {
		OperationResult operationResult = new OperationResult();
		try {
			Object activeOperation = super.getSession().getAttribute(EnmSession.ACTIVE_OPERATION.getId());
			if (activeOperation != null && (activeOperation.equals(EnmOperation.CREATE_USER) || activeOperation.equals(EnmOperation.UPDATE_USER))) {
				String userId = null;
				if (activeOperation.equals(EnmOperation.CREATE_USER)) {
					userId = "tmp_" + Double.valueOf((Math.random() * 100000000)).longValue();
					session.removeAttribute(EnmSession.TMP_USER_PLACE_ID.getId());
					session.setAttribute(EnmSession.TMP_USER_PLACE_ID.getId(), userId);
				} else {
					User user = super.getSessionUser(session);
					userId = user.getId().toString();
				}
				
				String rootPhotoFolder = this.webApplication.getRootUserPhotoPath();
				
				String userTmpPhotoFolderPath = FileUtil.concatPath(rootPhotoFolder, userId, "tmp");
				File userTmpPhotoFolder = new File(userTmpPhotoFolderPath);
				if (userTmpPhotoFolder.exists()) {
					FileUtils.cleanDirectory(userTmpPhotoFolder);
				} else {
					userTmpPhotoFolder.mkdirs();	
				}
				
				String fileName = prifilePhoto.getOriginalFilename();
				if (!fileName.toUpperCase().startsWith("DUMMY_")) {
					String tmpPhotoPath = userTmpPhotoFolderPath + File.separatorChar + fileName;
					File tmpFile = new File(tmpPhotoPath);
					prifilePhoto.transferTo(tmpFile);
				}
				
				session.removeAttribute(EnmSession.USER_PHOTO.getId());
				session.setAttribute(EnmSession.USER_PHOTO.getId(), prifilePhoto);
				
				operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
			} else {
				operationResult.setResultCode(EnmResultCode.ERROR.getValue());
				operationResult.setResultDesc("You are not authorized for this operation!");
			}
		} catch (Exception e) {
			operationResult.setResultCode(EnmResultCode.ERROR.getValue());
			operationResult.setResultDesc(CommonUtil.getExceptionMessage(e));
		}
		
		return new ResponseEntity<OperationResult>(operationResult, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/api/user/msisdn/sendcode", method = RequestMethod.POST)
    public ResponseEntity<OperationResult> sendMsisdnVerificationCode(@RequestParam("msisdn") String msisdn, @RequestParam("msisdnCountryCode") String msisdnCountryCode, HttpSession session) throws JsonParseException, JsonMappingException, IOException, ParseException {
    	OperationResult operationResult = new OperationResult();
		try {
			User user = super.getSessionUser(session);
			if (user != null) {
				Object activeOperation = super.getSession().getAttribute(EnmSession.ACTIVE_OPERATION.getId());
				if (activeOperation.equals(EnmOperation.UPDATE_USER) 
						|| activeOperation.equals(EnmOperation.TRUST_AND_VERIFICATION)
						|| activeOperation.equals(EnmOperation.VERIFICATION)) {
					if (EnmBoolStatus.NO.getId().equals(user.getMsisdnVerified())) {
						if ((msisdn != null && msisdnCountryCode == null) 
								|| (msisdn == null && msisdnCountryCode != null)) {
							operationResult.setResultCode(EnmResultCode.ERROR.getValue());
							operationResult.setErrorCode(EnmErrorCode.UNDEFINED_ERROR.getId());
							operationResult.setResultDesc("msisdn and country code parameters are required!");
						} else {
							boolean msisdnSent = false;
							if (msisdn != null && msisdnCountryCode != null) {
								msisdnSent = true;
							}
							if (msisdnSent || user.getMsisdn() != null) {
								
								session.removeAttribute(EnmSession.MSISDN_VERIFICATION_CODE.getId());
								
								if (msisdnSent) {
									User updatedUser = new User();
									updatedUser.setId(user.getId());
									updatedUser.setMsisdn(msisdn);
									updatedUser.setMsisdnCountryCode(msisdnCountryCode);
									updatedUser.setMsisdnVerified(EnmBoolStatus.NO.getId());
									
									OperationResult updateResult = this.userService.updateUser(updatedUser, false, null);
									if (OperationResult.isResultSucces(updateResult)) {
										
										user.setMsisdn(msisdn);
										user.setMsisdnCountryCode(msisdnCountryCode);
										user.setMsisdnVerified(EnmBoolStatus.NO.getId());
									} else {
										operationResult.setResultCode(EnmResultCode.ERROR.getValue());
										operationResult.setErrorCode(EnmErrorCode.UNDEFINED_ERROR.getId());
										operationResult.setResultDesc("User record could not be updated. Please try again later!");
										throw new OperationResultException(operationResult);
									}
								}
								
								Integer msisdnVerificationCode = new Double(Math.random() * 10000).intValue();
								
								sendVerifyMsisdnMail(user, msisdnVerificationCode);
								session.setAttribute(EnmSession.MSISDN_VERIFICATION_CODE.getId(), msisdnVerificationCode);
								operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
							} else {
								operationResult.setResultCode(EnmResultCode.ERROR.getValue());
								operationResult.setErrorCode(EnmErrorCode.UNDEFINED_ERROR.getId());
								operationResult.setResultDesc("msisdn is required!");
							}	
						}
					} else {
						operationResult.setResultCode(EnmResultCode.ERROR.getValue());
						operationResult.setErrorCode(EnmErrorCode.UNDEFINED_ERROR.getId());
						operationResult.setResultDesc("msisdn is already verified!");
					}
				} else {
					operationResult.setResultCode(EnmResultCode.ERROR.getValue());
					operationResult.setResultDesc("You are not authorized for this operation!");
				}
			} else {
				operationResult.setErrorCode(EnmErrorCode.USER_NOT_LOGGED_IN.getId());
				operationResult.setResultCode(EnmResultCode.ERROR.getValue());
				operationResult.setResultDesc(AppConstants.USER_NOT_LOGGED_IN);
			}
		} catch (OperationResultException e) {
			operationResult = e.getOperationResult();
		} catch (Exception e) {
			operationResult.setResultCode(EnmResultCode.ERROR.getValue());
			operationResult.setResultDesc("Operation failed because of an unexpected error. Please try again later!");
		}
		
		return new ResponseEntity<OperationResult>(operationResult, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/api/user/email/sendcode", method = RequestMethod.POST)
    public ResponseEntity<OperationResult> sendEmailVerificationCode(HttpSession session) {
    	OperationResult operationResult = new OperationResult();
		try {
			User user = super.getSessionUser(session);
			if (user != null) {
				Object activeOperation = super.getSession().getAttribute(EnmSession.ACTIVE_OPERATION.getId());
				if (activeOperation.equals(EnmOperation.TRUST_AND_VERIFICATION) 
						|| activeOperation.equals(EnmOperation.VERIFICATION)) {
					User updatedUser = new User();
					updatedUser.setId(user.getId());
					updatedUser.setEmailVerified(EnmBoolStatus.NO.getId());
					updatedUser.setUserActivationKeyEmail(SecurityUtil.generateUUID());
					
					OperationResult updateResult = this.userService.updateUser(updatedUser, false, null);
					if (OperationResult.isResultSucces(updateResult)) {
						user.setEmailVerified(updatedUser.getEmailVerified());
						user.setUserActivationKeyEmail(updatedUser.getUserActivationKeyEmail());
						this.sendEmailVerificationMail(user);
						
						operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
					} else {
						operationResult.setResultCode(EnmResultCode.ERROR.getValue());
						operationResult.setErrorCode(EnmErrorCode.UNDEFINED_ERROR.getId());
						operationResult.setResultDesc("User record could not be updated. Please try again later!");
					}
				} else {
					operationResult.setResultCode(EnmResultCode.ERROR.getValue());
					operationResult.setResultDesc("You are not authorized for this operation!");
				}
			} else {
				operationResult.setErrorCode(EnmErrorCode.USER_NOT_LOGGED_IN.getId());
				operationResult.setResultCode(EnmResultCode.ERROR.getValue());
				operationResult.setResultDesc(AppConstants.USER_NOT_LOGGED_IN);
			}
		} catch (Exception e) {
			operationResult.setResultCode(EnmResultCode.ERROR.getValue());
			operationResult.setResultDesc("Operation failed because of an unexpected error. Please try again later!");
		}
		
		return new ResponseEntity<OperationResult>(operationResult, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/api/user/email/isverified", method = RequestMethod.POST)
    public ResponseEntity<ValueOperationResult<Boolean>> isEmailVerified(HttpSession session) {
    	ValueOperationResult<Boolean> operationResult = new ValueOperationResult<Boolean>();
		try {
			User user = super.getSessionUser(session);
			if (user != null) {
				Object activeOperation = super.getSession().getAttribute(EnmSession.ACTIVE_OPERATION.getId());
				if (activeOperation.equals(EnmOperation.VERIFICATION)) {
					
					if (user.getEmailVerified().equals(EnmBoolStatus.YES.getId())) {
						operationResult.setResultValue(true);
					} else {
						operationResult.setResultValue(false);
					}
					operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
				} else {
					operationResult.setResultCode(EnmResultCode.ERROR.getValue());
					operationResult.setResultDesc("You are not authorized for this operation!");
				}
			} else {
				operationResult.setErrorCode(EnmErrorCode.USER_NOT_LOGGED_IN.getId());
				operationResult.setResultCode(EnmResultCode.ERROR.getValue());
				operationResult.setResultDesc(AppConstants.USER_NOT_LOGGED_IN);
			}
		} catch (Exception e) {
			operationResult.setResultCode(EnmResultCode.ERROR.getValue());
			operationResult.setResultDesc(CommonUtil.getExceptionMessage(e));
		}
		
		return new ResponseEntity<ValueOperationResult<Boolean>>(operationResult, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/api/user/msisdn/verify", method = RequestMethod.POST)
    public ResponseEntity<OperationResult> verifyMsisdnCode(@RequestParam("code") String code, HttpSession session) {
    	OperationResult operationResult = new OperationResult();
		try {
			User user = super.getSessionUser(session);
			if (user != null) {
				Object activeOperation = super.getSession().getAttribute(EnmSession.ACTIVE_OPERATION.getId());
				if (activeOperation.equals(EnmOperation.UPDATE_USER)
						|| activeOperation.equals(EnmOperation.TRUST_AND_VERIFICATION)
						|| activeOperation.equals(EnmOperation.VERIFICATION)) {
					Object sessionCode = session.getAttribute(EnmSession.MSISDN_VERIFICATION_CODE.getId());
					
					if (sessionCode != null) {
						if (sessionCode.toString().equals(code)) {
							User updatedUser = new User();
							updatedUser.setId(user.getId());
							updatedUser.setMsisdnVerified(EnmBoolStatus.YES.getId());
							
							OperationResult updateResult = this.userService.updateUser(updatedUser, false, null);
							if (OperationResult.isResultSucces(updateResult)) {
								user.setMsisdnVerified(EnmBoolStatus.YES.getId());
								operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
								session.removeAttribute(EnmSession.MSISDN_VERIFICATION_CODE.getId());
							} else {
								operationResult.setResultCode(EnmResultCode.ERROR.getValue());
								operationResult.setErrorCode(EnmErrorCode.UNDEFINED_ERROR.getId());
								operationResult.setResultDesc("User record could not be updated. Please try again later!");
							}
						} else {
							operationResult.setResultCode(EnmResultCode.ERROR.getValue());
							operationResult.setErrorCode(EnmErrorCode.UNDEFINED_ERROR.getId());
							operationResult.setResultDesc("Verification code is incorrect!");
						}
					} else {
						operationResult.setResultCode(EnmResultCode.ERROR.getValue());
						operationResult.setErrorCode(EnmErrorCode.UNDEFINED_ERROR.getId());
						operationResult.setResultDesc("System is not in msisdn verification state!");
					}
				} else {
					operationResult.setResultCode(EnmResultCode.ERROR.getValue());
					operationResult.setResultDesc("You are not authorized for this operation!");
				}
			} else {
				operationResult.setErrorCode(EnmErrorCode.USER_NOT_LOGGED_IN.getId());
				operationResult.setResultCode(EnmResultCode.ERROR.getValue());
				operationResult.setResultDesc(AppConstants.USER_NOT_LOGGED_IN);
			}
		} catch (Exception e) {
			operationResult.setResultCode(EnmResultCode.ERROR.getValue());
			operationResult.setResultDesc(CommonUtil.getExceptionMessage(e));
		}
		
		return new ResponseEntity<OperationResult>(operationResult, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/api/user/connectgoogle", method = RequestMethod.POST)
    public ResponseEntity<OperationResult> connectGoogleAccount(@RequestBody User uiUser, HttpSession session) {
    	OperationResult operationResult = new OperationResult();
		try {
			User user = super.getSessionUser(session);
			if (user != null) {
				if (uiUser.getGoogleId() != null && uiUser.getGoogleEmail() != null) {
					User dbUser = new User();
					dbUser.setGoogleId(uiUser.getGoogleId());
					
					User registeredUser = this.userService.getUser(dbUser);
					
					if(registeredUser == null) {
						dbUser.setId(user.getId());
						dbUser.setGoogleEmail(uiUser.getGoogleEmail());
						dbUser.setGoogleId(uiUser.getGoogleId());
						
						if (user.getFirstName() == null) {
							dbUser.setFirstName(uiUser.getFirstName());
						}
						if (user.getLastName() == null) {
							dbUser.setLastName(uiUser.getLastName());
						}
						if (user.getGender() == null && uiUser.getGender() != null) {
							EnmGender gender = EnmGender.getGender(uiUser.getGender());
							if (gender != null) {
								dbUser.setGender(uiUser.getGender());
							}
						}
						
						OperationResult updateUserResult = this.userService.updateUser(dbUser, false, null);
						
						if (OperationResult.isResultSucces(updateUserResult)) {
							operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
							dbUser = this.userService.getUser(dbUser);
							this.processLogin(session, dbUser, EnmLoginType.getLoginType(user.getLoginType()));
						} else {
							operationResult = updateUserResult;
						}
					} else {
						operationResult.setResultCode(EnmResultCode.ERROR.getValue());
						operationResult.setResultDesc("This google account is used by another user!");
					}	
				} else {
					operationResult.setResultCode(EnmResultCode.ERROR.getValue());
					operationResult.setResultDesc("googleId and email parameters are mandatory!");
				}
			} else {
				operationResult.setErrorCode(EnmErrorCode.USER_NOT_LOGGED_IN.getId());
				operationResult.setResultCode(EnmResultCode.ERROR.getValue());
				operationResult.setResultDesc(AppConstants.USER_NOT_LOGGED_IN);
			}
		} catch (Exception e) {
			operationResult.setResultCode(EnmResultCode.ERROR.getValue());
			operationResult.setResultDesc(CommonUtil.getExceptionMessage(e));
		}
		
		return new ResponseEntity<OperationResult>(operationResult, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/api/user/disconnectgoogle", method = RequestMethod.POST)
    public ResponseEntity<OperationResult> disconnectGoogleAccount(HttpSession session) {
    	OperationResult operationResult = new OperationResult();
		try {
			User user = super.getSessionUser(session);
			if (user != null) {
				if (user.getGoogleId() != null && user.getGoogleEmail() != null 
						&& ((user.getEmail() != null && user.getPassword() != null) 
								|| (user.getFacebookEmail() != null && user.getFacebookId() != null))) {
					User updatedUser = this.userService.getUser(user);
					
					updatedUser.setGoogleEmail(null);
					updatedUser.setGoogleId(null);
										
					OperationResult updateUserResult = this.userService.updateUser(updatedUser, true, null);
					
					if (OperationResult.isResultSucces(updateUserResult)) {
						user.setGoogleEmail(null);
						user.setGoogleId(null);
						operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
					} else {
						// TODO : log to file
						operationResult.setResultCode(EnmResultCode.ERROR.getValue());
						operationResult.setResultDesc("User record could not be updated, please try again later!");
					}	
				} else {
					operationResult.setResultCode(EnmResultCode.ERROR.getValue());
					operationResult.setResultDesc("Google is the only account binded to your user record!");
				}
			} else {
				operationResult.setErrorCode(EnmErrorCode.USER_NOT_LOGGED_IN.getId());
				operationResult.setResultCode(EnmResultCode.ERROR.getValue());
				operationResult.setResultDesc(AppConstants.USER_NOT_LOGGED_IN);
			}
		} catch (Exception e) {
			operationResult.setResultCode(EnmResultCode.ERROR.getValue());
			operationResult.setResultDesc(CommonUtil.getExceptionMessage(e));
		}
		
		return new ResponseEntity<OperationResult>(operationResult, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/api/user/connectfacebook", method = RequestMethod.POST)
    public ResponseEntity<OperationResult> connectFacebookAccount(@RequestBody User uiUser, HttpSession session) {
    	OperationResult operationResult = new OperationResult();
		try {
			User user = super.getSessionUser(session);
			if (user != null) {
				if (uiUser.getFacebookId() != null && uiUser.getFacebookEmail() != null) {
					User dbUser = new User();
					dbUser.setFacebookId(uiUser.getFacebookId());
					
					User registeredUser = this.userService.getUser(dbUser);
					
					if(registeredUser == null) {
						dbUser.setId(user.getId());
						dbUser.setFacebookEmail(uiUser.getFacebookEmail());
						dbUser.setFacebookId(uiUser.getFacebookId());
						
						if (user.getFirstName() == null) {
							dbUser.setFirstName(uiUser.getFirstName());
						}
						if (user.getLastName() == null) {
							dbUser.setLastName(uiUser.getLastName());
						}
						if (user.getGender() == null && uiUser.getGender() != null) {
							EnmGender gender = EnmGender.getGender(uiUser.getGender());
							if (gender != null) {
								dbUser.setGender(uiUser.getGender());
							}
						}
						
						OperationResult updateUserResult = this.userService.updateUser(dbUser, false, null);
						
						if (OperationResult.isResultSucces(updateUserResult)) {
							operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
							dbUser = this.userService.getUser(dbUser);
							this.processLogin(session, dbUser, EnmLoginType.getLoginType(user.getLoginType()));
						} else {
							operationResult = updateUserResult;
						}
					} else {
						operationResult.setResultCode(EnmResultCode.ERROR.getValue());
						operationResult.setResultDesc("This google account is used by another user!");
					}	
				} else {
					operationResult.setResultCode(EnmResultCode.ERROR.getValue());
					operationResult.setResultDesc("googleId and email parameters are mandatory!");
				}
			} else {
				operationResult.setErrorCode(EnmErrorCode.USER_NOT_LOGGED_IN.getId());
				operationResult.setResultCode(EnmResultCode.ERROR.getValue());
				operationResult.setResultDesc(AppConstants.USER_NOT_LOGGED_IN);
			}
		} catch (Exception e) {
			operationResult.setResultCode(EnmResultCode.ERROR.getValue());
			operationResult.setResultDesc(CommonUtil.getExceptionMessage(e));
		}
		
		return new ResponseEntity<OperationResult>(operationResult, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/api/user/disconnectfacebook", method = RequestMethod.POST)
    public ResponseEntity<OperationResult> disconnectFacebookAccount(HttpSession session) {
    	OperationResult operationResult = new OperationResult();
		try {
			User user = super.getSessionUser(session);
			if (user != null) {
				if (user.getFacebookId() != null && user.getFacebookEmail() != null 
						&& ((user.getEmail() != null && user.getPassword() != null) 
								|| (user.getGoogleEmail() != null && user.getGoogleId() != null))) {
					User updatedUser = this.userService.getUser(user);
					
					updatedUser.setFacebookEmail(null);
					updatedUser.setFacebookId(null);
										
					OperationResult updateUserResult = this.userService.updateUser(updatedUser, true, null);
					
					if (OperationResult.isResultSucces(updateUserResult)) {
						user.setFacebookEmail(null);
						user.setFacebookId(null);
						operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
					} else {
						// TODO : log to file
						operationResult.setResultCode(EnmResultCode.ERROR.getValue());
						operationResult.setResultDesc("User record could not be updated, please try again later!");
					}	
				} else {
					operationResult.setResultCode(EnmResultCode.ERROR.getValue());
					operationResult.setResultDesc("Google is the only account binded to your user record!");
				}
			} else {
				operationResult.setErrorCode(EnmErrorCode.USER_NOT_LOGGED_IN.getId());
				operationResult.setResultCode(EnmResultCode.ERROR.getValue());
				operationResult.setResultDesc(AppConstants.USER_NOT_LOGGED_IN);
			}
		} catch (Exception e) {
			operationResult.setResultCode(EnmResultCode.ERROR.getValue());
			operationResult.setResultDesc(CommonUtil.getExceptionMessage(e));
		}
		
		return new ResponseEntity<OperationResult>(operationResult, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/api/user/msisdn/remove", method = RequestMethod.POST)
    public ResponseEntity<OperationResult> removeMsisdn(HttpSession session) {
    	OperationResult operationResult = new OperationResult();
		try {
			User user = this.getSessionUser(session);
			if (user != null) {
				Object activeOperation = super.getSession().getAttribute(EnmSession.ACTIVE_OPERATION.getId());
				if (activeOperation.equals(EnmOperation.UPDATE_USER)
						|| activeOperation.equals(EnmOperation.TRUST_AND_VERIFICATION)) {
					
					User updatedUser = new User();
					updatedUser.setId(user.getId());
					updatedUser.setMsisdn(null);
					updatedUser.setMsisdnVerified(EnmBoolStatus.NO.getId());
					
					OperationResult updateResult = this.userService.updateUser(updatedUser, true, null);
					if (OperationResult.isResultSucces(updateResult)) {
						user.setMsisdnVerified(EnmBoolStatus.NO.getId());
						user.setMsisdn(null);
						operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
					} else {
						operationResult.setResultCode(EnmResultCode.ERROR.getValue());
						operationResult.setErrorCode(EnmErrorCode.UNDEFINED_ERROR.getId());
						operationResult.setResultDesc("User record could not be updated. Please try again later!");
					}
				} else {
					operationResult.setResultCode(EnmResultCode.ERROR.getValue());
					operationResult.setResultDesc("You are not authorized for this operation!");
				}
			} else {
				operationResult.setErrorCode(EnmErrorCode.USER_NOT_LOGGED_IN.getId());
				operationResult.setResultCode(EnmResultCode.ERROR.getValue());
				operationResult.setResultDesc(AppConstants.USER_NOT_LOGGED_IN);
			}
		} catch (Exception e) {
			operationResult.setResultCode(EnmResultCode.ERROR.getValue());
			operationResult.setResultDesc(CommonUtil.getExceptionMessage(e));
		}
		
		return new ResponseEntity<OperationResult>(operationResult, HttpStatus.OK);
    }
    
	private void processLogin(HttpSession session, User user, EnmLoginType loginType) {
		// Place records are read from db
		Place place = new Place();
		place.setHostUserId(user.getId());
		ListOperationResult<Place> placeListResult = this.placeService.listPlace(place, false, false, false);
		user.setPlaceList(placeListResult.getObjectList());
		if (user.getPlaceList() != null) {
			user.setPlaceListingCount(user.getPlaceList().size());
		} else {
			user.setPlaceListingCount(0);
		}
		
		// Favorite records are read from db
		Favorite favorite = new Favorite();
		favorite.setUserId(user.getId());
		
		List<Favorite> favoriteList = this.favoriteService.listFavorite(favorite, null, false, false, true);
		
		if (favoriteList != null && favoriteList.size() > 0) {
			for (Favorite tmpFavorite : favoriteList) {
				User hostUSer = tmpFavorite.getHostUser();
				
				User tmpUser = new User();
				tmpUser.setId(hostUSer.getId());
				tmpUser.setFirstName(hostUSer.getFirstName());
				
				String smallProfileUrl = this.webApplication.getUserPhotoUrl(hostUSer.getId(), hostUSer.getProfilePhotoId(), EnmSize.SMALL.getValue());
				tmpUser.setProfileImageUrl(smallProfileUrl);
				
				tmpFavorite.setHostUser(tmpUser);
				
				BaseModel entity = this.favoriteService.getEntityDetails(tmpFavorite.getEntityType(), tmpFavorite.getEntityId());
				
				tmpFavorite.setEntity(entity);
			}
		}
		
		user.addFavoriteList(favoriteList);
		
		user.setLoginType(loginType.getId());
		session.setAttribute(EnmSession.USER.getId(), user);
		session.setAttribute(EnmSession.LOGIN_TYPE.getId(), loginType.getId());
	}

	private void sendEmailVerificationMail(User user) throws UnsupportedEncodingException {
		String htmlFilePath = FileUtil.concatPath(this.webApplication.getLocalAppPath(), "static", "html", "VerifyEmail.html");	
		
		List<String> toList = new ArrayList<String>();
		toList.add(user.getEmail());
		
		String emailVerificationUrl = this.webApplication.getEmailVerificationUrl();
		emailVerificationUrl = emailVerificationUrl.replaceAll("#paramUserId#", user.getId().toString());
		emailVerificationUrl = emailVerificationUrl.replaceAll("#paramEmailVerificationCode#", user.getUserActivationKeyEmail());
		
		String emailVerificationUrlEncoded = URLEncoder.encode(emailVerificationUrl, "UTF-8");
		
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("#paramUserName#", user.getFirstName());
		paramMap.put("#paramEmailVerificationUrl#", emailVerificationUrl);
		paramMap.put("#paramEmailVerificationUrlEncoded#", emailVerificationUrlEncoded);
		this.mailUtil.sendMailFromTemplate(htmlFilePath, paramMap, toList, null, "LetsErasmus Email Verifitcation", null);
	}
	
	private void sendWelcomeMail(User user, EnmLoginType loginType) throws UnsupportedEncodingException {
		String htmlFilePath = FileUtil.concatPath(this.webApplication.getLocalAppPath(), "static", "html", "VerifyEmail.html");
		String email = null;
		
		if (loginType == EnmLoginType.LOCAL_ACCOUNT) {
			email = user.getEmail();
		} else if (loginType == EnmLoginType.GOOGLE) {
			email = user.getGoogleEmail();
		} else if (loginType == EnmLoginType.FACEBOOK) {
			email = user.getFacebookEmail();
		}
		
		List<String> toList = new ArrayList<String>();
		toList.add(email);
		
		String emailVerificationUrl = this.webApplication.getEmailVerificationUrl();
		emailVerificationUrl = emailVerificationUrl.replaceAll("#paramUserId#", user.getId().toString());
		emailVerificationUrl = emailVerificationUrl.replaceAll("#paramEmailVerificationCode#", user.getUserActivationKeyEmail());
		
		String emailVerificationUrlEncoded = URLEncoder.encode(emailVerificationUrl, "UTF-8");
		
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("#paramUserName#", user.getFirstName());
		paramMap.put("#paramEmailVerificationUrl#", emailVerificationUrl);
		paramMap.put("#paramEmailVerificationUrlEncoded#", emailVerificationUrlEncoded);
		this.mailUtil.sendMailFromTemplate(htmlFilePath, paramMap, toList, null, "LetsErasmus Email Verifitcation", null);
	}
		
	private void sendVerifyMsisdnMail(User user, Integer verificationCode) throws UnsupportedEncodingException {
		
		List<String> toList = new ArrayList<String>();
		toList.add(user.getEmail());
		
		String emailContent = "Your msisdn verification code is: " + verificationCode.toString();
		
		this.mailUtil.sendMail(emailContent, toList, null, "LetsErasmus Msisdn Verifitcation Code", null);
	}
	
}
