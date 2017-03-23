App.controller('mainCtrl', ['$scope', '$controller', 'userService', function($scope, $controller, userService) {
      var self = this;
      
      var selectedPlaceName = null;
      var selectedLat = null;
      var selectedLng = null;
      var selectedLocationId = null;

      self.isSearchValid = false;

      self.initialize = function() {
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
      	selectedPlaceName = result.name;
      	selectedLat = result.geometry.location.lat();
      	selectedLng = result.geometry.location.lng();
      	//selectedLat = result.geometry.viewport.b.b + ':' + result.geometry.viewport.b.f;
      	//selectedLng = result.geometry.viewport.f.b + ':' + result.geometry.viewport.f.f;
      	selectedLocationId = result.place_id;
      	
      	setTimeout(function() {
				$("#txtStartDatePicker").focus()
			}, 100);
      };

      self.search = function() {
    	var startDate = $.datepicker.formatDate('d.m.yy', $(
			"#txtStartDatePicker").datepicker("getDate"));
		var endDate = $.datepicker.formatDate('d.m.yy', $("#txtEndDatePicker")
					.datepicker("getDate"));
		
		openWindow(webApplicationUrlPrefix + '/pages/SearchResult.xhtml' 
				+ '?' + EnmUriParam.LOCATION + '=' + selectedPlaceName 
				+ "&" + EnmUriParam.CHECKIN_DATE + "=" + startDate 
				+ "&" + EnmUriParam.CHECKOUT_DATE + "=" + endDate
				+ "&" + EnmUriParam.LOCATION_ID + "=" + selectedLocationId, true);
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