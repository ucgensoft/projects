package com.ucgen.letserasmus.library.favorite.service;

import java.util.List;

import com.ucgen.common.exception.operation.OperationResultException;
import com.ucgen.common.operationresult.OperationResult;
import com.ucgen.common.operationresult.ValueOperationResult;
import com.ucgen.letserasmus.library.common.model.BaseModel;
import com.ucgen.letserasmus.library.favorite.model.Favorite;

public interface IFavoriteService {

	OperationResult insertFavorite(Favorite favorite) throws OperationResultException;
	
	List<Favorite> listFavorite(Favorite favorite, BaseModel entity, boolean entityFlag, boolean userFlag, boolean hostUserFlag);
	
	ValueOperationResult<Integer> deleteFavorite(Long id);
	
	BaseModel getEntityDetails(Integer entityType, Long entityId);
	
}
