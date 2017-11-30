package com.ucgen.letserasmus.library.community.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import com.ucgen.common.operationresult.ListOperationResult;
import com.ucgen.letserasmus.library.community.dao.CommunityGroupRowMapper;
import com.ucgen.letserasmus.library.community.dao.ICommunityDao;
import com.ucgen.letserasmus.library.community.dao.ICommunityDaoConstants;
import com.ucgen.letserasmus.library.community.model.CommunityGroup;

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
		
		if (communityGroup.getCountryId() != null) {
			sqlBuilder.append(" AND COUNTRY_ID = ?");
			argList.add(communityGroup.getCountryId());
		}
		
		List<CommunityGroup> groupList = this.getJdbcTemplate().query(sqlBuilder.toString(), argList.toArray(), new CommunityGroupRowMapper());
		
		ListOperationResult<CommunityGroup> groupListResult = new ListOperationResult<CommunityGroup>();
		
		groupListResult.setObjectList(groupList);
		
		return groupListResult;
	}
	
}
