package com.ucgen.letserasmus.library.file.dao;

import com.ucgen.common.operationresult.ListOperationResult;
import com.ucgen.common.operationresult.OperationResult;
import com.ucgen.common.operationresult.ValueOperationResult;
import com.ucgen.letserasmus.library.file.model.File;


public interface IFileDao {
	
	ListOperationResult<File> listFile(File file);
	
	OperationResult insertFile(File file);
	
	ValueOperationResult<Integer> updateFile(File file);
	
}
