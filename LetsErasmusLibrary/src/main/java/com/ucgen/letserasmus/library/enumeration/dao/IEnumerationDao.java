package com.ucgen.letserasmus.library.enumeration.dao;

import java.util.ArrayList;
import java.util.Map;

import com.ucgen.letserasmus.library.enumeration.model.Enumeration;

public interface IEnumerationDao {

	ArrayList<Enumeration> listEnumeration(Enumeration enumeration);
	
	Map<String, ArrayList<Enumeration>> listEnumeration();
	
}
