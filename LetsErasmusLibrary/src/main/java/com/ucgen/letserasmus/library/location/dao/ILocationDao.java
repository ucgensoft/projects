package com.ucgen.letserasmus.library.location.dao;

import java.util.List;

import com.ucgen.common.operationresult.ListOperationResult;
import com.ucgen.common.operationresult.OperationResult;
import com.ucgen.letserasmus.library.location.model.Location;

public interface ILocationDao {

	OperationResult insertLocation(Location location);
	
	ListOperationResult<Location> listLocation(Location location);
	
	ListOperationResult<Location> listLocation(List<Long> idList);
	
}
