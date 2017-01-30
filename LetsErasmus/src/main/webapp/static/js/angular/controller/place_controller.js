App.controller('placeCtrl', ['$scope', '$controller', 'placeService', 'commonService', 'enumerationService', 
                             function($scope, $controller, placeService, commonService, enumerationService) {
      var self = this;
      
      var marker = null;
      var map = null;
      var autocomplete = null;
      self.photoList = [];
      self.amentiesList = [];
      self.safetyAmentiesList = [];
      self.ruleList = [];
      
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
      
     /*
      self.amentiesList = [
        {id: 'washing_machine', text: 'Washing Machine'},
        {id: 'dishwasher', text: 'Dishwasher'},
        {id: 'fridge', text: 'Fridge'},
        {id: 'kitchen_supplies', text: 'Kitchen Supplies'},
        {id: 'closet_drawer', text: 'Closet/Drawer'},
        {id: 'tv', text: 'TV'},
        {id: 'heat', text: 'Heat'},
        {id: 'air_condition', text: 'Air Conditioning'},
        {id: 'iron', text: 'Iron'},
        {id: 'hair_dryer', text: 'Hair Dryer'},
        {id: 'bed_sheet', text: 'Bed Sheet'},
        {id: 'pillow', text: 'Pillow'},
        {id: 'towel', text: 'Towel'},
      ];
      
      self.safetyAmentiesList = [
                           {id: 'lock_on_bed_door', text: 'Lock on Bedroom Door'},
                           {id: 'smoke_detector', text: 'Smoke Detector'},
                           {id: 'carbon_monoxide_detector', text: 'Carbon Monoxide Detector'},
                           {id: 'fire_extinguisher', text: 'Fire Extinguisher'}
                         ];
      
      self.ruleList = [
                                 {id: 'pets', text: 'Suitable For Pets'},
                                 {id: 'smoke', text: 'Smoke Allowed'},
                                 {id: 'event_party', text: 'Events Or Parties Allowed'}
                               ];
      */
     
      self.initialize = function() {
    	  enumerationService.listEnumeration(null).then(function(operationResult) {
  	  			self.amentiesList = operationResult.resultValue["place_amenty"];
  	  			self.safetyAmentiesList = operationResult.resultValue["place_safety_amenty"];
  	  			self.ruleList = operationResult.resultValue["place_rule"];
    		},
			function(errResponse){
				console.error('Error while fetching Enumerations');
			}
	      );
    	  
    	  //$("#divStep1").css("display", "none");
    	  $("#divStep2").css("display", "none");
    	  $("#divStep3").css("display", "none");
    	  $("#divStep4").css("display", "none");
    	  $("#divStep5").css("display", "none");
    	  $("#divStep6").css("display", "none");
    	  $("#divStep7").css("display", "none");
    	  
    	  self.initPhotoTab();
    	  
    	  $("#txtStartDatePicker").datepicker(
			{
				minDate : new Date(),
				dateFormat : "d MM, y",
				onSelect : function(selectedDate, cal) {
					
				}
			});
    	  
    	  $("#txtEndDatePicker").datepicker(
    				{
    					minDate : new Date(),
    					dateFormat : "d MM, y",
    					onSelect : function(selectedDate, cal) {
    						
    					}
    				});    	  
      };
      
      self.initializeMap = function() {
    	  self.selectedLocation = new google.maps.LatLng(41.0082376, 28.97835889999999);
			
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
        	  			/*
        	  			var image = new Image();
        	  			image.src = event.target.result;
        	  			image.width = 200;
        	  			divPhotoContainer.appendChild(image);
        	  			*/
        	  			var photoId = generateRandomValue(1, 1000000);
        	  			self.photoList.push({ 'photoId':photoId, 'file': file, 'src': event.target.result});
        	  			commonService.fakeAjaxCall();
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
    	  	var formData = tests.formdata ? new FormData() : null;
    	  	for (var i = 0; i < files.length; i++) {
    	  		if (tests.formdata) {
    	  			formData.append('file', files[i]);
    	  		}
    	  		previewfile(files[i]);
    	  	}
    	  	/*
    	  	formData.append('fileName', 'Test');
    	  	
    	  	if (tests.formdata) {
    	  		placeService.addPhoto(formData);
    	  	}
    	  	*/
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
	            window.alert("Autocomplete's returned place contains no geometry");
	            return;
	        }
	  
	        map.setCenter(place.geometry.location);
	        map.setZoom(15);
	        /*
	        if (place.geometry.viewport) {
	            map.fitBounds(place.geometry.viewport);
	        } else {
	            map.setCenter(place.geometry.location);
	            map.setZoom(9);
	        }
	        */
	        /*
	        marker.setIcon(({
	            url: place.icon,
	            size: new google.maps.Size(71, 71),
	            origin: new google.maps.Point(0, 0),
	            anchor: new google.maps.Point(17, 34),
	            scaledSize: new google.maps.Size(35, 35)
	        }));
	        */
	        if (marker == null) {
	        	marker = new google.maps.Marker({
			        map: map,
					draggable: true,
			        position: place.geometry.location
			    });
	        	
	        	google.maps.event.addListener(marker, 'dragend', function() {
		              // updateMarkerStatus('Drag ended');
		              //geocodePosition(marker.getPosition());
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
	                //document.getElementById('txtState').value = place.address_components[i].long_name;
	            } else if(place.address_components[i].types[0] == 'locality'){
	                document.getElementById('hiddenLocality').value = place.address_components[i].long_name;
	                //document.getElementById('txtState').value = place.address_components[i].long_name;
	            }
	        }
	        /*
	        document.getElementById('location').innerHTML = place.formatted_address;
	        document.getElementById('lat').innerHTML = place.geometry.location.lat();
	        document.getElementById('lon').innerHTML = place.geometry.location.lng();
	        */
    	  
	    	//self.selectedPlaceName = result.name;
	    	//self.selectedLat = result.geometry.location.lat();
	    	//self.selectedLng = result.geometry.location.lng();
	    };
	    
	  self.validate = function(step) {
		  var isValid = true;
		  
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
    		  } else if ($("#txtStartDatePicker").datepicker("getDate") == ''
    			  || $("#txtEndDatePicker").datepicker("getDate") == '') {
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
    			  isValid = false;
    		  }
    	  } 
		  
		  if (step == null || step == 7) {
    		  if ($('#txtTitle').val() == '' 
    			  || $('#txtDescription').val() == '') {
    			  isValid = false;
    		  }
    	  }
		  
		  return isValid;
	  };
      
      self.next = function(step) {
    	  var isValid = self.validate(step);
    	  
    	  if (isValid) {
    		  $('#divProgress' + step).addClass('progress-section--completed');
    		  
    		  if (step == 7) {
    			  self.savePlace();
    		  } else {
    			  var currentStep = step;
            	  var nextStep = step + 1;
            	  if ($("#divStep" + nextStep).length > 0) {
            		  $("#divStep" + currentStep).css("display", "none");
            		  $("#divStep" + nextStep).css("display", "");
            	  }
            	  if (step == 4) {
            		  if (map == null) {
            			  self.initializeMap();
            		  }
            	  }   
    		  }
    	  } else {
    		  //showMessage("alert", "Warning", "Please fill required fields!");
    		  $.prompt("Please fill required fields!");
    		  return false;
    	  }  
      };
      
      self.back = function(step) {
    	  var currentStep = step;
    	  var nextStep = step - 1;
    	  if ($("#divStep" + nextStep).length > 0) {
    		  $("#divStep" + currentStep).css("display", "none");
    		  $("#divStep" + nextStep).css("display", "");
    	  }
      };
      
      self.savePlace = function() {
    	  var newPlace = {};
    	  if ($("#rdPlaceTypeEntirePlace")[0].checked) {
    		  newPlace.placeType = 1;
    	  } else if ($("#rdPlaceTypePrivateRoom")[0].checked) {
    		  newPlace.placeType = 2;
    	  } else if ($("#rdPlaceTypeSharedRoom")[0].checked) {
    		  newPlace.placeType = 3;
    	  } 
    	  newPlace.homeType = $("#cmbHomeType").val();
    	  newPlace.bedNumber = getCounterElementValue('spanBedNumber');
    	  newPlace.bathroomNumber = getCounterElementValue('spanBathroomNumber');
    	  newPlace.bathroomType = $('#cmbBathroomType').val();
    	  
    	  newPlace.guestNumber = getCounterElementValue('spanGuestNumber');
    	  newPlace.guestGender = $("#cmbGuestGender").val();
    	  newPlace.placeMateNumber = getCounterElementValue('spanPlaceMateNumber');
    	  if ($("#cmbPlaceMateGender")[0].selectedIndex > 0) {
    		  newPlace.placeMateGender = $("#cmbPlaceMateGender").val();
    	  }
    	  
    	  newPlace.price = $("#txtDailyPrice").val();
    	  if ($("#txtDepositPrice").val() != '') {
    		  newPlace.depositPrice = $("#txtDepositPrice").val();
    	  }
    	  newPlace.currencyId = $("#cmbCurrencyId").val();
    	  newPlace.billsIncluded = ($("#chbBillsIncluded")[0].checked ? 1 : 0);
    	  newPlace.startDate = $.datepicker.formatDate('d.m.yy', $("#txtStartDatePicker").datepicker("getDate"));
    	  newPlace.endDate = $.datepicker.formatDate('d.m.yy', $("#txtEndDatePicker").datepicker("getDate"));
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
    	  newPlace.location.city = $("#txtCity").val();
    	  newPlace.location.street = $("#txtStreet").val();
    	  newPlace.location.postalCode = $("#txtPostalCode").val();
    	  newPlace.location.userAddress = $("#txtBuildind").val();
    	  newPlace.location.latitude = $("#txtLatitude").val();
    	  newPlace.location.longitude = $("#txtLongitude").val();
    	  
    	  newPlace.title = $("#txtTitle").val()
    	  newPlace.description = $("#txtDescription").val()
    	  
    	  /*
    	  var formData = tests.formdata ? new FormData() : null;
		  	for (var i = 0; i < self.photoList.length; i++) {
		  		formData.append('file', self.photoList[i].file);
		  	}
      	  formData.append('place', angular.toJson(newPlace));
    	  */
      	  NProgress.start(4000, 10);
    	  placeService.savePlace(newPlace, self.photoList).then(
			function(operationResult) {
				NProgress.done(true); 
				if (operationResult.resultCode == EnmOperationResultCode.SUCCESS) {
					NProgress.done(true); 
					if (operationResult.resultCode == EnmOperationResultCode.SUCCESS) {
						alert('Congradulations! Your place is saved successfully!');	
						document.location.href = webApplicationUrlPrefix + '/pages/Main.xhtml';
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
      
      self.initialize();
      
  }]);