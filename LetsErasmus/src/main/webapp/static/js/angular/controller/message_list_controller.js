App.controller('messageListCtrl', ['$scope', '$controller', 'messageService', 
                                function($scope, $controller, messageService) {
      var self = this;
      self.displayedThreadList = [];
      self.hostThreadList = null; 
      self.clientThreadList = null;
      self.isHost = false;
      
      self.initialize = function() {
    	  if (placeListingExist) {
    		  self.changeSelectedTab('lnkHostThreads');
    	  } else {
    		  self.changeSelectedTab('lnkClientThreads');
    	  }
	 };
	 
	 self.listMessageThread = function(hostFlag, clientFlag, callBack) {
		  var entityType = EnmEntityType.PLACE;
	   	  
		  messageService.listMessageThread(entityType, hostFlag, clientFlag,
	  				function(messageMap) {
	  					if (hostFlag == 1) {
	  						self.hostThreadList = messageMap["hostThreadList"];
	  					} else if (clientFlag == 1) {
	  						self.clientThreadList = messageMap["clientThreadList"]
	  					}
	  					if (callBack) {
	  						callBack();
	  					}
	  				}
	  		  );
	 };
  	  	
  	self.changeSelectedTab = function(linkId) {
  		if (linkId == 'lnkHostThreads') {
  			self.isHost = true;
  			$('#lnkClientThreads').attr('aria-selected', 'false');
  			$('#lnkHostThreads').attr('aria-selected', 'true');
  			if (self.hostThreadList == null) {
  				self.listMessageThread(1, 0, function() {
  					self.displayedThreadList = self.hostThreadList;
  					self.displayThreadList();
  				});
  			} else {
  				self.displayedThreadList = self.hostThreadList;
  				self.displayThreadList();
  			}
  		} else {
  			self.isHost = false;
  			$('#lnkClientThreads').attr('aria-selected', 'true');
  			$('#lnkHostThreads').attr('aria-selected', 'false');
  			if (self.clientThreadList == null) {
  				self.listMessageThread(0, 1, function() {
  					self.displayedThreadList = self.clientThreadList;
  					self.displayThreadList();
  				});
  			} else {
  				self.displayedThreadList = self.clientThreadList;
  				self.displayThreadList();
  			}
  		}
  	};
  	
  	self.displayThreadList = function() {
  		if (self.displayedThreadList.length > 0) {
  			$('#divNoMessage').addClass('hidden');
  		} else {
  			$('#divNoMessage').removeClass('hidden');
  		}
  	};
  	
  	self.getUserProfilePhotoUrl = function(messageThread) {
  		var user = self.getUser(messageThread);
  		
  		var photoId = user.profilePhotoId;
  		var userId = user.id;
  		if (photoId != null) {
  			return generateUserProfilePhotoUrl(userId, photoId);
  		} else {
  			return defaultSmallProfilePhotoUrl;
  		}
  	};
  	
  	self.getUser = function(messageThread) {
  		var user = null;
  		if($('#lnkClientThreads').attr('aria-selected') == 'true') {
  			user = messageThread.hostUser;
  		} else {
  			user = messageThread.clientUser;
  		}
  		
  		return user;
  	};
  	
  	self.getCurrencySymbol = function(currencyId) {
  		return getCurrencySymbol(currencyId);
  	};
  	
  	self.getReservationStatus = function(messageThread) {
  		var statusText = '';
  		if (messageThread != null && messageThread.reservation != null) {
  			statusText = getReservationStatusDesc(messageThread.reservation.status);
  		}
  		return statusText;
  	};
  	
    self.initialize();
      
  }]);