package com.ucgen.letserasmus.library.file.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ucgen.common.operationresult.ListOperationResult;
import com.ucgen.common.operationresult.OperationResult;
import com.ucgen.common.operationresult.ValueOperationResult;
import com.ucgen.letserasmus.library.file.dao.IFileDao;
import com.ucgen.letserasmus.library.file.model.FileModel;
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
	public ListOperationResult<FileModel> listFile(FileModel file) {
		return this.fileDao.listFile(file);
	}

	@Override
	public OperationResult insertFile(FileModel file) {
		return this.fileDao.insertFile(file);
	} 

	@Override
	public ValueOperationResult<Integer> updateFile(FileModel file) {
		return this.fileDao.updateFile(file);
	}

	@Override
	public OperationResult deleteFile(Long id) {
		return this.fileDao.deleteFile(id);
	}

}
