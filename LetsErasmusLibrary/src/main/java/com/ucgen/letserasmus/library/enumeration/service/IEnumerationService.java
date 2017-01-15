package com.ucgen.letserasmus.library.enumeration.service;

import java.util.ArrayList;
import java.util.Map;

import com.ucgen.letserasmus.library.enumeration.model.Enumeration;

public interface IEnumerationService {

	ArrayList<Enumeration> listEnumeration(Enumeration enumeration);
	
	Map<String, ArrayList<Enumeration>> listEnumeration();
	
}
