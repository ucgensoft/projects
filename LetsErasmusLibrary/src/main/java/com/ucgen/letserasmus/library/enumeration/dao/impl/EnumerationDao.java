package com.ucgen.letserasmus.library.enumeration.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import com.ucgen.letserasmus.library.enumeration.dao.EnumerationRowMapper;
import com.ucgen.letserasmus.library.enumeration.dao.IEnumerationDao;
import com.ucgen.letserasmus.library.enumeration.model.Enumeration;

@Repository
public class EnumerationDao extends JdbcDaoSupport implements IEnumerationDao {

	private static final String LIST_ENUMERATION_SQL = "SELECT ENUM_TYPE, ENUM_KEY, ENUM_VALUE, ENUM_ORDER, DESCRIPTION, UI_LABEL, ICON_STYLE FROM ENUMERATION ORDER BY ENUM_TYPE, ENUM_ORDER";
	
	private Map<String, ArrayList<Enumeration>> enumerationMap;
	
	@Autowired
	public EnumerationDao(DataSource dataSource) {
		super();
		super.setDataSource(dataSource);
		enumerationMap = new HashMap<String, ArrayList<Enumeration>>();
	}
	
	private void refreshEnumerationCache() {
		List<Enumeration> enumerationList = this.getJdbcTemplate().query(LIST_ENUMERATION_SQL, new EnumerationRowMapper());
		for (Enumeration enumeration : enumerationList) {
			if (!this.enumerationMap.containsKey(enumeration.getEnumType())) {
				this.enumerationMap.put(enumeration.getEnumType(), new ArrayList<Enumeration>());
			}
			this.enumerationMap.get(enumeration.getEnumType()).add(enumeration);
		}
	}

	@Override
	public ArrayList<Enumeration> listEnumeration(Enumeration enumeration) {
		synchronized (this.enumerationMap) {
			if (this.enumerationMap == null || this.enumerationMap.size() == 0) {
				this.refreshEnumerationCache();
			}
		}		

		return this.enumerationMap.get(enumeration.getEnumType());
	}
	
	@Override
	public Map<String, ArrayList<Enumeration>> listEnumeration() {
		synchronized (this.enumerationMap) {
			if (this.enumerationMap == null || this.enumerationMap.size() == 0) {
				this.refreshEnumerationCache();
			}
		}
		return this.enumerationMap;
	}
	
}
