package com.ucgen.common.dao;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.jdbc.core.RowMapper;

import com.ucgen.common.exception.operation.UnsupportedException;

public abstract class BaseRowMapper<T> implements RowMapper<T>{

	private Set<String> colSet;
	
	private Map<String, String> sortFieldColMap;
	
	public BaseRowMapper() {
		fillFieldMaps();
	}
	
	public abstract void fillFieldMaps();
	
	public void addSortField(String fieldName, String dbColName) {
		if (sortFieldColMap == null) {
			sortFieldColMap = new HashMap<String, String>();
		}
		sortFieldColMap.put(fieldName, dbColName);
	}
	
	public String getSortColumn(String fieldName) {
		if (fieldName != null) {
			return sortFieldColMap.get(fieldName.toUpperCase());
		} else {
			return null;
		}
	}
	
	public String getFilterFieldColumn(String tblPrefix, String fieldName) {
		if (fieldName != null) {
			if (tblPrefix != null) {
				return tblPrefix + "."  + sortFieldColMap.get(fieldName.toUpperCase());
			} else {
				return sortFieldColMap.get(fieldName.toUpperCase());
			}
		} else {
			return null;
		}
	}
	
	public void initializeColSet(ResultSet rs) throws SQLException {
		if (colSet == null) {
			colSet = new HashSet<String>();
			ResultSetMetaData metaData = rs.getMetaData();
			for (int i = 1; i <= metaData.getColumnCount(); i++) {
				colSet.add(metaData.getColumnName(i).toUpperCase());
			}
		}
	}
	
	public boolean colExist(String colName) {
		if (this.colSet != null) {
			return colSet.contains(colName.toUpperCase());
		} else {
			return true;
		}
	}
	
	@SuppressWarnings("rawtypes")
	public static Field getField(Class cls, String fieldName) {
		try {
			if (fieldName.contains(".")) {
				String[] fieldArr = fieldName.split("\\.");
				Field field = cls.getDeclaredField(fieldArr[0]);
				cls = field.getType();
				fieldName = fieldArr[1];
			}
			return cls.getDeclaredField(fieldName);
		} catch (Exception e) {
			return null;
		}
	}
	
	public String getString(ResultSet rs, String colName) throws SQLException {
		if (this.colExist(colName)) {
			return rs.getString(colName);
		} else {
			return null;
		}
	}
	
	public BigDecimal getBigDecimal(ResultSet rs, String colName) throws SQLException {
		if (this.colExist(colName)) {
			return rs.getBigDecimal(colName);
		} else {
			return null;
		}
	}
	
	public Integer getInteger(ResultSet rs, String colName) throws SQLException {
		if (this.colExist(colName)) {
			Object value = rs.getObject(colName);
			if (value != null) {
				if (value instanceof Integer) {
					return (Integer) value;
				} else {
					return Integer.valueOf(value.toString());
				}
			} else {
				return null;
			}
		} else {
			return null;
		}
	}
	
	public Float getFloat(ResultSet rs, String colName) throws SQLException {
		if (this.colExist(colName)) {
			Object value = rs.getObject(colName);
			if (value != null) {
				if (value instanceof Float) {
					return (Float) value;
				} else if (value instanceof BigDecimal) {
					return ((BigDecimal) value).floatValue();
				}
			}
		}
		return null;
	}
	
	public Date getTimestamp(ResultSet rs, String colName) throws SQLException {
		if (this.colExist(colName)) {
			return rs.getTimestamp(colName);
		} else {
			return null;
		}
	}
	
	public Long getLong(ResultSet rs, String colName) throws SQLException {
		if (this.colExist(colName)) {
			Object value = rs.getObject(colName);
			if (value != null) {
				if (value instanceof Long) {
					return (Long) value;
				} else if (value instanceof Integer) {
					return ((Integer) value).longValue();
				} else if (value instanceof BigDecimal) {
					return ((BigDecimal) value).longValue();
				} else if (value instanceof String) {
					return Long.valueOf(value.toString());
				} else {
					return null;
				}
			} else {
				return null;
			}
		} else {
			return null;
		}
	}
	
	public void createSelectFilter(Class cls, String tblPrefix, Map<String, Object> filterMap, List<String> criteriaList, List<Object> argList) throws UnsupportedException {
		if (filterMap != null && filterMap.size() > 0) {
			Iterator<Entry<String, Object>> filterIterator = filterMap.entrySet().iterator();
			while (filterIterator.hasNext()) {
				Entry<String, Object> filterEntry = filterIterator.next();
				String dbFilterCol = this.getFilterFieldColumn(tblPrefix, filterEntry.getKey());
				if (dbFilterCol != null) {
					Field field = BaseRowMapper.getField(cls, filterEntry.getKey());
					if (field != null) {
						if (field.getType().equals(String.class)) {
							criteriaList.add("UPPER(" + dbFilterCol + ") LIKE UPPER (?)");
							argList.add("%" + filterEntry.getValue() + "%");
						} else if (field.getType().equals(Date.class)) {
							criteriaList.add("TRUNC(" + dbFilterCol + ") = ?");
							argList.add(filterEntry.getValue());
						} else {
							criteriaList.add(dbFilterCol + " = ?");
							argList.add(filterEntry.getValue());
						}
					} else {
						throw new UnsupportedException("BaseRowMapper-createSelectFilter(), filtre değeri OrderReconc objesinde bulunamadi. filterField:" + filterEntry.getKey());					
					}
				} else {
					throw new UnsupportedException("BaseRowMapper-createSelectFilter(), dbFilterCol değeri null. filterField:" + filterEntry.getKey());
				}
			}
		}
	}

}
