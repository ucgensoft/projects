package com.ucgen.letserasmus.library.reservation.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.ucgen.common.dao.BaseRowMapper;
import com.ucgen.common.dao.EnmJoinType;
import com.ucgen.common.dao.ForeignKey;
import com.ucgen.letserasmus.library.place.dao.PlaceRowMapper;
import com.ucgen.letserasmus.library.place.model.Place;
import com.ucgen.letserasmus.library.reservation.model.Reservation;
import com.ucgen.letserasmus.library.user.dao.UserRowMapper;
import com.ucgen.letserasmus.library.user.model.User;

public class ReservationRowMapper extends BaseRowMapper<Reservation> {

	public static final String TABLE_NAME = "RESERVATION";
	
	public static final String COL_ID = "ID";
	public static final String COL_PLACE_ID = "PLACE_ID";
	public static final String COL_HOST_USER_ID = "HOST_USER_ID";	
	public static final String COL_CLIENT_USER_ID = "CLIENT_USER_ID";
	public static final String COL_START_DATE = "START_DATE";
	public static final String COL_END_DATE = "END_DATE";
	public static final String COL_GUEST_NUMBER = "GUEST_NUMBER";
	public static final String COL_PLACE_PRICE = "PLACE_PRICE";
	public static final String COL_COMMISSION_RATE = "COMMISSION_RATE";
	public static final String COL_COMMISSION_FEE = "COMMISSION_FEE";
	public static final String COL_SERVICE_RATE = "SERVICE_RATE";
	public static final String COL_SERVICE_FEE = "SERVICE_FEE";
	public static final String COL_CURRENCY_ID = "CURRENCY_ID";
	public static final String COL_STATUS = "STATUS";
	public static final String COL_MESSAGE_THREAD_ID = "MESSAGE_THREAD_ID";
	
	public static final String FKEY_PLACE = "PLACE";
	public static final String FKEY_USER_HOST = "USER_HOST";
	public static final String FKEY_USER_CLIENT = "USER_CLIENT";
	
	public ReservationRowMapper() {
		this("R");
	}
	
	public ReservationRowMapper(String tablePrefix) {
		super(TABLE_NAME, tablePrefix);
	}
	
	public void addFKey(String keyName) {
		
		if (FKEY_PLACE.equals(keyName)) {
			PlaceRowMapper placeRowMapper = new PlaceRowMapper("P");
			ForeignKey<ReservationRowMapper, PlaceRowMapper> fKeyPlace = new ForeignKey<ReservationRowMapper, PlaceRowMapper>(this, placeRowMapper, EnmJoinType.LEFT);
			fKeyPlace.addFieldPair(COL_PLACE_ID, PlaceRowMapper.COL_ID);
			this.addFKey(FKEY_PLACE, fKeyPlace);
		}
		
		if (FKEY_USER_HOST.equals(keyName)) {
			UserRowMapper userRowMapper = new UserRowMapper("UH");
			ForeignKey<ReservationRowMapper, UserRowMapper> fKeyUserHost = new ForeignKey<ReservationRowMapper, UserRowMapper>(this, userRowMapper, EnmJoinType.LEFT);
			fKeyUserHost.addFieldPair(COL_HOST_USER_ID, UserRowMapper.COL_ID);
			this.addFKey(FKEY_USER_HOST, fKeyUserHost);
		} 
		
		if (FKEY_USER_HOST.equals(keyName)) {
			UserRowMapper userRowMapper = new UserRowMapper("UC");
			ForeignKey<ReservationRowMapper, UserRowMapper> fKeyUserClient = new ForeignKey<ReservationRowMapper, UserRowMapper>(this, userRowMapper, EnmJoinType.LEFT);
			fKeyUserClient.addFieldPair(COL_CLIENT_USER_ID, UserRowMapper.COL_ID);
			this.addFKey(FKEY_USER_HOST, fKeyUserClient);
		} 
			
	}
	
