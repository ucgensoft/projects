package com.ucgen.letserasmus.library.file.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ucgen.common.operationresult.ListOperationResult;
import com.ucgen.common.operationresult.ValueOperationResult;
import com.ucgen.letserasmus.library.file.dao.IFileDao;
import com.ucgen.letserasmus.library.file.model.File;
import com.ucgen.letserasmus.library.file.service.IFileService;

@Service
public class FileService implements IFileService{

	private IFileDao iFileDao;
	
	public IFileDao getiFileDao() {
		return iFileDao;
	}

	@Autowired
	public void setiFileDao(IFileDao iFileDao) {
		this.iFileDao = iFileDao;
	}

	@Override
	public ListOperationResult<File> getFile(Long id) {
		return this.getiFileDao().getFile(id);
	}

	@Override
	public ValueOperationResult<Integer> insertFile(File file) {
		return this.getiFileDao().insertFile(file);
	}

	@Override
	public ValueOperationResult<Integer> updateFile(File file) {
		return this.getiFileDao().updateFile(file);
	}

}
