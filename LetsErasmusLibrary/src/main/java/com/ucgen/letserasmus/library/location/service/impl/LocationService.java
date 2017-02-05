package com.ucgen.letserasmus.library.location.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ucgen.common.operationresult.ListOperationResult;
import com.ucgen.common.operationresult.OperationResult;
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
	public ListOperationResult<Location> list(Location location) {
		return this.locationDao.list(location);
	}

	@Override
	public ListOperationResult<Location> list(List<Long> idList) {
		return this.locationDao.list(idList);
	}

	@Override
	public OperationResult insert(Location location) {
		return this.locationDao.insert(location);
	}

	@Override
	public OperationResult update(Location location) {
		return this.locationDao.update(location);
	}
	
}
