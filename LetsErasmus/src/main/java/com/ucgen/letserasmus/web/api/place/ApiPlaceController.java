package com.ucgen.letserasmus.web.api.place;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.ucgen.common.operationresult.EnmResultCode;
import com.ucgen.common.operationresult.ListOperationResult;
import com.ucgen.common.operationresult.OperationResult;
import com.ucgen.common.operationresult.ValueOperationResult;
import com.ucgen.common.util.CommonUtil;
import com.ucgen.common.util.FileUtil;
import com.ucgen.common.util.ImageUtil;
import com.ucgen.letserasmus.library.common.enumeration.EnmEntityType;
import com.ucgen.letserasmus.library.common.enumeration.EnmErrorCode;
import com.ucgen.letserasmus.library.common.enumeration.EnmSize;
import com.ucgen.letserasmus.library.file.enumeration.EnmFileType;
import com.ucgen.letserasmus.library.file.model.FileModel;
import com.ucgen.letserasmus.library.file.model.Photo;
import com.ucgen.letserasmus.library.file.service.IFileService;
import com.ucgen.letserasmus.library.place.enumeration.EnmPlaceStatus;
import com.ucgen.letserasmus.library.place.model.Place;
import com.ucgen.letserasmus.library.place.service.IPlaceService;
import com.ucgen.letserasmus.library.review.model.Review;
import com.ucgen.letserasmus.library.review.service.IReviewService;
import com.ucgen.letserasmus.library.user.model.User;
import com.ucgen.letserasmus.web.api.BaseApiController;
import com.ucgen.letserasmus.web.view.application.AppConstants;
import com.ucgen.letserasmus.web.view.application.EnmOperation;
import com.ucgen.letserasmus.web.view.application.EnmSession;
import com.ucgen.letserasmus.web.view.application.WebApplication;

@RestController
public class ApiPlaceController extends BaseApiController {

	private IPlaceService placeService;
	private IFileService fileService;
	private IReviewService reviewService;
	private WebApplication webApplication;
	
	@Autowired
	public void setReviewService(IReviewService reviewService) {
		this.reviewService = reviewService;
	}

	@Autowired
	public void setWebApplication(WebApplication webApplication) {
		this.webApplication = webApplication;
	}
	
	@Autowired
	public void setPlaceService(IPlaceService placeService) {
		this.placeService = placeService;
	}
	
	@Autowired
	public void setFileService(IFileService fileService) {
		this.fileService = fileService;
	}
	
