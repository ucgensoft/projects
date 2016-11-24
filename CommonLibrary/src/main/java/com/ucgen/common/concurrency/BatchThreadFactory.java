package com.ucgen.common.concurrency;

import java.util.concurrent.ThreadFactory;

public class BatchThreadFactory implements ThreadFactory {

	private Integer appModuleId;
	private String appModuleName;
	private BatchThreadGroup tbasThreadGroup;
	private Integer threadSeq;
	
	public BatchThreadFactory(Integer appModuleId, String appModuleName) {
		this.appModuleId = appModuleId;
		this.appModuleName = appModuleName;
		tbasThreadGroup = new BatchThreadGroup("ThreadGroup_" + this.appModuleName + appModuleName + "_" +  appModuleId);
		threadSeq = 1;
	}
	
	@Override
	public Thread newThread(Runnable r) {
		Thread thread = new Thread(this.tbasThreadGroup, r);
		thread.setName("Thread_" + this.appModuleName + "_" + appModuleId + "_" + this.threadSeq);
		threadSeq++;
		return thread;
	}

}
