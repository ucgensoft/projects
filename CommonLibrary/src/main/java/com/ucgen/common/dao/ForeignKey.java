package com.ucgen.common.dao;

import java.util.HashMap;
import java.util.Map;

public class ForeignKey<K extends BaseRowMapper, M extends BaseRowMapper> {

	private Map<String, String> fieldPairMap;
	private Map<String, Object> staticCriteriaMap;
	private K sourceMapper;
	private M destMapper;
	private EnmJoinType joinType;
	
	public Map<String, String> getFieldPairMap() {
		return fieldPairMap;
	}

	public void setFieldPairMap(Map<String, String> fieldPairMap) {
		this.fieldPairMap = fieldPairMap;
	}

	public Map<String, Object> getStaticCriteriaMap() {
		return staticCriteriaMap;
	}

	public void setStaticCriteriaMap(Map<String, Object> staticCriteriaMap) {
		this.staticCriteriaMap = staticCriteriaMap;
	}

	public K getSourceMapper() {
		return sourceMapper;
	}

	public void setSourceMapper(K sourceMapper) {
		this.sourceMapper = sourceMapper;
	}

	public M getDestMapper() {
		return destMapper;
	}

	public void setDestMapper(M destMapper) {
		this.destMapper = destMapper;
	}

	public EnmJoinType getJoinType() {
		return joinType;
	}

	public void setJoinType(EnmJoinType joinType) {
		this.joinType = joinType;
	}

	public ForeignKey(K sourceMapper, M destMapper, EnmJoinType joinType) {
		this.sourceMapper = sourceMapper;
		this.destMapper = destMapper;
		this.joinType = joinType;
	}
	
	public void addFieldPair(String sourceField, String destField) {
		if (this.fieldPairMap == null) {
			this.fieldPairMap = new HashMap<String, String>();
		}
		this.fieldPairMap.put(sourceField, destField);
	}
	
	public void addStaticValueCriteria(String sourceField, Object value) {
		if (this.staticCriteriaMap == null) {
			this.staticCriteriaMap = new HashMap<String, Object>();
		}
		this.staticCriteriaMap.put(sourceField, value);
	}
	
}
