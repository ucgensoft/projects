package com.ucgen.letserasmus.library.location.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

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
	
	public static void changeCoordinates(Location location, int radius) {
		Random random = new Random();

		// Convert radius from meters to degrees
		double radiusInDegrees = radius / 111000f;

		double u = random.nextDouble();
		double v = random.nextDouble();
		double w = radiusInDegrees * Math.sqrt(u);
		double t = 2 * Math.PI * v;
		double x = w * Math.cos(t);
		double y = w * Math.sin(t);

		// Adjust the x-coordinate for the shrinking of the east-west distances
		double new_x = x / Math.cos(Math.toRadians(location.getLongitude().doubleValue()));

		double newLongitude = new_x + location.getLatitude().doubleValue();
		double newLatitude = y + location.getLongitude().doubleValue();
		
		location.setLongitude(new BigDecimal(newLatitude));
		location.setLatitude(new BigDecimal(newLongitude));
		
	}
	
}
