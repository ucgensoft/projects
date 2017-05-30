package com.ucgen.letserasmus.library.parameter.service;

public interface IParameterService 
{	
	
	public String getParameterValue(Integer parameterId);
	
	public void refreshCache();
	
}
