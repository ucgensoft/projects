package com.ucgen.letserasmus.web.api.review;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ucgen.common.exception.operation.OperationResultException;
import com.ucgen.common.operationresult.EnmResultCode;
import com.ucgen.common.operationresult.OperationResult;
import com.ucgen.common.operationresult.ValueOperationResult;
import com.ucgen.common.util.CommonUtil;
import com.ucgen.common.util.FileLogger;
import com.ucgen.letserasmus.library.common.enumeration.EnmEntityType;
import com.ucgen.letserasmus.library.common.enumeration.EnmErrorCode;
import com.ucgen.letserasmus.library.common.enumeration.EnmSize;
import com.ucgen.letserasmus.library.reservation.model.Reservation;
import com.ucgen.letserasmus.library.reservation.service.IReservationService;
import com.ucgen.letserasmus.library.review.model.Review;
import com.ucgen.letserasmus.library.review.service.IReviewService;
import com.ucgen.letserasmus.library.user.model.User;
import com.ucgen.letserasmus.web.api.BaseApiController;
import com.ucgen.letserasmus.web.view.application.AppConstants;
import com.ucgen.letserasmus.web.view.application.WebApplication;

@RestController
public class ApiReviewController extends BaseApiController {

	private IReviewService reviewService;
	private IReservationService reservationService;
	private WebApplication webApplication;
	
	@Autowired
	public void setWebApplication(WebApplication webApplication) {
		this.webApplication = webApplication;
	}

	@Autowired
	public void setReviewService(IReviewService reviewService) {
		this.reviewService = reviewService;
	}
	
	@Autowired
	public void setReservationService(IReservationService reservationService) {
		this.reservationService = reservationService;
	}
	
	@RequestMapping(value = "/api/review/listuserreview", method = RequestMethod.GET)
    public ResponseEntity<ValueOperationResult<Map<String, List<Review>>>> listUserReview(@RequestParam("userId") Long userId, HttpSession session) {
		ValueOperationResult<Map<String, List<Review>>> operationResult = new ValueOperationResult<Map<String, List<Review>>>();
		
		try {
			if (userId != null) {
				
				Review review = new Review();
				review.setReviewedUserId(userId);
				
				List<Review> reviewList = this.reviewService.listReview(review, null, false, true, false);
				
				Map<String, List<Review>> reviewMap = new HashMap<String, List<Review>>();
				List<Review> guestReviewList = new ArrayList<Review>();
				List<Review> hostReviewList = new ArrayList<Review>();
				reviewMap.put("guestReviewList", guestReviewList);
				reviewMap.put("hostReviewList", hostReviewList);
				
				if (reviewList != null && reviewList.size() > 0) {
					for (Review tmpReview : reviewList) {
						User user = tmpReview.getUser();
						
						User tmpUser = new User();
						tmpUser.setId(user.getId());
						tmpUser.setFirstName(user.getFirstName());
						
						String smallProfileUrl = this.webApplication.getUserPhotoUrl(user.getId(), user.getProfilePhotoId(), EnmSize.SMALL.getValue());
						tmpUser.setProfileImageUrl(smallProfileUrl);
						
						tmpReview.setUser(tmpUser);
						
						if (tmpReview.getEntityType().equals(EnmEntityType.USER.getId())) {
							hostReviewList.add(tmpReview);
						} else {
							guestReviewList.add(tmpReview);
						}
					}
				}
				
				operationResult.setResultValue(reviewMap);
				operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
			} else {
				operationResult.setResultCode(EnmResultCode.ERROR.getValue());
				operationResult.setResultDesc(AppConstants.MISSING_MANDATORY_PARAM);
			}
		} catch (Exception e) {
			FileLogger.log(Level.ERROR, "ApiReviewController-listUserReview()-Error: " + CommonUtil.getExceptionMessage(e));
			operationResult.setResultCode(EnmResultCode.EXCEPTION.getValue());
			operationResult.setResultDesc(AppConstants.CREATE_OPERATION_FAIL);
		}
		return new ResponseEntity<ValueOperationResult<Map<String, List<Review>>>>(operationResult, HttpStatus.OK);
    }
	
	@RequestMapping(value = "/api/review/listentityreview", method = RequestMethod.GET)
    public ResponseEntity<ValueOperationResult<List<Review>>> listEntityReview(@RequestParam("entityId") Long entityId, HttpSession session) {
		ValueOperationResult<List<Review>> operationResult = new ValueOperationResult<List<Review>>();
		
		try {
			if (entityId != null) {
				
				Review review = new Review();
				review.setEntityId(entityId);
				review.setEntityType(EnmEntityType.RESERVATION.getId());
				
				List<Review> reviewList = this.reviewService.listReview(review, null, true, true, false);
				
				List<Review> newReviewList = new ArrayList<Review>();
				
				if (reviewList != null && reviewList.size() > 0) {
					for (Review tmpReview : reviewList) {
						User user = tmpReview.getUser();
						Reservation reservation = (Reservation) tmpReview.getEntity();
						
						if (!user.getId().equals(reservation.getHostUserId())) {
							User tmpUser = new User();
							tmpUser.setId(user.getId());
							tmpUser.setFirstName(user.getFirstName());
							
							String smallProfileUrl = this.webApplication.getUserPhotoUrl(user.getId(), user.getProfilePhotoId(), EnmSize.SMALL.getValue());
							tmpUser.setProfileImageUrl(smallProfileUrl);
							
							tmpReview.setUser(tmpUser);
							
							newReviewList.add(tmpReview);
						}
					}
				}
				
				operationResult.setResultValue(newReviewList);
				operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
			} else {
				operationResult.setResultCode(EnmResultCode.ERROR.getValue());
				operationResult.setResultDesc(AppConstants.MISSING_MANDATORY_PARAM);
			}
		} catch (Exception e) {
			FileLogger.log(Level.ERROR, "ApiReviewController-listEntityReview()-Error: " + CommonUtil.getExceptionMessage(e));
			operationResult.setResultCode(EnmResultCode.EXCEPTION.getValue());
			operationResult.setResultDesc(AppConstants.CREATE_OPERATION_FAIL);
		}
		return new ResponseEntity<ValueOperationResult<List<Review>>>(operationResult, HttpStatus.OK);
    }
	
