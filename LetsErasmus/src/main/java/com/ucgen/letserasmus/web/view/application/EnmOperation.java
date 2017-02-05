package com.ucgen.letserasmus.web.view.application;

public enum EnmOperation {

	CREATE_USER(1),
	UPDATE_USER(2),
	LIST_USER_PLACE(3),
	EDIT_PLACE(4);
	
	private final int id;
	
	public int getId() {
		return this.id;
	}
	
	EnmOperation(int id) {
		this.id = id;
	}
	
	public static EnmOperation getOperation(int id) {
		for (EnmOperation operation : EnmOperation.values()) {
			if (operation.getId() == id) {
				return operation;
			}
		}
		return null;
	}
 	
}
