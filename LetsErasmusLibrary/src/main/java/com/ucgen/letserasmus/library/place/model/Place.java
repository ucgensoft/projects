package com.ucgen.letserasmus.library.place.model;

import java.math.BigDecimal;
import java.util.Date;

public class Place {
                
                private Long id;
                private Long hostUserId;
                private int placeTypeId;
                private int homeTypeId;
                private String title;
                private String description;
                private int status;
                private Long locationId;
                private String phone;
                private BigDecimal price;
                private String billsInclude;
                private BigDecimal depositPrice;
                private Long currencyId;
                private int bedNumber;
                private int bedTypeId;
                private int bathRoomNumber;
                private int bathRoomNumberType;
                private int placeMateNumber;
                private int placeMateGender;
                private int guestNumber;
                private int guestGender;
                private String rules;
                private String amenties;
                private String safetyAmenties;
                private int minumumStay;
                private int maximumStay;           
                private Date startDate;
                private Date endDate;
                private Long cancellationPolicyId;           
                private String createdBy;
                private Date createdDate;
                private Date createdDateGmt;
                private String modifiedBy;
                private Date modifiedDate;
                private Date modifiedDateGmt;
                
                public Long getId() {
                               return id;
                }
                public void setId(Long id) {
                               this.id = id;
                }
                public Long getHostUserId() {
                               return hostUserId;
                }
                public void setHostUserId(Long hostUserId) {
                               this.hostUserId = hostUserId;
                }
                public int getPlaceTypeId() {
                               return placeTypeId;
                }
                public void setPlaceTypeId(int placeTypeId) {
                               this.placeTypeId = placeTypeId;
                }
                public int getHomeTypeId() {
                               return homeTypeId;
                }
                public void setHomeTypeId(int homeTypeId) {
                               this.homeTypeId = homeTypeId;
                }
                public String getTitle() {
                               return title;
                }
                public void setTitle(String title) {
                               this.title = title;
                }
                public String getDescription() {
                               return description;
                }
                public void setDescription(String description) {
                               this.description = description;
                }
                public int getStatus() {
                               return status;
                }
                public void setStatus(int status) {
                               this.status = status;
                }
                public Long getLocationId() {
                               return locationId;
                }
                public void setLocationId(Long locationId) {
                               this.locationId = locationId;
                }
                public String getPhone() {
                               return phone;
                }
                public void setPhone(String phone) {
                               this.phone = phone;
                }
                public BigDecimal getPrice() {
                               return price;
                }
                public void setPrice(BigDecimal price) {
                               this.price = price;
                }
                public String getBillsInclude() {
                               return billsInclude;
                }
                public void setBillsInclude(String billsInclude) {
                               this.billsInclude = billsInclude;
                }
                public BigDecimal getDepositPrice() {
                               return depositPrice;
                }
                public void setDepositPrice(BigDecimal depositPrice) {
                               this.depositPrice = depositPrice;
                }
                public Long getCurrencyId() {
                               return currencyId;
                }
                public void setCurrencyId(Long currencyId) {
                               this.currencyId = currencyId;
                }
                public int getBedNumber() {
                               return bedNumber;
                }
                public void setBedNumber(int bedNumber) {
                               this.bedNumber = bedNumber;
                }
                public int getBedTypeId() {
                               return bedTypeId;
                }
                public void setBedTypeId(int bedTypeId) {
                               this.bedTypeId = bedTypeId;
                }
                public int getBathRoomNumber() {
                               return bathRoomNumber;
                }
                public void setBathRoomNumber(int bathRoomNumber) {
                               this.bathRoomNumber = bathRoomNumber;
                }
                public int getBathRoomNumberType() {
                               return bathRoomNumberType;
                }
                public void setBathRoomNumberType(int bathRoomNumberType) {
                               this.bathRoomNumberType = bathRoomNumberType;
                }
                public int getPlaceMateNumber() {
                               return placeMateNumber;
                }
                public void setPlaceMateNumber(int placeMateNumber) {
                               this.placeMateNumber = placeMateNumber;
                }
                public int getPlaceMateGender() {
                               return placeMateGender;
                }
                public void setPlaceMateGender(int placeMateGender) {
                               this.placeMateGender = placeMateGender;
                }
                public int getGuestNumber() {
                               return guestNumber;
                }
                public void setGuestNumber(int guestNumber) {
                               this.guestNumber = guestNumber;
                }
                public int getGuestGender() {
                               return guestGender;
                }
                public void setGuestGender(int guestGender) {
                               this.guestGender = guestGender;
                }
                public String getRules() {
                               return rules;
                }
                public void setRules(String rules) {
                               this.rules = rules;
                }
                public String getAmenties() {
                               return amenties;
                }
                public void setAmenties(String amenties) {
                               this.amenties = amenties;
                }
                public String getSafetyAmenties() {
                               return safetyAmenties;
                }
                public void setSafetyAmenties(String safetyAmenties) {
                               this.safetyAmenties = safetyAmenties;
                }
                public int getMinumumStay() {
                               return minumumStay;
                }
                public void setMinumumStay(int minumumStay) {
                               this.minumumStay = minumumStay;
                }
                public int getMaximumStay() {
                               return maximumStay;
                }
                public void setMaximumStay(int maximumStay) {
                               this.maximumStay = maximumStay;
                }
                public Date getStartDate() {
                               return startDate;
                }
                public void setStartDate(Date startDate) {
                               this.startDate = startDate;
                }
                public Date getEndDate() {
                               return endDate;
                }
                public void setEndDate(Date endDate) {
                               this.endDate = endDate;
                }
                public Long getCancellationPolicyId() {
                               return cancellationPolicyId;
                }
                public void setCancellationPolicyId(Long cancellationPolicyId) {
                               this.cancellationPolicyId = cancellationPolicyId;
                }
                public String getCreatedBy() {
                               return createdBy;
                }
                public void setCreatedBy(String createdBy) {
                               this.createdBy = createdBy;
                }
                public Date getCreatedDate() {
                               return createdDate;
                }
                public void setCreatedDate(Date createdDate) {
                               this.createdDate = createdDate;
                }
                public Date getCreatedDateGmt() {
                               return createdDateGmt;
                }
                public void setCreatedDateGmt(Date createdDateGmt) {
                               this.createdDateGmt = createdDateGmt;
                }
                public String getModifiedBy() {
                               return modifiedBy;
                }
                public void setModifiedBy(String modifiedBy) {
                               this.modifiedBy = modifiedBy;
                }
                public Date getModifiedDate() {
                               return modifiedDate;
                }
                public void setModifiedDate(Date modifiedDate) {
                               this.modifiedDate = modifiedDate;
                }
                public Date getModifiedDateGmt() {
                               return modifiedDateGmt;
                }
                public void setModifiedDateGmt(Date modifiedDateGmt) {
                               this.modifiedDateGmt = modifiedDateGmt;
                }              
                
                
                

}
