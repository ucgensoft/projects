App.controller('editUserCtrl', ['$scope', 'userService', 'commonService', '$sce', '$compile', 
                                function($scope, userService, commonService, sce, compile) {
      var self = this;
      self.html = '';
      var auth2 = null;
      var facebookLoginResponse = null;
      self.photo = null;
      var defaultPasswordText = "xxxxxx";
      
      
      var acceptedPhotoTypes = {
	  	'image/png' : true,
	  	'image/jpeg' : true,
	  	'image/gif' : true
	  };
      
      self.initialize = function() {
    	  
    	  /*
		  gapi.load('auth2', function() {
    	      auth2 = gapi.auth2.init({
    	        client_id: googleId,
    	        cookiepolicy: 'single_host_origin',
    	      });
    	    });
		  
		  FB.init({
			    appId: facebookId,
			    version: 'v2.7'
			  });
		 */
    	  $("#txtResidenceLocationName").geocomplete().bind("geocode:result",
	      			function(event, result) {
	      				//self.onPlaceChange(event, result);
	      			});
    	  
    	  var birthDate = $("#hiddenBirthDate").val();
    	  $("#txtBirthDatePicker").datepicker(
	      			{
	      				changeMonth: true,
	      	            changeYear: true,
	      	            yearRange: '-100:-10',
	      				maxDate : new Date(),
	      				dateFormat : "d MM, y"
	      			});
    	  $("#txtBirthDatePicker").datepicker().val(birthDate);
    	  //$('#txtPassword').val(defaultPasswordText);
    	  //$('#txtPasswordConfirm').val(defaultPasswordText);
    	  
    	  self.initPhotoHolder();
    	  
	 };
	 
	 self.initPhotoHolder = function() {
	   	  var holder = document.getElementById('photoHolder');
	   	  var fileupload = document.getElementById('filePhoto');
	
	   	  fileupload.onchange = function() {
	   	  	readfiles(this.files);
	   	  };
	
	   	  previewfile = function(file) {
	   	  	if (file != null && tests.filereader === true) {
	   	  		if (acceptedPhotoTypes[file.type] === true) {
	   	  			var reader = new FileReader();
	       	  		reader.onload = function(event) {
	       	  			self.photo = { 'file': file, 'src': event.target.result};
	       	  			$('#imgProfilePhoto').attr('src', self.photo.src);
	       	  			$('#divPhotoPreview').removeClass('hidden');
	       	  			$('#photoHolder').addClass('hidden');
	       	  			NProgress.done(true);
	       	  			fileupload.value = '';
	       	  		};
	       	  		
	       	  		NProgress.start(2000, 10);
	       	  		reader.readAsDataURL(file);
	   	  		} else {
	   	  			// File type not supported
	   	  		}
	   	  	} else {
	   	  		//fileReader not supported
	   	  	}
	   	  };
	
	   	  readfiles = function(files) {
	   		previewfile(files[0]);
	   	  };
	
	   	  holder.ondragover = function() {
	   	  	$(holder).addClass('hover')
	   	  	return false;
	   	  };
	
	   	  holder.ondragleave = function() {
	   	  	$(holder).removeClass('hover')
	   	  	return false;
	   	  };
	
	   	  holder.ondragend = function() {
	   	  	$(holder).removeClass('hover')
	   	  	return false;
	   	  };
	
	   	  holder.ondrop = function(e) {
	   	  	$(holder).removeClass('hover')
	   	  	e.preventDefault();
	   	  	readfiles(e.dataTransfer.files);
	   	  }  
     };
     
     self.removePhoto = function() {
    	 	self.photo = null;
  			$('#imgProfilePhoto').attr('src', '');
  			$('#divPhotoPreview').addClass('hidden');
  			$('#photoHolder').removeClass('hidden');
     };
	  
      self.saveChanges = function() {
    	  
  		var userFirstName = StringUtil.trim($("#txtFirstName").val());
  		var userLastName = StringUtil.trim($("#txtLastName").val());
  		var email = StringUtil.trim($("#txtEmail").val());
  		var gender = StringUtil.trim($("#cmbGender").val());
  		var password = StringUtil.trim($("#txtPassword").val());
  		var passwordConfirm = StringUtil.trim($("#txtPasswordConfirm").val());
  		var residenceLocationName = StringUtil.trim($("#txtResidenceLocationName").val());
  		var description = StringUtil.trim($("#txtDescription").val());
  		var schoolName = StringUtil.trim($("#txtSchoolName").val());
  		var jobTitle = StringUtil.trim($("#txtJobTitle").val());
  		var languages = StringUtil.trim($("#txtLanguages").val());
  		
  		if (gender == '') {
  			gender = null;
  		}
  		
  		if (password != '' && password != defaultPasswordText) {
  			if (passwordConfirm == '') {
  				$.prompt('Please confirm your password!');
  				//DialogUtil.showMessage(DialogUtil.MESSAGE_TYPE.WARNING, 'Warning', 'Please confirm your password!');
  				return;
  			} else if (password != passwordConfirm) {
  				$.prompt('Password and confirm passwords do not match!');
  				//DialogUtil.showMessage(DialogUtil.MESSAGE_TYPE.WARNING, 'Warning', 'Password and confirm passwords do not match!');
  				return;
  			}
  		}
  		
  		if (userFirstName == '' || userLastName == '' || email == '') {
  			DialogUtil.showMessage(DialogUtil.MESSAGE_TYPE.WARNING, 'Warning', 'Please fill mandatory fields!');
  		} else {
  			var user = {
  				firstName : userFirstName,
  				lastName : userLastName,
  				gender : gender,
  				email : email,
  				password : password,
  				residenceLocationName : residenceLocationName,
  				description : description,
  				schoolName : schoolName,
  				jobTitle : jobTitle,
  				languages : languages
  			};
  			
  			userService.updateUser(user, self.photo).then(
				function(operationResult) {
					NProgress.done(true); 
					if (operationResult.resultCode == EnmOperationResultCode.SUCCESS) {
						NProgress.done(true); 
						if (operationResult.resultCode == EnmOperationResultCode.SUCCESS) {
							$.prompt("Your profile is updated successfully!");
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
  	  	  
    self.initialize();
      
  }]);