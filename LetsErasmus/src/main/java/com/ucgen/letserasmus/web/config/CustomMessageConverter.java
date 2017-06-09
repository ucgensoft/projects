package com.ucgen.letserasmus.web.config;

import java.io.IOException;
import java.lang.reflect.Type;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import com.ucgen.common.util.FileUtil;
import com.ucgen.letserasmus.library.user.model.User;

public class CustomMessageConverter extends MappingJackson2HttpMessageConverter {
	
	
	@Override
	public Object read(Type type, Class<?> contextClass, HttpInputMessage inputMessage)
			throws IOException, HttpMessageNotReadableException {
		try {
			String body = FileUtil.readInputStream(inputMessage.getBody(), this.getDefaultCharset().name());
			Object obj = this.getObjectMapper().readValue(body, super.getJavaType(type, contextClass));
			return obj;
		} catch (Exception e) {
			throw e;
		}
	}
	
}
