package com.ucgen.letserasmus.library.place.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ucgen.letserasmus.library.common.model.BaseModel;
import com.ucgen.letserasmus.library.file.model.File;
import com.ucgen.letserasmus.library.location.model.Location;
import com.ucgen.letserasmus.library.user.model.User;

public class Place extends BaseModel {

	private static final long serialVersionUID = -4557066892117998451L;

	private Long id;
	private Long hostUserId;
	private Integer placeTypeId;
	private Integer homeTypeId;
	private String title;
	private String description;
	private Integer status;
	private Long locationId;
	private BigDecimal price;
	private String billsInclude;
	private BigDecimal depositPrice;
	private Integer currencyId;
	private Integer bedNumber;
	private Integer bedTypeId;
	private Integer bathRoomNumber;
	private Integer bathRoomType;
	private Integer placeMateNumber;
	private Integer placeMateGender;
	private Integer guestNumber;
	private Integer guestGender;
	private String rules;
	private String amenties;
	private String safetyAmenties;
	private Integer minimumStay;
	private Integer maximumStay;
	private Date startDate;
	private Date endDate;
	private Long cancellationPolicyId;
	private Long coverPhotoId;
	
	private Location location;
	private User user;
	private File coverPhoto;
	private List<File> photoList;
	
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
	public Integer getPlaceTypeId() {
		return placeTypeId;
	}
	public void setPlaceTypeId(Integer placeTypeId) {
		this.placeTypeId = placeTypeId;
	}
	public Integer getHomeTypeId() {
		return homeTypeId;
	}
	public void setHomeTypeId(Integer homeTypeId) {
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
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Long getLocationId() {
		return locationId;
	}
	public void setLocationId(Long locationId) {
		this.locationId = locationId;
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
	public Integer getCurrencyId() {
		return currencyId;
	}
	public void setCurrencyId(Integer currencyId) {
		this.currencyId = currencyId;
	}
	public Integer getBedNumber() {
		return bedNumber;
	}
	public void setBedNumber(Integer bedNumber) {
		this.bedNumber = bedNumber;
	}
	public Integer getBedTypeId() {
		return bedTypeId;
	}
	public void setBedTypeId(Integer bedTypeId) {
		this.bedTypeId = bedTypeId;
	}
	public Integer getBathRoomNumber() {
		return bathRoomNumber;
	}
	public void setBathRoomNumber(Integer bathRoomNumber) {
		this.bathRoomNumber = bathRoomNumber;
	}
	public Integer getBathRoomType() {
		return bathRoomType;
	}
	public void setBathRoomType(Integer bathRoomType) {
		this.bathRoomType = bathRoomType;
	}
	public Integer getPlaceMateNumber() {
		return placeMateNumber;
	}
	public void setPlaceMateNumber(Integer placeMateNumber) {
		this.placeMateNumber = placeMateNumber;
	}
	public Integer getPlaceMateGender() {
		return placeMateGender;
	}
	public void setPlaceMateGender(Integer placeMateGender) {
		this.placeMateGender = placeMateGender;
	}
	public Integer getGuestNumber() {
		return guestNumber;
	}
	public void setGuestNumber(Integer guestNumber) {
		this.guestNumber = guestNumber;
	}
	public Integer getGuestGender() {
		return guestGender;
	}
	public void setGuestGender(Integer guestGender) {
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
	public Integer getMinimumStay() {
		return minimumStay;
	}
	public void setMinimumStay(Integer minimumStay) {
		this.minimumStay = minimumStay;
	}
	public Integer getMaximumStay() {
		return maximumStay;
	}
	public void setMaximumStay(Integer maximumStay) {
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
	public Long getCoverPhotoId() {
		return coverPhotoId;
	}
	public void setCoverPhotoId(Long coverPhotoId) {
		this.coverPhotoId = coverPhotoId;
	}
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public File getCoverPhoto() {
		return coverPhoto;
	}
	public void setCoverPhoto(File coverPhoto) {
		this.coverPhoto = coverPhoto;
	}
	public List<File> getPhotoList() {
		return photoList;
	}
	public void setPhotoList(List<File> photoList) {
		this.photoList = photoList;
	}
	public void addPhoto(File photo) {
		if (this.photoList == null) {
			this.photoList = new ArrayList<File>();
		}
		this.photoList.add(photo);
	}
}
