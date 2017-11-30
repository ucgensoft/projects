package com.ucgen.letserasmus.library.simpleobject.service;

import java.util.List;
import java.util.TreeMap;

import com.ucgen.letserasmus.library.simpleobject.model.CancelPolicyRule;
import com.ucgen.letserasmus.library.simpleobject.model.City;
import com.ucgen.letserasmus.library.simpleobject.model.Country;
import com.ucgen.letserasmus.library.simpleobject.model.Question;
import com.ucgen.letserasmus.library.simpleobject.model.QuestionGroup;
import com.ucgen.letserasmus.library.simpleobject.model.University;

public interface ISimpleObjectService {

	List<Country> listCountry();
	
	List<City> listCity(City city);
	
	City getCity(City city);
	
	List<University> listUniversity(Integer countryId);
	
	Country getCountryWithIsoCode2(String isoCode2);
	
	List<QuestionGroup> listQuestionGroup();
	
	List<Question> listQuestion(String groupTitle, String searchText);
	
	String generateTransactionId();
	
	TreeMap<Integer, CancelPolicyRule> listCancelPolicyRule(Integer entityType, Integer policyId);
	
}
