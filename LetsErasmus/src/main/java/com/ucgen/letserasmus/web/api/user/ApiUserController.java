package com.ucgen.letserasmus.web.api.user;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Map;

import javax.servlet.http.HttpSession;

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
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ucgen.common.operationresult.EnmResultCode;
import com.ucgen.common.operationresult.OperationResult;
import com.ucgen.common.util.CommonUtil;
import com.ucgen.common.util.DateUtil;
import com.ucgen.common.util.FileUtil;
import com.ucgen.common.util.ImageUtil;
import com.ucgen.letserasmus.library.common.enumeration.EnmBoolStatus;
import com.ucgen.letserasmus.library.common.enumeration.EnmEntityType;
import com.ucgen.letserasmus.library.file.enumeration.EnmFileType;
import com.ucgen.letserasmus.library.file.model.Photo;
import com.ucgen.letserasmus.library.user.enumeration.EnmLoginType;
import com.ucgen.letserasmus.library.user.enumeration.EnmUserStatus;
import com.ucgen.letserasmus.library.user.model.User;
import com.ucgen.letserasmus.library.user.service.IUserService;
import com.ucgen.letserasmus.web.api.BaseApiController;
import com.ucgen.letserasmus.web.view.application.AppUtil;
import com.ucgen.letserasmus.web.view.application.EnmOperation;
import com.ucgen.letserasmus.web.view.application.EnmSession;

@RestController
public class ApiUserController extends BaseApiController {

	private IUserService userService;
	
	@Autowired
	public void setUserService(IUserService userService) {
		this.userService = userService;
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
			if (loginType != null) {
				if (loginType == EnmLoginType.LOCAL_ACCOUNT) {
					operationResult = this.loginWithLocalAccount(user, session);	
				} else if (loginType == EnmLoginType.GOOGLE) {
					operationResult = this.loginWithGoogleAccount(user, session);
				} else if (loginType == EnmLoginType.FACEBOOK) {
					operationResult = this.loginWithFacebookAccount(user, session);
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
				operationResult.setResultCode(EnmResultCode.ERROR.getValue());
				operationResult.setResultDesc("Email and password fields are mandatory.");
			}	
		} catch (Exception e) {
			operationResult.setResultCode(EnmResultCode.EXCEPTION.getValue());
			operationResult.setResultDesc("Create operation could not be completed. Please try again later!");
		}
		return operationResult;
    }
	
    private OperationResult loginWithGoogleAccount(User user, HttpSession session) {
		OperationResult operationResult = new OperationResult();
		try {
			if (user != null && user.getEmail() != null && user.getPassword() != null) {
				User dbUser = new User();
				dbUser.setEmail(user.getEmail());
				dbUser.setPassword(user.getPassword());
				dbUser = this.userService.getUser(dbUser);
				
				if(dbUser != null) {
					operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
					this.processLogin(session, dbUser, EnmLoginType.GOOGLE);
				} else {
					operationResult.setResultCode(EnmResultCode.WARNING.getValue());
					operationResult.setResultDesc("Email or password is incorrect.");
				}
			} else {
				operationResult.setResultCode(EnmResultCode.ERROR.getValue());
				operationResult.setResultDesc("Email and password fields are mandatory.");
			}
		} catch (Exception e) {
			operationResult.setResultCode(EnmResultCode.EXCEPTION.getValue());
			operationResult.setResultDesc("Create operation could not be completed. Please try again later!");
		}
		return operationResult;
    }
    
    private OperationResult loginWithFacebookAccount(User user, HttpSession session) {
		OperationResult operationResult = new OperationResult();
		try {
			if (user != null && user.getEmail() != null && user.getPassword() != null) {
				User dbUser = new User();
				dbUser.setEmail(user.getEmail());
				dbUser.setPassword(user.getPassword());
				dbUser = this.userService.getUser(dbUser);
				
				if(dbUser != null) {
					operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
					this.processLogin(session, dbUser, EnmLoginType.GOOGLE);
				} else {
					operationResult.setResultCode(EnmResultCode.WARNING.getValue());
					operationResult.setResultDesc("Email or password is incorrect.");
				}
			} else {
				operationResult.setResultCode(EnmResultCode.ERROR.getValue());
				operationResult.setResultDesc("Email and password fields are mandatory.");
			}
		} catch (Exception e) {
			operationResult.setResultCode(EnmResultCode.EXCEPTION.getValue());
			operationResult.setResultDesc("Create operation could not be completed. Please try again later!");
		}
		return operationResult;
    }
	
