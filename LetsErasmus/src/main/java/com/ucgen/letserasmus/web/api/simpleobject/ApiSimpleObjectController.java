package com.ucgen.letserasmus.web.api.simpleobject;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ucgen.common.operationresult.EnmResultCode;
import com.ucgen.common.operationresult.ListOperationResult;
import com.ucgen.common.util.CommonUtil;
import com.ucgen.common.util.FileLogger;
import com.ucgen.letserasmus.library.common.enumeration.EnmErrorCode;
import com.ucgen.letserasmus.library.parameter.enumeration.EnmParameter;
import com.ucgen.letserasmus.library.parameter.service.IParameterService;
import com.ucgen.letserasmus.library.simpleobject.model.Country;
import com.ucgen.letserasmus.library.simpleobject.model.Question;
import com.ucgen.letserasmus.library.simpleobject.model.QuestionGroup;
import com.ucgen.letserasmus.library.simpleobject.service.ISimpleObjectService;
import com.ucgen.letserasmus.library.user.model.User;
import com.ucgen.letserasmus.web.api.BaseApiController;
import com.ucgen.letserasmus.web.view.application.AppConstants;

@RestController
public class ApiSimpleObjectController extends BaseApiController {

	private ISimpleObjectService simpleObjectService;
	private IParameterService parameterService;
	
	@Autowired
	public void setParameterService(IParameterService parameterService) {
		this.parameterService = parameterService;
	}

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
			FileLogger.log(Level.ERROR, "ApiSimpleObjectController-listCountry()-Error: " + CommonUtil.getExceptionMessage(e));
			operationResult.setResultCode(EnmResultCode.ERROR.getValue());
			operationResult.setResultDesc(CommonUtil.getExceptionMessage(e));
		}
		
		return new ResponseEntity<ListOperationResult<Country>>(operationResult, HttpStatus.OK);
    }
	
	@RequestMapping(value = "/api/simpleobject/listquestiongroup", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResponseEntity<ListOperationResult<QuestionGroup>> listQuestionGroup(HttpSession session) {
		ListOperationResult<QuestionGroup> operationResult = new ListOperationResult<QuestionGroup>();
		try {
			List<QuestionGroup> questionGroupList = this.simpleObjectService.listQuestionGroup();
			operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
			operationResult.setObjectList(questionGroupList);
		} catch (Exception e) {
			FileLogger.log(Level.ERROR, "ApiSimpleObjectController-listQuestionGroup()-Error: " + CommonUtil.getExceptionMessage(e));
			operationResult.setResultCode(EnmResultCode.ERROR.getValue());
			operationResult.setResultDesc(CommonUtil.getExceptionMessage(e));
		}
		
		return new ResponseEntity<ListOperationResult<QuestionGroup>>(operationResult, HttpStatus.OK);
    }
	
	@RequestMapping(value = "/api/simpleobject/listquestion", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResponseEntity<ListOperationResult<Question>> listQuestion(@RequestParam("groupTitle") String groupTitle, @RequestParam("searchText") String searchText, HttpSession session) {
		ListOperationResult<Question> operationResult = new ListOperationResult<Question>();
		try {
			if ((groupTitle == null || groupTitle.trim().length() == 0) && (searchText == null || searchText.trim().length() == 0)) {
				groupTitle = this.parameterService.getParameterValue(EnmParameter.HELP_DEFAULT_GROUP.getId());
			}
			
			List<Question> questionList = this.simpleObjectService.listQuestion(groupTitle, searchText);
			operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
			operationResult.setObjectList(questionList);
		} catch (Exception e) {
			FileLogger.log(Level.ERROR, "ApiSimpleObjectController-listQuestion()-Error: " + CommonUtil.getExceptionMessage(e));
			operationResult.setResultCode(EnmResultCode.ERROR.getValue());
			operationResult.setResultDesc(CommonUtil.getExceptionMessage(e));
		}
		
		return new ResponseEntity<ListOperationResult<Question>>(operationResult, HttpStatus.OK);
    }
	
}
