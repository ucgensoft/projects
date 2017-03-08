package com.ucgen.common.concurrency;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

import com.ucgen.common.concurrency.enumeration.EnmSocketCommand;

public class ListenerThread extends Thread {

	private Boolean threadStopped;
	private Integer listenerPort;
	
	public Boolean getThreadStopped() {
		return threadStopped;
	}

	public void setThreadStopped(Boolean threadStopped) {
		this.threadStopped = threadStopped;
	}

	public Integer getListenerPort() {
		return listenerPort;
	}

	public void setListenerPort(Integer listenerPort) {
		this.listenerPort = listenerPort;
	}

	private BatchProcessManager batchProcessManager = null;
	
	public ListenerThread(BatchProcessManager batchProcessManager) {
		this.batchProcessManager = batchProcessManager;
		this.threadStopped = false;
	}
	
	@Override
	public void run() {
    	ServerSocket serverSocket = null;
    	ObjectOutputStream out = null;
    	ObjectInputStream in = null;
    	Socket clientSocket = null;
		try {
			if (this.listenerPort != null) {
				serverSocket = new ServerSocket(this.listenerPort);
			} else {
				serverSocket = new ServerSocket(0);
				this.listenerPort = serverSocket.getLocalPort();
			}
			serverSocket.setSoTimeout(3000);
			while (true) {
				if (!threadStopped) {
					try {
						clientSocket = serverSocket.accept();
						
						out = new ObjectOutputStream(clientSocket.getOutputStream());
			            in = new ObjectInputStream(clientSocket.getInputStream()); 
			            
			            Object msg = in.readObject();
			            out.writeObject(EnmSocketCommand.OK.getId());
			            if (msg != null && msg.equals(EnmSocketCommand.STOP.getId())) {
			            	//batchProcessManager.stop(EnmInterruptSource.USER);
			            	break;
			            }
					} catch (SocketTimeoutException e) {
						// timeout özellikle set edildi. Handle edilmesine gerek yok.
					} finally {
						close(clientSocket);
						close(in);
						close(out);
					}
				} else {
					break;
				}
	        }
		} catch (IOException e) {
			System.out.println(e.getMessage());
		} catch (ClassNotFoundException e) {
			System.out.println(e.getMessage());
		} finally {
			close(clientSocket);
			close(serverSocket);
			close(in);
			close(out);
		}
    }
    
    private void close(Object closable) {
		try {
			if (closable != null) {
				if (closable instanceof Socket) {
					((Socket) closable).close();
				} else if (closable instanceof ServerSocket) {
					((ServerSocket) closable).close();
				} else if (closable instanceof OutputStream) {
					((OutputStream) closable).close();
				} else if (closable instanceof InputStream) {
					((InputStream) closable).close();
				}
	    	}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
    }
    	
}
