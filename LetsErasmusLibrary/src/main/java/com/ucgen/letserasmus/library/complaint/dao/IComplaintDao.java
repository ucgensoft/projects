package com.ucgen.letserasmus.library.complaint.dao;

import java.util.List;

import com.ucgen.common.exception.operation.OperationResultException;
import com.ucgen.common.operationresult.OperationResult;
import com.ucgen.common.operationresult.ValueOperationResult;
import com.ucgen.letserasmus.library.common.model.BaseModel;
import com.ucgen.letserasmus.library.complaint.model.Complaint;

public interface IComplaintDao {

	OperationResult insertComplaint(Complaint complaint) throws OperationResultException;
	
	List<Complaint> listComplaint(Complaint complaint, BaseModel entity, boolean entityFlag, boolean userFlag, boolean hostUserFlag);
	
	ValueOperationResult<Integer> deleteComplaint(Long id);
	
}
