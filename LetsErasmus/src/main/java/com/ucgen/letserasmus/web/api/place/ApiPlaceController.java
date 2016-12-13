package com.ucgen.letserasmus.web.api.place;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ucgen.common.operationresult.EnmResultCode;
import com.ucgen.common.operationresult.ListOperationResult;
import com.ucgen.common.operationresult.OperationResult;
import com.ucgen.letserasmus.library.place.model.Place;
import com.ucgen.letserasmus.library.place.service.IPlaceService;
import com.ucgen.letserasmus.library.user.model.User;

@RestController
public class ApiPlaceController {

	private IPlaceService placeService;
	
	@Autowired
	public void setPlaceService(IPlaceService placeService) {
		this.placeService = placeService;
	}
	
	@RequestMapping(value = "/api/place/list", method = RequestMethod.GET)
    public ResponseEntity<ListOperationResult<Place>> listPlace(@RequestParam Map<String, String> requestParams) {
		HttpStatus httpStatus = null;
		ListOperationResult<Place> listResult = this.placeService.listPlace(null, true);
		if (OperationResult.isResultSucces(listResult)) {
			httpStatus = HttpStatus.OK;
		} else {
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<ListOperationResult<Place>>(listResult, httpStatus);
    }
	
	@RequestMapping(value = "/api/place/login", method = RequestMethod.POST)
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
