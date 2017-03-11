package com.ucgen.letserasmus.library.place.service;

import java.util.List;

import com.ucgen.common.exception.operation.OperationResultException;
import com.ucgen.common.operationresult.ListOperationResult;
import com.ucgen.common.operationresult.OperationResult;
import com.ucgen.common.operationresult.ValueOperationResult;
import com.ucgen.letserasmus.library.file.model.FileModel;
import com.ucgen.letserasmus.library.place.model.Place;

public interface IPlaceService {
	
	ValueOperationResult<Place> getPlace(Long id);
	
	OperationResult insertPlace(Place place) throws OperationResultException ;
	
	OperationResult updatePlace(Place place, FileModel coverPhoto, List<FileModel> newPhotoList, List<Long> deletePhotoList) throws OperationResultException;
	
	ListOperationResult<Place> listPlace(Place place, boolean locationFlag, boolean photoFlag, boolean userFlag, Integer pageSize, Integer pageNumber);

}
