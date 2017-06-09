package com.ucgen.letserasmus.web.config;

import java.nio.charset.Charset;
import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@EnableWebMvc
@Import (AppConfig.class)
@EnableTransactionManagement
public class SpringMVCConfiguration extends WebMvcConfigurerAdapter{
		
	@Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> httpMessageConverters) {
		CustomMessageConverter jacksonConverter = new CustomMessageConverter();
		jacksonConverter.setObjectMapper(new ObjectMapper());
		jacksonConverter.setDefaultCharset(Charset.forName("UTF-8"));
		
		StringHttpMessageConverter stringConverter = new StringHttpMessageConverter();
		stringConverter.setDefaultCharset(Charset.forName("UTF-8"));
		
		httpMessageConverters.add(jacksonConverter);
		httpMessageConverters.add(stringConverter);
		
		super.configureMessageConverters(httpMessageConverters);
    }
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/static/**").addResourceLocations("/static/");
		registry.addResourceHandler("/place/**").addResourceLocations("/place/");
		registry.addResourceHandler("/user/**").addResourceLocations("/user/");
	}
	
}