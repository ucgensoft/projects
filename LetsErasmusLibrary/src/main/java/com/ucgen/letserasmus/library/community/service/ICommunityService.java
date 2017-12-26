package com.ucgen.letserasmus.library.community.service;

import com.ucgen.common.operationresult.ListOperationResult;
import com.ucgen.common.operationresult.OperationResult;
import com.ucgen.letserasmus.library.community.model.CommunityGroup;
import com.ucgen.letserasmus.library.community.model.CommunityTopic;
import com.ucgen.letserasmus.library.community.model.CommunityTopicMessage;

public interface ICommunityService {

	public ListOperationResult<CommunityGroup> listCommunityGroup(CommunityGroup communityGroup);
	
	public CommunityGroup getCommunityGroup(CommunityGroup communityGroup);
	
	public ListOperationResult<CommunityTopic> listCommunityTopic(CommunityTopic communityTopic, boolean readUser, boolean readGroup);
	
	public ListOperationResult<CommunityTopic> listCommonTopic(boolean readUser, boolean readGroup);
	
	public CommunityTopic getCommunityTopic(Long id, boolean readUser, boolean readGroup);
	
	public OperationResult createCommunityTopic(CommunityTopic communityTopic);
	
	public OperationResult updateCommunityTopic(CommunityTopic communityTopic);
	
	public ListOperationResult<CommunityTopicMessage> listCommunityTopicMessage(CommunityTopicMessage communityTopicMessage, boolean readUser);

	public OperationResult createCommunityTopicMessage(CommunityTopicMessage communityTopicMessage);
	
}
