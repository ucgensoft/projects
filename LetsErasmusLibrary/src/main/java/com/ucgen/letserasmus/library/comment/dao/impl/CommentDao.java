package com.ucgen.letserasmus.library.comment.dao.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import com.ucgen.common.operationresult.EnmResultCode;
import com.ucgen.common.operationresult.ListOperationResult;
import com.ucgen.common.operationresult.OperationResult;
import com.ucgen.letserasmus.library.comment.dao.CommentRowMapper;
import com.ucgen.letserasmus.library.comment.dao.ICommentDao;
import com.ucgen.letserasmus.library.comment.model.Comment;

@Repository
public class CommentDao extends JdbcDaoSupport implements ICommentDao {
	
	private static final String LIST_COMMENT_SQL = " select `ID`, `ENTITY_TYPE`, `ENTITY_ID`, `AUTHOR_USER_ID`, `COMMENT_CONTENT`, `STATUS`, `CREATED_BY`, `CREATED_DATE`, `CREATED_DATE_GMT`, `MODIFIED_BY`, `MODIFIED_DATE`, `MODIFIED_DATE_GMT` FROM COMMENT ";
	private static final String INSERT_COMMENT_SQL = "INSERT INTO `COMMENT` (`ID`, `ENTITY_TYPE`, `ENTITY_ID`, `AUTHOR_USER_ID`, `COMMENT_CONTENT`, `STATUS`, `CREATED_BY`, `CREATED_DATE`, `CREATED_DATE_GMT`, `MODIFIED_BY`, `MODIFIED_DATE`, `MODIFIED_DATE_GMT`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";

	@Autowired
	public CommentDao(DataSource dataSource) {
		super();
		super.setDataSource(dataSource);
	}
	
	@Override
	public ListOperationResult<Comment> listComment() {
		ListOperationResult<Comment> listOperationResult = new ListOperationResult<Comment>();
		
		List<Comment> commentList = super.getJdbcTemplate().query(LIST_COMMENT_SQL, new CommentRowMapper());
		
		listOperationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
		listOperationResult.setObjectList(commentList);
		
		return listOperationResult;
	}

	@Override
	public OperationResult insertComment(final List<Comment> commentList) {
		
		OperationResult operationResult = new OperationResult();
		
		super.getJdbcTemplate().batchUpdate(INSERT_COMMENT_SQL,new BatchPreparedStatementSetter() 
		{
			
			@Override
			public void setValues(PreparedStatement ps, int index) throws SQLException {
				Comment comment = commentList.get(index);
				if(comment.getId()!=null){
					ps.setLong(1, comment.getId());
				}else {
					ps.setNull(1, Types.BIGINT);
				}
				ps.setInt(2,comment.getEntityType());
				ps.setLong(3,comment.getEntityId());
				ps.setLong(4,comment.getAuthorUserId());
				ps.setString(5,comment.getCommentContent());
				ps.setInt(6,comment.getStatus());
				ps.setString(7,comment.getCreatedBy());
				if(comment.getCreatedDate()!=null) {
					ps.setTimestamp(8, new java.sql.Timestamp(comment.getCreatedDate().getTime()));
				}else {
					ps.setNull(8, Types.TIMESTAMP);
				}					
				if(comment.getCreatedDateGmt()!=null) {
					ps.setTimestamp(9, new java.sql.Timestamp(comment.getCreatedDateGmt().getTime()));
				}else {
					ps.setNull(9, Types.TIMESTAMP);
				}
				ps.setString(10,comment.getModifiedBy());
				if(comment.getModifiedDate()!=null) {
					ps.setTimestamp(11, new java.sql.Timestamp(comment.getModifiedDate().getTime()));
				}else {
					ps.setNull(11, Types.TIMESTAMP);
				}
				if(comment.getModifiedDateGmt()!=null) {
					ps.setTimestamp(12, new java.sql.Timestamp(comment.getModifiedDateGmt().getTime()));
				}else {
					ps.setNull(12, Types.TIMESTAMP);
				}
				
			}
			
			@Override
			public int getBatchSize() {
				return commentList.size();
			}
		});
		
		operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
		return operationResult;
	}
	
}
