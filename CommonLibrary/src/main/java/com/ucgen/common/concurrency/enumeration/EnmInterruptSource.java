package com.ucgen.common.concurrency.enumeration;

public enum EnmInterruptSource {

	USER(1, "User"), 
	WORKER_THREAD(2, "WorkerThread");

	private int id;
	private String name;

	public int getId() {
		return this.id;
	}
	
	public String getName() {
		return this.name;
	}

	EnmInterruptSource(int id, String name) {
		this.id = id;
		this.name = name;
	}
	
}
