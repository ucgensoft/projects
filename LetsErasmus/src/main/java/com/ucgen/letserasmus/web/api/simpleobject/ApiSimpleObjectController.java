package com.ucgen.letserasmus.web.api.simpleobject;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ucgen.common.operationresult.EnmResultCode;
import com.ucgen.common.operationresult.ListOperationResult;
import com.ucgen.common.util.CommonUtil;
import com.ucgen.letserasmus.library.common.enumeration.EnmErrorCode;
import com.ucgen.letserasmus.library.simpleobject.model.Country;
import com.ucgen.letserasmus.library.simpleobject.service.ISimpleObjectService;
import com.ucgen.letserasmus.library.user.model.User;
import com.ucgen.letserasmus.web.api.BaseApiController;
import com.ucgen.letserasmus.web.view.application.AppConstants;

@RestController
public class ApiSimpleObjectController extends BaseApiController {

	private ISimpleObjectService simpleObjectService;
	
	@Autowired
	public void setSimpleObjectService(ISimpleObjectService simpleObjectService) {
		this.simpleObjectService = simpleObjectService;
	}
	
	@RequestMapping(value = "/api/simpleobject/listcountry", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResponseEntity<ListOperationResult<Country>> listCountry(HttpSession session) {
		ListOperationResult<Country> operationResult = new ListOperationResult<Country>();
		try {
			User sessionUser = super.getSessionUser(session);
			if (sessionUser != null) {
				List<Country> countryList = this.simpleObjectService.listCountry();
				operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
				operationResult.setObjectList(countryList);
			} else {
				operationResult.setErrorCode(EnmErrorCode.USER_NOT_LOGGED_IN.getId());
				operationResult.setResultCode(EnmResultCode.ERROR.getValue());
				operationResult.setResultDesc(AppConstants.USER_NOT_LOGGED_IN);
			}
		} catch (Exception e) {
			operationResult.setResultCode(EnmResultCode.ERROR.getValue());
			operationResult.setResultDesc(CommonUtil.getExceptionMessage(e));
		}
		
		return new ResponseEntity<ListOperationResult<Country>>(operationResult, HttpStatus.OK);
    }
	
}
