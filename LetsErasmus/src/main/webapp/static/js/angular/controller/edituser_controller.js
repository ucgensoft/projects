App.controller('editUserCtrl', ['$scope', 'userService', 'commonService', '$sce', '$compile', 
                                function($scope, userService, commonService, sce, compile) {
      var self = this;
      self.html = '';
      var auth2 = null;
      var facebookLoginResponse = null;
      self.photo = null;
      var defaultPasswordText = "xxxxxx";
      self.countryList = [];
      self.dummyModel = null;
      
      
      var acceptedPhotoTypes = {
	  	'image/png' : true,
	  	'image/jpeg' : true,
	  	'image/gif' : true
	  };
      
      self.initialize = function() {
    	  
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
	      				dateFormat : "dd.mm.yy"
	      			});
    	  $("#txtBirthDatePicker").datepicker().val(birthDate);
    	  //$('#txtPassword').val(defaultPasswordText);
    	  //$('#txtPasswordConfirm').val(defaultPasswordText);
    	  
    	  self.initPhotoHolder();
    	  
    	  commonService.listCountry( function(countryList) {
					if (countryList) {
						self.countryList = countryList;
					}
				}
		  );
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
	       	  			$('#divPhotoPreview').removeClass('hidden-force');
	       	  			$('#photoHolder').addClass('hidden-force');
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
  			$('#divPhotoPreview').addClass('hidden-force');
  			$('#photoHolder').removeClass('hidden-force');
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
  		var birthDate = $("#txtBirthDatePicker").datepicker("getDate")
  		
  		if (gender == '-1') {
  			gender = null;
  		}
  		
  		if (StringUtil.trim(password) != '' && password != defaultPasswordText) {
  			if (passwordConfirm == '') {
	  				DialogUtil.warn( 'Please confirm your password!', null);
	  				return;
	  			} else if (password != passwordConfirm) {
	  				DialogUtil.warn( 'Two passwords do not match!', null);
	  				return;
	  			}	
  		} else {
  			password = null;
  		}
  		
  		if (userFirstName == '' || userLastName == '' || email == '') {
  			DialogUtil.warn( 'Please fill mandatory fields!');
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
  				languages : languages,
  				birthDate : birthDate
  			};
  			
  			var newUserPhoto = null;
  			if (self.photo == null) {
  				if (userProfilePhotoId != '' && $('#divPhotoPreview').hasClass('hidden-force')) {
  					var emptyFile = createEmptyFile('deleted');
  					newUserPhoto = { 'file': emptyFile, 'src': null};
  				} else {
  					var emptyFile = createEmptyFile('dummy');
  					newUserPhoto = { 'file': emptyFile, 'src': null};
  				}
  			}
  			
  			userService.updateUser(user, (self.photo != null ? self.photo : newUserPhoto),
					function(isSuccess) {
						if (isSuccess) {
							DialogUtil.success( 'Your profile is updated successfully!', function() {
								reloadPage();
							});
						}
					}
			  );
  		}
  		
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
  		DialogUtil.confirm('Your phone number will be deleted. Do you want to continue?', function() {
  			userService.removeMsisdn(function(isSuccess) {
					if (isSuccess) {
						reloadPage();
					}
  				}
  			);
  		});
  	};
  	
  	self.sendMsisdnVerificationCode = function() {
  		var prefix = null;
	  	var msisdn = null;
  		
  		if ($('#divNoPhoneNumber').length > 0) {
  			var prefix = $('#divCountryPrefix')[0].innerText;
  	  		var msisdn = $('#txtMsisdn').val();
	  	  	if (prefix == null || prefix == '' || StringUtil.trim(msisdn) == '') {
	  			DialogUtil.warn( 'Select a country and type your phone number please!', null);
	  			return;
	  		}
  		} else {
  			var prefix = 'X';
  	  		var msisdn = 'X';
  		}
  		userService.sendMsisdnVerificationCode(prefix, msisdn,
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
  	
  	self.verifyMsisdnCode = function() {
  		var code = $('#txtVerificationCode').val();
  		if (StringUtil.trim(code) != '') {
  			userService.verifyMsisdnCode(code,
  					function(isSuccess) {
  						if (isSuccess) {
  							DialogUtil.success('Your phone number is verified successfully!', function() {
  								reloadPage();
  							});
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
  	
  	self.deactivateUser = function() {
  		DialogUtil.confirm('Your profile will be deactivated and you will be logged out. Do you wish to proceed?', function(response) {
	  			if (response) {
	  		  		userService.deactivateUser(function(isSuccess) {
	  						  if (isSuccess) {
	  							  DialogUtil.success('Your profile is deactivated!', function() {
	  								openWindow(webApplicationUrlPrefix + "/pages/Main.html", true);
	  							  });
	  						  }
	  			  		  }
	  			  	  );
	  			}
	  		});
  	};
  	
    self.initialize();
      
  }]);