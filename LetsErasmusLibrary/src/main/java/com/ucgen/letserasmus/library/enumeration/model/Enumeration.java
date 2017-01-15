package com.ucgen.letserasmus.library.enumeration.model;

import com.ucgen.letserasmus.library.common.model.BaseModel;

public class Enumeration extends BaseModel {

	private static final long serialVersionUID = -1279968039585044224L;

	private String enumType;
	private String enumKey;
	private String enumValue;
	private Integer enumOrder;
	private String uiLabel;
	private String description;
	private String iconContent;
	private String iconUrl;
	private String iconStyle;
	
	public Enumeration() {
		this(null);
	}
	
	public Enumeration(String enumType) {
		this.enumType = enumType;
	}
	
	public String getEnumType() {
		return enumType;
	}
	public void setEnumType(String enumType) {
		this.enumType = enumType;
	}
	public String getEnumKey() {
		return enumKey;
	}
	public void setEnumKey(String enumKey) {
		this.enumKey = enumKey;
	}
	public String getEnumValue() {
		return enumValue;
	}
	public void setEnumValue(String enumValue) {
		this.enumValue = enumValue;
	}
	public Integer getEnumOrder() {
		return enumOrder;
	}
	public void setEnumOrder(Integer enumOrder) {
		this.enumOrder = enumOrder;
	}
	public String getUiLabel() {
		return uiLabel;
	}
	public void setUiLabel(String uiLabel) {
		this.uiLabel = uiLabel;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	public String getIconContent() {
		return iconContent;
	}

	public void setIconContent(String iconContent) {
		this.iconContent = iconContent;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	public String getIconStyle() {
		return iconStyle;
	}

	public void setIconStyle(String iconStyle) {
		this.iconStyle = iconStyle;
	}

}
