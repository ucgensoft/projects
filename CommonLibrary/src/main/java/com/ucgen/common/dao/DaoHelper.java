package com.ucgen.common.dao;

import java.sql.Connection;

public class DaoHelper {

	public static void closeConnection(Connection con) {
		try {
			if (con != null && !con.isClosed()) {
				con.close();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
}
