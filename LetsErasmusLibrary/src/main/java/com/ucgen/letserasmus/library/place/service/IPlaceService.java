package com.ucgen.letserasmus.library.place.service;

import com.ucgen.common.exception.operation.OperationResultException;
import com.ucgen.common.operationresult.ListOperationResult;
import com.ucgen.common.operationresult.OperationResult;
import com.ucgen.common.operationresult.ValueOperationResult;
import com.ucgen.letserasmus.library.place.model.Place;

public interface IPlaceService {
	
	ValueOperationResult<Place> getPlace(Long id);
	
	OperationResult insertPlace(Place place) throws OperationResultException ;
	
	ValueOperationResult<Integer> updatePlace(Place place);
	
	ListOperationResult<Place> listPlace(Place place, boolean locationFlag);

}
