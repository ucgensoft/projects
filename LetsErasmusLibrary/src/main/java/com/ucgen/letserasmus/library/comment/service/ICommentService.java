package com.ucgen.letserasmus.library.comment.service;

import java.util.List;

import com.ucgen.common.operationresult.ListOperationResult;
import com.ucgen.common.operationresult.OperationResult;
import com.ucgen.letserasmus.library.comment.model.Comment;

public interface ICommentService {

	ListOperationResult<Comment> listComment();
	OperationResult insertComment(List<Comment> commentList);
	
}
