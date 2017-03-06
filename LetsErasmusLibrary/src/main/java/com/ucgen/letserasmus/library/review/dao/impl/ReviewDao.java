package com.ucgen.letserasmus.library.review.dao.impl;

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
import com.ucgen.letserasmus.library.common.enumeration.EnmEntityType;
import com.ucgen.letserasmus.library.common.model.BaseModel;
import com.ucgen.letserasmus.library.review.dao.IReviewDao;
import com.ucgen.letserasmus.library.review.dao.ReviewRowMapper;
import com.ucgen.letserasmus.library.review.model.Review;

@Repository
public class ReviewDao extends JdbcDaoSupport implements IReviewDao {
		
	private static final String INSERT_REVIEW_SQL = "INSERT INTO REVIEW (USER_ID, REVIEWED_USER_ID, ENTITY_TYPE, ENTITY_ID, " 
			+ " RANK, DESCRIPTION, CREATED_BY, CREATED_DATE) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
	
	private UtilityDao utilityDao;
	
	@Autowired
	public void setUtilityDao(UtilityDao utilityDao) {
		this.utilityDao = utilityDao;
	}
	
	@Autowired
	public ReviewDao(DataSource dataSource) {
		super();
		super.setDataSource(dataSource);
	}
	
	@Override
	public OperationResult insertReview(Review review) throws OperationResultException {
		OperationResult operationResult = new OperationResult();
		
		List<Object> argList = new ArrayList<Object>();
				
		argList.add(review.getUserId());
		argList.add(review.getReviewedUserId());
		argList.add(review.getEntityType());
		argList.add(review.getEntityId());
		argList.add(review.getRank());
		argList.add(review.getDescription());
		argList.add(review.getCreatedBy());
		argList.add(review.getCreatedDate());
		
		this.getJdbcTemplate().update(INSERT_REVIEW_SQL, argList.toArray());
		
		review.setId(this.utilityDao.getLastIncrementId());
				
		operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
						
		return operationResult;
	}
	
	@Override
	public List<Review> listReview(Review review, BaseModel entity, boolean entityFlag, boolean userFlag, boolean reviewedUserFlag) {
		StringBuilder sqlBuilder = new StringBuilder();
		List<Object> argList = new ArrayList<Object>();
		
		ReviewRowMapper reviewRowMapper = new ReviewRowMapper();
		
		if (userFlag) {
			reviewRowMapper.addFKey(ReviewRowMapper.FKEY_USER);
		}
		if (reviewedUserFlag) {
			reviewRowMapper.addFKey(ReviewRowMapper.FKEY_REVIEWED_USER);
		}
		if (entityFlag) {
			if (review.getEntityType().equals(EnmEntityType.RESERVATION.getId())) {
				reviewRowMapper.addEntiyFKey(ReviewRowMapper.FKEY_ENTITY, EnmEntityType.RESERVATION);
			}
		}
		
		sqlBuilder.append(reviewRowMapper.getSelectSqlWithForeignKeys());
		
		if (review != null) {
			if (review.getId() != null) {
				sqlBuilder.append(" AND " + reviewRowMapper.getCriteriaColumnName(ReviewRowMapper.COL_ID) + " = ? ");
				argList.add(review.getId());
			}
			if (review.getUserId() != null) {
				sqlBuilder.append(" AND " + reviewRowMapper.getCriteriaColumnName(ReviewRowMapper.COL_USER_ID) + " = ? ");
				argList.add(review.getUserId());
			}
			if (review.getReviewedUserId() != null) {
				sqlBuilder.append(" AND " + reviewRowMapper.getCriteriaColumnName(ReviewRowMapper.COL_REVIEWED_USER_ID) + " = ? ");
				argList.add(review.getReviewedUserId());
			}
			if (review.getEntityType() != null) {
				sqlBuilder.append(" AND " + reviewRowMapper.getCriteriaColumnName(ReviewRowMapper.COL_ENTITY_TYPE) + " = ? ");
				argList.add(review.getEntityType());
			}
			if (review.getEntityId() != null) {
				sqlBuilder.append(" AND " + reviewRowMapper.getCriteriaColumnName(ReviewRowMapper.COL_ENTITY_ID) + " = ? ");
				argList.add(review.getEntityId());
			}
			
		}
				
		List<Review> reviewList = super.getJdbcTemplate().query(sqlBuilder.toString(), argList.toArray(), reviewRowMapper);		
				
		return reviewList;
	}

}
