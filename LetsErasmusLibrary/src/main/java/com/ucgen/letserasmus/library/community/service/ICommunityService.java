package com.ucgen.letserasmus.library.community.service;

import com.ucgen.common.operationresult.ListOperationResult;
import com.ucgen.letserasmus.library.community.model.CommunityGroup;

public interface ICommunityService {

	public ListOperationResult<CommunityGroup> listCommunityGroup(CommunityGroup communityGroup);
	
	public CommunityGroup getCommunityGroup(CommunityGroup communityGroup);
	
}
