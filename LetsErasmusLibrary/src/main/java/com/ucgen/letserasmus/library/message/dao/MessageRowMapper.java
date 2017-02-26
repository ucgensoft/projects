package com.ucgen.letserasmus.library.message.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.ucgen.common.dao.BaseRowMapper;
import com.ucgen.common.dao.EnmJoinType;
import com.ucgen.common.dao.ForeignKey;
import com.ucgen.letserasmus.library.message.model.Message;
import com.ucgen.letserasmus.library.user.dao.UserRowMapper;
import com.ucgen.letserasmus.library.user.model.User;

public class MessageRowMapper extends BaseRowMapper<Message> {

	public static final String TABLE_NAME = "MESSAGE";
	
	public static final String COL_ID = "ID";
	public static final String COL_SENDER_USER_ID = "SENDER_USER_ID";	
	public static final String COL_RECEIVER_USER_ID = "RECEIVER_USER_ID";
	public static final String COL_MESSAGE_TITLE = "MESSAGE_TITLE";
	public static final String COL_MESSAGE_TEXT = "MESSAGE_TEXT";
	public static final String COL_MESSAGE_THREAD_ID = "MESSAGE_THREAD_ID";
	
	public static final String FKEY_USER_SENDER = "USER_SENDER";
	public static final String FKEY_USER_RECEIVER = "USER_RECEIVER";
	
	public MessageRowMapper() {
		this("M");
	}
	
	public MessageRowMapper(String tablePrefix) {
		super(TABLE_NAME, tablePrefix);
	}
	
	public void addFKey(String keyName) {
				
		if (FKEY_USER_SENDER.equals(keyName)) {
			UserRowMapper userRowMapper = new UserRowMapper("US");
			ForeignKey<MessageRowMapper, UserRowMapper> fKeyUserSender = new ForeignKey<MessageRowMapper, UserRowMapper>(this, userRowMapper, EnmJoinType.LEFT);
			fKeyUserSender.addFieldPair(COL_SENDER_USER_ID, UserRowMapper.COL_ID);
			this.addFKey(FKEY_USER_SENDER, fKeyUserSender);
		} 
		
		if (FKEY_USER_RECEIVER.equals(keyName)) {
			UserRowMapper userRowMapper = new UserRowMapper("UR");
			ForeignKey<MessageRowMapper, UserRowMapper> fKeyUserReceiver = new ForeignKey<MessageRowMapper, UserRowMapper>(this, userRowMapper, EnmJoinType.LEFT);
			fKeyUserReceiver.addFieldPair(COL_RECEIVER_USER_ID, UserRowMapper.COL_ID);
			this.addFKey(FKEY_USER_RECEIVER, fKeyUserReceiver);
		} 
			
	}
	
	@Override
	public Message mapRow(ResultSet rs, int rowNum) throws SQLException {
		super.initializeColSet(rs);
		
		Message message = new Message();		
		
		message.setId(super.getLong(rs, COL_ID));		
		message.setSenderUserId(super.getLong(rs, COL_SENDER_USER_ID));
		message.setReceiverUserId(super.getLong(rs, COL_RECEIVER_USER_ID));
		message.setMessageTitle(super.getString(rs, COL_MESSAGE_TITLE));
		message.setMessageText(super.getString(rs, COL_MESSAGE_TEXT));
		message.setMessageThreadId(super.getLong(rs, COL_MESSAGE_THREAD_ID));
		
		message.setCreatedBy(super.getString(rs, COL_CREATED_BY));		
		message.setCreatedDate(super.getTimestamp(rs, COL_CREATED_DATE));
		message.setCreatedDateGmt(super.getTimestamp(rs, COL_CREATED_DATE_GMT));		
		message.setModifiedBy(super.getString(rs, COL_MODIFIED_BY));
		message.setModifiedDate(super.getTimestamp(rs, COL_MODIFIED_DATE));
		message.setModifiedDateGmt(super.getTimestamp(rs, COL_MODIFIED_DATE_GMT));		
			
		if (this.getfKeyMap() != null) {
			
			if (this.getfKeyMap().containsKey(FKEY_USER_SENDER)) {
				ForeignKey<MessageRowMapper, UserRowMapper> fKey = this.getfKeyMap().get(FKEY_USER_SENDER);
				User user = fKey.getDestMapper().mapRow(rs, rowNum);
				message.setSenderUser(user);
			}
			
			if (this.getfKeyMap().containsKey(FKEY_USER_RECEIVER)) {
				ForeignKey<MessageRowMapper, UserRowMapper> fKey = this.getfKeyMap().get(FKEY_USER_RECEIVER);
				User user = fKey.getDestMapper().mapRow(rs, rowNum);
				message.setReceiverUser(user);
			}
		}
		
		return message;
	}
	@Override
	public void fillFieldMaps() {
		// TODO Auto-generated method stub
	}

	@Override
	public void initializeColList() {
		super.addColumn(COL_ID);
		super.addColumn(COL_MESSAGE_THREAD_ID);
		super.addColumn(COL_SENDER_USER_ID);
		super.addColumn(COL_RECEIVER_USER_ID);
		super.addColumn(COL_MESSAGE_TITLE);
		super.addColumn(COL_MESSAGE_TEXT);
		super.addColumn(COL_CREATED_DATE);		
	}
	
}
