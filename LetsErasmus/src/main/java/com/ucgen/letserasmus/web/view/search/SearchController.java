package com.ucgen.letserasmus.web.view.search;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

import org.apache.log4j.Level;

import com.ucgen.common.util.CommonUtil;
import com.ucgen.common.util.FileLogger;
import com.ucgen.common.util.WebUtil;
import com.ucgen.letserasmus.library.parameter.enumeration.EnmParameter;
import com.ucgen.letserasmus.library.parameter.service.IParameterService;
import com.ucgen.letserasmus.web.view.BaseController;

@ManagedBean
public class SearchController extends BaseController {

	private static String PARAM_LOCATION_NAME = "loc";
	private static String PARAM_LOCATION_ID = "locationId";
	private static String PARAM_START_DATE = "checkinDate";
	private static String PARAM_END_DATE = "checkoutDate";
		
	@ManagedProperty(value="#{parameterService}")
	private IParameterService parameterService;
	
	public void setParameterService(IParameterService parameterService) {
		this.parameterService = parameterService;
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
