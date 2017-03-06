package com.ucgen.letserasmus.library.review.dao;

import java.util.List;

import com.ucgen.common.exception.operation.OperationResultException;
import com.ucgen.common.operationresult.OperationResult;
import com.ucgen.letserasmus.library.common.model.BaseModel;
import com.ucgen.letserasmus.library.review.model.Review;

public interface IReviewDao {

	OperationResult insertReview(Review review) throws OperationResultException;
	
	List<Review> listReview(Review review, BaseModel entity, boolean entityFlag, boolean userFlag, boolean reviewedUserFlag);
	
}
