package com.ucgen.letserasmus.library.message.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ucgen.common.dao.UtilityDao;
import com.ucgen.common.exception.operation.OperationResultException;
import com.ucgen.common.operationresult.EnmResultCode;
import com.ucgen.common.operationresult.OperationResult;
import com.ucgen.common.operationresult.ValueOperationResult;
import com.ucgen.common.util.StringUtil;
import com.ucgen.letserasmus.library.common.enumeration.EnmEntityType;
import com.ucgen.letserasmus.library.message.dao.IMessageDao;
import com.ucgen.letserasmus.library.message.dao.MessageRowMapper;
import com.ucgen.letserasmus.library.message.dao.MessageThreadRowMapper;
import com.ucgen.letserasmus.library.message.model.Message;
import com.ucgen.letserasmus.library.message.model.MessageThread;

@Repository
public class MessageDao extends JdbcDaoSupport implements IMessageDao {

	private static final String INSERT_MESSAGE_SQL = "INSERT INTO MESSAGE (MESSAGE_THREAD_ID, SENDER_USER_ID, RECEIVER_USER_ID, " 
			+ " MESSAGE_TITLE, MESSAGE_TEXT, STATUS, CREATED_BY, CREATED_DATE) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";	
		
	private static final String INSERT_MESSAGE_THREAD_SQL = "INSERT INTO MESSAGE_THREAD (ENTITY_TYPE, ENTITY_ID, HOST_USER_ID, CLIENT_USER_ID, " 
			+ " THREAD_TITLE, CREATED_BY, CREATED_DATE, MODIFIED_BY, MODIFIED_DATE) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
	
	private static final String UPDATE_MESSAGE_THREAD_SQL = "UPDATE MESSAGE_THREAD SET $1 WHERE ID = ? ";
	
	private static final String GET_LAST_MESSAGE_SQL = "SELECT * FROM MESSAGE WHERE MESSAGE_THREAD_ID = ? ORDER BY ID DESC LIMIT 1";
	
	private UtilityDao utilityDao;
	
	@Autowired
	public void setUtilityDao(UtilityDao utilityDao) {
		this.utilityDao = utilityDao;
	}
	
	@Autowired
	public MessageDao(DataSource dataSource) {
		super();
		super.setDataSource(dataSource);
	}
	
	@Override
	public OperationResult insertMessageThread(MessageThread messageThread) throws OperationResultException {
		OperationResult operationResult = new OperationResult();
		
		List<Object> argList = new ArrayList<Object>();
				
		argList.add(messageThread.getEntityType());
		argList.add(messageThread.getEntityId());
		argList.add(messageThread.getHostUserId());
		argList.add(messageThread.getClientUserId());
		argList.add(messageThread.getThreadTitle());
		argList.add(messageThread.getCreatedBy());
		argList.add(messageThread.getCreatedDate());
		argList.add(messageThread.getCreatedBy());
		argList.add(messageThread.getCreatedDate());
				
		this.getJdbcTemplate().update(INSERT_MESSAGE_THREAD_SQL, argList.toArray());
		
		messageThread.setId(this.utilityDao.getLastIncrementId());
		
		List<Message> messageList = messageThread.getMessageList();
		
		if (messageList != null && messageList.size() > 0) {
			for (Message message : messageList) {
				message.setMessageThreadId(messageThread.getId());
				OperationResult createMessageResult = this.insertMessage(message);
				if (!OperationResult.isResultSucces(createMessageResult)) {
					throw new OperationResultException(createMessageResult);
				}
			}
		}
		
		operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
						
		return operationResult;
	}
	
