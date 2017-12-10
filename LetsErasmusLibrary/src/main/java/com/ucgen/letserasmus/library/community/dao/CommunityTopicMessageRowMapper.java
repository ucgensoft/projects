package com.ucgen.letserasmus.library.community.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.ucgen.common.dao.BaseRowMapper;
import com.ucgen.letserasmus.library.community.model.CommunityTopicMessage;

public class CommunityTopicMessageRowMapper extends BaseRowMapper<CommunityTopicMessage> {

	public static final String COL_ID = "ID";
	public static final String COL_COMMUNITY_TOPIC_ID = "COMMUNITY_TOPIC_ID";
	public static final String COL_USER_ID = "USER_ID";
	public static final String COL_DESCRIPTION = "DESCRIPTION";
		
	@Override
	public CommunityTopicMessage mapRow(ResultSet rs, int rowNum) throws SQLException {
		super.initializeColSet(rs);
		CommunityTopicMessage communityTopicMessage = new CommunityTopicMessage();
		
		communityTopicMessage.setId(super.getLong(rs, COL_ID));
		communityTopicMessage.setCommunityTopicId(super.getLong(rs, COL_COMMUNITY_TOPIC_ID));
		communityTopicMessage.setUserId(super.getLong(rs, COL_USER_ID));
		communityTopicMessage.setDescription(super.getString(rs, COL_DESCRIPTION));
		communityTopicMessage.setCreatedBy(super.getString(rs, COL_CREATED_BY));		
		communityTopicMessage.setCreatedDate(super.getTimestamp(rs, COL_CREATED_DATE));
		communityTopicMessage.setCreatedDateGmt(super.getTimestamp(rs, COL_CREATED_DATE_GMT));		
		communityTopicMessage.setModifiedBy(super.getString(rs, COL_MODIFIED_BY));
		communityTopicMessage.setModifiedDate(super.getTimestamp(rs, COL_MODIFIED_DATE));
		communityTopicMessage.setModifiedDateGmt(super.getTimestamp(rs, COL_MODIFIED_DATE_GMT));		
		
		return communityTopicMessage;
	}

	@Override
	public void fillFieldMaps() {
	}

	@Override
	public void initializeColList() {
	}

}
