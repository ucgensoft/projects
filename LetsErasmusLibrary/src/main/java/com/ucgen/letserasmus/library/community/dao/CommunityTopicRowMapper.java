package com.ucgen.letserasmus.library.community.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.ucgen.common.dao.BaseRowMapper;
import com.ucgen.common.dao.EnmJoinType;
import com.ucgen.common.dao.ForeignKey;
import com.ucgen.letserasmus.library.community.model.CommunityGroup;
import com.ucgen.letserasmus.library.community.model.CommunityTopic;
import com.ucgen.letserasmus.library.file.dao.FileRowMapper;
import com.ucgen.letserasmus.library.file.model.FileModel;
import com.ucgen.letserasmus.library.location.dao.LocationRowMapper;
import com.ucgen.letserasmus.library.location.model.Location;
import com.ucgen.letserasmus.library.place.dao.PlaceRowMapper;
import com.ucgen.letserasmus.library.user.dao.UserRowMapper;
import com.ucgen.letserasmus.library.user.model.User;

public class CommunityTopicRowMapper extends BaseRowMapper<CommunityTopic> {

	public static final String TABLE_NAME = "COMMUNITY_TOPIC";
	
	public static final String COL_ID = "ID";
	public static final String COL_COMMUNITY_GROUP_ID = "COMMUNITY_GROUP_ID";
	public static final String COL_USER_ID = "USER_ID";
	public static final String COL_TITLE = "TITLE";
	public static final String COL_DESCRIPTION = "DESCRIPTION";
	public static final String COL_SUB_URL = "SUB_URL";	
	public static final String COL_LAST_ACTIVITY_DATE = "LAST_ACTIVITY_DATE";
		
	public static final String FKEY_USER = "USER";
	public static final String FKEY_GROUP = "GROUP";
	
	public CommunityTopicRowMapper() {
		this("CT");
	}
	
	public CommunityTopicRowMapper(String tablePrefix) {
		super(TABLE_NAME, tablePrefix);
	}
	
	public void addFKey(String keyName) {		
		if (FKEY_USER.equals(keyName)) {
			UserRowMapper userRowMapper = new UserRowMapper("U");
			ForeignKey<CommunityTopicRowMapper, UserRowMapper> fKeyUser = new ForeignKey<CommunityTopicRowMapper, UserRowMapper>(this, userRowMapper, EnmJoinType.LEFT);
			fKeyUser.addFieldPair(COL_USER_ID, LocationRowMapper.COL_ID);
			this.addFKey(FKEY_USER, fKeyUser);
		}
		if (FKEY_GROUP.equals(keyName)) {
			CommunityGroupRowMapper groupRowMapper = new CommunityGroupRowMapper("G");
			ForeignKey<CommunityTopicRowMapper, CommunityGroupRowMapper> fKeyGroup = new ForeignKey<CommunityTopicRowMapper, CommunityGroupRowMapper>(this, groupRowMapper, EnmJoinType.LEFT);
			fKeyGroup.addFieldPair(COL_COMMUNITY_GROUP_ID, LocationRowMapper.COL_ID);
			this.addFKey(FKEY_GROUP, fKeyGroup);
		}
	}
	
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
		communityTopic.setLastActivityDate(super.getTimestamp(rs, COL_LAST_ACTIVITY_DATE));
		communityTopic.setCreatedDate(super.getTimestamp(rs, COL_CREATED_DATE));
		communityTopic.setCreatedDateGmt(super.getTimestamp(rs, COL_CREATED_DATE_GMT));		
		communityTopic.setModifiedBy(super.getString(rs, COL_MODIFIED_BY));
		communityTopic.setModifiedDate(super.getTimestamp(rs, COL_MODIFIED_DATE));
		communityTopic.setModifiedDateGmt(super.getTimestamp(rs, COL_MODIFIED_DATE_GMT));		
		
		if (this.getfKeyMap() != null) {
			if (this.getfKeyMap().containsKey(FKEY_USER)) {
				ForeignKey<CommunityTopicRowMapper, UserRowMapper> fKey = this.getfKeyMap().get(FKEY_USER);
				User user = fKey.getDestMapper().mapRow(rs, rowNum);
				communityTopic.setUser(user);
			}
		}
		
		if (this.getfKeyMap() != null) {
			if (this.getfKeyMap().containsKey(FKEY_GROUP)) {
				ForeignKey<CommunityTopicRowMapper, CommunityGroupRowMapper> fKey = this.getfKeyMap().get(FKEY_GROUP);
				CommunityGroup group = fKey.getDestMapper().mapRow(rs, rowNum);
				communityTopic.setCommunityGroup(group);
			}
		}
		
		return communityTopic;
	}

	@Override
	public void fillFieldMaps() {
	}

	@Override
	public void initializeColList() {
		super.addColumn(COL_ID);
		super.addColumn(COL_COMMUNITY_GROUP_ID);
		super.addColumn(COL_USER_ID);
		super.addColumn(COL_TITLE);
		super.addColumn(COL_DESCRIPTION);
		super.addColumn(COL_SUB_URL);
		super.addColumn(COL_LAST_ACTIVITY_DATE);
		super.addColumn(COL_CREATED_DATE);
		super.addColumn(COL_MODIFIED_DATE);
	}

}
