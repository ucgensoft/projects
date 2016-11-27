package com.ucgen.letserasmus.web.view.main;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

import com.ucgen.letserasmus.library.place.service.IPlaceService;
import com.ucgen.letserasmus.web.view.BaseController;

@ManagedBean
public class MainController extends BaseController {

	private String searchPlace;
	private String searchStartDate;
	private String searchEndDate;
	
	@ManagedProperty(value="#{placeService}")
	private IPlaceService placeService;
	
	public String getSearchPlace() {
		return searchPlace;
	}

	public void setSearchPlace(String searchPlace) {
		this.searchPlace = searchPlace;
	}

	public String getSearchStartDate() {
		return searchStartDate;
	}

	public void setSearchStartDate(String searchStartDate) {
		this.searchStartDate = searchStartDate;
	}

	public String getSearchEndDate() {
		return searchEndDate;
	}

	public void setSearchEndDate(String searchEndDate) {
		this.searchEndDate = searchEndDate;
	}

	public IPlaceService getPlaceService() {
		return placeService;
	}

	public void setPlaceService(IPlaceService placeService) {
		this.placeService = placeService;
	}
	
	public MainController() {
		
	}
	
	public void search() {
		try {
			System.out.println();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
}
