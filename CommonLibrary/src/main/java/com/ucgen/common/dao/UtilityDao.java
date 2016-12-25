package com.ucgen.common.dao;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

@Repository
public class UtilityDao extends JdbcDaoSupport {

	@Autowired
	public UtilityDao(DataSource dataSource) {
		super();
		super.setDataSource(dataSource);
	}	
	
	public Long getLastIncrementId() {
		return this.getJdbcTemplate().queryForObject("SELECT LAST_INSERT_ID()", Long.class);
	}
	
}
