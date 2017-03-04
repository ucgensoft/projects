package com.ucgen.letserasmus.library.parameter.dao.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import com.ucgen.letserasmus.library.parameter.dao.IParameterDao;
import com.ucgen.letserasmus.library.parameter.dao.ParameterRowMapper;
import com.ucgen.letserasmus.library.parameter.model.Parameter;

@Repository
public class ParameterDao extends JdbcDaoSupport implements IParameterDao {
	
	private static int CACHE_REFRESH_PERIOD = 100 * 60 * 60;
	
	private static final String GET_ALL_PARAMETERS_SQL = "SELECT ID, NAME, VALUE FROM PARAMETER";
	
	private static Map<Integer, String> parametersMap = new HashMap<Integer, String>();
	
	protected Date lastRefreshDate = null;
	
	@Autowired
	public ParameterDao(DataSource dataSource) {
		super();
		super.setDataSource(dataSource);
	}
	
	public Date getLastRefreshDate() {
		return lastRefreshDate;
	}

	public void setLastRefreshDate(Date lastRefreshDate) {
		this.lastRefreshDate = lastRefreshDate;
	}
	
	public String getParameterValue(Integer parameterId) {
		synchronized (parametersMap) {
			if (parametersMap == null || this.isCacheExpired()) {
				this.refreshCache();
			} 
			if(parametersMap.containsKey(parameterId)) {
				return parametersMap.get(parameterId);
			} else {
				return null;
			}
		}
	}
	
	public List<Parameter> getAllParameters() {
		return getJdbcTemplate().query(GET_ALL_PARAMETERS_SQL, new ParameterRowMapper());
	}
	
	public boolean isCacheExpired() {
		Date now = new Date();
		if (this.lastRefreshDate == null 
				|| (now.getTime() - this.lastRefreshDate.getTime() > ParameterDao.CACHE_REFRESH_PERIOD)) {
			return true;
		} else {
			return false;
		}
	}
	
	public void refreshCache() {
		synchronized (parametersMap) {
			List<Parameter> parameterList = getAllParameters();
			if (parameterList != null && parameterList.size() > 0) {
				parametersMap = new HashMap<Integer, String>();
				
				for (Parameter parameter : parameterList) {
					parametersMap.put(parameter.getId(), parameter.getValue());
				}
				this.lastRefreshDate = new Date();
			}
		}
	}
	
}