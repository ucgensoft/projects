package com.ucgen.letserasmus.web.view.search;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

import org.apache.log4j.Level;

import com.ucgen.common.operationresult.ListOperationResult;
import com.ucgen.common.operationresult.OperationResult;
import com.ucgen.common.util.CommonUtil;
import com.ucgen.common.util.FileLogger;
import com.ucgen.common.util.WebUtil;
import com.ucgen.letserasmus.library.parameter.enumeration.EnmParameter;
import com.ucgen.letserasmus.library.parameter.service.IParameterService;
import com.ucgen.letserasmus.library.place.model.Place;
import com.ucgen.letserasmus.library.place.service.IPlaceService;
import com.ucgen.letserasmus.web.view.BaseController;

@ManagedBean
public class SearchController extends BaseController {

	private static String PARAM_LOCATION_NAME = "loc";
	private static String PARAM_LOCATION_ID = "locationId";
	private static String PARAM_START_DATE = "checkinDate";
	private static String PARAM_END_DATE = "checkoutDate";
		
	@ManagedProperty(value="#{parameterService}")
	private IParameterService parameterService;
	
	@ManagedProperty(value="#{placeService}")
	private IPlaceService placeService;
	
	private List<Place> placeList;
	
	public List<Place> getPlaceList() {
		try {
			if (this.placeList == null) {
				ListOperationResult<Place> operationResult = this.placeService.listPlace(null, null, false, false, false, Integer.MAX_VALUE, 1);
				if (OperationResult.isResultSucces(operationResult)) {
					this.placeList = operationResult.getObjectList();
				}
			}
		} catch (Exception e) {
			FileLogger.log(Level.ERROR, "SearchController-getPlaceList()- Error: " + CommonUtil.getExceptionMessage(e));
		}
		return placeList;
	}

	public void setParameterService(IParameterService parameterService) {
		this.parameterService = parameterService;
	}
	
	public void setPlaceService(IPlaceService placeService) {
		this.placeService = placeService;
	}

	private UISearchCriteria searchCriteria;
	
	public UISearchCriteria getSearchCriteria() {
		try {
			if (this.searchCriteria == null) {
				String locationName = super.getRequestParameter(PARAM_LOCATION_NAME);
				String locationId = super.getRequestParameter(PARAM_LOCATION_ID);
				String startDate = super.getRequestParameter(PARAM_START_DATE);
				String endDate = super.getRequestParameter(PARAM_END_DATE);
				
				searchCriteria = new UISearchCriteria();
				searchCriteria.setLocationName(locationName);
				searchCriteria.setLocationId(locationId);
				searchCriteria.setStartDate(startDate);
				searchCriteria.setEndDate(endDate);
			}
		} catch (Exception e) {
			FileLogger.log(Level.ERROR, "SearchController-getSearchCriteria()- Error: " + CommonUtil.getExceptionMessage(e));
		}
		return this.searchCriteria;
	}
	
	public String getPageUrl() {
		String pageUrl = "";
		try {
			String urlPrefix = this.parameterService.getParameterValue(EnmParameter.LETSERASMUS_URL_PREFIX.getId());
			pageUrl = WebUtil.concatUrl(urlPrefix, "/room/search/" + this.searchCriteria.getLocationName() + "/" + this.searchCriteria.getLocationId());
		} catch (Exception e) {
			FileLogger.log(Level.ERROR, "SearchController-getPageUrl()- Error: " + CommonUtil.getExceptionMessage(e));
		}
		return pageUrl;
	}
	
}
