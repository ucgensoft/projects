package com.ucgen.letserasmus.library.review.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.ucgen.common.dao.BaseRowMapper;
import com.ucgen.common.dao.EnmJoinType;
import com.ucgen.common.dao.ForeignKey;
import com.ucgen.letserasmus.library.common.enumeration.EnmEntityType;
import com.ucgen.letserasmus.library.place.dao.PlaceRowMapper;
import com.ucgen.letserasmus.library.reservation.dao.ReservationRowMapper;
import com.ucgen.letserasmus.library.reservation.model.Reservation;
import com.ucgen.letserasmus.library.review.model.Review;
import com.ucgen.letserasmus.library.user.dao.UserRowMapper;
import com.ucgen.letserasmus.library.user.model.User;

public class ReviewRowMapper extends BaseRowMapper<Review> {

	public static final String TABLE_NAME = "REVIEW";
	
	public static final String COL_ID = "ID";
	public static final String COL_USER_ID = "USER_ID";	
	public static final String COL_REVIEWED_USER_ID = "REVIEWED_USER_ID";
	public static final String COL_ENTITY_TYPE = "ENTITY_TYPE";
	public static final String COL_ENTITY_ID = "ENTITY_ID";
	public static final String COL_RANK = "RANK";
	public static final String COL_DESCRIPTION = "DESCRIPTION";
	
	public static final String FKEY_USER = "USER";
	public static final String FKEY_REVIEWED_USER = "REVIEWED_USER";
	public static final String FKEY_ENTITY = "ENTITY";
	
	public ReviewRowMapper() {
		this("RW");
	}
	
	public ReviewRowMapper(String tablePrefix) {
		super(TABLE_NAME, tablePrefix);
	}
	
	public void addFKey(String keyName) {
				
		if (FKEY_USER.equals(keyName)) {
			UserRowMapper userRowMapper = new UserRowMapper("U");
			ForeignKey<ReviewRowMapper, UserRowMapper> fKeyUser = new ForeignKey<ReviewRowMapper, UserRowMapper>(this, userRowMapper, EnmJoinType.LEFT);
			fKeyUser.addFieldPair(COL_USER_ID, UserRowMapper.COL_ID);
			this.addFKey(FKEY_USER, fKeyUser);
		}
		
		if (FKEY_REVIEWED_USER.equals(keyName)) {
			UserRowMapper userRowMapper = new UserRowMapper("RU");
			ForeignKey<ReviewRowMapper, UserRowMapper> fKeyReviewedUser = new ForeignKey<ReviewRowMapper, UserRowMapper>(this, userRowMapper, EnmJoinType.LEFT);
			fKeyReviewedUser.addFieldPair(COL_REVIEWED_USER_ID, UserRowMapper.COL_ID);
			this.addFKey(FKEY_USER, fKeyReviewedUser);
		}
			
	}
	
	public void addEntiyFKey(String keyName, EnmEntityType entityType) {
		
		if (entityType.equals(EnmEntityType.RESERVATION)) {
			ReservationRowMapper reservationRowMapper = new ReservationRowMapper("R");
			ForeignKey<ReviewRowMapper, ReservationRowMapper> fKeyReservation = new ForeignKey<ReviewRowMapper, ReservationRowMapper>(this, reservationRowMapper, EnmJoinType.LEFT);
			fKeyReservation.addFieldPair(COL_ENTITY_ID, ReservationRowMapper.COL_ID);
			fKeyReservation.addStaticValueCriteria(COL_ENTITY_TYPE, entityType.getId());
			this.addFKey(FKEY_ENTITY, fKeyReservation);
		}			
	}
	
	@Override
	public Review mapRow(ResultSet rs, int rowNum) throws SQLException {
		super.initializeColSet(rs);
		
		Review review = new Review();		
		
		review.setId(super.getLong(rs, COL_ID));		
		review.setUserId(super.getLong(rs, COL_USER_ID));
		review.setReviewedUserId(super.getLong(rs, COL_REVIEWED_USER_ID));
		review.setEntityType(super.getInteger(rs, COL_ENTITY_TYPE));
		review.setEntityId(super.getLong(rs, COL_ENTITY_ID));
		review.setRank(super.getInteger(rs, COL_RANK));
		review.setDescription(super.getString(rs, COL_DESCRIPTION));
		
		review.setCreatedBy(super.getString(rs, COL_CREATED_BY));		
		review.setCreatedDate(super.getTimestamp(rs, COL_CREATED_DATE));
		review.setCreatedDateGmt(super.getTimestamp(rs, COL_CREATED_DATE_GMT));		
		review.setModifiedBy(super.getString(rs, COL_MODIFIED_BY));
		review.setModifiedDate(super.getTimestamp(rs, COL_MODIFIED_DATE));
		review.setModifiedDateGmt(super.getTimestamp(rs, COL_MODIFIED_DATE_GMT));		
			
		if (this.getfKeyMap() != null) {
			
			if (this.getfKeyMap().containsKey(FKEY_USER)) {
				ForeignKey<ReviewRowMapper, UserRowMapper> fKey = this.getfKeyMap().get(FKEY_USER);
				User user = fKey.getDestMapper().mapRow(rs, rowNum);
				review.setUser(user);
			}
			
			if (this.getfKeyMap().containsKey(FKEY_REVIEWED_USER)) {
				ForeignKey<ReviewRowMapper, UserRowMapper> fKey = this.getfKeyMap().get(FKEY_REVIEWED_USER);
				User user = fKey.getDestMapper().mapRow(rs, rowNum);
				review.setReviewedUser(user);
			}
						
			if (this.getfKeyMap().containsKey(FKEY_ENTITY)) {
				if (review.getEntityType() != null) {
					if (review.getEntityType().equals(EnmEntityType.RESERVATION.getId())) {
						ForeignKey<ReviewRowMapper, ReservationRowMapper> fKey = this.getfKeyMap().get(FKEY_ENTITY);
						Reservation reservation = fKey.getDestMapper().mapRow(rs, rowNum);
						review.setEntity(reservation);
					}
				}
			}
		}
		
		return review;
	}
	@Override
	public void fillFieldMaps() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initializeColList() {
		super.addColumn(COL_ID);
		super.addColumn(COL_USER_ID);
		super.addColumn(COL_REVIEWED_USER_ID);
		super.addColumn(COL_ENTITY_TYPE);
		super.addColumn(COL_ENTITY_ID);		
		super.addColumn(COL_RANK);
		super.addColumn(COL_DESCRIPTION);
		super.addColumn(COL_CREATED_DATE);		
	}
	
}
