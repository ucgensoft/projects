App.controller('displayUserCtrl', ['$scope', '$controller', 'placeService', 'reviewService',
                                function($scope, $controller, placeService, reviewService) {
      var self = this;
      self.reviewGroupList = [];
      self.reviewCount = 0;
      
      self.initialize = function() {
    	  var userId = getUriParam('userId');
    	  if (userId != null && StringUtil.trim(userId) != '') {
    		  reviewService.listUserReview(userId,
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
  	
    self.initialize();
      
  }]);