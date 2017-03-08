App.controller('displayUserCtrl', ['$scope', '$controller', 'placeService', 'reviewService',
                                function($scope, $controller, placeService, reviewService) {
      var self = this;
      self.reviewGroupList = [];
      self.reviewCount = 0;
      self.userId = null;
      
      self.initialize = function() {
    	  self.userId = getUriParam('userId');
    	  listComplaint();
    	  if (self.userId != null && StringUtil.trim(self.userId) != '') {
    		  reviewService.listUserReview(self.userId,
    			  function(reviewMap) {
	  					var guestReviewList = {
	  							groupTitle : 'Reviews From Guests',
	  							reviewList : reviewMap['guestReviewList']
	  					};
	  					var hostReviewList = {
	  							groupTitle : 'Reviews From Hosts',
	  							reviewList : reviewMap['hostReviewList']
	  					};
	  					
	  					self.reviewGroupList = [guestReviewList, hostReviewList];
	  					self.reviewCount = guestReviewList.reviewList.length + hostReviewList.reviewList.length
	  				}
	  		  );
    	  }
	 };
	 
	 self.openComplaintWindow = function() {
    	 ajaxHtml(webApplicationUrlPrefix + '/static/html/Complaint.html', 'divCommonModal', function() {
   		     $('#hiddenComplaintEntityType').val(EnmEntityType.USER);
   		     $('#hiddenComplaintEntityId').val(self.userId);
    		 $('#divCommonModal').css('display', '');
     	});
     };
     
     self.isUserComplainted = function() {
    	 if (self.userId != null) {
    	   	  	if (userComplaintMap && userComplaintMap[EnmEntityType.USER.toString()]) {
    	   		  complaintMap = userComplaintMap[EnmEntityType.USER.toString()];
    	   		  if (complaintMap[self.userId.toString()]) {
    					  return true;
    				  }
    	   	  }
    	 }
   	  	return false;
     };
  	
    self.initialize();
      
  }]);