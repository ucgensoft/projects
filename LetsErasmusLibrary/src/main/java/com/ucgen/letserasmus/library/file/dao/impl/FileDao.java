package com.ucgen.letserasmus.library.file.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import com.ucgen.common.dao.UtilityDao;
import com.ucgen.common.operationresult.EnmResultCode;
import com.ucgen.common.operationresult.ListOperationResult;
import com.ucgen.common.operationresult.OperationResult;
import com.ucgen.common.operationresult.ValueOperationResult;
import com.ucgen.letserasmus.library.file.dao.FileRowMapper;
import com.ucgen.letserasmus.library.file.dao.IFileDao;
import com.ucgen.letserasmus.library.file.model.File;

@Repository
public class FileDao extends JdbcDaoSupport implements IFileDao {

	private static final String LIST_FILE_SQL = " SELECT * FROM FILE WHERE 1=1 ";
	
	private static final String INSERT_FILE_SQL = " INSERT INTO FILE(FILE_NAME, FILE_TYPE, FILE_SIZE, ENTITY_TYPE, ENTITY_ID, CREATED_BY, CREATED_DATE, CREATED_DATE_GMT) VALUES (?,?,?,?,?,?,?,?) ";
	
	private static final String UPDATE_FILE_SQL = " UPDATE FILE SET FILE_NAME=?,FILE_TYPE=?,ENTITY_TYPE=?,ENTITY_ID=?,CREATED_BY=?,CREATED_DATE=?,CREATED_DATE_GMT=?,MODIFIED_BY=?,MODIFIED_DATE=?,MODIFIED_DATE_GMT=? WHERE ID=? ";
	
	private UtilityDao utilityDao;
		
	@Autowired
	public void setUtilityDao(UtilityDao utilityDao) {
		this.utilityDao = utilityDao;
	}
	
	@Autowired
	public FileDao(DataSource dataSource) {
		super();
		super.setDataSource(dataSource);
	}	
	
	@Override
	public ListOperationResult<File> listFile(File file) {
		
		ListOperationResult<File> listOperationResult = new ListOperationResult<File>();
		List<Object> argList = new ArrayList<Object>();
		StringBuilder sqlBuilder = new StringBuilder(LIST_FILE_SQL);
		
		if (file.getEntityType() != null) {
			sqlBuilder.append(" AND ENTITY_TYPE = ?");
			argList.add(file.getEntityType());
		}
		
		if (file.getEntityId() != null) {
			sqlBuilder.append(" AND ENTITY_ID = ?");
			argList.add(file.getEntityId());
		}
		
		if (file.getFileType() != null) {
			sqlBuilder.append(" AND FILE_TYPE = ?");
			argList.add(file.getFileType());
		}
		
		if (file.getId() != null) {
			sqlBuilder.append(" AND ID = ?");
			argList.add(file.getId());
		}
		
		sqlBuilder.append(" ORDER BY ID");
		
		List<File> fileList = super.getJdbcTemplate().query(sqlBuilder.toString(), argList.toArray(), new FileRowMapper());		
		
		listOperationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
		listOperationResult.setObjectList(fileList);
		
		return listOperationResult;
	}

	@Override
	public OperationResult insertFile(File file) {

		OperationResult operationResult = new OperationResult();
		
		List<Object> argList = new ArrayList<Object>();
		
		argList.add(file.getFileName());
		argList.add(file.getFileType());
		argList.add(file.getFileSize());
		argList.add(file.getEntityType());
		argList.add(file.getEntityId());
		argList.add(file.getCreatedBy());
		argList.add(file.getCreatedDate());		
		argList.add(file.getCreatedDateGmt());
		
		this.getJdbcTemplate().update(INSERT_FILE_SQL, argList.toArray());
		
		operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
		
		file.setId(this.utilityDao.getLastIncrementId());
		
		return operationResult;
	}

	@Override
	public ValueOperationResult<Integer> updateFile(File file) 
	{
		ValueOperationResult<Integer> operationResult = new ValueOperationResult<Integer>();		
		List<Object> argList = new ArrayList<Object>();
		argList.add(file.getFileName());
		argList.add(file.getFileType());
		argList.add(file.getEntityType());
		argList.add(file.getEntityId());
		argList.add(file.getCreatedBy());
		argList.add(file.getCreatedDate());		
		argList.add(file.getCreatedDateGmt());
		argList.add(file.getModifiedBy());
		argList.add(file.getModifiedDate());
		argList.add(file.getModifiedDateGmt());
		argList.add(file.getId());
		int updatedRowCount =  this.getJdbcTemplate().update(UPDATE_FILE_SQL, argList.toArray() );
		
		operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
		operationResult.setResultValue(updatedRowCount);
		
		return operationResult;
	}

}