	@Override
	public OperationResult updateMessageThread(MessageThread messageThread) {
		ValueOperationResult<Integer> operationResult = new ValueOperationResult<Integer>();		
		List<Object> argList = new ArrayList<Object>();
		
		String updateSql = new String(UPDATE_MESSAGE_THREAD_SQL);
		StringBuilder updateFields = new StringBuilder();
						
		if (messageThread.getEntityId() != null) {
			StringUtil.append(updateFields, MessageThreadRowMapper.COL_ENTITY_ID + " = ?", ",");
			argList.add(messageThread.getEntityId());
		}
				
		if (messageThread.getModifiedBy() != null) {
			StringUtil.append(updateFields, "MODIFIED_BY = ?", ",");
			argList.add(messageThread.getModifiedBy());
		}
		
		if (messageThread.getModifiedDate() != null) {
			StringUtil.append(updateFields, "MODIFIED_DATE = ?", ",");
			argList.add(messageThread.getModifiedDate());
		}
		
		argList.add(messageThread.getId());

		updateSql = updateSql.replace("$1", updateFields);
		int updatedRowCount =  this.getJdbcTemplate().update(updateSql, argList.toArray() );
		
		operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
		operationResult.setResultValue(updatedRowCount);
		
		return operationResult;
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public OperationResult insertMessage(Message message) {
		OperationResult operationResult = new OperationResult();
		
		List<Object> argList = new ArrayList<Object>();
				
		argList.add(message.getMessageThreadId());
		argList.add(message.getSenderUserId());
		argList.add(message.getReceiverUserId());
		argList.add(message.getMessageTitle());
		argList.add(message.getMessageText());
		argList.add(message.getStatus());
		argList.add(message.getCreatedBy());
		argList.add(message.getCreatedDate());
		
		
		this.getJdbcTemplate().update(INSERT_MESSAGE_SQL, argList.toArray());
		
		message.setId(this.utilityDao.getLastIncrementId());
		
		operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
						
		return operationResult;
	}

	@Override
	public List<Message> listMessage(Message message, boolean senderUserFlag, boolean receiverUserFlag) {
		StringBuilder sqlBuilder = new StringBuilder();
		List<Object> argList = new ArrayList<Object>();
		
		MessageRowMapper messageRowMapper = new MessageRowMapper();
		
		if (senderUserFlag) {
			messageRowMapper.addFKey(MessageRowMapper.FKEY_USER_SENDER);
		}
		if (receiverUserFlag) {
			messageRowMapper.addFKey(MessageRowMapper.FKEY_USER_RECEIVER);
		}
		
		sqlBuilder.append(messageRowMapper.getSelectSqlWithForeignKeys());
		
		if (message != null) {
			if (message.getMessageThreadId() != null) {
				sqlBuilder.append(" AND " + messageRowMapper.getCriteriaColumnName(MessageRowMapper.COL_MESSAGE_THREAD_ID) + " = ? ");
				argList.add(message.getMessageThreadId());
			}
			if (message.getId() != null) {
				sqlBuilder.append(" AND " + messageRowMapper.getCriteriaColumnName(MessageRowMapper.COL_ID) + " = ? ");
				argList.add(message.getId());
			}
			if (message.getSenderUserId() != null) {
				sqlBuilder.append(" AND " + messageRowMapper.getCriteriaColumnName(MessageRowMapper.COL_SENDER_USER_ID) + " = ? ");
				argList.add(message.getSenderUserId());
			}
			if (message.getReceiverUserId() != null) {
				sqlBuilder.append(" AND " + messageRowMapper.getCriteriaColumnName(MessageRowMapper.COL_RECEIVER_USER_ID) + " = ? ");
				argList.add(message.getSenderUserId());
			}			
		}
		
		sqlBuilder.append(" ORDER BY " + messageRowMapper.getCriteriaColumnName(MessageRowMapper.COL_CREATED_DATE) + " ASC");
		
		List<Message> messageList = super.getJdbcTemplate().query(sqlBuilder.toString(), argList.toArray(), messageRowMapper);
		
		return messageList;
	}
	
	@Override
	public List<MessageThread> listMessageThread(MessageThread messageThread, boolean entityFlag, boolean hostUserFlag, 
			boolean clientUserFlag, boolean lastMessageFlag) {
		StringBuilder sqlBuilder = new StringBuilder();
		List<Object> argList = new ArrayList<Object>();
		
		MessageThreadRowMapper messageThreadRowMapper = new MessageThreadRowMapper();
		
		if (hostUserFlag) {
			messageThreadRowMapper.addFKey(MessageThreadRowMapper.FKEY_USER_HOST);
		}
		if (clientUserFlag) {
			messageThreadRowMapper.addFKey(MessageThreadRowMapper.FKEY_USER_CLIENT);
		}
		if (entityFlag && messageThread.getEntityType() != null) {
			messageThreadRowMapper.addEntiyFKey(MessageThreadRowMapper.FKEY_ENTITY, EnmEntityType.getEntityType(messageThread.getEntityType()));
		}
		
		sqlBuilder.append(messageThreadRowMapper.getSelectSqlWithForeignKeys());
		
		if (messageThread != null) {
			if (messageThread.getId() != null) {
				sqlBuilder.append(" AND " + messageThreadRowMapper.getCriteriaColumnName(MessageThreadRowMapper.COL_ID) + " = ? ");
				argList.add(messageThread.getId());
			}
			if (messageThread.getHostUserId() != null) {
				sqlBuilder.append(" AND " + messageThreadRowMapper.getCriteriaColumnName(MessageThreadRowMapper.COL_HOST_USER_ID) + " = ? ");
				argList.add(messageThread.getHostUserId());
			}
			if (messageThread.getClientUserId() != null) {
				sqlBuilder.append(" AND " + messageThreadRowMapper.getCriteriaColumnName(MessageThreadRowMapper.COL_CLIENT_USER_ID) + " = ? ");
				argList.add(messageThread.getClientUserId());
			}
			if (messageThread.getEntityType() != null) {
				sqlBuilder.append(" AND " + messageThreadRowMapper.getCriteriaColumnName(MessageThreadRowMapper.COL_ENTITY_TYPE) + " = ? ");
				argList.add(messageThread.getEntityType());
			}
			if (messageThread.getEntityId() != null) {
				sqlBuilder.append(" AND " + messageThreadRowMapper.getCriteriaColumnName(MessageThreadRowMapper.COL_ENTITY_ID) + " = ? ");
				argList.add(messageThread.getEntityId());
			}
		}
		
		sqlBuilder.append(" ORDER BY " + messageThreadRowMapper.getCriteriaColumnName(MessageThreadRowMapper.COL_MODIFIED_DATE) + " DESC");
		
		List<MessageThread> messageThreadList = super.getJdbcTemplate().query(sqlBuilder.toString(), argList.toArray(), messageThreadRowMapper);
		if (lastMessageFlag) {
			for (MessageThread dbMessageThread : messageThreadList) {
				Message lastMessage = this.getLastMessage(dbMessageThread.getId());
				dbMessageThread.addMessage(lastMessage);
			}
		}
				
		return messageThreadList;
	}

	@Override
	public MessageThread getMessageThread(MessageThread messageThread, boolean entityFlag, boolean hostUserFlag,
			boolean clientUserFlag) {
		
		List<MessageThread> messageThreadList = this.listMessageThread(messageThread, entityFlag, hostUserFlag, clientUserFlag, false);		
		if (messageThreadList != null && messageThreadList.size() > 0) {
			return messageThreadList.get(0);
		} else {
			return null;
		}
	}

	@Override
	public Message getLastMessage(Long messageThreadId) {
		List<Message> messageList = this.getJdbcTemplate().query(GET_LAST_MESSAGE_SQL, new Object[] { messageThreadId }, new MessageRowMapper(null));
		if (messageList != null && messageList.size() > 0) {
			return messageList.get(0);
		} else {
			return null;
		}
	}

	
	
}
