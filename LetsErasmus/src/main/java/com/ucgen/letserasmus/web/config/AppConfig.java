package com.ucgen.letserasmus.web.config;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

@Configuration
@ComponentScan(basePackages = "com.ucgen")
//@ImportResource("classpath:config/spring/database-config.xml")
@EnableTransactionManagement
public class AppConfig implements TransactionManagementConfigurer {
			
	@Bean(name = "multipartResolver")
    public StandardServletMultipartResolver resolver() {
        return new StandardServletMultipartResolver();
    }
	
	@Bean(name="dataSource")
	public DataSource dataSource() {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		
		//dataSource.setUrl("jdbc:mysql://localhost:3306/letserasmus_db?useUnicode=true&amp;characterEncoding=UTF-8");
		/*
		dataSource.setUrl("jdbc:mysql://localhost:3306/letserasmus_db");
		dataSource.setUsername("root");
		dataSource.setPassword("root");
		*/
		
		dataSource.setUrl("jdbc:mysql://letserasmusdb.caidndq6n0sz.eu-west-1.rds.amazonaws.com:23101/letserasmus_db");
		dataSource.setUsername("erasmus_admin");
		dataSource.setPassword("zUgFA5Ye42U*LNXD");
		
		return dataSource;
	}
	
	@Bean(name="transactionManager")
    public DataSourceTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }
	
	@Override
	public DataSourceTransactionManager annotationDrivenTransactionManager() {
		return transactionManager();
	}

}
