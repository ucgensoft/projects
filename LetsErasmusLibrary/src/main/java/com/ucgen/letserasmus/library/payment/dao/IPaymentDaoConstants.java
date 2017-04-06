package com.ucgen.letserasmus.library.payment.dao;

public interface IPaymentDaoConstants {

	String INSERT_PAYMENT_METHOD_SQL = "INSERT INTO PAYMENT_METHOD (USER_ID, CARD_NUMBER, CARD_TYPE, EXP_DATE, BLUESNAP_ID, "
			+ "CREATED_BY, CREATED_DATE) VALUES(?, ?, ?, ?, ?, ?, ?)";

	String LIST_PAYMENT_METHOD_SQL = "SELECT * FROM PAYMENT_METHOD PM WHERE 1=1";
	
}
