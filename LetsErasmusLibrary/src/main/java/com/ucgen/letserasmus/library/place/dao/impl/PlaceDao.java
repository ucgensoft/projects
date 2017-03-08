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
		ListOperationResult<Place> listOperationResult = this.listPlace(place, true, true, true);
		
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
						
		if (place.getPlaceTypeId() != null) {
			StringUtil.append(updateFields, "PLACE_TYPE_ID = ?", ",");
			argList.add(place.getPlaceTypeId());
		}
		
		if (place.getHomeTypeId() != null) {
			StringUtil.append(updateFields, "HOME_TYPE_ID = ?", ",");
			argList.add(place.getHomeTypeId());
		}
		
		if (place.getPrice() != null) {
			StringUtil.append(updateFields, "PRICE = ?", ",");
			argList.add(place.getPrice());
		}
		
		if (place.getBillsInclude() != null) {
			StringUtil.append(updateFields, "BILLS_INCLUDE = ?", ",");
			argList.add(place.getBillsInclude());
		}
		
		if (place.getDepositPrice() != null) {
			StringUtil.append(updateFields, "DEPOSIT_PRICE = ?", ",");
			argList.add(place.getDepositPrice());
		}
		
		if (place.getCurrencyId() != null) {
			StringUtil.append(updateFields, "CURRENCY_ID = ?", ",");
			argList.add(place.getCurrencyId());
		}
		
		if (place.getBedNumber() != null) {
			StringUtil.append(updateFields, "BED_NUMBER = ?", ",");
			argList.add(place.getBedNumber());
		}
		
		if (place.getBedTypeId() != null) {
			StringUtil.append(updateFields, "BED_TYPE_ID = ?", ",");
			argList.add(place.getBedTypeId());
		}
		
		if (place.getBathRoomNumber() != null) {
			StringUtil.append(updateFields, "BATHROOM_NUMBER = ?", ",");
			argList.add(place.getBathRoomNumber());
		}
		
		if (place.getBathRoomType() != null) {
			StringUtil.append(updateFields, "BATHROOM_TYPE = ?", ",");
			argList.add(place.getBathRoomType());
		}
		
		if (place.getPlaceMateGender() != null) {
			StringUtil.append(updateFields, "PLACE_MATE_GENDER = ?", ",");
			argList.add(place.getPlaceMateGender());
		}
		
		if (place.getPlaceMateNumber() != null) {
			StringUtil.append(updateFields, "PLACE_MATE_NUMBER = ?", ",");
			argList.add(place.getPlaceMateNumber());
		}
		
		if (place.getGuestGender() != null) {
			StringUtil.append(updateFields, "GUEST_GENDER = ?", ",");
			argList.add(place.getGuestGender());
		}
		
		if (place.getGuestNumber() != null) {
			StringUtil.append(updateFields, "GUEST_NUMBER = ?", ",");
			argList.add(place.getGuestNumber());
		}
		
		if (place.getAmenties() != null) {
			StringUtil.append(updateFields, "AMENTIES = ?", ",");
			argList.add(place.getAmenties());
		}
		
		if (place.getSafetyAmenties() != null) {
			StringUtil.append(updateFields, "SAFETY_AMENTIES = ?", ",");
			argList.add(place.getSafetyAmenties());
		}
		
		if (place.getMinimumStay() != null) {
			StringUtil.append(updateFields, "MINIMUM_STAY = ?", ",");
			argList.add(place.getMinimumStay());
		}
		
		if (place.getMaximumStay() != null) {
			StringUtil.append(updateFields, "MAXIMUM_STAY = ?", ",");
			argList.add(place.getMaximumStay());
		}
		
		if (place.getStartDate() != null) {
			StringUtil.append(updateFields, "START_DATE = ?", ",");
			argList.add(place.getStartDate());
		}
		
		if (place.getEndDate() != null) {
			StringUtil.append(updateFields, "END_DATE = ?", ",");
			argList.add(place.getEndDate());
		}
		
		if (place.getRules() != null) {
			StringUtil.append(updateFields, "RULES = ?", ",");
			argList.add(place.getRules());
		}
		
		if (place.getCoverPhotoId() != null) {
			StringUtil.append(updateFields, "COVER_PHOTO_ID = ?", ",");
			argList.add(place.getCoverPhotoId());
		}
		
		if (place.getStatus() != null) {
			StringUtil.append(updateFields, "STATUS = ?", ",");
			argList.add(place.getStatus());
		}
		
		if (place.getTitle() != null) {
			StringUtil.append(updateFields, "TITLE = ?", ",");
			argList.add(place.getTitle());
		}
		
		if (place.getDescription() != null) {
			StringUtil.append(updateFields, "DESCRIPTION = ?", ",");
			argList.add(place.getDescription());
		}
		
		if (place.getModifiedBy() != null) {
			StringUtil.append(updateFields, "MODIFIED_BY = ?", ",");
			argList.add(place.getModifiedBy());
		}
		
		if (place.getModifiedDate() != null) {
			StringUtil.append(updateFields, "MODIFIED_DATE = ?", ",");
			argList.add(place.getModifiedDate());
		}
		
		argList.add(place.getId());

		updateSql = updateSql.replace("$1", updateFields);
		int updatedRowCount =  this.getJdbcTemplate().update(updateSql, argList.toArray() );
		
		operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
		operationResult.setResultValue(updatedRowCount);
		
		return operationResult;
	}

	@Override
	public ListOperationResult<Place> listPlace(Place place, boolean locationFlag, boolean photoFlag, boolean userFlag) {
		ListOperationResult<Place> listOperationResult = new ListOperationResult<Place>();
		StringBuilder sqlBuilder = new StringBuilder();
		List<Object> argList = new ArrayList<Object>();
		
		PlaceRowMapper placeRowMapper = new PlaceRowMapper();
		if (locationFlag) {
			placeRowMapper.addFKey(PlaceRowMapper.FKEY_LOCATION);
		}
		if (photoFlag) {
			placeRowMapper.addFKey(PlaceRowMapper.FKEY_FILE);
		}
		if (userFlag) {
			placeRowMapper.addFKey(PlaceRowMapper.FKEY_USER);
		}
		
		sqlBuilder.append(placeRowMapper.getSelectSqlWithForeignKeys());
		
		if (place != null) {
			if (place.getId() != null) {
				sqlBuilder.append(" AND " + placeRowMapper.getCriteriaColumnName(PlaceRowMapper.COL_ID) + " = ? ");
				argList.add(place.getId());
			}
			if (place.getHostUserId() != null) {
				sqlBuilder.append(" AND " + placeRowMapper.getCriteriaColumnName(PlaceRowMapper.COL_HOST_USER_ID) + " = ? ");
				argList.add(place.getHostUserId());
			}
		}
		
		if (photoFlag) {
			sqlBuilder.append(" AND " + FileRowMapper.COL_ENTITY_TYPE + " = ? ");
			argList.add(EnmEntityType.PLACE.getId());
		}
		
		List<Place> fileList = super.getJdbcTemplate().query(sqlBuilder.toString(), argList.toArray(), placeRowMapper);		
		
		listOperationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
		listOperationResult.setObjectList(fileList);		
		
		return listOperationResult;
	}

}
