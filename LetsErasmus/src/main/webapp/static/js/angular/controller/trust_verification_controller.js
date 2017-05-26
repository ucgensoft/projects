App.controller('trustAndverificationCtrl', ['$scope', 'userService', 'commonService', '$sce', '$compile', 
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
						reloadPage();
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
	  			DialogUtil.warn( 'Select a country and type your phone number please!', null);
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
  							reloadPage();
  						}
  					}
  			  );
  		} else {
  			DialogUtil.warn( 'Please type the verification code!', null);
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
  	
  	self.conntectGoogleAccount = function(user) {
  		if (user) {
  			if (user.googleEmail != null && user.googleId != null) {
  				userService.connectGoogleAccount(user,
  	  					function(isSuccess) {
  	  						if (isSuccess) {
  	  							reloadPage();
  	  						}
  	  					}
  	  			  );
  			} else {
  				DialogUtil.warn( 'Google email and ID are mandatory!');
  			}
  		}
  	};
  	
  	self.disconnectGoogleAccount = function() {
  		userService.disconnectGoogleAccount(
				function(isSuccess) {
					if (isSuccess) {
						reloadPage();
					}
				}
		  );
  	};
  	
  	self.loginWithFacebook = function() {
  	  FB.login(self.facebookLoginCallback, {scope: 'public_profile, email'});
    };

    self.facebookLoginCallback = function(response) {
  		if (response.status == 'connected') {
  			facebookTokenId = response.authResponse.accessToken;
  			FB.api('/me', {fields: 'email, id, cover,name,first_name,last_name,age_range,link,gender' 
  				+ ',locale,picture,timezone,updated_time,verified'}, function(resp) {
  				
  				var email = resp.email;
		        	var firstName = resp.first_name;
		        	var lastName = resp.last_name;
		        	var gender = null;
		        	var facebookId = resp.id;
		        	var loginType = EnmLoginType.FACEBOOK;
		        	var profileImageUrl = resp.picture.data.url;
		        	
		        	if (resp.gender) {
		        		if (resp.gender.toUpperCase() == 'MALE') {
		        			gender = 'M';
		        		} else if (resp.gender.toUpperCase() == 'FEMALE') {
		        			gender = 'F';
		        		}
		        	}
		        	
		        	var user = {
		        			firstName : firstName,
		        			lastName : lastName,
		        			facebookEmail : email,
		        			gender : gender,
		        			facebookId : facebookId,
		        			facebookTokenId : facebookTokenId,
		        			profileImageUrl : profileImageUrl,
		        			loginType : loginType
		        	};
		        	userService.connectFacebookAccount(user,
		    				function(isSuccess) {
		    					if (isSuccess) {
		    						reloadPage();
		    					}
		    				}
		    		  );
  				
  				});
  		} else {
  			FB.login(self.facebookLoginCallback, {scope: 'public_profile, email'});
  		}
  	};
  	
  	self.disconnectFacebookAccount = function() {
  		userService.disconnectFacebookAccount(
				function(isSuccess) {
					if (isSuccess) {
						reloadPage();
					}
				}
		  );
  	};
  	
    self.initialize();
      
  }]);

function onGoogleLibraryLoaded() {
	if($('#btnConnectGoogleAccount').length > 0) {
		var bodyScope = angular.element( $('#divBody') ).scope();
		var headerScope = angular.element( $('#divPageHeader') ).scope();
		headerScope.ctrl.attachGoogleSignin('btnConnectGoogleAccount', bodyScope.ctrl.conntectGoogleAccount);
	}
}