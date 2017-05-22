App.controller('tripListCtrl', ['$scope', '$controller', 'reservationService', 'reviewService',
                                function($scope, $controller, reservationService, reviewService) {
      var self = this;
      self.upcomingList = [];
      self.ongoingList = [];
      self.oldList = [];
      var activeReservationId = null;
      
      self.initialize = function() {
    	  self.listTrips();
	 };
	 
	 self.listTrips = function() {
		  reservationService.listTrips( 
			  function(reservationMap) {
				  self.upcomingList = reservationMap['upcomingList']; 
				  self.ongoingList = reservationMap['ongoingList'];
				  self.oldList = reservationMap['oldList'];
	  		  }
	  	  );
	 };
	   	
  	self.getCurrencySymbol = function(currencyId) {
  		return getCurrencySymbol(currencyId);
  	};
  	  	
  	self.openCancelReservationWindow = function(reservationId) {
  		activeReservationId = reservationId;
  		ajaxHtml(webApplicationUrlPrefix + '/static/html/CancelReservation.htm', 'divCommonModal', function() {
  			$('#divCommonModal').css('display', '');
    	});
  	};
  	
  	self.openCancelReservationWindow = function(reservationId) {
  		activeReservationId = reservationId;
  		ajaxHtml(webApplicationUrlPrefix + '/static/html/CancelReservation.htm', 'divCommonModal', function() {
  			$('#divCommonModal').css('display', '');
    	});
  	};
  	
  	self.cancelReservation = function() {
  		var messageText = StringUtil.trim($('#txtNewMessage').val());
  		if (messageText != '') {
  			DialogUtil.confirm('Reservation request will be cancelled, dou you want to continue?', function(response) {
  	  			if (response) {
  	  				var reservationId = activeReservationId;
  	  		  		var status = EnmReservationStatus.CLIENT_CANCELLED;
  	  		  		reservationService.updateReservation(reservationId, messageText, status,
  	  					  function(isSuccess) {
  	  						  if (isSuccess) {
  	  							  DialogUtil.success('Reservation request is cancelled.', function() {
  	  								  reloadPage();
  	  							  });
  	  						  }
  	  			  		  }
  	  			  	  );
  	  			}
  	  		});
  		} else {
  			DialogUtil.warn( 'Please type a message to guest.');
  		}
  	};
  	
  	self.openRecallReservationWindow = function(reservationId) {
  		activeReservationId = reservationId;
  		ajaxHtml(webApplicationUrlPrefix + '/static/html/RecallReservation.htm', 'divCommonModal', function() {
  			$('#divCommonModal').css('display', '');
    	});
  	};
  	
  	self.recallReservation = function() {
  		var messageText = StringUtil.trim($('#txtNewMessage').val());
  		if (messageText != '') {
  			DialogUtil.confirm('Reservation request will be recalled, dou you want to continue?', function(response) {
  	  			if (response) {
  	  				var reservationId = activeReservationId;
  	  		  		var status = EnmReservationStatus.RECALLED;
  	  		  		reservationService.updateReservation(reservationId, messageText, status,
  	  					  function(isSuccess) {
  	  						  if (isSuccess) {
  	  							  DialogUtil.success('Reservation request is recalled.', function() {
  	  								  reloadPage();
  	  							  });
  	  						  }
  	  			  		  }
  	  			  	  );
  	  			}
  	  		});
  		} else {
  			DialogUtil.warn( 'Please type a message to guest.');
  		}
  	};
  	
  	self.getReservationStatus = function(status) {
  		return getReservationStatusDesc(status);;
  	};
  	
  	self.getCurrencySymbol = function(currencyId) {
  		return getCurrencySymbol(currencyId);
  	};
  	
  	self.generateUserProfilePhotoUrl = function(userId, photoId, size) {
  		return generateUserProfilePhotoUrl(userId, photoId, size);
  	};
  	
  	self.isReviewed = function(reservation) {
  		if (reservation.clientReviewId != null) {
  			return true;
  		} else {
  			return false;
  		}
  	};
  	
  	self.openReviewWindow = function(reservation) {
  		activeReservationId = reservation.id;
  		ajaxHtml(webApplicationUrlPrefix + '/static/html/Review.htm', 'divCommonModal', function() {
  			$('#divCommonModal').css('display', '');
    	});
  	};
  	
  	self.sendReview = function () {
  		var description = StringUtil.trim($('#txtDescription').val());
  		var rank = null;
  		for(var i = 1; i <= 5; i++) {
  			if ($('#rdRank' + i)[0].checked) {
  				rank = i;
  				break;
  			}
  		}
  		if (StringUtil.trim(description) != '' && rank != null) {
  			var reservationId = activeReservationId;
  			var review = {
  					rank : rank,
  					description : description,
  					entityType : EnmEntityType.RESERVATION,
  					entityId : reservationId
  			}
	  		reviewService.createReview(review,
				  function(isSuccess) {
					  if (isSuccess) {
						  DialogUtil.success('Your review is sent succesfully.', function() {
							  reloadPage();
						  });
					  }
		  		  }
		  	  );
  		} else {
  			DialogUtil.warn( 'Please type description and choose a rank!');
  		}
  	};
  	
    self.initialize();
      
  }]);

function cancelReservation() {
	var scope = angular.element( $('#divBody') ).scope();
	scope.ctrl.cancelReservation();
}

function recallReservation() {
	var scope = angular.element( $('#divBody') ).scope();
	scope.ctrl.recallReservation();
}

function sendReview() {
	var scope = angular.element( $('#divBody') ).scope();
	scope.ctrl.sendReview();
}