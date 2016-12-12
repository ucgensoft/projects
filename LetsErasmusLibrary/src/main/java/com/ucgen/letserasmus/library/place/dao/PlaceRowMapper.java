package com.ucgen.letserasmus.library.place.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.ucgen.common.dao.BaseRowMapper;
import com.ucgen.common.dao.ForeignKey;
import com.ucgen.letserasmus.library.location.dao.LocationRowMapper;
import com.ucgen.letserasmus.library.location.model.Location;
import com.ucgen.letserasmus.library.place.model.Place;
import com.ucgen.letserasmus.library.user.dao.UserRowMapper;

public class PlaceRowMapper extends BaseRowMapper<Place> {
	
	public static final String TABLE_NAME = "PLACE";
	
	public static final String COL_ID = "ID";
	public static final String COL_HOST_USER_ID = "HOST_USER_ID";	
	public static final String COL_PLACE_TYPE_ID = "PLACE_TYPE_ID";
	public static final String COL_HOME_TYPE_ID = "HOME_TYPE_ID";
	public static final String COL_TITLE = "TITLE";	
	public static final String COL_DESCRIPTION = "DESCRIPTION";
	public static final String COL_STATUS = "STATUS";	
	public static final String COL_LOCATION_ID = "LOCATION_ID";
	public static final String COL_PRICE = "PRICE";		
	public static final String COL_BILLS_INCLUDE = "BILLS_INCLUDE";
	public static final String COL_DEPOSIT_PRICE = "DEPOSIT_PRICE";	
	public static final String COL_CURRENCY_ID = "CURRENCY_ID";
	public static final String COL_BED_NUMBER = "BED_NUMBER";
	public static final String COL_BED_TYPE_ID = "BED_TYPE_ID";
	public static final String COL_BATHROOM_NUMBER = "BATHROOM_NUMBER";
	public static final String COL_BATHROOM_TYPE = "BATHROOM_TYPE";	
	public static final String COL_PLACE_MATE_NUMBER = "PLACE_MATE_NUMBER";
	public static final String COL_PLACE_MATE_GENDER = "PLACE_MATE_GENDER";
	public static final String COL_GUEST_NUMBER = "GUEST_NUMBER";	
	public static final String COL_GUEST_GENDER = "GUEST_GENDER";
	public static final String COL_RULES = "RULES";
	public static final String COL_AMENTIES = "AMENTIES";	
	public static final String COL_SAFETY_AMENTIES = "SAFETY_AMENTIES";
	public static final String COL_MINUMUM_STAY = "MINUMUM_STAY";
	public static final String COL_MAXIMUM_STAY = "MAXIMUM_STAY";		
	public static final String COL_START_DATE = "START_DATE";
	public static final String COL_END_DATE = "END_DATE";	
	public static final String COL_CANCELLATION_POLICY_ID = "CANCELLATION_POLICY_ID";	
	public static final String COL_CREATED_BY = "CREATED_BY";
	public static final String COL_CREATED_DATE = "CREATED_DATE";
	public static final String COL_CREATED_DATE_GMT = "CREATED_DATE_GMT";
	public static final String COL_MODIFIED_BY = "MODIFIED_BY";
	public static final String COL_MODIFIED_DATE = "MODIFIED_DATE";
	public static final String COL_MODIFIED_DATE_GMT = "MODIFIED_DATE_GMT";
	
	public static final String FKEY_LOCATION = "LOCATION";
	public static final String FKEY_USER = "USER";
	
	public PlaceRowMapper() {
		this("P");
	}
	
	public PlaceRowMapper(String tablePrefix) {
		super(TABLE_NAME, tablePrefix);
	}
	
	public void addFKey(String keyName) {
		if (FKEY_LOCATION.equals(keyName)) {
			LocationRowMapper locationRowMapper = new LocationRowMapper("L");
			ForeignKey<PlaceRowMapper, LocationRowMapper> fKeyLocation = new ForeignKey<PlaceRowMapper, LocationRowMapper>(this, locationRowMapper);
			fKeyLocation.addFieldPair(COL_LOCATION_ID, LocationRowMapper.COL_ID);
			this.addFKey(FKEY_LOCATION, fKeyLocation);
		} if (FKEY_USER.equals(keyName)) {
			UserRowMapper userRowMapper = new UserRowMapper("L");
			ForeignKey<PlaceRowMapper, UserRowMapper> fKeyUser = new ForeignKey<PlaceRowMapper, UserRowMapper>(this, userRowMapper);
			fKeyUser.addFieldPair(COL_HOST_USER_ID, LocationRowMapper.COL_ID);
			this.addFKey(FKEY_USER, fKeyUser);
		}
	}
	
