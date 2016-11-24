package com.ucgen.common.concurrency;

public class BatchThreadGroup extends ThreadGroup {

	public BatchThreadGroup(String name) {
		super(name);
	}

	@Override
	public void uncaughtException(Thread t, Throwable e) {
		System.out.println(e.getMessage());
	}

}
