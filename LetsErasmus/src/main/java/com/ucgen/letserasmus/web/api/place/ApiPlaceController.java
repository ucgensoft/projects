package com.ucgen.letserasmus.web.api.place;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.ucgen.common.util.CommonUtil;
import com.ucgen.common.util.DateUtil;
import com.ucgen.common.util.ImageUtil;
import com.ucgen.letserasmus.library.common.enumeration.EnmEntityType;
import com.ucgen.letserasmus.library.file.enumeration.EnmFileType;
import com.ucgen.letserasmus.library.file.model.Photo;
import com.ucgen.letserasmus.library.location.model.Location;
import com.ucgen.letserasmus.library.place.enumeration.EnmPlaceStatus;
import com.ucgen.letserasmus.library.place.model.Place;
import com.ucgen.letserasmus.library.place.service.IPlaceService;
import com.ucgen.letserasmus.library.user.model.User;
import com.ucgen.letserasmus.web.api.BaseApiController;

@RestController
public class ApiPlaceController extends BaseApiController {

	private IPlaceService placeService;
	
	@Autowired
	public void setPlaceService(IPlaceService placeService) {
		this.placeService = placeService;
	}
	
	@RequestMapping(value = "/api/place/create", method = RequestMethod.POST)
    public ResponseEntity<OperationResult> createPlace(@RequestParam("photoList") MultipartFile[] photoList, @RequestParam("place") String place, HttpSession session) throws JsonParseException, JsonMappingException, IOException, ParseException {
		HttpStatus httpStatus = null;
		OperationResult operationResult = new OperationResult();
		
		try {
			String strPlaceJSON = place;
			
			ObjectMapper mapper = new ObjectMapper();
			
			Map placeMap = mapper.readValue(strPlaceJSON, Map.class);
			
			Place newPlace = new Place();
			
			newPlace.setPlaceTypeId(this.getInteger(placeMap.get("placeType")));
			newPlace.setHomeTypeId(this.getInteger(placeMap.get("homeType")));
			newPlace.setBathRoomNumber(this.getInteger(placeMap.get("batchroomNumber")));
			newPlace.setAmenties(placeMap.get("amenties").toString());
			newPlace.setSafetyAmenties(placeMap.get("safetyAmenties").toString());
			newPlace.setRules(placeMap.get("rules").toString());
			newPlace.setBathRoomNumber(this.getInteger(placeMap.get("batchroomNumber")));
			newPlace.setBathRoomType(this.getInteger(placeMap.get("batchroomType")));
			newPlace.setBedNumber(this.getInteger(placeMap.get("bedNumber")));
			newPlace.setGuestNumber(this.getInteger(placeMap.get("guestNumber")));
			
			if (placeMap.get("placeMateNumber") != null && !placeMap.get("placeMateNumber").equals("0")) {
				newPlace.setPlaceMateNumber(this.getInteger(placeMap.get("placeMateNumber")));
				newPlace.setPlaceMateGender(this.getInteger(placeMap.get("placeMateNumber")));
			}
			
			newPlace.setPrice(new BigDecimal(placeMap.get("price").toString()));
			newPlace.setDepositPrice(new BigDecimal(placeMap.get("depositPrice").toString()));
			newPlace.setCurrencyId(this.getInteger(placeMap.get("currencyId")));
			if (placeMap.get("billsIncluded").equals("1")) {
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
			
			newPlace.setStatus(EnmPlaceStatus.INITIAL.getValue());
			
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
			newLocation.setCreatedBy(createdBy);
			
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
					Photo photo = newPlace.getPhotoList().get(i);
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
	
	@RequestMapping(value = "/api/place/list", method = RequestMethod.GET)
    public ResponseEntity<ListOperationResult<Place>> listPlace(@RequestParam Map<String, String> requestParams) {
		HttpStatus httpStatus = null;
		ListOperationResult<Place> listResult = this.placeService.listPlace(null, true);
		if (OperationResult.isResultSucces(listResult)) {
			httpStatus = HttpStatus.OK;
		} else {
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<ListOperationResult<Place>>(listResult, httpStatus);
    }
		
	@RequestMapping(value = "/api/place/addphoto", method = RequestMethod.POST)
    public ResponseEntity<ListOperationResult<Place>> addPhoto(@RequestParam("photolist") MultipartFile[] file, 
    		@RequestParam("placeId") Integer placeId) {
		HttpStatus httpStatus = null;
		ListOperationResult<Place> listResult = this.placeService.listPlace(null, true);
		if (OperationResult.isResultSucces(listResult)) {
			httpStatus = HttpStatus.OK;
		} else {
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<ListOperationResult<Place>>(listResult, httpStatus);
    }
	
}
