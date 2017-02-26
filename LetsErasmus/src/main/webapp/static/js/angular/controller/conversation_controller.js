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
  	
  	self.acceptReservation = function() {
  		DialogUtil.confirm('Confirm', 'Reservation request will be accepted, dou you want to continue?', function(response) {
  			if (response) {
  				var reservationId = self.messageThread.reservation.id;
  		  		var messageText = 'You welcome! :)';
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
  	};
  	
  	self.rejectReservation = function() {
  		DialogUtil.confirm('Confirm', 'Reservation request will be rejected, dou you want to continue?', function(response) {
  			if (response) {
  				var reservationId = self.messageThread.reservation.id;
  		  		var messageText = 'I have a guest at that time';
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
  	};
  	
  	self.showReservationBox = function() {
  		 return self.messageThread != null && self.messageThread.reservation != null 
  		 	&& self.messageThread.reservation.status == EnmReservationStatus.PENDING 
  		 	&& self.messageThread.activeUserId == self.messageThread.reservation.hostUserId;
  	};
  	
    self.initialize();
      
  }]);