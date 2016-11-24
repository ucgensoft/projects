package com.ucgen.letserasmus.library.comment.dao;

import java.util.List;

import com.ucgen.common.operationresult.ListOperationResult;
import com.ucgen.common.operationresult.OperationResult;
import com.ucgen.letserasmus.library.comment.model.Comment;

public interface ICommentDao {
	ListOperationResult<Comment> listComment();
	OperationResult insertComment(List<Comment> commentList);
}
