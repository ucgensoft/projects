package com.ucgen.letserasmus.web.config;

import javax.servlet.Filter;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletRegistration;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import com.ucgen.letserasmus.web.filter.SecurityFilter;

public class AnnotationConfigInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

	protected Class<?>[] getRootConfigClasses() {
        return new Class[] { SpringMVCConfiguration.class };
    }
  
    protected Class<?>[] getServletConfigClasses() {
        return null;
    }
  
    protected String[] getServletMappings() {
    	return new String[] { "/" };
    }
    
    protected Filter[] getServletFilters() {    	
    	Filter [] singleton = { new SecurityFilter() };
    	return singleton;
	}
    
    @Override
    protected void customizeRegistration(ServletRegistration.Dynamic registration) {
        registration.setMultipartConfig(getMultipartConfigElement());
    }
 
    private MultipartConfigElement getMultipartConfigElement() {
    	MultipartConfigElement multipartConfigElement = new MultipartConfigElement( LOCATION, MAX_FILE_SIZE, MAX_REQUEST_SIZE, FILE_SIZE_THRESHOLD);
        return multipartConfigElement;
    }
 
    private static final String LOCATION = "D:\\tmp\\upload_file";
    
   	//private static final String LOCATION = "/var/lib/tomcat8/webapps/ROOT/tmp/upload_file"; // Temporary location where files will be stored
 
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB : Max file size.
                                                        
    private static final long MAX_REQUEST_SIZE = 20 * 1024 * 1024; // 20MB : Total request size containing Multi part.
     
    private static final int FILE_SIZE_THRESHOLD = 0; // Size threshold after which files will be written to disk
 
}