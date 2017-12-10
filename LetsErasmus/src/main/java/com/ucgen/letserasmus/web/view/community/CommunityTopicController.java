package com.ucgen.letserasmus.web.view.community;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

import com.ucgen.common.operationresult.EnmResultCode;
import com.ucgen.common.operationresult.ListOperationResult;
import com.ucgen.common.operationresult.OperationResult;
import com.ucgen.letserasmus.library.common.enumeration.EnmErrorCode;
import com.ucgen.letserasmus.library.community.model.CommunityTopic;
import com.ucgen.letserasmus.library.community.model.CommunityTopicMessage;
import com.ucgen.letserasmus.library.community.service.ICommunityService;
import com.ucgen.letserasmus.web.view.BaseController;

@ManagedBean
public class CommunityTopicController extends BaseController {
	
	private static String PARAM_TOPIC_SUB_URL = "communityTopicSubUrl";
	private CommunityTopic communityTopic;
	
	@ManagedProperty(value="#{communityService}")
	private ICommunityService communityService;
	
	public void setCommunityService(ICommunityService communityService) {
		this.communityService = communityService;
	}

	@PostConstruct
	public void initialize() {
		String communityTopicSubUrl = super.getRequestParameter(PARAM_TOPIC_SUB_URL);
		if (communityTopicSubUrl != null && !communityTopicSubUrl.isEmpty()) {
			CommunityTopic communityTopic = new CommunityTopic();
			communityTopic.setSubUrl(communityTopicSubUrl);
			
			List<CommunityTopic> communityTopicList = this.communityService.listCommunityTopic(communityTopic).getObjectList();
			if (communityTopicList != null && communityTopicList.size() > 0) {
				this.communityTopic = communityTopicList.get(0);
				ListOperationResult<CommunityTopicMessage> messageListResult = this.communityService.listCommunityTopicMessage(new CommunityTopicMessage(null, this.communityTopic.getId()));
				if (OperationResult.isResultSucces(messageListResult)) {
					this.communityTopic.setMessageList(messageListResult.getObjectList());
				}
			} else {
				OperationResult operationResult = new OperationResult();
				operationResult.setResultCode(EnmResultCode.ERROR.getValue());
				operationResult.setResultDesc("Community Topic not found for sub url: " + communityTopicSubUrl);;
				operationResult.setErrorCode(EnmErrorCode.PAGE_NOT_FOUND.getId());
				//throw new OperationResultException(operationResult);
			}
		}
	}
		
}
