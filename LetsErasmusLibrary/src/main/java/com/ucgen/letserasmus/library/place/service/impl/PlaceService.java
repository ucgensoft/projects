package com.ucgen.letserasmus.library.place.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ucgen.common.exception.operation.OperationResultException;
import com.ucgen.common.operationresult.ListOperationResult;
import com.ucgen.common.operationresult.OperationResult;
import com.ucgen.common.operationresult.ValueOperationResult;
import com.ucgen.letserasmus.library.file.model.Photo;
import com.ucgen.letserasmus.library.file.service.IFileService;
import com.ucgen.letserasmus.library.location.service.ILocationService;
import com.ucgen.letserasmus.library.place.dao.IPlaceDao;
import com.ucgen.letserasmus.library.place.model.Place;
import com.ucgen.letserasmus.library.place.service.IPlaceService;

@Service
public class PlaceService implements IPlaceService{

	private ILocationService locationService;
	private IPlaceDao placeDao;
	private IFileService fileService;
	
	@Autowired
	public void setPlaceDao(IPlaceDao placeDao) {
		this.placeDao = placeDao;
	}
	
	@Autowired
	public void setLocationService(ILocationService locationService) {
		this.locationService = locationService;
	}
	
	@Autowired
	public void setFileService(IFileService fileService) {
		this.fileService = fileService;
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
			if (place.getPhotoList().size() > 0) {
				for (int i = 0; i < place.getPhotoList().size(); i++) {
					Photo photo = place.getPhotoList().get(i);
					photo.setEntityId(place.getId());
					OperationResult insertPhotoResult = fileService.insertFile(photo);
					if (OperationResult.isResultSucces(insertPhotoResult)) {
						if (i == 0) {
							place.setCoverPhotoId(photo.getId());
						}
					} else {
						insertPhotoResult.setResultDesc("Photo record not be insert. Error: " + insertPhotoResult.getResultDesc());
						throw new OperationResultException(insertPhotoResult);
					}
				}
			}
			
			this.placeDao.updatePlace(place);
			
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
