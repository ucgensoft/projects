package com.ucgen.letserasmus.library.parameter.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ucgen.letserasmus.library.parameter.dao.IParameterDao;
import com.ucgen.letserasmus.library.parameter.service.IParameterService;

@Service
public class ParameterService implements IParameterService {

	private IParameterDao parameterDao;

	@Autowired
	public void setGeneralParametersDao(IParameterDao generalParametersDao) {
		this.parameterDao = generalParametersDao;
	}
	
	public String getParameterValue(Integer parameterId) throws Exception {
		return parameterDao.getParameterValue(parameterId);		
	}
	
}