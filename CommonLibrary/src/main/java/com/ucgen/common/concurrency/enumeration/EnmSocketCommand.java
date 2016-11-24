package com.ucgen.common.concurrency.enumeration;

public enum EnmSocketCommand {
	STOP(1), OK(2);

	private int id;

	public int getId() {
		return this.id;
	}

	EnmSocketCommand(int id) {
		this.id = id;
	}
}
