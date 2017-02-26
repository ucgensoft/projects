package com.ucgen.letserasmus.web.view.place;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

import com.ucgen.common.operationresult.OperationResult;
import com.ucgen.common.operationresult.ValueOperationResult;
import com.ucgen.common.util.CommonUtil;
import com.ucgen.letserasmus.library.place.model.Place;
import com.ucgen.letserasmus.library.place.service.IPlaceService;
import com.ucgen.letserasmus.web.view.BaseController;

@ManagedBean
public class PlaceDetailController extends BaseController {

	private static String PARAM_PLACE_ID = "placeId";
	
	@ManagedProperty(value="#{placeService}")
	private IPlaceService placeService;
	
	public void setPlaceService(IPlaceService placeService) {
		this.placeService = placeService;
	}

	private Place place;
	
	public Place getPlace() {
		if (this.place == null) {
			String placeId = super.getRequestParameter(PARAM_PLACE_ID);
			if (placeId != null) {
				ValueOperationResult<Place> operationResult = this.placeService.getPlace(new Long(placeId));
				if (OperationResult.isResultSucces(operationResult)) {
					this.place = operationResult.getResultValue();
				}
			} 
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
			return "[]";
		}
	}
	
}
