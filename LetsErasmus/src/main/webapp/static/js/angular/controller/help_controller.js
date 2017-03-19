App.controller('helpCtrl', ['$scope', 'commonService', '$sce', '$compile', 
                                function($scope, commonService, sce, compile) {
      var self = this;
      
      self.questionGroupList = [];
      self.questionList = [];
      self.selectedGroupTitle = null;
      
      self.initialize = function() {
    	  
    	  commonService.listQuestionGroup(function(tmpQuestionGroupList) {
			if (tmpQuestionGroupList) {
				self.questionGroupList = tmpQuestionGroupList;
			}
		  });
    	  
    	  self.listQuestion();
    	      	  
	 };
	 
	 self.listQuestion = function() {
		 var searchText = StringUtil.trim($('#txtSearch').val());
		 var groupTitle = '';
		 
		 if (self.selectedGroupTitle) {
			 groupTitle = self.selectedGroupTitle;
		 }
		 
		 commonService.listQuestion(groupTitle, searchText, function(tmpQuestionList) {
  			if (tmpQuestionList) {
  				self.questionList = tmpQuestionList;
  			}
  		  });
		 
		 if (searchText != '') {
			 $('#divQuestionGroup').addClass('hidden-force');
		 } else {
			 $('#divQuestionGroup').removeClass('hidden-force');
		 }
	 }
	  	  	
  	 self.search = function() {
  		self.listQuestion();
  	};
  	
  	self.clearSearch = function() {
  		$('#txtSearch').val('');
  		self.search();
  	};
  	
  	self.onQuestionGroupSelected = function(groupTitle) {
  		self.selectedGroupTitle = groupTitle;
  		self.listQuestion();
  	};
  	
  	self.onQuestionHeaderClick = function(questionId) {
  		var btnElement = $('#btnQuestion_' + questionId)[0]; 
  		btnElement.classList.toggle("active");
		var divAnswer = $('#divAnswer_' + questionId)[0]
  		var divAnswer = btnElement.nextElementSibling;
		if (divAnswer.style.maxHeight) {
			divAnswer.style.maxHeight = null;
		} else {
			divAnswer.style.maxHeight = divAnswer.scrollHeight + "px";
		}
  	};
  	
  	self.to_trusted = function(html_code) {
  	    return sce.trustAsHtml(html_code);
  	}
  	
    self.initialize();
      
  }]);