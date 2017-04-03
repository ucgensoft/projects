package com.ucgen.letserasmus.library.log.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.ucgen.common.dao.BaseRowMapper;
import com.ucgen.common.dao.EnmJoinType;
import com.ucgen.common.dao.ForeignKey;
import com.ucgen.letserasmus.library.common.enumeration.EnmEntityType;
import com.ucgen.letserasmus.library.log.model.TransactionLog;
import com.ucgen.letserasmus.library.place.dao.PlaceRowMapper;

public class TransactionLogRowMapper extends BaseRowMapper<TransactionLog> {

	public static final String TABLE_NAME = "TRANSACTION_LOG";
	
	public static final String COL_ID = "ID";
	public static final String COL_USER_ID = "USER_ID";	
	public static final String COL_OPERATION_ID = "OPERATION_ID";
	public static final String COL_OPERATION_DATE = "OPERATION_DATE";
	public static final String COL_ENTITY_TYPE = "ENTITY_TYPE";
	public static final String COL_ENTITY_ID = "ENTITY_ID";
	
	public static final String FKEY_USER = "USER";
	public static final String FKEY_ENTITY = "ENTITY";
	
	public TransactionLogRowMapper() {
		this("TL");
	}
	
	public TransactionLogRowMapper(String tablePrefix) {
		super(TABLE_NAME, tablePrefix);
	}
	
	public void addFKey(String keyName) {
			
	}
	
	public void addEntiyFKey(String keyName, EnmEntityType entityType) {
		
		if (entityType.equals(EnmEntityType.PLACE)) {
			PlaceRowMapper placeRowMapper = new PlaceRowMapper("P");
			ForeignKey<TransactionLogRowMapper, PlaceRowMapper> fKeyPlace = new ForeignKey<TransactionLogRowMapper, PlaceRowMapper>(this, placeRowMapper, EnmJoinType.LEFT);
			fKeyPlace.addFieldPair(COL_ENTITY_ID, PlaceRowMapper.COL_ID);
			fKeyPlace.addStaticValueCriteria(COL_ENTITY_TYPE, entityType.getId());
			this.addFKey(FKEY_ENTITY, fKeyPlace);
		}			
	}
	
	@Override
	public TransactionLog mapRow(ResultSet rs, int rowNum) throws SQLException {
		super.initializeColSet(rs);
		
		TransactionLog transactionLog = new TransactionLog();		
		
		transactionLog.setId(super.getLong(rs, COL_ID));		
		transactionLog.setUserId(super.getLong(rs, COL_USER_ID));
		transactionLog.setOperationId(super.getInteger(rs, COL_OPERATION_ID));
		transactionLog.setOperationDate(super.getTimestamp(rs, COL_OPERATION_DATE));
		transactionLog.setEntityType(super.getInteger(rs, COL_ENTITY_TYPE));
		transactionLog.setEntityId(super.getLong(rs, COL_ENTITY_ID));
		
		transactionLog.setCreatedBy(super.getString(rs, COL_CREATED_BY));		
		transactionLog.setCreatedDate(super.getTimestamp(rs, COL_CREATED_DATE));
		transactionLog.setCreatedDateGmt(super.getTimestamp(rs, COL_CREATED_DATE_GMT));		
		
		return transactionLog;
	}
	@Override
	public void fillFieldMaps() {
		// TODO Auto-generated method stub
	}

	@Override
	public void initializeColList() {
		super.addColumn(COL_ID);
		super.addColumn(COL_USER_ID);
		super.addColumn(COL_OPERATION_ID);
		super.addColumn(COL_OPERATION_DATE);
		super.addColumn(COL_ENTITY_TYPE);
		super.addColumn(COL_ENTITY_ID);		
		super.addColumn(COL_CREATED_DATE);		
	}
	
}
