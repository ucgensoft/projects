package com.ucgen.letserasmus.library.community.dao;

import com.ucgen.common.operationresult.ListOperationResult;
import com.ucgen.letserasmus.library.community.model.CommunityGroup;

public interface ICommunityDao {

	public ListOperationResult<CommunityGroup> listCommunityGroup(CommunityGroup communityGroup);
	
}
