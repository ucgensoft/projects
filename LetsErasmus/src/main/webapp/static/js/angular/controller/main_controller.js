App.controller('mainCtrl', ['$scope', '$controller', 'userService', function($scope, $controller, userService) {
      var self = this;
      
      var selectedPlaceName = null;
      var selectedLat = null;
      var selectedLng = null;
      var selectedLocationId = null;
      var selectedLanguage = null;

      self.isSearchValid = false;

      self.initialize = function() {
    	  
    	  if (isMobile()) {
    		  var height = (typeof window.outerHeight != 'undefined')?Math.max(window.outerHeight, $(window).height()):$(window).height();
        	  $('#homeFirstScreenDiv').css('height', height);
    	  }
    	  
    	  $("#txtSearchPlace").geocomplete().bind("geocode:result", function(event, result) {
  				self.onPlaceChange(event, result);
  			});
    	  
	      	$("#txtStartDatePicker").datepicker(
  			{
  				minDate: '+0',
  				dateFormat : "dd.mm.yy",
  				onSelect : function(selectedDate, cal) {
  					self.validateSearch();
  					setTimeout(function() {
  						var minDate = $('#txtStartDatePicker').datepicker('getDate');
  			            $("#txtEndDatePicker").datepicker( "option", "minDate", minDate.addMonths(1).addDays(-1));
  						$("#txtEndDatePicker").focus()
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
      };
      
      self.onPlaceChange = function (event, result) {
    	  var selectedCountry = null;
    	  var selectedCity = null;
    	 for (var i = 0; i < result.address_components.length; i++) {
    		 if(result.address_components[i].types[0] == 'country') {
    			 selectedCountry = result.address_components[i].long_name;
    			 selectedLanguage = result.address_components[i].short_name;
    		 } else if(result.address_components[i].types[0] == 'administrative_area_level_1') {
            	selectedCity = result.address_components[i].long_name;
            }
        }
    	  
    	 if (selectedCity != null) {
    		 selectedPlaceName = selectedCity;
    	 } else {
    		 selectedPlaceName = selectedCountry;
    	 }
      	selectedLat = result.geometry.location.lat();
      	selectedLng = result.geometry.location.lng();
      	//selectedLat = result.geometry.viewport.b.b + ':' + result.geometry.viewport.b.f;
      	//selectedLng = result.geometry.viewport.f.b + ':' + result.geometry.viewport.f.f;
      	selectedLocationId = result.place_id;
      	
      	setTimeout(function() {
	      		if ($("#txtStartDatePicker").datepicker("getDate") == null) {
	      			$("#txtStartDatePicker").focus();
	      		} else if ($("#txtEndDatePicker").datepicker("getDate") == null) {
	      			$("#txtEndDatePicker").focus();
	      		}
				self.validateSearch();
			}, 100);
      };

      self.search = function() {
    	var startDate = $.datepicker.formatDate('dd.mm.yy', $("#txtStartDatePicker").datepicker("getDate"));
		var endDate = $.datepicker.formatDate('dd.mm.yy', $("#txtEndDatePicker")
					.datepicker("getDate"));
		
		openWindow(webApplicationUrlPrefix + globalSearchResultUrlTemplate.replace('{locationName}', selectedPlaceName.toLowerCaseWithLang(selectedLanguage)).replace('{locationId}', selectedLocationId) 
				+ "?" + EnmUriParam.CHECKIN_DATE + "=" + startDate + "&" + EnmUriParam.CHECKOUT_DATE + "=" + endDate, true);
      };

      self.validateSearch = function() {
      	var startDate = $.datepicker.formatDate('dd.mm.yy', $("#txtStartDatePicker")
      			.datepicker("getDate"));
      	var endDate = $.datepicker.formatDate('dd.mm.yy', $("#txtEndDatePicker")
      			.datepicker("getDate"));

      	if (selectedPlaceName == null || selectedPlaceName == ''
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

      self.initialize();
      
  }]);