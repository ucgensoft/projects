package com.ucgen.letserasmus.library.user.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ucgen.common.exception.operation.OperationResultException;
import com.ucgen.common.operationresult.OperationResult;
import com.ucgen.common.operationresult.ValueOperationResult;
import com.ucgen.letserasmus.library.file.model.FileModel;
import com.ucgen.letserasmus.library.file.service.IFileService;
import com.ucgen.letserasmus.library.user.dao.IUserDao;
import com.ucgen.letserasmus.library.user.model.User;
import com.ucgen.letserasmus.library.user.service.IUserService;

@Service
public class UserService implements IUserService{


	private IUserDao userDao;
	private IFileService fileService;

	public IUserDao getUserDao() {
		return userDao;
	}

	@Autowired
	public void setFileService(IFileService fileService) {
		this.fileService = fileService;
	}

	@Autowired
	public void setUserDao(IUserDao userDao) {
		this.userDao = userDao;
	}

	@Override
	public User getUser(User user) {
		return this.getUserDao().getUser(user);
	}

	@Override
	public OperationResult insertUser(User user) {
		return this.getUserDao().insertUser(user);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public ValueOperationResult<Integer> updateUser(User user, boolean setNull) throws OperationResultException {
		Long oldProfilePhotoId = user.getProfilePhotoId();
		FileModel profilePhoto = user.getProfilePhoto();
		if (user.getProfilePhoto() != null && user.getProfilePhoto().getId() == null) {
			OperationResult insertPhotoResult = fileService.insertFile(profilePhoto);
			if (OperationResult.isResultSucces(insertPhotoResult)) {
				user.setProfilePhotoId(profilePhoto.getId());
			} else {
				insertPhotoResult.setResultDesc("Photo record not be insert. Error: " + insertPhotoResult.getResultDesc());
				throw new OperationResultException(insertPhotoResult);
			}
		}
		
		ValueOperationResult<Integer> createUserResult = this.getUserDao().updateUser(user, setNull);
		if (OperationResult.isResultSucces(createUserResult)) {
			if (oldProfilePhotoId != null && !oldProfilePhotoId.equals(user.getProfilePhotoId())) {
				this.fileService.deleteFile(oldProfilePhotoId);
			}
			return createUserResult;
		} else {
			createUserResult.setResultDesc("User could not be updated. Error: " + createUserResult.getResultDesc());
			throw new OperationResultException(createUserResult);
		}
	}

}
