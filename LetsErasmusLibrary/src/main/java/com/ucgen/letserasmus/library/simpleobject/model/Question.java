package com.ucgen.letserasmus.library.simpleobject.model;

public class Question {

	private Integer id;
	private String groupTitle;
	private String question;
	private String answer;
	private Integer questionOrder;
	private String isFaq;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getGroupTitle() {
		return groupTitle;
	}
	public void setGroupTitle(String groupTitle) {
		this.groupTitle = groupTitle;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public Integer getQuestionOrder() {
		return questionOrder;
	}
	public void setQuestionOrder(Integer questionOrder) {
		this.questionOrder = questionOrder;
	}
	public String getIsFaq() {
		return isFaq;
	}
	public void setIsFaq(String isFaq) {
		this.isFaq = isFaq;
	}
	
}
