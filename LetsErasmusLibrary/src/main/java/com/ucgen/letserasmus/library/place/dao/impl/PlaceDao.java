package com.ucgen.letserasmus.library.place.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import com.ucgen.common.dao.UtilityDao;
import com.ucgen.common.operationresult.EnmResultCode;
import com.ucgen.common.operationresult.ListOperationResult;
import com.ucgen.common.operationresult.OperationResult;
import com.ucgen.common.operationresult.ValueOperationResult;
import com.ucgen.common.util.StringUtil;
import com.ucgen.letserasmus.library.common.enumeration.EnmEntityType;
import com.ucgen.letserasmus.library.file.dao.FileRowMapper;
import com.ucgen.letserasmus.library.place.dao.IPlaceDao;
import com.ucgen.letserasmus.library.place.dao.PlaceRowMapper;
import com.ucgen.letserasmus.library.place.model.Place;

@Repository
public class PlaceDao extends JdbcDaoSupport implements IPlaceDao{

	private static final String LIST_PLACE_SQL = " SELECT P.ID, HOST_USER_ID, PLACE_TYPE_ID, HOME_TYPE_ID, TITLE, DESCRIPTION, STATUS, LOCATION_ID, " 
			+ " PRICE, BILLS_INCLUDE, DEPOSIT_PRICE, CURRENCY_ID, BED_NUMBER, BED_TYPE_ID, BATHROOM_NUMBER, BATHROOM_TYPE, " 
			+ " PLACE_MATE_NUMBER, PLACE_MATE_GENDER, GUEST_NUMBER, GUEST_GENDER, RULES, AMENTIES, SAFETY_AMENTIES, MINIMUM_STAY, " 
			+ " MAXIMUM_STAY, START_DATE, END_DATE, CANCELLATION_POLICY_ID, L.LATITUDE L_LATITUDE, L.LONGITUDE L_LONGITUDE" 
			+ " FROM PLACE P, LOCATION L WHERE P.LOCATION_ID = L.ID AND 1=1 ";
	
	private static final String INSERT_PLACE_SQL = " INSERT INTO PLACE(HOST_USER_ID, PLACE_TYPE_ID, HOME_TYPE_ID, TITLE, DESCRIPTION, STATUS, LOCATION_ID, PRICE, BILLS_INCLUDE, DEPOSIT_PRICE, CURRENCY_ID, BED_NUMBER, BED_TYPE_ID, BATHROOM_NUMBER, BATHROOM_TYPE, PLACE_MATE_NUMBER, PLACE_MATE_GENDER, GUEST_NUMBER, GUEST_GENDER, RULES, AMENTIES, SAFETY_AMENTIES, MINIMUM_STAY, MAXIMUM_STAY, START_DATE, END_DATE, CANCELLATION_POLICY_ID, CREATED_BY, CREATED_DATE, CREATED_DATE_GMT, MODIFIED_BY, MODIFIED_DATE, MODIFIED_DATE_GMT) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
	
	private static final String UPDATE_PLACE_SQL = " UPDATE PLACE SET $1 WHERE ID=? ";
	
	private UtilityDao utilityDao;
	
	@Autowired
	public PlaceDao(DataSource dataSource) {
		super();
		super.setDataSource(dataSource);
	}	
	
	@Autowired
	public void setUtilityDao(UtilityDao utilityDao) {
		this.utilityDao = utilityDao;
	}
	
	@Override
	public ValueOperationResult<Place> getPlace(Long id) {
		ValueOperationResult<Place> operationResult = new ValueOperationResult<Place>();
		Place place = new Place();
		place.setId(id);
		ListOperationResult<Place> listOperationResult = this.listPlace(place, true);
		
		operationResult.setResultCode(listOperationResult.getResultCode());
		operationResult.setResultDesc(listOperationResult.getResultDesc());
		
		if (OperationResult.isResultSucces(listOperationResult)) {
			if (listOperationResult.getObjectList() != null 
					&& listOperationResult.getObjectList().size() > 0) {
				operationResult.setResultValue(listOperationResult.getObjectList().get(0));
			}
		}
		
		return operationResult;
	}

	@Override
	public OperationResult insertPlace(Place place) {
		
		OperationResult operationResult = new OperationResult();
		
		List<Object> argList = new ArrayList<Object>();
		
		argList.add(place.getHostUserId());
		argList.add(place.getPlaceTypeId());
		argList.add(place.getHomeTypeId());
		argList.add(place.getTitle());
		argList.add(place.getDescription());
		argList.add(place.getStatus());
		argList.add(place.getLocationId());
		argList.add(place.getPrice());
		argList.add(place.getBillsInclude());
		argList.add(place.getDepositPrice());		
		argList.add(place.getCurrencyId());
		argList.add(place.getBedNumber());
		argList.add(place.getBedTypeId());
		argList.add(place.getBathRoomNumber());
		argList.add(place.getBathRoomType());
		argList.add(place.getPlaceMateNumber());
		argList.add(place.getPlaceMateGender());
		argList.add(place.getGuestNumber());
		argList.add(place.getGuestGender());
		argList.add(place.getRules());
		argList.add(place.getAmenties());
		argList.add(place.getSafetyAmenties());
		argList.add(place.getMinimumStay());
		argList.add(place.getMaximumStay());
		argList.add(place.getStartDate());
		argList.add(place.getEndDate());
		argList.add(place.getCancellationPolicyId());
		argList.add(place.getCreatedBy());
		argList.add(place.getCreatedDate());		
		argList.add(place.getCreatedDateGmt());
		argList.add(place.getModifiedBy());
		argList.add(place.getModifiedDate());
		argList.add(place.getModifiedDateGmt());
		
		this.getJdbcTemplate().update(INSERT_PLACE_SQL, argList.toArray());
		
		place.setId(this.utilityDao.getLastIncrementId());
		
		operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
						
		return operationResult;
	}

