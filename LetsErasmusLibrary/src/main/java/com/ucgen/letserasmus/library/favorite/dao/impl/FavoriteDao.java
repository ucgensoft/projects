package com.ucgen.letserasmus.library.favorite.dao.impl;

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
import com.ucgen.letserasmus.library.favorite.dao.FavoriteRowMapper;
import com.ucgen.letserasmus.library.favorite.dao.IFavoriteDao;
import com.ucgen.letserasmus.library.favorite.model.Favorite;
import com.ucgen.letserasmus.library.review.dao.ReviewRowMapper;

@Repository
public class FavoriteDao extends JdbcDaoSupport implements IFavoriteDao {
		
	private static final String INSERT_FAVORITE_SQL = "INSERT INTO FAVORITE (USER_ID, HOST_USER_ID, ENTITY_TYPE, ENTITY_ID, " 
			+ " CREATED_BY, CREATED_DATE) VALUES (?, ?, ?, ?, ?, ?)";
	
	private static final String DELETE_FAVORITE_SQL = "DELETE FROM FAVORITE WHERE ID = ?";
	
	private UtilityDao utilityDao;
	
	@Autowired
	public void setUtilityDao(UtilityDao utilityDao) {
		this.utilityDao = utilityDao;
	}
	
	@Autowired
	public FavoriteDao(DataSource dataSource) {
		super();
		super.setDataSource(dataSource);
	}
	
	@Override
	public OperationResult insertFavorite(Favorite favorite) throws OperationResultException {
		OperationResult operationResult = new OperationResult();
		
		List<Object> argList = new ArrayList<Object>();
				
		argList.add(favorite.getUserId());
		argList.add(favorite.getHostUserId());
		argList.add(favorite.getEntityType());
		argList.add(favorite.getEntityId());
		argList.add(favorite.getCreatedBy());
		argList.add(favorite.getCreatedDate());
		
		this.getJdbcTemplate().update(INSERT_FAVORITE_SQL, argList.toArray());
		
		favorite.setId(this.utilityDao.getLastIncrementId());
				
		operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
						
		return operationResult;
	}
	
	@Override
	public List<Favorite> listFavorite(Favorite favorite, BaseModel entity, boolean entityFlag, boolean userFlag, boolean hostUserFlag) {
		StringBuilder sqlBuilder = new StringBuilder();
		List<Object> argList = new ArrayList<Object>();
		
		FavoriteRowMapper favoriteRowMapper = new FavoriteRowMapper();
		
		if (userFlag) {
			favoriteRowMapper.addFKey(FavoriteRowMapper.FKEY_USER);
		}
		if (hostUserFlag) {
			favoriteRowMapper.addFKey(FavoriteRowMapper.FKEY_HOST_USER);
		}
		if (entityFlag) {
			if (favorite.getEntityType().equals(EnmEntityType.PLACE.getId())) {
				favoriteRowMapper.addEntiyFKey(FavoriteRowMapper.FKEY_ENTITY, EnmEntityType.PLACE);
			}
		}
		
		sqlBuilder.append(favoriteRowMapper.getSelectSqlWithForeignKeys());
		
		if (favorite != null) {
			if (favorite.getId() != null) {
				sqlBuilder.append(" AND " + favoriteRowMapper.getCriteriaColumnName(FavoriteRowMapper.COL_ID) + " = ? ");
				argList.add(favorite.getId());
			}
			if (favorite.getUserId() != null) {
				sqlBuilder.append(" AND " + favoriteRowMapper.getCriteriaColumnName(FavoriteRowMapper.COL_USER_ID) + " = ? ");
				argList.add(favorite.getUserId());
			}
			if (favorite.getHostUserId() != null) {
				sqlBuilder.append(" AND " + favoriteRowMapper.getCriteriaColumnName(FavoriteRowMapper.COL_HOST_USER_ID) + " = ? ");
				argList.add(favorite.getHostUserId());
			}
			if (favorite.getEntityType() != null) {
				sqlBuilder.append(" AND " + favoriteRowMapper.getCriteriaColumnName(FavoriteRowMapper.COL_ENTITY_TYPE) + " = ? ");
				argList.add(favorite.getEntityType());
			}
			if (favorite.getEntityId() != null) {
				sqlBuilder.append(" AND " + favoriteRowMapper.getCriteriaColumnName(FavoriteRowMapper.COL_ENTITY_ID) + " = ? ");
				argList.add(favorite.getEntityId());
			}
			
		}
				
		List<Favorite> favoriteList = super.getJdbcTemplate().query(sqlBuilder.toString(), argList.toArray(), favoriteRowMapper);		
				
		return favoriteList;
	}

	@Override
	public ValueOperationResult<Integer> deleteFavorite(Long id) {
		ValueOperationResult<Integer> operationResult = new ValueOperationResult<Integer>();
		
		Integer deletedRowCount = this.getJdbcTemplate().update(DELETE_FAVORITE_SQL, new Object[] { id });
		
		operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
		operationResult.setResultValue(deletedRowCount);
		
		return operationResult;
	}

}
