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
	      },
	      
	      listCountry : function(callBack) {
	    	  NProgress.start(4000, 5);
				var data = {
				}
				
				var config = {
					params : data,
					headers : {
						'Accept' : 'application/json'
					}
				};
				return $http.get(webApplicationUrlPrefix + '/api/simpleobject/listcountry', config).then(function(response) {
						NProgress.done(true);
						var result = isResultSuccess(response.data, true);
						if (callBack) {
							callBack(response.data.objectList);
						}
					}, function(errResponse) {
						DialogUtil.error('Error', errResponse, 'OK');
						if (callBack) {
							callBack(false);
						}
					});
			}
	};
	
}]);
