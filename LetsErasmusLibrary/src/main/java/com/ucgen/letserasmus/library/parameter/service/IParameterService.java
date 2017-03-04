package com.ucgen.letserasmus.library.parameter.service;

import java.sql.SQLException;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;

public interface IParameterService 
{	
	
	public String getParameterValue(Integer parameterId)  throws DataAccessException, CannotGetJdbcConnectionException, SQLException, Exception;
	
}
