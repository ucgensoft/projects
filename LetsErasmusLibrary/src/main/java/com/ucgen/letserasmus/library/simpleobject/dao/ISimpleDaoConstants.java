package com.ucgen.letserasmus.library.simpleobject.dao;

public interface ISimpleDaoConstants {

	String LIST_COUNTRY_SQL = "SELECT * FROM COUNTRY";
	
	String LIST_QUESTION_GROUP_SQL = "SELECT DISTINCT Q_GROUP, GROUP_ORDER FROM HELP ORDER BY GROUP_ORDER";
	
	String LIST_QUESTION_SQL = "SELECT * FROM HELP WHERE 1=1";
	
	String GET_TRANSACTION_ID_SQL = "SELECT COUNT(1) FROM TRANSACTION_ID WHERE ID = ?";
	
	String INSERT_TRANSACTION_ID_SQL = "INSERT INTO TRANSACTION_ID (ID) VALUES(?)";
	
	String LIST_CANCEL_POLICY_RULE_SQL = "SELECT * FROM CANCEL_POLICY_RULE";
	
}