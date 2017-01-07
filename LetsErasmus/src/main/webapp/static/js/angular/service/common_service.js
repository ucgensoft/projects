'use strict';

App.factory('commonService', ['$http', '$q', function($http, $q){

	return {
		fakeAjaxCall : function(fnc) {
	    	  $http.get(webApplicationUrlPrefix + '/api/fakerequest').then(function(response) {
	    		  if (fnc) {
	    			 fnc();
	    		  }
	            }, function(response) {
	            	if (fnc) {
		    			 fnc();
		    		  }
	            });
	      }
	};
	

}]);
