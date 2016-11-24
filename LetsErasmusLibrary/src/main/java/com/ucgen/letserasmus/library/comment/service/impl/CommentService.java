package com.ucgen.letserasmus.library.comment.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ucgen.common.operationresult.ListOperationResult;
import com.ucgen.common.operationresult.OperationResult;
import com.ucgen.letserasmus.library.comment.dao.ICommentDao;
import com.ucgen.letserasmus.library.comment.model.Comment;
import com.ucgen.letserasmus.library.comment.service.ICommentService;

@Service
public class CommentService implements ICommentService {

	private ICommentDao commentDao;
	
	public ICommentDao getCommentDao() {
		return commentDao;
	}

	@Autowired
	public void setCommentDao(ICommentDao commentDao) {
		this.commentDao = commentDao;
	}

	@Override
	public ListOperationResult<Comment> listComment() {
		return this.commentDao.listComment();
	}

	@Override
	public OperationResult insertComment(List<Comment> commentList) {
		return this.commentDao.insertComment(commentList);
	}

}
