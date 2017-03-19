package com.ucgen.letserasmus.library.simpleobject.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.ucgen.common.dao.BaseRowMapper;
import com.ucgen.letserasmus.library.simpleobject.model.QuestionGroup;

public class QuestionGroupRowMapper extends BaseRowMapper<QuestionGroup> {

	public static final String TABLE_NAME = "HELP";
	
	public static final String COL_Q_GROUP = "Q_GROUP";
	public static final String COL_GROUP_ORDER = "GROUP_ORDER";
	
	public QuestionGroupRowMapper() {
		this(null);
	}
	
	public QuestionGroupRowMapper(String tablePrefix) {
		super(TABLE_NAME, tablePrefix);
	}
	
	@Override
	public QuestionGroup mapRow(ResultSet rs, int rowNum) throws SQLException {
		super.initializeColSet(rs);
		
		QuestionGroup questionGroup = new QuestionGroup();
		
		questionGroup.setGroupTitle(super.getString(rs, COL_Q_GROUP));
		questionGroup.setGroupOrder(super.getInteger(rs, COL_GROUP_ORDER));
		
		return questionGroup;
	}

	@Override
	public void fillFieldMaps() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initializeColList() {
		super.addColumn(COL_Q_GROUP);
		super.addColumn(COL_GROUP_ORDER);
	}
	
}