App.controller('reservationListCtrl', ['$scope', '$controller', 'reservationService', 
                                function($scope, $controller, reservationService) {
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
  		ajaxHtml(webApplicationUrlPrefix + '/static/html/AcceptReservation.html', 'divReservationModal', function() {
  			$('#divReservationModal').css('display', '');
    	});
  	};
  	
  	self.acceptReservation = function() {
  		var messageText = StringUtil.trim($('#txtNewMessage').val());
  		if (messageText != '' && $('#chbTerms').val()) {
  			DialogUtil.confirm('Confirm', 'Reservation request will be accepted, dou you want to continue?', function(response) {
  	  			if (response) {
  	  				var reservationId = activeReservationId;
  	  		  		var status = EnmReservationStatus.CONFIRMED;
  	  		  		reservationService.updateReservation(reservationId, messageText, status,
  	  					  function(isSuccess) {
  	  						  if (isSuccess) {
  	  							  DialogUtil.info('Sucess', 'Congradulations! Reservation request is accepted.', 'OK', function() {
  	  								  location.reload();
  	  							  });
  	  						  }
  	  			  		  }
  	  			  	  );
  	  			}
  	  		});
  		} else {
  			DialogUtil.warn('Warning', 'Please type a message to guest and confirm that you have read the terms.', 'OK');
  		}
  	};
  	
  	self.openDeclineReservationWindow = function(reservationId) {
  		activeReservationId = reservationId;
  		ajaxHtml(webApplicationUrlPrefix + '/static/html/DeclineReservation.html', 'divReservationModal', function() {
  			$('#divReservationModal').css('display', '');
    	});
  	};
  	
  	self.declineReservation = function() {
  		var messageText = StringUtil.trim($('#txtNewMessage').val());
  		if (messageText != '') {
  			DialogUtil.confirm('Confirm', 'Reservation request will be rejected, dou you want to continue?', function(response) {
  	  			if (response) {
  	  				var reservationId = activeReservationId;
  	  		  		var status = EnmReservationStatus.DECLINED;
  	  		  		reservationService.updateReservation(reservationId, messageText, status,
  	  					  function(isSuccess) {
  	  						  if (isSuccess) {
  	  							  DialogUtil.info('Sucess', 'Reservation request is rejected.', 'OK', function() {
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
  	}
  	
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