App.controller('searchResultCtrl', ['$scope', '$controller', '$http', 'placeService', 'commonService', 'enumerationService', 
                                    function($scope, $controller, $http, placeService, commonService, enumerationService) {
      var self = this;
      
      var map = null; 
      var infoWindow = null;
      self.selectedLocation = null;
      self.selectedLat = null;
      self.selectedLng = null;
      self.selectedPlaceName = null;
      self.selectedLocationId = null;
      self.locSearchCriteria = null;
      self.selectedStartDate = null;
      self.selectedEndDate = null;
      self.minPrice = 0;
      self.maxPrice = 500;
      self.currentPageIndex = 1;
      self.totalPageNumber = 0;
      
      self.amentiesList = [];
      self.safetyAmentiesList = [];
      self.ruleList = [];
      
      self.originalPlaceList = [];
      self.searchList = null;
      self.placeList = [];
      var geocoder = new google.maps.Geocoder;
      var mapBoundaries = null;

      var basicSearchFlag = false;
      
      initialize = function() {
    	  
    	  	self.selectedPlaceName = getUriParam(EnmUriParam.LOCATION);
	    	self.selectedStartDate = getUriParam(EnmUriParam.CHECKIN_DATE);
	    	self.selectedEndDate = getUriParam(EnmUriParam.CHECKOUT_DATE);
	    	self.selectedLocationId = getUriParam(EnmUriParam.LOCATION_ID);
		  
	    	var mapOptions = {
					//zoom: 14,
					//center: self.selectedLocation,
					disableDefaultUI: true,
					zoomControl: true,
					zoomControlOptions: {
			              position: google.maps.ControlPosition.TOP_LEFT
			         }
			};
	    	
	    	map = new google.maps.Map(document.getElementById('divMap'), mapOptions);
	    	
	    	geocoder.geocode({'placeId': self.selectedLocationId}, function(results, status) {
	            if (status === 'OK') {
	              if (results[0]) {
	            	var result = results[0]; 
	               
	                map.setCenter(result.geometry.location);
	                
	                self.locSearchCriteria = self.getDisplayArea(result.geometry.viewport);
	                
	                if (self.locSearchCriteria.lat1 != result.geometry.viewport.f.f 
	                		&& self.locSearchCriteria.lng1 != result.geometry.viewport.b.b) {
	                	 map.setZoom(15);
	                }
	                
	                var point1 = new google.maps.LatLng(self.locSearchCriteria.lat1, self.locSearchCriteria.lng1);
	                var point2 = new google.maps.LatLng(self.locSearchCriteria.lat2, self.locSearchCriteria.lng2);
	                
	                var bounds = new google.maps.LatLngBounds();
	                
	                bounds.extend(point1);
	                bounds.extend(point2);
	              	
	                map.fitBounds(bounds);
	                
	                self.locSearchCriteria.lat1 =  map.getBounds().getNorthEast().lat();
	                self.locSearchCriteria.lng1 = map.getBounds().getNorthEast().lng();
	                self.locSearchCriteria.lat2 =  map.getBounds().getSouthWest().lat();
	                self.locSearchCriteria.lng2 = map.getBounds().getSouthWest().lng();
	                
	                self.selectedLat = result.geometry.location.lat();
	    	    	self.selectedLng = result.geometry.location.lng();
	    	    	
	    	    	self.selectedLocation = new google.maps.LatLng(self.selectedLat, self.selectedLng);
	    	    	
	    	    	marker = new google.maps.Marker({
	    		        map: map,
	    				draggable: false,
	    		        position: self.selectedLocation
	    		    });
	    	    	
	    	    	map.controls[google.maps.ControlPosition.TOP_LEFT].push($('#divMapCheckbox')[0]);
	    	    	
	    	    	map.addListener('zoom_changed', self.mapDisplayAreaChanged);
	    	    	map.addListener('dragend', self.mapDisplayAreaChanged);
	    	    	
	    	    	$('#divMapCheckbox').removeClass('hidden-force');
	    	    	
	    	    	self.listPlace(self.displayPlaceList);
	              }
	            } else {
	              console.log('Geocoder failed due to: ' + status);
	            }
	          });
			
			infoWindow = new google.maps.InfoWindow();
			
			/*
			google.maps.event.addListener(map, 'click', function() {
		          //infoWindow.close();
		        });
			*/
			
			$("#txtSearchPlace").geocomplete().bind("geocode:result",
	      			function(event, result) {
	      				self.onPlaceChange(event, result);
	      			});
			
			$("#txtStartDatePicker").datepicker(
  			{
  				minDate : '+0',
  				dateFormat : "dd.mm.yy",
  				onSelect : function(selectedDate, cal) {
  					self.validateSearch();
  					setTimeout(function() {
  						
  						var minDate = $('#txtStartDatePicker').datepicker('getDate');
  			            $("#txtEndDatePicker").datepicker( "option", "minDate", minDate.addMonths(1).addDays(-1));
  			            $("#txtEndDatePicker").focus()
  			            self.validateSearch();
  					}, 200);
  				}
  			});
			
			$("#txtEndDatePicker").datepicker(
  			{
  				minDate: '+1m',
  				dateFormat : "dd.mm.yy",
  				onSelect : function(selectedDate, cal) {
  					self.validateSearch();
  				}
  			});

			if (self.selectedStartDate != null && self.selectedStartDate != '') {
				var tmpStartDate = Date.parse(self.selectedStartDate);
				$("#txtStartDatePicker").datepicker('setDate', tmpStartDate);
				
				 $("#txtEndDatePicker").datepicker( "option", "minDate", tmpStartDate.addMonths(1).addDays(-1)); 
				 
				 if (self.selectedEndDate != null && self.selectedEndDate != '') {
					 var tmpEndDate = Date.parse(self.selectedEndDate);
					 $("#txtEndDatePicker").datepicker('setDate', tmpEndDate);
				 }
			}
	      	
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
      
      self.mapDisplayAreaChanged = function() {
    	  if (document.getElementById('chbSearchOnMove').checked) {
    		  self.locSearchCriteria.lat1 =  map.getBounds().getNorthEast().lat();
              self.locSearchCriteria.lng1 = map.getBounds().getNorthEast().lng();
              self.locSearchCriteria.lat2 =  map.getBounds().getSouthWest().lat();
              self.locSearchCriteria.lng2 = map.getBounds().getSouthWest().lng();
        	  
        	  self.listPlace(function() {
        		  if (basicSearchFlag) {
        			  self.basicSearch();
        		  } else {
        			  self.displayPlaceList();
        		  }
        	  });
    	  }
      };
      
      self.getDisplayArea = function(viewPort) {
    	  var lat1 = viewPort.f.f;
    	  var lat2 = viewPort.f.b
    	  
    	  var lng1 = viewPort.b.b;
    	  var lng2 = viewPort.b.f;
    	      	      	  
    	  var verticalDistance = MapUtil.getDistanceLat(lat1, lat2);
    	  var horizontalDistance = MapUtil.getDistanceLng(lng1, lng2, lat1);
    	  
    	  if (verticalDistance < minMapEdgeLength) {
    		  var vertDistDiff = minMapEdgeLength - verticalDistance;
    		  var latDegreeDiff = MapUtil.meterToDegreeLat(vertDistDiff);
    		  var adjustAmount = latDegreeDiff / 2;
    		  
    		  if ((lat1 > 0 && lat2 > 0) || (lat1 < 0 && lat2 < 0)) {
        		  if (Math.abs(lat1) > Math.abs(lat2)) {
        			  lat1 = MapUtil.adjustLat(lat1, adjustAmount);
        			  lat2 = MapUtil.adjustLat(lat2, -1 * adjustAmount);
        		  } else {
        			  lat2 = MapUtil.adjustLat(lat2, adjustAmount);
        			  lat1 = MapUtil.adjustLat(lat1, -1 * adjustAmount);
        		  }
        	  } else {
        		  lat1 = MapUtil.adjustLat(lat1, adjustAmount);
        		  lat2 = MapUtil.adjustLat(lat2, adjustAmount);
        	  }
    	  }
    	  
    	  if (horizontalDistance < minMapEdgeLength) {
    		  var horizontalDistDiff = minMapEdgeLength - horizontalDistance;
    		  var lngDegreeDiff = MapUtil.meterToDegreeLng(horizontalDistDiff, lat1);
    		  var adjustAmount = lngDegreeDiff / 2;
    		  
    		  if ((lng1 > 0 && lng2 > 0) || (lng1 < 0 && lng2 < 0)) {
        		  if (Math.abs(lng1) > Math.abs(lng2)) {
        			  lng1 = MapUtil.adjustLat(lng1, adjustAmount);
        			  lng2 = MapUtil.adjustLat(lng2, -1 * adjustAmount);
        		  } else {
        			  lng2 = MapUtil.adjustLat(lng2, adjustAmount);
        			  lng1 = MapUtil.adjustLat(lng1, -1 * adjustAmount);
        		  }
        	  } else {
        		  lng1 = MapUtil.adjustLat(lng1, adjustAmount);
        		  lng2 = MapUtil.adjustLat(lng2, adjustAmount);
        	  }
    	  }
    	  
    	  /*
    	  viewPort.f.f = lat1;
    	  viewPort.f.b = lat2;
    	  
    	  viewPort.b.b = lng1;
    	  viewPort.b.f = lng2;
    	  */
    	  
    	  return {
    		  lat1 : lat1,
    		  lat2 : lat2,
    		  lng1 : lng1,
    		  lng2 : lng2
    	  };
    	  
      };
      
      self.setPriceRange = function (selectedMinPrice, selectedMaxPrice) {
    	  	self.minPrice= selectedMinPrice;
	        self.maxPrice= selectedMaxPrice;
	        $("#spanMinPrice")[0].innerText=self.minPrice.toString();
	        $("#spanMaxPrice")[0].innerText=self.maxPrice.toString();
	        self.basicSearch();
      };
      	
      self.search = function() {
    	var startDate = $.datepicker.formatDate('dd.mm.yy', $("#txtStartDatePicker").datepicker("getDate"));
    	
		var endDate = $.datepicker.formatDate('dd.mm.yy', $("#txtEndDatePicker").datepicker("getDate"));
		
		openWindow(webApplicationUrlPrefix + '/pages/SearchResult.xhtml' 
				+ '?' + EnmUriParam.LOCATION + '=' + self.selectedPlaceName
				+ "&" + EnmUriParam.CHECKIN_DATE + "=" + startDate 
				+ "&" + EnmUriParam.CHECKOUT_DATE + "=" + endDate
				+ "&" + EnmUriParam.LOCATION_ID + "=" + self.selectedLocationId, true);
		
      };
      
      self.basicSearch = function() {
    	var newPlaceList = [];
    	
    	for(var i = 0; i < self.originalPlaceList.length; i++) {
    		var place = self.originalPlaceList[i];
    		var isMatching = true;
    		if (place.price < self.minPrice || place.price > self.maxPrice) {
    			isMatching = false;
    		}
    		
    		if (isMatching) {
    			var placeTypeSelected = false;
    			var isPlaceTypeMatching = false;
	    		for(var x = 1; x < 4; x++) {
	    			if($("#iconPlaceTypeId_" + x).hasClass('IconFilter-icon--enabled')) {
	    				placeTypeSelected = true;
    					if (place.placeTypeId == x) {
    						isPlaceTypeMatching = true;
    					}
	    			}
	    		}
	    		if (placeTypeSelected && !isPlaceTypeMatching) {
	    			isMatching = false;
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
    			var guestGender = $('#chbLgbtFriendly').val();
    			if ($('#chbLgbtFriendly')[0].checked && place.lgbtFriendly != 'Y') {
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
    	
    	self.searchList = newPlaceList;
    	self.currentPageIndex = 1;
    	self.displayPlaceList();
    	self.hideExtendedSearchFields();
    	commonService.fakeAjaxCall(self.refreshMap);
    	
    	basicSearchFlag = true;
      };

      self.setCurrentPage = function(index) {
    	  if (index > 0 && index <= self.totalPageNumber) {
    		  self.currentPageIndex = index;
    		  self.displayPlaceList();
    	  }
      };
      
      self.changeCurrentPage = function(changeAmount) {
    	  var index = self.currentPageIndex + changeAmount;
    	  if (index > 0 && index <= self.totalPageNumber) {
    		  self.currentPageIndex = index;
    		  self.displayPlaceList();
    	  }
      };
      
      self.validateSearch = function() {
	      	var startDate = $.datepicker.formatDate('dd.mm.yy', $("#txtStartDatePicker").datepicker("getDate"));
	      	var endDate = $.datepicker.formatDate('dd.mm.yy', $("#txtEndDatePicker").datepicker("getDate"));

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
	    	self.selectedLocationId = result.place_id;
	    	setTimeout(function() {
	    		if ($("#txtStartDatePicker").datepicker("getDate") == null) {
	      			$("#txtStartDatePicker").focus();
	      		} else if ($("#txtEndDatePicker").datepicker("getDate") == null) {
	      			$("#txtEndDatePicker").focus();
	      		}
				self.validateSearch();
			}, 100);
	    };
	    
	  self.getOriginalPlaceList = function() {
		  return (self.searchList == null ? self.originalPlaceList : self.searchList);
	  };
        
	  self.displayPlaceList = function(callBack) {
		  //self.currentPageIndex = 1;
		  var tmpPlaceList = self.getOriginalPlaceList();
		  if (tmpPlaceList.length > searchResultPageSize) {
			  var start = (self.currentPageIndex - 1) * searchResultPageSize;
			  var end = start + searchResultPageSize;
			  
			  self.placeList = [];
			  for(var i = start; i < end; i++) {
				  self.placeList.push(tmpPlaceList[i]);
			  }
		  } else {
			  self.placeList = tmpPlaceList;
		  }
		  
		  self.totalPageNumber = Math.ceil(tmpPlaceList.length / searchResultPageSize);
		  
		  setTimeout(function() {
			  self.refreshMap();
			  if (callBack) {
				  callBack();
			  }
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
      		  var photoUrl = generatePlacePhotoUrl(place.id , currentPhoto.id, 'small');
      		  $("#imgPlaceSlider_" + place.id).attr('src', photoUrl);
    	  }
      }
	  
	  self.listPlace = function(fn) {
    	  placeService.listPlace(self.selectedStartDate, self.selectedEndDate, null, self.locSearchCriteria
    			  , 
    			  function(resultPlaceList) {
			  	  		self.originalPlaceList = resultPlaceList;
				  		self.currentPageIndex = 1;
				  		self.searchList = null;
				  		if (fn) {
				  			fn();
				  		}
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
      
     self.showExtendedSearchFields = function() {

    	  $('#divExtendedSearchFields').removeClass('hidden-force');
    	  $('#filterToolBarId_Extended').removeClass('hidden-force');

    	  $('#filterToolBarId').addClass('hidden-force');
    	  $('#searchResultsId').addClass('hidden-force');
    	  $('#divPagination').addClass('hidden-force');
	};

	self.hideExtendedSearchFields = function() {
		$('#divExtendedSearchFields').addClass('hidden-force');
  	  	$('#filterToolBarId_Extended').addClass('hidden-force');
		
  	  	$('#filterToolBarId').removeClass('hidden-force');
  	  	$('#searchResultsId').removeClass('hidden-force');
  	  	$('#divPagination').removeClass('hidden-force');
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
			return "Entire Place";
		} else if (place.placeTypeId == 2) {
			return "Private Room";
		} else if (place.placeTypeId == 3) {
			return "Shared Room";
		}
	};
	
	self.getPlaceTypeCss = function(place) {
		if (place.placeTypeId == 1) {
			return "icon-entire-place";
		} else if (place.placeTypeId == 2) {
			return "icon-private-room";
		} else if (place.placeTypeId == 3) {
			return "icon-shared-room";
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
      
      self.getCurrencySymbol = function(currencyId) {
    	  return getCurrencySymbol(currencyId);
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
    	  self.animateMarker(placeId, false);
    	  var startDate = getUriParam(EnmUriParam.CHECKIN_DATE);
    	  var endDate = getUriParam(EnmUriParam.CHECKOUT_DATE);
    	  
    	  var placeDetailUrl = webApplicationUrlPrefix + '/pages/PlaceDetail.xhtml' 
    	  	+ '?' + EnmUriParam.PLACE_ID + '=' + placeId
    	  	+ '&' + EnmUriParam.CHECKIN_DATE + '=' + startDate
    	  	+ '&' + EnmUriParam.CHECKOUT_DATE + '=' + endDate;
    	  
    	  openWindow(placeDetailUrl, false);
      };
      
      self.onFavoriteIconClicked = function(placeId) {
    	  if ($('#favouriteIcon_' + placeId).hasClass('FavouriteItem-icon--active')) {
    		  removeFavorite(EnmEntityType.PLACE, placeId, function(result) {
    			 if (result) {
    				 //$('#favouriteIcon_' + placeId).removeClass('FavouriteItem-icon--active')
    			 } 
    		  });
    	  } else {
    		  addFavorite(EnmEntityType.PLACE, placeId, function(result) {
     			 if (result) {
     				//$('#favouriteIcon_' + placeId).addClass('FavouriteItem-icon--active')
     			 } 
     		  });
    	  }
      };
      
      self.isPlaceFavorite = function(placeId) {
    	  if (userFavoriteMap && userFavoriteMap[EnmEntityType.PLACE.toString()]) {
    		  placeFavoriteMap = userFavoriteMap[EnmEntityType.PLACE.toString()];
    		  if (placeFavoriteMap[placeId.toString()]) {
				  return true;
			  }
    	  }
    	  return false;
      };
      
      self.generatePlacePhotoUrl = function(placeId, photoId, size) {
  		return generatePlacePhotoUrl(placeId, photoId, size);
  	  };
      	
      initialize();
      
  }]);