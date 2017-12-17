package com.ucgen.letserasmus.library.community.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.ucgen.common.dao.BaseRowMapper;
import com.ucgen.common.dao.EnmJoinType;
import com.ucgen.common.dao.ForeignKey;
import com.ucgen.letserasmus.library.community.model.CommunityTopicMessage;
import com.ucgen.letserasmus.library.location.dao.LocationRowMapper;
import com.ucgen.letserasmus.library.user.dao.UserRowMapper;
import com.ucgen.letserasmus.library.user.model.User;

public class CommunityTopicMessageRowMapper extends BaseRowMapper<CommunityTopicMessage> {

	public static final String TABLE_NAME = "COMMUNITY_TOPIC_MESSAGE";
	
	public static final String COL_ID = "ID";
	public static final String COL_COMMUNITY_TOPIC_ID = "COMMUNITY_TOPIC_ID";
	public static final String COL_USER_ID = "USER_ID";
	public static final String COL_DESCRIPTION = "DESCRIPTION";
		
	public static final String FKEY_USER = "USER";
	
	public CommunityTopicMessageRowMapper() {
		this("CTM");
	}
	
	public CommunityTopicMessageRowMapper(String tablePrefix) {
		super(TABLE_NAME, tablePrefix);
	}
	
	public void addFKey(String keyName) {		
		if (FKEY_USER.equals(keyName)) {
			UserRowMapper userRowMapper = new UserRowMapper("U");
			ForeignKey<CommunityTopicMessageRowMapper, UserRowMapper> fKeyUser = new ForeignKey<CommunityTopicMessageRowMapper, UserRowMapper>(this, userRowMapper, EnmJoinType.LEFT);
			fKeyUser.addFieldPair(COL_USER_ID, LocationRowMapper.COL_ID);
			this.addFKey(FKEY_USER, fKeyUser);
		}
	}
	
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
		
		if (this.getfKeyMap() != null) {
			if (this.getfKeyMap().containsKey(FKEY_USER)) {
				ForeignKey<CommunityTopicMessageRowMapper, UserRowMapper> fKey = this.getfKeyMap().get(FKEY_USER);
				User user = fKey.getDestMapper().mapRow(rs, rowNum);
				communityTopicMessage.setUser(user);
			}
		}
		
		return communityTopicMessage;
	}

	@Override
	public void fillFieldMaps() {
	}

	@Override
	public void initializeColList() {
		super.addColumn(COL_ID);
		super.addColumn(COL_COMMUNITY_TOPIC_ID);
		super.addColumn(COL_CREATED_DATE);
		super.addColumn(COL_DESCRIPTION);
		super.addColumn(COL_MODIFIED_DATE);
		super.addColumn(COL_USER_ID);
	}

}
