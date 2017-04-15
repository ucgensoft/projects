App.controller('displayUserCtrl', ['$scope', '$controller', 'placeService', 'reviewService', 'userService',
                                function($scope, $controller, placeService, reviewService, userService) {
      var self = this;
      self.reviewGroupList = [];
      self.reviewCount = 0;
      self.user = null;
      self.userId = null;
      var complaintList = null;
      
      self.initialize = function() {
    	  self.userId = getUriParam('userId');
    	  
    	  if (loginType != '') {
    		  listComplaint(EnmEntityType.USER);
    	  }
    	  
    	  if (self.userId != null && StringUtil.trim(self.userId) != '') {
    		  userService.getUser(self.userId,
        			  function(tmpUser) {
    			  			if (tmpUser != null) {
    			  				self.user = tmpUser;
        	  					self.displayUserInfo();
    			  			} else {
    			  				DialogUtil.warn('Warning', 'User not found in the system!', 'OK');
    			  			}
    	  				}
    	  		  );
    	  }
	 };
	 
	 self.displayUserInfo = function() {
		 reviewService.listUserReview(self.user.id,
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
    		 return isEntityComplainted(EnmEntityType.USER, self.userId);
    	 } else {
    		 return false;
    	 }
     };
     
     self.getUserProfilePhotoUrl = function() {
    	 if (self.user != null && self.user.id) {
    		 return generateUserProfilePhotoUrl(self.user.id, self.user.profilePhotoId, EnmImageSize.MEDIUM);
    	 } else {
    		 return '';
    	 }
     };
     
     self.isActiveUser = function() {
    	if (self.user != null && self.user.id == loginUserId) {
    		return true;
    	} else {
    		return false;
    	}
     };
  	
    self.initialize();
      
  }]);