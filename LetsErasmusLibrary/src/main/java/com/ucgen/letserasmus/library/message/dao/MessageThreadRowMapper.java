package com.ucgen.letserasmus.library.message.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.ucgen.common.dao.BaseRowMapper;
import com.ucgen.common.dao.EnmJoinType;
import com.ucgen.common.dao.ForeignKey;
import com.ucgen.letserasmus.library.common.enumeration.EnmEntityType;
import com.ucgen.letserasmus.library.message.model.MessageThread;
import com.ucgen.letserasmus.library.place.dao.PlaceRowMapper;
import com.ucgen.letserasmus.library.reservation.dao.ReservationRowMapper;
import com.ucgen.letserasmus.library.reservation.model.Reservation;
import com.ucgen.letserasmus.library.user.dao.UserRowMapper;
import com.ucgen.letserasmus.library.user.model.User;

public class MessageThreadRowMapper extends BaseRowMapper<MessageThread> {

	public static final String TABLE_NAME = "MESSAGE_THREAD";
	
	public static final String COL_ID = "ID";
	public static final String COL_HOST_USER_ID = "HOST_USER_ID";	
	public static final String COL_CLIENT_USER_ID = "CLIENT_USER_ID";
	public static final String COL_ENTITY_TYPE = "ENTITY_TYPE";
	public static final String COL_ENTITY_ID = "ENTITY_ID";
	public static final String COL_THREAD_TITLE = "THREAD_TITLE";
	
	public static final String FKEY_USER_HOST = "USER_HOST";
	public static final String FKEY_USER_CLIENT = "USER_CLIENT";
	public static final String FKEY_ENTITY = "ENTITY";
	
	public MessageThreadRowMapper() {
		this("MT");
	}
	
	public MessageThreadRowMapper(String tablePrefix) {
		super(TABLE_NAME, tablePrefix);
	}
	
	public void addFKey(String keyName) {
				
		if (FKEY_USER_HOST.equals(keyName)) {
			UserRowMapper userRowMapper = new UserRowMapper("UH");
			ForeignKey<MessageThreadRowMapper, UserRowMapper> fKeyUserHost = new ForeignKey<MessageThreadRowMapper, UserRowMapper>(this, userRowMapper, EnmJoinType.LEFT);
			fKeyUserHost.addFieldPair(COL_HOST_USER_ID, UserRowMapper.COL_ID);
			this.addFKey(FKEY_USER_HOST, fKeyUserHost);
		} 
		
		if (FKEY_USER_CLIENT.equals(keyName)) {
			UserRowMapper userRowMapper = new UserRowMapper("UC");
			ForeignKey<MessageThreadRowMapper, UserRowMapper> fKeyUserClient = new ForeignKey<MessageThreadRowMapper, UserRowMapper>(this, userRowMapper, EnmJoinType.LEFT);
			fKeyUserClient.addFieldPair(COL_CLIENT_USER_ID, UserRowMapper.COL_ID);
			this.addFKey(FKEY_USER_CLIENT, fKeyUserClient);
		} 
			
	}
	
	public void addEntiyFKey(String keyName, EnmEntityType entityType) {
		
		if (entityType.equals(EnmEntityType.RESERVATION)) {
			ReservationRowMapper reservationRowMapper = new ReservationRowMapper("R");
			ForeignKey<MessageThreadRowMapper, ReservationRowMapper> fKeyReservation = new ForeignKey<MessageThreadRowMapper, ReservationRowMapper>(this, reservationRowMapper, EnmJoinType.LEFT);
			fKeyReservation.addFieldPair(COL_ENTITY_ID, PlaceRowMapper.COL_ID);
			fKeyReservation.addStaticValueCriteria(COL_ENTITY_TYPE, EnmEntityType.RESERVATION.getId());
			this.addFKey(FKEY_ENTITY, fKeyReservation);
		}			
	}
	
	@Override
	public MessageThread mapRow(ResultSet rs, int rowNum) throws SQLException {
		super.initializeColSet(rs);
		
		MessageThread messageThread = new MessageThread();		
		
		messageThread.setId(super.getLong(rs, COL_ID));		
		messageThread.setHostUserId(super.getLong(rs, COL_HOST_USER_ID));
		messageThread.setClientUserId(super.getLong(rs, COL_CLIENT_USER_ID));
		messageThread.setEntityType(super.getInteger(rs, COL_ENTITY_TYPE));
		messageThread.setEntityId(super.getLong(rs, COL_ENTITY_ID));
		messageThread.setThreadTitle(super.getString(rs, COL_THREAD_TITLE));
		
		messageThread.setCreatedBy(super.getString(rs, COL_CREATED_BY));		
		messageThread.setCreatedDate(super.getTimestamp(rs, COL_CREATED_DATE));
		messageThread.setCreatedDateGmt(super.getTimestamp(rs, COL_CREATED_DATE_GMT));		
		messageThread.setModifiedBy(super.getString(rs, COL_MODIFIED_BY));
		messageThread.setModifiedDate(super.getTimestamp(rs, COL_MODIFIED_DATE));
		messageThread.setModifiedDateGmt(super.getTimestamp(rs, COL_MODIFIED_DATE_GMT));		
			
		if (this.getfKeyMap() != null) {
			
			if (this.getfKeyMap().containsKey(FKEY_USER_HOST)) {
				ForeignKey<MessageThreadRowMapper, UserRowMapper> fKey = this.getfKeyMap().get(FKEY_USER_HOST);
				User user = fKey.getDestMapper().mapRow(rs, rowNum);
				messageThread.setHostUser(user);
			}
			
			if (this.getfKeyMap().containsKey(FKEY_USER_CLIENT)) {
				ForeignKey<MessageThreadRowMapper, UserRowMapper> fKey = this.getfKeyMap().get(FKEY_USER_CLIENT);
				User user = fKey.getDestMapper().mapRow(rs, rowNum);
				messageThread.setClientUser(user);
			}
			
			if (this.getfKeyMap().containsKey(FKEY_ENTITY)) {
				if (messageThread.getEntityType() != null) {
					if (messageThread.getEntityType().equals(EnmEntityType.RESERVATION.getId())) {
						ForeignKey<MessageThreadRowMapper, ReservationRowMapper> fKey = this.getfKeyMap().get(FKEY_ENTITY);
						Reservation reservation = fKey.getDestMapper().mapRow(rs, rowNum);
						messageThread.setEntity(reservation);
					}
				}
			}
		}
		
		return messageThread;
	}
	@Override
	public void fillFieldMaps() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initializeColList() {
		super.addColumn(COL_ID);
		super.addColumn(COL_THREAD_TITLE);
		super.addColumn(COL_HOST_USER_ID);
		super.addColumn(COL_CLIENT_USER_ID);
		super.addColumn(COL_ENTITY_TYPE);
		super.addColumn(COL_ENTITY_ID);		
		super.addColumn(COL_CREATED_DATE);		
	}
	
}
