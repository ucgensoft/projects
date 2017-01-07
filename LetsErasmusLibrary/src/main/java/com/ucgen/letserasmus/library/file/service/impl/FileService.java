package com.ucgen.letserasmus.library.file.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ucgen.common.operationresult.ListOperationResult;
import com.ucgen.common.operationresult.OperationResult;
import com.ucgen.common.operationresult.ValueOperationResult;
import com.ucgen.letserasmus.library.file.dao.IFileDao;
import com.ucgen.letserasmus.library.file.model.File;
import com.ucgen.letserasmus.library.file.service.IFileService;

@Service
public class FileService implements IFileService{

	private IFileDao fileDao;
	
	public IFileDao getFileDao() {
		return fileDao;
	}

	@Autowired
	public void setiFileDao(IFileDao fileDao) {
		this.fileDao = fileDao;
	}

	@Override
	public ListOperationResult<File> listFile(File file) {
		return this.fileDao.listFile(file);
	}

	@Override
	public OperationResult insertFile(File file) {
		return this.fileDao.insertFile(file);
	} 

	@Override
	public ValueOperationResult<Integer> updateFile(File file) {
		return this.fileDao.updateFile(file);
	}

}
