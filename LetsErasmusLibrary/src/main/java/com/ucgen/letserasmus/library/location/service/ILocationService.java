package com.ucgen.letserasmus.library.location.service;

import java.util.List;

import com.ucgen.common.operationresult.ListOperationResult;
import com.ucgen.letserasmus.library.location.model.Location;

public interface ILocationService {

	ListOperationResult<Location> listLocation(Location location);
	
	ListOperationResult<Location> listLocation(List<Long> idList);
	
}
