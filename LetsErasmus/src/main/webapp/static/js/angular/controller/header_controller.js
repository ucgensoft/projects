App.controller('headerCtrl', ['$scope', 'userService', '$sce', '$compile', 'favoriteService', 'complaintService', 
                              function($scope, userService, sce, compile, favoriteService, complaintService) {
      var self = this;
      self.html = '';
      var auth2 = null;
      var facebookLoginResponse = null;
      
      self.initialize = function() {
    	  if (loginType != '') {
    		  self.listFavorite(
    				  function(favoriteMap) {
    			  		userFavoriteMap = favoriteMap;
			  		  }
			  	  );
    	  }
		  gapi.load('auth2', function() {
    	      auth2 = gapi.auth2.init({
    	        client_id: googleId,
    	        cookiepolicy: 'single_host_origin',
    	        // Request scopes in addition to 'profile' and 'email'
    	        //scope: 'additional_scope'
    	      });
    	      // some pages use google library in body part.
    	      if (typeof onGoogleLibraryLoaded == 'function') {
    	    	  onGoogleLibraryLoaded();
    	      }
    	    });
		  
		  FB.init({
			    appId: facebookId,
			    version: 'v2.7'
			  });
		 
		  self.checkUrlOperation();
	 };
	 
	 self.checkUrlOperation = function() {
		 var paramOp = getUriParam("op"); 
		  
	   	  if (paramOp != null) {
	   		  if (paramOp == EnmOperation.CONFIRM_EMAIL) {
	   			  var id = getUriParam("user");
	   			  var code = getUriParam("code");
	   			  if (id != null && id != '' && code != null && code != '') {
	   				  userService.confirmEmail(id, code).then(
							function(operationResult) {
								if (isResultSuccess(operationResult, false)) {
									DialogUtil.info('Success', 'Congradulations! Your mail is verified successfully!', 'OK', function() {
										var newUrl = clearUrlParameter(location.href, null);
										if (sessionActive) {
											newUrl = clearUrlParameter(location.href, null);
										} else {
											newUrl += '?op=' + EnmOperation.LOGIN; 
										}
										openWindow(newUrl, true);
										
									});
								}
							}, function(errResponse) {
								
							});
	   				  sessionActive
	   			  }
	       	  } else if (paramOp == EnmOperation.LOGIN) {
	       		  openLoginWindow();
	       	  }
	   	  } 
	 };
      
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
    		  self.attachGoogleSignin('linkGoogleSignin', self.signup);
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
    	  if (auth2 != null) {
    		  ajaxHtml(webApplicationUrlPrefix + '/pages/Login.xhtml', 'divModalContent', function() {
        		  self.attachGoogleSignin('linkGoogleSignin', self.signup);
        		  openModal();  
        	  });
    	  } else {
    		  setTimeout(self.openLoginWindow, 400);
    	  }
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
		        	self.signup(user);
    				
    				});
    		} else {
    			FB.login(self.facebookLoginCallback, {scope: 'public_profile, email'});
    		}
    	};
      
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
  		userService.createUser(user,
				function(isSuccess) {
					if (isSuccess) {
						if (globalRedirectUrl != null) {
							var redirectUrl = globalRedirectUrl;
							closeModal();
							openWindow(redirectUrl, true);
						} else {
							location.reload();
						}
					}
				}
		  );
  	};
  	
  	self.loginWithLocalAccount = function() {
  		var email = StringUtil.trim($("#txtEmail").val());
  		var password = StringUtil.trim($("#txtPassword").val());
  		
  		if (email == '' || password == '') {
  			DialogUtil.warn('Warning', 'Please fill mandatory fields!', 'OK', null);
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
  		userService.login(user,
					function(isSuccess) {
						if (isSuccess) {
							if (globalRedirectUrl != null) {
								openWindow(globalRedirectUrl, true);
							} else {
								location.reload();
							}
						}
					}
			  );
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
							DialogUtil.error('Error', 'Operation could not be completed!', 'OK', null);
						}
					} else {
						DialogUtil.error('Error', 'Operation could not be completed!', 'OK', null);
					}
				}, function(errResponse) {
					NProgress.done(true);
					DialogUtil.error('Error', 'Operation could not be completed!', 'OK', null);
				}); 
  	};
  	  
  	self.attachGoogleSignin = function(elementId, callBack) {
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
  			        			googleEmail : email,
  			        			gender : gender,
  			        			googleId : googleId,
  			        			profileImageUrl : profileImageUrl,
  			        			loginType : loginType
  			        	};
  			        	callBack(user);
  		        });
  	        });
  	    	  
  	        }, function(error) {
  	        	DialogUtil.error('Error', JSON.stringify(error, undefined, 2), 'OK', null);
  	        });
  	  };
  	  
  	  self.addFavorite = function(entityType, entityId, callBack) {
  		  var favorite = {
  				entityType : entityType, 
  				entityId : entityId
  		  };
  		favoriteService.addFavorite(favorite, 
  			  function(favoriteMap) {
  				userFavoriteMap = favoriteMap;
  				if (callBack) {
  					callBack(result);
  				  }
  	  		  }
  	  	  );
  	  };
  	  
  	self.removeFavorite = function(entityType, entityId, callBack) {
		  var favorite = {
				entityType : entityType, 
				entityId : entityId
		  };
		favoriteService.removeFavorite(favorite, 
			function(favoriteMap) {
					userFavoriteMap = favoriteMap;
				  if (callBack) {
					callBack(favoriteMap);
				  }
	  		  }
	  	  );
	  };
	  
	  self.listFavorite = function(callBack) {
		favoriteService.listFavorite(
			  function(favoriteMap) {
				  if (callBack) {
					callBack(favoriteMap);
				  }
	  		  }
	  	  );
	  };
	  
	  self.listComplaint = function(callBack) {
		complaintService.listComplaint(
			  function(complaintMap) {
				  userComplaintMap = complaintMap;
				  if (callBack) {
					callBack(complaintMap);
				  }
	  		  }
	  	  );
	  };
	  
	  self.createComplaint = function() {
		  var entityType = $('#hiddenComplaintEntityType').val();
		  var entityId = $('#hiddenComplaintEntityId').val();
		  var description = $('#txtComplaintDescription').val();
  		  var complaint = {
  				entityType : entityType, 
  				entityId : entityId,
  				description : description
  		  };
  		complaintService.createComplaint(complaint, 
  			  function(complaintMap) {
  				DialogUtil.info('Success', 'Your complaint has been reported to LetsErasmus team. ' 
  						+ 'Necessary action will be taken as soon as possible. Thanks for reporting.', 'OK', 
  						function() {
  							location.reload();
  						}
  				);
  	  		  }
  	  	  );
  	  };
  	  
  	self.deleteComplaint = function(entityType, entityId, callBack) {
		  var complaint = {
				entityType : entityType, 
				entityId : entityId
		  };
		complaintService.deleteComplaint(complaint, 
			function(complaintMap) {
					userComplaintMap = complaintMap;
				  if (callBack) {
					callBack(complaintMap);
				  }
	  		  }
	  	  );
	  };
	  
	  self.onBecomeHostClicked = function() {
		  globalRedirectUrl = webApplicationUrlPrefix + '/pages/Place.xhtml';
		  self.openLoginWindow();
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

function addFavorite(entityType, entityId, callBack) {
	var scope = angular.element( $('#divPageHeader') ).scope();
	scope.ctrl.addFavorite(entityType, entityId, callBack);
}

function removeFavorite(entityType, entityId, callBack) {
	var scope = angular.element( $('#divPageHeader') ).scope();
	scope.ctrl.removeFavorite(entityType, entityId, callBack);
}

function createComplaint() {
	var scope = angular.element( $('#divPageHeader') ).scope();
	scope.ctrl.createComplaint();
}

function deleteComplaint(entityType, entityId, callBack) {
	var scope = angular.element( $('#divPageHeader') ).scope();
	scope.ctrl.deleteComplaint(entityType, entityId, callBack);
}

function listComplaint(callBack) {
	var scope = angular.element( $('#divPageHeader') ).scope();
	scope.ctrl.listComplaint(callBack);
}