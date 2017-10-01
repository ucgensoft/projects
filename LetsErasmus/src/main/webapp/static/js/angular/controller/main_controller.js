App.controller('mainCtrl', ['$scope', '$controller', 'userService', function($scope, $controller, userService) {
      var self = this;
      
      var selectedCountry = null;
      var selectedPlaceName = null;
      var selectedPlaceId = null;
      var selectedLat = null;
      var selectedLng = null;
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
    	  selectedCountry = null;
    	  selectedPlaceName = null;
    	  
    	  for (var i = 0; i < result.address_components.length; i++) {
      		 if(result.address_components[i].types[0] == 'country') {
      			 selectedCountry = result.address_components[i].long_name;
      			 selectedLanguage = result.address_components[i].short_name;
      		 } else if(result.address_components[i].types[0] == 'administrative_area_level_1') {
              	//selectedCity = result.address_components[i].long_name;
              }
            }

     	selectedPlaceName = result.name;
    	  
      	selectedLat = result.geometry.location.lat();
      	selectedLng = result.geometry.location.lng();
      	//selectedLat = result.geometry.viewport.b.b + ':' + result.geometry.viewport.b.f;
      	//selectedLng = result.geometry.viewport.f.b + ':' + result.geometry.viewport.f.f;
      	selectedPlaceId = result.place_id;
      	
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
		
		var locationName = selectedCountry.toLowerCaseWithLang(selectedLanguage);
		
		if (selectedPlaceName != null) {
			locationName = locationName + '--' + selectedPlaceName.replaceAll(' ', '-').replaceAll(',', '-').replaceAll('/', '-').replaceAll('\\.', '');
		}
		
		var url = globalSearchResultUrlTemplate.replace('{locationName}', locationName).replace('{locationId}', selectedPlaceId);
		
		url = WebUtil.addParameter(url, [{name: EnmUriParam.CHECKIN_DATE, value: startDate}, {name: EnmUriParam.CHECKOUT_DATE, value: endDate}])
		openWindow(url, true);
      };

      self.validateSearch = function() {
      	var startDate = $.datepicker.formatDate('dd.mm.yy', $("#txtStartDatePicker")
      			.datepicker("getDate"));
      	var endDate = $.datepicker.formatDate('dd.mm.yy', $("#txtEndDatePicker")
      			.datepicker("getDate"));
      	
      	/*
      	if (selectedPlaceName == null || selectedPlaceName == ''
      			|| startDate == null || startDate == '' || endDate == null
      			|| endDate == '') {
      	*/
      	if (selectedPlaceName == null || selectedPlaceName == '') {
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