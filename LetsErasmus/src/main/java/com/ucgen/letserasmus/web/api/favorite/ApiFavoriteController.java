package com.ucgen.letserasmus.web.api.favorite;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ucgen.common.operationresult.EnmResultCode;
import com.ucgen.common.operationresult.OperationResult;
import com.ucgen.common.operationresult.ValueOperationResult;
import com.ucgen.common.util.CommonUtil;
import com.ucgen.common.util.FileLogger;
import com.ucgen.letserasmus.library.common.enumeration.EnmEntityType;
import com.ucgen.letserasmus.library.common.enumeration.EnmErrorCode;
import com.ucgen.letserasmus.library.common.enumeration.EnmSize;
import com.ucgen.letserasmus.library.common.model.BaseModel;
import com.ucgen.letserasmus.library.favorite.model.Favorite;
import com.ucgen.letserasmus.library.favorite.service.IFavoriteService;
import com.ucgen.letserasmus.library.place.model.Place;
import com.ucgen.letserasmus.library.place.service.IPlaceService;
import com.ucgen.letserasmus.library.user.model.User;
import com.ucgen.letserasmus.web.api.BaseApiController;
import com.ucgen.letserasmus.web.view.application.AppConstants;
import com.ucgen.letserasmus.web.view.application.WebApplication;

@RestController
public class ApiFavoriteController extends BaseApiController {

	private IFavoriteService favoriteService;
	private WebApplication webApplication;
	private IPlaceService placeService;
	
	@Autowired
	public void setFavoriteService(IFavoriteService favoriteService) {
		this.favoriteService = favoriteService;
	}

	@Autowired
	public void setWebApplication(WebApplication webApplication) {
		this.webApplication = webApplication;
	}
	
	@Autowired
	public void setPlaceService(IPlaceService placeService) {
		this.placeService = placeService;
	}

	@RequestMapping(value = "/api/favorite/list", method = RequestMethod.GET)
    public ResponseEntity<ValueOperationResult<Map<Integer, Map<Long, Favorite>>>> listFavorite(HttpSession session) {
		ValueOperationResult<Map<Integer, Map<Long, Favorite>>> operationResult = new ValueOperationResult<Map<Integer, Map<Long, Favorite>>>();
		
		try {
			User sessionUser = this.getSessionUser(session);
			if (sessionUser != null) {
				
				Favorite favorite = new Favorite();
				favorite.setUserId(sessionUser.getId());
				favorite.setEntityType(EnmEntityType.PLACE.getId());
				
				if (sessionUser.getFavoriteMap() == null) {
					List<Favorite> favoriteList = this.favoriteService.listFavorite(favorite, null, true, false, true);
					
					if (favoriteList != null && favoriteList.size() > 0) {
						for (Favorite tmpFavorite : favoriteList) {
							User user = tmpFavorite.getHostUser();
							
							User tmpUser = new User();
							tmpUser.setId(user.getId());
							tmpUser.setFirstName(user.getFirstName());
							
							String smallProfileUrl = this.webApplication.getUserPhotoUrl(user.getId(), user.getProfilePhotoId(), EnmSize.SMALL.getValue());
							tmpUser.setProfileImageUrl(smallProfileUrl);
							
							tmpFavorite.setHostUser(tmpUser);
							
							BaseModel entity = this.favoriteService.getEntityDetails(tmpFavorite.getEntityType(), tmpFavorite.getEntityId());
							
							tmpFavorite.setEntity(entity);
						}
					}
					sessionUser.createFavoriteMap();
					sessionUser.addFavoriteList(favoriteList);
				}
				
				operationResult.setResultValue(sessionUser.getFavoriteMap());
				operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
			} else {
				operationResult.setErrorCode(EnmErrorCode.USER_NOT_LOGGED_IN.getId());
				operationResult.setResultCode(EnmResultCode.ERROR.getValue());
				operationResult.setResultDesc(AppConstants.USER_NOT_LOGGED_IN);
			}
		} catch (Exception e) {
			operationResult.setResultCode(EnmResultCode.EXCEPTION.getValue());
			operationResult.setResultDesc(AppConstants.LIST_OPERATION_FAIL);
			FileLogger.log(Level.ERROR, "ApiFavoriteController-listFavorite()-Error: " + CommonUtil.getExceptionMessage(e));
		}
		return new ResponseEntity<ValueOperationResult<Map<Integer, Map<Long, Favorite>>>>(operationResult, HttpStatus.OK);
    }
	
