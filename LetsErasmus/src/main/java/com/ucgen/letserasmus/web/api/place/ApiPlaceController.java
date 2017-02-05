package com.ucgen.letserasmus.web.api.place;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

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
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ucgen.common.operationresult.EnmResultCode;
import com.ucgen.common.operationresult.ListOperationResult;
import com.ucgen.common.operationresult.OperationResult;
import com.ucgen.common.operationresult.ValueOperationResult;
import com.ucgen.common.util.CommonUtil;
import com.ucgen.common.util.DateUtil;
import com.ucgen.common.util.ImageUtil;
import com.ucgen.letserasmus.library.common.enumeration.EnmEntityType;
import com.ucgen.letserasmus.library.file.enumeration.EnmFileType;
import com.ucgen.letserasmus.library.file.model.Photo;
import com.ucgen.letserasmus.library.file.service.IFileService;
import com.ucgen.letserasmus.library.location.model.Location;
import com.ucgen.letserasmus.library.place.enumeration.EnmPlaceStatus;
import com.ucgen.letserasmus.library.place.model.Place;
import com.ucgen.letserasmus.library.place.service.IPlaceService;
import com.ucgen.letserasmus.library.user.model.User;
import com.ucgen.letserasmus.web.api.BaseApiController;
import com.ucgen.letserasmus.web.view.application.EnmOperation;
import com.ucgen.letserasmus.web.view.application.EnmSession;

@RestController
public class ApiPlaceController extends BaseApiController {

	private IPlaceService placeService;
	
	private IFileService fileService;
	
	@Autowired
	public void setPlaceService(IPlaceService placeService) {
		this.placeService = placeService;
	}
	
	@Autowired
	public void setFileService(IFileService fileService) {
		this.fileService = fileService;
	}
	
