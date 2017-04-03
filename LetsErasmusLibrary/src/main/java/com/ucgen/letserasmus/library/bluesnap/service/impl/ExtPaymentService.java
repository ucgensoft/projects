package com.ucgen.letserasmus.library.bluesnap.service.impl;

import com.ucgen.common.operationresult.ValueOperationResult;
import com.ucgen.letserasmus.library.bluesnap.model.ExtVendor;
import com.ucgen.letserasmus.library.bluesnap.service.IExtPaymentService;

public class ExtPaymentService implements IExtPaymentService {

	@Override
	public ValueOperationResult<Long> createVendor(ExtVendor extVendor) {
		/*
		final String uri = "http://localhost:8080/springrestexample/employees";
	     
	    RestTemplate restTemplate = new RestTemplate();
	     
	    HttpHeaders headers = new HttpHeaders();
	    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
	    HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
	     
	    ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
	     
	    System.out.println(result);
	    */
		return null;
	}

}