	@Override
	public Place mapRow(ResultSet rs, int rowNum) throws SQLException {
		super.initializeColSet(rs);
		Place place = new Place();		
		place.setId(super.getLong(rs, COL_ID));
		place.setHostUserId(super.getLong(rs, COL_HOST_USER_ID));
		place.setPlaceTypeId(super.getInteger(rs, COL_PLACE_TYPE_ID));
		place.setHomeTypeId(super.getInteger(rs, COL_HOME_TYPE_ID));
		place.setTitle(super.getString(rs, COL_TITLE));
		place.setDescription(super.getString(rs, COL_DESCRIPTION));
		place.setStatus(super.getInteger(rs, COL_STATUS));
		place.setLocationId(super.getLong(rs, COL_LOCATION_ID));
		place.setPrice(super.getBigDecimal(rs, COL_PRICE));
		place.setBillsInclude(super.getString(rs, COL_BILLS_INCLUDE));
		place.setDepositPrice(super.getBigDecimal(rs, COL_DEPOSIT_PRICE));
		place.setCurrencyId(super.getLong(rs, COL_CURRENCY_ID));
		place.setBedNumber(super.getInteger(rs, COL_BED_NUMBER));
		place.setBedTypeId(super.getInteger(rs, COL_BED_TYPE_ID));
		place.setBathRoomNumber(super.getInteger(rs, COL_BATHROOM_NUMBER));
		place.setBathRoomType(super.getInteger(rs, COL_BATHROOM_TYPE));
		place.setPlaceMateNumber(super.getInteger(rs, COL_PLACE_MATE_NUMBER));
		place.setPlaceMateGender(super.getInteger(rs, COL_PLACE_MATE_GENDER));
		place.setGuestNumber(super.getInteger(rs, COL_GUEST_NUMBER));
		place.setGuestGender(super.getInteger(rs, COL_GUEST_GENDER));
		place.setRules(super.getString(rs, COL_RULES));
		place.setAmenties(super.getString(rs, COL_AMENTIES));
		place.setSafetyAmenties(super.getString(rs, COL_SAFETY_AMENTIES));
		place.setMinumumStay(super.getInteger(rs, COL_MINUMUM_STAY));
		place.setMaximumStay(super.getInteger(rs, COL_MAXIMUM_STAY));
		place.setStartDate(super.getTimestamp(rs, COL_START_DATE));
		place.setEndDate(super.getTimestamp(rs, COL_END_DATE));
		place.setCreatedBy(super.getString(rs, COL_CREATED_BY));		
		place.setCreatedDate(super.getTimestamp(rs, COL_CREATED_DATE));
		place.setCreatedDateGmt(super.getTimestamp(rs, COL_CREATED_DATE_GMT));		
		place.setModifiedBy(super.getString(rs, COL_MODIFIED_BY));
		place.setModifiedDate(super.getTimestamp(rs, COL_MODIFIED_DATE));
		place.setModifiedDateGmt(super.getTimestamp(rs, COL_MODIFIED_DATE_GMT));		
	
		if (this.getfKeyMap().containsKey(FKEY_LOCATION)) {
			ForeignKey<PlaceRowMapper, LocationRowMapper> fKey = this.getfKeyMap().get(FKEY_LOCATION);
			Location location = fKey.getDestMapper().mapRow(rs, rowNum);
			place.setLocation(location);
		}
		
		return place;
	}
	@Override
	public void fillFieldMaps() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initializeColList() {
		super.addColumn(COL_AMENTIES);
		super.addColumn(COL_BATHROOM_NUMBER);
		super.addColumn(COL_BATHROOM_TYPE);
		super.addColumn(COL_BED_NUMBER);
		super.addColumn(COL_BED_TYPE_ID);
		super.addColumn(COL_BILLS_INCLUDE);
		super.addColumn(COL_CANCELLATION_POLICY_ID);
		super.addColumn(COL_CREATED_BY);
		super.addColumn(COL_CREATED_DATE);
		super.addColumn(COL_CREATED_DATE_GMT);
		super.addColumn(COL_CURRENCY_ID);
		super.addColumn(COL_DEPOSIT_PRICE);
		super.addColumn(COL_DESCRIPTION);
		super.addColumn(COL_END_DATE);
		super.addColumn(COL_GUEST_GENDER);
		super.addColumn(COL_GUEST_NUMBER);
		super.addColumn(COL_HOME_TYPE_ID);
		super.addColumn(COL_HOST_USER_ID);
		super.addColumn(COL_ID);
		super.addColumn(COL_LOCATION_ID);
		super.addColumn(COL_MAXIMUM_STAY);
		super.addColumn(COL_MINUMUM_STAY);
		super.addColumn(COL_MODIFIED_BY);
		super.addColumn(COL_MODIFIED_DATE);
		super.addColumn(COL_MODIFIED_DATE_GMT);
		super.addColumn(COL_PLACE_MATE_GENDER);
		super.addColumn(COL_PLACE_MATE_NUMBER);
		super.addColumn(COL_PLACE_TYPE_ID);
		super.addColumn(COL_PRICE);
		super.addColumn(COL_RULES);
		super.addColumn(COL_SAFETY_AMENTIES);
		super.addColumn(COL_START_DATE);
		super.addColumn(COL_STATUS);
		super.addColumn(COL_TITLE);
	}	

}
