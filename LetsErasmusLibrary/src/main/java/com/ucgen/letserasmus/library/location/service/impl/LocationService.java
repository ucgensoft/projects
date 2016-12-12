package com.ucgen.letserasmus.library.location.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ucgen.common.operationresult.ListOperationResult;
import com.ucgen.letserasmus.library.location.dao.ILocationDao;
import com.ucgen.letserasmus.library.location.model.Location;
import com.ucgen.letserasmus.library.location.service.ILocationService;

@Service
public class LocationService implements ILocationService {

	private ILocationDao locationDao;
	
	@Autowired
	public void setLocationDao(ILocationDao locationDao) {
		this.locationDao = locationDao;
	}

	@Override
	public ListOperationResult<Location> listLocation(Location location) {
		return this.locationDao.listLocation(location);
	}

	@Override
	public ListOperationResult<Location> listLocation(List<Long> idList) {
		return this.locationDao.listLocation(idList);
	}
	
}
