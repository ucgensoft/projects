package com.ucgen.letserasmus.library.simpleobject.dao.impl;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import com.ucgen.letserasmus.library.simpleobject.dao.CountryRowMapper;
import com.ucgen.letserasmus.library.simpleobject.dao.ISimpleObjectDao;
import com.ucgen.letserasmus.library.simpleobject.model.Country;

@Repository
public class SimpleObjectDao extends JdbcDaoSupport implements ISimpleObjectDao {
	
	private static final String LIST_COUNTRY_SQL = "SELECT * FROM COUNTRY";
	
	@Autowired
	public SimpleObjectDao(DataSource dataSource) {
		super();
		super.setDataSource(dataSource);
	}
	
	@Override
	public List<Country> listCountry() {
		return this.getJdbcTemplate().query(LIST_COUNTRY_SQL, new CountryRowMapper());
	}

}
