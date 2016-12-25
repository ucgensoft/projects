package com.ucgen.letserasmus.web.api.place;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
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

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ucgen.common.operationresult.EnmResultCode;
import com.ucgen.common.operationresult.ListOperationResult;
import com.ucgen.common.operationresult.OperationResult;
import com.ucgen.common.util.DateUtil;
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
    public ResponseEntity<OperationResult> createPlace(@RequestBody String place, HttpSession session) throws JsonParseException, JsonMappingException, IOException, ParseException {
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
			
			OperationResult createResult = this.placeService.insertPlace(newPlace);
			
			if (OperationResult.isResultSucces(createResult)) {
				httpStatus = HttpStatus.OK;
			} else {
				httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
			}
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
	
	@RequestMapping(value = "/api/place/login", method = RequestMethod.POST)
    public ResponseEntity<OperationResult> login(@RequestParam Map<String, String> requestParams, HttpSession session) {
		HttpStatus httpStatus = null;
		OperationResult operationResult = new OperationResult();
		operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
		operationResult.setResultDesc("Login iþlemi baþarýlý.");
		
		if (OperationResult.isResultSucces(operationResult)) {
			User user = new User();
			user.setEmail(requestParams.get("email"));
			session.setAttribute("USER", user);
		}
		
		httpStatus = HttpStatus.OK;
		return new ResponseEntity<OperationResult>(operationResult, httpStatus);
    }
	
}
