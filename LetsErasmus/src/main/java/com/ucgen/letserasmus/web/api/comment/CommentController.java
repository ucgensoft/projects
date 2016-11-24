package com.ucgen.letserasmus.web.api.comment;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ucgen.common.operationresult.ListOperationResult;
import com.ucgen.common.operationresult.OperationResult;
import com.ucgen.letserasmus.library.comment.model.Comment;
import com.ucgen.letserasmus.library.comment.service.ICommentService;
import com.ucgen.letserasmus.web.api.BaseApiController;

@RestController
public class CommentController extends BaseApiController {

	private ICommentService commentService;

	public ICommentService getCommentService() {
		return commentService;
	}

	@Autowired
	public void setCommentService(ICommentService commentService) {
		this.commentService = commentService;
	}
	
	@RequestMapping(value = "/api/comment/list", method = RequestMethod.GET)
    public ResponseEntity<List<Comment>> listComment(@RequestParam Map<String,String> requestParams) {
		List<Comment> commentList = null;
		HttpStatus httpStatus = null;
		ListOperationResult<Comment> listResult = this.commentService.listComment();
		if (OperationResult.isResultSucces(listResult)) {
			commentList = listResult.getObjectList();
			httpStatus = HttpStatus.OK;
		} else {
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<List<Comment>>(commentList, httpStatus);
    }
	
}
