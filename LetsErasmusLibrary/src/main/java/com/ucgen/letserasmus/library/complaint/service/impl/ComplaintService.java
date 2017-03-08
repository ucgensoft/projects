package com.ucgen.letserasmus.library.complaint.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ucgen.common.exception.operation.OperationResultException;
import com.ucgen.common.operationresult.OperationResult;
import com.ucgen.common.operationresult.ValueOperationResult;
import com.ucgen.letserasmus.library.common.model.BaseModel;
import com.ucgen.letserasmus.library.complaint.dao.IComplaintDao;
import com.ucgen.letserasmus.library.complaint.model.Complaint;
import com.ucgen.letserasmus.library.complaint.service.IComplaintService;

@Service
public class ComplaintService implements IComplaintService {

	private IComplaintDao complaintDao;
	
	@Autowired
	public void setComplaintDao(IComplaintDao complaintDao) {
		this.complaintDao = complaintDao;
	}

	@Override
	public OperationResult insertComplaint(Complaint complaint) throws OperationResultException {
		return this.complaintDao.insertComplaint(complaint);
	}

	@Override
	public List<Complaint> listComplaint(Complaint complaint, BaseModel entity, boolean entityFlag, boolean userFlag,
			boolean hostUserFlag) {
		return this.complaintDao.listComplaint(complaint, entity, entityFlag, userFlag, hostUserFlag);
	}

	@Override
	public ValueOperationResult<Integer> deleteComplaint(Long id) {
		return this.complaintDao.deleteComplaint(id);
	}
	
}