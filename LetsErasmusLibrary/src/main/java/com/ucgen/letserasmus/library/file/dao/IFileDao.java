package com.ucgen.letserasmus.library.file.dao;

import com.ucgen.common.operationresult.ListOperationResult;
import com.ucgen.common.operationresult.ValueOperationResult;
import com.ucgen.letserasmus.library.file.model.File;


public interface IFileDao {
	ListOperationResult<File> getFile(Long id);
	ValueOperationResult<Integer> insertFile(File file);
	ValueOperationResult<Integer> updateFile(File file);
}
