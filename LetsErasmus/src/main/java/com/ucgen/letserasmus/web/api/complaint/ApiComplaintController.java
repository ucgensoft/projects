package com.ucgen.letserasmus.web.api.complaint;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ucgen.common.operationresult.EnmResultCode;
import com.ucgen.common.operationresult.OperationResult;
import com.ucgen.common.operationresult.ValueOperationResult;
import com.ucgen.common.util.CommonUtil;
import com.ucgen.common.util.FileLogger;
import com.ucgen.letserasmus.library.common.enumeration.EnmEntityType;
import com.ucgen.letserasmus.library.common.enumeration.EnmErrorCode;
import com.ucgen.letserasmus.library.complaint.enumeration.EnmComplaintStatus;
import com.ucgen.letserasmus.library.complaint.model.Complaint;
import com.ucgen.letserasmus.library.complaint.service.IComplaintService;
import com.ucgen.letserasmus.library.place.model.Place;
import com.ucgen.letserasmus.library.place.service.IPlaceService;
import com.ucgen.letserasmus.library.user.model.User;
import com.ucgen.letserasmus.web.api.BaseApiController;
import com.ucgen.letserasmus.web.view.application.AppConstants;

@RestController
public class ApiComplaintController extends BaseApiController {

	private IComplaintService complaintService;
	private IPlaceService placeService;
	
	@Autowired
	public void setComplaintService(IComplaintService complaintService) {
		this.complaintService = complaintService;
	}

	@Autowired
	public void setPlaceService(IPlaceService placeService) {
		this.placeService = placeService;
	}

	@RequestMapping(value = "/api/complaint/list", method = RequestMethod.GET)
    public ResponseEntity<ValueOperationResult<Map<Integer, Map<Long, Complaint>>>> listComplaint(@RequestParam("entityType") Integer entityType, 
    		HttpSession session) {
		ValueOperationResult<Map<Integer, Map<Long, Complaint>>> operationResult = new ValueOperationResult<Map<Integer, Map<Long, Complaint>>>();
		
		try {
			User sessionUser = this.getSessionUser(session);
			if (sessionUser != null) {
				
				Complaint complaint = new Complaint();
				complaint.setUserId(sessionUser.getId());
				complaint.setEntityType(entityType);
				complaint.setStatus(EnmComplaintStatus.OPEN.getId());
				
				List<Complaint> complaintList = this.complaintService.listComplaint(complaint, null, false, false, false);
				
				sessionUser.createComplaintMap();
				sessionUser.addComplaintList(complaintList);
				
				operationResult.setResultValue(sessionUser.getComplaintMap());
				operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
			} else {
				operationResult.setErrorCode(EnmErrorCode.USER_NOT_LOGGED_IN.getId());
				operationResult.setResultCode(EnmResultCode.ERROR.getValue());
				operationResult.setResultDesc(AppConstants.USER_NOT_LOGGED_IN);
			}
		} catch (Exception e) {
			operationResult.setResultCode(EnmResultCode.EXCEPTION.getValue());
			operationResult.setResultDesc(AppConstants.LIST_OPERATION_FAIL);
			FileLogger.log(Level.ERROR, "ApiComplaintController-listComplaint()-Error: " + CommonUtil.getExceptionMessage(e));
		}
		return new ResponseEntity<ValueOperationResult<Map<Integer, Map<Long, Complaint>>>>(operationResult, HttpStatus.OK);
    }
	
