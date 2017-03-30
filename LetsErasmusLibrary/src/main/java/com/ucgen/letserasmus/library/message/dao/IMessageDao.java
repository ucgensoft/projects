package com.ucgen.letserasmus.library.message.dao;

import java.util.List;

import com.ucgen.common.exception.operation.OperationResultException;
import com.ucgen.common.operationresult.OperationResult;
import com.ucgen.letserasmus.library.message.model.Message;
import com.ucgen.letserasmus.library.message.model.MessageThread;

public interface IMessageDao {

	OperationResult insertMessageThread(MessageThread messageThread) throws OperationResultException;
	
	OperationResult updateMessageThread(MessageThread messageThread);
	
	OperationResult insertMessage(Message message);
	
	List<Message> listMessage(Message message, boolean senderUserFlag, boolean receiverUserFlag);

	List<MessageThread> listMessageThread(MessageThread messageThread, boolean entityFlag, boolean hostUserFlag, boolean clientUserFlag);
	
	MessageThread getMessageThread(MessageThread messageThread, boolean entityFlag, boolean hostUserFlag, boolean clientUserFlag);
	
}
