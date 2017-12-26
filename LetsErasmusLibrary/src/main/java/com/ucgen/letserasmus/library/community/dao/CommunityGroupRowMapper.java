package com.ucgen.letserasmus.library.community.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.ucgen.common.dao.BaseRowMapper;
import com.ucgen.letserasmus.library.community.model.CommunityGroup;

public class CommunityGroupRowMapper extends BaseRowMapper<CommunityGroup> {

	public static final String TABLE_NAME = "COMMUNITY_GROUP";
	
	public static final String COL_ID = "ID";
	public static final String COL_NAME = "NAME";
	public static final String COL_COUNTRY_ID = "COUNTRY_ID";
	public static final String COL_CITY_ID = "CITY_ID";
	public static final String COL_UNIVERSITY_ID = "UNIVERSITY_ID";
	public static final String COL_SUB_URL = "SUB_URL";
	
	public CommunityGroupRowMapper() {
		this(null);
	}
	
	public CommunityGroupRowMapper(String tablePrefix) {
		super(TABLE_NAME, tablePrefix);
	}
	
	@Override
	public CommunityGroup mapRow(ResultSet rs, int rowNum) throws SQLException {
		super.initializeColSet(rs);
		CommunityGroup communityGroup = new CommunityGroup();
		
		communityGroup.setId(super.getLong(rs, COL_ID));
		communityGroup.setName(super.getString(rs, COL_NAME));
		communityGroup.setCountryId(super.getInteger(rs, COL_COUNTRY_ID));
		communityGroup.setCityId(super.getInteger(rs, COL_CITY_ID));
		communityGroup.setUniversityId(super.getInteger(rs, COL_UNIVERSITY_ID));
		communityGroup.setSubUrl(super.getString(rs, COL_SUB_URL));
		communityGroup.setCreatedBy(super.getString(rs, COL_CREATED_BY));		
		communityGroup.setCreatedDate(super.getTimestamp(rs, COL_CREATED_DATE));
		communityGroup.setCreatedDateGmt(super.getTimestamp(rs, COL_CREATED_DATE_GMT));		
		communityGroup.setModifiedBy(super.getString(rs, COL_MODIFIED_BY));
		communityGroup.setModifiedDate(super.getTimestamp(rs, COL_MODIFIED_DATE));
		communityGroup.setModifiedDateGmt(super.getTimestamp(rs, COL_MODIFIED_DATE_GMT));		
		
		return communityGroup;
	}

	@Override
	public void fillFieldMaps() {
	}

	@Override
	public void initializeColList() {
		super.addColumn(COL_ID);
		super.addColumn(COL_NAME);
		super.addColumn(COL_COUNTRY_ID);
		super.addColumn(COL_CITY_ID);
		super.addColumn(COL_UNIVERSITY_ID);
		super.addColumn(COL_SUB_URL);
	}

}
