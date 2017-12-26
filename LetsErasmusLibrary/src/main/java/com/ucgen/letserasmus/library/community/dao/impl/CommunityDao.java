package com.ucgen.letserasmus.library.community.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import com.ucgen.common.operationresult.EnmResultCode;
import com.ucgen.common.operationresult.ListOperationResult;
import com.ucgen.common.operationresult.OperationResult;
import com.ucgen.letserasmus.library.community.dao.CommunityGroupRowMapper;
import com.ucgen.letserasmus.library.community.dao.CommunityTopicMessageRowMapper;
import com.ucgen.letserasmus.library.community.dao.CommunityTopicRowMapper;
import com.ucgen.letserasmus.library.community.dao.ICommunityDao;
import com.ucgen.letserasmus.library.community.dao.ICommunityDaoConstants;
import com.ucgen.letserasmus.library.community.model.CommunityGroup;
import com.ucgen.letserasmus.library.community.model.CommunityTopic;
import com.ucgen.letserasmus.library.community.model.CommunityTopicMessage;

@Repository
public class CommunityDao extends JdbcDaoSupport implements ICommunityDao, ICommunityDaoConstants {

	@Autowired
	public CommunityDao(DataSource dataSource) {
		super();
		super.setDataSource(dataSource);
	}

	@Override
	public ListOperationResult<CommunityGroup> listCommunityGroup(CommunityGroup communityGroup) {
		StringBuilder sqlBuilder = new StringBuilder(LIST_COMMUNITY_GROUP_SQL);
		List<Object> argList = new ArrayList<Object>();
		if (communityGroup.getId() != null) {
			sqlBuilder.append(" AND ID = ?");
			argList.add(communityGroup.getId());
		}
		
		if (communityGroup.getSubUrl() != null) {
			sqlBuilder.append(" AND SUB_URL = ?");
			argList.add(communityGroup.getSubUrl());
		}
		
		if (communityGroup.getCountryId() != null) {
			sqlBuilder.append(" AND COUNTRY_ID = ?");
			argList.add(communityGroup.getCountryId());
		}
		
		List<CommunityGroup> groupList = this.getJdbcTemplate().query(sqlBuilder.toString(), argList.toArray(), new CommunityGroupRowMapper());
		
		ListOperationResult<CommunityGroup> groupListResult = new ListOperationResult<CommunityGroup>();
		
		groupListResult.setResultCode(EnmResultCode.SUCCESS.getValue());
		groupListResult.setObjectList(groupList);
		
		return groupListResult;
	}

	@Override
	public ListOperationResult<CommunityTopic> listCommunityTopic(CommunityTopic communityTopic, boolean readUser, boolean readGroup) {
		StringBuilder sqlBuilder = new StringBuilder();
		List<Object> argList = new ArrayList<Object>();
		
		CommunityTopicRowMapper communityTopicRowMapper = new CommunityTopicRowMapper("CT");
		if (readUser) {
			communityTopicRowMapper.addFKey(CommunityTopicRowMapper.FKEY_USER);
		}
		if (readGroup) {
			communityTopicRowMapper.addFKey(CommunityTopicRowMapper.FKEY_GROUP);
		}
		
		sqlBuilder.append(communityTopicRowMapper.getSelectSqlWithForeignKeys());
		
		if (communityTopic.getId() != null) {
			sqlBuilder.append(" AND CT.ID = ?");
			argList.add(communityTopic.getId());
		}
		
		if (communityTopic.getSubUrl() != null) {
			sqlBuilder.append(" AND CT.SUB_URL = ?");
			argList.add(communityTopic.getSubUrl());
		}
		
		if (communityTopic.getCommunityGroupId() != null) {
			sqlBuilder.append(" AND CT.COMMUNITY_GROUP_ID = ?");
			argList.add(communityTopic.getCommunityGroupId());
		}
		
		sqlBuilder.append(" ORDER BY IF(CT.MODIFIED_DATE IS NOT NULL, CT.MODIFIED_DATE, CT.CREATED_DATE) DESC");
		
		List<CommunityTopic> topicList = this.getJdbcTemplate().query(sqlBuilder.toString(), argList.toArray(), communityTopicRowMapper);
		
		ListOperationResult<CommunityTopic> topicListResult = new ListOperationResult<CommunityTopic>();
		
		topicListResult.setResultCode(EnmResultCode.SUCCESS.getValue());
		topicListResult.setObjectList(topicList);
		
		return topicListResult;
	}
	
	@Override
	public ListOperationResult<CommunityTopic> listCommonTopic(boolean readUser, boolean readGroup) {
		StringBuilder sqlBuilder = new StringBuilder();
		List<Object> argList = new ArrayList<Object>();
		
		CommunityTopicRowMapper communityTopicRowMapper = new CommunityTopicRowMapper("CT");
		if (readUser) {
			communityTopicRowMapper.addFKey(CommunityTopicRowMapper.FKEY_USER);
		}
		if (readGroup) {
			communityTopicRowMapper.addFKey(CommunityTopicRowMapper.FKEY_GROUP);
		}
		
		sqlBuilder.append(communityTopicRowMapper.getSelectSqlWithForeignKeys());
				
		sqlBuilder.append(" AND CT.COMMUNITY_GROUP_ID IN (SELECT ID FROM COMMUNITY_GROUP WHERE COUNTRY_ID IS NULL)");
		
		sqlBuilder.append(" ORDER BY IF(CT.MODIFIED_DATE IS NOT NULL, CT.MODIFIED_DATE, CT.CREATED_DATE) DESC");
		
		List<CommunityTopic> topicList = this.getJdbcTemplate().query(sqlBuilder.toString(), argList.toArray(), communityTopicRowMapper);
		
		ListOperationResult<CommunityTopic> topicListResult = new ListOperationResult<CommunityTopic>();
		
		topicListResult.setResultCode(EnmResultCode.SUCCESS.getValue());
		topicListResult.setObjectList(topicList);
		
		return topicListResult;
	}
	
