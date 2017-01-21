App.controller('headerCtrl', ['$scope', 'userService', '$sce', '$compile', function($scope, userService, sce, compile) {
      var self = this;
      self.html = '';
      
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
    		  openModal();  
    	  });
      };
      
      self.signup = function() {
    	  
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
  				password : password
  			};
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
  		}
  		
  	};
  	
  	self.login = function() {
  		var email = StringUtil.trim($("#txtEmail").val());
  		var password = StringUtil.trim($("#txtPassword").val());
  		
  		if (email == '' || password == '') {
  			DialogUtil.showMessage(DialogUtil.MESSAGE_TYPE.WARNING, 'Warning', 'Please fill mandatory fields!');
  		} else {
  			var user = {
  				email : email,
  				password : password
  			};
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
  		}
  	};
  	
  	self.logout = function() {
  		NProgress.start(2000, 10);
		userService.logout().then(
				function(operationResult) {
					NProgress.done(true); 
					if (operationResult.resultCode == EnmOperationResultCode.SUCCESS) {
						NProgress.done(true); 
						if (operationResult.resultCode == EnmOperationResultCode.SUCCESS) {
							location.href = webApplicationUrlPrefix + "/pages/Main.xhtml";
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
      
	  self.initialize = function() {
		  
	  };
      
      self.initialize();
      
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
	scope.ctrl.signup();
}

function login() {
	var scope = angular.element( $('#divPageHeader') ).scope();
	scope.ctrl.login();
}
