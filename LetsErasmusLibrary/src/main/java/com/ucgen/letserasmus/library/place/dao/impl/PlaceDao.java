package com.ucgen.letserasmus.library.place.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import com.ucgen.common.operationresult.EnmResultCode;
import com.ucgen.common.operationresult.ListOperationResult;
import com.ucgen.common.operationresult.ValueOperationResult;

import com.ucgen.letserasmus.library.place.dao.IPlaceDao;
import com.ucgen.letserasmus.library.place.dao.PlaceRowMapper;
import com.ucgen.letserasmus.library.place.model.Place;

@Repository
public class PlaceDao extends JdbcDaoSupport implements IPlaceDao{

	
	private static final String GET_PLACE_WITH_ID_SQL = " SELECT `ID`, `HOST_USER_ID`, `PLACE_TYPE_ID`, `HOME_TYPE_ID`, `TITLE`, `DESCRIPTION`, `STATUS`, `LOCATION_ID`, `PHONE`, `PRICE`, `BILLS_INCLUDE`, `DEPOSIT_PRICE`, `CURRENCY_ID`, `BED_NUMBER`, `BED_TYPE_ID`, `BATHROOM_NUMBER`, `BATHROOM_TYPE`, `PLACE_MATE_NUMBER`, `PLACE_MATE_GENDER`, `GUEST_NUMBER`, `GUEST_GENDER`, `RULES`, `AMENTIES`, `SAFETY_AMENTIES`, `MINUMUM_STAY`, `MAXIMUM_STAY`, `START_DATE`, `END_DATE`, `CANCELLATION_POLICY_ID`, `CREATED_BY`, `CREATED_DATE`, `CREATED_DATE_GMT`, `MODIFIED_BY`, `MODIFIED_DATE`, `MODIFIED_DATE_GMT` FROM `PLACE` WHERE `ID`=? ";
	private static final String INSERT_PLACE_SQL = " INSERT INTO `PLACE`(`HOST_USER_ID`, `PLACE_TYPE_ID`, `HOME_TYPE_ID`, `TITLE`, `DESCRIPTION`, `STATUS`, `LOCATION_ID`, `PHONE`, `PRICE`, `BILLS_INCLUDE`, `DEPOSIT_PRICE`, `CURRENCY_ID`, `BED_NUMBER`, `BED_TYPE_ID`, `BATHROOM_NUMBER`, `BATHROOM_TYPE`, `PLACE_MATE_NUMBER`, `PLACE_MATE_GENDER`, `GUEST_NUMBER`, `GUEST_GENDER`, `RULES`, `AMENTIES`, `SAFETY_AMENTIES`, `MINUMUM_STAY`, `MAXIMUM_STAY`, `START_DATE`, `END_DATE`, `CANCELLATION_POLICY_ID`, `CREATED_BY`, `CREATED_DATE`, `CREATED_DATE_GMT`, `MODIFIED_BY`, `MODIFIED_DATE`, `MODIFIED_DATE_GMT`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
	private static final String UPDATE_PLACE_SQL = " UPDATE `PLACE` SET `ID`=?,`HOST_USER_ID`=?,`PLACE_TYPE_ID`=?,`HOME_TYPE_ID`=?,`TITLE`=?,`DESCRIPTION`=?,`STATUS`=?,`LOCATION_ID`=?,`PHONE`=?,`PRICE`=?,`BILLS_INCLUDE`=?,`DEPOSIT_PRICE`=?,`CURRENCY_ID`=?,`BED_NUMBER`=?,`BED_TYPE_ID`=?,`BATHROOM_NUMBER`=?,`BATHROOM_TYPE`=?,`PLACE_MATE_NUMBER`=?,`PLACE_MATE_GENDER`=?,`GUEST_NUMBER`=?,`GUEST_GENDER`=?,`RULES`=?,`AMENTIES`=?,`SAFETY_AMENTIES`=?,`MINUMUM_STAY`=?,`MAXIMUM_STAY`=?,`START_DATE`=?,`END_DATE`=?,`CANCELLATION_POLICY_ID`=?,`CREATED_BY`=?,`CREATED_DATE`=?,`CREATED_DATE_GMT`=?,`MODIFIED_BY`=?,`MODIFIED_DATE`=?,`MODIFIED_DATE_GMT`=? WHERE `ID`=? ";
	
	@Autowired
	public PlaceDao(DataSource dataSource) {
		super();
		super.setDataSource(dataSource);
	}	
	
	@Override
	public ListOperationResult<Place> getPlace(Long id) {
		ListOperationResult<Place> listOperationResult = new ListOperationResult<Place>();
		List<Place> fileList = super.getJdbcTemplate().query(GET_PLACE_WITH_ID_SQL, new Object[] { id }, new PlaceRowMapper());		
		listOperationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
		listOperationResult.setObjectList(fileList);		
		return listOperationResult;
	}

	@Override
	public ValueOperationResult<Integer> insertPlace(Place place) {
		
		ValueOperationResult<Integer> operationResult = new ValueOperationResult<Integer>();
		
		List<Object> argList = new ArrayList<Object>();
		
		argList.add(place.getHostUserId());
		argList.add(place.getPlaceTypeId());
		argList.add(place.getHomeTypeId());
		argList.add(place.getTitle());
		argList.add(place.getDescription());
		argList.add(place.getStatus());
		argList.add(place.getLocationId());
		argList.add(place.getPhone());
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
		argList.add(place.getMinumumStay());
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
		
		int i = this.getJdbcTemplate().update(INSERT_PLACE_SQL, argList.toArray());
		
		operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
		operationResult.setResultValue(i);				
		return operationResult;
	}

	@Override
	public ValueOperationResult<Integer> updatePlace(Place place) {
		
		ValueOperationResult<Integer> operationResult = new ValueOperationResult<Integer>();		
		List<Object> argList = new ArrayList<Object>();
		argList.add(place.getHostUserId());
		argList.add(place.getPlaceTypeId());
		argList.add(place.getHomeTypeId());
		argList.add(place.getTitle());
		argList.add(place.getDescription());
		argList.add(place.getStatus());
		argList.add(place.getLocationId());
		argList.add(place.getPhone());
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
		argList.add(place.getMinumumStay());
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
		argList.add(place.getId());
		int updatedRowCount =  this.getJdbcTemplate().update(UPDATE_PLACE_SQL, argList.toArray() );
		
		operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
		operationResult.setResultValue(updatedRowCount);
		
		return operationResult;
	}

}
