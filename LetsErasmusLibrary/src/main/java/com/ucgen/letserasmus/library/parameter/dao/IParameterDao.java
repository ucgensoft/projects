package com.ucgen.letserasmus.library.parameter.dao;

import java.sql.SQLException;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;

public interface IParameterDao 
{

	public String getParameterValue(Integer parameterId) throws DataAccessException, CannotGetJdbcConnectionException, SQLException, Exception;

}