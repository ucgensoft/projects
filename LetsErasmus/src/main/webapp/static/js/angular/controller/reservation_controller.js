App.controller('reservationCtrl', ['$scope', 'reservationService', 
                                function($scope, reservationService) {
      var self = this;
        
      self.initialize = function() {
    	  
    	  
	 };
  	  	
  	self.onBtnFinishClick = function() {
  		var reservation = {
  			messageText : 'I would like to book your place.'
  		};
  		reservationService.finishReservation(reservation,
				function(isSuccess) {
					if (isSuccess) {
						DialogUtil.info('Success', 'Congradulations! Your request is sent to host.', 'OK', function() {
							var url = webApplicationUrlPrefix + '/pages/dashboard/MessageList.xhtml';
							openWindow(url, true);
						});
					}
				}
		  );
  	};
  	
    self.initialize();
      
  }]);