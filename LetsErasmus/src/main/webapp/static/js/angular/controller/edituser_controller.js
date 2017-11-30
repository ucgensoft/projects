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
      self.isErasmusStudent = (globalProfileTypeId == 1);
      self.homeCountryId = globalHomeCountryId;
      self.erasmusCountryId = globalErasmusCountryId;
      
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
	   		$('#imgProfilePhoto').css('transform', 'rotate(' + 0 + 'deg)');
	   	  	readfiles(this.files);
	   	  };
	
	   	  previewfile = function(file) {
	   	  	if (file != null && tests.filereader === true) {
	   	  		if (acceptedPhotoTypes[file.type] === true) {
	   	  			var reader = new FileReader();
	       	  		reader.onload = function(event) {
		       	  		EXIF.getData(file, function () {
		       	  			var defaultAngle = ImageUtil.getDefaultAngle(this.exifdata.Orientation);
		    	   		    
			       	  		self.photo = { 'file': file, 'angle':0, 'defaultAngle':defaultAngle, 'src': event.target.result};
		       	  			$('#imgProfilePhoto').attr('src', self.photo.src);
		       	  			$('#divPhotoPreview').removeClass('hidden-force');
		       	  			$('#photoHolder').addClass('hidden-force');
		       	  			refreshAngularScope($scope)
		       	  			NProgress.done(true);
		       	  			fileupload.value = '';
		    	   		});
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
  		//var birthDate = $("#txtBirthDatePicker").datepicker("getDate")
  		var birthDate = $("#txtBirthDatePicker").val()
  		var homeCountryId = $("#cmbHomeCountry").val();
  		var homeCityId = $("#cmbHomeCity").val();
  		var homeUniversityId = $("#cmbHomeUniversity").val();
  		var isErasmusStudent = $('#chbIsErasmusStudent')[0].checked;
  		var erasmusCountryId = $("#cmbErasmusCountry").val();
  		var erasmusCityId = $("#cmbErasmusCity").val();
  		var erasmusUniversityId = $("#cmbErasmusUniversity").val();
  		var profileTypeId = null;
  		
  		if (isErasmusStudent) {
  			profileTypeId = 1;
  		} else {
  			profileTypeId = 2;
  		}
  		
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
  			var homeCountryId = $("#cmbHomeCountry").val();
  	  		var homeCityId = $("#cmbHomeCity").val();
  	  		var homeUniversityId = $("#cmbHomeUniversity").val();
  	  		var isErasmusStudent = $('#chbIsErasmusStudent')[0].checked;
  	  		var erasmusCountryId = $("#cmbErasmusCountry").val();
  	  		var erasmusCityId = $("#cmbErasmusCity").val();
  	  		var erasmusUniversityId = $("#cmbErasmusUniversity").val();
  			
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
  				birthDate : birthDate,
  				profileTypeId : profileTypeId,
  				homeCountryId : homeCountryId,
  				homeCityId : homeCityId,
  				homeUniversityId : homeUniversityId,
  				erasmusCountryId : erasmusCountryId,
  				erasmusCityId : erasmusCityId,
  				erasmusUniversityId : erasmusUniversityId
  			};
  			
  			var newUserPhoto = null;
  			if (self.photo == null) {
  				if (userProfilePhotoId != '' && $('#divPhotoPreview').hasClass('hidden-force')) {
  					var emptyFile = createEmptyFile('deleted');
  					newUserPhoto = { 'file': emptyFile, 'src': null};
  				} else {
  					var emptyFile = createEmptyFile('dummy');
  					newUserPhoto = { 'file': emptyFile, 'src': null, 'angle' : 0, 'defaultAngle' : 0};
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
  	
  	self.onIsErasmusChange = function(event) {
  		var isErasmusStudent = $('#chbIsErasmusStudent')[0].checked;
  		if (isErasmusStudent) {
  			$('#divErasmusInfo').removeClass('hidden-force');
  		} else {
  			$('#divErasmusInfo').addClass('hidden-force');
  		}
  	};
  	
  	self.onHomeCountryChange = function() {
  		commonService.listCity(self.homeCountryId, function(cityList) {
  			$('#cmbHomeCity').find('option:not(:first)').remove();
  			if (cityList != null && cityList.length > 0) {
  				for(var i = 0; i < cityList.length; i++) {
  	  				$('#cmbHomeCity').append('<option value="' + cityList[i].id + '">' + cityList[i].name + '</option>');
  	  			}
  			}
  			$('#cmbHomeCity').val('-1');
  		});
  		commonService.listUniversity(self.homeCountryId, function(universityList) {
  			$('#cmbHomeUniversity').find('option:not(:first)').remove();
  			if (universityList != null && universityList.length > 0) {
  				for(var i = 0; i < universityList.length; i++) {
  	  				$('#cmbHomeUniversity').append('<option value="' + universityList[i].id + '">' + universityList[i].name + '</option>');
  	  			}
  			}
  			$('#cmbHomeUniversity').val('-1');
  		});
  	};
  	
  	self.onErasmusCountryChange = function() {
  		commonService.listCity(self.erasmusCountryId, function(cityList) {
  			$('#cmbErasmusCity').find('option:not(:first)').remove();
  			if (cityList != null && cityList.length > 0) {
  				for(var i = 0; i < cityList.length; i++) {
  	  				$('#cmbErasmusCity').append('<option value="' + cityList[i].id + '">' + cityList[i].name + '</option>');
  	  			}
  			}
  			$('#cmbErasmusCity').val('-1');
  		});
  		commonService.listUniversity(self.erasmusCountryId, function(universityList) {
  			$('#cmbErasmusUniversity').find('option:not(:first)').remove();
  			if (universityList != null && universityList.length > 0) {
  				for(var i = 0; i < universityList.length; i++) {
  	  				$('#cmbErasmusUniversity').append('<option value="' + universityList[i].id + '">' + universityList[i].name + '</option>');
  	  			}
  			}
  			$('#cmbErasmusUniversity').val('-1');
  		});
  	};
  	
  	self.deactivateUser = function() {
  		DialogUtil.confirm('Your profile will be deactivated and you will be logged out. Do you wish to proceed?', function(response) {
	  			if (response) {
	  		  		userService.deactivateUser(function(isSuccess) {
	  						  if (isSuccess) {
	  							  DialogUtil.success('Your profile is deactivated!', function() {
	  								openWindow(webApplicationUrlPrefix, true);
	  							  });
	  						  }
	  			  		  }
	  			  	  );
	  			}
	  		});
  	};
  	
  	self.rotatePhoto = function() {
  		ImageUtil.rotate('imgProfilePhoto', self.photo, 90);
  	};
  	
    self.initialize();
      
  }]);