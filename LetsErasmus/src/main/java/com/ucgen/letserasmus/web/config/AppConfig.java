package com.ucgen.letserasmus.web.config;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.context.annotation.Bean;

public class AppConfig {

	@Bean
	public DataSource dataSource() {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		
		/*
		dataSource.setUrl("jdbc:mysql://mysql.letserasmus.com/letserasmus_com_1");
		dataSource.setUsername("letserasmuscom1");
		dataSource.setPassword("sK7LcUdM");
		*/
		
		dataSource.setUrl("jdbc:mysql://localhost:3306/motokroscum_com");
		dataSource.setUsername("root");
		dataSource.setPassword("admin");
		
		return dataSource;
	}
	
}
