package com.ucgen.letserasmus.library.user.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ucgen.common.operationresult.ListOperationResult;
import com.ucgen.common.operationresult.ValueOperationResult;
import com.ucgen.letserasmus.library.user.dao.IUserDao;
import com.ucgen.letserasmus.library.user.model.User;
import com.ucgen.letserasmus.library.user.service.IUserService;

@Service
public class UserService implements IUserService{


	private IUserDao userDao;

	public IUserDao getUserDao() {
		return userDao;
	}

	@Autowired
	public void setUserDao(IUserDao userDao) {
		this.userDao = userDao;
	}

	@Override
	public ListOperationResult<User> getUser(Long id) {
		return this.getUserDao().getUser(id);
	}

	@Override
	public ValueOperationResult<Integer> insertUser(User user) {
		return this.getUserDao().insertUser(user);
	}

	@Override
	public ValueOperationResult<Integer> updateUser(User user) {
		return this.getUserDao().updateUser(user);
	}

}
