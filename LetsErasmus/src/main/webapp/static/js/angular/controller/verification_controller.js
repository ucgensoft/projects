App.controller('verificationCtrl', ['$scope', 'userService', 'commonService', 
                                function($scope, userService, commonService) {
      var self = this;
      
      self.countryList = [];
      self.dummyModel = null;
      var checkEmailTimer = null;
      var activeOperation = null;
      var operationToken = null;
      
      
      self.initialize = function() {
    	  
    	  activeOperation = getUriParam(EnmUriParam.OPERATION);
    	  operationToken = getUriParam(EnmUriParam.OPERATION_TOKEN);
    	  
    	  if ((activeOperation == EnmOperation.CREATE_RESERVATION && operationToken != null) 
    			  || activeOperation == EnmOperation.CREATE_PLACE) {
    		  if ($('#divVerifyMsisdn').length > 0) {
        		  commonService.listCountry( function(countryList) {
    					if (countryList) {
    						self.countryList = countryList;
    						if (userMsisdn != '') {
    							$('#cmbCountry').val(userMsisdn);
    						}
    					}
    				}
        		  );
        	  }
        	  
        	  if ($('#divVerifyMsisdn').length > 0) {
        		  setTimeout(function() {
        			  	$('#cmbCountry').val(userCountryCode);
        			  }, 500);
        	  }
        	  
        	  if ($('#divVerifyEmail').length > 0) {
        		  checkEmailTimer = setInterval(self.checkEmailVerification, 4000);
        	  }
    	  } else {
    		DialogUtil.error('Invalid operation parameter!', function() {
    			closeWindow();
    		});  
    	  }
    	      	  
	 };
	 
	 self.checkEmailVerification = function() {
		 userService.isEmailVerified(
					function(isSuccess) {
						if (isSuccess) {
							clearInterval(checkEmailTimer);
							$('#imgLoading').addClass('hidden');
							$('#imgEmailVerified').removeClass('hidden');
							setTimeout(function() {
								var url = self.getOperationUrl();
  	  							openWindow(url, true);
							}, 1000);
						}
					}
			  );
	 };
  	  	  	
  	self.sendMsisdnVerificationCode = function() {
  		
  		var prefix = $('#cmbCountry').val();
	  	var msisdn = $('#txtMsisdn').val();
  	  	
	  	if (prefix == null || StringUtil.trim(prefix) == '' || StringUtil.trim(msisdn) == '') {
  			DialogUtil.warn( 'Select a country and type your phone number please!', null);
  			return;
  		}
  	  	
  		userService.sendMsisdnVerificationCode(prefix, msisdn,
				function(isSuccess) {
					if (isSuccess) {
						$('#divVerificationCode').css('display', 'block');
				  		$('#divVerifyMsisdn').css('display', 'none');
					}
				}
		  );
  	};
  	
  	self.verifyMsisdnCode = function() {
  		var code = $('#txtVerificationCode').val();
  		if (StringUtil.trim(code) != '') {
  			userService.verifyMsisdnCode(code,
  					function(isSuccess) {
  						if (isSuccess) {
  							if (emailVerified == 'N') {
  								reloadPage();
  							} else {
  								var url = self.getOperationUrl();
  	  							openWindow(url, true);
  							}
  						}
  					}
  			  );
  		} else {
  			DialogUtil.warn( 'Please type the verification code!', null);
  		}
  	};
  	
  	self.onBtnChangeMsisdnClicked = function() {
  		$('#divVerificationCode').css('display', 'none');
  		$('#divVerifyMsisdn').css('display', 'block');
  	};
  	
  	self.onBtnSendCodeAgain = function() {
  		self.sendMsisdnVerificationCode();
  	};
  	
  	self.sendEmailVerificationCode = function() {
  		userService.sendEmailVerificationCode(function(isSuccess) {
					if (isSuccess) {
						DialogUtil.success( 'Verification code is sent to your email address. Please click the verification link in the mail.');
					}
		});
  	};
  	
  	self.getOperationUrl = function() {
  		var url = null;
  		var parameters = document.location.href.substring(document.location.href.indexOf('?'));
  		if (activeOperation == EnmOperation.CREATE_PLACE) {
  			url = webApplicationUrlPrefix + '/pages/Place.html' + parameters;
  		} else if (activeOperation == EnmOperation.CREATE_RESERVATION) {
  			url = webApplicationUrlPrefix + '/pages/Payment.html' + parameters;
  		}
		
  		return url;
  	};
  	
    self.initialize();
      
  }]);