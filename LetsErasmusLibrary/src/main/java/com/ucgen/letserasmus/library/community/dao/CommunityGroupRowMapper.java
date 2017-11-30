package com.ucgen.letserasmus.library.community.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.ucgen.common.dao.BaseRowMapper;
import com.ucgen.letserasmus.library.community.model.CommunityGroup;

public class CommunityGroupRowMapper extends BaseRowMapper<CommunityGroup> {

	public static final String COL_ID = "ID";
	public static final String COL_NAME = "NAME";
	public static final String COL_COUNTRY_ID = "COUNTRY_ID";
	public static final String COL_CITY_ID = "CITY_ID";
	public static final String COL_UNIVERSITY_ID = "UNIVERSITY_ID";
	
	@Override
	public CommunityGroup mapRow(ResultSet rs, int rowNum) throws SQLException {
		super.initializeColSet(rs);
		CommunityGroup communityGroup = new CommunityGroup();
		
		communityGroup.setId(super.getInteger(rs, COL_ID));
		communityGroup.setName(super.getString(rs, COL_NAME));
		communityGroup.setCountryId(super.getInteger(rs, COL_COUNTRY_ID));
		communityGroup.setCityId(super.getInteger(rs, COL_CITY_ID));
		communityGroup.setUniversityId(super.getInteger(rs, COL_UNIVERSITY_ID));
		
		return communityGroup;
	}

	@Override
	public void fillFieldMaps() {
	}

	@Override
	public void initializeColList() {
	}

}