	@Override
	public Reservation mapRow(ResultSet rs, int rowNum) throws SQLException {
		super.initializeColSet(rs);
		
		Reservation reservation = new Reservation();		
		
		reservation.setId(super.getLong(rs, COL_ID));
		reservation.setPlaceId(super.getLong(rs, COL_PLACE_ID));
		reservation.setHostUserId(super.getLong(rs, COL_HOST_USER_ID));
		reservation.setClientUserId(super.getLong(rs, COL_CLIENT_USER_ID));
		reservation.setStartDate(super.getTimestamp(rs, COL_START_DATE));
		reservation.setEndDate(super.getTimestamp(rs, COL_END_DATE));
		reservation.setPlacePrice(super.getBigDecimal(rs, COL_PLACE_PRICE));
		reservation.setCommissionRate(super.getBigDecimal(rs, COL_COMMISSION_RATE));
		reservation.setCommissionFee(super.getBigDecimal(rs, COL_COMMISSION_FEE));
		reservation.setServiceRate(super.getBigDecimal(rs, COL_SERVICE_RATE));
		reservation.setServiceFee(super.getBigDecimal(rs, COL_SERVICE_FEE));
		reservation.setStatus(super.getInteger(rs, COL_STATUS));
		reservation.setCurrencyId(super.getInteger(rs, COL_CURRENCY_ID));
		reservation.setMessageThreadId(super.getLong(rs, COL_MESSAGE_THREAD_ID));
		
		reservation.setCreatedBy(super.getString(rs, COL_CREATED_BY));		
		reservation.setCreatedDate(super.getTimestamp(rs, COL_CREATED_DATE));
		reservation.setCreatedDateGmt(super.getTimestamp(rs, COL_CREATED_DATE_GMT));		
		reservation.setModifiedBy(super.getString(rs, COL_MODIFIED_BY));
		reservation.setModifiedDate(super.getTimestamp(rs, COL_MODIFIED_DATE));
		reservation.setModifiedDateGmt(super.getTimestamp(rs, COL_MODIFIED_DATE_GMT));		
			
		if (this.getfKeyMap() != null) {
			if (this.getfKeyMap().containsKey(FKEY_PLACE)) {
				ForeignKey<ReservationRowMapper, PlaceRowMapper> fKey = this.getfKeyMap().get(FKEY_PLACE);
				Place place = fKey.getDestMapper().mapRow(rs, rowNum);
				reservation.setPlace(place);
			}
			
			if (this.getfKeyMap().containsKey(FKEY_USER_HOST)) {
				ForeignKey<ReservationRowMapper, UserRowMapper> fKey = this.getfKeyMap().get(FKEY_USER_HOST);
				User user = fKey.getDestMapper().mapRow(rs, rowNum);
				reservation.setHostUser(user);
			}
			
			if (this.getfKeyMap().containsKey(FKEY_USER_CLIENT)) {
				ForeignKey<ReservationRowMapper, UserRowMapper> fKey = this.getfKeyMap().get(FKEY_USER_CLIENT);
				User user = fKey.getDestMapper().mapRow(rs, rowNum);
				reservation.setClientUser(user);
			}
		}
		
		return reservation;
	}
	@Override
	public void fillFieldMaps() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initializeColList() {
		super.addColumn(COL_ID);
		super.addColumn(COL_PLACE_ID);
		super.addColumn(COL_HOST_USER_ID);
		super.addColumn(COL_CLIENT_USER_ID);
		super.addColumn(COL_START_DATE);
		super.addColumn(COL_END_DATE);
		super.addColumn(COL_PLACE_PRICE);
		super.addColumn(COL_COMMISSION_RATE);
		super.addColumn(COL_COMMISSION_FEE);
		super.addColumn(COL_SERVICE_RATE);
		super.addColumn(COL_SERVICE_FEE);
		super.addColumn(COL_CURRENCY_ID);
		super.addColumn(COL_STATUS);
		super.addColumn(COL_MESSAGE_THREAD_ID);
	}
	
}
