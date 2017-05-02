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
    		  DialogUtil.error('Error', 'Missing parameters in url!', 'OK', function() {
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
  	  			DialogUtil.warn('Warning', 'Please fill mandatory parameters!', 'OK');
  	  		}, 1500);
  		} else {
  			DialogUtil.info('Warning', 'Please fill mandatory fields.', 'OK');
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
			zipCode : zipCode
		};
			
		var reservation = {
			messageText : 'I would like to book your place.',
			operationToken : operationToken,
			uiPaymentMethod : uiPaymentMethod
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
  	
  	self.onBtnFinishClickStripe = function() {
  		var paymentFormValid = validateListener();
  		if (paymentFormValid) {
  			NProgress.start(4000, 5);
  			submitListener(self.finishReservationStripe);
  		} else {
  			DialogUtil.info('Warning', 'Please fill mandatory fields.', 'OK');
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
							DialogUtil.info('Success', 'Congratulations! Your request is sent to host.', 'OK', function() {
								var url = webApplicationUrlPrefix + '/pages/dashboard/MessageList.xhtml';
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
  				DialogUtil.warn('Warning', 'We can not continue payment operation currently. Please try again later!', 'OK', function() {
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