package com.ucgen.common.concurrency;

@SuppressWarnings("rawtypes")
public abstract class BatchProcessManager<T extends BatchTask, K extends BatchWorker> {
	
	/*
	public static final int DEFAULT_THREAD_COUNT = 10;
	
	public ListenerThread listenerThread;
	private boolean processStopped = false;
	private EnmInterruptSource interruptSource;
	protected Object atomicTaskOperationLock;
	
	private Integer threadCount;
	private AppModule appModule;
	private BatchProcessLog batchProcessLog;
	
	private List<T> taskList;
	
	private ILogService logService;
	
	public void addTask(T task) {
		synchronized (atomicTaskOperationLock) {
			if (this.taskList == null) {
				this.taskList = new ArrayList<T>();
			}
			this.taskList.add(task);
		}
	}
	
	public int getTaskCount() {
		if (this.taskList != null) {
			return this.taskList.size();
		} else {
			return 0;
		}
	}

	public ILogService getLogService() {
		return logService;
	}

	@Autowired
	public void setLogService(ILogService logService) {
		this.logService = logService;
	}

	public AppModule getAppModule() {
		return appModule;
	}

	public void setAppModule(AppModule appModule) {
		this.appModule = appModule;
	}

	public Integer getThreadCount() {
		return threadCount;
	}

	public void setThreadCount(Integer threadCount) {
		this.threadCount = threadCount;
	}
	
	public BatchProcessLog getBatchProcessLog() {
		return batchProcessLog;
	}

	public void setBatchProcessLog(BatchProcessLog batchProcessLog) {
		this.batchProcessLog = batchProcessLog;
	}
	
	public boolean isProcessStopped() {
		return this.processStopped;
	}
	
	public EnmInterruptSource getInterruptSource() {
		return interruptSource;
	}

	public BatchProcessManager() {
		this.atomicTaskOperationLock = new String();
		this.threadCount = DEFAULT_THREAD_COUNT;
	}

	public void startListenerThread() {
		listenerThread = new ListenerThread(this);
		listenerThread.setName(this.appModule.getId() + "_" + this.appModule.getName() + "_Listener");
		listenerThread.start();
		while (listenerThread.getListenerPort() == null) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				FileLogger.logOperation(Level.ERROR, "Listener thread port bilgisi al�n�rken hata olu�tu. Hata:" + e.getMessage());
			}
		}
	}
	
	public Integer getListenerPort() {
		if (this.listenerThread != null) {
			return this.listenerThread.getListenerPort();
		} else {
			return null;
		}
	}
	
	public void start() {
		BatchProcessLog batchProcessLog = null;
		try {
			startListenerThread();
			batchProcessLog = this.getLogService().insertBatchProcessLog(this.getAppModule().getId(), new Date(), 0, 0, 0, 0, this.getAppModule().getName(), getListenerPort());
			this.setBatchProcessLog(batchProcessLog);
			
			OperationResult createTaskResult = this.createTasks();
			
			if (OperationResult.isResultSucces(createTaskResult)) {
				if (this.getTaskCount() > 0) {
					this.getLogService().insertAppLog(this.getAppModule().getId(), EnmAppLogType.INFO.getValue(), "Thread'ler �al��t�r�l�yor. ��lem Say�s�:" + this.getTaskCount(), this.getAppModule().getName());
					this.startWorkerThreads();
					String statusMessage = "Ba�ar�l� i�lem say�s�:" + batchProcessLog.getSuccessTaskCount() + ", Ba�ar�s�z i�lem say�s�:" + batchProcessLog.getFailTaskCount(); 
					if (!this.isProcessStopped()) {
						String message = "��lem tamamland�! " + statusMessage;
						this.getLogService().insertAppLog(this.getAppModule().getId(), EnmAppLogType.INFO.getValue(), message, this.getAppModule().getName());
						FileLogger.logOperation(Level.INFO, message);
						if (batchProcessLog.getFailTaskCount() == 0) {
							batchProcessLog.setStatus(EnmBatchProcessLogStatus.SUCCESS.getId());
						} else {
							batchProcessLog.setStatus(EnmBatchProcessLogStatus.FAIL.getId());
						}
						batchProcessLog.setDescription(message);
					} else {
						String message = "Process kullan�c� taraf�ndan sonland�r�ld�! " + statusMessage;
						this.getLogService().insertAppLog(this.getAppModule().getId(), EnmAppLogType.INFO.getValue(), message, this.getAppModule().getName());
						FileLogger.logOperation(Level.ERROR, message);
						batchProcessLog.setStatus(EnmBatchProcessLogStatus.STOPPED.getId());
						batchProcessLog.setDescription(message);
					}
				} else {
					String message = "��lem say�s� = 0. Hi� bir i�lem yap�lmad�!";
					FileLogger.logOperation(Level.ERROR, message);
					batchProcessLog.setDescription(message);
					batchProcessLog.setStatus(EnmBatchProcessLogStatus.SUCCESS.getId());
				}
			} else {
				String message = "Yap�lacak i�lemler haz�rlan�rken hata olu�tu. HAta: " + OperationResult.getResultDesc(createTaskResult);
				this.getLogService().insertAppLog(this.getAppModule().getAppId(), EnmAppLogType.ERROR.getValue(), message, this.getAppModule().getName());
				FileLogger.logOperation(Level.ERROR, message);
				batchProcessLog.setDescription(message);
				batchProcessLog.setStatus(EnmBatchProcessLogStatus.FAIL.getId());
			}
			batchProcessLog.setEndTime(new Date());
		} catch (Exception e) {
			String message = "BatchProcessManager-start() - Process ana metodunda hata olu�tu. Hata:" + e.getMessage();
			FileLogger.logOperation(Level.ERROR, message);
			this.getLogService().insertAppLog(this.getAppModule().getId(), EnmAppLogType.EXCEPTION.getValue(), message, this.getAppModule().getName());
			batchProcessLog.setStatus(EnmBatchProcessLogStatus.FAIL.getId());
			batchProcessLog.setDescription(message);
		} finally {
			if (batchProcessLog != null) {
				batchProcessLog.setEndTime(new Date());
				this.updateBatchProcessLog();
			}
			listenerThread.setThreadStopped(true);
		}
	}
	
	public abstract OperationResult createTasks();
	
	//public abstract void doPreProcess();
	
	//public abstract void doPostProcess();
	
	
	public void stop(EnmInterruptSource interruptSource) {
		this.processStopped = true;
		this.interruptSource = interruptSource;
	}
	
	public T getTask() {
		if (!processStopped) {
			synchronized (atomicTaskOperationLock) {
				if (this.taskList.size() > 0) {
					T batchTask = this.taskList.get(0);
					this.taskList.remove(0);
					return batchTask;
				} else {
					return null;
				}
			}
		} else {
			return null;
		}
	}
	
	public abstract void onStartTask(T task);
	
	public void onCompleteTask(T task) {
		try {
			synchronized (this.getBatchProcessLog()) {
				Integer processedTaskCount = this.getBatchProcessLog().getProcessedTaskCount();
				Integer successTaskCount = this.getBatchProcessLog().getSuccessTaskCount();
				Integer failTaskCount = this.getBatchProcessLog().getFailTaskCount();
				
				processedTaskCount++;
				if (OperationResult.isResultSucces(task.getOperationResult())) {
					successTaskCount++;
				} else {
					failTaskCount++;
				}
				
				this.getBatchProcessLog().setProcessedTaskCount(processedTaskCount);
				this.getBatchProcessLog().setSuccessTaskCount(successTaskCount);
				this.getBatchProcessLog().setFailTaskCount(failTaskCount);
				
			}
			this.updateBatchProcessLog();
		} catch (Exception e) {
			String message = "BatchProcessManager-onCompleteTask() - Worker thread i�lemi tamamlad�ktan sonra g�ncelleme i�lemlerinde hata olu�tu. Hata:" + e.getMessage();
			FileLogger.logOperation(Level.ERROR, message);
			this.logService.insertAppLog(this.getAppModule().getId(), EnmAppLogType.EXCEPTION.getValue(), message, this.getAppModule().getName());
		}
	}
	
	public abstract K createRunnable();
	
	protected void startWorkerThreads() throws InterruptedException {
		BatchThreadFactory threadFactory = new BatchThreadFactory(this.appModule.getId(), this.appModule.getName());
		ThreadPoolExecutor executer = new ThreadPoolExecutor(this.getThreadCount(), this.getThreadCount(), 10, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(this.getThreadCount()), threadFactory);
		for (int i = 0; i < this.getThreadCount(); i++) {
			K worker = createRunnable();
			executer.execute(worker);
		}
		executer.shutdown();
		executer.awaitTermination(6, TimeUnit.HOURS);
	}
	
	protected void updateBatchProcessLog() {
		synchronized (this.batchProcessLog) {
			this.logService.updateBatchProcessLog(this.batchProcessLog.getId(), this.batchProcessLog.getEndTime(), 
					this.batchProcessLog.getTotalTaskCount(), this.batchProcessLog.getProcessedTaskCount(), this.batchProcessLog.getSuccessTaskCount(), 
					this.batchProcessLog.getFailTaskCount(), this.batchProcessLog.getStatus(), this.appModule.getName(), this.getListenerPort(), this.batchProcessLog.getDescription());
		}
	}
	*/
}