	@RequestMapping(value = "/api/place/create", method = RequestMethod.POST)
    public ResponseEntity<OperationResult> createPlace(@RequestParam("photoList") MultipartFile[] photoList, @RequestParam("place") String place, HttpSession session) throws JsonParseException, JsonMappingException, IOException, ParseException {
		HttpStatus httpStatus = null;
		OperationResult operationResult = new OperationResult();
		
		try {
			Date createdDate = new Date();
			String strPlaceJSON = place;
			
			ObjectMapper mapper = new ObjectMapper();
			
			Map placeMap = mapper.readValue(strPlaceJSON, Map.class);
			
			Place newPlace = new Place();
			
			newPlace.setHostUserId(super.getSessionUser(session).getId());
			newPlace.setPlaceTypeId(this.getInteger(placeMap.get("placeType")));
			newPlace.setHomeTypeId(this.getInteger(placeMap.get("homeType")));
			newPlace.setAmenties(placeMap.get("amenties").toString());
			newPlace.setSafetyAmenties(placeMap.get("safetyAmenties").toString());
			newPlace.setRules(placeMap.get("rules").toString());
			newPlace.setBathRoomNumber(this.getInteger(placeMap.get("bathroomNumber")));
			newPlace.setBathRoomType(this.getInteger(placeMap.get("bathroomType")));
			newPlace.setBedNumber(this.getInteger(placeMap.get("bedNumber")));
			newPlace.setBedTypeId(this.getInteger(placeMap.get("bedTypeId")));
			newPlace.setGuestNumber(this.getInteger(placeMap.get("guestNumber")));
			newPlace.setGuestGender(this.getInteger(placeMap.get("guestGender")));
			
			if (placeMap.get("placeMateNumber") != null && !placeMap.get("placeMateNumber").equals("0")) {
				newPlace.setPlaceMateNumber(this.getInteger(placeMap.get("placeMateNumber")));
				newPlace.setPlaceMateGender(this.getInteger(placeMap.get("placeMateNumber")));
			}
			
			newPlace.setPrice(new BigDecimal(placeMap.get("price").toString()));
			newPlace.setDepositPrice(new BigDecimal(placeMap.get("depositPrice").toString()));
			newPlace.setCurrencyId(this.getInteger(placeMap.get("currencyId")));
			
			String billsIncluded = this.getString(placeMap.get("billsIncluded"));
			if (billsIncluded.equalsIgnoreCase("Y")) {
				newPlace.setBillsInclude("Y");
			} else {
				newPlace.setBillsInclude("N");
			}
			
			if (placeMap.get("startDate") != null && !placeMap.get("startDate").toString().isEmpty()) {
				newPlace.setStartDate(DateUtil.valueOf(placeMap.get("startDate").toString(), DateUtil.SHORT_DATE_FORMAT));
			}
			
			if (placeMap.get("endDate") != null && !placeMap.get("endDate").toString().isEmpty()) {
				newPlace.setEndDate(DateUtil.valueOf(placeMap.get("endDate").toString(), DateUtil.SHORT_DATE_FORMAT));
			}
			
			if (placeMap.get("minimumStay") != null) {
				newPlace.setMinimumStay(this.getInteger(placeMap.get("minimumStay")));
			}
			
			if (placeMap.get("maximumStay") != null) {
				newPlace.setMaximumStay(this.getInteger(placeMap.get("maximumStay")));
			}
			
			if (placeMap.get("title") != null) {
				newPlace.setTitle(placeMap.get("title").toString());
			}
			
			if (placeMap.get("description") != null) {
				newPlace.setDescription(placeMap.get("description").toString());
			}
			
			newPlace.setStatus(EnmPlaceStatus.ACTIVE.getValue());
			
			Location newLocation = new Location();
			
			Map locationMap = (Map) placeMap.get("location");
			
			newLocation.setCountry(this.getString(locationMap.get("country")));
			newLocation.setState(this.getString(locationMap.get("city")));
			newLocation.setStreet(this.getString(locationMap.get("street")));
			newLocation.setLocality(this.getString(locationMap.get("locality")));
			newLocation.setPostalCode(this.getString(locationMap.get("postalCode")));
			newLocation.setUserAddress(this.getString(locationMap.get("userAddress")));
			newLocation.setLatitude(this.getBigDecimal(locationMap.get("latitude")));
			newLocation.setLongitude(this.getBigDecimal(locationMap.get("longitude")));
			
			User appUser = this.getSessionUser(session);
			
			String createdBy = "LetsErasmus";
			if (appUser != null) {
				createdBy = appUser.getFullName();
			}
			newPlace.setCreatedBy(createdBy);
			newPlace.setCreatedDate(createdDate);
			newLocation.setCreatedBy(createdBy);
			newLocation.setCreatedDate(createdDate);
			
			newPlace.setLocation(newLocation);
			
			if (photoList != null && photoList.length > 0) {
				for (MultipartFile multipartFile : photoList) {
					String fileSuffix = multipartFile.getContentType().split("/")[1];
					String fileName = multipartFile.getOriginalFilename().split("\\.")[0];
					Photo photo = new Photo();
					photo.setFileName(fileName);
					photo.setFileSize(multipartFile.getSize());
					photo.setEntityType(EnmEntityType.PLACE.getValue());
					photo.setCreatedBy(createdBy);
					photo.setFileType(EnmFileType.getFileTypeWithSuffix(fileSuffix).getValue());
					photo.setCreatedDate(createdDate);
					
					newPlace.addPhoto(photo);
				}
			}
			
			OperationResult createResult = this.placeService.insertPlace(newPlace);
			
			try {
				String rootPhotoFolder = "D:\\Personal\\Development\\startup\\workspace\\projects\\LetsErasmus\\src\\main\\webapp\\place\\images\\";
				String placePhotoFolder = rootPhotoFolder + newPlace.getId(); 
				(new File(placePhotoFolder)).mkdirs();
				for (int i = 0; i < photoList.length; i++) {
					MultipartFile multiPartFile = photoList[i];
					com.ucgen.letserasmus.library.file.model.File photo = newPlace.getPhotoList().get(i);
					String tmpPhotoPath = placePhotoFolder + File.separatorChar + photo.getId() + "." + EnmFileType.getFileType(photo.getFileType());
					String smallPhotoPath = placePhotoFolder + File.separatorChar + photo.getId() + "_small." + EnmFileType.getFileType(photo.getFileType()).getFileSuffix();
					String largePhotoPath = placePhotoFolder + File.separatorChar + photo.getId() + "_large." + EnmFileType.getFileType(photo.getFileType()).getFileSuffix();
					File tmpFile = new File(tmpPhotoPath);
					multiPartFile.transferTo(tmpFile);
					ImageUtil.resizeImage(tmpFile, largePhotoPath, 800, 800);
					ImageUtil.resizeImage(tmpFile, smallPhotoPath, 500, 300);
					tmpFile.delete();
				}
			} catch (Exception e) {
				System.out.println(CommonUtil.getExceptionMessage(e));
			}
			
			operationResult = createResult;
			httpStatus = HttpStatus.OK;			
		} catch (Exception e) {
			operationResult.setResultCode(EnmResultCode.EXCEPTION.getValue());
			operationResult.setResultDesc("Create operation could not be completed. Please try again later!");
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<OperationResult>(operationResult, httpStatus);
    }
	
	@RequestMapping(value = "/api/place/update", method = RequestMethod.POST)
    public ResponseEntity<OperationResult> updatePlace(@RequestParam("photoList") MultipartFile[] photoList, @RequestParam("place") String place, HttpSession session) throws JsonParseException, JsonMappingException, IOException, ParseException {
		HttpStatus httpStatus = null;
		OperationResult operationResult = new OperationResult();
		
		try {
			User user = super.getSessionUser(session);
			if (user != null) {
				Object activeOperation = super.getSession().getAttribute(EnmSession.ACTIVE_OPERATION.getId());
				if (activeOperation != null && activeOperation.equals(EnmOperation.EDIT_PLACE)) {
					Place sessionPlace = (Place) session.getAttribute(EnmSession.ACTIVE_PLACE.getId());
					
					String modifiedBy = user.getFullName();
					Date modifiedDate = new Date();
					
					String strPlaceJSON = place;
					
					ObjectMapper mapper = new ObjectMapper();
					
					Map placeMap = mapper.readValue(strPlaceJSON, Map.class);
					
					Place newPlace = new Place();
					
					newPlace.setId(sessionPlace.getId());
					newPlace.setPlaceTypeId(this.getInteger(placeMap.get("placeType")));
					newPlace.setHomeTypeId(this.getInteger(placeMap.get("homeType")));
					newPlace.setAmenties(placeMap.get("amenties").toString());
					newPlace.setSafetyAmenties(placeMap.get("safetyAmenties").toString());
					newPlace.setRules(placeMap.get("rules").toString());
					newPlace.setBathRoomNumber(this.getInteger(placeMap.get("bathroomNumber")));
					newPlace.setBathRoomType(this.getInteger(placeMap.get("bathroomType")));
					newPlace.setBedNumber(this.getInteger(placeMap.get("bedNumber")));
					newPlace.setBedTypeId(this.getInteger(placeMap.get("bedTypeId")));
					newPlace.setGuestNumber(this.getInteger(placeMap.get("guestNumber")));
					newPlace.setGuestGender(this.getInteger(placeMap.get("guestGender")));
					
					if (placeMap.get("placeMateNumber") != null && !placeMap.get("placeMateNumber").equals("0")) {
						newPlace.setPlaceMateNumber(this.getInteger(placeMap.get("placeMateNumber")));
						newPlace.setPlaceMateGender(this.getInteger(placeMap.get("placeMateNumber")));
					}
					
					newPlace.setPrice(new BigDecimal(placeMap.get("price").toString()));
					newPlace.setDepositPrice(new BigDecimal(placeMap.get("depositPrice").toString()));
					newPlace.setCurrencyId(this.getInteger(placeMap.get("currencyId")));
					
					String billsIncluded = this.getString(placeMap.get("billsIncluded"));
					if (billsIncluded.equalsIgnoreCase("Y")) {
						newPlace.setBillsInclude("Y");
					} else {
						newPlace.setBillsInclude("N");
					}
					
					if (placeMap.get("startDate") != null && !placeMap.get("startDate").toString().isEmpty()) {
						newPlace.setStartDate(DateUtil.valueOf(placeMap.get("startDate").toString(), DateUtil.SHORT_DATE_FORMAT));
					}
					
					if (placeMap.get("endDate") != null && !placeMap.get("endDate").toString().isEmpty()) {
						newPlace.setEndDate(DateUtil.valueOf(placeMap.get("endDate").toString(), DateUtil.SHORT_DATE_FORMAT));
					}
					
					if (placeMap.get("minimumStay") != null) {
						newPlace.setMinimumStay(this.getInteger(placeMap.get("minimumStay")));
					}
					
					if (placeMap.get("maximumStay") != null) {
						newPlace.setMaximumStay(this.getInteger(placeMap.get("maximumStay")));
					}
					
					if (placeMap.get("title") != null) {
						newPlace.setTitle(placeMap.get("title").toString());
					}
					
					if (placeMap.get("description") != null) {
						newPlace.setDescription(placeMap.get("description").toString());
					}
					
					newPlace.setStatus(EnmPlaceStatus.ACTIVE.getValue());
					
					Location newLocation = new Location();
					
					Map locationMap = (Map) placeMap.get("location");
					
					newLocation.setId(sessionPlace.getLocation().getId());
					newLocation.setCountry(this.getString(locationMap.get("country")));
					newLocation.setState(this.getString(locationMap.get("city")));
					newLocation.setStreet(this.getString(locationMap.get("street")));
					newLocation.setLocality(this.getString(locationMap.get("locality")));
					newLocation.setPostalCode(this.getString(locationMap.get("postalCode")));
					newLocation.setUserAddress(this.getString(locationMap.get("userAddress")));
					newLocation.setLatitude(this.getBigDecimal(locationMap.get("latitude")));
					newLocation.setLongitude(this.getBigDecimal(locationMap.get("longitude")));
					
					newPlace.setModifiedBy(modifiedBy);
					newPlace.setModifiedDate(modifiedDate);
					
					newPlace.setLocation(newLocation);
					
					OperationResult createResult = this.placeService.updatePlace(newPlace);
					
					/*
					if (photoList != null && photoList.length > 0) {
						for (MultipartFile multipartFile : photoList) {
							String fileSuffix = multipartFile.getContentType().split("/")[1];
							String fileName = multipartFile.getOriginalFilename().split("\\.")[0];
							Photo photo = new Photo();
							photo.setFileName(fileName);
							photo.setFileSize(multipartFile.getSize());
							photo.setEntityType(EnmEntityType.PLACE.getValue());
							photo.setCreatedBy(modifiedBy);
							photo.setFileType(EnmFileType.getFileTypeWithSuffix(fileSuffix).getValue());
							photo.setCreatedDate(modifiedDate);
							
							newPlace.addPhoto(photo);
						}
					}
					
					OperationResult createResult = this.placeService.insertPlace(newPlace);
					
					try {
						String rootPhotoFolder = "D:\\Personal\\Development\\startup\\workspace\\projects\\LetsErasmus\\src\\main\\webapp\\place\\images\\";
						String placePhotoFolder = rootPhotoFolder + newPlace.getId(); 
						(new File(placePhotoFolder)).mkdirs();
						for (int i = 0; i < photoList.length; i++) {
							MultipartFile multiPartFile = photoList[i];
							com.ucgen.letserasmus.library.file.model.File photo = newPlace.getPhotoList().get(i);
							String tmpPhotoPath = placePhotoFolder + File.separatorChar + photo.getId() + "." + EnmFileType.getFileType(photo.getFileType());
							String smallPhotoPath = placePhotoFolder + File.separatorChar + photo.getId() + "_small." + EnmFileType.getFileType(photo.getFileType()).getFileSuffix();
							String largePhotoPath = placePhotoFolder + File.separatorChar + photo.getId() + "_large." + EnmFileType.getFileType(photo.getFileType()).getFileSuffix();
							File tmpFile = new File(tmpPhotoPath);
							multiPartFile.transferTo(tmpFile);
							ImageUtil.resizeImage(tmpFile, largePhotoPath, 800, 800);
							ImageUtil.resizeImage(tmpFile, smallPhotoPath, 500, 300);
							tmpFile.delete();
						}
					} catch (Exception e) {
						System.out.println(CommonUtil.getExceptionMessage(e));
					}
					*/
					operationResult = createResult;
					httpStatus = HttpStatus.OK;
				} else {
					operationResult.setResultCode(EnmResultCode.ERROR.getValue());
					operationResult.setResultDesc("You are not authorized for this operation!");
				}
			} else {
				operationResult.setResultCode(EnmResultCode.ERROR.getValue());
				operationResult.setResultDesc("You are not logged in or session is expired. Please login first.");
			}
			httpStatus = HttpStatus.OK;			
		} catch (Exception e) {
			operationResult.setResultCode(EnmResultCode.EXCEPTION.getValue());
			operationResult.setResultDesc("Update operation could not be completed. Please try again later!");
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<OperationResult>(operationResult, httpStatus);
    }
	
	@RequestMapping(value = "/api/place/get", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResponseEntity<ValueOperationResult<Place>> getPlace(@RequestParam("placeId") Long placeId, HttpSession session) {
		User user = super.getSessionUser(session);
		ValueOperationResult<Place> getResult = this.placeService.getPlace(placeId);
		Place place = getResult.getResultValue();
		if (place != null && user.getId().equals(place.getHostUserId())) {
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
				operationResult.setResultCode(EnmResultCode.WARNING.getValue());
				operationResult.setResultDesc("You need to login first.");
			}
		} catch (Exception e) {
			operationResult.setResultCode(EnmResultCode.EXCEPTION.getValue());
			operationResult.setResultDesc("Place list could not be fetched from database.");
		}
		return new ResponseEntity<ValueOperationResult<Map<String, List<Place>>>>(operationResult, HttpStatus.OK);
    }
		
	@RequestMapping(value = "/api/place/addphoto", method = RequestMethod.POST)
    public ResponseEntity<ListOperationResult<Place>> addPhoto(@RequestParam("photolist") MultipartFile[] file, 
    		@RequestParam("placeId") Integer placeId) {
		HttpStatus httpStatus = null;
		ListOperationResult<Place> listResult = this.placeService.listPlace(null, true, true, true);
		if (OperationResult.isResultSucces(listResult)) {
			httpStatus = HttpStatus.OK;
		} else {
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<ListOperationResult<Place>>(listResult, httpStatus);
    }
	
	@RequestMapping(value = "/api/place/listphoto", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResponseEntity<ListOperationResult<com.ucgen.letserasmus.library.file.model.File>> listPhoto(@RequestParam Map<String, String> requestParams) {
		HttpStatus httpStatus = null;
		String strPlaceId = requestParams.get("placeId");
		com.ucgen.letserasmus.library.file.model.File file = new com.ucgen.letserasmus.library.file.model.File();
		file.setEntityType(EnmEntityType.PLACE.getValue());
		file.setEntityId(Long.valueOf(strPlaceId));
		
		ListOperationResult<com.ucgen.letserasmus.library.file.model.File> listResult = this.fileService.listFile(file);
		
		if (OperationResult.isResultSucces(listResult)) {
			httpStatus = HttpStatus.OK;
		} else {
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<ListOperationResult<com.ucgen.letserasmus.library.file.model.File>>(listResult, httpStatus);
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
										
										operationResult = this.placeService.updatePlace(updatedPlace);
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
				operationResult.setResultCode(EnmResultCode.ERROR.getValue());
				operationResult.setResultDesc("You are not logged in or session is expired. Please login first.");
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
