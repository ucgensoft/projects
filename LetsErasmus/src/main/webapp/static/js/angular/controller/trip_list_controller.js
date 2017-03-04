App.controller('tripListCtrl', ['$scope', '$controller', 'reservationService', 
                                function($scope, $controller, reservationService) {
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
  		ajaxHtml(webApplicationUrlPrefix + '/static/html/CancelReservation.html', 'divReservationModal', function() {
  			$('#divReservationModal').css('display', '');
    	});
  	};
  	
  	self.openCancelReservationWindow = function(reservationId) {
  		activeReservationId = reservationId;
  		ajaxHtml(webApplicationUrlPrefix + '/static/html/CancelReservation.html', 'divReservationModal', function() {
  			$('#divReservationModal').css('display', '');
    	});
  	};
  	
  	self.cancelReservation = function() {
  		var messageText = StringUtil.trim($('#txtNewMessage').val());
  		if (messageText != '') {
  			DialogUtil.confirm('Confirm', 'Reservation request will be cancelled, dou you want to continue?', function(response) {
  	  			if (response) {
  	  				var reservationId = activeReservationId;
  	  		  		var status = EnmReservationStatus.CLIENT_CANCELLED;
  	  		  		reservationService.updateReservation(reservationId, messageText, status,
  	  					  function(isSuccess) {
  	  						  if (isSuccess) {
  	  							  DialogUtil.info('Sucess', 'Reservation request is cancelled.', 'OK', function() {
  	  								  location.reload();
  	  							  });
  	  						  }
  	  			  		  }
  	  			  	  );
  	  			}
  	  		});
  		} else {
  			DialogUtil.warn('Warning', 'Please type a message to guest.', 'OK');
  		}
  	};
  	
  	self.openRecallReservationWindow = function(reservationId) {
  		activeReservationId = reservationId;
  		ajaxHtml(webApplicationUrlPrefix + '/static/html/RecallReservation.html', 'divReservationModal', function() {
  			$('#divReservationModal').css('display', '');
    	});
  	};
  	
  	self.recallReservation = function() {
  		var messageText = StringUtil.trim($('#txtNewMessage').val());
  		if (messageText != '') {
  			DialogUtil.confirm('Confirm', 'Reservation request will be recalled, dou you want to continue?', function(response) {
  	  			if (response) {
  	  				var reservationId = activeReservationId;
  	  		  		var status = EnmReservationStatus.RECALLED;
  	  		  		reservationService.updateReservation(reservationId, messageText, status,
  	  					  function(isSuccess) {
  	  						  if (isSuccess) {
  	  							  DialogUtil.info('Sucess', 'Reservation request is recalled.', 'OK', function() {
  	  								  location.reload();
  	  							  });
  	  						  }
  	  			  		  }
  	  			  	  );
  	  			}
  	  		});
  		} else {
  			DialogUtil.warn('Warning', 'Please type a message to guest.', 'OK');
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
  	}
  	
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