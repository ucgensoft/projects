package com.ucgen.letserasmus.library.comment.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.ucgen.common.dao.BaseRowMapper;
import com.ucgen.letserasmus.library.comment.model.Comment;

public class CommentRowMapper extends BaseRowMapper<Comment> {

	public static final String COL_ID = "ID";
	public static final String COL_ENTITY_TYPE = "ENTITY_TYPE";
	public static final String COL_ENTITY_ID = "ENTITY_ID";	
	public static final String COL_AUTHOR_USER_ID = "AUTHOR_USER_ID";
	public static final String COL_COMMENT_CONTENT = "COMMENT_CONTENT";
	public static final String COL_STATUS = "STATUS";
	public static final String COL_CREATED_BY = "CREATED_BY";
	public static final String COL_CREATED_DATE = "CREATED_DATE";
	public static final String COL_CREATED_DATE_GMT = "CREATED_DATE_GMT";
	public static final String COL_MODIFIED_BY = "MODIFIED_BY";
	public static final String COL_MODIFIED_DATE = "MODIFIED_DATE";
	public static final String COL_MODIFIED_DATE_GMT = "MODIFIED_DATE_GMT";

	
	@Override
	public Comment mapRow(ResultSet rs, int rowNum) throws SQLException {
		super.initializeColSet(rs);
		Comment comment = new Comment();
		
		comment.setId(super.getLong(rs, COL_ID));
		comment.setEntityType(super.getInteger(rs,COL_ENTITY_TYPE));
		comment.setEntityId(super.getLong(rs, COL_ENTITY_ID));
		comment.setAuthorUserId(super.getLong(rs, COL_AUTHOR_USER_ID));
		comment.setCommentContent(super.getString(rs, COL_COMMENT_CONTENT));
		comment.setStatus(super.getInteger(rs, COL_STATUS));
		comment.setCreatedBy(super.getString(rs, COL_CREATED_BY));		
		comment.setCreatedDate(super.getTimestamp(rs, COL_CREATED_DATE));
		comment.setCreatedDateGmt(super.getTimestamp(rs, COL_CREATED_DATE_GMT));		
		comment.setModifiedBy(super.getString(rs, COL_MODIFIED_BY));
		comment.setModifiedDate(super.getTimestamp(rs, COL_MODIFIED_DATE));
		comment.setModifiedDateGmt(super.getTimestamp(rs, COL_MODIFIED_DATE_GMT));		
		return comment;
	}

	@Override
	public void fillFieldMaps() {
		
		
	}

}
