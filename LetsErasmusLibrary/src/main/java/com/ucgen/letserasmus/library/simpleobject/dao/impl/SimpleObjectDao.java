package com.ucgen.letserasmus.library.simpleobject.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import com.ucgen.common.operationresult.EnmResultCode;
import com.ucgen.common.operationresult.OperationResult;
import com.ucgen.letserasmus.library.common.enumeration.EnmErrorCode;
import com.ucgen.letserasmus.library.simpleobject.dao.CancelPolicyRuleRowMapper;
import com.ucgen.letserasmus.library.simpleobject.dao.CountryRowMapper;
import com.ucgen.letserasmus.library.simpleobject.dao.ISimpleDaoConstants;
import com.ucgen.letserasmus.library.simpleobject.dao.ISimpleObjectDao;
import com.ucgen.letserasmus.library.simpleobject.dao.QuestionGroupRowMapper;
import com.ucgen.letserasmus.library.simpleobject.dao.QuestionRowMapper;
import com.ucgen.letserasmus.library.simpleobject.model.CancelPolicyRule;
import com.ucgen.letserasmus.library.simpleobject.model.Country;
import com.ucgen.letserasmus.library.simpleobject.model.Question;
import com.ucgen.letserasmus.library.simpleobject.model.QuestionGroup;

@Repository
public class SimpleObjectDao extends JdbcDaoSupport implements ISimpleObjectDao, ISimpleDaoConstants {
	
	private List<Country> countryList;
	private Map<String, Country> countryMapIso2;
	private Map<Integer, Map<Integer, TreeMap<Integer, CancelPolicyRule>>> cancelPolicyRuleMap;
	
	@Autowired
	public SimpleObjectDao(DataSource dataSource) {
		super();
		super.setDataSource(dataSource);
	}
	
	@Override
	public synchronized List<Country> listCountry() {
		if (countryList == null) {
			this.countryList = this.getJdbcTemplate().query(LIST_COUNTRY_SQL, new CountryRowMapper());
			countryMapIso2 = new HashMap<String, Country>();
			for (Country country : countryList) {
				countryMapIso2.put(country.getIsoCodeTwoDigit().toUpperCase(), country);
			}
		}
		return this.countryList;
	}
	
	@Override
	public Country getCountryWithIsoCode2(String isoCode2) {
		if (isoCode2 != null) {
			if (this.countryList == null) {
				this.listCountry();
			}
			return this.countryMapIso2.get(isoCode2.toUpperCase());
		} else {
			return null;
		}
	}

	@Override
	public List<QuestionGroup> listQuestionGroup() {
		List<QuestionGroup> questionGroupList = this.getJdbcTemplate().query(LIST_QUESTION_GROUP_SQL, new QuestionGroupRowMapper());
		for(int i = 1; i <= questionGroupList.size(); i++) {
			questionGroupList.get(i - 1).setId(i);
		}
		return questionGroupList;
	}

	@Override
	public List<Question> listQuestion(String groupTitle, String searchText) {
		StringBuilder listQuestionBuilder = new StringBuilder(LIST_QUESTION_SQL);
		List<Object> argList = new ArrayList<Object>();
		
		if (groupTitle != null && groupTitle.trim().length() > 0) {
			if (groupTitle.equalsIgnoreCase("FAQ")) {
				listQuestionBuilder.append(" AND FAQ_STATUS = ? ");
				argList.add("Y");
			} else {
				listQuestionBuilder.append(" AND Q_GROUP = ? ");
				argList.add(groupTitle);
			}
		}
		
		if (searchText != null && searchText.trim().length() > 0) {
			searchText = "%" + searchText + "%";
			listQuestionBuilder.append(" AND (LOWER(QUESTION) LIKE LOWER(?) OR LOWER(ANSWER) LIKE LOWER(?) )");
			argList.add(searchText);
			argList.add(searchText);
		}
		return this.getJdbcTemplate().query(listQuestionBuilder.toString(), argList.toArray(), new QuestionRowMapper());
	}

	@Override
	public void updateCountry(List<String> countryList) {
		for (String string : countryList) {
			String[] countryParts = string.split(",");
			this.getJdbcTemplate().update("UPDATE COUNTRY SET BLUESNAP_COUNTRY_CODE = ? WHERE UPPER(NAME) = UPPER(?)", 
					new Object[] { countryParts[0], countryParts[1] });
		}
		
	}

	@Override
	public OperationResult insertTransactionId(String transactionId) {
		OperationResult operationResult = new OperationResult();
		Integer count = this.getJdbcTemplate().queryForObject(GET_TRANSACTION_ID_SQL, new Object[] { transactionId }, Integer.class);
		if (count == 0) {
			this.getJdbcTemplate().update(INSERT_TRANSACTION_ID_SQL, new Object[] { transactionId });
			operationResult.setResultCode(EnmResultCode.SUCCESS.getValue());
		} else {
			operationResult.setResultCode(EnmResultCode.ERROR.getValue());
			operationResult.setErrorCode(EnmErrorCode.ALREADY_EXIST.getId());
		}
		return operationResult;
	}

	@Override
	public TreeMap<Integer, CancelPolicyRule> listCancelPolicyRule(Integer entityType, Integer policyId) {
		if (this.cancelPolicyRuleMap == null) {
			this.refreshCancelPolicyMap();
		}
		return this.cancelPolicyRuleMap.get(entityType).get(policyId);
	}
	
	private synchronized void refreshCancelPolicyMap() {
		if (this.cancelPolicyRuleMap == null) {
			List<CancelPolicyRule> cancelPlicyRuleList = this.getJdbcTemplate().query(LIST_CANCEL_POLICY_RULE_SQL, new CancelPolicyRuleRowMapper());
			this.cancelPolicyRuleMap = new HashMap<Integer, Map<Integer, TreeMap<Integer, CancelPolicyRule>>>();
			for (CancelPolicyRule cancelPolicyRule : cancelPlicyRuleList) {
				if (!this.cancelPolicyRuleMap.containsKey(cancelPolicyRule.getEntityType())) {	
					Map<Integer, TreeMap<Integer, CancelPolicyRule>> policyIdMap = new HashMap<Integer, TreeMap<Integer, CancelPolicyRule>>();					
					this.cancelPolicyRuleMap.put(cancelPolicyRule.getEntityType(), policyIdMap);
				} 
				
				if (!this.cancelPolicyRuleMap.get(cancelPolicyRule.getEntityType()).containsKey(cancelPolicyRule.getPolicyId())) {
					TreeMap<Integer, CancelPolicyRule> entityCancelPlicyRuleMap = new TreeMap<Integer, CancelPolicyRule>();
					this.cancelPolicyRuleMap.get(cancelPolicyRule.getEntityType()).put(cancelPolicyRule.getPolicyId(), entityCancelPlicyRuleMap);
				}
				
				this.cancelPolicyRuleMap.get(cancelPolicyRule.getEntityType()).get(cancelPolicyRule.getPolicyId()).put(cancelPolicyRule.getRemainingDays(), cancelPolicyRule);
			}
		}
	}

}
