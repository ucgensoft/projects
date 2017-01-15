package com.ucgen.letserasmus.library.enumeration.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.ucgen.common.dao.BaseRowMapper;
import com.ucgen.letserasmus.library.enumeration.model.Enumeration;

public class EnumerationRowMapper extends BaseRowMapper<Enumeration> {

	public static final String TABLE_NAME = "ENUMERATION";
	
	public static final String COL_ENUM_TYPE = "ENUM_TYPE";
	public static final String COL_ENUM_KEY = "ENUM_KEY";
	public static final String COL_ENUM_VALUE = "ENUM_VALUE";
	public static final String COL_ENUM_ORDER = "ENUM_ORDER";
	public static final String COL_UI_LABEL = "UI_LABEL";
	public static final String COL_DESCRIPTION = "DESCRIPTION";
	public static final String COL_ICON_CONTENT = "ICON_CONTENT";
	public static final String COL_ICON_URL = "ICON_URL";
	public static final String COL_ICON_STYLE = "ICON_STYLE";
	
	public EnumerationRowMapper() {
		this(null);
	}
	
	public EnumerationRowMapper(String tablePrefix) {
		super(TABLE_NAME, tablePrefix);
	}
	
	@Override
	public Enumeration mapRow(ResultSet rs, int rowNum) throws SQLException {
		super.initializeColSet(rs);
		
		Enumeration enumeration = new Enumeration();
		
		enumeration.setEnumType(super.getString(rs, COL_ENUM_TYPE));
		enumeration.setEnumKey(super.getString(rs, COL_ENUM_KEY));
		enumeration.setEnumValue(super.getString(rs, COL_ENUM_VALUE));
		enumeration.setEnumOrder(super.getInteger(rs, COL_ENUM_ORDER));
		enumeration.setUiLabel(super.getString(rs, COL_UI_LABEL));
		enumeration.setDescription(super.getString(rs, COL_DESCRIPTION));
		enumeration.setIconContent(super.getString(rs, COL_ICON_CONTENT));
		enumeration.setIconUrl(super.getString(rs, COL_ICON_URL));
		enumeration.setIconStyle(super.getString(rs, COL_ICON_STYLE));
		
		return enumeration;
	}

	@Override
	public void fillFieldMaps() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initializeColList() {
		super.addColumn(COL_ENUM_TYPE);
		super.addColumn(COL_ENUM_KEY);
		super.addColumn(COL_ENUM_VALUE);
		super.addColumn(COL_ENUM_ORDER);
		super.addColumn(COL_UI_LABEL);
		super.addColumn(COL_DESCRIPTION);
		super.addColumn(COL_ICON_CONTENT);
		super.addColumn(COL_ICON_URL);
		super.addColumn(COL_ICON_STYLE);
	}

}