	@Override
	public ValueOperationResult<Integer> updatePlace(Place place) {
		
		ValueOperationResult<Integer> operationResult = new ValueOperationResult<Integer>();		
		List<Object> argList = new ArrayList<Object>();
		
		String updateSql = new String(UPDATE_PLACE_SQL);
		StringBuilder updateFields = new StringBuilder();
		
		//SET ID=?,HOST_USER_ID=?,PLACE_TYPE_ID=?,HOME_TYPE_ID=?,TITLE=?,DESCRIPTION=?,STATUS=?,LOCATION_ID=?, PRICE=?,BILLS_INCLUDE=?,DEPOSIT_PRICE=?,CURRENCY_ID=?,BED_NUMBER=?,BED_TYPE_ID=?,BATHROOM_NUMBER=?,BATHROOM_TYPE=?,PLACE_MATE_NUMBER=?,PLACE_MATE_GENDER=?,GUEST_NUMBER=?,GUEST_GENDER=?,RULES=?,AMENTIES=?,SAFETY_AMENTIES=?,MINIMUM_STAY=?,MAXIMUM_STAY=?,START_DATE=?,END_DATE=?,CANCELLATION_POLICY_ID=?,CREATED_BY=?,CREATED_DATE=?,CREATED_DATE_GMT=?,MODIFIED_BY=?,MODIFIED_DATE=?,MODIFIED_DATE_GMT=?
		
		if (place.getCoverPhotoId() != null) {
			StringUtil.append(updateFields, "COVER_PHOTO_ID = ?", ",");
			argList.add(place.getCoverPhotoId());
		}
		argList.add(place.getId());
		/*
		argList.add(place.getHostUserId());
		argList.add(place.getPlaceTypeId());
		argList.add(place.getHomeTypeId());
		argList.add(place.getTitle());
		argList.add(place.getDescription());
		argList.add(place.getStatus());
		argList.add(place.getLocationId());
		argList.add(place.getPrice());
		argList.add(place.getBillsInclude());
		argList.add(place.getDepositPrice());		
		argList.add(place.getCurrencyId());
		argList.add(place.getBedNumber());
		argList.add(place.getBedTypeId());
		argList.add(place.getBathRoomNumber());
		argList.add(place.getBathRoomType());
		argList.add(place.getPlaceMateNumber());
		argList.add(place.getPlaceMateGender());
		argList.add(place.getGuestNumber());
		argList.add(place.getGuestGender());
		argList.add(place.getRules());
		argList.add(place.getAmenties());
		argList.add(place.getSafetyAmenties());
		argList.add(place.getMinimumStay());
		argList.add(place.getMaximumStay());
		argList.add(place.getStartDate());
		argList.add(place.getEndDate());
		argList.add(place.getCancellationPolicyId());
		argList.add(place.getCreatedBy());
		argList.add(place.getCreatedDate());		
		argList.add(place.getCreatedDateGmt());
		argList.add(place.getModifiedBy());
		argList.add(place.getModifiedDate());
		argList.add(place.getModifiedDateGmt());
		*/
		updateSql = updateSql.replace("$1", updateFields);
		int updatedRowCount =  this.getJdbcTemplate().update(updateSql, argList.toArray() );
		
		operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
		operationResult.setResultValue(updatedRowCount);
		
		return operationResult;
	}

	@Override
	public ListOperationResult<Place> listPlace(Place place, boolean locationFlag) {
		ListOperationResult<Place> listOperationResult = new ListOperationResult<Place>();
		StringBuilder sqlBuilder = new StringBuilder();
		List<Object> argList = new ArrayList<Object>();
		
		PlaceRowMapper placeRowMapper = new PlaceRowMapper();
		placeRowMapper.addFKey(PlaceRowMapper.FKEY_LOCATION);
		placeRowMapper.addFKey(PlaceRowMapper.FKEY_FILE);
		placeRowMapper.addFKey(PlaceRowMapper.FKEY_USER);
		
		sqlBuilder.append(placeRowMapper.getSelectSqlWithForeignKeys());
		
		if (place != null) {
			if (place.getId() != null) {
				sqlBuilder.append(" AND " + placeRowMapper.getCriteriaColumnName(PlaceRowMapper.COL_ID) + " = ? ");
				argList.add(place.getId());
			}
		}
		
		sqlBuilder.append(" AND " + FileRowMapper.COL_ENTITY_TYPE + " = ? ");
		argList.add(EnmEntityType.PLACE.getValue());
		
		List<Place> fileList = super.getJdbcTemplate().query(sqlBuilder.toString(), argList.toArray(), placeRowMapper);		
		
		listOperationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
		listOperationResult.setObjectList(fileList);		
		
		return listOperationResult;
	}

}
