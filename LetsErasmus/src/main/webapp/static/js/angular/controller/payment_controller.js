App.controller('paymentCtrl', ['$scope', 'reservationService', 'commonService', 'paymentService',
                                function($scope, reservationService, commonService, paymentService) {
      var self = this;
      self.reservation = null;
      
      var operationToken = null;
      var operationId = null;
        
      self.initialize = function() {
    	  
    	  operationId = getUriParam(EnmUriParam.OPERATION);
    	  operationToken = getUriParam(EnmUriParam.OPERATION_TOKEN);
    	  
    	  if (operationId != null && operationToken != null) {
    		  commonService.getTokenObject(operationToken, operationId,
  					function(reservation) {
      			  		self.reservation = reservation;
  					}
      		  ); 
    	  } else {
    		  DialogUtil.error('Error', 'Missin parameters in url!', 'OK', function() {
      			closeWindow();
      		  }); 
    	  }
	 };
	 
	 self.getPlaceTypeDescription = function() {
		  if (self.reservation != null && self.reservation.place != null) {
			var place = self.reservation.place;
			if (place.placeTypeId == 1) {
				return "Entire Place";
			} else if (place.placeTypeId == 2) {
				return "Private Room";
			} else if (place.placeTypeId == 3) {
				return "Shared Room";
			}
		  } else {
			  return "";
		  }
	};
	
	self.getLocationText = function() {
		  if (self.reservation != null) {
			  var place = self.reservation.place;
			  return place.location.country + ", " + place.location.state;
		  } else {
			  return "";
		  }
	  };
	  
	  self.getCurrencySymbol = function() {
		  if (self.reservation != null) {
			  return getCurrencySymbol(self.reservation.currencyId);
		  } else {
			  return "";
		  }
	  };
	  
	  self.getCurrencyAbbr = function() {
		  if (self.reservation != null) {
			  return getCurrencyAbbr(self.reservation.currencyId);
		  } else {
			  return "";
		  }
	  };
	  	
  	self.onBtnFinishClick = function() {
  		var reservation = {
  			messageText : 'I would like to book your place.',
  			operationToken : operationToken
  		};
  		reservationService.finishReservation(reservation,
				function(isSuccess) {
					if (isSuccess) {
						DialogUtil.info('Success', 'Congratulations! Your request is sent to host.', 'OK', function() {
							var url = webApplicationUrlPrefix + '/pages/dashboard/MessageList.xhtml';
							openWindow(url, true);
						});
					}
				}
		  );
  	};
  	
  	self.getPaymentToken = function(callBack) {
  		paymentService.getPaymentToken(operationToken, operationId, function(isSuccess, paymentToken) {
  			if (isSuccess) {
  				callBack(paymentToken);
  			} else {
  				DialogUtil.warn('Warning', 'We can not continue payment operation currently. Please try again later!', 'OK', function() {
  					closeWindow();
  				});
  			}
  		});
  	}
  	
    self.initialize();
      
  }]);

function getPaymentToken(callBack) {
	var scope = angular.element( $('#divBody') ).scope();
	scope.ctrl.getPaymentToken(callBack);
}