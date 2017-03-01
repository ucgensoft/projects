package com.ucgen.common.model;

public class Size {

	private Float width;
	private Float height;
	
	public Float getWidth() {
		return width;
	}

	public void setWidth(Float width) {
		this.width = width;
	}

	public Float getHeight() {
		return height;
	}

	public void setHeight(Float height) {
		this.height = height;
	}

	public Size(Float width, Float height) {
		this.width = width;
		this.height = height;
	}
	
}
