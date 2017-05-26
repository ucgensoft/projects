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
	   				  userService.confirmEmail(id, code,
							function(result) {
								if (result) {
									DialogUtil.success('Congratulations! Your mail is verified successfully!', function() {
										var newUrl = clearUrlParameter(location.href, null);
										if (sessionActive) {
											newUrl = clearUrlParameter(location.href, null);
										} else {
											newUrl += '?op=' + EnmOperation.LOGIN; 
										}
										openWindow(newUrl, true);
										
									});
								}
							});
	   			  }
	       	  } else if (paramOp == EnmOperation.LOGIN) {
	       		  if (loginUserId == null || loginUserId == '') {
	       			openLoginWindow();
	       		  }
	       	  } else if (paramOp == EnmOperation.RESET_PASSWORD) {
	       		  var userId = getUriParam("user");
	   			  var code = getUriParam("code");
	   			  if (userId != null && userId != '' && code != null && code != '') {
	   				userService.resetPassword(userId, code,
							function(isSuccess) {
								if (isSuccess) {
									DialogUtil.success( 'We reset your password successfully and new password is sent to your email!', function() {
										var newUrl = clearUrlParameter(location.href, null);
										newUrl += '?op=' + EnmOperation.LOGIN; 
										openWindow(newUrl, true);
									});
								}
							}
					  );
	   			  }
	       	  }
	   	  } 
	 };
      
      self.openSignUpWindow = function() {
    	  /*
    	  getHtml(webApplicationUrlPrefix + '/pages/Signup.html', function(html) {
    		  $('#divModalContent').html(sce.trustAsHtml(html));
    		});
    		*/
    	  //var htmlcontent = $('#divModalContent');
    	  //htmlcontent.load(webApplicationUrlPrefix + '/pages/Signup.html');
    	  //compile(htmlcontent.contents())($scope);
    	  
    	  ajaxHtml(webApplicationUrlPrefix + '/pages/Signup.html', 'divModalContent', function() {
    		  self.attachGoogleSignin('linkGoogleSignin', self.signup);
    		  openModal();  
    	  });
    	  /*
    	  getHtml(webApplicationUrlPrefix + '/pages/Signup.html', function(html) {
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
    		  ajaxHtml(webApplicationUrlPrefix + '/pages/Login.html', 'divModalContent', function() {
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
		        	
		        	if (resp.email) {
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
		        	} else {
		        		DialogUtil.warn('Your facebook account is not verified. Only verified accounts are allowed to login!');
		        	}
		        	
    				});
    		} else {
    			//FB.login(self.facebookLoginCallback, {scope: 'public_profile, email'});
    		}
    	};
      
      self.signupWithLocalAccount = function() {
    	  
  		var userFirstName = StringUtil.trim($("#txtFirstName").val());
  		var userLastName = StringUtil.trim($("#txtLastName").val());
  		var email = StringUtil.trim($("#txtEmail").val());
  		var password = StringUtil.trim($("#txtPassword").val());
  		
  		if (userFirstName == '' || userLastName == '' || email == '' || password == '') {
  			DialogUtil.warn('Please fill mandatory fields!');
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
							var reloadUrl = location.href;
							var paramOp = getUriParam('op'); 
					   	  	if (paramOp != null) {
					   	  		if (paramOp == EnmOperation.LOGIN) {
					   	  			reloadUrl = clearUrlParameter(reloadUrl, 'op');
					   	  		}
					   	  	}	  
						   	openWindow(reloadUrl, true);
						}
					}
				}
		  );
  	};
  	
  	self.loginWithLocalAccount = function() {
  		var email = StringUtil.trim($("#txtEmail").val());
  		var password = StringUtil.trim($("#txtPassword").val());
  		
  		if (email == '' || password == '') {
  			DialogUtil.warn( 'Please fill mandatory fields!');
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
								var reloadUrl = location.href;
								var paramOp = getUriParam('op'); 
						   	  	if (paramOp != null) {
						   	  		if (paramOp == EnmOperation.LOGIN) {
						   	  			reloadUrl = clearUrlParameter(reloadUrl, 'op');
						   	  		}
						   	  	}	  
							   	openWindow(reloadUrl, true);
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
									location.href = webApplicationUrlPrefix + "/pages/Main.html";
								}, 500);
							} else if (loginType == EnmLoginType.FACEBOOK) {
								$('#frameGoogleLogout').attr('src', 'https://www.facebook.com/logout.php?next='+ urlEncodedUrlPrefix + '&access_token=' + facebookTokenId);
								setTimeout(function() {
									location.href = webApplicationUrlPrefix + "/pages/Main.html";
								}, 500);
							} else {
								location.href = webApplicationUrlPrefix + "/pages/Main.html";
							}
						} else {
							DialogUtil.error('Operation could not be completed!');
						}
					} else {
						DialogUtil.error('Operation could not be completed!');
					}
				}, function(errResponse) {
					NProgress.done(true);
					DialogUtil.error('Operation could not be completed!');
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
  	        	DialogUtil.error(JSON.stringify(error, undefined, 2));
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
  					callBack(true);
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
	  
	  self.listComplaint = function(entityType, callBack) {
		complaintService.listComplaint(entityType,
			  function(complaintMap) {
				  userComplaintMap = complaintMap;
				  if (callBack) {
					callBack(complaintMap);
				  }
	  		  }
	  	  );
	  };
	  
	  self.openComplaintWindow = function(entityType, entityId) {
		  if (loginUserId != null) {
			  ajaxHtml(webApplicationUrlPrefix + '/static/html/Complaint.htm', 'divCommonModal', function() {
		   		     $('#hiddenComplaintEntityType').val(entityType);
		   		     $('#hiddenComplaintEntityId').val(entityId);
		    		 $('#divCommonModal').css('display', '');
		     	});
		  } else {
			  self.openLoginWindow();
		  }
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
  				DialogUtil.success('Your complaint has been reported to our team. ' 
  						+ 'Necessary actions will be taken as soon as possible. Thanks for reporting.', 
  						function() {
  							reloadPage();
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
		  var placeUrl = webApplicationUrlPrefix + '/pages/Place.html';
		  if (loginUserId == '') {
			  globalRedirectUrl = null;
			  self.openLoginWindow();
		  } else {
			  if (globalIsUserVerified) {
				  openWindow(placeUrl, true);
			  } else {
				  var verificationUrl = webApplicationUrlPrefix + '/pages/Verification.html?op=' + EnmOperation.CREATE_PLACE;
				  openWindow(verificationUrl, true);
			  }
		  }
	  };
  	  
	  self.onResetPasswordBtnClicked = function() {
		  	var email = StringUtil.trim($("#txtEmail").val());
	  		
	  		if (email == '') {
	  			DialogUtil.warn( 'Please enter your email!', null);
	  		} else {
	  			DialogUtil.confirm('Your password will be reset, do you want to continue ?', function(isAccepted) {
	  				if (isAccepted) {
	  					userService.sendResetPasswordEmail(email,
								function(isSuccess) {
									if (isSuccess) {
										DialogUtil.success('An email is sent to your mail address. Please follow the instructions in the mail to reset your password!');
									}
								}
						  );
	  				}
	  			});
	  		}
	  }
	  
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

function openComplaintWindow(entityType, entityId) {
	var scope = angular.element( $('#divPageHeader') ).scope();
	scope.ctrl.openComplaintWindow(entityType, entityId);
}

function createComplaint() {
	var scope = angular.element( $('#divPageHeader') ).scope();
	scope.ctrl.createComplaint();
}

function deleteComplaint(entityType, entityId, callBack) {
	var scope = angular.element( $('#divPageHeader') ).scope();
	scope.ctrl.deleteComplaint(entityType, entityId, callBack);
}

function listComplaint(entityType, callBack) {
	var scope = angular.element( $('#divPageHeader') ).scope();
	scope.ctrl.listComplaint(entityType, callBack);
}

function isEntityComplainted(entityType, entityId) {
	if (userComplaintMap && userComplaintMap[entityType.toString()]) {
		entityComplaintMap = userComplaintMap[entityType.toString()];
		if (entityComplaintMap[entityId.toString()]) {
			return true;
		}
	}
	return false;
}

function onResetPasswordBtnClicked() {
	var scope = angular.element( $('#divPageHeader') ).scope();
	scope.ctrl.onResetPasswordBtnClicked();
}

function onBecomeHostClicked() {
	var scope = angular.element( $('#divPageHeader') ).scope();
	scope.ctrl.onBecomeHostClicked();
}