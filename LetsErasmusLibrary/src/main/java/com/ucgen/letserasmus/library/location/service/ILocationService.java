package com.ucgen.letserasmus.library.location.service;

import java.util.List;

import com.ucgen.common.operationresult.ListOperationResult;
import com.ucgen.common.operationresult.OperationResult;
import com.ucgen.letserasmus.library.location.model.Location;

public interface ILocationService {

	OperationResult insertLocation(Location location);
	
	ListOperationResult<Location> listLocation(Location location);
	
	ListOperationResult<Location> listLocation(List<Long> idList);
	
}
