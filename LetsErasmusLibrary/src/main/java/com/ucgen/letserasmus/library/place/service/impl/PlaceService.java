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

	private IPlaceDao placeDao;

	@Autowired
	public void setPlaceDao(IPlaceDao placeDao) {
		this.placeDao = placeDao;
	}

	@Override
	public ValueOperationResult<Place> getPlace(Long id) {
		return this.placeDao.getPlace(id);
	}

	@Override
	public ValueOperationResult<Integer> insertPlace(Place place) {
		return this.placeDao.insertPlace(place);
	}

	@Override
	public ValueOperationResult<Integer> updatePlace(Place place) {
		return this.placeDao.updatePlace(place);
	}

	@Override
	public ListOperationResult<Place> listPlace(Place place, boolean locationFlag) {
		return this.placeDao.listPlace(place, locationFlag);
	}

}
