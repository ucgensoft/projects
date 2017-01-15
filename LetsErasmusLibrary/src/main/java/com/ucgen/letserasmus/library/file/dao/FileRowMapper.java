package com.ucgen.letserasmus.library.file.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.ucgen.common.dao.BaseRowMapper;
import com.ucgen.letserasmus.library.file.model.File;


public class FileRowMapper extends BaseRowMapper<File> {
	
	public static final String TABLE_NAME = "FILE";
	
	public static final String COL_ID = "ID";
	public static final String COL_FILE_NAME = "FILE_NAME";	
	public static final String COL_FILE_TYPE = "FILE_TYPE";
	public static final String COL_FILE_SIZE = "FILE_SIZE";
	public static final String COL_ENTITY_TYPE = "ENTITY_TYPE";
	public static final String COL_ENTITY_ID = "ENTITY_ID";
	public static final String COL_CREATED_BY = "CREATED_BY";
	public static final String COL_CREATED_DATE = "CREATED_DATE";
	public static final String COL_CREATED_DATE_GMT = "CREATED_DATE_GMT";
	public static final String COL_MODIFIED_BY = "MODIFIED_BY";
	public static final String COL_MODIFIED_DATE = "MODIFIED_DATE";
	public static final String COL_MODIFIED_DATE_GMT = "MODIFIED_DATE_GMT";	

	public FileRowMapper() {
		this(null);
	}
	
	public FileRowMapper(String tablePrefix) {
		super(TABLE_NAME, tablePrefix);
	}
	
	@Override
	public File mapRow(ResultSet rs, int rowNum) throws SQLException {
		super.initializeColSet(rs);
		File file = new File();		
		
		file.setId(super.getLong(rs, COL_ID));
		file.setFileName(super.getString(rs, COL_FILE_NAME));
		file.setFileType(super.getInteger(rs, COL_FILE_TYPE));
		file.setFileSize(super.getLong(rs, COL_FILE_SIZE));
		file.setEntityType(super.getInteger(rs, COL_ENTITY_TYPE));
		file.setEntityId(super.getLong(rs, COL_ENTITY_ID));
		file.setCreatedBy(super.getString(rs, COL_CREATED_BY));		
		file.setCreatedDate(super.getTimestamp(rs, COL_CREATED_DATE));
		file.setCreatedDateGmt(super.getTimestamp(rs, COL_CREATED_DATE_GMT));		
		file.setModifiedBy(super.getString(rs, COL_MODIFIED_BY));
		file.setModifiedDate(super.getTimestamp(rs, COL_MODIFIED_DATE));
		file.setModifiedDateGmt(super.getTimestamp(rs, COL_MODIFIED_DATE_GMT));		
		
		return file;
	}

	@Override
	public void fillFieldMaps() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initializeColList() {
		super.addColumn(COL_ID);
		super.addColumn(COL_FILE_NAME);
		super.addColumn(COL_FILE_TYPE);
		super.addColumn(COL_FILE_SIZE);
		super.addColumn(COL_ENTITY_TYPE);
		super.addColumn(COL_ENTITY_ID);
	}

}
