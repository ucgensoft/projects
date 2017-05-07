App.controller('conversationCtrl', ['$scope', '$controller', 'messageService', 'reservationService', 
                                function($scope, $controller, messageService, reservationService) {
      var self = this;
      self.messageThread = null;
      
      self.initialize = function() {
    	  
    	  if (loginUserId != '') {
    		  listComplaint(EnmEntityType.MESSAGE);
    	  }
    	  
    	  var messageThreadId = getUriParam('threadId');
    	  if (messageThreadId) {
    		  self.getMessageThread(messageThreadId, function() {
    			  if(self.showReservationBox()) {
    				  self.startTimer();
    			  }
    		  });
    	  }
	 };
	 
	 self.startTimer = function() {
		 var seconds_left = 10;
		 var interval = setInterval(function() {
			 var dateDiffString = getDateDiffString(new Date(self.messageThread.reservation.expireDate), new Date());
		     $('#spanRemainingTime').html(dateDiffString);
		     if (dateDiffString == '00:00:00') {
		    	 clearInterval(interval);
		     }
		 }, 1000);
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
  	
  	self.getCurrencySymbol = function() {
  		if (self.messageThread != null) {
  			return getCurrencySymbol(self.messageThread.currencyId);
  		}
  	};
  	
  	self.getTotalLabel = function() {
  		if (self.messageThread != null) {
  			if (self.messageThread.hostUserId == loginUserId) {
  				 return "You earn";
  			 } else {
  				 return "Total";
  			 }
  		}
	 };
	 
  	self.getFeeLabel = function() {
  		if (self.messageThread != null) {
  			if (self.messageThread.hostUserId == loginUserId) {
  				 return "Host Service Fee";
  			 } else {
  				 return "Tenant Service Fee";
  			 }
  		}
	 };
	 
  	self.getUserLabel = function() {
  		if (self.messageThread != null) {
  			if (self.messageThread.hostUserId == loginUserId) {
  				 return "Requested by ";
  			 } else {
  				 return "Hosted by ";
  			 }
  		}
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
  			DialogUtil.warn('Please type message text.');
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
  			DialogUtil.confirm('Reservation request will be accepted, dou you want to continue?', function() {
  				var reservationId = self.messageThread.reservation.id;
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
  	  		});
  		} else {
  			DialogUtil.warn('Please type a message to guest and confirm that you have read the terms.');
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
  			DialogUtil.confirm('Reservation request will be rejected, dou you want to continue?', function() {
  				var reservationId = self.messageThread.reservation.id;
  		  		var status = EnmReservationStatus.DECLINED;
  		  		reservationService.updateReservation(reservationId, messageText, status,
  					  function(isSuccess) {
  						  if (isSuccess) {
  							  DialogUtil.info('Reservation request is rejected.', function() {
  								  reloadPage();
  							  });
  						  }
  			  		  }
  			  	  );
  	  		});
  		} else {
  			DialogUtil.warn('Please type a message to guest.');
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
 		DialogUtil.confirm('Booking request will be sent to host, dou you want to continue?', function() {
 			var reservationId = self.messageThread.reservation.id;
	  		var status = EnmReservationStatus.PENDING;
	  		reservationService.updateReservation(reservationId, 'message', status,
				  function(isSuccess) {
					  if (isSuccess) {
						  DialogUtil.success('Congratulations! Booking request is sent to host.', function() {
							  reloadPage();
						  });
					  }
		  		  }
		  	  );
	  		});
  	};
  	
  	self.openComplaintWindow = function(messageId) {
  		openComplaintWindow(EnmEntityType.MESSAGE, messageId);
    };
    
    self.isMessageComplainted = function(messageId) {
    	return isEntityComplainted(EnmEntityType.MESSAGE, messageId); 
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

function getDateDiffString(dateFuture, datePast) {
	if (dateFuture > datePast) {
		var seconds = Math.floor((dateFuture - (datePast))/1000);
		var minutes = Math.floor(seconds/60);
		var hours = Math.floor(minutes/60);
		var days = Math.floor(hours/24);

		hours = hours-(days*24);
		minutes = minutes-(days*24*60)-(hours*60);
		seconds = seconds-(days*24*60*60)-(hours*60*60)-(minutes*60);

		if (hours < 10) {
			timeString = '0' + hours;
		}
		if (minutes < 10) {
			minutes = '0' + minutes;
		}
		if (seconds < 10) {
			seconds = '0' + seconds;
		}
		return hours + ':' + minutes + ':' + seconds;
	} else {
		return '00:00:00';
	}
}