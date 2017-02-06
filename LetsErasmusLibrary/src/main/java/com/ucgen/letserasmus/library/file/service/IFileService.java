package com.ucgen.letserasmus.library.file.service;

import com.ucgen.common.operationresult.ListOperationResult;
import com.ucgen.common.operationresult.OperationResult;
import com.ucgen.common.operationresult.ValueOperationResult;
import com.ucgen.letserasmus.library.file.model.FileModel;

public interface IFileService {

	ListOperationResult<FileModel> listFile(FileModel file);
	
	OperationResult insertFile(FileModel file);
	
	ValueOperationResult<Integer> updateFile(FileModel file);
	
	OperationResult deleteFile(Long id);
	
}
