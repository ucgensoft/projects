package com.ucgen.letserasmus.web.view.place;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

import org.apache.log4j.Level;

import com.ucgen.common.operationresult.OperationResult;
import com.ucgen.common.operationresult.ValueOperationResult;
import com.ucgen.common.util.CommonUtil;
import com.ucgen.common.util.FileLogger;
import com.ucgen.letserasmus.library.parameter.enumeration.EnmParameter;
import com.ucgen.letserasmus.library.parameter.service.IParameterService;
import com.ucgen.letserasmus.library.place.enumeration.EnmPlaceType;
import com.ucgen.letserasmus.library.place.model.Place;
import com.ucgen.letserasmus.library.place.service.IPlaceService;
import com.ucgen.letserasmus.web.view.BaseController;
import com.ucgen.letserasmus.web.view.application.WebApplication;

@ManagedBean
public class PlaceDetailController extends BaseController {

	private static String PARAM_PLACE_ID = "placeId";
	
	@ManagedProperty(value="#{placeService}")
	private IPlaceService placeService;
	
	public void setPlaceService(IPlaceService placeService) {
		this.placeService = placeService;
	}

	@ManagedProperty(value="#{parameterService}")
	private IParameterService parameterService;
	
	public void setParameterService(IParameterService parameterService) {
		this.parameterService = parameterService;
	}
	
	private Place place;
	
	public Place getPlace() {
		try {
			if (this.place == null) {
				String placeId = super.getRequestParameter(PARAM_PLACE_ID);
				if (placeId != null) {
					ValueOperationResult<Place> operationResult = this.placeService.getPlace(new Long(placeId));
					if (OperationResult.isResultSucces(operationResult)) {
						this.place = operationResult.getResultValue();
					}
				} 
			}
		} catch (Exception e) {
			FileLogger.log(Level.ERROR, "PlaceDetailController-getPlace- Error: " + CommonUtil.getExceptionMessage(e));
		}
		return this.place;
	}
	
	public String getGuestNumberArr() {
		try {
			if (this.place == null) {
				this.getPlace();
			}
			if (this.place != null) {
				List<Integer> numList = new ArrayList<>();
				for (int i = 1; i <= this.place.getGuestNumber(); i++) {
					numList.add(i);
				}
				String objJson = CommonUtil.toJson(numList.toArray());
				return objJson;
			} else {
				return "[]";
			}
		} catch (Exception e) {
			FileLogger.log(Level.ERROR, "PlaceDetailController-getGuestNumberArr- Error: " + CommonUtil.getExceptionMessage(e));
			return "[]";
		}
	}
	
	public String getPlaceDetailUrl() {
		try {
			String urlPrefix = this.parameterService.getParameterValue(EnmParameter.LETSERASMUS_URL_PREFIX.getId());
			Place tmpPlace = this.getPlace();
			if (tmpPlace != null) {
				return this.getWebApplication().getPlaceDetailUrl(tmpPlace.getId().toString());
			} else {
				return urlPrefix;
			}
		} catch (Exception e) {
			FileLogger.log(Level.ERROR, "PlaceDetailController-getPlaceDetailUrl- Error: " + CommonUtil.getExceptionMessage(e));
			return "https://www.letserasmus.com";
		}
	}
		
	public String getPageTitle() {
		try {
			Place tmpPlace = this.getPlace();
			if (tmpPlace != null) {
				if (tmpPlace.getLocation() != null) {
					return tmpPlace.getTitle() + " - " + EnmPlaceType.getPlaceType(tmpPlace.getPlaceTypeId()).getText() 
						+ " for rent in " + tmpPlace.getLocation().getState() + ", " + tmpPlace.getLocation().getCountry() + " - Let's Erasmus";
				} else {
					return tmpPlace.getTitle() + " - " + EnmPlaceType.getPlaceType(tmpPlace.getPlaceTypeId()).getText() + " for rent" + " - Let's Erasmus";
				}
			} else {
				return WebApplication.DEFAULT_PAGE_TITLE;
			}
		} catch (Exception e) {
			FileLogger.log(Level.ERROR, "PlaceDetailController-getPageTitle- Error: " + CommonUtil.getExceptionMessage(e));
			return "https://www.letserasmus.com";
		}
	}
	
	public String getPageDescription() {
		try {
			Place tmpPlace = this.getPlace();
			if (tmpPlace != null) {
				if (tmpPlace.getDescription().length() > 300) {
					return tmpPlace.getDescription().substring(0, 300) + "...";
				} else {
					return tmpPlace.getDescription();
				}
			} else {
				return WebApplication.DEFAULT_PAGE_DESCRIPTION;
			}
		} catch (Exception e) {
			FileLogger.log(Level.ERROR, "PlaceDetailController-getPageTitle- Error: " + CommonUtil.getExceptionMessage(e));
			return "https://www.letserasmus.com";
		}
	}
	
	public String getLocationText() {
		try {
			Place tmpPlace = this.getPlace();
			if (tmpPlace != null) {
				String locationText = tmpPlace.getLocation().getCountry() + ", " + tmpPlace.getLocation().getState();
				  if (tmpPlace.getLocation().getStreet() != null && tmpPlace.getLocation().getStreet() != "") {
					  locationText += ", " + tmpPlace.getLocation().getStreet();
				  }
				  return locationText;
			} else {
				return "";
			}
		} catch (Exception e) {
			FileLogger.log(Level.ERROR, "PlaceDetailController-getLocationText- Error: " + CommonUtil.getExceptionMessage(e));
			return "";
		}
	}
	
	public String getPlaceTypeDescription() {
		String placeTypeDesc = "";
		try {
			Place tmpPlace = this.getPlace();
			if (tmpPlace != null) {
				if (tmpPlace.getPlaceTypeId().intValue() == 1) {
					placeTypeDesc = "Entire Place";
				} else if (tmpPlace.getPlaceTypeId().intValue() == 2) {
					placeTypeDesc = "Private Room";
				} else if (tmpPlace.getPlaceTypeId().intValue() == 3) {
					placeTypeDesc = "Shared Room";
				}
			}
		} catch (Exception e) {
			FileLogger.log(Level.ERROR, "PlaceDetailController-getPlaceTypeDescription- Error: " + CommonUtil.getExceptionMessage(e));
		}
		return placeTypeDesc;
	}
	
	public String getHomeTypeDescription() {
		String homeTypeDesc = "";
		try {
			Place tmpPlace = this.getPlace();
			if (tmpPlace != null) {
				if (tmpPlace.getHomeTypeId().intValue() == 1) {
					homeTypeDesc = "House";
				} else if (tmpPlace.getHomeTypeId().intValue() == 2) {
					homeTypeDesc = "Apartment";
				} else if (tmpPlace.getHomeTypeId().intValue() == 3) {
					homeTypeDesc = "Hostel";
				}
			}
		} catch (Exception e) {
			FileLogger.log(Level.ERROR, "PlaceDetailController-getHomeTypeDescription- Error: " + CommonUtil.getExceptionMessage(e));
		}
		return homeTypeDesc;
	}
	
	public String getPriceText() {
		String priceText = "";
		try {
			Place tmpPlace = this.getPlace();
			if (tmpPlace != null) {
				priceText = this.getWebApplication().getPriceText(tmpPlace.getPrice(), tmpPlace.getCurrencyId());
			}
		} catch (Exception e) {
			FileLogger.log(Level.ERROR, "PlaceDetailController-getPriceText- Error: " + CommonUtil.getExceptionMessage(e));
		}
		return priceText;
	}
	
}
