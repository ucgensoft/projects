package com.ucgen.letserasmus.library.enumeration.service.impl;

import java.util.ArrayList;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ucgen.letserasmus.library.enumeration.dao.IEnumerationDao;
import com.ucgen.letserasmus.library.enumeration.model.Enumeration;
import com.ucgen.letserasmus.library.enumeration.service.IEnumerationService;

@Service
public class EnumerationService implements IEnumerationService {

	private IEnumerationDao enumerationDao;
	
	@Autowired
	public void setEnumerationDao(IEnumerationDao enumerationDao) {
		this.enumerationDao = enumerationDao;
	}

	@Override
	public ArrayList<Enumeration> listEnumeration(Enumeration enumeration) {
		return this.enumerationDao.listEnumeration(enumeration);
	}

	@Override
	public Map<String, ArrayList<Enumeration>> listEnumeration() {
		return this.enumerationDao.listEnumeration();
	}
	
}
