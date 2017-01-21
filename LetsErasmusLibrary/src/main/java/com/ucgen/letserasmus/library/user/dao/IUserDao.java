package com.ucgen.letserasmus.library.user.dao;

import com.ucgen.common.operationresult.OperationResult;
import com.ucgen.common.operationresult.ValueOperationResult;
import com.ucgen.letserasmus.library.user.model.User;

public interface IUserDao {	
	
	User getUser(User user);
	
	OperationResult insertUser(User user);
	
	ValueOperationResult<Integer> updateUser(User user);
	
}
