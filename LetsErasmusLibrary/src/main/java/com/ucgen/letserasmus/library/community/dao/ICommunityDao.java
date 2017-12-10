package com.ucgen.letserasmus.library.community.dao;

import com.ucgen.common.operationresult.ListOperationResult;
import com.ucgen.common.operationresult.OperationResult;
import com.ucgen.letserasmus.library.community.model.CommunityGroup;
import com.ucgen.letserasmus.library.community.model.CommunityTopic;
import com.ucgen.letserasmus.library.community.model.CommunityTopicMessage;

public interface ICommunityDao {

	public ListOperationResult<CommunityGroup> listCommunityGroup(CommunityGroup communityGroup);
	
	public ListOperationResult<CommunityTopic> listCommunityTopic(CommunityTopic communityTopic);
	
	public OperationResult createCommunityTopic(CommunityTopic communityTopic);
	
	public OperationResult updateCommunityTopic(CommunityTopic communityTopic);
	
	public ListOperationResult<CommunityTopicMessage> listCommunityTopicMessage(CommunityTopicMessage communityTopicMessage);
	
	public OperationResult createCommunityTopicMessage(CommunityTopicMessage communityTopicMessage);
	
}
