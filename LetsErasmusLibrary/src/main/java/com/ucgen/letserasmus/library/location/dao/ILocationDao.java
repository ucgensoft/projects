package com.ucgen.letserasmus.library.location.dao;

import java.util.List;

import com.ucgen.common.operationresult.ListOperationResult;
import com.ucgen.common.operationresult.OperationResult;
import com.ucgen.letserasmus.library.location.model.Location;

public interface ILocationDao {

	Location get(Long id);
	
	OperationResult insert(Location location);
	
	ListOperationResult<Location> list(Location location);
	
	ListOperationResult<Location> list(List<Long> idList);
	
	OperationResult update(Location location);
	
}
