package com.ucgen.letserasmus.library.simpleobject.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import com.ucgen.letserasmus.library.simpleobject.dao.CountryRowMapper;
import com.ucgen.letserasmus.library.simpleobject.dao.ISimpleObjectDao;
import com.ucgen.letserasmus.library.simpleobject.dao.QuestionGroupRowMapper;
import com.ucgen.letserasmus.library.simpleobject.dao.QuestionRowMapper;
import com.ucgen.letserasmus.library.simpleobject.model.Country;
import com.ucgen.letserasmus.library.simpleobject.model.Question;
import com.ucgen.letserasmus.library.simpleobject.model.QuestionGroup;

@Repository
public class SimpleObjectDao extends JdbcDaoSupport implements ISimpleObjectDao {
	
	private static final String LIST_COUNTRY_SQL = "SELECT * FROM COUNTRY";
	private static final String LIST_QUESTION_GROUP_SQL = "SELECT DISTINCT Q_GROUP, GROUP_ORDER FROM HELP ORDER BY GROUP_ORDER";
	private static final String LIST_QUESTION_SQL = "SELECT * FROM HELP WHERE 1=1";
	
	@Autowired
	public SimpleObjectDao(DataSource dataSource) {
		super();
		super.setDataSource(dataSource);
	}
	
	@Override
	public List<Country> listCountry() {
		return this.getJdbcTemplate().query(LIST_COUNTRY_SQL, new CountryRowMapper());
	}

	@Override
	public List<QuestionGroup> listQuestionGroup() {
		List<QuestionGroup> questionGroupList = this.getJdbcTemplate().query(LIST_QUESTION_GROUP_SQL, new QuestionGroupRowMapper());
		for(int i = 1; i <= questionGroupList.size(); i++) {
			questionGroupList.get(i - 1).setId(i);
		}
		return questionGroupList;
	}

	@Override
	public List<Question> listQuestion(String groupTitle, String searchText) {
		StringBuilder listQuestionBuilder = new StringBuilder(LIST_QUESTION_SQL);
		List<Object> argList = new ArrayList<Object>();
		
		if (groupTitle != null && groupTitle.trim().length() > 0) {
			if (groupTitle.equalsIgnoreCase("FAQ")) {
				listQuestionBuilder.append(" AND FAQ_STATUS = ? ");
				argList.add("Y");
			} else {
				listQuestionBuilder.append(" AND Q_GROUP = ? ");
				argList.add(groupTitle);
			}
		}
		
		if (searchText != null && searchText.trim().length() > 0) {
			searchText = "%" + searchText + "%";
			listQuestionBuilder.append(" AND (LOWER(QUESTION) LIKE LOWER(?) OR LOWER(ANSWER) LIKE LOWER(?) )");
			argList.add(searchText);
			argList.add(searchText);
		}
		return this.getJdbcTemplate().query(listQuestionBuilder.toString(), argList.toArray(), new QuestionRowMapper());
	}

}