	@RequestMapping(value = "/api/favorite/add", method = RequestMethod.POST)
    public ResponseEntity<ValueOperationResult<Map<Integer, Map<Long,Favorite>>>> addFavorite(@RequestBody Favorite favorite, HttpSession session) {
		ValueOperationResult<Map<Integer, Map<Long,Favorite>>> operationResult = new ValueOperationResult<Map<Integer, Map<Long,Favorite>>>();
		
		try {
			User user = super.getSessionUser(session);
			if (user != null) {
				EnmEntityType entityType = EnmEntityType.getEntityType(favorite.getEntityType());
				if (favorite.getEntityType() != null && entityType != null 
						&& favorite.getEntityId() != null && entityType.equals(EnmEntityType.PLACE)) {
					
					Favorite userFavorite = new Favorite();
					userFavorite.setUserId(user.getId());
					
					List<Favorite> userFavoriteList = this.favoriteService.listFavorite(favorite, null, false, false, false);
					
					if (userFavoriteList.size() < 20) {
						boolean itemAlreadyExist = false;
						for (Favorite tmpFavorite : userFavoriteList) {
							if (tmpFavorite.getEntityType().equals(favorite.getEntityType()) 
									&& tmpFavorite.getEntityId().equals(favorite.getEntityId())) {
								itemAlreadyExist = true;
							}
						}
						
						if (!itemAlreadyExist) {
							Long hostUserId = null;
							
							if (favorite.getEntityType().equals(EnmEntityType.PLACE.getId())) {
								ValueOperationResult<Place> getOperationResult = this.placeService.getPlace(favorite.getEntityId());
								hostUserId = getOperationResult.getResultValue().getHostUserId();
							}
							
							if (hostUserId != null) {
								if (!hostUserId.equals(user.getId())) {
									Favorite newFavorite = new Favorite();
									
									newFavorite.setUserId(user.getId());
									newFavorite.setHostUserId(hostUserId);
									newFavorite.setEntityType(favorite.getEntityType());
									newFavorite.setEntityId(favorite.getEntityId());
									
									newFavorite.setCreatedBy(user.getFullName());
									newFavorite.setCreatedDate(new Date());
									OperationResult createResult = this.favoriteService.insertFavorite(newFavorite);
									if (OperationResult.isResultSucces(createResult)) {
										
										BaseModel entity = this.favoriteService.getEntityDetails(newFavorite.getEntityType(), newFavorite.getEntityId());
										
										newFavorite.setEntity(entity);
										
										user.addFavorite(newFavorite);
										operationResult.setResultValue(user.getFavoriteMap());
										operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
									} else {
										operationResult.setResultCode(EnmResultCode.ERROR.getValue());
										operationResult.setResultDesc(AppConstants.OPERATION_FAIL);
									}
								} else {
									operationResult.setResultCode(EnmResultCode.WARNING.getValue());
									operationResult.setResultDesc("This is your own listing. You can not add to favorite!");
								}
							} else {
								operationResult.setResultCode(EnmResultCode.WARNING.getValue());
								operationResult.setResultDesc(AppConstants.ITEM_NOT_FOUND);
							}
						} else {
							operationResult.setResultCode(EnmResultCode.WARNING.getValue());
							operationResult.setResultDesc(AppConstants.WISH_LIST_DOUBLE);
						}
					} else {
						operationResult.setResultCode(EnmResultCode.WARNING.getValue());
						operationResult.setResultDesc(AppConstants.WISH_LIST_MAX);
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
			operationResult.setResultCode(EnmResultCode.EXCEPTION.getValue());
			operationResult.setResultDesc(AppConstants.CREATE_OPERATION_FAIL);
			FileLogger.log(Level.ERROR, "ApiFavoriteController-addFavorite()-Error: " + CommonUtil.getExceptionMessage(e));
		}
		return new ResponseEntity<ValueOperationResult<Map<Integer, Map<Long,Favorite>>>>(operationResult, HttpStatus.OK);
    }
	
	@RequestMapping(value = "/api/favorite/remove", method = RequestMethod.POST)
    public ResponseEntity<ValueOperationResult<Map<Integer, Map<Long,Favorite>>>> removeFavorite(@RequestBody Favorite favorite, HttpSession session) {
		ValueOperationResult<Map<Integer, Map<Long,Favorite>>> operationResult = new ValueOperationResult<Map<Integer, Map<Long,Favorite>>>();
		
		try {
			User user = super.getSessionUser(session);
			if (user != null) {
				if (favorite.getEntityId() != null && favorite.getEntityId() != null) {
					
					Favorite userFavorite = new Favorite();
					userFavorite.setUserId(user.getId());
					userFavorite.setEntityType(favorite.getEntityType());
					userFavorite.setEntityId(favorite.getEntityId());
					
					List<Favorite> favoriteList = this.favoriteService.listFavorite(favorite, null, false, false, false);
					
					if (favoriteList.size() > 0) {
						Long userId = favoriteList.get(0).getUserId();
												
						if (userId.equals(favoriteList.get(0).getUserId())) {
							ValueOperationResult<Integer> deleteResult = this.favoriteService.deleteFavorite(favoriteList.get(0).getId());
							if (OperationResult.isResultSucces(deleteResult)) {
								user.removeFavorite(favorite);
								operationResult.setResultValue(user.getFavoriteMap());
								operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
							} else {
								operationResult.setResultCode(EnmResultCode.ERROR.getValue());
								operationResult.setResultDesc(AppConstants.OPERATION_FAIL);
							}
						} else {
							operationResult.setResultCode(EnmResultCode.WARNING.getValue());
							operationResult.setResultDesc(AppConstants.UNAUTHORIZED_OPERATION);
						}
					} else {
						operationResult.setResultCode(EnmResultCode.WARNING.getValue());
						operationResult.setResultDesc(AppConstants.WISH_LIST_NOT_FOUND);
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
			FileLogger.log(Level.ERROR, "ApiFavoriteController-removeFavorite()-Error: " + CommonUtil.getExceptionMessage(e));
			operationResult.setResultCode(EnmResultCode.EXCEPTION.getValue());
			operationResult.setResultDesc(AppConstants.CREATE_OPERATION_FAIL);
		}
		return new ResponseEntity<ValueOperationResult<Map<Integer, Map<Long,Favorite>>>>(operationResult, HttpStatus.OK);
    }
	
}
