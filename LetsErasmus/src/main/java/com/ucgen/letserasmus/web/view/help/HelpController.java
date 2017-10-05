package com.ucgen.letserasmus.web.view.help;

import java.util.List;
import java.util.Locale;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.apache.log4j.Level;

import com.ucgen.common.util.CommonUtil;
import com.ucgen.common.util.FileLogger;
import com.ucgen.common.util.WebUtil;
import com.ucgen.letserasmus.library.simpleobject.model.Question;
import com.ucgen.letserasmus.library.simpleobject.model.QuestionGroup;
import com.ucgen.letserasmus.library.simpleobject.service.ISimpleObjectService;
import com.ucgen.letserasmus.web.view.BaseController;

@ManagedBean
@ViewScoped
public class HelpController extends BaseController {

	private static String PARAM_GROUP_KEY = "groupKey";
	
	@ManagedProperty(value="#{simpleObjectService}")
	private ISimpleObjectService simpleObjectService;
	
	public void setSimpleObjectService(ISimpleObjectService simpleObjectService) {
		this.simpleObjectService = simpleObjectService;
	}
	
	private String selectedGroupKey;
	private List<QuestionGroup> helpGroupList;
	private List<Question> questionList;
	
	public String getSelectedGroupKey() {
		try {
			this.initialize();
		} catch (Exception e) {
			FileLogger.log(Level.ERROR, "HelpController-getSelectedGroupKey- Error: " + CommonUtil.getExceptionMessage(e));
		}
		return selectedGroupKey;
	}

	public List<QuestionGroup> getHelpGroupList() {
		try {
			this.initialize();
		} catch (Exception e) {
			FileLogger.log(Level.ERROR, "HelpController-getHelpGroupList- Error: " + CommonUtil.getExceptionMessage(e));
		}
		return this.helpGroupList;
	}

	public List<Question> getQuestionList() {
		try {
			this.initialize();
		} catch (Exception e) {
			FileLogger.log(Level.ERROR, "HelpController-getQuestionList- Error: " + CommonUtil.getExceptionMessage(e));
		}
		return questionList;
	}
	
	private void initialize() {
		
		if (helpGroupList == null) {
			helpGroupList = this.simpleObjectService.listQuestionGroup();
			for (QuestionGroup questionGroup : helpGroupList) {
				questionGroup.setKey(questionGroup.getGroupTitle().toLowerCase(Locale.ENGLISH).replace(" ", "-"));
			}
		}
		
		String paramGroupKey = super.getRequestParameter(PARAM_GROUP_KEY);
		if (this.selectedGroupKey == null 
				|| !this.selectedGroupKey.equals(paramGroupKey)) {
			this.selectedGroupKey = paramGroupKey;
			
			if (this.selectedGroupKey == null) {
				this.selectedGroupKey = "FAQ";
			}
			
			this.selectedGroupKey = this.selectedGroupKey.replace("-", " ");
			this.selectedGroupKey = this.selectedGroupKey.toLowerCase();
			this.selectedGroupKey = Character.toString(this.selectedGroupKey.charAt(0)).toUpperCase()+this.selectedGroupKey.substring(1);
			String groupTitle = this.selectedGroupKey.replace("-", " ");

			questionList = this.simpleObjectService.listQuestion(groupTitle, null);
		}
		
	}
	
	public String getPageUrl() {
		String url = null;
		try {
			this.initialize();
			
			if (this.selectedGroupKey == null) {
				url = WebUtil.concatUrl(super.getWebApplication().getUrlPrefix(), "help");
			} else {
				url = WebUtil.concatUrl(super.getWebApplication().getUrlPrefix(), "help", this.selectedGroupKey);
			}
		} catch (Exception e) {
			FileLogger.log(Level.ERROR, "HelpController-getPageUrl- Error: " + CommonUtil.getExceptionMessage(e));
		}
		return url;
	}
	
}
