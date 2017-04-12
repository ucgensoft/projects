package com.ucgen.letserasmus.library.simpleobject.dao;

import java.util.List;

import com.ucgen.letserasmus.library.simpleobject.model.Country;
import com.ucgen.letserasmus.library.simpleobject.model.Question;
import com.ucgen.letserasmus.library.simpleobject.model.QuestionGroup;

public interface ISimpleObjectDao {

	List<Country> listCountry();
	
	List<QuestionGroup> listQuestionGroup();
	
	List<Question> listQuestion(String groupTitle, String searchText);
	
	void updateCountry(List<String> countryList);
	
}
