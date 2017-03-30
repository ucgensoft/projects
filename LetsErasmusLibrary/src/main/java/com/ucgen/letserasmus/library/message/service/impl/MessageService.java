package com.ucgen.letserasmus.library.message.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ucgen.common.exception.operation.OperationResultException;
import com.ucgen.common.operationresult.OperationResult;
import com.ucgen.letserasmus.library.message.dao.IMessageDao;
import com.ucgen.letserasmus.library.message.model.Message;
import com.ucgen.letserasmus.library.message.model.MessageThread;
import com.ucgen.letserasmus.library.message.service.IMessageService;

@Service
public class MessageService implements IMessageService {

	private IMessageDao messageDao;
	
	@Autowired
	public void setMessageDao(IMessageDao messageDao) {
		this.messageDao = messageDao;
	}

	@Override
	public OperationResult insertMessageThread(MessageThread messageThread) throws OperationResultException {
		return this.messageDao.insertMessageThread(messageThread);
	}
	
	@Override
	public OperationResult updateMessageThread(MessageThread messageThread) {
		return this.messageDao.updateMessageThread(messageThread);
	}
	
	@Override
	public OperationResult insertMessage(Message message) {
		return this.messageDao.insertMessage(message);
	}

	@Override
	public List<Message> listMessage(Message message, boolean senderUserFlag, boolean receiverUserFlag) {
		return this.messageDao.listMessage(message, senderUserFlag, receiverUserFlag);
	}

	@Override
	public List<MessageThread> listMessageThread(MessageThread messageThread, boolean entityFlag, boolean hostUserFlag,
			boolean clientUserFlag) {
		return this.messageDao.listMessageThread(messageThread, entityFlag, hostUserFlag, clientUserFlag);
	}

	@Override
	public MessageThread getMessageThread(MessageThread messageThread, boolean entityFlag, boolean hostUserFlag,
			boolean clientUserFlag) {
		return this.messageDao.getMessageThread(messageThread, entityFlag, hostUserFlag, clientUserFlag);
	}
	
}