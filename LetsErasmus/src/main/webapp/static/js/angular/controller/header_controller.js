App.controller('headerCtrl', ['$scope', 'userService', '$sce', '$compile', function($scope, userService, sce, compile) {
      var self = this;
      self.html = '';
      var auth2 = null;
      var facebookLoginResponse = null;
      
      self.initialize = function() {
		  gapi.load('auth2', function() {
    	      auth2 = gapi.auth2.init({
    	        client_id: googleId,
    	        cookiepolicy: 'single_host_origin',
    	        // Request scopes in addition to 'profile' and 'email'
    	        //scope: 'additional_scope'
    	      });
    	    });
		  
		  FB.init({
			    appId: facebookId,
			    version: 'v2.7' // or v2.1, v2.2, v2.3, ...
			  });
	 };
      
      self.attachGoogleSignin = function (elementId) {
    	  var element = $('#' + elementId)[0];
  	      auth2.attachClickHandler(element, {},
  	        function(googleUser) {
	  	    	gapi.client.load('plus', 'v1', function () {
		            var request = gapi.client.plus.people.get({
		                'userId': 'me'
		            });
		            request.execute(function (resp) {
		            	var email = resp.emails[0].value;
			        	var firstName = resp.name.givenName;
			        	var lastName = resp.name.familyName;
			        	var gender = null;
			        	var googleId = resp.id;
			        	var loginType = EnmLoginType.GOOGLE;
			        	var profileImageUrl = resp.image.url;
			        	
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
			        			email : email,
			        			gender : gender,
			        			googleId : googleId,
			        			profileImageUrl : profileImageUrl,
			        			loginType : loginType
			        	};
			        	self.signup(user);
		        });
	        });
  	    	  
  	        }, function(error) {
  	          alert(JSON.stringify(error, undefined, 2));
  	        });
  	  }
      
      self.openSignUpWindow = function() {
    	  /*
    	  getHtml(webApplicationUrlPrefix + '/pages/Signup.xhtml', function(html) {
    		  $('#divModalContent').html(sce.trustAsHtml(html));
    		});
    		*/
    	  //var htmlcontent = $('#divModalContent');
    	  //htmlcontent.load(webApplicationUrlPrefix + '/pages/Signup.xhtml');
    	  //compile(htmlcontent.contents())($scope);
    	  
    	  ajaxHtml(webApplicationUrlPrefix + '/pages/Signup.xhtml', 'divModalContent', function() {
    		  self.attachGoogleSignin('linkGoogleSignin');
    		  openModal();  
    	  });
    	  /*
    	  getHtml(webApplicationUrlPrefix + '/pages/Signup.xhtml', function(html) {
    		  self.html = html;
        	  setTimeout(function() {
        	compile(htmlcontent.contents())($scope);
            	  openModal(); 
        	  }, 500);
    	  });
    	  */
      };
      
      self.openLoginWindow = function() {
    	  ajaxHtml(webApplicationUrlPrefix + '/pages/Login.xhtml', 'divModalContent', function() {
    		  self.attachGoogleSignin('linkGoogleSignin');
    		  openModal();  
    	  });
      };
      
      self.loginWithFacebook = function() {
    	  FB.login(self.facebookLoginCallback, {scope: 'public_profile, email'});
    	}

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
		        			email : email,
		        			gender : gender,
		        			facebookId : facebookId,
		        			facebookTokenId : facebookTokenId,
		        			profileImageUrl : profileImageUrl,
		        			loginType : loginType
		        	};
		        	self.signup(user);
    				
    				});
    		} else {
    			FB.login(self.facebookLoginCallback, {scope: 'public_profile, email'});
    		}
    	}
      
      self.signupWithLocalAccount = function() {
    	  
  		var userFirstName = StringUtil.trim($("#txtFirstName").val());
  		var userLastName = StringUtil.trim($("#txtLastName").val());
  		var email = StringUtil.trim($("#txtEmail").val());
  		var password = StringUtil.trim($("#txtPassword").val());
  		
  		if (userFirstName == '' || userLastName == '' || email == '' || password == '') {
  			DialogUtil.showMessage(DialogUtil.MESSAGE_TYPE.WARNING, 'Warning', 'Please fill mandatory fields!');
  		} else {
  			var user = {
  				firstName : userFirstName,
  				lastName : userLastName,
  				email : email,
  				password : password,
  				loginType : EnmLoginType.LOCAL_ACCOUNT
  			};
  			self.signup(user);
  		}
  		
  	};
  	
  	self.signup = function(user) {
  	  
		NProgress.start(2000, 10);
		userService.createUser(user).then(
				function(operationResult) {
					NProgress.done(true); 
					if (operationResult.resultCode == EnmOperationResultCode.SUCCESS) {
						NProgress.done(true); 
						if (operationResult.resultCode == EnmOperationResultCode.SUCCESS) {
							location.reload();
						} else {
							alert('Operation could not be completed. Please try again later!');
						}
					} else {
						alert('Operation could not be completed. Please try again later!');
					}
				}, function(errResponse) {
					NProgress.done(true);
					alert('Operation could not be completed. Please try again later!');
				});
  	};
  	
  	self.loginWithLocalAccount = function() {
  		var email = StringUtil.trim($("#txtEmail").val());
  		var password = StringUtil.trim($("#txtPassword").val());
  		
  		if (email == '' || password == '') {
  			DialogUtil.showMessage(DialogUtil.MESSAGE_TYPE.WARNING, 'Warning', 'Please fill mandatory fields!');
  		} else {
  			var user = {
  				email : email,
  				password : password,
  				loginType : EnmLoginType.LOCAL_ACCOUNT
  			};
  			self.login(user);
  		}
  	};
  	
  	self.login = function(user) {
  		NProgress.start(2000, 10);
		userService.login(user).then(
				function(operationResult) {
					NProgress.done(true); 
					if (operationResult.resultCode == EnmOperationResultCode.SUCCESS) {
						NProgress.done(true); 
						if (operationResult.resultCode == EnmOperationResultCode.SUCCESS) {
							location.reload();
						} else {
							alert('Operation could not be completed. Please try again later!');
						}
					} else {
						alert('Operation could not be completed. Please try again later!');
					}
				}, function(errResponse) {
					NProgress.done(true);
					alert('Operation could not be completed. Please try again later!');
				}); 
  	};
  	
  	self.logout = function() {
  		NProgress.start(2000, 10);
		userService.logout().then(
				function(operationResult) {
					NProgress.done(true); 
					if (operationResult.resultCode == EnmOperationResultCode.SUCCESS) {
						NProgress.done(true); 
						if (operationResult.resultCode == EnmOperationResultCode.SUCCESS) {
							if (loginType == EnmLoginType.GOOGLE) {
								$('#frameGoogleLogout').attr('src', 'https://accounts.google.com/logout');
								setTimeout(function() {
									location.href = webApplicationUrlPrefix + "/pages/Main.xhtml";
								}, 500);
							} else if (loginType == EnmLoginType.FACEBOOK) {
								$('#frameGoogleLogout').attr('src', 'https://www.facebook.com/logout.php?next='+ urlEncodedUrlPrefix + '&access_token=' + facebookTokenId);
								setTimeout(function() {
									location.href = webApplicationUrlPrefix + "/pages/Main.xhtml";
								}, 500);
							} else {
								location.href = webApplicationUrlPrefix + "/pages/Main.xhtml";
							}
						} else {
							alert('Operation could not be completed!');
						}
					} else {
						alert('Operation could not be completed!');
					}
				}, function(errResponse) {
					NProgress.done(true);
					alert('Operation could not be completed!');
				}); 
  	};
  	  
     //self.initialize();
      
  }]);

function openSignUpWindow() {
	var scope = angular.element( $('#divPageHeader') ).scope();
	scope.ctrl.openSignUpWindow();
}

function openLoginWindow() {
	var scope = angular.element( $('#divPageHeader') ).scope();
	scope.ctrl.openLoginWindow();
}

function signup() {
	var scope = angular.element( $('#divPageHeader') ).scope();
	scope.ctrl.signupWithLocalAccount();
}

function login() {
	var scope = angular.element( $('#divPageHeader') ).scope();
	scope.ctrl.loginWithLocalAccount();
}

function loginWithFacebook() {
	var scope = angular.element( $('#divPageHeader') ).scope();
	scope.ctrl.loginWithFacebook();
}

function onPageLoadHeader() {
	var scope = angular.element( $('#divPageHeader') ).scope();
	scope.ctrl.initialize();
}