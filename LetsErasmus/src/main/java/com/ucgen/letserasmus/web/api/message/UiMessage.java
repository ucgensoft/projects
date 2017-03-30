package com.ucgen.letserasmus.web.api.message;

import com.ucgen.letserasmus.library.common.model.BaseModel;

public class UiMessage extends BaseModel {

	private static final long serialVersionUID = 7823978904780312439L;

	public static final Integer MESSAGE_TYPE_TEXT = 1;
	public static final Integer MESSAGE_TYPE_TRANSACTION = 2;
	
	private Long senderUserId;
	private Long receiverUserId;
	private String messageTitle;
	private String messageText;
	private Integer status;
	private Integer messageType;
	
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
	public Integer getMessageType() {
		return messageType;
	}
	public void setMessageType(Integer messageType) {
		this.messageType = messageType;
	}
	
	
}
