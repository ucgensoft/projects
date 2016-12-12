/*
function createQuestAnswer() {
	return {
			id 				: null,
			portfolioId 	: null,
			serviceId 		: null,
			applicationId 	: null,
			teamId 			: null,
			question 		: null,
			answer 			: null
		  }; 
}
*/

App.controller('searchResultCtrl', ['$scope', '$controller', 'placeService', 'commonService', function($scope, $controller, placeService, commonService) {
      var self = this;
      
      self.placeList = [];

	  self.setPlaceList = function(list) {
		  self.placeList = list;
	  }
	  
	  self.listPlace = function(fn) {
    	  placeService.listPlace()
              .then(fn,
        					function(errResponse){
        						console.error('Error while fetching Portfolio');
        					}
  			       );
      };
                 
      self.listPlaces(setPlaceList);
      
  }]);