    public OperationResult signupWithLocalAccount(User user, HttpSession session) {
		OperationResult operationResult = new OperationResult();
		
		try {
			User dbUser = new User();
			dbUser.setEmail(user.getEmail());
			dbUser = this.userService.getUser(dbUser);
			
			if(dbUser == null) {
				user.setEmailVerified(EnmBoolStatus.NO.getId());
				user.setMsisdnVerified(EnmBoolStatus.NO.getId());
				user.setStatus(EnmUserStatus.ACTIVE.getValue());
				OperationResult createUserResult = this.userService.insertUser(user);
				
				if (OperationResult.isResultSucces(createUserResult)) {
					operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
					this.processLogin(session, user, EnmLoginType.LOCAL_ACCOUNT);
				} else {
					operationResult = createUserResult;
				}
			} else {
				operationResult.setResultCode(EnmResultCode.WARNING.getValue());
				operationResult.setResultDesc("This email is in use! If you forgot your password click 'Forgot password' link in login page.");
			}	
		} catch (Exception e) {
			operationResult.setResultCode(EnmResultCode.EXCEPTION.getValue());
			operationResult.setResultDesc("Create operation could not be completed. Please try again later!");
		}
		return operationResult;
    }
	
    public OperationResult signupWithGoogleAccount(User user, HttpSession session) {
		OperationResult operationResult = new OperationResult();
		
		try {
			if (user.getGoogleId() != null && user.getEmail() != null) {
				User dbUser = new User();
				dbUser.setEmail(user.getEmail());
				dbUser.setGoogleId(user.getGoogleId());
				dbUser = this.userService.getUser(dbUser);
				
				if(dbUser == null) {
					user.setEmailVerified(EnmBoolStatus.NO.getId());
					user.setMsisdnVerified(EnmBoolStatus.NO.getId());
					user.setStatus(EnmUserStatus.ACTIVE.getValue());
					OperationResult createUserResult = this.userService.insertUser(user);
					
					if (OperationResult.isResultSucces(createUserResult)) {
						operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
						this.processLogin(session, user, EnmLoginType.LOCAL_ACCOUNT);
					} else {
						operationResult = createUserResult;
					}
				} else {
					dbUser.setLoginType(EnmLoginType.GOOGLE.getId());
					dbUser.setProfileImageUrl(user.getProfileImageUrl());
					this.processLogin(session, dbUser, EnmLoginType.GOOGLE);
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
    
    public OperationResult signupWithFacebookAccount(User user, HttpSession session) {
		OperationResult operationResult = new OperationResult();
		
		try {
			if (user.getFacebookId() != null && user.getEmail() != null) {
				User dbUser = new User();
				dbUser.setEmail(user.getEmail());
				dbUser.setFacebookId(user.getFacebookId());
				dbUser = this.userService.getUser(dbUser);
				
				if(dbUser == null) {
					user.setEmailVerified(EnmBoolStatus.NO.getId());
					user.setMsisdnVerified(EnmBoolStatus.NO.getId());
					user.setStatus(EnmUserStatus.ACTIVE.getValue());
					OperationResult createUserResult = this.userService.insertUser(user);
					
					if (OperationResult.isResultSucces(createUserResult)) {
						operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
						this.processLogin(session, user, EnmLoginType.LOCAL_ACCOUNT);
					} else {
						operationResult = createUserResult;
					}
				} else {
					dbUser.setLoginType(EnmLoginType.FACEBOOK.getId());
					dbUser.setFacebookTokenId(user.getFacebookTokenId());
					dbUser.setProfileImageUrl(user.getProfileImageUrl());
					this.processLogin(session, dbUser, EnmLoginType.GOOGLE);
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
	
    @RequestMapping(value = "/api/user/update", method = RequestMethod.POST)
    public ResponseEntity<OperationResult> updateUser(@RequestParam("profilePhoto") MultipartFile profilePhoto, @RequestParam("user") String userJson, HttpSession session) throws JsonParseException, JsonMappingException, IOException, ParseException {
		HttpStatus httpStatus = null;
		OperationResult operationResult = new OperationResult();
		boolean profilePhotoChanged = (profilePhoto != null && !profilePhoto.getOriginalFilename().equalsIgnoreCase("dummy"));
		try {			
			Object activeOperation = super.getSession().getAttribute(EnmSession.ACTIVE_OPERATION.getId());
			if (activeOperation != null && activeOperation.equals(EnmOperation.UPDATE_USER)) {
				User sessionUser = super.getSessionUser(session);
				if (sessionUser != null) {
					String modifiedBy = sessionUser.getFullName();
					Long oldProfilePhotoId = sessionUser.getProfilePhotoId();
					
					ObjectMapper mapper = new ObjectMapper();
					
					Map userMap = mapper.readValue(userJson, Map.class);
					
					User user = new User();
					
					user.setId(sessionUser.getId());
					user.setFirstName(this.getString(userMap.get("firstName")));
					user.setLastName(this.getString(userMap.get("lastName")));
					user.setGender(this.getString(userMap.get("gender")));
					user.setPassword(this.getString(userMap.get("password")));
					user.setEmail(this.getString(userMap.get("email")));
					user.setResidenceLocationName(this.getString(userMap.get("residenceLocationName")));
					user.setDescription(this.getString(userMap.get("description")));
					user.setJobTitle(this.getString(userMap.get("jobTitle")));
					user.setSchoolName(this.getString(userMap.get("schoolName")));
					user.setLanguages(this.getString(userMap.get("languages")));
					user.setMsisdn(this.getString(userMap.get("msisdn")));
					
					if (userMap.get("birthDate") != null && !userMap.get("birthDate").toString().isEmpty()) {
						user.setBirthDate(DateUtil.valueOf(userMap.get("birthDate").toString(), DateUtil.SHORT_DATE_FORMAT));
					}
					
					if (sessionUser.getProfilePhoto() != null) {
						user.setProfilePhotoId(sessionUser.getProfilePhoto().getId());
					}
					
					if (profilePhotoChanged) {
						String fileSuffix = profilePhoto.getContentType().split("/")[1];
						String fileName = profilePhoto.getOriginalFilename().split("\\.")[0];
						Photo photo = new Photo();
						photo.setFileName(fileName);
						photo.setFileSize(profilePhoto.getSize());
						photo.setEntityType(EnmEntityType.USER.getValue());
						photo.setEntityId(user.getId());
						photo.setCreatedBy(modifiedBy);
						photo.setFileType(EnmFileType.getFileTypeWithSuffix(fileSuffix).getValue());
						
						user.setProfilePhoto(photo);
					}
					
					OperationResult createResult = this.userService.updateUser(user);
					if (OperationResult.isResultSucces(createResult) && profilePhotoChanged) {
						try {
							String rootPhotoFolder = "D:\\Personal\\Development\\startup\\workspace\\projects\\LetsErasmus\\src\\main\\webapp\\user\\images\\";
							String userPhotoFolder = rootPhotoFolder + user.getId(); 
							(new File(FileUtil.concatPath(userPhotoFolder, "tmp"))).mkdirs();
							
							com.ucgen.letserasmus.library.file.model.FileModel photo = user.getProfilePhoto();
							
							String tmpPhotoPath = FileUtil.concatPath(userPhotoFolder, "tmp", photo.getId() + "." + EnmFileType.getFileType(photo.getFileType()).getFileSuffix());
							String newSmallFileName = AppUtil.getSmallUserPhotoName(user.getId(), photo.getId(), EnmFileType.getFileType(photo.getFileType()));
							String newLargeFileName = newSmallFileName.replace("small", "large");
							
							File tmpFile = new File(tmpPhotoPath);
							profilePhoto.transferTo(tmpFile);
							
							if (oldProfilePhotoId != null) {
								FileUtil.cleanDirectory(userPhotoFolder, false);
							}

							ImageUtil.resizeImage(tmpFile, FileUtil.concatPath(userPhotoFolder, newLargeFileName ), 400, 700);
							ImageUtil.resizeImage(tmpFile, FileUtil.concatPath(userPhotoFolder, newSmallFileName ), 300, 300);
							
							tmpFile.delete();
							
						} catch (Exception e) {
							System.out.println(CommonUtil.getExceptionMessage(e));
						}
					}
					this.setSessionAttribute(EnmSession.USER.getId(), this.userService.getUser(user));
					operationResult = createResult;
				} else {
					operationResult.setResultCode(EnmResultCode.ERROR.getValue());
					operationResult.setResultDesc("There is no user logged in!");
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
    
	private void processLogin(HttpSession session, User user, EnmLoginType loginType) {
		session.setAttribute(EnmSession.USER.getId(), user);
		session.setAttribute("login_type", loginType.getId());
	}
	
}
