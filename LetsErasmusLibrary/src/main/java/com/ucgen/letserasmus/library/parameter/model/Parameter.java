package com.ucgen.letserasmus.library.parameter.model;

import com.ucgen.letserasmus.library.common.model.BaseModel;

public class Parameter extends BaseModel {
	
	private static final long serialVersionUID = 2756047222061635513L;

	private Integer id;
	private String name;
	private String value;
	private String description;

	public Parameter() {
		
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
