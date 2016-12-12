package com.ucgen.letserasmus.library.location.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import com.ucgen.common.operationresult.EnmResultCode;
import com.ucgen.common.operationresult.ListOperationResult;
import com.ucgen.letserasmus.library.location.dao.ILocationDao;
import com.ucgen.letserasmus.library.location.dao.LocationRowMapper;
import com.ucgen.letserasmus.library.location.model.Location;

@Repository
public class LocationDao extends JdbcDaoSupport implements ILocationDao {

	private static final String LIST_LOCATION_SQL = "SELECT ID, NAME, LATITUDE, LONGITUDE FROM LOCATION WHERE 1=1";
	
	@Autowired
	public LocationDao(DataSource dataSource) {
		super();
		super.setDataSource(dataSource);
	}	
	
	@Override
	public ListOperationResult<Location> listLocation(Location location) {
		return null;
	}

	@Override
	public ListOperationResult<Location> listLocation(List<Long> idList) {
		StringBuilder sqlBuilder = new StringBuilder(LIST_LOCATION_SQL);
		List<Object> argList = new ArrayList<Object>();
		
		if (idList != null && idList.size() > 0) {
			String strIdList = StringUtils.join(idList, ",");
			sqlBuilder.append(" AND ID IN(" + strIdList + ")");
			argList.addAll(idList);
		}
		
		List<Location> locationList = this.getJdbcTemplate().query(sqlBuilder.toString(), argList.toArray(), new LocationRowMapper());
		
		ListOperationResult<Location> operationResult = new ListOperationResult<Location>();
		operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
		operationResult.setObjectList(locationList);
		
		return operationResult;
	}

}