	@RequestMapping(value = "/api/complaint/create", method = RequestMethod.POST)
    public ResponseEntity<ValueOperationResult<Map<Integer, Map<Long, Complaint>>>> createComplaint(@RequestBody Complaint complaint, HttpSession session) {
		ValueOperationResult<Map<Integer, Map<Long, Complaint>>> operationResult = new ValueOperationResult<Map<Integer, Map<Long, Complaint>>>();
		
		try {
			User user = super.getSessionUser(session);
			if (user != null) {
				EnmEntityType entityType = EnmEntityType.getEntityType(complaint.getEntityType());
				if (complaint.getEntityType() != null && entityType != null 
						&& complaint.getEntityId() != null && complaint.getDescription() != null 
						&& (entityType.equals(EnmEntityType.PLACE) || entityType.equals(EnmEntityType.USER)
								|| entityType.equals(EnmEntityType.MESSAGE))) {
					
					Complaint userComplaint = new Complaint();
					userComplaint.setUserId(user.getId());
					
					List<Complaint> userComplaintList = this.complaintService.listComplaint(complaint, null, false, false, false);
					
					boolean itemAlreadyExist = false;
					for (Complaint tmpComplaint : userComplaintList) {
						if (tmpComplaint.getEntityType().equals(complaint.getEntityType()) 
								&& tmpComplaint.getEntityId().equals(complaint.getEntityId())) {
							itemAlreadyExist = true;
						}
					}
					
					if (!itemAlreadyExist) {
						Long hostUserId = null;
						
						if (complaint.getEntityType().equals(EnmEntityType.PLACE.getId())) {
							ValueOperationResult<Place> getOperationResult = this.placeService.getPlace(complaint.getEntityId());
							hostUserId = getOperationResult.getResultValue().getHostUserId();
							if (hostUserId.equals(user.getId())) {
								operationResult.setResultCode(EnmResultCode.WARNING.getValue());
								operationResult.setResultDesc("I admire your honesty. This is your own place :)");
							}
						} else if (complaint.getEntityType().equals(EnmEntityType.USER.getId())) {
							if(complaint.getEntityId().equals(user.getId())) {
								operationResult.setResultCode(EnmResultCode.WARNING.getValue());
								operationResult.setResultDesc("I admire your honesty. You can not report yourself :)");
							}
						}
						if (operationResult == null || operationResult.getResultCode() == null) {
							userComplaint.setHostUserId(hostUserId);
							userComplaint.setDescription(complaint.getDescription());
							userComplaint.setEntityType(complaint.getEntityType());
							userComplaint.setEntityId(complaint.getEntityId());
							userComplaint.setStatus(EnmComplaintStatus.OPEN.getId());
							
							userComplaint.setCreatedBy(user.getFullName());
							userComplaint.setCreatedDate(new Date());
							
							OperationResult createResult = this.complaintService.insertComplaint(userComplaint);
							if (OperationResult.isResultSucces(createResult)) {
								user.addComplaint(userComplaint);
								
								operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
								operationResult.setResultValue(user.getComplaintMap());
								
							} else {
								operationResult.setResultCode(EnmResultCode.ERROR.getValue());
								operationResult.setResultDesc(AppConstants.CREATE_OPERATION_FAIL);
							}
						}
					} else {
						operationResult.setResultCode(EnmResultCode.WARNING.getValue());
						operationResult.setResultDesc(AppConstants.COMPLAINT_DOUBLE);
					}
					
				} else {
					operationResult.setResultCode(EnmResultCode.ERROR.getValue());
					operationResult.setResultDesc(AppConstants.MISSING_MANDATORY_PARAM);
				}
			} else {
				operationResult.setErrorCode(EnmErrorCode.USER_NOT_LOGGED_IN.getId());
				operationResult.setResultCode(EnmResultCode.ERROR.getValue());
				operationResult.setResultDesc(AppConstants.USER_NOT_LOGGED_IN);
			}
		} catch (Exception e) {
			operationResult.setResultCode(EnmResultCode.EXCEPTION.getValue());
			operationResult.setResultDesc(AppConstants.CREATE_OPERATION_FAIL);
			FileLogger.log(Level.ERROR, "ApiComplaintController-createComplaint()-Error: " + CommonUtil.getExceptionMessage(e));
		}
		return new ResponseEntity<ValueOperationResult<Map<Integer, Map<Long, Complaint>>>>(operationResult, HttpStatus.OK);
    }
	
	@RequestMapping(value = "/api/complaint/delete", method = RequestMethod.POST)
    public ResponseEntity<ValueOperationResult<Map<Integer, Map<Long, Complaint>>>> deleteComplaint(@RequestBody Complaint complaint, HttpSession session) {
		ValueOperationResult<Map<Integer, Map<Long, Complaint>>> operationResult = new ValueOperationResult<Map<Integer, Map<Long, Complaint>>>();
		
		try {
			User user = super.getSessionUser(session);
			if (user != null) {
				if (complaint.getEntityId() != null && complaint.getEntityId() != null) {
					
					Complaint userComplaint = new Complaint();
					userComplaint.setUserId(user.getId());
					userComplaint.setEntityType(complaint.getEntityType());
					userComplaint.setEntityId(complaint.getEntityId());
					userComplaint.setStatus(EnmComplaintStatus.OPEN.getId());
					
					List<Complaint> complaintList = this.complaintService.listComplaint(complaint, null, false, false, false);
					
					if (complaintList.size() > 0) {												
						ValueOperationResult<Integer> deleteResult = this.complaintService.deleteComplaint(complaintList.get(0).getId());
						if (OperationResult.isResultSucces(deleteResult)) {
							user.getComplaintMap().get(complaint.getEntityType()).remove(complaint.getEntityId());
							operationResult.setResultValue(user.getComplaintMap());
							operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
						} else {
							operationResult.setResultCode(EnmResultCode.ERROR.getValue());
							operationResult.setResultDesc(AppConstants.OPERATION_FAIL);
						}
					} else {
						operationResult.setResultCode(EnmResultCode.WARNING.getValue());
						operationResult.setResultDesc(AppConstants.COMPLAINT_NOT_FOUND);
					}
				} else {
					operationResult.setResultCode(EnmResultCode.ERROR.getValue());
					operationResult.setResultDesc(AppConstants.MISSING_MANDATORY_PARAM);
				}
			} else {
				operationResult.setErrorCode(EnmErrorCode.USER_NOT_LOGGED_IN.getId());
				operationResult.setResultCode(EnmResultCode.ERROR.getValue());
				operationResult.setResultDesc(AppConstants.USER_NOT_LOGGED_IN);
			}
		} catch (Exception e) {
			operationResult.setResultCode(EnmResultCode.EXCEPTION.getValue());
			operationResult.setResultDesc(AppConstants.CREATE_OPERATION_FAIL);
			FileLogger.log(Level.ERROR, "ApiComplaintController-deleteComplaint()-Error: " + CommonUtil.getExceptionMessage(e));
		}
		return new ResponseEntity<ValueOperationResult<Map<Integer, Map<Long, Complaint>>>>(operationResult, HttpStatus.OK);
    }
	
}
