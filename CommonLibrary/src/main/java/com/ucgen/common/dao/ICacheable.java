package com.ucgen.common.dao;


public interface ICacheable {

	boolean isCacheExpired();
	
	void refreshCache();
	
}
