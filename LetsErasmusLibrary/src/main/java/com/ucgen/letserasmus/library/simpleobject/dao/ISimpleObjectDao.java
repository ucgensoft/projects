
package com.ucgen.letserasmus.library.simpleobject.dao;

import java.util.List;
import java.util.TreeMap;

import com.ucgen.common.operationresult.OperationResult;
import com.ucgen.letserasmus.library.simpleobject.model.CancelPolicyRule;
import com.ucgen.letserasmus.library.simpleobject.model.City;
import com.ucgen.letserasmus.library.simpleobject.model.Country;
import com.ucgen.letserasmus.library.simpleobject.model.Question;
import com.ucgen.letserasmus.library.simpleobject.model.QuestionGroup;
import com.ucgen.letserasmus.library.simpleobject.model.University;

public interface ISimpleObjectDao {

	List<Country> listCountry();

	List<City> listCity(City city);
	
	City getCity(City city);
	
	List<University> listUniversity(Integer countryId);
	
	List<QuestionGroup> listQuestionGroup();
	
	Country getCountryWithIsoCode2(String isoCode2);
	
	List<Question> listQuestion(String groupTitle, String searchText);
	
	void updateCountry(List<String> countryList);
	
	OperationResult insertTransactionId(String transactionId);

	TreeMap<Integer, CancelPolicyRule> listCancelPolicyRule(Integer entityType, Integer policyId);
	
}
