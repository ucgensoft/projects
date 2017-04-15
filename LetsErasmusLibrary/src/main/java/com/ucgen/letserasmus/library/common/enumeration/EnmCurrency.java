package com.ucgen.letserasmus.library.common.enumeration;

public enum EnmCurrency {

	TL(1, "TRY"),
	EURO(2, "EUR"),
	DOLLAR(3, "USD");
	
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
