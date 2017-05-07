App.controller('reservationListCtrl', ['$scope', '$controller', 'reservationService', 'reviewService', 
                                function($scope, $controller, reservationService, reviewService) {
      var self = this;
      self.upcomingList = [];
      self.ongoingList = [];
      self.oldList = [];
      var activeReservationId = null;
      
      self.initialize = function() {
    	  self.listReservations();
	 };
	 
	 self.listReservations = function() {
		  reservationService.listReservation( 
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
  	  	
  	self.openAcceptReservationWindow = function(reservationId) {
  		activeReservationId = reservationId;
  		ajaxHtml(webApplicationUrlPrefix + '/static/html/AcceptReservation.html', 'divCommonModal', function() {
  			$('#divCommonModal').css('display', '');
    	});
  	};
  	
  	self.acceptReservation = function() {
  		var messageText = StringUtil.trim($('#txtNewMessage').val());
  		if (messageText != '' && $('#chbTerms').val()) {
  			DialogUtil.confirm('Reservation request will be accepted, dou you want to continue?', function(response) {
  	  			if (response) {
  	  				var reservationId = activeReservationId;
  	  		  		var status = EnmReservationStatus.CONFIRMED;
  	  		  		reservationService.updateReservation(reservationId, messageText, status,
  	  					  function(isSuccess) {
  	  						  if (isSuccess) {
  	  							  DialogUtil.success('Congratulations! Reservation request is accepted.', function() {
  	  								  reloadPage();
  	  							  });
  	  						  }
  	  			  		  }
  	  			  	  );
  	  			}
  	  		});
  		} else {
  			DialogUtil.warn( 'Please type a message to guest and confirm that you have read the terms.');
  		}
  	};
  	
  	self.openDeclineReservationWindow = function(reservationId) {
  		activeReservationId = reservationId;
  		ajaxHtml(webApplicationUrlPrefix + '/static/html/DeclineReservation.html', 'divCommonModal', function() {
  			$('#divCommonModal').css('display', '');
    	});
  	};
  	
  	self.openCancelReservationWindow = function(reservationId) {
  		activeReservationId = reservationId;
  		ajaxHtml(webApplicationUrlPrefix + '/static/html/CancelReservation.html', 'divCommonModal', function() {
  			$('#divCommonModal').css('display', '');
    	});
  	};
  	
  	self.cancelReservation = function() {
  		var messageText = StringUtil.trim($('#txtNewMessage').val());
  		if (messageText != '') {
  			DialogUtil.confirm('Reservation request will be cancelled, dou you want to continue?', function(response) {
  	  			if (response) {
  	  				var reservationId = activeReservationId;
  	  		  		var status = EnmReservationStatus.HOST_CANCELLED;
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
  	
  	self.declineReservation = function() {
  		var messageText = StringUtil.trim($('#txtNewMessage').val());
  		if (messageText != '') {
  			DialogUtil.confirm('Reservation request will be declined, dou you want to continue?', function(response) {
  	  			if (response) {
  	  				var reservationId = activeReservationId;
  	  		  		var status = EnmReservationStatus.DECLINED;
  	  		  		reservationService.updateReservation(reservationId, messageText, status,
  	  					  function(isSuccess) {
  	  						  if (isSuccess) {
  	  							  DialogUtil.success('Reservation request is declined.', function() {
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
  	
  	self.showReservationBox = function() {
  		 return self.messageThread != null && self.messageThread.reservation != null 
  		 	&& self.messageThread.reservation.status == EnmReservationStatus.PENDING 
  		 	&& self.messageThread.activeUserId == self.messageThread.reservation.hostUserId;
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
  		if (reservation.hostReviewId != null) {
  			return true;
  		} else {
  			return false;
  		}
  	};
  	
  	self.openReviewWindow = function(reservation) {
  		activeReservationId = reservation.id;
  		ajaxHtml(webApplicationUrlPrefix + '/static/html/Review.html', 'divCommonModal', function() {
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

function acceptReservation() {
	var scope = angular.element( $('#divBody') ).scope();
	scope.ctrl.acceptReservation();
}

function declineReservation() {
	var scope = angular.element( $('#divBody') ).scope();
	scope.ctrl.declineReservation();
}

function cancelReservation() {
	var scope = angular.element( $('#divBody') ).scope();
	scope.ctrl.cancelReservation();
}

function sendReview() {
	var scope = angular.element( $('#divBody') ).scope();
	scope.ctrl.sendReview();
}