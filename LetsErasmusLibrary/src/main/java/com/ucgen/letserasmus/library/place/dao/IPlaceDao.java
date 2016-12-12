package com.ucgen.letserasmus.library.place.dao;

import com.ucgen.common.operationresult.ListOperationResult;
import com.ucgen.common.operationresult.ValueOperationResult;
import com.ucgen.letserasmus.library.place.model.Place;

public interface IPlaceDao {
	
	ValueOperationResult<Place> getPlace(Long id);
	
	ValueOperationResult<Integer> insertPlace(Place place);
	
	ValueOperationResult<Integer> updatePlace(Place place);
	
	ListOperationResult<Place> listPlace(Place place, boolean locationFlag);

}
