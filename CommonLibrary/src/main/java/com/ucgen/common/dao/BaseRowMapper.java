package com.ucgen.common.dao;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
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

	protected String tableName;
	protected String shortTableName;
	private Set<String> selectedColSet;
	private List<String> colList;
	private Map<String, String> sortFieldColMap;
	
	private Map<String, ForeignKey> fKeyMap;
	
	public Map<String, ForeignKey> getfKeyMap() {
		return fKeyMap;
	}

	public void setfKeyMap(Map<String, ForeignKey> fKeyMap) {
		this.fKeyMap = fKeyMap;
	}

	public void addFKey(String keyName, ForeignKey fKey) {
		if (this.fKeyMap == null) {
			this.fKeyMap = new HashMap<String, ForeignKey>();
		}
		this.fKeyMap.put(keyName, fKey);
	}
	
	public String getShortTableName() {
		return shortTableName;
	}

	public void setShortTableName(String shortTableName) {
		this.shortTableName = shortTableName;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public List<String> getColList() {
		return colList;
	}

	public void setColList(List<String> colList) {
		this.colList = colList;
	}
	
	public void addColumn(String columnName) {
		if (this.colList == null) {
			this.colList = new ArrayList<String>();
		}
		this.colList.add(columnName);
	}

	protected BaseRowMapper() {
		this(null, null);
	}
	
	protected BaseRowMapper(String tableName) {
		this(tableName, null);
	}
	
	protected BaseRowMapper(String tableName, String shortTableName) {
		this.tableName = tableName;
		this.shortTableName = shortTableName;
		fillFieldMaps();
		initializeColList();
	}
	
	public abstract void fillFieldMaps();
	public abstract void initializeColList();
	
	public String getSelectSql() {
		StringBuilder sqlBuilder = new StringBuilder();
		
		sqlBuilder.append("SELECT ");
		for (int i = 0; i < this.colList.size(); i++) {
			sqlBuilder.append((i == 0 ? "" : ",") + colList.get(i));
		}
		sqlBuilder.append(" FROM ");
		sqlBuilder.append(this.tableName);
		sqlBuilder.append(" WHERE 1=1 ");
		return sqlBuilder.toString();
	}
	
	public String getSelectSqlWithForeignKeys() {
		StringBuilder selectBuilder = new StringBuilder("SELECT ");
		StringBuilder fromBuilder = new StringBuilder(" FROM " + this.getTableName() + " " + this.getShortTableName());
		StringBuilder whereBuilder = new StringBuilder(" WHERE 1=1 ");
		
		for (int i = 0; i < this.colList.size(); i++) {
			selectBuilder.append((i == 0 ? "" : ",") +  this.shortTableName + "." + colList.get(i) + " " + this.getColumnName(colList.get(i)));
		}
		
		for (ForeignKey<BaseRowMapper, BaseRowMapper> fKey : this.getfKeyMap().values()) {
			List<String> fKeyColList = fKey.getDestMapper().getColList(); 
			for (int i = 0; i < fKeyColList.size(); i++) {
				selectBuilder.append("," +  fKey.getDestMapper().shortTableName + "." + fKeyColList.get(i) + " "  + fKey.getDestMapper().getColumnName(fKeyColList.get(i)));
			}
			for (String fieldPairKey : fKey.getFieldPairMap().keySet()) {
				whereBuilder.append(" AND ");
				whereBuilder.append(this.shortTableName + "." + fieldPairKey);
				whereBuilder.append("=");
				whereBuilder.append(fKey.getDestMapper().getShortTableName() + "." + fKey.getFieldPairMap().get(fieldPairKey));
			}
			fromBuilder.append("," + fKey.getDestMapper().getTableName() + " " + fKey.getDestMapper().getShortTableName());
		}
		
		return selectBuilder.toString() + fromBuilder.toString() + whereBuilder.toString();
	}
	
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
		if (selectedColSet == null) {
			selectedColSet = new HashSet<String>();
			ResultSetMetaData metaData = rs.getMetaData();
			for (int i = 1; i <= metaData.getColumnCount(); i++) {
				selectedColSet.add(metaData.getColumnLabel(i).toUpperCase());
			}
		}
	}
	
	public boolean colExist(String colName) {
		if (this.selectedColSet != null) {
			return selectedColSet.contains(colName.toUpperCase());
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
		String columnName = this.getColumnName(colName);
		if (this.colExist(columnName)) {
			return rs.getString(columnName);
		} else {
			return null;
		}
	}
	
	public BigDecimal getBigDecimal(ResultSet rs, String colName) throws SQLException {
		String columnName = this.getColumnName(colName);
		if (this.colExist(columnName)) {
			return rs.getBigDecimal(columnName);
		} else {
			return null;
		}
	}
	
	public Integer getInteger(ResultSet rs, String colName) throws SQLException {
		String columnName = this.getColumnName(colName);
		if (this.colExist(columnName)) {
			Object value = rs.getObject(columnName);
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
		String columnName = this.getColumnName(colName);
		if (this.colExist(columnName)) {
			Object value = rs.getObject(columnName);
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
		String columnName = this.getColumnName(colName);
		if (this.colExist(columnName)) {
			return rs.getTimestamp(columnName);
		} else {
			return null;
		}
	}
	
	public Long getLong(ResultSet rs, String colName) throws SQLException {
		String columnName = this.getColumnName(colName);
		if (this.colExist(columnName)) {
			Object value = rs.getObject(columnName);
			if (value != null) {
				if (value instanceof Long) {
					return (Long) value;
				} else if (value instanceof Integer) {
					return ((Integer) value).longValue();
				} else if (value instanceof BigInteger) {
					return ((BigInteger) value).longValue();
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
						throw new UnsupportedException("BaseRowMapper-createSelectFilter(), filtre deðeri OrderReconc objesinde bulunamadi. filterField:" + filterEntry.getKey());					
					}
				} else {
					throw new UnsupportedException("BaseRowMapper-createSelectFilter(), dbFilterCol deðeri null. filterField:" + filterEntry.getKey());
				}
			}
		}
	}
	
	public String getColumnName(String colName) {
		return (this.shortTableName == null ? "" : this.shortTableName + "_") + colName;
	}

}
