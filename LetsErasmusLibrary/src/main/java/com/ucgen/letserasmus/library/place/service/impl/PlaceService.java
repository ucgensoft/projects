package com.ucgen.letserasmus.library.place.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ucgen.common.operationresult.ListOperationResult;
import com.ucgen.common.operationresult.ValueOperationResult;
import com.ucgen.letserasmus.library.place.dao.IPlaceDao;
import com.ucgen.letserasmus.library.place.model.Place;
import com.ucgen.letserasmus.library.place.service.IPlaceService;

@Service
public class PlaceService implements IPlaceService{

	private IPlaceDao iPlaceDao;
	
	public IPlaceDao getiPlaceDao() {
		return iPlaceDao;
	}

	@Autowired
	public void setiPlaceDao(IPlaceDao iPlaceDao) {
		this.iPlaceDao = iPlaceDao;
	}

	@Override
	public ListOperationResult<Place> getPlace(Long id) {
		return this.getiPlaceDao().getPlace(id);
	}

	@Override
	public ValueOperationResult<Integer> insertPlace(Place place) {
		return this.getiPlaceDao().insertPlace(place);
	}

	@Override
	public ValueOperationResult<Integer> updatePlace(Place place) {
		return this.getiPlaceDao().updatePlace(place);
	}

}
