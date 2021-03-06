App.controller('paymentCtrl', ['$scope', 'reservationService', 'commonService', 'paymentService',
                                function($scope, reservationService, commonService, paymentService) {
      var self = this;
      self.reservation = null;
      
      var operationToken = null;
      var operationId = null;
      var submitListener = null;
      var initializeListener = null;
      var validateListener = null;
        
      self.initialize = function() {
    	  window.onbeforeunload = function(){
    		  if (step != null && step > 1) {
    			  return confirm('Do you want to leave without save?');
    		  }
    		};
    	  operationId = getUriParam(EnmUriParam.OPERATION);
    	  operationToken = getUriParam(EnmUriParam.OPERATION_TOKEN);
    	  
    	  if (operationId != null && operationToken != null) {
    		  commonService.getTokenObject(operationToken, operationId,
  					function(reservation) {
      			  		self.reservation = reservation;
      			  		if (initializeListener != null) {
      			  			initializeListener(self.reservation.clientUser.firstName, self.reservation.clientUser.lastName);
      			  		}
  					}
      		  ); 
    	  } else {
    		  DialogUtil.error('Missing parameters in url!', function() {
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
	  	
	var validationTimer = null;
  	self.onBtnFinishClick = function() {
  		var paymentFormValid = validateListener();
  		var firstName = StringUtil.trim($("#txtFirstName").val());
  		var lastName = StringUtil.trim($("#txtLastName").val());
  		if (firstName != '' && lastName != '' && paymentFormValid) {
  			NProgress.start(4000, 5);
  			submitListener(self.finishReservation);
  			validationTimer = setTimeout(function() {
  				NProgress.done(true);
  	  			DialogUtil.warn( 'Please fill mandatory parameters!');
  	  		}, 5000);
  		} else {
  			DialogUtil.warn('Please fill mandatory fields.');
  		}
  	};
  	
  	self.finishReservation = function(zipCode) {
  		if (validationTimer != null) {
  			clearTimeout(validationTimer);
  		}
  		var paymentFormValid = validateListener();
  		var firstName = StringUtil.trim($("#txtFirstName").val());
  		var lastName = StringUtil.trim($("#txtLastName").val());
  		
  		var uiPaymentMethod = {
			cardHolderFirstName : firstName,
			cardHolderLastName : lastName,
			zipCode : zipCode,
			fraudSessionId : fraudSessionId
		};
			
		var reservation = {
			messageText : 'I would like to book your place.',
			operationToken : operationToken,
			uiPaymentMethod : uiPaymentMethod
  		};
  		reservationService.finishReservation(reservation,
				function(isSuccess) {
					if (isSuccess) {
						DialogUtil.success( 'Congratulations! Your request is sent to host.', function() {
							var url = webApplicationUrlPrefix + '/dashboard/messages';
							openWindow(url, true);
						});
					}
				}
		  );
  		
  	};
  	
  	self.onBtnFinishClickStripe = function() {
  		var paymentFormValid = validateListener();
  		if (paymentFormValid) {
  			NProgress.start(4000, 5);
  			submitListener(self.finishReservationStripe);
  		} else {
  			DialogUtil.warn('Please fill mandatory fields.');
  		}
  	};
  	
  	self.finishReservationStripe = function(operationResult) {
  		if (operationResult.resultCode == EnmOperationResultCode.SUCCESS) {
  			var uiPaymentMethod = operationResult.resultValue;
				
			var reservation = {
				messageText : 'I would like to book your place.',
				operationToken : operationToken,
				uiPaymentMethod : uiPaymentMethod
	  		};
	  		reservationService.finishReservation(reservation,
					function(isSuccess) {
						if (isSuccess) {
							DialogUtil.success( 'Congratulations! Your request is sent to host.', function() {
								var url = webApplicationUrlPrefix + '/dashboard/messages';
								openWindow(url, true);
							});
						}
					}
			  );
  		} else {
  			
  		}
  	};
  	
  	self.getPaymentToken = function(callBack) {
  		paymentService.getPaymentToken(operationToken, operationId, function(isSuccess, paymentToken) {
  			if (isSuccess) {
  				callBack(paymentToken);
  			} else {
  				DialogUtil.warn( 'We can not continue payment operation currently. Please try again later!', function() {
  					closeWindow();
  				});
  			}
  		});
  	};
  	
  	self.addListener = function(funcSubmit, funcValidate, funcInitialize) {
  		submitListener = funcSubmit;
  		validateListener = funcValidate;
  		initializeListener = funcInitialize;
  		if (self.reservation != null && initializeListener != null) {
  			initializeListener(self.reservation.clientUser.firstName, self.reservation.clientUser.lastName);
  		}
  	};
  	
  	self.getBlueSnapFraudLink = function() {
  		if (self.reservation != null && self.reservation.hostUser.payoutMethod.externalSystemId == 2) {
  			return 'https://www.bluesnap.com/servlet/logo.htm?s=' + fraudSessionId;
  		} else {
  			return "";
  		}
  	};
  	
  	self.getBlueSnapFraudLogoLink = function() {
  		if (self.reservation != null && self.reservation.hostUser.payoutMethod.externalSystemId == 2) {
  			return 'https://www.bluesnap.com/servlet/logo.gif?s=' + fraudSessionId;
  		} else {
  			return "";
  		}
  	};
  	
    self.initialize();
      
  }]);

function getPaymentToken(callBack) {
	var scope = angular.element( $('#divBody') ).scope();
	scope.ctrl.getPaymentToken(callBack);
}

function addListener(funcSubmit, funcValidate, funcInitialize) {
	var scope = angular.element( $('#divBody') ).scope();
	scope.ctrl.addListener(funcSubmit, funcValidate, funcInitialize);
}