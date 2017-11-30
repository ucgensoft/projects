package com.ucgen.letserasmus.web.view.user;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

import org.apache.log4j.Level;

import com.ucgen.common.util.CommonUtil;
import com.ucgen.common.util.FileLogger;
import com.ucgen.letserasmus.library.simpleobject.model.City;
import com.ucgen.letserasmus.library.simpleobject.model.Country;
import com.ucgen.letserasmus.library.simpleobject.model.University;
import com.ucgen.letserasmus.library.simpleobject.service.ISimpleObjectService;
import com.ucgen.letserasmus.library.user.model.User;
import com.ucgen.letserasmus.web.view.BaseController;

@ManagedBean
public class UserController extends BaseController {
	
	private static String PARAM_USER_ID = "userId";
	private List<Country> countryList;
	private List<City> homeCityList;
	private List<University> homeUniversityList;
	private List<City> erasmusCityList;
	private List<University> erasmusUniversityList;
	
	@ManagedProperty(value="#{simpleObjectService}")
	private ISimpleObjectService simpleObjectService;
	
	public void setSimpleObjectService(ISimpleObjectService simpleObjectService) {
		this.simpleObjectService = simpleObjectService;
	}

	public List<Country> getCountryList() {
		return countryList;
	}

	public List<City> getHomeCityList() {
		return homeCityList;
	}

	public List<University> getHomeUniversityList() {
		return homeUniversityList;
	}

	public List<City> getErasmusCityList() {
		return erasmusCityList;
	}

	public List<University> getErasmusUniversityList() {
		return erasmusUniversityList;
	}

	@PostConstruct
	public void initialize() {
		try {
			User user = super.getSessionUser();
			if (user != null) {
				this.countryList = this.simpleObjectService.listCountry();
				if (user.getHomeCountryId() != null) {
					City city = new City();
					city.setCountryId(user.getHomeCountryId());
					this.homeCityList = this.simpleObjectService.listCity(city);
					this.homeUniversityList = this.simpleObjectService.listUniversity(user.getHomeCountryId());
				}
				if (user.getErasmusCountryId() != null) {
					City city = new City();
					city.setCountryId(user.getErasmusCountryId());
					this.erasmusCityList = this.simpleObjectService.listCity(city);
					this.erasmusUniversityList = this.simpleObjectService.listUniversity(user.getErasmusCountryId());
				}
			}
		} catch (Exception e) {
			FileLogger.log(Level.ERROR, "UserController-initialize()- Error: " + CommonUtil.getExceptionMessage(e));
		}
	}
	
	public Long getUserId() {
		try {
			String paramUserId = super.getRequestParameter(PARAM_USER_ID);
			if (paramUserId != null) {
				return Long.valueOf(paramUserId);
			} else {
				return null;
			}
		} catch (Exception e) {
			FileLogger.log(Level.ERROR, "UserController-getUserId()- Error: " + CommonUtil.getExceptionMessage(e));
			return null;
		}
	}
	
}
