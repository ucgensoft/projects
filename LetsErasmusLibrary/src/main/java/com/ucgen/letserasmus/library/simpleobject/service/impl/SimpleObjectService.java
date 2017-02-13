package com.ucgen.letserasmus.library.simpleobject.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ucgen.letserasmus.library.simpleobject.dao.ISimpleObjectDao;
import com.ucgen.letserasmus.library.simpleobject.model.Country;
import com.ucgen.letserasmus.library.simpleobject.service.ISimpleObjectService;

@Service
public class SimpleObjectService implements ISimpleObjectService {

	private ISimpleObjectDao simpleObjectDao;
	
	@Autowired
	public void setSimpleObjectDao(ISimpleObjectDao simpleObjectDao) {
		this.simpleObjectDao = simpleObjectDao;
	}

	@Override
	public List<Country> listCountry() {
		return this.simpleObjectDao.listCountry();
	}

}
