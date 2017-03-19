package com.ucgen.letserasmus.library.simpleobject.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.ucgen.common.dao.BaseRowMapper;
import com.ucgen.letserasmus.library.simpleobject.model.Question;

public class QuestionRowMapper extends BaseRowMapper<Question> {

	public static final String TABLE_NAME = "HELP";
	
	public static final String COL_ID = "ID";
	public static final String COL_Q_GROUP = "Q_GROUP";
	public static final String COL_QUESTION = "QUESTION";
	public static final String COL_ANSWER = "ANSWER";
	public static final String COL_QUESTION_ORDER = "QUESTION_ORDER";
	public static final String COL_FAQ_STATUS = "FAQ_STATUS";
	
	public QuestionRowMapper() {
		this(null);
	}
	
	public QuestionRowMapper(String tablePrefix) {
		super(TABLE_NAME, tablePrefix);
	}
	
	@Override
	public Question mapRow(ResultSet rs, int rowNum) throws SQLException {
		super.initializeColSet(rs);
		
		Question question = new Question();
		
		question.setId(super.getInteger(rs, COL_ID));
		question.setGroupTitle(super.getString(rs, COL_Q_GROUP));
		question.setQuestion(super.getString(rs, COL_QUESTION));
		question.setAnswer(super.getString(rs, COL_ANSWER));
		question.setQuestionOrder(super.getInteger(rs, COL_QUESTION_ORDER));
		question.setIsFaq(super.getString(rs, COL_FAQ_STATUS));
		
		return question;
	}

	@Override
	public void fillFieldMaps() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initializeColList() {
		super.addColumn(COL_Q_GROUP);
		super.addColumn(COL_QUESTION);
		super.addColumn(COL_ANSWER);
		super.addColumn(COL_QUESTION_ORDER);
		super.addColumn(COL_FAQ_STATUS);
	}
	
}

