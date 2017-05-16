
package com.ucgen.letserasmus.library.simpleobject.dao;

import java.util.List;
import java.util.TreeMap;

import com.ucgen.common.operationresult.OperationResult;
import com.ucgen.letserasmus.library.simpleobject.model.CancelPolicyRule;
import com.ucgen.letserasmus.library.simpleobject.model.Country;
import com.ucgen.letserasmus.library.simpleobject.model.Question;
import com.ucgen.letserasmus.library.simpleobject.model.QuestionGroup;

public interface ISimpleObjectDao {

	List<Country> listCountry();
	
	List<QuestionGroup> listQuestionGroup();
	
	Country getCountryWithIsoCode2(String isoCode2);
	
	List<Question> listQuestion(String groupTitle, String searchText);
	
	void updateCountry(List<String> countryList);
	
	OperationResult insertTransactionId(String transactionId);

	TreeMap<Integer, CancelPolicyRule> listCancelPolicyRule(Integer entityType, Integer policyId);
	
}
