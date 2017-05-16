App.controller('placeDetailCtrl', ['$scope', '$controller', 'placeService', 'reservationService', 
                                   'commonService', 'enumerationService', 'reviewService', 
                                   function($scope, $controller, placeService, reservationService, 
                                		   commonService, enumerationService, reviewService) {
      var self = this;
      
      var marker = null;
      var map = null;
      var autocomplete = null;
      self.place = null;
      self.amentiesList = [];
      self.safetyAmentiesList = [];
      self.ruleList = [];
      var mainSliderId = "MainSlider";
      self.guestNumberArr = guestNumberArr;
      self.reviewList = [];
                  
      self.initialize = function() {
    	  var placeId = getUriParam('placeId');
    	  if (placeId == null || placeId == "") {
    		  openWindow(webApplicationUrlPrefix + "/pages/Main.xhtml");
    	  } else {
    		  placeService.getPlace(placeId).then(function(operationResult) {
        	  		self.displayPlaceDetails(operationResult.resultValue)
          		},
				function(errResponse){
					console.error('Error while fetching place detail');
				}
		      );
    		  
    		  $("#txtStartDatePicker").datepicker(
				{
					minDate : '+0',
					dateFormat : "dd.mm.yy",
					onSelect : function(selectedDate, cal) {
						var minDate = $('#txtStartDatePicker').datepicker('getDate');
  			            $("#txtEndDatePicker").datepicker( "option", "minDate", minDate.addMonths(1).addDays(-1));
					}
				});
    		  
    		  $("#txtEndDatePicker").datepicker(
				{
					minDate : '+1m',
					dateFormat : "dd.mm.yy",
					onSelect : function(selectedDate, cal) {
						
					}
				});
    		  
    		  var checkinDate = getUriParam(EnmUriParam.CHECKIN_DATE);
        	  var checkoutDate = getUriParam(EnmUriParam.CHECKOUT_DATE);
        	  
        	  if (checkinDate != null && checkinDate != '') {
        		  var tmpStartDate = Date.parse(checkinDate);
  				  $("#txtStartDatePicker").datepicker('setDate', tmpStartDate);
  				
  				  $("#txtEndDatePicker").datepicker( "option", "minDate", tmpStartDate.addMonths(1).addDays(-1)); 
  				 
  				 if (checkoutDate != null && checkoutDate!= '') {
  					 var tmpEndDate = Date.parse(checkoutDate);
  					 $("#txtEndDatePicker").datepicker('setDate', tmpEndDate);
  				 }
  				 
        	  }
        	  
        	  
        	  /*
        	  reviewService.listPlaceReview(placeId,
    			  function(reviewList) {
    			  	self.reviewList	= reviewList;
	  			  }
	  		  );
	  		  */
    	  }
      };
      
      self.getCurrentPhotoIndex = function(sliderKey) {
    	  return $('#hiddenPhotoIndex' + sliderKey).val();
      };
      
      self.displayPlaceDetails = function(place) {
    	  self.place = place;
    	  
    	  if (loginUserId != '') {
    		  listComplaint(EnmEntityType.PLACE);
    	  }
    	  
    	  if (self.place.coverPhoto != null) {
    		  var photoIndex = 0;
	      		for(var i = 0; i < self.place.photoList.length; i++) {
	      			var photo = self.place.photoList[i];
	      			if (photo.id == self.place.coverPhotoId) {
	      				photoIndex = i;
	      				break;
	      			}
	      		}
	      		$('#hiddenPhotoIndexMainSlider').val(photoIndex);
	      		self.displayCurrentPhoto();
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
    	  
    	  enumerationService.listEnumeration(null).then(function(operationResult) {
	  			self.amentiesList = operationResult.resultValue["place_amenty"];
	  			self.safetyAmentiesList = operationResult.resultValue["place_safety_amenty"];
	  			self.ruleList = operationResult.resultValue["place_rule"];
	  		},
				function(errResponse){
					console.error('Error while fetching Enumerations');
				}
		      );
    	  
    	  self.initializeMap();
      };
      
      self.hasAmenty = function(amentyKey) {
    	  if (self.place.amentyMap[amentyKey] == 1) {
    		  return true;
    	  } else {
    		  return false;
    	  }
      };
      
      self.hasSafetyAmenty = function(safetyAmentyKey) {
    	  if (self.place.safetyAmentyMap[safetyAmentyKey] == 1) {
    		  return true;
    	  } else {
    		  return false;
    	  }
      };
      
      self.hasRule = function(ruleKey) {
    	  if (self.place.ruleMap[ruleKey] == 1) {
    		  return true;
    	  } else {
    		  return false;
    	  }
      };
      
      self.changePhoto = function(step) {
    	  if (self.place.photoList.length > 1) {
    		  var currentPhotoIndex = self.getCurrentPhotoIndex(mainSliderId);
        	  if (currentPhotoIndex != "") {
        		  var intCurrentIndex = parseInt(currentPhotoIndex) + step;
        		  if (intCurrentIndex == self.place.photoList.length) {
        			  intCurrentIndex = 0;
        		  } else if (intCurrentIndex < 0) {
        			  intCurrentIndex = (self.place.photoList.length - 1);
        		  }
        		  $('#hiddenPhotoIndexMainSlider').val(intCurrentIndex);
        		  self.displayCurrentPhoto();
        	  }
    	  }
      };
      
      self.displayCurrentPhoto = function() {
    	  var intCurrentIndex = parseInt($('#hiddenPhotoIndexMainSlider').val());
    	  var currentPhoto = self.place.photoList[intCurrentIndex];
  		  var photoUrl = generatePlacePhotoUrl(self.place.id , currentPhoto.id, 'medium');
  		  $("#divPhotoSlider").css('background-image', 'url(' + photoUrl + ')');
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
				return "Private Room";
			} else if (self.place.placeTypeId == 3) {
				return "Shared Room";
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
	  	
	 self.onBookBtnClick = function() {
		 var placeId = self.place.id;
		 var guestNumber = $('#cmbGuestNumber').val();
		 var startDate = $("#txtStartDatePicker").datepicker("getDate");
		 var endDate = $("#txtEndDatePicker").datepicker("getDate");
		 
		 if (guestNumber != null && guestNumber != '' 
			 && startDate != null && startDate != ''
				 && endDate != null && endDate != '') {
			 var reservation = {
					 placeId : placeId,
					 guestNumber : guestNumber,
					 startDate : startDate,
					 endDate : endDate
				 }
				 
				 reservationService.startReservation(reservation, function(nextUrl) {
					 if (nextUrl) {
						 openWindow(nextUrl, true);
					 }
				 });
		 } else {
			 DialogUtil.warn( '\'Guest number\', \'Check In\' and \'Check Out\' are mandatory parameters for booking.');
		 }
	 };
	 
	 self.onFavoriteIconClicked = function(placeId) {
	  var placeId = self.place.id;
   	  if ($('#spanAddFavorite').hasClass('hidden-force')) {
   		  removeFavorite(EnmEntityType.PLACE, placeId, function(result) {
   			 if (result) {
   				 $('#spanRemoveFavorite').removeClass('hidden-force')
   				 $('#spanRemoveFavorite').addClass('hidden-force')
   			 } 
   		  });
   	  } else {
   		  addFavorite(EnmEntityType.PLACE, placeId, function(result) {
    			 if (result) {
    				 $('#spanRemoveFavorite').addClass('hidden-force')
       				 $('#spanRemoveFavorite').removeClass('hidden-force')
    			 } 
    		  });
   	  }
     };
     
     self.isPlaceFavorite = function() {
    	 if (self.place != null) {
    		 var placeId = self.place.id;
    	   	  	if (userFavoriteMap && userFavoriteMap[EnmEntityType.PLACE.toString()]) {
    	   		  placeFavoriteMap = userFavoriteMap[EnmEntityType.PLACE.toString()];
    	   		  if (placeFavoriteMap[placeId.toString()]) {
    					  return true;
    				  }
    	   	  }
    	 }
   	  	return false;
     };
     
     self.openComplaintWindow = function() {
    	 openComplaintWindow(EnmEntityType.PLACE, self.place.id);
     };
     
     self.isPlaceFavorite = function() {
    	 if (self.place != null) {
    		 var placeId = self.place.id;
    	   	  	if (userFavoriteMap && userFavoriteMap[EnmEntityType.PLACE.toString()]) {
    	   		  placeFavoriteMap = userFavoriteMap[EnmEntityType.PLACE.toString()];
    	   		  if (placeFavoriteMap[placeId.toString()]) {
    					  return true;
    				  }
    	   	  }
    	 }
   	  	return false;
     };
     
     self.isPlaceComplainted = function() {
    	 if (self.place != null) {
    		 return isEntityComplainted(EnmEntityType.PLACE, self.place.id);
    	 } else {
    		 return false;
    	 }
     };
     
     self.onContactHostBtnClicked = function() {
    	 if (loginUserId != '') {
    		 ajaxHtml(webApplicationUrlPrefix + '/static/html/ContactHost.html', 'divCommonModal', function() {
        		 $('#headerContactHost').html(self.place.user.firstName);
        		 $('#lnkContactHostUser').attr('href', $('#lnkHostUser').attr('href'));
        		 $('#imgContactHostUser').attr('src', $('#imgHostUser').attr('src'));
        		 $('#imgContactHostUser').attr('title', self.place.user.firstName);
        		 
        		 $('#imgContactHostClientUser').attr('src', $('#hiddenActiveUserProfileImageUrl').val());
        		 
        		 $("#txtContactHostStartDatePicker").datepicker(
				{
					minDate : new Date(),
					dateFormat : "dd.mm.yy",
					onSelect : function(selectedDate, cal) {
						
					}
				});
    		  
	    	  $("#txtContactHostEndDatePicker").datepicker(
				{
					minDate : new Date(),
					dateFormat : "dd.mm.yy",
					onSelect : function(selectedDate, cal) {
						
					}
				});
	    	  
	    	  var startDate = $("#txtStartDatePicker").datepicker("getDate")
	 		  var endDate = $("#txtEndDatePicker").datepicker("getDate")
	    	  
	    	  $("#txtContactHostStartDatePicker").datepicker('setDate', startDate);
	    	  $("#txtContactHostEndDatePicker").datepicker('setDate', endDate);
	    	  
	    	  $.each($('#cmbGuestNumber')[0].options, function (i, option) {
	    		    $('#cmbContactHostGuestNumber').append($('<option>', { 
	    		        value: option.value,
	    		        text : option.text 
	    		    }));
	    		});
	    	          		 
         	}); 
    	 } else {
    		 openLoginWindow();
    	 }
     };
     
     self.contactHost = function() {
    	 var placeId = self.place.id;
		 var guestNumber = $('#cmbContactHostGuestNumber').val();
		 var startDate = $("#txtContactHostStartDatePicker").datepicker("getDate");
		 var endDate = $("#txtContactHostEndDatePicker").datepicker("getDate");
		 var message = StringUtil.trim($("#txtContactHostMessage").val());
		 
		 if (guestNumber != null && guestNumber != '' 
			 && startDate != null && startDate != ''
				 && endDate != null && endDate != '' && message != '') {
			 var reservation = {
					 placeId : placeId,
					 guestNumber : guestNumber,
					 startDate : startDate,
					 endDate : endDate,
					 messageText : message
				 };
				 
				 reservationService.createInquiry(reservation, function() {
					 DialogUtil.success( 'Your message has been successfully sent to host!', function() {
						 reloadPage()
					 });
				 });
		 } else {
			 DialogUtil.warn( 'Please fill mandatory parameters!');
		 } 
     };
     
      self.initialize();
      
  }]);

function contactHost() {
	var scope = angular.element( $('#divBody') ).scope();
	scope.ctrl.contactHost();
}