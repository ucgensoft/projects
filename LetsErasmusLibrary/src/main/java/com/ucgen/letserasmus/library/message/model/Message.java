package com.ucgen.letserasmus.library.message.model;

import com.ucgen.letserasmus.library.common.model.BaseModel;
import com.ucgen.letserasmus.library.user.model.User;

public class Message extends BaseModel {

	private static final long serialVersionUID = 8626824546065736638L;

	private Long id;
	private Long messageThreadId;
	private Long senderUserId;
	private Long receiverUserId;
	private String messageTitle;
	private String messageText;
	private Integer status;
	
	private MessageThread messageThread;
	private User senderUser;
	private User receiverUser;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getMessageThreadId() {
		return messageThreadId;
	}
	public void setMessageThreadId(Long messageThreadId) {
		this.messageThreadId = messageThreadId;
	}
	public Long getSenderUserId() {
		return senderUserId;
	}
	public void setSenderUserId(Long senderUserId) {
		this.senderUserId = senderUserId;
	}
	public Long getReceiverUserId() {
		return receiverUserId;
	}
	public void setReceiverUserId(Long receiverUserId) {
		this.receiverUserId = receiverUserId;
	}
	public String getMessageTitle() {
		return messageTitle;
	}
	public void setMessageTitle(String messageTitle) {
		this.messageTitle = messageTitle;
	}
	public String getMessageText() {
		return messageText;
	}
	public void setMessageText(String messageText) {
		this.messageText = messageText;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public User getSenderUser() {
		return senderUser;
	}
	public void setSenderUser(User senderUser) {
		this.senderUser = senderUser;
	}
	public User getReceiverUser() {
		return receiverUser;
	}
	public void setReceiverUser(User receiverUser) {
		this.receiverUser = receiverUser;
	}
	public MessageThread getMessageThread() {
		return messageThread;
	}
	public void setMessageThread(MessageThread messageThread) {
		this.messageThread = messageThread;
	}
	
}
