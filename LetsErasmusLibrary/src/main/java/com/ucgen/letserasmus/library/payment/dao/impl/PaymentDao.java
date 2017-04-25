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
import com.ucgen.common.util.StringUtil;
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
		argList.add(payoutMethod.getVendorEmail());
		argList.add(payoutMethod.getBankCountry());
		argList.add(payoutMethod.getBlueSnapVendorId());
		
		argList.add(payoutMethod.getCreatedBy());
		argList.add(payoutMethod.getCreatedDate());
		
		this.getJdbcTemplate().update(INSERT_PAYOUT_METHOD_SQL, argList.toArray());
		
		payoutMethod.setId(this.utilityDao.getLastIncrementId());
				
		operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
						
		return operationResult;
	}

	@Override
	public OperationResult updatePayoutMethod(PayoutMethod payoutMethod) {
		OperationResult operationResult = new OperationResult();		
		List<Object> argList = new ArrayList<Object>();
		
		String updateSql = new String(UPDATE_PAYOUT_METHOD_SQL);
		StringBuilder updateFields = new StringBuilder();
						
		if (payoutMethod.getVendorNationalId() != null) {
			StringUtil.append(updateFields, PayoutMethodRowMapper.COL_VENDOR_NATIONAL_ID + " = ?", ",");
			argList.add(payoutMethod.getVendorNationalId());
		}
		
		if (payoutMethod.getVendorTaxId() != null) {
			StringUtil.append(updateFields, PayoutMethodRowMapper.COL_VENDOR_TAXID + " = ?", ",");
			argList.add(payoutMethod.getVendorTaxId());
		}
		
		if (payoutMethod.getVendorFirstName() != null) {
			StringUtil.append(updateFields, PayoutMethodRowMapper.COL_VENDOR_FIRST_NAME + " = ?", ",");
			argList.add(payoutMethod.getVendorFirstName());
		}
		
		if (payoutMethod.getVendorLastName() != null) {
			StringUtil.append(updateFields, PayoutMethodRowMapper.COL_VENDOR_LAST_NAME + " = ?", ",");
			argList.add(payoutMethod.getVendorLastName());
		}
		
		if (payoutMethod.getVendorEmail() != null) {
			StringUtil.append(updateFields, PayoutMethodRowMapper.COL_VENDOR_EMAIL + " = ?", ",");
			argList.add(payoutMethod.getVendorEmail());
		}
		
		if (payoutMethod.getVendorBirthDate() != null) {
			StringUtil.append(updateFields, PayoutMethodRowMapper.COL_VENDOR_BIRTH_DATE + " = ?", ",");
			argList.add(payoutMethod.getVendorBirthDate());
		}
		
		if (payoutMethod.getVendorAddress() != null) {
			StringUtil.append(updateFields, PayoutMethodRowMapper.COL_VENDOR_ADDRESS + " = ?", ",");
			argList.add(payoutMethod.getVendorAddress());
		}
		
		if (payoutMethod.getVendorAddress2() != null) {
			StringUtil.append(updateFields, PayoutMethodRowMapper.COL_VENDOR_ADDRESS2 + " = ?", ",");
			argList.add(payoutMethod.getVendorAddress2());
		}
		
		if (payoutMethod.getVendorCountry() != null) {
			StringUtil.append(updateFields, PayoutMethodRowMapper.COL_VENDOR_COUNTRY + " = ?", ",");
			argList.add(payoutMethod.getVendorCountry());
		}
		
		if (payoutMethod.getVendorCity() != null) {
			StringUtil.append(updateFields, PayoutMethodRowMapper.COL_VENDOR_CITY + " = ?", ",");
			argList.add(payoutMethod.getVendorCity());
		}
		
		if (payoutMethod.getVendorState() != null) {
			StringUtil.append(updateFields, PayoutMethodRowMapper.COL_VENDOR_STATE + " = ?", ",");
			argList.add(payoutMethod.getVendorState());
		}
		
		if (payoutMethod.getVendorZip() != null) {
			StringUtil.append(updateFields, PayoutMethodRowMapper.COL_VENDOR_ZIP + " = ?", ",");
			argList.add(payoutMethod.getVendorZip());
		}
		
		if (payoutMethod.getBankAccountId() != null) {
			StringUtil.append(updateFields, PayoutMethodRowMapper.COL_BANK_ACCOUNT_ID + " = ?", ",");
			argList.add(payoutMethod.getBankAccountId());
		}
		
		if (payoutMethod.getBankAccountIban() != null) {
			StringUtil.append(updateFields, PayoutMethodRowMapper.COL_BANK_ACCOUNT_IBAN + " = ?", ",");
			argList.add(payoutMethod.getBankAccountIban());
		}
		
		if (payoutMethod.getBankAccountClass() != null) {
			StringUtil.append(updateFields, PayoutMethodRowMapper.COL_BANK_ACCOUNT_CLASS + " = ?", ",");
			argList.add(payoutMethod.getBankAccountClass());
		}
		
		if (payoutMethod.getBankAccountType() != null) {
			StringUtil.append(updateFields, PayoutMethodRowMapper.COL_BANK_ACCOUNT_TYPE + " = ?", ",");
			argList.add(payoutMethod.getBankAccountType());
		}
		
		if (payoutMethod.getBankAccountHolderName() != null) {
			StringUtil.append(updateFields, PayoutMethodRowMapper.COL_BANK_ACCOUNT_HOLDER_NAME + " = ?", ",");
			argList.add(payoutMethod.getBankAccountHolderName());
		}
		
		if (payoutMethod.getBankId() != null) {
			StringUtil.append(updateFields, PayoutMethodRowMapper.COL_BANK_ID + " = ?", ",");
			argList.add(payoutMethod.getBankId());
		}
		
		if (payoutMethod.getBankName() != null) {
			StringUtil.append(updateFields, PayoutMethodRowMapper.COL_BANK_NAME + " = ?", ",");
			argList.add(payoutMethod.getBankName());
		}
		
		if (payoutMethod.getBankSwiftBic() != null) {
			StringUtil.append(updateFields, PayoutMethodRowMapper.COL_BANK_SWIFT_BIC + " = ?", ",");
			argList.add(payoutMethod.getBankSwiftBic());
		}
		
		if (payoutMethod.getBankCountry() != null) {
			StringUtil.append(updateFields, PayoutMethodRowMapper.COL_BANK_COUNTRY + " = ?", ",");
			argList.add(payoutMethod.getBankCountry());
		}
		
		if (payoutMethod.getBankState() != null) {
			StringUtil.append(updateFields, PayoutMethodRowMapper.COL_BANK_STATE + " = ?", ",");
			argList.add(payoutMethod.getBankState());
		}
		
		if (payoutMethod.getModifiedBy() != null) {
			StringUtil.append(updateFields, "MODIFIED_BY = ?", ",");
			argList.add(payoutMethod.getModifiedBy());
		}
		
		if (payoutMethod.getModifiedDate() != null) {
			StringUtil.append(updateFields, "MODIFIED_DATE = ?", ",");
			argList.add(payoutMethod.getModifiedDate());
		}
		
		argList.add(payoutMethod.getId());

		updateSql = updateSql.replace("$1", updateFields);
		this.getJdbcTemplate().update(updateSql, argList.toArray() );
		
		operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
		
		return operationResult;
	}
	
}
