package com.ucgen.letserasmus.library.place.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ucgen.common.exception.operation.OperationResultException;
import com.ucgen.common.operationresult.ListOperationResult;
import com.ucgen.common.operationresult.OperationResult;
import com.ucgen.common.operationresult.ValueOperationResult;
import com.ucgen.letserasmus.library.location.service.ILocationService;
import com.ucgen.letserasmus.library.place.dao.IPlaceDao;
import com.ucgen.letserasmus.library.place.model.Place;
import com.ucgen.letserasmus.library.place.service.IPlaceService;

@Service
public class PlaceService implements IPlaceService{

	private ILocationService locationService;
	private IPlaceDao placeDao;

	@Autowired
	public void setPlaceDao(IPlaceDao placeDao) {
		this.placeDao = placeDao;
	}
	
	@Autowired
	public void setLocationService(ILocationService locationService) {
		this.locationService = locationService;
	}

	@Override
	public ValueOperationResult<Place> getPlace(Long id) {
		return this.placeDao.getPlace(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public OperationResult insertPlace(Place place) throws OperationResultException {
		if (place.getLocation() != null) {
			OperationResult createLocationResult = this.locationService.insertLocation(place.getLocation());
			if (OperationResult.isResultSucces(createLocationResult)) {
				place.setLocationId(place.getLocation().getId());
			} else {
				createLocationResult.setResultDesc("Location could not be created. Error: " + createLocationResult.getResultDesc());
				throw new OperationResultException(createLocationResult);
			}
		}
		
		OperationResult createPlaceResult = this.placeDao.insertPlace(place);
		if (OperationResult.isResultSucces(createPlaceResult)) {
			return createPlaceResult;
		} else {
			createPlaceResult.setResultDesc("Listing could not be created. Error: " + createPlaceResult.getResultDesc());
			throw new OperationResultException(createPlaceResult);
		}
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
