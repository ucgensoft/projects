package com.ucgen.letserasmus.web.view.community;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

import org.apache.log4j.Level;

import com.ucgen.common.util.CommonUtil;
import com.ucgen.common.util.FileLogger;
import com.ucgen.letserasmus.library.common.enumeration.EnmEntityType;
import com.ucgen.letserasmus.library.community.model.CommunityGroup;
import com.ucgen.letserasmus.library.community.service.ICommunityService;
import com.ucgen.letserasmus.library.message.model.MessageThread;
import com.ucgen.letserasmus.library.message.service.IMessageService;
import com.ucgen.letserasmus.library.simpleobject.model.City;
import com.ucgen.letserasmus.library.simpleobject.model.Country;
import com.ucgen.letserasmus.library.simpleobject.service.ISimpleObjectService;
import com.ucgen.letserasmus.web.view.BaseController;

@ManagedBean
public class CommunityTopicListController extends BaseController {
	
	private static String PARAM_CITY_NAME = "cityName";
	private List<Country> countryList;
	private List<City> cityList;
	private List<MessageThread> messageThreadList;
	private Integer selectedCountryId;
	private Integer selectedCityId;
	private CommunityGroup communityGroup;
	
	@ManagedProperty(value="#{simpleObjectService}")
	private ISimpleObjectService simpleObjectService;
	
	@ManagedProperty(value="#{communityService}")
	private ICommunityService communityService;
	
	@ManagedProperty(value="#{messageService}")
	private IMessageService messageService;
	
	public void setSimpleObjectService(ISimpleObjectService simpleObjectService) {
		this.simpleObjectService = simpleObjectService;
	}

	public void setCommunityService(ICommunityService communityService) {
		this.communityService = communityService;
	}

	public void setMessageService(IMessageService messageService) {
		this.messageService = messageService;
	}

	public List<Country> getCountryList() {
		return countryList;
	}

	public List<City> getCityList() {
		return cityList;
	}

	public void setCityList(List<City> cityList) {
		this.cityList = cityList;
	}

	public void setCountryList(List<Country> countryList) {
		this.countryList = countryList;
	}

	public List<MessageThread> getMessageThreadList() {
		return messageThreadList;
	}

	public Integer getSelectedCountryId() {
		return selectedCountryId;
	}

	public void setSelectedCountryId(Integer selectedCountryId) {
		this.selectedCountryId = selectedCountryId;
	}

	public Integer getSelectedCityId() {
		return selectedCityId;
	}

	public void setSelectedCityId(Integer selectedCityId) {
		this.selectedCityId = selectedCityId;
	}

	public CommunityGroup getCommunityGroup() {
		return communityGroup;
	}

	@PostConstruct
	public void initialize() {
		try {
			this.countryList = this.simpleObjectService.listCountry();
			
			String cityName = super.getRequestParameter(PARAM_CITY_NAME);
			if (cityName != null && !cityName.isEmpty()) {
				City city = new City();
				city.setName(cityName);
				city = this.simpleObjectService.getCity(city);
				if (city != null) {
					this.selectedCountryId = city.getCountryId();
					this.selectedCityId = city.getId();
					CommunityGroup communityGroup = new CommunityGroup();
					communityGroup.setCityId(this.selectedCityId);
					this.communityGroup = this.communityService.getCommunityGroup(communityGroup);
					MessageThread messageThread = new MessageThread();
					messageThread.setEntityType(EnmEntityType.COMMUNITYGROUP.getId());
					this.messageThreadList = this.messageService.listMessageThread(messageThread, false, false, false, true);
				}
			}
		} catch (Exception e) {
			FileLogger.log(Level.ERROR, "CommunityTopicListController-initialize()- Error: " + CommonUtil.getExceptionMessage(e));
		}
	}
		
}
