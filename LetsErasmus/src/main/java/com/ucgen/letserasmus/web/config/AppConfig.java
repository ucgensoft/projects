package com.ucgen.letserasmus.web.config;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

@EnableTransactionManagement
public class AppConfig implements TransactionManagementConfigurer {

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
		
		/*
		dataSource.setUrl("jdbc:mysql://89.163.140.58:3306/kemalgul_erasmus");
		dataSource.setUsername("kemalgul_admin");
		dataSource.setPassword("kgadmin");
		*/
		return dataSource;
	}
	
	@Bean
    public PlatformTransactionManager txManager() {
        return new DataSourceTransactionManager(dataSource());
    }
	
	@Override
	public PlatformTransactionManager annotationDrivenTransactionManager() {
		return txManager();
	}
	
}
