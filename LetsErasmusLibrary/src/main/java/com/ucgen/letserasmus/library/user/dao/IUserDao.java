package com.ucgen.letserasmus.library.user.dao;

import com.ucgen.common.operationresult.ListOperationResult;
import com.ucgen.common.operationresult.ValueOperationResult;
import com.ucgen.letserasmus.library.user.model.User;

public interface IUserDao {	
	ListOperationResult<User> getUser(Long id);
	ValueOperationResult<Integer> insertUser(User user);
	ValueOperationResult<Integer> updateUser(User user);
}
