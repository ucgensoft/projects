App.controller('verificationCtrl', ['$scope', 'userService', 'commonService', '$sce', '$compile', 
                                function($scope, userService, commonService, sce, compile) {
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
    		DialogUtil.error('Error', 'Invalid operation parameter!', 'OK', function() {
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
  			DialogUtil.warn('Warning', 'Select a country and type your phone number please!', 'OK', null);
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
  								location.reload();
  							} else {
  								var url = self.getOperationUrl();
  	  							openWindow(url, true);
  							}
  						}
  					}
  			  );
  		} else {
  			DialogUtil.warn('Warning', 'Please type the verification code!', 'OK', null);
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
						DialogUtil.info('Success', 'Verification code is sent to your email address. Please click the verification link in the mail.', 'OK');
					}
		});
  	};
  	
  	self.getOperationUrl = function() {
  		var url = null;
  		var parameters = document.location.href.substring(document.location.href.indexOf('?') + 1);
  		// url will vary for different operation types.
  		url = webApplicationUrlPrefix + '/pages/Payment.xhtml?' + parameters;
		
  		return url;
  	};
  	
    self.initialize();
      
  }]);