package com.ucgen.letserasmus.library.payment.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import com.ucgen.common.dao.UtilityDao;
import com.ucgen.common.operationresult.EnmResultCode;
import com.ucgen.common.operationresult.OperationResult;
import com.ucgen.letserasmus.library.payment.dao.IPaymentDao;
import com.ucgen.letserasmus.library.payment.dao.IPaymentDaoConstants;
import com.ucgen.letserasmus.library.payment.dao.PaymentMethodRowMapper;
import com.ucgen.letserasmus.library.payment.dao.PayoutMethodRowMapper;
import com.ucgen.letserasmus.library.payment.model.PaymentMethod;
import com.ucgen.letserasmus.library.payment.model.PayoutMethod;

@Repository
public class PaymentDao extends JdbcDaoSupport implements IPaymentDao, IPaymentDaoConstants {

	private UtilityDao utilityDao;
	
	@Autowired
	public PaymentDao(DataSource dataSource) {
		super();
		this.setDataSource(dataSource);
	}
	
	@Autowired
	public void setUtilityDao(UtilityDao utilityDao) {
		this.utilityDao = utilityDao;
	}

	@Override
	public OperationResult insertPaymentMethod(PaymentMethod paymentMethod) {
		OperationResult operationResult = new OperationResult();
		
		List<Object> argList = new ArrayList<Object>();
				
		argList.add(paymentMethod.getUserId());
		argList.add(paymentMethod.getCardNumber());
		argList.add(paymentMethod.getCardType());
		argList.add(paymentMethod.getExpDate());
		argList.add(paymentMethod.getBlueSnapId());
		argList.add(paymentMethod.getCreatedBy());
		argList.add(paymentMethod.getCreatedDate());
		
		this.getJdbcTemplate().update(INSERT_PAYMENT_METHOD_SQL, argList.toArray());
		
		paymentMethod.setId(this.utilityDao.getLastIncrementId());
				
		operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
						
		return operationResult;
	}

	@Override
	public PaymentMethod getPaymentMethod(PaymentMethod paymentMethod) {
		List<PaymentMethod> paymentMethodList = this.listPaymentMethod(paymentMethod);
		if (paymentMethodList != null && paymentMethodList.size() > 0) {
			return paymentMethodList.get(0);
		} else {
			return null;
		}
	}

	@Override
	public List<PaymentMethod> listPaymentMethod(PaymentMethod paymentMethod) {
		StringBuilder sqlBuilder = new StringBuilder(LIST_PAYMENT_METHOD_SQL);
		List<Object> argList = new ArrayList<Object>();
		
		PaymentMethodRowMapper paymentMethodRowMapper = new PaymentMethodRowMapper(null);
		
		if (paymentMethod != null) {
			if (paymentMethod.getId() != null) {
				sqlBuilder.append(" AND " + paymentMethodRowMapper.getCriteriaColumnName(PaymentMethodRowMapper.COL_ID) + " = ? ");
				argList.add(paymentMethod.getId());
			}
			if (paymentMethod.getUserId() != null) {
				sqlBuilder.append(" AND " + paymentMethodRowMapper.getCriteriaColumnName(PaymentMethodRowMapper.COL_USER_ID) + " = ? ");
				argList.add(paymentMethod.getUserId());
			}
			if (paymentMethod.getBlueSnapId() != null) {
				sqlBuilder.append(" AND " + paymentMethodRowMapper.getCriteriaColumnName(PaymentMethodRowMapper.COL_BLUESNAP_ID) + " = ? ");
				argList.add(paymentMethod.getBlueSnapId());
			}
			
		}
				
		List<PaymentMethod> paymentMethodList = super.getJdbcTemplate().query(sqlBuilder.toString(), argList.toArray(), paymentMethodRowMapper);		
				
		return paymentMethodList;
	}

	@Override
	public List<PayoutMethod> listPayoutMethod(PayoutMethod payoutMethod) {
		StringBuilder sqlBuilder = new StringBuilder(LIST_PAYOUT_METHOD_SQL);
		List<Object> argList = new ArrayList<Object>();
		
		PayoutMethodRowMapper payoutMethodRowMapper = new PayoutMethodRowMapper(null);
		
		if (payoutMethod != null) {
			if (payoutMethod.getId() != null) {
				sqlBuilder.append(" AND " + payoutMethodRowMapper.getCriteriaColumnName(PayoutMethodRowMapper.COL_ID) + " = ? ");
				argList.add(payoutMethod.getId());
			}
			if (payoutMethod.getUserId() != null) {
				sqlBuilder.append(" AND " + payoutMethodRowMapper.getCriteriaColumnName(PayoutMethodRowMapper.COL_USER_ID) + " = ? ");
				argList.add(payoutMethod.getUserId());
			}
			if (payoutMethod.getBlueSnapVendorId() != null) {
				sqlBuilder.append(" AND " + payoutMethodRowMapper.getCriteriaColumnName(PayoutMethodRowMapper.COL_BLUESNAP_VENDOR_ID) + " = ? ");
				argList.add(payoutMethod.getBlueSnapVendorId());
			}
			
		}
				
		List<PayoutMethod> payoutMethodList = super.getJdbcTemplate().query(sqlBuilder.toString(), argList.toArray(), payoutMethodRowMapper);		
				
		return payoutMethodList;
	}

	@Override
	public PayoutMethod getPayoutMethod(PayoutMethod payoutMethod) {
		List<PayoutMethod> payoutMethodList = this.listPayoutMethod(payoutMethod);
		if (payoutMethodList != null && payoutMethodList.size() > 0) {
			return payoutMethodList.get(0);
		} else {
			return null;
		}
	}

	@Override
	public OperationResult insertPayoutMethod(PayoutMethod payoutMethod) {
OperationResult operationResult = new OperationResult();
		
		List<Object> argList = new ArrayList<Object>();
				
		argList.add(payoutMethod.getUserId());
		argList.add(payoutMethod.getEmail());
		argList.add(payoutMethod.getBlueSnapCountryCode());
		argList.add(payoutMethod.getBlueSnapVendorId());
		
		argList.add(payoutMethod.getCreatedBy());
		argList.add(payoutMethod.getCreatedDate());
		
		this.getJdbcTemplate().update(INSERT_PAYOUT_METHOD_SQL, argList.toArray());
		
		payoutMethod.setId(this.utilityDao.getLastIncrementId());
				
		operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
						
		return operationResult;
	}
	
}
