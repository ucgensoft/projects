package com.ucgen.letserasmus.web.config;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletRegistration;
import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
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
		
		
		dataSource.setUrl("jdbc:mysql://localhost:3306/motokroscum_com");
		dataSource.setUsername("root");
		dataSource.setPassword("admin");
		
		/*
		dataSource.setUrl("jdbc:mysql://dbletserasmus.cknhbnlsvkjo.us-west-2.rds.amazonaws.com:1234/letserasmus_db");
		dataSource.setUsername("admin");
		dataSource.setPassword("LetsErasmus");
		*/
		
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
 
	@Bean(name = "multipartConfigElement")
    public MultipartConfigElement multipartConfigElement() {
    	MultipartConfigElement multipartConfigElement = new MultipartConfigElement( LOCATION, MAX_FILE_SIZE, MAX_REQUEST_SIZE, FILE_SIZE_THRESHOLD);
        return multipartConfigElement;
    }
 
    private static final String LOCATION = "d:\\tmp\\upload_file"; // Temporary location where files will be stored
 
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB : Max file size.
                                                        
    private static final long MAX_REQUEST_SIZE = 20 * 1024 * 1024; // 20MB : Total request size containing Multi part.
     
    private static final int FILE_SIZE_THRESHOLD = 0; // Size threshold after which files will be written to disk

}
