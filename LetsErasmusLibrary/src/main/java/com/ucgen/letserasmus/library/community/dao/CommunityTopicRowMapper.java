package com.ucgen.letserasmus.library.community.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.ucgen.common.dao.BaseRowMapper;
import com.ucgen.letserasmus.library.community.model.CommunityTopic;

public class CommunityTopicRowMapper extends BaseRowMapper<CommunityTopic> {

	public static final String COL_ID = "ID";
	public static final String COL_COMMUNITY_GROUP_ID = "COMMUNITY_GROUP_ID";
	public static final String COL_USER_ID = "USER_ID";
	public static final String COL_TITLE = "TITLE";
	public static final String COL_DESCRIPTION = "DESCRIPTION";
	public static final String COL_SUB_URL = "SUB_URL";	
		
	@Override
	public CommunityTopic mapRow(ResultSet rs, int rowNum) throws SQLException {
		super.initializeColSet(rs);
		CommunityTopic communityTopic = new CommunityTopic();
		
		communityTopic.setId(super.getLong(rs, COL_ID));
		communityTopic.setCommunityGroupId(super.getLong(rs, COL_COMMUNITY_GROUP_ID));
		communityTopic.setUserId(super.getLong(rs, COL_USER_ID));
		communityTopic.setTitle(super.getString(rs, COL_TITLE));
		communityTopic.setDescription(super.getString(rs, COL_DESCRIPTION));
		communityTopic.setSubUrl(super.getString(rs, COL_SUB_URL));
		communityTopic.setCreatedBy(super.getString(rs, COL_CREATED_BY));		
		communityTopic.setCreatedDate(super.getTimestamp(rs, COL_CREATED_DATE));
		communityTopic.setCreatedDateGmt(super.getTimestamp(rs, COL_CREATED_DATE_GMT));		
		communityTopic.setModifiedBy(super.getString(rs, COL_MODIFIED_BY));
		communityTopic.setModifiedDate(super.getTimestamp(rs, COL_MODIFIED_DATE));
		communityTopic.setModifiedDateGmt(super.getTimestamp(rs, COL_MODIFIED_DATE_GMT));		
		
		return communityTopic;
	}

	@Override
	public void fillFieldMaps() {
	}

	@Override
	public void initializeColList() {
	}

}
