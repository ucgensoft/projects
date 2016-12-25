App.controller('placeCtrl', ['$scope', '$controller', 'placeService', function($scope, $controller, placeService) {
      var self = this;
      
      var marker = null;
      var map = null;
      var autocomplete = null;
      
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
      
      
      self.initialize = function() {
    	  $("#divStep2").css("display", "none");
    	  $("#divStep3").css("display", "none");
    	  $("#divStep4").css("display", "none");
    	  $("#divStep5").css("display", "none");
    	  $("#divStep6").css("display", "none");
    	  $("#divStep7").css("display", "none");
    	  
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
      }
      
      self.onMarkerMove = function() {
    	    
      };
      
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
	        
	        //Location details
	        
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
      
      self.next = function(step) {
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
      };
      
      self.back = function(step) {
    	  var currentStep = step;
    	  var nextStep = step - 1;
    	  if ($("#divStep" + nextStep).length > 0) {
    		  $("#divStep" + currentStep).css("display", "none");
    		  $("#divStep" + nextStep).css("display", "");
    	  }
      };
      
      self.finish = function() {
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
    	  newPlace.bathroomType = getCounterElementValue('cmbBathroomType');
    	  
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
    	  newPlace.billsIncluded = ($("#rdPlaceTypeSharedRoom")[0].checked ? 1 : 0);
    	  newPlace.startDate = $.datepicker.formatDate('d.m.yy', $("#txtStartDatePicker").datepicker("getDate"));
    	  newPlace.endDate = $.datepicker.formatDate('d.m.yy', $("#txtEndDatePicker").datepicker("getDate"));
    	  
    	  var amenties = '';
    	  for (var i = 0; i < self.amentiesList.length; i++) {
    		  var itemAmenties = self.amentiesList[i];
    		  if ($('#chb_' + itemAmenties.id)[0].checked) {
    			  if (amenties != '') {
    				  amenties = amenties + ",";
    			  }
    			  amenties = amenties + "'" + itemAmenties.id + "'";
    		  }
    	  }
    	  newPlace.amenties = amenties;
    	  
    	  var safetyAmenties = '';
    	  for (var i = 0; i < self.safetyAmentiesList.length; i++) {
    		  var itemAmenties = self.safetyAmentiesList[i];
    		  if ($('#chb_' + itemAmenties.id)[0].checked) {
    			  if (safetyAmenties != '') {
    				  safetyAmenties = safetyAmenties + ",";
    			  }
    			  safetyAmenties = safetyAmenties + "'" + itemAmenties.id + "'";
    		  }
    	  }
    	  
    	  newPlace.safetyAmenties = safetyAmenties;
    	  
    	  var rules = '';
    	  for (var i = 0; i < self.ruleList.length; i++) {
    		  var rule = self.ruleList[i];
    		  if ($('#chb_' + rule.id)[0].checked) {
    			  if (rules != '') {
    				  rules = rules + ",";
    			  }
    			  rules = rules + "'" + rule.id + "'";
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
    	  
    	  placeService.createPlace(newPlace);
    	  
      };
      
      self.initialize();
      
  }]);