package com.ucgen.letserasmus.library.simpleobject.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ucgen.common.operationresult.OperationResult;
import com.ucgen.common.util.SecurityUtil;
import com.ucgen.letserasmus.library.common.enumeration.EnmErrorCode;
import com.ucgen.letserasmus.library.parameter.dao.IParameterDao;
import com.ucgen.letserasmus.library.parameter.enumeration.EnmParameter;
import com.ucgen.letserasmus.library.simpleobject.dao.ISimpleObjectDao;
import com.ucgen.letserasmus.library.simpleobject.model.Country;
import com.ucgen.letserasmus.library.simpleobject.model.Question;
import com.ucgen.letserasmus.library.simpleobject.model.QuestionGroup;
import com.ucgen.letserasmus.library.simpleobject.service.ISimpleObjectService;

@Service
public class SimpleObjectService implements ISimpleObjectService {

	private ISimpleObjectDao simpleObjectDao;
	private IParameterDao parameterDao;
	
	@Autowired
	public void setSimpleObjectDao(ISimpleObjectDao simpleObjectDao) {
		this.simpleObjectDao = simpleObjectDao;
	}

	@Autowired
	public void setParameterDao(IParameterDao parameterDao) {
		this.parameterDao = parameterDao;
	}

	@Override
	public List<Country> listCountry() {
		return this.simpleObjectDao.listCountry();
	}

	@Override
	public List<QuestionGroup> listQuestionGroup() {
		return this.simpleObjectDao.listQuestionGroup();
	}

	@Override
	public List<Question> listQuestion(String groupTitle, String searchText) {
		return this.simpleObjectDao.listQuestion(groupTitle, searchText);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public String generateTransactionId() {
		String transactionIdLength = this.parameterDao.getParameterValue(EnmParameter.TRANSACTION_ID_LENGTH.getId());
		String transactionId = SecurityUtil.generateAlphaNumericCode(Integer.valueOf(transactionIdLength));
		OperationResult operationResult = this.simpleObjectDao.insertTransactionId(transactionId);
		if (OperationResult.isResultSucces(operationResult)) {
			return transactionId;
		} else if (operationResult.getErrorCode() == EnmErrorCode.ALREADY_EXIST.getId()) {
			return this.generateTransactionId();
		} else {
			return null;
		}
	}
	
}
