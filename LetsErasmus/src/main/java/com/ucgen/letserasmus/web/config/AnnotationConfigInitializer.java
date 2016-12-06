package com.ucgen.letserasmus.web.config;

import javax.servlet.Filter;

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
 
}