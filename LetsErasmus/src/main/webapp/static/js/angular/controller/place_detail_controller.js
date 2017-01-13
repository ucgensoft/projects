App.controller('placeDetailCtrl', ['$scope', '$controller', 'placeService', 'commonService', function($scope, $controller, placeService, commonService) {
      var self = this;
      
      var marker = null;
      var map = null;
      var autocomplete = null;
      self.place = null;
                  
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
    	  var placeId = getUriParam('placeId');
    	  if (placeId == null || placeId == "") {
    		  openWindow(webApplicationUrlPrefix + "/pages/Main.xhtml");
    	  } else {
    		  placeService.getPlace(placeId).then(function(operationResult) {
        	  		self.displayPlaceDetails(operationResult.resultValue)
          		},
				function(errResponse){
					console.error('Error while fetching Portfolio');
				}
		      );
    		  
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
    	  }
      };
      
      self.displayPlaceDetails = function(place) {
    	  self.place = place;
    	  self.initializeMap();
      };
      
      self.initializeMap = function() {
    	  var placeLocation = new google.maps.LatLng(self.place.location.latitude, self.place.location.longitude);
			
			var mapOptions = {
					zoom: 15,
					center: placeLocation,
					disableDefaultUI: false
				}
			
			map = new google.maps.Map(document.getElementById('divMap'), mapOptions);

			map.setCenter(placeLocation);

	        marker = new google.maps.Marker({
		        map: map,
				draggable: false,
		        position: placeLocation
		    });
        		        
	        //marker.setPosition(placeLocation);
	        //marker.setVisible(true);
			
      };
      
      self.onTabChange = function(tabIndex) {
    	  for(var i = 1; i < 5; i++) {
    		  if (i == tabIndex) {
    			  $("#btnTab" + i).addClass('text_1aunhvg-o_O-tab_wurtoe-o_O-tab_selected_1i7fvde') ;
    			  $("#btnTab" + i).removeClass('text_1aunhvg-o_O-tab_wurtoe');
    			  $("#spanTabText" + i).addClass('selectedTab');
    		  } else {
    			  $("#btnTab" + i).addClass('text_1aunhvg-o_O-tab_wurtoe') ;
    			  $("#btnTab" + i).removeClass('text_1aunhvg-o_O-tab_wurtoe-o_O-tab_selected_1i7fvde') ; 
        		  $("#spanTabText" + i).removeClass('selectedTab');
    		  }
    	  }
    	  if (tabIndex == 1) {
    		  document.location.href = "#divPlaceSummary";
    	  }
    	  if (tabIndex == 2) {
    		  document.location.href = "#divHostPorfile";
    	  }
    	  if (tabIndex == 3) {
    		  document.location.href = "#divLocation";
    	  }
    	  if (tabIndex == 4) {
    		  document.location.href = "#divReviews";
    	  }
      };
      
      self.getPriceText = function() {
    	  if (self.place != null) {
    		  return self.place.price + " " + getCurrencySymbol(self.place.currencyId);
    	  } else {
    		  return "";
    	  }
	  };
      
	  self.getLocationText = function() {
		  if (self.place != null) {
			  var locationText = self.place.location.country + ", " + self.place.location.state;
			  if (self.place.location.street != null && self.place.location.street != "") {
				  locationText += ", " + self.place.location.street;
			  }
			  return locationText;
		  } else {
			  return "";
		  }
	  };
	  
	  self.getPlaceTypeDescription = function() {
		  if (self.place != null) {
			if (self.place.placeTypeId == 1) {
				return "Entire Place";
			} else if (self.place.placeTypeId == 2) {
				return "Shared Room";
			} else if (self.place.placeTypeId == 3) {
				return "Rivate Room";
			}
		  } else {
			  return "";
		  }
	};
		
	 self.getHomeTypeDescription = function() {
		 if (self.place != null) {
			if (self.place.homeTypeId == 1) {
				return "House";
			} if (self.place.homeTypeId == 2) {
				return "Apartment";
			} if (self.place.homeTypeId == 3) {
				return "Hostel";
			}
		 } else {
			 return "";
		 }
	 };
	  	
      self.initialize();
      
  }]);