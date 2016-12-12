package com.ucgen.common.dao;

import java.util.HashMap;
import java.util.Map;

public class ForeignKey<K extends BaseRowMapper, M extends BaseRowMapper> {

	private Map<String, String> fieldPairMap;
	private K sourceMapper;
	private M destMapper;
	
	public Map<String, String> getFieldPairMap() {
		return fieldPairMap;
	}

	public void setFieldPairMap(Map<String, String> fieldPairMap) {
		this.fieldPairMap = fieldPairMap;
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

	public ForeignKey(K sourceMapper, M destMapper) {
		this.sourceMapper = sourceMapper;
		this.destMapper = destMapper;
	}
	
	public void addFieldPair(String sourceField, String destField) {
		if (this.fieldPairMap == null) {
			this.fieldPairMap = new HashMap<String, String>();
		}
		this.fieldPairMap.put(sourceField, destField);
	}
	
}
