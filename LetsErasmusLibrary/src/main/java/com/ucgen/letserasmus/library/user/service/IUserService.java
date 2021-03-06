package com.ucgen.letserasmus.library.user.service;

import com.ucgen.common.exception.operation.OperationResultException;
import com.ucgen.common.operationresult.OperationResult;
import com.ucgen.common.operationresult.ValueOperationResult;
import com.ucgen.letserasmus.library.user.model.User;

public interface IUserService {
	
	User getUser(User user);
	
	OperationResult insertUser(User user, String createdBy) throws OperationResultException;
	
	ValueOperationResult<Integer> updateUser(User user, boolean setNull, Long deleteProfilePhotoId) throws OperationResultException;

	User getUserForLogin(User user);
	
	OperationResult deactivateUser(User user) throws OperationResultException ;
	
}
