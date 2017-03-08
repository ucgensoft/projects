package com.ucgen.letserasmus.library.complaint.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.ucgen.common.dao.BaseRowMapper;
import com.ucgen.common.dao.EnmJoinType;
import com.ucgen.common.dao.ForeignKey;
import com.ucgen.letserasmus.library.common.enumeration.EnmEntityType;
import com.ucgen.letserasmus.library.complaint.model.Complaint;
import com.ucgen.letserasmus.library.place.dao.PlaceRowMapper;
import com.ucgen.letserasmus.library.place.model.Place;
import com.ucgen.letserasmus.library.user.dao.UserRowMapper;
import com.ucgen.letserasmus.library.user.model.User;

public class ComplaintRowMapper extends BaseRowMapper<Complaint> {

	public static final String TABLE_NAME = "COMPLAINT";
	
	public static final String COL_ID = "ID";
	public static final String COL_USER_ID = "USER_ID";	
	public static final String COL_HOST_USER_ID = "HOST_USER_ID";
	public static final String COL_ENTITY_TYPE = "ENTITY_TYPE";
	public static final String COL_ENTITY_ID = "ENTITY_ID";
	public static final String COL_DESCRIPTON = "DESCRIPTION";	
	public static final String COL_STATUS = "STATUS";
	
	public static final String FKEY_USER = "USER";
	public static final String FKEY_HOST_USER = "HOST_USER";
	public static final String FKEY_ENTITY = "ENTITY";
	
	public ComplaintRowMapper() {
		this("C");
	}
	
	public ComplaintRowMapper(String tablePrefix) {
		super(TABLE_NAME, tablePrefix);
	}
	
	public void addFKey(String keyName) {
				
		if (FKEY_USER.equals(keyName)) {
			UserRowMapper userRowMapper = new UserRowMapper("U");
			ForeignKey<ComplaintRowMapper, UserRowMapper> fKeyUser = new ForeignKey<ComplaintRowMapper, UserRowMapper>(this, userRowMapper, EnmJoinType.LEFT);
			fKeyUser.addFieldPair(COL_USER_ID, UserRowMapper.COL_ID);
			this.addFKey(FKEY_USER, fKeyUser);
		}
		
		if (FKEY_HOST_USER.equals(keyName)) {
			UserRowMapper userRowMapper = new UserRowMapper("HU");
			ForeignKey<ComplaintRowMapper, UserRowMapper> fKeyHostUser = new ForeignKey<ComplaintRowMapper, UserRowMapper>(this, userRowMapper, EnmJoinType.LEFT);
			fKeyHostUser.addFieldPair(COL_HOST_USER_ID, UserRowMapper.COL_ID);
			this.addFKey(FKEY_HOST_USER, fKeyHostUser);
		}
			
	}
	
	public void addEntiyFKey(String keyName, EnmEntityType entityType) {
		
		if (entityType.equals(EnmEntityType.PLACE)) {
			PlaceRowMapper placeRowMapper = new PlaceRowMapper("P");
			ForeignKey<ComplaintRowMapper, PlaceRowMapper> fKeyPlace = new ForeignKey<ComplaintRowMapper, PlaceRowMapper>(this, placeRowMapper, EnmJoinType.LEFT);
			fKeyPlace.addFieldPair(COL_ENTITY_ID, PlaceRowMapper.COL_ID);
			fKeyPlace.addStaticValueCriteria(COL_ENTITY_TYPE, entityType.getId());
			this.addFKey(FKEY_ENTITY, fKeyPlace);
		}			
	}
	
	@Override
	public Complaint mapRow(ResultSet rs, int rowNum) throws SQLException {
		super.initializeColSet(rs);
		
		Complaint complaint = new Complaint();		
		
		complaint.setId(super.getLong(rs, COL_ID));		
		complaint.setUserId(super.getLong(rs, COL_USER_ID));
		complaint.setHostUserId(super.getLong(rs, COL_HOST_USER_ID));
		complaint.setEntityType(super.getInteger(rs, COL_ENTITY_TYPE));
		complaint.setEntityId(super.getLong(rs, COL_ENTITY_ID));
		complaint.setDescription(super.getString(rs, COL_DESCRIPTON));
		complaint.setStatus(super.getInteger(rs, COL_STATUS));
		
		complaint.setCreatedBy(super.getString(rs, COL_CREATED_BY));		
		complaint.setCreatedDate(super.getTimestamp(rs, COL_CREATED_DATE));
		complaint.setCreatedDateGmt(super.getTimestamp(rs, COL_CREATED_DATE_GMT));		
		complaint.setModifiedBy(super.getString(rs, COL_MODIFIED_BY));
		complaint.setModifiedDate(super.getTimestamp(rs, COL_MODIFIED_DATE));
		complaint.setModifiedDateGmt(super.getTimestamp(rs, COL_MODIFIED_DATE_GMT));		
			
		if (this.getfKeyMap() != null) {
			
			if (this.getfKeyMap().containsKey(FKEY_USER)) {
				ForeignKey<ComplaintRowMapper, UserRowMapper> fKey = this.getfKeyMap().get(FKEY_USER);
				User user = fKey.getDestMapper().mapRow(rs, rowNum);
				complaint.setUser(user);
			}
			
			if (this.getfKeyMap().containsKey(FKEY_HOST_USER)) {
				ForeignKey<ComplaintRowMapper, UserRowMapper> fKey = this.getfKeyMap().get(FKEY_HOST_USER);
				User user = fKey.getDestMapper().mapRow(rs, rowNum);
				complaint.setHostUser(user);
			}
						
			if (this.getfKeyMap().containsKey(FKEY_ENTITY)) {
				if (complaint.getEntityType() != null) {
					if (complaint.getEntityType().equals(EnmEntityType.PLACE.getId())) {
						ForeignKey<ComplaintRowMapper, PlaceRowMapper> fKey = this.getfKeyMap().get(FKEY_ENTITY);
						Place place = fKey.getDestMapper().mapRow(rs, rowNum);
						complaint.setEntity(place);
					}
				}
			}
		}
		
		return complaint;
	}
	@Override
	public void fillFieldMaps() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initializeColList() {
		super.addColumn(COL_ID);
		super.addColumn(COL_USER_ID);
		super.addColumn(COL_HOST_USER_ID);
		super.addColumn(COL_ENTITY_TYPE);
		super.addColumn(COL_ENTITY_ID);		
		super.addColumn(COL_DESCRIPTON);
		super.addColumn(COL_STATUS);
		super.addColumn(COL_CREATED_DATE);		
	}
	
}
