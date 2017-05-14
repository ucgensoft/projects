package com.ucgen.letserasmus.library.simpleobject.model;

import java.math.BigDecimal;

import com.ucgen.letserasmus.library.common.model.BaseModel;

public class CancelPolicyRule extends BaseModel {

	private static final long serialVersionUID = -2115410669614507565L;

	private Integer entityType;
	private Integer policyId;
	private Integer remainingDays;
	private BigDecimal refundRate;
	
	public Integer getEntityType() {
		return entityType;
	}
	public void setEntityType(Integer entityType) {
		this.entityType = entityType;
	}
	public Integer getPolicyId() {
		return policyId;
	}
	public void setPolicyId(Integer policyId) {
		this.policyId = policyId;
	}
	public Integer getRemainingDays() {
		return remainingDays;
	}
	public void setRemainingDays(Integer remainingDays) {
		this.remainingDays = remainingDays;
	}
	public BigDecimal getRefundRate() {
		if (this.refundRate != null) {
			return refundRate.divide(new BigDecimal(100));
		} else {
			return null;
		}
	}
	public void setRefundRate(BigDecimal refundRate) {
		this.refundRate = refundRate;
	}
	
}
