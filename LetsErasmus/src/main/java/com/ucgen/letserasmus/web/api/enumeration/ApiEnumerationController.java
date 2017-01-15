package com.ucgen.letserasmus.web.api.enumeration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ucgen.common.operationresult.EnmResultCode;
import com.ucgen.common.operationresult.ValueOperationResult;
import com.ucgen.letserasmus.library.enumeration.model.Enumeration;
import com.ucgen.letserasmus.library.enumeration.service.IEnumerationService;
import com.ucgen.letserasmus.web.api.BaseApiController;

@RestController
public class ApiEnumerationController extends BaseApiController {

	private IEnumerationService enumerationService;
	
	@Autowired
	public void setEnumerationService(IEnumerationService enumerationService) {
		this.enumerationService = enumerationService;
	}
	
	@RequestMapping(value = "/api/enumeration/list", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResponseEntity<ValueOperationResult<Map<String, ArrayList<Enumeration>>>> listEnumeration(@RequestParam Map<String, String> requestParams) {
		HttpStatus httpStatus = null;
		String enumType = requestParams.get("enumType");
		
		Map<String, ArrayList<Enumeration>> enumerationMap = null;
		
		if (enumType != null) {
			enumerationMap = new HashMap<String, ArrayList<Enumeration>>();
			ArrayList<Enumeration> enumerationList = this.enumerationService.listEnumeration(new Enumeration(enumType));
			enumerationMap.put(enumType, enumerationList);
		} else {
			enumerationMap = this.enumerationService.listEnumeration();
		}
		
		ValueOperationResult<Map<String, ArrayList<Enumeration>>> listResult = new ValueOperationResult<>();
		listResult.setResultCode(EnmResultCode.SUCCESS.getValue());
		listResult.setResultValue(enumerationMap);
		
		httpStatus = HttpStatus.OK;
		
		return new ResponseEntity<ValueOperationResult<Map<String, ArrayList<Enumeration>>>>(listResult, httpStatus);
    }
	
}
