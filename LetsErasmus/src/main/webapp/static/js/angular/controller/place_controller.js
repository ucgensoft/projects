App.controller('placeCtrl', ['$scope', '$controller', 'placeService', 'commonService', 'enumerationService', 'paymentService', 
                             function($scope, $controller, placeService, commonService, enumerationService, paymentService) {
      var self = this;
      
      var pageMode = null;
      
      self.dummyModel = null;
      
      var marker = null;
      var map = null;
      var autocomplete = null;
      self.countryList = [];
      self.place = null;
      self.photoList = [];
      self.amentiesList = [];
      self.safetyAmentiesList = [];
      self.ruleList = [];
      self.hasPayout = false;
      
      var acceptedPhotoTypes = {
      	  	'image/png' : true,
      	  	'image/jpeg' : true,
      	  	'image/gif' : true
      	  };
      
     var support = {
      	  	filereader : document.getElementById('filereader'),
      	  	formdata : document.getElementById('formdata'),
      	  	progress : document.getElementById('progress')
      	  };
           
      self.initialize = function() {
    	  window.onbeforeunload = function(){
    		  if (step != null && step > 1) {
    			  return confirm('Do you want to leave without save?');
    		  }
    		};
    	  
    	  if (loginType != '') {
    		  $("#txtStartDatePicker").datepicker(
				{
					minDate: '+0',
					dateFormat : "dd.mm.yy",
					onSelect : function(selectedDate, cal) {
						var minDate = $('#txtStartDatePicker').datepicker('getDate');
  			            $("#txtEndDatePicker").datepicker( "option", "minDate", minDate.addMonths(1).addDays(-1));
					}
				});
	    	  
	    	  $("#txtEndDatePicker").datepicker(
				{
					minDate: '+1m',
					dateFormat : "dd.mm.yy",
					onSelect : function(selectedDate, cal) {
						
					}
				});
	    	  
	    	  var placeId = getUriParam('placeId');
	    	  if (placeId != null && placeId != "") {
	    		  self.hasPayout = true;
	    		  placeService.getPlace(placeId).then(function(operationResult) {
	        	  		self.displayPlaceDetails(operationResult.resultValue)
	          		},
					function(errResponse){
						console.error('Error while fetching Portfolio');
					}
			      );
	    	  } else {
	    		  paymentService.hasPayoutMethod(function(hasPayout) {
	    			  self.hasPayout = hasPayout;
	    			  if (!self.hasPayout) {
	    				  $("#txtBirthDatePicker").datepicker(
				  			{
				  				changeMonth: true,
				  	            changeYear: true,
				  	            yearRange: '-100:-10',
				  				maxDate : new Date(),
				  				dateFormat : "dd.mm.yy",
				  				defaultDate: "1990-01-01"
				  			});
	    			  }
	    			  self.displayTab(1);
	    		  });
	    		  
	    		  commonService.listCountry(function(countryList) {
	    			  self.countryList = countryList;
	    		  });
	    	  }
	    	  
	    	  enumerationService.listEnumeration(null).then(function(operationResult) {
	  	  			self.amentiesList = operationResult.resultValue["place_amenty"];
	  	  			self.safetyAmentiesList = operationResult.resultValue["place_safety_amenty"];
	  	  			self.ruleList = operationResult.resultValue["place_rule"];
	    		},
				function(errResponse){
					console.error('Error while fetching Enumerations');
				}
		      );
	    	  
	    	  self.displayTab(1);
	    	  /*
	    	  //$("#divStep1").css("display", "none");
	    	  $("#divStep2").css("display", "none");
	    	  $("#divStep3").css("display", "none");
	    	  $("#divStep4").css("display", "none");
	    	  $("#divStep5").css("display", "none");
	    	  $("#divStep6").css("display", "none");
	    	  $("#divStep7").css("display", "none");
	    	  $("#divStep8").css("display", "none");
	    	  */
	    	  
	    	  self.initPhotoTab();
    	  } else {
    		  openLoginWindow();
    	  }
      };
      
      self.displayTab = function(tabId) {
    	  for(var i = 0; i <= 9; i++) {
    		  if (i == tabId) {
    			  $("#divStep" + i).removeClass("hidden-force");
    		  } else {
    			  $("#divStep" + i).addClass("hidden-force");
    		  }
    	  }
      };
      
      self.displayPlaceDetails = function(place) {
    	  self.place = place;
    	  
    	  var photoIndex = 0;
			for(var i = 0; i < self.place.photoList.length; i++) {
				var photo = self.place.photoList[i];
				self.photoList.push({ 'photoId': photo.id, 'file': null, 'src': generatePlacePhotoUrl(self.place.id, photo.id, EnmImageSize.SMALL), 'angle': 0 });
			}
    	  
    	  self.place.amentyMap = {};
    	  if (self.place.amenties != null && self.place.amenties != '') {
    		  var amentyList = self.place.amenties.split(',');
    		  for (var i = 0; i < amentyList.length; i++) {
    			  var strAmenty = amentyList[i];
    			  self.place.amentyMap[strAmenty] = 1;
    		  }
    	  }
    	  
    	  self.place.safetyAmentyMap = {};
    	  if (self.place.safetyAmenties != null && self.place.safetyAmenties != '') {
    		  var safetyAmentyList = self.place.safetyAmenties.split(',');
    		  for (var i = 0; i < safetyAmentyList.length; i++) {
    			  var strSafetyAmenty = safetyAmentyList[i];
    			  self.place.safetyAmentyMap[strSafetyAmenty] = 1;
    		  }
    	  }
    	  
    	  self.place.ruleMap = {};
    	  if (self.place.rules != null && self.place.rules != '') {
    		  var ruleList = self.place.rules.split(',');
    		  for (var i = 0; i < ruleList.length; i++) {
    			  var strRule = ruleList[i];
    			  self.place.ruleMap[strRule] = 1;
    		  }
    	  }
    	  
    	  if (self.place.placeTypeId == 1) {
    		  $("#rdPlaceTypeEntirePlace").attr('checked', 'checked');
    	  } else if (self.place.placeTypeId == 2) {
    		  $("#rdPlaceTypePrivateRoom").attr('checked', 'checked');
    	  } else if (self.place.placeTypeId == 3) {
    		  $("#rdPlaceTypeSharedRoom").attr('checked', 'checked');
    	  }

    	  $("#cmbHomeType").val(self.place.homeTypeId);
    	  setCounterElementValue('spanBedNumber', self.place.bedNumber);
    	  setCounterElementValue('spanBathroomNumber', self.place.bathRoomNumber);
    	  $('#cmbBathroomType').val(self.place.bathRoomType)
    	      	  
    	  setCounterElementValue('spanGuestNumber', self.place.guestNumber);
    	  
    	  $("#cmbGuestGender").val(self.place.guestGender);
    	  if (self.place.lgbtFriendly == 'Y') {
    		  $("#chbLgbtFriendly").attr('checked', 'checked');
    	  }
    	  setCounterElementValue('spanPlaceMateNumber', self.place.placeMateNumber);
    	  if (self.place.placeMateGender) {
    		  $("#cmbPlaceMateGender").val(self.place.placeMateGender);
    	  }
    	      	  
    	  $("#txtDailyPrice").val(self.place.price);
    	  $("#txtDepositPrice").val(self.place.depositPrice)

    	  $("#cmbCurrencyId").val(self.place.currencyId);
    	  if (self.place.billsIncluded == 'Y') {
    		  $("#chbBillsIncluded").attr('checked', 'checked');
    	  }
    	  
    	  $("#txtStartDatePicker").datepicker(
			{
				minDate : new Date(),
				defaultDate : new Date(self.place.startDate),
				dateFormat : "d MM, y",
				onSelect : function(selectedDate, cal) {
					
				}
			});
    	  
    	  $("#txtEndDatePicker").datepicker(
			{
				minDate : new Date(),
				defaultDate : new Date(self.place.endDate),
				dateFormat : "d MM, y",
				onSelect : function(selectedDate, cal) {
					
				}
			});
    	  
    	  $("#txtStartDatePicker").datepicker('setDate', new Date(self.place.startDate));
    	  $("#txtEndDatePicker").datepicker('setDate', new Date(self.place.endDate));
    	  
    	  $("#txtMinStay").val(self.place.minimumStay);
    	  $("#txtMaxStay").val(self.place.maximumStay);
    	  
    	  $("#hiddenLocality").val(self.place.location.locality);
    	  $("#txtCountry").val(self.place.location.country);
    	  $("#txtCity").val(self.place.location.state);
    	  $("#txtStreet").val(self.place.location.street);
    	  $("#txtPostalCode").val(self.place.location.postalCode);
    	  $("#txtBuilding").val(self.place.location.userAddress);
    	  $("#txtLatitude").val(self.place.location.latitude);
    	  $("#txtLongitude").val(self.place.location.longitude);
    	  $("#txtSearch").val(self.place.location.street);
    	  
    	  $("#txtTitle").val(self.place.title);
    	  $("#txtDescription").val(self.place.description);
    	  $("#cmbCancellationPolicy").val(self.place.cancellationPolicyId);
    	  
      };
      
      self.hasAmenty = function(amentyKey) {
    	  if (self.place != null) {
    		  if (self.place.amentyMap[amentyKey] == 1) {
        		  return true;
        	  } else {
        		  return false;
        	  }
    	  } else {
    		  return false;
    	  }
      };
      
      self.hasSafetyAmenty = function(safetyAmentyKey) {
    	  if (self.place != null) {
    		  if (self.place.safetyAmentyMap[safetyAmentyKey] == 1) {
        		  return true;
        	  } else {
        		  return false;
        	  }
    	  } else {
    		  return false;
    	  }
      };
      
      self.hasRule = function(ruleKey) {
    	  if (self.place != null) {
    		  if (self.place.ruleMap[ruleKey] == 1) {
        		  return true;
        	  } else {
        		  return false;
        	  }
    	  } else {
    		  return false;
    	  }
      };
      
      self.initializeMap = function() {
    	  var tmpLatitude = 41.0082376;
    	  var tmpLongitude = 28.97835889999999;
    	  
    	  if (self.place != null) {
    		  tmpLatitude = self.place.location.latitude;
    		  tmpLongitude = self.place.location.longitude;
    	  }
    	  
    	  self.selectedLocation = new google.maps.LatLng(tmpLatitude, tmpLongitude);
			
			var mapOptions = {
					zoom: 13,
					center: self.selectedLocation,
					disableDefaultUI: false
				}
			
			map = new google.maps.Map(document.getElementById('divMap'), mapOptions);
			
		    var input = document.getElementById('txtSearch');
		    //map.controls[google.maps.ControlPosition.TOP_LEFT].push(input);

		    autocomplete = new google.maps.places.Autocomplete(input);
		    //autocomplete.bindTo('bounds', map);

		    autocomplete.addListener('place_changed', function(event, result) {
		    	self.onPlaceChange(event, result);
		    });
		    
		    if (marker == null) {
	        	marker = new google.maps.Marker({
			        map: map,
					draggable: true,
			        position: self.selectedLocation
			    });
	        	
	        	google.maps.event.addListener(marker, 'dragend', function() {
		        		$("#txtLatitude").val(marker.getPosition().lat);
		    	        $("#txtLongitude").val(marker.getPosition().lng);
		            });
	  	        }
	  	        
	  	        marker.setPosition(self.selectedLocation);
	  	        marker.setVisible(true);
	      };
      
      self.onMarkerMove = function() {
    	    
      };
      
      self.initPhotoTab = function() {
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
        	  			EXIF.getData(file, function () {
		       	  			var defaultAngle = ImageUtil.getDefaultAngle(this.exifdata.Orientation);
		       	  			
				       	  	var photoId = generateRandomValue(100, 1000000);
	        	  			self.photoList.push({ 'photoId':photoId, 'file': file, 'src': event.target.result, 'angle' : 0, 'defaultAngle':defaultAngle});
	        	  			commonService.fakeAjaxCall();
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
    	  	for (var i = 0; i < files.length; i++) {
    	  		previewfile(files[i]);
    	  	}
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
      
      self.removePhoto = function(photoId) {
    	  for (var i = 0; i < self.photoList.length; i++) {
    		  if (self.photoList[i].photoId == photoId) {
    			  self.photoList.splice(i, 1);
    			  break;
    		  }
    	  }
    	  commonService.fakeAjaxCall();
      }
      
      self.onPlaceChange = function (event, result) {
	        var place = autocomplete.getPlace();
	        if (!place.geometry) {
	            DialogUtil.error("Autocomplete's returned place contains no geometry");
	            return;
	        }
	  
	        map.setCenter(place.geometry.location);
	        map.setZoom(15);
	        
	        if (marker == null) {
	        	marker = new google.maps.Marker({
			        map: map,
					draggable: true,
			        position: place.geometry.location
			    });
	        	
	        	google.maps.event.addListener(marker, 'dragend', function() {
		        		$("#txtLatitude").val(marker.getPosition().lat);
		    	        $("#txtLongitude").val(marker.getPosition().lng);
		            });
	        }
	        
	        marker.setPosition(place.geometry.location);
	        marker.setVisible(true);
	    
	        $("#txtLatitude").val(place.geometry.location.lat);
	        $("#txtLongitude").val(place.geometry.location.lng);
	        
	        //Location details test 
	        
	        for (var i = 0; i < place.address_components.length; i++) {
	            if(place.address_components[i].types[0] == 'postal_code'){
	                document.getElementById('txtPostalCode').value = place.address_components[i].long_name;
	            } else if(place.address_components[i].types[0] == 'country'){
	                document.getElementById('txtCountry').value = place.address_components[i].long_name;
	            } else if(place.address_components[i].types[0] == 'administrative_area_level_1'){
	                document.getElementById('txtCity').value = place.address_components[i].long_name;
	            } else if(place.address_components[i].types[0] == 'locality'){
	                document.getElementById('hiddenLocality').value = place.address_components[i].long_name;
	            }
	        }
	    };
	    
	  self.validate = function(step) {
		  var isValid = true;
		  
		  var operationResult = newOperationResult(null, null, null);
		  
		  if (step == null || step == 1) {
    		  if ($("#rdPlaceTypeEntirePlace")[0].checked == false 
        			  && $("#rdPlaceTypePrivateRoom")[0].checked == false
        			  && $("#rdPlaceTypeSharedRoom")[0].checked == false) {
    			  isValid = false;
        	  } else if ($("#cmbHomeType")[0].selectedIndex == 0) {
        		  isValid = false;
        	  } else if (getCounterElementValue('spanBedNumber') == 0) {
        		  isValid = false;
        	  } else if (getCounterElementValue('spanBathroomNumber') > 0 
        			  && $('#cmbBathroomType')[0].selectedIndex == 0) {
        		  isValid = false;
        	  }
    	  } 
		  
		  if (step == null || step == 2) {
    		  if (getCounterElementValue('spanGuestNumber') == 0 
    				  || $("#cmbGuestGender")[0].selectedIndex == 0) {
    			  isValid = false;
    		  } else if ((getCounterElementValue('spanPlaceMateNumber') > 0 
    				  && $("#cmbPlaceMateGender")[0].selectedIndex == 0) 
    				  || (getCounterElementValue('spanPlaceMateNumber') == 0 
    	    				  && $("#cmbPlaceMateGender")[0].selectedIndex != 0)) {
    			  isValid = false;
    		  }
    	  } 
		  
		  if (step == null || step == 3) {
    		  if ($("#txtDailyPrice").val() == '' 
    			  || $("#txtDailyPrice").val() == '0') {
    			  isValid = false;
    		  } else if ($("#cmbCurrencyId")[0].selectedIndex == 0) {
    			  isValid = false;
    		  } else if ($("#txtStartDatePicker").datepicker("getDate") == null
    			  || $("#txtEndDatePicker").datepicker("getDate") == null) {
    			  isValid = false;
    		  } else if ($("#cmbBankCountry").length > 0 && $("#cmbBankCountry")[0].selectedIndex == 0) {
    			  isValid = false;
    		  }
    	  } 
		  
		  if (step == null || step == 4) {
    		  
    	  } 
		  
		  if (step == null || step == 5) {
    		  if ($("#txtLatitude").val() == '' 
    			  || $("#txtLongitude").val() == ''
    			  || $("#txtCountry").val() == ''
    			  || $("#txtCity").val() == ''
    			  || $("#txtStreet").val() == '') {
    			  isValid = false;
    		  }
    	  } 
		  
		  if (step == null || step == 6) {
    		  if (self.photoList.length == 0) {
    			  operationResult[OperationResult.resultDesc] = 'Please upload photo of your place. Minimum 1 photo is mandatory.';
    			  isValid = false;
    		  } else if (self.photoList.length > maxPhotoCount) {
    			  operationResult[OperationResult.resultDesc] = 'Maximum photo number for a place definition is: ' + maxPhotoCount;
    			  isValid = false;  
    		  }
    	  } 
		  
		  if (step == null || step == 7) {
    		  if ($('#txtTitle').val() == '' 
    			  || $('#txtDescription').val() == ''
    				  || $("#cmbCancellationPolicy")[0].selectedIndex == 0) {
    			  isValid = false;
    		  }
    	  }
		  
		  if (step == null || step == 8) {
    		  if (($("#rdAccountClassPersonal")[0].checked == false 
    				  && $("#rdAccountClassBusiness")[0].checked == false)
    			  || StringUtil.trim($('#txtFirstName').val()) == '' 
    			  || StringUtil.trim($('#txtLastName').val()) == ''
    			  || $("#cmbVendorCountry")[0].selectedIndex == 0
    			  || StringUtil.trim($('#txtVendorCity').val()) == ''
    			  || StringUtil.trim($('#txtVendorPostalCode').val()) == ''
    			  || StringUtil.trim($('#txtVendorAddress1').val()) == '') {
    			  isValid = false;
    		  }
    	  }
		  if (isValid) {
			  operationResult[OperationResult.resultCode] = EnmOperationResultCode.SUCCESS;
		  } else {
			  operationResult[OperationResult.resultCode] = EnmOperationResultCode.WARNING;
		  }
		  return operationResult;
	  };
      
      self.next = function(step) {
    	  var operationResult = self.validate(step);
    	  
    	  if (operationResult[OperationResult.resultCode] == EnmOperationResultCode.SUCCESS) {
    		  $('#divProgress' + step).addClass('progress-section--completed');
    		  
    		  if (step == 9) {
    			  self.savePlace();
    		  } else {
    			  var currentStep = step;
    			  var nextStep = null;
    	    	  if (self.hasPayout && step == 7) {
    	    		  nextStep = step + 2;
    	    	  } else {
    	    		  nextStep = step + 1;
    	    	  }
            	  if ($("#divStep" + nextStep).length > 0) {
            		  self.displayTab(nextStep);
            	  }
            	  if (step == 4) {
            		  if (map == null) {
            			  self.initializeMap();
            		  }
            	  }
    		  }
    	  } else {
    		  if (operationResult[OperationResult.resultDesc] != null) {
    			  DialogUtil.warn(operationResult[OperationResult.resultDesc]);
    		  } else {
    			  DialogUtil.warn('Please fill required fields!');
    		  }
    		  return false;
    	  }  
      };
      
      self.back = function(step) {
    	  var currentStep = step;
    	  var nextStep = null;
    	  if (self.hasPayout && step == 9) {
    		  nextStep = step - 2;
    	  } else {
    		  nextStep = step - 1;
    	  }
    	  if ($("#divStep" + nextStep).length > 0) {
    		  self.displayTab(nextStep);
    	  }
      };
      
      self.savePlace = function() {
    	  var newPlace = {};
    	  if ($("#rdPlaceTypeEntirePlace")[0].checked) {
    		  newPlace.placeTypeId = 1;
    	  } else if ($("#rdPlaceTypePrivateRoom")[0].checked) {
    		  newPlace.placeTypeId = 2;
    	  } else if ($("#rdPlaceTypeSharedRoom")[0].checked) {
    		  newPlace.placeTypeId = 3;
    	  } 
    	  newPlace.homeTypeId = $("#cmbHomeType").val();
    	  newPlace.bedNumber = getCounterElementValue('spanBedNumber');
    	  newPlace.bathRoomNumber = getCounterElementValue('spanBathroomNumber');
    	  newPlace.bathRoomType = $('#cmbBathroomType').val();
    	  
    	  newPlace.guestNumber = getCounterElementValue('spanGuestNumber');
    	  newPlace.guestGender = $("#cmbGuestGender").val();
    	  newPlace.placeMateNumber = getCounterElementValue('spanPlaceMateNumber');
    	  if ($("#cmbPlaceMateGender")[0].selectedIndex > 0) {
    		  newPlace.placeMateGender = $("#cmbPlaceMateGender").val();
    	  }
    	  newPlace.lgbtFriendly = ($("#chbLgbtFriendly")[0].checked ? 'Y' : 'N');
    	  
    	  newPlace.price = $("#txtDailyPrice").val();
    	  if ($("#txtDepositPrice").val() != '') {
    		  newPlace.depositPrice = $("#txtDepositPrice").val();
    	  }
    	  newPlace.currencyId = $("#cmbCurrencyId").val();
    	  newPlace.billsInclude = ($("#chbBillsIncluded")[0].checked ? 'Y' : 'N');
    	  newPlace.startDate = $("#txtStartDatePicker").datepicker("getDate");
    	  newPlace.endDate = $("#txtEndDatePicker").datepicker("getDate");
    	  newPlace.minimumStay = $("#txtMinStay").val();
    	  newPlace.maximumStay = $("#txtMaxStay").val();
    	  
    	  var amenties = '';
    	  for (var i = 0; i < self.amentiesList.length; i++) {
    		  var itemAmenties = self.amentiesList[i];
    		  if ($('#chb_' + itemAmenties.enumKey)[0].checked) {
    			  if (amenties != '') {
    				  amenties = amenties + ",";
    			  }
    			  amenties = amenties + "'" + itemAmenties.enumKey + "'";
    		  }
    	  }
    	  newPlace.amenties = amenties;
    	  
    	  var safetyAmenties = '';
    	  for (var i = 0; i < self.safetyAmentiesList.length; i++) {
    		  var itemAmenties = self.safetyAmentiesList[i];
    		  if ($('#chb_' + itemAmenties.enumKey)[0].checked) {
    			  if (safetyAmenties != '') {
    				  safetyAmenties = safetyAmenties + ",";
    			  }
    			  safetyAmenties = safetyAmenties + "'" + itemAmenties.enumKey + "'";
    		  }
    	  }
    	  
    	  newPlace.safetyAmenties = safetyAmenties;
    	  
    	  var rules = '';
    	  for (var i = 0; i < self.ruleList.length; i++) {
    		  var rule = self.ruleList[i];
    		  if ($('#chb_' + rule.enumKey)[0].checked) {
    			  if (rules != '') {
    				  rules = rules + ",";
    			  }
    			  rules = rules + "'" + rule.enumKey + "'";
    		  }
    	  }
    	  
    	  newPlace.rules = rules;
    	  
    	  newPlace.location = {};
    	  newPlace.location.locality = $("#hiddenLocality").val();
    	  newPlace.location.country = $("#txtCountry").val();
    	  newPlace.location.state = $("#txtCity").val();
    	  newPlace.location.street = $("#txtStreet").val();
    	  newPlace.location.postalCode = $("#txtPostalCode").val();
    	  newPlace.location.userAddress = $("#txtBuilding").val();
    	  newPlace.location.latitude = $("#txtLatitude").val();
    	  newPlace.location.longitude = $("#txtLongitude").val();
    	  
    	  newPlace.title = $("#txtTitle").val();
    	  newPlace.description = $("#txtDescription").val();
    	  newPlace.cancellationPolicyId = $('#cmbCancellationPolicy').val();
    	  
    	  if (self.place == null) {
    		  
    		  tmpSavePlace = function() {
    			  placeService.savePlace(newPlace, self.photoList,
  	  					function(isSuccess) {
  	  						if (isSuccess) {
  	  							DialogUtil.success( 'Congratulations! Your place is saved successfully!', function() {
  	  								document.location.href = webApplicationUrlPrefix + '/pages/dashboard/Listings.html';
  	  							});
  	  						}
  	  					}
      			  );
    		  }
    			
    		  if (!self.hasPayout) {
    			  var vendorEntityType = null;
    			  if ($("#rdAccountClassPersonal")[0].checked) {
    				  vendorEntityType = 'P';
    			  } else if ($("#rdAccountClassBusiness")[0].checked) {
    				  vendorEntityType = 'B';
    			  }
    			  
    			  var vendorFirstName = StringUtil.trim($("#txtFirstName").val());
    			  var vendorLastName = StringUtil.trim($("#txtLastName").val());
    			  var vendorBirthDate = $("#txtBirthDatePicker").datepicker("getDate");
    			  
    			  var vendorCountry = $("#cmbVendorCountry").val();
    			  var vendorCity = StringUtil.trim($("#txtVendorCity").val());
    			  var vendorZip = StringUtil.trim($("#txtVendorPostalCode").val());
    			  var vendorAddress = StringUtil.trim($("#txtVendorAddress1").val());
    			  var vendorAddress2 = StringUtil.trim($("#txtVendorAddress2").val());
    			  
    			  var payoutMethod = {
    					  vendorEntityType : vendorEntityType,
    					  vendorFirstName : vendorFirstName,
    					  vendorLastName : vendorLastName,
    					  vendorBirthDate : vendorBirthDate,
    					  vendorCountry : vendorCountry,
    					  vendorCity : vendorCity,
    					  vendorZip : vendorZip,
    					  vendorAddress : vendorAddress,
    					  vendorAddress2 : vendorAddress2
    			  };
    			  
    			  paymentService.createPayoutMethod(payoutMethod, function() {
    				  self.hasPayout = true;
    				  tmpSavePlace();
        		  });
    		  } else {
    			  tmpSavePlace();
    		  }
    	  } else {
    		  placeService.updatePlace(newPlace, self.photoList,
  					function(isSuccess) {
    			  		if (isSuccess) {
    			  			DialogUtil.success( 'Congratulations! Your place is updated successfully!', function() {
    							document.location.href = webApplicationUrlPrefix + '/pages/dashboard/Listings.html';
    						});
    			  		}
  					}
    		  );
    	  }    	  
      };
      
      self.movePhotoUp = function(photoIndex) {
    	  var prevPhoto = self.photoList[photoIndex-1];
    	  var currentPhoto = self.photoList[photoIndex];
    	  
    	  self.photoList[photoIndex-1] = currentPhoto;
    	  self.photoList[photoIndex] = prevPhoto;
    	  //$scope.$apply(function() {});
      };
      
      self.rotatePhoto = function(photo) {
    	  ImageUtil.rotate('imgPlacePhoto_' + photo.photoId, photo, 90);
      };
      
      self.initialize();
      
  }]);