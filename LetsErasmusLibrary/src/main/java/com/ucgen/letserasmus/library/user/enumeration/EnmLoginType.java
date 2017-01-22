package com.ucgen.letserasmus.library.user.enumeration;

public enum EnmLoginType {

	LOCAL_ACCOUNT(1),
	GOOGLE(2),
	FACEBOOK(3);
	
	private Integer id;
	
	public Integer getId() {
		return this.id;
	}
	
	EnmLoginType(Integer id) {
		this.id = id;
	}
	
	public static EnmLoginType getLoginType(Integer id) {
		for (EnmLoginType loginType : EnmLoginType.values()) {
			if (loginType.getId().equals(id)) {
				return loginType;
			}
		}
		return null;
	}
	
}
