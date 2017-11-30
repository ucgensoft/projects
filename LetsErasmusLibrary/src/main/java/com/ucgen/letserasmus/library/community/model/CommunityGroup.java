package com.ucgen.letserasmus.library.community.model;

import java.util.ArrayList;
import java.util.List;

import com.ucgen.letserasmus.library.message.model.MessageThread;

public class CommunityGroup {

	private Integer id;
	private String name;
	private Integer countryId;
	private Integer cityId;
	private Integer universityId;
	
	private List<MessageThread> topicList;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getCountryId() {
		return countryId;
	}
	public void setCountryId(Integer countryId) {
		this.countryId = countryId;
	}
	public Integer getCityId() {
		return cityId;
	}
	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}
	public Integer getUniversityId() {
		return universityId;
	}
	public void setUniversityId(Integer universityId) {
		this.universityId = universityId;
	}
	public List<MessageThread> getTopicList() {
		return topicList;
	}
	public void setTopicList(List<MessageThread> topicList) {
		this.topicList = topicList;
	}
	public void addTopic(MessageThread messageThread) {
		if (this.topicList == null) {
			this.topicList = new ArrayList<MessageThread>();
		}
		this.topicList.add(messageThread);
	}
	
}
