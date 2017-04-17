package com.ucgen.letserasmus.library.reservation.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import com.ucgen.common.dao.UtilityDao;
import com.ucgen.common.operationresult.EnmResultCode;
import com.ucgen.common.operationresult.OperationResult;
import com.ucgen.common.operationresult.ValueOperationResult;
import com.ucgen.common.util.StringUtil;
import com.ucgen.letserasmus.library.reservation.dao.IReservationDao;
import com.ucgen.letserasmus.library.reservation.dao.ReservationRowMapper;
import com.ucgen.letserasmus.library.reservation.model.Reservation;

@Repository
public class ReservationDao extends JdbcDaoSupport implements IReservationDao {

	private static final String INSERT_RESERVATION_SQL = "INSERT INTO RESERVATION (PLACE_ID, HOST_USER_ID, CLIENT_USER_ID, START_DATE, "
			+ " END_DATE, GUEST_NUMBER, PLACE_PRICE, SERVICE_RATE, SERVICE_FEE, COMMISSION_RATE, COMMISSION_FEE, CURRENCY_ID, STATUS,"
			+ " CREATED_BY, CREATED_DATE, MESSAGE_THREAD_ID, TRANSACTION_ID, BLUESNAP_TRANSACTION_ID, PAYMENT_STATUS, CANCELLATION_POLICY_ID)" 
			+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";	
	
	private static final String UPDATE_RESERVATION_SQL = " UPDATE RESERVATION SET $1 WHERE ID=? ";
	
	private UtilityDao utilityDao;
	
	@Autowired
	public void setUtilityDao(UtilityDao utilityDao) {
		this.utilityDao = utilityDao;
	}
	
	@Autowired
	public ReservationDao(DataSource dataSource) {
		super();
		super.setDataSource(dataSource);
	}
	
	@Override
	public OperationResult insert(Reservation reservation) {
		OperationResult operationResult = new OperationResult();
		
		List<Object> argList = new ArrayList<Object>();
		
		argList.add(reservation.getPlaceId());
		argList.add(reservation.getHostUserId());
		argList.add(reservation.getClientUserId());
		argList.add(reservation.getStartDate());
		argList.add(reservation.getEndDate());
		argList.add(reservation.getGuestNumber());
		argList.add(reservation.getPlacePrice());
		argList.add(reservation.getServiceRate());
		argList.add(reservation.getServiceFee());
		argList.add(reservation.getCommissionRate());
		argList.add(reservation.getCommissionFee());
		argList.add(reservation.getCurrencyId());
		argList.add(reservation.getStatus());
		argList.add(reservation.getCreatedBy());
		argList.add(reservation.getCreatedDate());
		argList.add(reservation.getMessageThreadId());
		argList.add(reservation.getTransactionId());
		argList.add(reservation.getBlueSnapTransactionId());
		argList.add(reservation.getPaymentStatus());
		argList.add(reservation.getCancellationPolicyId());
		
		this.getJdbcTemplate().update(INSERT_RESERVATION_SQL, argList.toArray());
		
		reservation.setId(this.utilityDao.getLastIncrementId());
		
		operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
						
		return operationResult;
	}