	@RequestMapping(value = "/api/review/createreview", method = RequestMethod.POST)
    public ResponseEntity<OperationResult> createReview(@RequestBody Review review, HttpSession session) {
		OperationResult operationResult = new OperationResult();
		
		try {
			User user = super.getSessionUser(session);
			if (user != null) {
				EnmEntityType entityType = EnmEntityType.getEntityType(review.getEntityType());
				if (review.getEntityType() != null && review.getEntityId() != null 
						&& entityType != null
						&& entityType.equals(EnmEntityType.RESERVATION)
						&& review.getRank() != null && !review.getDescription().trim().isEmpty()) {
					if (entityType.equals(EnmEntityType.RESERVATION)) {
						operationResult = this.reviewReservation(user, review);
					}
					
				} else {
					operationResult.setResultCode(EnmResultCode.ERROR.getValue());
					operationResult.setResultDesc(AppConstants.MISSING_MANDATORY_PARAM);
				}
			} else {
				operationResult.setErrorCode(EnmErrorCode.USER_NOT_LOGGED_IN.getId());
				operationResult.setResultCode(EnmResultCode.ERROR.getValue());
				operationResult.setResultDesc(AppConstants.USER_NOT_LOGGED_IN);
			}
		} catch (Exception e) {
			FileLogger.log(Level.ERROR, "ApiReviewController-createReview()-Error: " + CommonUtil.getExceptionMessage(e));
			operationResult.setResultCode(EnmResultCode.EXCEPTION.getValue());
			operationResult.setResultDesc(AppConstants.CREATE_OPERATION_FAIL);
		}
		return new ResponseEntity<OperationResult>(operationResult, HttpStatus.OK);
    }
	
	@Transactional(rollbackFor = Exception.class)
	private OperationResult reviewReservation(User user, Review review) throws OperationResultException {
		OperationResult operationResult = new OperationResult();
		
		Reservation reservation = new Reservation();
		reservation.setId(review.getEntityId());
		
		List<Reservation> reservationList = this.reservationService.list(reservation, false, false, false);
		
		if (reservationList != null && reservationList.size() > 0) {
			reservation = reservationList.get(0);
			if (user.getId().equals(reservation.getHostUserId()) 
					|| user.getId().equals(reservation.getClientUserId())) {
				Date operationDate = new Date();
				boolean userHasReview = false;
				
				if (user.getId().equals(reservation.getHostUserId()) 
						&& reservation.getHostReviewId() != null) {
					userHasReview = true;
				}
				
				if (user.getId().equals(reservation.getClientUserId()) 
						&& reservation.getClientReviewId() != null) {
					userHasReview = true;
				}
				
				if (!userHasReview) {
					Review newReview = new Review();
					newReview.setUserId(user.getId());
					if (user.getId().equals(reservation.getHostUserId())) {
						newReview.setEntityType(EnmEntityType.USER.getId());
						newReview.setEntityId(reservation.getClientUserId());
						newReview.setReviewedUserId(reservation.getClientUserId());
					} else {
						newReview.setEntityType(EnmEntityType.PLACE.getId());
						newReview.setEntityId(reservation.getPlaceId());
						newReview.setReviewedUserId(reservation.getHostUserId());
					}
					newReview.setRank(review.getRank());
					newReview.setDescription(review.getDescription());
					newReview.setCreatedBy(user.getFullName());
					newReview.setCreatedDate(operationDate);
					
					OperationResult createResult = this.reviewService.insertReview(newReview);
					if (OperationResult.isResultSucces(createResult)) {
						if (user.getId().equals(reservation.getHostUserId())) {
							reservation.setHostReviewId(newReview.getId());
						} else {
							reservation.setClientReviewId(newReview.getId());
						}
						reservation.setModifiedBy(user.getFullName());
						reservation.setModifiedDate(operationDate);
						
						OperationResult updateResult = this.reservationService.update(reservation, null);
						if (OperationResult.isResultSucces(updateResult)) {
							operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
						} else {
							operationResult.setResultCode(EnmResultCode.ERROR.getValue());
							operationResult.setResultDesc(AppConstants.REVIEW_NOT_SAVED);
							throw new OperationResultException(operationResult);
						}
					} else {
						operationResult.setResultCode(EnmResultCode.ERROR.getValue());
						operationResult.setResultDesc(AppConstants.REVIEW_NOT_SAVED);
					}
				} else {
					operationResult.setResultCode(EnmResultCode.ERROR.getValue());
					operationResult.setResultDesc(AppConstants.REVIEW_DOUBLE);
				}							
			} else {
				operationResult.setResultCode(EnmResultCode.ERROR.getValue());
				operationResult.setResultDesc(AppConstants.UNAUTHORIZED_OPERATION);
			}
		} else {
			operationResult.setResultCode(EnmResultCode.ERROR.getValue());
			operationResult.setResultDesc(AppConstants.PLACE_LIST_NOT_FOUND);
		}
		return operationResult;
	}
	
}
