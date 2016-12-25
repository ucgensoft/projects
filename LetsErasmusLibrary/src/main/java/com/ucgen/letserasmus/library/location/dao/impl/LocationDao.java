package com.ucgen.letserasmus.library.location.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import com.ucgen.common.dao.UtilityDao;
import com.ucgen.common.operationresult.EnmResultCode;
import com.ucgen.common.operationresult.ListOperationResult;
import com.ucgen.common.operationresult.OperationResult;
import com.ucgen.letserasmus.library.location.dao.ILocationDao;
import com.ucgen.letserasmus.library.location.dao.LocationRowMapper;
import com.ucgen.letserasmus.library.location.model.Location;

@Repository
public class LocationDao extends JdbcDaoSupport implements ILocationDao {

	private static final String LIST_LOCATION_SQL = "SELECT ID, NAME, LATITUDE, LONGITUDE FROM LOCATION WHERE 1=1";
	private static final String INSERT_LOCATION_SQL = "INSERT INTO `LOCATION`(`USER_ADDRESS`, `NAME`, `LATITUDE`, `LONGITUDE`, "
			+ " `FORMATTED_ADDRESS`, `LOCATION_TYPE`, `ROUTE`, `STREET_NUMBER`, `POSTAL_CODE`, `LOCALITY`, `SUB_LOCALITY`, `COUNTRY`, "
			+ " `COUNTRY_CODE`, `STATE`, `REFERANCE`, `URL`, `WEBSITE`, `CREATED_BY`, `CREATED_DATE_GMT`) "
			+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	
	private UtilityDao utilityDao;
	
	@Autowired
	public LocationDao(DataSource dataSource) {
		super();
		super.setDataSource(dataSource);
	}
	
	@Autowired
	public void setUtilityDao(UtilityDao utilityDao) {
		this.utilityDao = utilityDao;
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

	@Override
	public OperationResult insertLocation(Location location) {
		OperationResult operationResult = new OperationResult();
		
		List<Object> argList = new ArrayList<Object>();
		
		argList.add(location.getUserAddress());
		argList.add(location.getName());
		argList.add(location.getLatitude());
		argList.add(location.getLongitude());
		argList.add(location.getFormattedAddress());
		argList.add(location.getLocationType());
		argList.add(location.getRoute());
		argList.add(location.getStreet());
		argList.add(location.getPostalCode());
		argList.add(location.getLocality());
		argList.add(location.getSubLocality());
		argList.add(location.getCountry());
		argList.add(location.getCountryCode());
		argList.add(location.getState());
		argList.add(null);
		argList.add(null);
		argList.add(null);
		argList.add(location.getCreatedBy());
		argList.add(location.getCreatedDateGmt());
		
		this.getJdbcTemplate().update(INSERT_LOCATION_SQL, argList.toArray());
		
		Long id = this.utilityDao.getLastIncrementId();
		location.setId(id);
		
		operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
		
		return operationResult;
	}

}
