App.controller('verificationCtrl', ['$scope', 'userService', 'commonService', '$sce', '$compile', 
                                function($scope, userService, commonService, sce, compile) {
      var self = this;
      self.countryList = [];
      self.dummyModel = null;
      
      self.initialize = function() {
    	  commonService.listCountry( function(countryList) {
				if (countryList) {
					self.countryList = countryList;
				}
			} ); 
	 };
  	  
  	self.openAddMsisdnPart = function() {
  		$('#divAddMsisdn').css('display', 'block');
  		$('#divVerificationContainer').css('display', 'block');
  		$('#divNoPhoneNumber').css('display', 'none')
  	};
  	
  	self.cancelAddMsisdn = function() {
  		$('#divVerificationContainer').css('display', 'none');
  		$('#divAddMsisdn').css('display', 'none');
  		$('#divNoPhoneNumber').css('display', 'block')
  	};
  	
  	self.removeMsisdn = function() {
  		userService.removeMsisdn(function(isSuccess) {
					if (isSuccess) {
						location.reload();
					}
				}
		  );
  	};
  	
  	self.sendMsisdnVerificationCode = function() {
  		var phoneNumber = ' ';
  		if ($('#divNoPhoneNumber').length > 0) {
  			var prefix = $('#divCountryPrefix')[0].innerText;
  	  		var msisdn = $('#txtMsisdn').val();
	  	  	if (prefix == null || prefix == '' || StringUtil.trim(msisdn) == '') {
	  			DialogUtil.warn('Warning', 'Select a country and type your phone number please!', 'OK', null);
	  			return;
	  		} else {
	  			phoneNumber = prefix + msisdn;
	  		}
  		}
  		userService.sendMsisdnVerificationCode(phoneNumber,
				function(isSuccess) {
					if (isSuccess) {
						$('#divVerificationContainer').css('display', 'block');
				  		$('#divVerifyMsisdn').css('display', 'block');
				  		$('#divAddMsisdn').css('display', 'none');
				  		
				  		$('#divDisplayMsisdn').css('display', 'none');
					}
				}
		  );
  	};
  	
  	self.sendEmailVerificationCode = function() {
  		userService.sendEmailVerificationCode(function(isSuccess) {
					if (isSuccess) {
						$('#divResendEmailCodeSuccess').css('display', 'block');
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
  							location.reload();
  						}
  					}
  			  );
  		} else {
  			DialogUtil.warn('Warning', 'Please type the verification code!', 'OK', null);
  		}
  	};
  	
  	self.cancelVerifyMsisdn = function() {
  		if ($('#divNoPhoneNumber').length > 0) {
  			$('#divAddMsisdn').css('display', 'block');
  			$('#divVerificationContainer').css('display', 'block');
  		} else {
  			$('#divDisplayMsisdn').css('display', 'block');
  			$('#divVerificationContainer').css('display', 'none');
  		}
  		$('#divVerifyMsisdn').css('display', 'none');
  	};
  	
  	self.onCountryChange = function(param1) {
  		$('#divCountryPrefix')[0].innerText= $('#cmbCountry').val();
  	};
  	
    self.initialize();
      
  }]);