App.controller('searchResultCtrl', ['$scope', '$controller', '$http', 'placeService', 'commonService', 'enumerationService', 
                                    function($scope, $controller, $http, placeService, commonService, enumerationService) {
      var self = this;
      
      var map = null; 
      var infoWindow = null;
      self.selectedLocation = null;
      self.selectedLat = null;
      self.selectedLng = null;
      self.selectedPlaceName = null;
      self.minPrice = 0;
      self.maxPrice = 500;
      
      self.amentiesList = [];
      self.safetyAmentiesList = [];
      self.ruleList = [];
      
      var originalPlaceList = [];
      self.placeList = [];

      initialize = function() {
    	  
    	  	self.selectedPlaceName = getUriParam(EnmUriParam.LOCATION);
    	  	self.selectedLat = getUriParam(EnmUriParam.LATITUDE);  
    	  	self.selectedLng = getUriParam(EnmUriParam.LONGITUDE);
	    	var startDate = getUriParam(EnmUriParam.CHECKIN_DATE);
	    	var endDate = getUriParam(EnmUriParam.CHECKOUT_DATE);
		  
			self.selectedLocation = new google.maps.LatLng(self.selectedLat, self.selectedLng);
			var mapOptions = {
				zoom: 14,
				center: self.selectedLocation,
				disableDefaultUI: false
			}
			
			map = new google.maps.Map(document.getElementById('divMap'), mapOptions);
			
			infoWindow = new google.maps.InfoWindow();
			
			google.maps.event.addListener(map, 'click', function() {
		          //infoWindow.close();
		        });
			
			self.listPlace(self.setPlaceList);
			
			$("#txtSearchPlace").geocomplete().bind("geocode:result",
	      			function(event, result) {
	      				self.onPlaceChange(event, result);
	      			});
			
			$("#txtStartDatePicker").datepicker(
  			{
  				minDate : new Date(),
  				dateFormat : "dd.mm.yy",
  				onSelect : function(selectedDate, cal) {
  					self.validateSearch();
  					setTimeout(function() {
  						$("#txtEndDatePicker").focus()
  					}, 200);
  				}
  			});

			$("#txtStartDatePicker").datepicker().val(startDate);
			
	      	$("#txtEndDatePicker").datepicker(
  			{
  				minDate : new Date(),
  				dateFormat : "dd.mm.yy",
  				onSelect : function(selectedDate, cal) {
  					self.validateSearch();
  				}
  			});
	      	
	      	$("#txtEndDatePicker").datepicker().val(endDate);
	      	
	      	
      	    $( "#divPriceSlider" ).slider({
      	      range: true,
      	      min: 0,
      	      max: 500,
      	      values: [ 0, 500 ],
      	      slide: function( event, ui ) {
      	    	self.setPriceRange(ui.values[0], ui.values[1]);
      	      }
      	    });
      	    
      	  $(".ui-slider-range").css("background-color", "rgb(3, 169, 244)");
      	  
      	enumerationService.listEnumeration(null).then(function(operationResult) {
		  			self.amentiesList = operationResult.resultValue["place_amenty"];
		  			self.safetyAmentiesList = operationResult.resultValue["place_safety_amenty"];
		  			self.ruleList = operationResult.resultValue["place_rule"];
			},
			function(errResponse){
				console.error('Error while fetching Enumerations');
			}
	      );
      };
      
      self.setPriceRange = function (selectedMinPrice, selectedMaxPrice) {
    	  	self.minPrice= selectedMinPrice;
	        self.maxPrice= selectedMaxPrice;
	        $("#spanMinPrice")[0].innerText=self.minPrice.toString();
	        $("#spanMaxPrice")[0].innerText=self.maxPrice.toString();
	        self.basicSearch();
      };
      	
		self.search = function() {
	    	var startDate = $.datepicker.formatDate('d.m.yy', $(
				"#txtStartDatePicker").datepicker("getDate"));
	    	
			var endDate = $.datepicker.formatDate('d.m.yy', $("#txtEndDatePicker")
						.datepicker("getDate"));
			
			openWindow(webApplicationUrlPrefix + '/pages/SearchResult.xhtml' 
					+ '?' + EnmUriParam.LOCATION + '=' + self.selectedPlaceName 
					+ '&' + EnmUriParam.LATITUDE + '=' + self.selectedLat 
					+ '&' + EnmUriParam.LONGITUDE + '=' + self.selectedLng 
					+ "&" + EnmUriParam.CHECKIN_DATE + "=" + startDate 
					+ "&" + EnmUriParam.CHECKOUT_DATE + "=" + endDate, true);
			
	      };
	      
	      self.basicSearch = function() {
	    	var newPlaceList = [];
	    	
	    	for(var i = 0; i < originalPlaceList.length; i++) {
	    		var place = originalPlaceList[i];
	    		var isMatching = true;
	    		if (place.price < self.minPrice || place.price > self.maxPrice) {
	    			isMatching = false;
	    		}
	    		
	    		if (isMatching) {
	    			var placeTypeSelected = false;
		    		for(var x = 1; x < 10; x++) {
		    			if ($("#iconPlaceTypeId_" + x).length > 0) {
		    				if($("#iconPlaceTypeId_" + x).hasClass('IconFilter-icon--enabled')) {
		    					if (place.placeTypeId != x) {
		    						isMatching = false;
		    						break;
		    					}
			    			}
		    			} else {
		    				break;
		    			}
		    		}
	    		}
	    		
	    		if (isMatching) {
	    			var guestNumber = $('#txtGuestNumber').val();
	    			if (guestNumber != '' && parseInt(guestNumber) > place.guestNumber) {
	    				isMatching = false;
	    			}
	    		}
	    		
	    		if (isMatching) {
	    			var guestGender = $('#cmbGuestGender').val();
	    			if (guestGender != '-1' && guestGender != place.guestGender) {
	    				isMatching = false;
	    			}
	    		}
	    		
	    		if (isMatching) {
	    			var bathroomType = $('#cmbBathroomType').val();
	    			if (bathroomType != '-1' && parseInt(bathroomType) != place.bathroomType) {
	    				isMatching = false;
	    			}
	    		}
	    		
	    		if (isMatching) {
	    			var bedNumber = $('#txtBedNumber').val();
	    			if (bedNumber != '' && parseInt(bedNumber) > place.bedNumber) {
	    				isMatching = false;
	    			}
	    		}
	    		
	    		if (isMatching) {
	    			for (var x = 0; x < self.amentiesList.length; x++) {
			      		  var itemAmenties = self.amentiesList[x];
			      		  if ($('#chb_' + itemAmenties.enumKey)[0].checked) {
			      			if (place.amenties == null || place.amenties == '' 
			      				|| place.amenties.indexOf(itemAmenties.enumKey) < 0) {
			      				isMatching = false;
			      				break;
			      			}
			      		  }
			      	  }
	    		}
	    		
	    		if (isMatching) {
	    			for (var x = 0; x < self.safetyAmentiesList.length; x++) {
			      		  var itemSafetyAmenties = self.safetyAmentiesList[x];
			      		  if ($('#chb_' + itemSafetyAmenties.enumKey)[0].checked) {
			      			if (place.amenties == null || place.amenties == '' 
			      				|| place.amenties.indexOf(itemSafetyAmenties.enumKey) < 0) {
			      				isMatching = false;
			      				break;
			      			}
			      		  }
			      	  }
	    		}
	    		
	    		if (isMatching) {
	    			for (var x = 0; x < self.ruleList.length; x++) {
			      		  var itemRule = self.ruleList[x];
			      		  if ($('#chb_' + itemRule.enumKey)[0].checked) {
			      			if (place.rules == null || place.rules == '' 
			      				|| place.rules.indexOf(itemRule.enumKey) < 0) {
			      				isMatching = false;
			      				break;
			      			}
			      		  }
			      	  }
	    		}
	    		
	    		if (isMatching) {
	    			newPlaceList.push(place);
	    		}
	    	}
	    	
	    	self.placeList = newPlaceList;
	    	//refreshAngularScope($scope);
	    	hideExtendedSearchFields();
	    	commonService.fakeAjaxCall(self.refreshMap);
	      };

	      self.validateSearch = function() {
	      	var startDate = $.datepicker.formatDate('d.m.yy', $("#txtStartDatePicker")
	      			.datepicker("getDate"));
	      	var endDate = $.datepicker.formatDate('d.m.yy', $("#txtEndDatePicker")
	      			.datepicker("getDate"));

	      	if (self.selectedPlaceName == null || self.selectedPlaceName == ''
	      			|| startDate == null || startDate == '' || endDate == null
	      			|| endDate == '') {
	      		$("#btnSearch").attr("disabled", true);
	      		isSearchValid = false;
	      		return false;
	      	} else {
	      		$("#btnSearch").attr("disabled", false);
	      		isSearchValid = true;
	      		return true;
	      	}
	      };
      
      self.onPlaceChange = function (event, result) {
	    	self.selectedPlaceName = result.name;
	    	self.selectedLat = result.geometry.location.lat();
	    	self.selectedLng = result.geometry.location.lng();
	    	setTimeout(function() {
				$("#txtStartDatePicker").focus()
			}, 100);
	    };
        
	  self.setPlaceList = function(list) {
		  self.placeList = list;
		  setTimeout(function() {
			  self.refreshMap();
		  }, 500);
	  };
	  
	  self.refreshMap = function() {
		  map.customMarkerMap = [];
		  for(var i = 0; i < self.placeList.length; i++) {
			  var place = self.placeList[i];
			  var myLatlng = new google.maps.LatLng(place.location.latitude, place.location.longitude);
			  var overlay = new CustomMarker(
						myLatlng, 
						map,
						{
							marker_id: place.id,
							marker_element_id : "divMapMarker_" + place.id,
							infoWindow: infoWindow
						}
					);
			  map.customMarkerMap.push(overlay);
			  self.calculateDistance(myLatlng, self.selectedLocation, place);
		  }
	  };
	  
	  self.changePhoto = function(place, step) {
		  if (place.photoList != null) {
			  self.applyPhotoChange(place, step);
		  } else {
			  self.listPhoto(place.id, function(photoList) {
				  place.photoList = photoList;
				  self.applyPhotoChange(place, step);
			  });
		  }
      };
      
      self.applyPhotoChange = function(place, step) {
    	  var currentPhotoIndex = $('#hiddenPlaceSliderPhotoIndex_' + place.id).val();
    	  if (currentPhotoIndex != "") {
    		  var intCurrentIndex = parseInt(currentPhotoIndex) + step;
    		  if (intCurrentIndex == place.photoList.length) {
    			  intCurrentIndex = 0;
    		  } else if (intCurrentIndex < 0) {
    			  intCurrentIndex = (place.photoList.length - 1);
    		  }
    		  $('#hiddenPlaceSliderPhotoIndex_' + place.id).val(intCurrentIndex);
    		  
        	  var currentPhoto = place.photoList[intCurrentIndex];
        	  var photoName = currentPhoto.id + "_large." + currentPhoto.fileSuffix;;
      		  var photoUrl = webApplicationUrlPrefix + "/place/images/" + place.id + "/" + photoName;
      		  $("#imgPlaceSlider_" + place.id).attr('src', photoUrl);
    	  }
      }
	  
	  self.listPlace = function(fn) {
    	  placeService.listPlace()
              .then(function(operationResult) {
            	  		originalPlaceList = operationResult.objectList;
            	  		self.setPlaceList(operationResult.objectList)
              		},
					function(errResponse){
						console.error('Error while fetching Portfolio');
					}
  			      );
      };
      
      self.listPhoto = function(placeId, fn) {
    	  placeService.listPhoto(placeId)
              .then(function(operationResult) {
            	  		fn(operationResult.objectList)
              		},
					function(errResponse){
						console.error('Error while fetching Portfolio');
					}
  			      );
      };
      
      showExtendedSearchFields = function() {
		document.getElementById("divExtendedSearchFields").style.display = "";
		document.getElementById("filterToolBarId_Extended").style.display = "";

		document.getElementById("filterToolBarId").style.display = "none";
		document.getElementById("searchResultsId").style.display = "none";
		document.getElementById("divPagination").style.display = "none";
	};

	hideExtendedSearchFields = function() {
		document.getElementById("divExtendedSearchFields").style.display = "none";
		document.getElementById("filterToolBarId_Extended").style.display = "none";

		document.getElementById("filterToolBarId").style.display = "";
		document.getElementById("searchResultsId").style.display = "";
		document.getElementById("divPagination").style.display = "";
	};
	
	self.onFilterElementClick = function(element, selectedClassName) {
		if($(element.target).hasClass(selectedClassName)) {
			$(element.target).removeClass(selectedClassName);
		} else {
			$(element.target).addClass(selectedClassName);
		}
	};
	
	self.calculateDistance = function (origin, destination, place) {
		var service = new google.maps.DistanceMatrixService;
		 service.getDistanceMatrix({
	          origins: [origin],
	          destinations: [destination],
	          travelMode: 'DRIVING',
	          unitSystem: google.maps.UnitSystem.METRIC,
	          avoidHighways: false,
	          avoidTolls: false
	        }, function(response, status) {
	          if (status == 'OK') {
	        	  $("#spanPlaceDistance_" + place.id)[0].innerText = response.rows[0].elements[0].distance.text;
	        	  place.distance = response.rows[0].elements[0].distance.text;
	          }
	        }); 
	};
	
	self.getPlaceTypeDescription = function(place) {
		if (place.placeTypeId == 1) {
			return "Shared Flat";
		} else if (place.placeTypeId == 2) {
			return "Host Family";
		} else if (place.placeTypeId == 3) {
			return "Student Hall";
		} else if (place.placeTypeId == 3) {
			return "Full Apartment";
		}
	};
	
	self.getBillsDescription = function(place) {
		if (place.bills_include == 'Y') {
			return "Bills Included";
		} else {
			return "";
		}
	};
	
	self.getPriceText = function(place) {
		return place.price + " " + getCurrencySymbol(place.currencyId);
	};
	
	self.deleteMarkers = function () {
		if (map != null && map.customMarkerMap != undefined) {
			for (var i = 0; i < map.customMarkerMap.length; i++) {
	        	var customMarker = map.customMarkerMap[i];
	        	customMarker.remove();
	        	customMarker.map = null;
	        	customMarker = null;
	        }
	        map.customMarkerMap = [];
		}
      };
      
      self.animateMarker = function (placeId, animateFlag) {
    	  if (map != null && map.customMarkerMap != undefined) {
    		  var customMarker = null;
    		  for (var i = 0; i < map.customMarkerMap.length; i++) {
		        customMarker = map.customMarkerMap[i];
		        if (customMarker.args.marker_id == placeId) {
		        	break;
		        }
		      }
    		  
    		  if (customMarker != null) {
    			  if (animateFlag) {
    				  customMarker.bounce(true);
	  		      } else {
	  		    	customMarker.bounce(false);
	  		      }
    		  }
    	  }
      };
      
      self.openPlaceDetailWindow = function(placeId) {
    	  
    	  var startDate = getUriParam(EnmUriParam.CHECKIN_DATE);
    	  var endDate = getUriParam(EnmUriParam.CHECKOUT_DATE);
    	  
    	  var placeDetailUrl = webApplicationUrlPrefix + '/pages/PlaceDetail.xhtml' 
    	  	+ '?' + EnmUriParam.PLACE_ID + '=' + placeId
    	  	+ '&' + EnmUriParam.CHECKIN_DATE + '=' + startDate
    	  	+ '&' + EnmUriParam.CHECKOUT_DATE + '=' + endDate;
    	  
    	  openWindow(placeDetailUrl, false);
      };
      	
	initialize();
      
  }]);