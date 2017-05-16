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
      	selectedPlaceName = result.name;
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