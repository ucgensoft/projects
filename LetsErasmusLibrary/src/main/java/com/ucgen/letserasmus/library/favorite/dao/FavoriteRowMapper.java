package com.ucgen.letserasmus.library.favorite.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.ucgen.common.dao.BaseRowMapper;
import com.ucgen.common.dao.EnmJoinType;
import com.ucgen.common.dao.ForeignKey;
import com.ucgen.letserasmus.library.common.enumeration.EnmEntityType;
import com.ucgen.letserasmus.library.favorite.model.Favorite;
import com.ucgen.letserasmus.library.place.dao.PlaceRowMapper;
import com.ucgen.letserasmus.library.place.model.Place;
import com.ucgen.letserasmus.library.user.dao.UserRowMapper;
import com.ucgen.letserasmus.library.user.model.User;

public class FavoriteRowMapper extends BaseRowMapper<Favorite> {

	public static final String TABLE_NAME = "FAVORITE";
	
	public static final String COL_ID = "ID";
	public static final String COL_USER_ID = "USER_ID";	
	public static final String COL_HOST_USER_ID = "HOST_USER_ID";
	public static final String COL_ENTITY_TYPE = "ENTITY_TYPE";
	public static final String COL_ENTITY_ID = "ENTITY_ID";
	
	public static final String FKEY_USER = "USER";
	public static final String FKEY_HOST_USER = "HOST_USER";
	public static final String FKEY_ENTITY = "ENTITY";
	
	public FavoriteRowMapper() {
		this("F");
	}
	
	public FavoriteRowMapper(String tablePrefix) {
		super(TABLE_NAME, tablePrefix);
	}
	
	public void addFKey(String keyName) {
				
		if (FKEY_USER.equals(keyName)) {
			UserRowMapper userRowMapper = new UserRowMapper("U");
			ForeignKey<FavoriteRowMapper, UserRowMapper> fKeyUser = new ForeignKey<FavoriteRowMapper, UserRowMapper>(this, userRowMapper, EnmJoinType.LEFT);
			fKeyUser.addFieldPair(COL_USER_ID, UserRowMapper.COL_ID);
			this.addFKey(FKEY_USER, fKeyUser);
		}
		
		if (FKEY_HOST_USER.equals(keyName)) {
			UserRowMapper userRowMapper = new UserRowMapper("HU");
			ForeignKey<FavoriteRowMapper, UserRowMapper> fKeyHostUser = new ForeignKey<FavoriteRowMapper, UserRowMapper>(this, userRowMapper, EnmJoinType.LEFT);
			fKeyHostUser.addFieldPair(COL_HOST_USER_ID, UserRowMapper.COL_ID);
			this.addFKey(FKEY_HOST_USER, fKeyHostUser);
		}
			
	}
	
	public void addEntiyFKey(String keyName, EnmEntityType entityType) {
		
		if (entityType.equals(EnmEntityType.PLACE)) {
			PlaceRowMapper placeRowMapper = new PlaceRowMapper("P");
			ForeignKey<FavoriteRowMapper, PlaceRowMapper> fKeyPlace = new ForeignKey<FavoriteRowMapper, PlaceRowMapper>(this, placeRowMapper, EnmJoinType.LEFT);
			fKeyPlace.addFieldPair(COL_ENTITY_ID, PlaceRowMapper.COL_ID);
			fKeyPlace.addStaticValueCriteria(COL_ENTITY_TYPE, entityType.getId());
			this.addFKey(FKEY_ENTITY, fKeyPlace);
		}			
	}
	
	@Override
	public Favorite mapRow(ResultSet rs, int rowNum) throws SQLException {
		super.initializeColSet(rs);
		
		Favorite favorite = new Favorite();		
		
		favorite.setId(super.getLong(rs, COL_ID));		
		favorite.setUserId(super.getLong(rs, COL_USER_ID));
		favorite.setHostUserId(super.getLong(rs, COL_HOST_USER_ID));
		favorite.setEntityType(super.getInteger(rs, COL_ENTITY_TYPE));
		favorite.setEntityId(super.getLong(rs, COL_ENTITY_ID));
		
		favorite.setCreatedBy(super.getString(rs, COL_CREATED_BY));		
		favorite.setCreatedDate(super.getTimestamp(rs, COL_CREATED_DATE));
		favorite.setCreatedDateGmt(super.getTimestamp(rs, COL_CREATED_DATE_GMT));		
		favorite.setModifiedBy(super.getString(rs, COL_MODIFIED_BY));
		favorite.setModifiedDate(super.getTimestamp(rs, COL_MODIFIED_DATE));
		favorite.setModifiedDateGmt(super.getTimestamp(rs, COL_MODIFIED_DATE_GMT));		
			
		if (this.getfKeyMap() != null) {
			
			if (this.getfKeyMap().containsKey(FKEY_USER)) {
				ForeignKey<FavoriteRowMapper, UserRowMapper> fKey = this.getfKeyMap().get(FKEY_USER);
				User user = fKey.getDestMapper().mapRow(rs, rowNum);
				favorite.setUser(user);
			}
			
			if (this.getfKeyMap().containsKey(FKEY_HOST_USER)) {
				ForeignKey<FavoriteRowMapper, UserRowMapper> fKey = this.getfKeyMap().get(FKEY_HOST_USER);
				User user = fKey.getDestMapper().mapRow(rs, rowNum);
				favorite.setHostUser(user);
			}
						
			if (this.getfKeyMap().containsKey(FKEY_ENTITY)) {
				if (favorite.getEntityType() != null) {
					if (favorite.getEntityType().equals(EnmEntityType.PLACE.getId())) {
						ForeignKey<FavoriteRowMapper, PlaceRowMapper> fKey = this.getfKeyMap().get(FKEY_ENTITY);
						Place place = fKey.getDestMapper().mapRow(rs, rowNum);
						favorite.setEntity(place);
					}
				}
			}
		}
		
		return favorite;
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
		super.addColumn(COL_CREATED_DATE);		
	}
	
}
