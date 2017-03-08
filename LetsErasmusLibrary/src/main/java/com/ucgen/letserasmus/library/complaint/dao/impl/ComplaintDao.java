package com.ucgen.letserasmus.library.complaint.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import com.ucgen.common.dao.UtilityDao;
import com.ucgen.common.exception.operation.OperationResultException;
import com.ucgen.common.operationresult.EnmResultCode;
import com.ucgen.common.operationresult.OperationResult;
import com.ucgen.common.operationresult.ValueOperationResult;
import com.ucgen.letserasmus.library.common.enumeration.EnmEntityType;
import com.ucgen.letserasmus.library.common.model.BaseModel;
import com.ucgen.letserasmus.library.complaint.dao.ComplaintRowMapper;
import com.ucgen.letserasmus.library.complaint.dao.IComplaintDao;
import com.ucgen.letserasmus.library.complaint.model.Complaint;

@Repository
public class ComplaintDao extends JdbcDaoSupport implements IComplaintDao {
		
	private static final String INSERT_COMPLAINT_SQL = "INSERT INTO COMPLAINT (USER_ID, HOST_USER_ID, ENTITY_TYPE, ENTITY_ID, " 
			+ " DESCRIPTION, STATUS, CREATED_BY, CREATED_DATE) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
	
	private static final String DELETE_COMPLAINT_SQL = "DELETE FROM COMPLAINT WHERE ID = ?";
	
	private UtilityDao utilityDao;
	
	@Autowired
	public void setUtilityDao(UtilityDao utilityDao) {
		this.utilityDao = utilityDao;
	}
	
	@Autowired
	public ComplaintDao(DataSource dataSource) {
		super();
		super.setDataSource(dataSource);
	}
	
	@Override
	public OperationResult insertComplaint(Complaint complaint) throws OperationResultException {
		OperationResult operationResult = new OperationResult();
		
		List<Object> argList = new ArrayList<Object>();
				
		argList.add(complaint.getUserId());
		argList.add(complaint.getHostUserId());
		argList.add(complaint.getEntityType());
		argList.add(complaint.getEntityId());
		argList.add(complaint.getDescription());
		argList.add(complaint.getStatus());
		argList.add(complaint.getCreatedBy());
		argList.add(complaint.getCreatedDate());
		
		this.getJdbcTemplate().update(INSERT_COMPLAINT_SQL, argList.toArray());
		
		complaint.setId(this.utilityDao.getLastIncrementId());
				
		operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
						
		return operationResult;
	}
	
	@Override
	public List<Complaint> listComplaint(Complaint complaint, BaseModel entity, boolean entityFlag, boolean userFlag, boolean hostUserFlag) {
		StringBuilder sqlBuilder = new StringBuilder();
		List<Object> argList = new ArrayList<Object>();
		
		ComplaintRowMapper complaintRowMapper = new ComplaintRowMapper();
		
		if (userFlag) {
			complaintRowMapper.addFKey(ComplaintRowMapper.FKEY_USER);
		}
		if (hostUserFlag) {
			complaintRowMapper.addFKey(ComplaintRowMapper.FKEY_HOST_USER);
		}
		if (entityFlag) {
			if (complaint.getEntityType().equals(EnmEntityType.PLACE.getId())) {
				complaintRowMapper.addEntiyFKey(ComplaintRowMapper.FKEY_ENTITY, EnmEntityType.PLACE);
			}
		}
		
		sqlBuilder.append(complaintRowMapper.getSelectSqlWithForeignKeys());
		
		if (complaint != null) {
			if (complaint.getId() != null) {
				sqlBuilder.append(" AND " + complaintRowMapper.getCriteriaColumnName(ComplaintRowMapper.COL_ID) + " = ? ");
				argList.add(complaint.getId());
			}
			if (complaint.getUserId() != null) {
				sqlBuilder.append(" AND " + complaintRowMapper.getCriteriaColumnName(ComplaintRowMapper.COL_USER_ID) + " = ? ");
				argList.add(complaint.getUserId());
			}
			if (complaint.getHostUserId() != null) {
				sqlBuilder.append(" AND " + complaintRowMapper.getCriteriaColumnName(ComplaintRowMapper.COL_HOST_USER_ID) + " = ? ");
				argList.add(complaint.getHostUserId());
			}
			if (complaint.getEntityType() != null) {
				sqlBuilder.append(" AND " + complaintRowMapper.getCriteriaColumnName(ComplaintRowMapper.COL_ENTITY_TYPE) + " = ? ");
				argList.add(complaint.getEntityType());
			}
			if (complaint.getEntityId() != null) {
				sqlBuilder.append(" AND " + complaintRowMapper.getCriteriaColumnName(ComplaintRowMapper.COL_ENTITY_ID) + " = ? ");
				argList.add(complaint.getEntityId());
			}
			
			if (complaint.getEntityId() != null) {
				sqlBuilder.append(" AND " + complaintRowMapper.getCriteriaColumnName(ComplaintRowMapper.COL_STATUS) + " = ? ");
				argList.add(complaint.getEntityId());
			}
			
		}
				
		List<Complaint> complaintList = super.getJdbcTemplate().query(sqlBuilder.toString(), argList.toArray(), complaintRowMapper);		
				
		return complaintList;
	}

	@Override
	public ValueOperationResult<Integer> deleteComplaint(Long id) {
		ValueOperationResult<Integer> operationResult = new ValueOperationResult<Integer>();
		
		Integer deletedRowCount = this.getJdbcTemplate().update(DELETE_COMPLAINT_SQL, new Object[] { id });
		
		operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
		operationResult.setResultValue(deletedRowCount);
		
		return operationResult;
	}

}
