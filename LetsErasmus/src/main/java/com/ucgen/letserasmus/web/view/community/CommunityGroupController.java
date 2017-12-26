package com.ucgen.letserasmus.web.view.community;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

import com.ucgen.common.operationresult.EnmResultCode;
import com.ucgen.common.operationresult.ListOperationResult;
import com.ucgen.common.operationresult.OperationResult;
import com.ucgen.letserasmus.library.common.enumeration.EnmErrorCode;
import com.ucgen.letserasmus.library.community.model.CommunityGroup;
import com.ucgen.letserasmus.library.community.model.CommunityTopic;
import com.ucgen.letserasmus.library.community.service.ICommunityService;
import com.ucgen.letserasmus.library.message.service.IMessageService;
import com.ucgen.letserasmus.library.simpleobject.model.City;
import com.ucgen.letserasmus.library.simpleobject.model.Country;
import com.ucgen.letserasmus.library.simpleobject.service.ISimpleObjectService;
import com.ucgen.letserasmus.web.view.BaseController;

@ManagedBean
public class CommunityGroupController extends BaseController {
	
	private static String PARAM_GROUP_SUB_URL = "communityGroupSubUrl";
	private List<Country> countryList;
	private List<City> cityList;
	private List<CommunityGroup> communityGroupList;
	private List<CommunityTopic> communityTopicList;
	private List<CommunityTopic> commonTopicList;
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

	public List<CommunityGroup> getCommunityGroupList() {
		return communityGroupList;
	}

	public List<CommunityTopic> getCommunityTopicList() {
		return communityTopicList;
	}
	
	public List<CommunityTopic> getCommonTopicList() {
		return this.commonTopicList;
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
		this.countryList = this.simpleObjectService.listCountry();
		
		String communityGroupSubUrl = super.getRequestParameter(PARAM_GROUP_SUB_URL);
		if (communityGroupSubUrl != null && !communityGroupSubUrl.isEmpty()) {
			CommunityGroup communityGroup = new CommunityGroup();
			communityGroup.setSubUrl(communityGroupSubUrl);
			
			CommunityGroup dbCommunityGroup = this.communityService.getCommunityGroup(communityGroup);
			if (dbCommunityGroup != null) {
				this.communityGroup = dbCommunityGroup;
				this.selectedCountryId = this.communityGroup.getCountryId();
				this.selectedCityId = this.communityGroup.getCityId();
				CommunityGroup countryGroup = new CommunityGroup();
				countryGroup.setCountryId(this.communityGroup.getCountryId());
				ListOperationResult<CommunityGroup> listCommGroupResult = this.communityService.listCommunityGroup(countryGroup);
				if (OperationResult.isResultSucces(listCommGroupResult)) {
					this.communityGroupList = listCommGroupResult.getObjectList();
				}
				this.communityTopicList = this.communityService.listCommunityTopic(new CommunityTopic(null, this.communityGroup.getId()), true, true).getObjectList();
			} else {
				OperationResult operationResult = new OperationResult();
				operationResult.setResultCode(EnmResultCode.ERROR.getValue());
				operationResult.setResultDesc("Community Group not found for sub url: " + communityGroupSubUrl);;
				operationResult.setErrorCode(EnmErrorCode.PAGE_NOT_FOUND.getId());
				//throw new OperationResultException(operationResult);
			}
		}
		this.commonTopicList = this.communityService.listCommonTopic(true, true).getObjectList();
	}
		
}
