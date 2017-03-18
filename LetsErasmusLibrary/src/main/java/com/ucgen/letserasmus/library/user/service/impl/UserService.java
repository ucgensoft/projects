package com.ucgen.letserasmus.library.user.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ucgen.common.exception.operation.OperationResultException;
import com.ucgen.common.operationresult.EnmResultCode;
import com.ucgen.common.operationresult.ListOperationResult;
import com.ucgen.common.operationresult.OperationResult;
import com.ucgen.common.operationresult.ValueOperationResult;
import com.ucgen.letserasmus.library.file.model.FileModel;
import com.ucgen.letserasmus.library.file.service.IFileService;
import com.ucgen.letserasmus.library.place.enumeration.EnmPlaceStatus;
import com.ucgen.letserasmus.library.place.model.Place;
import com.ucgen.letserasmus.library.place.service.IPlaceService;
import com.ucgen.letserasmus.library.user.dao.IUserDao;
import com.ucgen.letserasmus.library.user.model.User;
import com.ucgen.letserasmus.library.user.service.IUserService;

@Service
public class UserService implements IUserService{

	private IUserDao userDao;
	private IFileService fileService;
	private IPlaceService placeService;

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
	
	@Autowired
	public void setPlaceService(IPlaceService placeService) {
		this.placeService = placeService;
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
	public ValueOperationResult<Integer> updateUser(User user, boolean setNull, Long deleteProfilePhotoId) throws OperationResultException {
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
		} else if (deleteProfilePhotoId != null) {
			OperationResult deleteFileResult = this.fileService.deleteFile(deleteProfilePhotoId);
			if (!OperationResult.isResultSucces(deleteFileResult)) {
				throw new OperationResultException(deleteFileResult);
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

	@Override
	public User getUserForLogin(User user) {
		return this.userDao.getUserForLogin(user);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public OperationResult deactivateUser(User user) throws OperationResultException {
		OperationResult operationResult = new OperationResult();
		String operationBy = user.getModifiedBy();
		Date operationDate = new Date();
		
		Place place = new Place();
		place.setHostUserId(user.getId());
		
		ListOperationResult<Place> placeListResult = this.placeService.listPlace(place, false, false, false, null, null);
		
		if (OperationResult.isResultSucces(placeListResult)) {
			for (Place userPlace : placeListResult.getObjectList()) {
				userPlace.setStatus(EnmPlaceStatus.DEACTIVE.getValue());
				userPlace.setModifiedBy(operationBy);
				userPlace.setModifiedDate(operationDate);
				OperationResult updatePlaceResult = this.placeService.updatePlace(userPlace, null, null, null);
				if (!OperationResult.isResultSucces(updatePlaceResult)) {
					throw new OperationResultException(updatePlaceResult);
				}
			}
			OperationResult updateUserResult = this.userDao.updateUser(user, false);
			if (!OperationResult.isResultSucces(updateUserResult)) {
				throw new OperationResultException(updateUserResult);
			} else {
				operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
				return operationResult;
			}
		} else {
			throw new OperationResultException(placeListResult);
		}
	}

}
