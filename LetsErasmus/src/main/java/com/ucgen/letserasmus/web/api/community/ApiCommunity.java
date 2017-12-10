package com.ucgen.letserasmus.web.api.community;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ucgen.common.operationresult.EnmResultCode;
import com.ucgen.common.operationresult.ListOperationResult;
import com.ucgen.common.operationresult.OperationResult;
import com.ucgen.common.operationresult.ValueOperationResult;
import com.ucgen.common.util.CommonUtil;
import com.ucgen.common.util.FileLogger;
import com.ucgen.letserasmus.library.common.enumeration.EnmErrorCode;
import com.ucgen.letserasmus.library.community.model.CommunityGroup;
import com.ucgen.letserasmus.library.community.model.CommunityTopic;
import com.ucgen.letserasmus.library.community.service.ICommunityService;
import com.ucgen.letserasmus.library.user.model.User;
import com.ucgen.letserasmus.web.api.BaseApiController;
import com.ucgen.letserasmus.web.view.application.AppConstants;

@RestController
public class ApiCommunity extends BaseApiController {

	private ICommunityService communityService;
	
	@Autowired
	public void setCommunityService(ICommunityService communityService) {
		this.communityService = communityService;
	}

	@RequestMapping(value = "/api/community/group/list", method = RequestMethod.GET)
    public ResponseEntity<ValueOperationResult<List<CommunityGroup>>> listCommunityGroup(@RequestParam("countryId") Integer countryId, 
    		HttpSession session) {
		ValueOperationResult<List<CommunityGroup>> operationResult = new ValueOperationResult<List<CommunityGroup>>();
		try {
			if (countryId != null) {
				CommunityGroup communityGroup = new CommunityGroup();
				communityGroup.setCountryId(countryId);
				
				ListOperationResult<CommunityGroup> groupListResult = this.communityService.listCommunityGroup(communityGroup);
				
				if (OperationResult.isResultSucces(groupListResult)) {
					operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
					operationResult.setResultValue(groupListResult.getObjectList());
				} else {
					operationResult.setResultCode(EnmResultCode.ERROR.getValue());
					operationResult.setResultDesc(AppConstants.UNEXPECTED_ERROR);
				}
			} else {
				operationResult.setResultCode(EnmResultCode.ERROR.getValue());
				operationResult.setResultDesc(AppConstants.MISSING_MANDATORY_PARAM);
			}
		} catch (Exception e) {
			operationResult.setResultCode(EnmResultCode.EXCEPTION.getValue());
			operationResult.setResultDesc(AppConstants.LIST_OPERATION_FAIL);
			FileLogger.log(Level.ERROR, "ApiCommunity-listCommunityGroup()-Error: " + CommonUtil.getExceptionMessage(e));
		}
		return new ResponseEntity<ValueOperationResult<List<CommunityGroup>>>(operationResult, HttpStatus.OK);
    }
	
