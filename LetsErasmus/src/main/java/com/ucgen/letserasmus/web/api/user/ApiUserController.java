package com.ucgen.letserasmus.web.api.user;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ucgen.common.operationresult.EnmResultCode;
import com.ucgen.common.operationresult.OperationResult;
import com.ucgen.letserasmus.library.user.model.User;
import com.ucgen.letserasmus.web.api.BaseApiController;

@RestController
public class ApiUserController extends BaseApiController {

	@RequestMapping(value = "/api/user/login", method = RequestMethod.POST)
    public ResponseEntity<OperationResult> login(@RequestParam Map<String, String> requestParams, HttpSession session) {
		HttpStatus httpStatus = null;
		OperationResult operationResult = new OperationResult();
		operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
		operationResult.setResultDesc("Login iþlemi baþarýlý.");
		
		if (OperationResult.isResultSucces(operationResult)) {
			User user = new User();
			user.setEmail(requestParams.get("email"));
			session.setAttribute("USER", user);
		}
		
		httpStatus = HttpStatus.OK;
		return new ResponseEntity<OperationResult>(operationResult, httpStatus);
    }
	
}
