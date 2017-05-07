package com.ucgen.letserasmus.library.common.enumeration;

public enum EnmCurrency {

	TL(1, "TRY"),
	DOLLAR(2, "USD"),
	EURO(3, "EUR");
	
	private Integer id;
	private String blueSnapCode;
	
	public Integer getId() {
		return this.id;
	}
	public String getBlueSnapCode() {
		return this.blueSnapCode;
	}
	
	EnmCurrency(Integer id, String blueSnapCode) {
		this.id = id;
		this.blueSnapCode = blueSnapCode;
	}
	
	public static EnmCurrency getCurrency(Integer id) {
		for (EnmCurrency currency : EnmCurrency.values()) {
			if (currency.getId().equals(id)) {
				return currency;
			}
		}
		return null;
	}
	
}
