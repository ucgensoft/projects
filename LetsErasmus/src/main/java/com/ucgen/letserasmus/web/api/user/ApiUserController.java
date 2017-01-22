package com.ucgen.letserasmus.web.api.user;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ucgen.common.operationresult.EnmResultCode;
import com.ucgen.common.operationresult.OperationResult;
import com.ucgen.letserasmus.library.common.enumeration.EnmBoolStatus;
import com.ucgen.letserasmus.library.user.enumeration.EnmLoginType;
import com.ucgen.letserasmus.library.user.enumeration.EnmUserStatus;
import com.ucgen.letserasmus.library.user.model.User;
import com.ucgen.letserasmus.library.user.service.IUserService;
import com.ucgen.letserasmus.web.api.BaseApiController;

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
				} else if (loginType == EnmLoginType.LOCAL_ACCOUNT) {
					operationResult = this.loginWithGoogleAccount(user, session);
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
	
	private void processLogin(HttpSession session, User user, EnmLoginType loginType) {
		session.setAttribute("user", user);
		session.setAttribute("login_type", loginType.getId());
	}
	
}
