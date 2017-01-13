App.controller('mainCtrl', ['$scope', '$controller', function($scope, $controller) {
      var self = this;
      
      var selectedPlaceName = null;
      var selectedLat = null;
      var selectedLng = null;

      self.isSearchValid = false;

      self.initialize = function() {
    	  $(function() {
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

    	      	$("#txtEndDatePicker").datepicker(
    	      			{
    	      				minDate : new Date(),
    	      				dateFormat : "d MM, y",
    	      				onSelect : function(selectedDate, cal) {
    	      					self.validateSearch();
    	      				}
    	      			});
    	      });
      };
      
      self.onPlaceChange = function (event, result) {
      	selectedPlaceName = result.name;
      	selectedLat = result.geometry.location.lat();
      	selectedLng = result.geometry.location.lng();
      	setTimeout(function() {
				$("#txtStartDatePicker").focus()
			}, 100);
      };

      self.search = function() {
    	var startDate = $.datepicker.formatDate('d.m.yy', $(
			"#txtStartDatePicker").datepicker("getDate"));
		var endDate = $.datepicker.formatDate('d.m.yy', $("#txtEndDatePicker")
					.datepicker("getDate"));
		
		openWindow(webApplicationUrlPrefix + '/pages/SearchResult.xhtml?place='
					+ selectedPlaceName + "&lat=" + selectedLat + "&lng="
					+ selectedLng + "&startDate=" + startDate + "&endDate="
					+ endDate, true);
      };

      self.validateSearch = function() {
      	var startDate = $.datepicker.formatDate('d.m.yy', $("#txtStartDatePicker")
      			.datepicker("getDate"));
      	var endDate = $.datepicker.formatDate('d.m.yy', $("#txtEndDatePicker")
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