	@Override
	public CommunityTopic getCommunityTopic(Long id, boolean readUser, boolean readGroup) {
		CommunityTopic communityTopic = new CommunityTopic();
		communityTopic.setId(id);
		
		List<CommunityTopic> topicList = this.listCommunityTopic(communityTopic, readUser, readGroup).getObjectList();
		if (topicList != null && topicList.size() > 0) {
			return topicList.get(0);
		} else {
			return null;
		}
	}

	@Override
	public OperationResult createCommunityTopic(CommunityTopic communityTopic) {
		List<Object> argList = new ArrayList<Object>();
		
		argList.add(communityTopic.getCommunityGroupId());
		argList.add(communityTopic.getUserId());
		argList.add(communityTopic.getTitle());
		argList.add(communityTopic.getDescription());
		argList.add(communityTopic.getSubUrl());
		argList.add(communityTopic.getLastActivityDate());
		argList.add(communityTopic.getCreatedBy());
		argList.add(communityTopic.getCreatedDate());
		
		this.getJdbcTemplate().update(CREATE_COMMUNITY_TOPIC_SQL, argList.toArray());
		
		OperationResult operationResult = new OperationResult();
		operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
		
		return operationResult;
	}
	
	@Override
	public OperationResult updateCommunityTopic(CommunityTopic communityTopic) {
		List<Object> argList = new ArrayList<Object>();
		
		argList.add(communityTopic.getDescription());
		argList.add(communityTopic.getModifiedBy());
		argList.add(communityTopic.getModifiedDate());
		argList.add(communityTopic.getId());
		
		this.getJdbcTemplate().update(UPDATE_COMMUNITY_TOPIC_SQL, argList.toArray());
		
		OperationResult operationResult = new OperationResult();
		operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
		
		return operationResult;
	}
	
	@Override
	public ListOperationResult<CommunityTopicMessage> listCommunityTopicMessage(CommunityTopicMessage communityTopicMessage, boolean readUser) {
		StringBuilder sqlBuilder = new StringBuilder();
		List<Object> argList = new ArrayList<Object>();
		
		CommunityTopicMessageRowMapper communityTopicMessageRowMapper = new CommunityTopicMessageRowMapper("CTM");
		if (readUser) {
			communityTopicMessageRowMapper.addFKey(CommunityTopicRowMapper.FKEY_USER);
		}
		
		sqlBuilder.append(communityTopicMessageRowMapper.getSelectSqlWithForeignKeys());
		
		if (communityTopicMessage.getId() != null) {
			sqlBuilder.append(" AND CTM.ID = ?");
			argList.add(communityTopicMessage.getId());
		}
		
		if (communityTopicMessage.getCommunityTopicId() != null) {
			sqlBuilder.append(" AND CTM.COMMUNITY_TOPIC_ID = ?");
			argList.add(communityTopicMessage.getCommunityTopicId());
		}
		
		sqlBuilder.append(" ORDER BY CTM.CREATED_DATE");
		
		List<CommunityTopicMessage> messageList = this.getJdbcTemplate().query(sqlBuilder.toString(), argList.toArray(), communityTopicMessageRowMapper);
		
		ListOperationResult<CommunityTopicMessage> messageListResult = new ListOperationResult<CommunityTopicMessage>();
		
		messageListResult.setResultCode(EnmResultCode.SUCCESS.getValue());
		messageListResult.setObjectList(messageList);
		
		return messageListResult;
	}

	@Override
	public OperationResult createCommunityTopicMessage(CommunityTopicMessage communityTopicMessage) {
		List<Object> argList = new ArrayList<Object>();
		
		argList.add(communityTopicMessage.getCommunityTopicId());
		argList.add(communityTopicMessage.getUserId());
		argList.add(communityTopicMessage.getDescription());
		argList.add(communityTopicMessage.getCreatedBy());
		argList.add(communityTopicMessage.getCreatedDate());
		
		this.getJdbcTemplate().update(CREATE_COMMUNITY_TOPIC_MESSAGE_SQL, argList.toArray());
		
		CommunityTopic communityTopic = this.getCommunityTopic(communityTopicMessage.getCommunityTopicId(), false, false);
		if (communityTopic != null) {
			communityTopic.setLastActivityDate(communityTopicMessage.getCreatedDate());
			communityTopic.setModifiedDate(communityTopicMessage.getCreatedDate());
			communityTopic.setModifiedBy(communityTopicMessage.getCreatedBy());
			this.updateCommunityTopic(communityTopic);
		}
		
		OperationResult operationResult = new OperationResult();
		operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
		
		return operationResult;
	}
	
}