	@RequestMapping(value = "/api/community/topic/create", method = RequestMethod.POST)
    public ResponseEntity<OperationResult> createCommunityTopic(@RequestBody CommunityTopic communityTopic, HttpSession session) {
		OperationResult operationResult = new OperationResult();
		
		try {
			User user = super.getSessionUser(session);
			if (user != null) {
				if (communityTopic.getCommunityGroupId() != null 
						&& communityTopic.getTitle() != null && !communityTopic.getTitle().trim().isEmpty() 
						&& communityTopic.getDescription() != null && !communityTopic.getDescription().trim().isEmpty()) {
					String subUrl = communityTopic.getTitle().replaceAll("[^a-zA-Z0-9/]" , "-");
					communityTopic.setSubUrl(subUrl);
					communityTopic.setUserId(user.getId());
					communityTopic.setCreatedBy(user.getFullName());
					communityTopic.setCreatedDate(new Date());
					OperationResult createResult = this.communityService.createCommunityTopic(communityTopic);
					if (OperationResult.isResultSucces(createResult)) {
						operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
						operationResult.setResultDesc(AppConstants.COMMUNITY_CREATE_TOPIC_SUCCESS);
					} else {
						operationResult.setResultCode(EnmResultCode.ERROR.getValue());
						operationResult.setResultDesc(AppConstants.COMMUNITY_CREATE_TOPIC_FAIL);
					}
				} else {
					operationResult.setResultCode(EnmResultCode.ERROR.getValue());
					operationResult.setResultDesc(AppConstants.MISSING_MANDATORY_PARAM);
				}
			} else {
				operationResult.setErrorCode(EnmErrorCode.USER_NOT_LOGGED_IN.getId());
				operationResult.setResultCode(EnmResultCode.ERROR.getValue());
				operationResult.setResultDesc(AppConstants.USER_NOT_LOGGED_IN);
			}
		} catch (Exception e) {
			operationResult.setResultCode(EnmResultCode.EXCEPTION.getValue());
			operationResult.setResultDesc(AppConstants.CREATE_OPERATION_FAIL);
			FileLogger.log(Level.ERROR, "ApiCommunity-createCommunityTopic()-Error: " + CommonUtil.getExceptionMessage(e));
		}
		return new ResponseEntity<OperationResult>(operationResult, HttpStatus.OK);
    }
	
	@RequestMapping(value = "/api/community/topic/update", method = RequestMethod.POST)
    public ResponseEntity<OperationResult> updateCommunityTopic(@RequestBody CommunityTopic communityTopic, HttpSession session) {
		OperationResult operationResult = new OperationResult();
		
		try {
			User user = super.getSessionUser(session);
			if (user != null) {
				if (communityTopic.getId() != null
						&& communityTopic.getDescription() != null && !communityTopic.getDescription().trim().isEmpty()) {
					CommunityTopic dbCommunityTopic = this.communityService.getCommunityTopic(communityTopic.getId());
					if (dbCommunityTopic != null) {
						if (dbCommunityTopic.getUserId().equals(user.getId())) {
							dbCommunityTopic.setDescription(communityTopic.getDescription());
							communityTopic.setModifiedBy(user.getFullName());
							communityTopic.setModifiedDate(new Date());
							OperationResult createResult = this.communityService.updateCommunityTopic(communityTopic);
							if (OperationResult.isResultSucces(createResult)) {
								operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
								operationResult.setResultDesc(AppConstants.COMMUNITY_CREATE_TOPIC_SUCCESS);
							} else {
								operationResult.setResultCode(EnmResultCode.ERROR.getValue());
								operationResult.setResultDesc(AppConstants.COMMUNITY_CREATE_TOPIC_FAIL);
							}
						} else {
							operationResult.setResultCode(EnmResultCode.ERROR.getValue());
							operationResult.setResultDesc(AppConstants.UNAUTHORIZED_OPERATION);
						}
					} else {
						operationResult.setResultCode(EnmResultCode.ERROR.getValue());
						operationResult.setResultDesc(AppConstants.COMMUNITY_CREATE_TOPIC_NOT_FOUND);
					}
				} else {
					operationResult.setResultCode(EnmResultCode.ERROR.getValue());
					operationResult.setResultDesc(AppConstants.MISSING_MANDATORY_PARAM);
				}
			} else {
				operationResult.setErrorCode(EnmErrorCode.USER_NOT_LOGGED_IN.getId());
				operationResult.setResultCode(EnmResultCode.ERROR.getValue());
				operationResult.setResultDesc(AppConstants.USER_NOT_LOGGED_IN);
			}
		} catch (Exception e) {
			operationResult.setResultCode(EnmResultCode.EXCEPTION.getValue());
			operationResult.setResultDesc(AppConstants.CREATE_OPERATION_FAIL);
			FileLogger.log(Level.ERROR, "ApiCommunity-updateCommunityTopic()-Error: " + CommonUtil.getExceptionMessage(e));
		}
		return new ResponseEntity<OperationResult>(operationResult, HttpStatus.OK);
    }
	
}