	@RequestMapping(value = "/api/place/create", method = RequestMethod.POST)
    public ResponseEntity<OperationResult> createPlace(@RequestBody Place place, HttpSession session) {
		HttpStatus httpStatus = null;
		OperationResult operationResult = new OperationResult();
		
		try {
			User user = super.getSessionUser(session);
			if (user != null) {
				Object activeOperation = super.getSession().getAttribute(EnmSession.ACTIVE_OPERATION.getId());
				if (activeOperation != null && activeOperation.equals(EnmOperation.CREATE_PLACE)) {
					MultipartFile[] photoList = (MultipartFile[]) session.getAttribute(EnmSession.PLACE_PHOTO_LIST.getId());
					session.removeAttribute(EnmSession.PLACE_PHOTO_LIST.getId());
					if (photoList != null && photoList.length > 0) {
						Date createdDate = new Date();
						String createdBy = user.getFullName();
						
						place.setHostUserId(super.getSessionUser(session).getId());
						place.setCreatedDate(createdDate);
						place.setCreatedBy(createdBy);
						place.setStatus(EnmPlaceStatus.ACTIVE.getValue());
						
						place.getLocation().setCreatedDate(createdDate);
						place.getLocation().setCreatedBy(createdBy);
						
						if (photoList != null && photoList.length > 0) {
							for (MultipartFile multipartFile : photoList) {
								String fileSuffix = multipartFile.getContentType().split("/")[1];
								String fileName = multipartFile.getOriginalFilename().split("\\.")[0];
								Photo photo = new Photo();
								photo.setFileName(fileName);
								photo.setFileSize(multipartFile.getSize());
								photo.setEntityType(EnmEntityType.PLACE.getId());
								photo.setCreatedBy(createdBy);
								photo.setFileType(EnmFileType.getFileTypeWithSuffix(fileSuffix).getValue());
								photo.setCreatedDate(createdDate);
								
								photo.setFile(multipartFile);
								
								place.addPhoto(photo);
							}
						}
						
						OperationResult createResult = this.placeService.insertPlace(place);
						
						try {
							String rootPhotoFolder = this.webApplication.getRootPlacePhotoPath();
							String placePhotoFolderPath = rootPhotoFolder + place.getId(); 
							File placePhotoFolder = new File(placePhotoFolderPath);
							
							if (!placePhotoFolder.exists()) {
								placePhotoFolder.mkdirs();
							}
							
							String tmpPlaceId = (String) session.getAttribute(EnmSession.TMP_PHOTO_PLACE_ID.getId());
							String tmpPhotoDirPath = FileUtil.concatPath(rootPhotoFolder, tmpPlaceId);
							
							for (int i = 0; i < place.getPhotoList().size(); i++) {
								FileModel photo = place.getPhotoList().get(i);
								MultipartFile multipartFile = photo.getFile();
								String fileName = multipartFile.getOriginalFilename();
								
								String tmpPhotoPath = FileUtil.concatPath(rootPhotoFolder, tmpPlaceId, "tmp", fileName);
								
								String smallPhotoPath = this.webApplication.getPlacePhotoPath(place.getId(), photo.getId(), EnmSize.SMALL.getValue());
								String mediumPhotoPath = this.webApplication.getPlacePhotoPath(place.getId(), photo.getId(), EnmSize.MEDIUM.getValue());
																
								File tmpFile = new File(tmpPhotoPath);
								
								ImageUtil.resizeImage(tmpFile, mediumPhotoPath, this.webApplication.getMediumPlacePhotoSize());
								ImageUtil.resizeImage(tmpFile, smallPhotoPath, this.webApplication.getSmallPlacePhotoSize());
								tmpFile.delete();
							}
							(new File(tmpPhotoDirPath)).delete();
						} catch (Exception e) {
							System.out.println(CommonUtil.getExceptionMessage(e));
						}
						
						operationResult = createResult;
						httpStatus = HttpStatus.OK;	
					} else {
						operationResult.setResultCode(EnmResultCode.ERROR.getValue());
						operationResult.setResultDesc("At least one photo should be added!");
					}
				} else {
					operationResult.setResultCode(EnmResultCode.ERROR.getValue());
					operationResult.setResultDesc("You are not authorized for this operation!");
				}
			} else {
				operationResult.setErrorCode(EnmErrorCode.USER_NOT_LOGGED_IN.getId());
				operationResult.setResultCode(EnmResultCode.ERROR.getValue());
				operationResult.setResultDesc(AppConstants.USER_NOT_LOGGED_IN);
			}
			httpStatus = HttpStatus.OK;			
		} catch (Exception e) {
			operationResult.setResultCode(EnmResultCode.EXCEPTION.getValue());
			operationResult.setResultDesc("Create operation could not be completed. Please try again later!");
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<OperationResult>(operationResult, httpStatus);
    }
	
	@RequestMapping(value = "/api/place/update", method = RequestMethod.POST)
    public ResponseEntity<OperationResult> updatePlace(@RequestBody Place place, HttpSession session) {
		HttpStatus httpStatus = null;
		OperationResult operationResult = new OperationResult();
		
		try {
			User user = super.getSessionUser(session);
			if (user != null) {
				Object activeOperation = super.getSession().getAttribute(EnmSession.ACTIVE_OPERATION.getId());
				if (activeOperation != null && activeOperation.equals(EnmOperation.EDIT_PLACE)) {
					MultipartFile[] photoList = (MultipartFile[]) session.getAttribute(EnmSession.PLACE_PHOTO_LIST.getId());
					session.removeAttribute(EnmSession.PLACE_PHOTO_LIST.getId());
					if (photoList != null && photoList.length > 0) {
						Date createdDate = new Date();
						String createdBy = user.getFullName();
						
						Place activePlace = (Place) session.getAttribute(EnmSession.ACTIVE_PLACE.getId());
						
						place.setId(activePlace.getId());
						place.setHostUserId(super.getSessionUser(session).getId());
						place.setCreatedDate(createdDate);
						place.setCreatedBy(createdBy);
						place.setStatus(EnmPlaceStatus.ACTIVE.getValue());
						
						place.getLocation().setId(activePlace.getLocation().getId());
						place.getLocation().setCreatedDate(createdDate);
						place.getLocation().setCreatedBy(createdBy);
						
						Long coverPhotoId = null;
						Photo coverPhoto = null;
						
						List<FileModel> newPhotoList = new ArrayList<>();
						List<Long> deletePhotoList = new ArrayList<Long>();
						
						if (photoList != null && photoList.length > 0) {
							for (int i = 0; i < photoList.length; i++) {
								MultipartFile multipartFile = photoList[i];
								String fileName = multipartFile.getOriginalFilename();
								if (!fileName.toUpperCase().startsWith("DUMMY_")) {
									String fileSuffix = multipartFile.getContentType().split("/")[1];
									String photoFileName = multipartFile.getOriginalFilename().split("\\.")[0];
									Photo photo = new Photo();
									photo.setEntityId(activePlace.getId());
									photo.setFileName(photoFileName);
									photo.setFileSize(multipartFile.getSize());
									photo.setEntityType(EnmEntityType.PLACE.getId());
									photo.setCreatedBy(createdBy);
									photo.setFileType(EnmFileType.getFileTypeWithSuffix(fileSuffix).getValue());
									photo.setCreatedDate(createdDate);
									
									photo.setFile(multipartFile);
									
									if (i== 0) {
										coverPhoto = photo;
									} else {
										newPhotoList.add(photo);
									}
								} else if (i == 0) {
									Long tmpFileId = Long.valueOf(fileName.substring(fileName.indexOf("_") + 1));
									if (!tmpFileId.equals(activePlace.getCoverPhotoId())) {
										coverPhotoId = tmpFileId;
									}
								}
							}
							for (FileModel oldPhoto : activePlace.getPhotoList()) {
								for (int i = 0; i< photoList.length; i++) {
									MultipartFile multipartFile = photoList[i];
									String fileName = multipartFile.getOriginalFilename();
									if (fileName.toUpperCase().startsWith("DUMMY_")) {
										Long tmpFileId = Long.valueOf(fileName.substring(fileName.indexOf("_") + 1));
										if (tmpFileId.equals(oldPhoto.getId())) {
											break;
										}
									}
									if (i == (photoList.length -1)) {
										deletePhotoList.add(oldPhoto.getId());
									}
								}
							}
						}
						
						if (coverPhotoId != null) {
							place.setCoverPhotoId(coverPhotoId);
						} else {
							place.setCoverPhotoId(null);
						}
						
						OperationResult createResult = this.placeService.updatePlace(place, coverPhoto, newPhotoList, deletePhotoList);
						
						if (coverPhoto != null) {
							newPhotoList.add(0, coverPhoto);
						}
						
						if (newPhotoList != null && newPhotoList.size() > 0) {
							try {
								String rootPhotoFolder = this.webApplication.getRootPlacePhotoPath();
								String placePhotoFolderPath = rootPhotoFolder + place.getId(); 
								File placePhotoFolder = new File(placePhotoFolderPath);
								
								if (!placePhotoFolder.exists()) {
									placePhotoFolder.mkdirs();
								}

								for (int i = 0; i < newPhotoList.size(); i++) {
									FileModel photo = newPhotoList.get(i);
									MultipartFile multipartFile = photo.getFile();
									String fileName = multipartFile.getOriginalFilename();
									if (!fileName.toUpperCase().startsWith("DUMMY_")) {
										String tmpPhotoPath = FileUtil.concatPath(rootPhotoFolder, place.getId().toString(), "tmp", fileName);
										
										File tmpFile = new File(tmpPhotoPath);
										
										String smallPhotoPath = this.webApplication.getPlacePhotoPath(place.getId(), photo.getId(), EnmSize.SMALL.getValue());
										String mediumPhotoPath = this.webApplication.getPlacePhotoPath(place.getId(), photo.getId(), EnmSize.MEDIUM.getValue());
										
										ImageUtil.resizeImage(tmpFile, mediumPhotoPath, this.webApplication.getMediumPlacePhotoSize());
										ImageUtil.resizeImage(tmpFile, smallPhotoPath, this.webApplication.getSmallPlacePhotoSize());
										
										tmpFile.delete();
									}
								}
							} catch (Exception e) {
								System.out.println(CommonUtil.getExceptionMessage(e));
							}
						}
						
						operationResult = createResult;
						httpStatus = HttpStatus.OK;	
					} else {
						operationResult.setResultCode(EnmResultCode.ERROR.getValue());
						operationResult.setResultDesc("At least one photo should be added!");
					}
				} else {
					operationResult.setResultCode(EnmResultCode.ERROR.getValue());
					operationResult.setResultDesc("You are not authorized for this operation!");
				}
			} else {
				operationResult.setErrorCode(EnmErrorCode.USER_NOT_LOGGED_IN.getId());
				operationResult.setResultCode(EnmResultCode.ERROR.getValue());
				operationResult.setResultDesc(AppConstants.USER_NOT_LOGGED_IN);
			}
			httpStatus = HttpStatus.OK;			
		} catch (Exception e) {
			operationResult.setResultCode(EnmResultCode.EXCEPTION.getValue());
			operationResult.setResultDesc("Create operation could not be completed. Please try again later!");
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<OperationResult>(operationResult, httpStatus);
    }
	
	@RequestMapping(value = "/api/place/get", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResponseEntity<ValueOperationResult<Place>> getPlace(@RequestParam("placeId") Long placeId, HttpSession session) {
		User user = super.getSessionUser(session);
		ValueOperationResult<Place> getResult = this.placeService.getPlace(placeId);
		Place place = getResult.getResultValue();
		if (place != null) {
			
			Review review = new Review();
			review.setEntityType(EnmEntityType.PLACE.getId());
			review.setEntityId(place.getId());
			
			List<Review> reviewList = this.reviewService.listReview(review, null, false, true, false);
			
			if (reviewList != null && reviewList.size() > 0) {
				for (Review tmpReview : reviewList) {
					User reviewUser = new User();
					reviewUser.setFirstName(tmpReview.getUser().getFirstName());
					reviewUser.setId(tmpReview.getUserId());
					
					String profileImageUrl = this.webApplication.getUserPhotoUrl(reviewUser.getId(), tmpReview.getUser().getProfilePhotoId(), EnmSize.SMALL.getValue());
					
					reviewUser.setProfileImageUrl(profileImageUrl);
					
					reviewUser.setProfileImageUrl(profileImageUrl);
					
					tmpReview.setUser(reviewUser);
				}
				place.setReviewList(reviewList);
			}
			
			session.removeAttribute(EnmSession.ACTIVE_PLACE.getId());
			session.setAttribute(EnmSession.ACTIVE_PLACE.getId(), place);
		}
		return new ResponseEntity<ValueOperationResult<Place>>(getResult, HttpStatus.OK);
    }
	
	@RequestMapping(value = "/api/place/list", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResponseEntity<ListOperationResult<Place>> listPlace(@RequestParam Map<String, String> requestParams) {
		HttpStatus httpStatus = null;
		ListOperationResult<Place> listResult = this.placeService.listPlace(null, true, true, true);
		if (OperationResult.isResultSucces(listResult)) {
			httpStatus = HttpStatus.OK;
		} else {
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<ListOperationResult<Place>>(listResult, httpStatus);
    }
	
	@RequestMapping(value = "/api/place/listuserplace", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResponseEntity<ValueOperationResult<Map<String, List<Place>>>> listUserPlace(HttpSession session) {
		ValueOperationResult<Map<String, List<Place>>> operationResult = new ValueOperationResult<Map<String, List<Place>>>();
		try {
			User user = super.getSessionUser(session);
			if (user != null) {
				Place place = new Place();
				place.setHostUserId(user.getId());
				ListOperationResult<Place> listResult = this.placeService.listPlace(place, true, true, false);
				
				List<Place> placeList = listResult.getObjectList();
				Map<String, List<Place>> placeMap = new HashMap<>();
				List<Place> activeList = new ArrayList<Place>();
				List<Place> deactiveList = new ArrayList<Place>();
				if (placeList != null && placeList.size() > 0) {
					for (Place tmpPlace : placeList) {
						if (tmpPlace.getStatus().equals(EnmPlaceStatus.ACTIVE.getValue())) {
							activeList.add(tmpPlace);
						} else if (tmpPlace.getStatus().equals(EnmPlaceStatus.DEACTIVE.getValue())) {
							deactiveList.add(tmpPlace);
						}
					}
				}
				placeMap.put("active", activeList);
				placeMap.put("deactive", deactiveList);
				
				operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
				operationResult.setResultValue(placeMap);
			} else {
				operationResult.setErrorCode(EnmErrorCode.USER_NOT_LOGGED_IN.getId());
				operationResult.setResultCode(EnmResultCode.ERROR.getValue());
				operationResult.setResultDesc(AppConstants.USER_NOT_LOGGED_IN);
			}
		} catch (Exception e) {
			operationResult.setResultCode(EnmResultCode.EXCEPTION.getValue());
			operationResult.setResultDesc("Place list could not be fetched from database.");
		}
		return new ResponseEntity<ValueOperationResult<Map<String, List<Place>>>>(operationResult, HttpStatus.OK);
    }
		
	@RequestMapping(value = "/api/place/savephoto", method = RequestMethod.POST)
    public ResponseEntity<OperationResult> savePhoto(@RequestParam("photoList") MultipartFile[] fileArr, HttpSession session) {
		OperationResult operationResult = new OperationResult();
		try {
			User sessionUser = super.getSessionUser(session);
			if (sessionUser != null) {
				Object activeOperation = super.getSession().getAttribute(EnmSession.ACTIVE_OPERATION.getId());
				if (activeOperation != null && (activeOperation.equals(EnmOperation.CREATE_PLACE) || activeOperation.equals(EnmOperation.EDIT_PLACE))) {
					String placeId = null;
					if (activeOperation.equals(EnmOperation.CREATE_PLACE)) {
						placeId = "tmp_" + Double.valueOf((Math.random() * 100000000)).longValue();
						session.removeAttribute(EnmSession.TMP_PHOTO_PLACE_ID.getId());
						session.setAttribute(EnmSession.TMP_PHOTO_PLACE_ID.getId(), placeId);
					} else {
						Place place = (Place) session.getAttribute(EnmSession.ACTIVE_PLACE.getId());
						placeId = place.getId().toString();
					}
					
					String rootPhotoFolder = this.webApplication.getRootPlacePhotoPath();
					
					String placeTmpPhotoFolderPath = rootPhotoFolder + placeId + File.separatorChar + "tmp";
					File placeTmpPhotoFolder = new File(placeTmpPhotoFolderPath);
					if (placeTmpPhotoFolder.exists()) {
						FileUtils.cleanDirectory(placeTmpPhotoFolder);
					} else {
						placeTmpPhotoFolder.mkdirs();	
					}
					for (int i = 0; i < fileArr.length; i++) {
						MultipartFile multipartFile = fileArr[i];
						String fileName = multipartFile.getOriginalFilename();
						if (!fileName.toUpperCase().startsWith("DUMMY_")) {
							String tmpPhotoPath = placeTmpPhotoFolderPath + File.separatorChar + fileName;
							File tmpFile = new File(tmpPhotoPath);
							multipartFile.transferTo(tmpFile);
						}
					}
					
					session.removeAttribute(EnmSession.PLACE_PHOTO_LIST.getId());
					session.setAttribute(EnmSession.PLACE_PHOTO_LIST.getId(), fileArr);
					
					operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
				} else {
					operationResult.setResultCode(EnmResultCode.ERROR.getValue());
					operationResult.setResultDesc("You are not authorized for this operation!");
				}
			} else {
				operationResult.setErrorCode(EnmErrorCode.USER_NOT_LOGGED_IN.getId());
				operationResult.setResultCode(EnmResultCode.ERROR.getValue());
				operationResult.setResultDesc(AppConstants.USER_NOT_LOGGED_IN);
			}
		} catch (Exception e) {
			operationResult.setResultCode(EnmResultCode.ERROR.getValue());
			operationResult.setResultDesc(CommonUtil.getExceptionMessage(e));
		}
		
		return new ResponseEntity<OperationResult>(operationResult, HttpStatus.OK);
    }
	
	@RequestMapping(value = "/api/place/listphoto", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResponseEntity<ListOperationResult<FileModel>> listPhoto(@RequestParam Map<String, String> requestParams) {
		HttpStatus httpStatus = null;
		String strPlaceId = requestParams.get("placeId");
		FileModel file = new FileModel();
		file.setEntityType(EnmEntityType.PLACE.getId());
		file.setEntityId(Long.valueOf(strPlaceId));
		
		ListOperationResult<FileModel> listResult = this.fileService.listFile(file);
		
		if (OperationResult.isResultSucces(listResult)) {
			httpStatus = HttpStatus.OK;
		} else {
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<ListOperationResult<FileModel>>(listResult, httpStatus);
    }
	
	@RequestMapping(value = "/api/place/updateplacestatus", method = RequestMethod.POST)
    public ResponseEntity<OperationResult> updatePlaceStatus(@RequestBody Place place, HttpSession session) throws JsonParseException, JsonMappingException, IOException, ParseException {
		HttpStatus httpStatus = null;
		OperationResult operationResult = new OperationResult();
		
		try {
			Date modifiedDate = new Date();
			User user = super.getSessionUser(session);
			if (user != null) {
				Object activeOperation = super.getSession().getAttribute(EnmSession.ACTIVE_OPERATION.getId());
				if (activeOperation != null && activeOperation.equals(EnmOperation.LIST_USER_PLACE)) {
					Long placeId = place.getId();
					Integer newStatus = place.getStatus();
					if (placeId != null && newStatus != null) {
						ValueOperationResult<Place> getPlaceResult = this.placeService.getPlace(placeId);
						if (OperationResult.isResultSucces(getPlaceResult)) {
							Place dbPlace = getPlaceResult.getResultValue();
							if (dbPlace != null) {
								if (dbPlace.getHostUserId().equals(user.getId())) {
									if (dbPlace.getStatus().equals(EnmPlaceStatus.ACTIVE.getValue()) 
											|| dbPlace.getStatus().equals(EnmPlaceStatus.DEACTIVE.getValue())) {
										Place updatedPlace = new Place();
										updatedPlace.setId(placeId);
										updatedPlace.setStatus(newStatus);
										updatedPlace.setModifiedBy(user.getFullName());
										updatedPlace.setModifiedDate(modifiedDate);
										
										operationResult = this.placeService.updatePlace(updatedPlace, null, null, null);
									} else {
										operationResult.setResultCode(EnmResultCode.ERROR.getValue());
										operationResult.setResultDesc("Existing status of place is not suitable for this operation.");
									}
								} else {
									operationResult.setResultCode(EnmResultCode.ERROR.getValue());
									operationResult.setResultDesc("You are not authorized to edit this listing");
								}
							} else {
								operationResult.setResultCode(EnmResultCode.WARNING.getValue());
								operationResult.setResultDesc("There is no place definition with this id!");
							}
						} else {
							operationResult = getPlaceResult;
						}
					} else {
						operationResult.setResultCode(EnmResultCode.ERROR.getValue());
						operationResult.setResultDesc("placeId and status parameters are mandatory");
					}
				} else {
					operationResult.setResultCode(EnmResultCode.ERROR.getValue());
					operationResult.setResultDesc("You are not authorized for this operation!");
				}
			} else {
				operationResult.setErrorCode(EnmErrorCode.USER_NOT_LOGGED_IN.getId());
				operationResult.setResultCode(EnmResultCode.ERROR.getValue());
				operationResult.setResultDesc(AppConstants.USER_NOT_LOGGED_IN);
			}
			httpStatus = HttpStatus.OK;			
		} catch (Exception e) {
			operationResult.setResultCode(EnmResultCode.EXCEPTION.getValue());
			operationResult.setResultDesc("Create operation could not be completed. Please try again later!");
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<OperationResult>(operationResult, httpStatus);
    }
	
	
}
