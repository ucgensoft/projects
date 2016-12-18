App.controller('searchResultCtrl', ['$scope', '$controller', 'placeService', 'commonService', function($scope, $controller, placeService, commonService) {
      var self = this;
      
      var map = null; 
      var infoWindow = null;
      self.selectedLocation = null;
      self.selectedLat = null;
      self.selectedLng = null;
      self.selectedPlaceName = null;
      self.minPrice = 0;
      self.maxPrice = 500;
      
      var originalPlaceList = [];
      self.placeList = [];

      initialize = function() {
    	  	self.selectedPlaceName = getUriParam('place');
    	  	self.selectedLat = getUriParam('lat');  
    	  	self.selectedLng = getUriParam('lng');
	    	var startDate = getUriParam('startDate');
	    	var endDate = getUriParam('endDate');
		  
			self.selectedLocation = new google.maps.LatLng(self.selectedLat, self.selectedLng);
			var mapOptions = {
				zoom: 14,
				center: self.selectedLocation,
				disableDefaultUI: false
			}
			
			map = new google.maps.Map(document.getElementById('divMap'), mapOptions);
			
			infoWindow = new google.maps.InfoWindow();
			
			self.listPlace(self.setPlaceList);
			
			$("#txtSearchPlace").geocomplete().bind("geocode:result",
	      			function(event, result) {
	      				self.onPlaceChange(event, result);
	      			});
			
			$("#txtStartDatePicker").datepicker(
  			{
  				minDate : new Date(),
  				dateFormat : "d MM, y",
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
  				dateFormat : "d MM, y",
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
      	  $(".ui-slider-range").css("background-color", "rgb(255, 133, 79)");
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
			
			openWindow(webApplicationUrlPrefix + '/pages/SearchResult.xhtml?place='
						+ self.selectedPlaceName + "&lat=" + self.selectedLat + "&lng="
						+ self.selectedLng + "&startDate=" + startDate + "&endDate="
						+ endDate, true);
	      };
	      
	      self.basicSearch = function() {
	    	var newPlaceList = [];
	    	
	    	for(var i = 0; i < originalPlaceList.length; i++) {
	    		var place = originalPlaceList[i];
	    		var isMatching = false;
	    		if (place.price >= self.minPrice && place.price <= self.maxPrice) {
	    			isMatching = true;
	    		}
	    		
	    		if (isMatching) {
	    			var placeTypeSelected = false;
		    		for(var x = 1; x < 10; x++) {
		    			if ($("#iconPlaceTypeId_" + x).length > 0) {
		    				if($("#iconPlaceTypeId_" + x).hasClass('IconFilter-icon--enabled')) {
		    					placeTypeSelected = true;
		    					if (place.placeTypeId == x) {
		    						isMatching = true;
		    						break;
		    					} else {
		    						isMatching = false;
		    					}
			    			}
		    			} else {
		    				break;
		    			}
		    		}
		    		
		    		if (!placeTypeSelected) {
		    			isMatching = true;
		    		}
	    		}
	    			    		
	    		if (isMatching) {
	    			newPlaceList.push(place);
	    		}
	    	}
	    	
	    	self.listPlaceAjax(null);
	    	//self.listPlaceAjax(self.setPlaceList);
	    	//self.setPlaceList(newPlaceList);
	    	self.placeList = newPlaceList;
	      };
	      
	      self.advancedSearch = function() {
	    	var newPlaceList = [];
	    	
	    	for(var i = 0; i < self.placeList.length; i++) {
	    		var isMatching = false;
	    		
	    	}
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
			  self.deleteMarkers();
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
		  }, 500);
	  };
	  
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
      
      self.listPlaceAjax = function(fn) {
    	  placeService.listPlace()
              .then(function(operationResult) {
            	  		
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
	
	self.getHomeTypeDescription = function(place) {
		if (place.bills_include == 'Y') {
			return "Bills Included";
		} else {
			return "";
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
		if (place.currencyId == 1) {
			return place.price + " TL";
		} else if (place.currencyId == 2) {
			return place.price + " $";
		} else if (place.currencyId == 3) {
			return place.price + " €";
		}
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
	initialize();
      
  }]);