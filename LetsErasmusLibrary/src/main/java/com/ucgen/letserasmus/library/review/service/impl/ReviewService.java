package com.ucgen.letserasmus.library.review.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ucgen.common.exception.operation.OperationResultException;
import com.ucgen.common.operationresult.OperationResult;
import com.ucgen.letserasmus.library.common.model.BaseModel;
import com.ucgen.letserasmus.library.review.dao.IReviewDao;
import com.ucgen.letserasmus.library.review.model.Review;
import com.ucgen.letserasmus.library.review.service.IReviewService;

@Service
public class ReviewService implements IReviewService {

	private IReviewDao reviewDao;
	
	@Autowired
	public void setReviewDao(IReviewDao reviewDao) {
		this.reviewDao = reviewDao;
	}

	@Override
	public OperationResult insertReview(Review review) throws OperationResultException {
		return this.reviewDao.insertReview(review);
	}

	@Override
	public List<Review> listReview(Review review, BaseModel entity, boolean entityFlag, boolean userFlag, boolean reviewedUserFlag) {
		return this.reviewDao.listReview(review, entity, entityFlag, userFlag, reviewedUserFlag);
	}
	
}