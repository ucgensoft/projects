package com.ucgen.letserasmus.library.favorite.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ucgen.common.exception.operation.OperationResultException;
import com.ucgen.common.operationresult.OperationResult;
import com.ucgen.common.operationresult.ValueOperationResult;
import com.ucgen.letserasmus.library.common.enumeration.EnmEntityType;
import com.ucgen.letserasmus.library.common.model.BaseModel;
import com.ucgen.letserasmus.library.favorite.dao.IFavoriteDao;
import com.ucgen.letserasmus.library.favorite.model.Favorite;
import com.ucgen.letserasmus.library.favorite.service.IFavoriteService;
import com.ucgen.letserasmus.library.location.model.Location;
import com.ucgen.letserasmus.library.place.model.Place;
import com.ucgen.letserasmus.library.place.service.IPlaceService;

@Service
public class FavoriteService implements IFavoriteService {

	private IFavoriteDao favoriteDao;
	private IPlaceService placeService;
	
	@Autowired
	public void setPlaceService(IPlaceService placeService) {
		this.placeService = placeService;
	}

	@Autowired
	public void setFavoriteDao(IFavoriteDao favoriteDao) {
		this.favoriteDao = favoriteDao;
	}

	@Override
	public OperationResult insertFavorite(Favorite favorite) throws OperationResultException {
		return this.favoriteDao.insertFavorite(favorite);
	}

	@Override
	public List<Favorite> listFavorite(Favorite favorite, BaseModel entity, boolean entityFlag, boolean userFlag,
			boolean hostUserFlag) {
		return this.favoriteDao.listFavorite(favorite, entity, entityFlag, userFlag, hostUserFlag);
	}

	@Override
	public ValueOperationResult<Integer> deleteFavorite(Long id) {
		return this.favoriteDao.deleteFavorite(id);
	}

	@Override
	public BaseModel getEntityDetails(Integer entityType, Long entityId) {
		BaseModel entity = null;
		if (entityType.equals(EnmEntityType.PLACE.getId())) {
			Place place = this.placeService.getPlace(entityId).getResultValue();
			Place tmpPlace = new Place();
			tmpPlace.setId(place.getId());
			tmpPlace.setCoverPhotoId(place.getCoverPhotoId());
			tmpPlace.setTitle(place.getTitle());
			Location location = new Location();
			tmpPlace.setLocation(location);
			location.setCountry(place.getLocation().getCountry());
			location.setState(place.getLocation().getState());
			entity = tmpPlace;
		}
		return entity;
	}
	
}