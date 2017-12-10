package com.ucgen.letserasmus.library.community.service.impl;

import org.apache.log4j.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ucgen.common.operationresult.ListOperationResult;
import com.ucgen.common.operationresult.OperationResult;
import com.ucgen.common.util.FileLogger;
import com.ucgen.letserasmus.library.community.dao.ICommunityDao;
import com.ucgen.letserasmus.library.community.model.CommunityGroup;
import com.ucgen.letserasmus.library.community.model.CommunityTopic;
import com.ucgen.letserasmus.library.community.model.CommunityTopicMessage;
import com.ucgen.letserasmus.library.community.service.ICommunityService;

@Service
public class CommunityService implements ICommunityService {

	private ICommunityDao communityDao;
	
	@Autowired
	public void setCommunityDao(ICommunityDao communityDao) {
		this.communityDao = communityDao;
	}

	@Override
	public ListOperationResult<CommunityGroup> listCommunityGroup(CommunityGroup communityGroup) {
		return this.communityDao.listCommunityGroup(communityGroup);
	}

	@Override
	public CommunityGroup getCommunityGroup(CommunityGroup communityGroup) {
		ListOperationResult<CommunityGroup> communityGroupResult = this.listCommunityGroup(communityGroup);
		if (OperationResult.isResultSucces(communityGroupResult)) {
			if (communityGroupResult.getObjectList() != null 
					&& communityGroupResult.getObjectList().size() > 0) {
				return communityGroupResult.getObjectList().get(0);
			} else {
				return null;
			}
		} else {
			FileLogger.log(Level.ERROR, "CommunityService-getCommunityGroup- Error: " + OperationResult.getResultDesc(communityGroupResult));
			return null;
		}
	}

	@Override
	public OperationResult createCommunityTopic(CommunityTopic communityTopic) {
		return this.communityDao.createCommunityTopic(communityTopic);
	}
	
	@Override
	public OperationResult updateCommunityTopic(CommunityTopic communityTopic) {
		return this.communityDao.updateCommunityTopic(communityTopic);
	}
	
	@Override
	public ListOperationResult<CommunityTopic> listCommunityTopic(CommunityTopic communityTopic) {
		return this.communityDao.listCommunityTopic(communityTopic);
	}

	@Override
	public CommunityTopic getCommunityTopic(Long id) {
		CommunityTopic communityTopic = new CommunityTopic();
		communityTopic.setId(id);
		ListOperationResult<CommunityTopic> communityTopicResult = this.listCommunityTopic(communityTopic);
		if (OperationResult.isResultSucces(communityTopicResult)) {
			if (communityTopicResult.getObjectList() != null 
					&& communityTopicResult.getObjectList().size() > 0) {
				return communityTopicResult.getObjectList().get(0);
			} else {
				return null;
			}
		} else {
			FileLogger.log(Level.ERROR, "CommunityService-getCommunityTopic- Error: " + OperationResult.getResultDesc(communityTopicResult));
			return null;
		}
	}
	
	@Override
	public ListOperationResult<CommunityTopicMessage> listCommunityTopicMessage(
			CommunityTopicMessage communityTopicMessage) {
		return this.communityDao.listCommunityTopicMessage(communityTopicMessage);
	}

	@Override
	public OperationResult createCommunityTopicMessage(CommunityTopicMessage communityTopicMessage) {
		return this.communityDao.createCommunityTopicMessage(communityTopicMessage);
	}

}