	@Override
	public List<Reservation> list(Reservation reservation, boolean placeFlag, boolean hostUserFlag, boolean clientUserFlag) {
		StringBuilder sqlBuilder = new StringBuilder();
		List<Object> argList = new ArrayList<Object>();
		
		ReservationRowMapper reservationRowMapper = new ReservationRowMapper();
		
		if (placeFlag) {
			reservationRowMapper.addFKey(ReservationRowMapper.FKEY_PLACE);
		}
		if (hostUserFlag) {
			reservationRowMapper.addFKey(ReservationRowMapper.FKEY_USER_HOST);
		}
		if (clientUserFlag) {
			reservationRowMapper.addFKey(ReservationRowMapper.FKEY_USER_CLIENT);
		}
		
		sqlBuilder.append(reservationRowMapper.getSelectSqlWithForeignKeys());
		
		if (reservation != null) {
			if (reservation.getId() != null) {
				sqlBuilder.append(" AND " + reservationRowMapper.getCriteriaColumnName(ReservationRowMapper.COL_ID) + " = ? ");
				argList.add(reservation.getId());
			}
			if (reservation.getHostUserId() != null) {
				sqlBuilder.append(" AND " + reservationRowMapper.getCriteriaColumnName(ReservationRowMapper.COL_HOST_USER_ID) + " = ? ");
				argList.add(reservation.getHostUserId());
			}
			if (reservation.getClientUserId() != null) {
				sqlBuilder.append(" AND " + reservationRowMapper.getCriteriaColumnName(ReservationRowMapper.COL_CLIENT_USER_ID) + " = ? ");
				argList.add(reservation.getClientUserId());
			}
			if (reservation.getStatus() != null) {
				sqlBuilder.append(" AND " + reservationRowMapper.getCriteriaColumnName(ReservationRowMapper.COL_STATUS) + " = ? ");
				argList.add(reservation.getStatus());
			}
			if (reservation.getMessageThreadId() != null) {
				sqlBuilder.append(" AND " + reservationRowMapper.getCriteriaColumnName(ReservationRowMapper.COL_MESSAGE_THREAD_ID) + " = ? ");
				argList.add(reservation.getMessageThreadId());
			}
			if (reservation.getPlaceId() != null) {
				sqlBuilder.append(" AND " + reservationRowMapper.getCriteriaColumnName(ReservationRowMapper.COL_PLACE_ID) + " = ? ");
				argList.add(reservation.getPlaceId());
			}
		}
				
		List<Reservation> reservationList = super.getJdbcTemplate().query(sqlBuilder.toString(), argList.toArray(), reservationRowMapper);		
				
		return reservationList;
	}

	@Override
	public OperationResult update(Reservation reservation) {
		ValueOperationResult<Integer> operationResult = new ValueOperationResult<Integer>();		
		List<Object> argList = new ArrayList<Object>();
		
		String updateSql = new String(UPDATE_RESERVATION_SQL);
		StringBuilder updateFields = new StringBuilder();
		
		if (reservation.getStatus() != null) {
			StringUtil.append(updateFields, "STATUS = ?", ",");
			argList.add(reservation.getStatus());
		}
		
		if (reservation.getClientReviewId() != null) {
			StringUtil.append(updateFields, "CLIENT_REVIEW_ID = ?", ",");
			argList.add(reservation.getClientReviewId());
		}
		
		if (reservation.getHostReviewId() != null) {
			StringUtil.append(updateFields, "HOST_REVIEW_ID = ?", ",");
			argList.add(reservation.getHostReviewId());
		}
		
		if (reservation.getBlueSnapTransactionId() != null) {
			StringUtil.append(updateFields, "BLUESNAP_TRANSACTION_ID = ?", ",");
			argList.add(reservation.getBlueSnapTransactionId());
		}
		
		if (reservation.getPaymentStatus() != null) {
			StringUtil.append(updateFields, "PAYMENT_STATUS = ?", ",");
			argList.add(reservation.getPaymentStatus());
		}
		
		if (reservation.getCancellationPolicyId() != null) {
			StringUtil.append(updateFields, "CANCELLATION_POLICY_ID = ?", ",");
			argList.add(reservation.getCancellationPolicyId());
		}
		
		if (reservation.getModifiedBy() != null) {
			StringUtil.append(updateFields, "MODIFIED_BY = ?", ",");
			argList.add(reservation.getModifiedBy());
		}
		
		StringUtil.append(updateFields, "MODIFIED_DATE = ?", ",");
		
		if (reservation.getModifiedDate() != null) {
			argList.add(reservation.getModifiedDate());
		} else {
			argList.add(new Date());
		}
		
		argList.add(reservation.getId());

		updateSql = updateSql.replace("$1", updateFields);
		int updatedRowCount =  this.getJdbcTemplate().update(updateSql, argList.toArray() );
		
		operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
		operationResult.setResultValue(updatedRowCount);
		
		return operationResult;
	}

}
