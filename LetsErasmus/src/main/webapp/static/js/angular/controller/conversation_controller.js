App.controller('conversationCtrl', ['$scope', '$controller', 'messageService', 'reservationService', 
                                function($scope, $controller, messageService, reservationService) {
      var self = this;
      self.messageThread = null;
      
      self.initialize = function() {
    	  var messageThreadId = getUriParam('threadId');
    	  if (messageThreadId) {
    		  self.getMessageThread(messageThreadId);
    	  }
	 };
	 
	 self.getMessageThread = function(threadId, callBack) {
		  messageService.getMessageThread(threadId, 
			  function(messageThread) {
				  	self.messageThread = messageThread;
				  	if (callBack) {
						callBack();
					}
	  		  }
	  	  );
	 };
	 
	 self.getUser = function(message) {
		 if (message.senderUserId == self.messageThread.hostUserId) {
			 return {
				id : self.messageThread.hostUserId,
				firstName : self.messageThread.hostUserFirstName,
				profilePhotoUrlSmall : self.messageThread.hostUserProfilePhotoUrl,
				profilePhotoUrlMedium : self.messageThread.hostUserProfilePhotoUrlMedium
			 };
		 } else {
			 return {
				id : self.messageThread.clientUserId,
				firstName : self.messageThread.clientUserFirstName,
				profilePhotoUrlSmall : self.messageThread.clientUserProfilePhotoUrl,
				profilePhotoUrlMedium : self.messageThread.clientUserProfilePhotoUrlMedium
			 };
		 }
	 };
	 
	 self.getDestinationUser = function() {
		 if (self.messageThread != null) {
			 if (self.messageThread.activeUserId != self.messageThread.hostUserId) {
				 return {
					id : self.messageThread.hostUserId,
					firstName : self.messageThread.hostUserFirstName,
					profilePhotoUrlSmall : self.messageThread.hostUserProfilePhotoUrl,
					profilePhotoUrlMedium : self.messageThread.hostUserProfilePhotoUrlMedium
				 };
			 } else {
				 return {
					id : self.messageThread.clientUserId,
					firstName : self.messageThread.clientUserFirstName,
					profilePhotoUrlSmall : self.messageThread.clientUserProfilePhotoUrl,
					profilePhotoUrlMedium : self.messageThread.clientUserProfilePhotoUrlMedium
				 };
			 }
		 }
	 };
	 
	 self.getActiveUser = function() {
		 if (self.messageThread != null) {
			 if (self.messageThread.activeUserId == self.messageThread.hostUserId) {
				 return {
					id : self.messageThread.hostUserId,
					firstName : self.messageThread.hostUserFirstName,
					profilePhotoUrlSmall : self.messageThread.hostUserProfilePhotoUrl,
					profilePhotoUrlMedium : self.messageThread.hostUserProfilePhotoUrlMedium
				 };
			 } else {
				 return {
					id : self.messageThread.clientUserId,
					firstName : self.messageThread.clientUserFirstName,
					profilePhotoUrlSmall : self.messageThread.clientUserProfilePhotoUrl,
					profilePhotoUrlMedium : self.messageThread.clientUserProfilePhotoUrlMedium
				 };
			 }
		 }
	 };
  	
  	self.getCurrencySymbol = function(currencyId) {
  		return getCurrencySymbol(currencyId);
  	};
  	
  	self.sendMessage = function() {
  		var messageText = StringUtil.trim($('#txtMessage').val());
  		if (messageText != '') {
  			messageService.sendMessage(self.messageThread.id, messageText,
  				  function(newMessage) {
  					  self.messageThread.messageList.splice(0, 0, newMessage);
  					$('#txtMessage').val('');
  		  		  }
  		  	  );
  		} else {
  			DialogUtil.warn('Warning', 'Please type message text.', 'OK');
  		}
  	};
  	
  	self.openAcceptReservationWindow = function() {
  		ajaxHtml(webApplicationUrlPrefix + '/static/html/AcceptReservation.html', 'divCommonModal', function() {
  			$('#divCommonModal').css('display', '');
    	});
  	};
  	
  	self.acceptReservation = function() {
  		var messageText = StringUtil.trim($('#txtNewMessage').val());
  		if (messageText != '' && $('#chbTerms').val()) {
  			DialogUtil.confirm('Confirm', 'Reservation request will be accepted, dou you want to continue?', function(response) {
  	  			if (response) {
  	  				var reservationId = self.messageThread.reservation.id;
  	  		  		var status = EnmReservationStatus.CONFIRMED;
  	  		  		reservationService.updateReservation(reservationId, messageText, status,
  	  					  function(isSuccess) {
  	  						  if (isSuccess) {
  	  							  DialogUtil.info('Sucess', 'Congratulations! Reservation request is accepted.', 'OK', function() {
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
  	
  	self.openDeclineReservationWindow = function() {
  		ajaxHtml(webApplicationUrlPrefix + '/static/html/DeclineReservation.html', 'divCommonModal', function() {
  			$('#divCommonModal').css('display', '');
    	});
  	};
  	
  	self.declineReservation = function() {
  		var messageText = StringUtil.trim($('#txtNewMessage').val());
  		if (messageText != '') {
  			DialogUtil.confirm('Confirm', 'Reservation request will be rejected, dou you want to continue?', function(response) {
  	  			if (response) {
  	  				var reservationId = self.messageThread.reservation.id;
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
  	
  	self.showBookingBox = function() {
 		 return self.messageThread != null && self.messageThread.reservation != null 
 		 	&& self.messageThread.reservation.status == EnmReservationStatus.INQUIRY 
 		 	&& self.messageThread.activeUserId == self.messageThread.reservation.clientUserId;
 	};
 	
 	self.sendBookingRequest = function() {
 		DialogUtil.confirm('Confirm', 'Booking request will be sent to host, dou you want to continue?', function(response) {
	  			if (response) {
	  				var reservationId = self.messageThread.reservation.id;
	  		  		var status = EnmReservationStatus.PENDING;
	  		  		reservationService.updateReservation(reservationId, 'message', status,
	  					  function(isSuccess) {
	  						  if (isSuccess) {
	  							  DialogUtil.info('Sucess', 'Congratulations! Booking request is sent to host.', 'OK', function() {
	  								  location.reload();
	  							  });
	  						  }
	  			  		  }
	  			  	  );
	  			}
	  		});
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