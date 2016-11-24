package com.ucgen.common.concurrency;

public abstract class BatchWorker<T, K> implements Runnable {
	
	private T batchProcessManager;

	public T getBatchProcessManager() {
		return batchProcessManager;
	}

	public void setBatchProcessManager(T batchProcessManager) {
		this.batchProcessManager = batchProcessManager;
	}
	
	public abstract void onStart(K task);
	
	public abstract void onComplete(K task);
	
}
