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
				var data = {
				}
				
				var config = {
					params : data,
					headers : {
						'Accept' : 'application/json'
					}
				};
				return $http.get(webApplicationUrlPrefix + '/api/simpleobject/listcountry', config).then(function(response) {
						var result = isResultSuccess(response.data, true, function() {
							callBack(response.data.objectList);
						}, false);
					}, function(errResponse) {
						DialogUtil.error(errResponse);
					});
			},
			
			listCity : function(countryId, callBack) {
				var data = {
					countryId : countryId
				}
				
				var config = {
					params : data,
					headers : {
						'Accept' : 'application/json'
					}
				};
				return $http.get(webApplicationUrlPrefix + '/api/simpleobject/listcity', config).then(function(response) {
						var result = isResultSuccess(response.data, true, function() {
							callBack(response.data.objectList);
						}, false);
					}, function(errResponse) {
						DialogUtil.error(errResponse);
					});
			},
			
			listUniversity : function(countryId, callBack) {
				var data = {
					countryId : countryId
				}
				
				var config = {
					params : data,
					headers : {
						'Accept' : 'application/json'
					}
				};
				return $http.get(webApplicationUrlPrefix + '/api/simpleobject/listuniversity', config).then(function(response) {
						var result = isResultSuccess(response.data, true, function() {
							callBack(response.data.objectList);
						}, false);
					}, function(errResponse) {
						DialogUtil.error(errResponse);
					});
			},
			
			listQuestionGroup : function(callBack) {
	    	  NProgress.start(4000, 5);
				var data = {
				}
				
				var config = {
					params : data,
					headers : {
						'Accept' : 'application/json'
					}
				};
				return $http.get(webApplicationUrlPrefix + '/api/simpleobject/listquestiongroup', config).then(function(response) {
						NProgress.done(true);
						var result = isResultSuccess(response.data, true, function() {
							callBack(response.data.objectList);
						}, false);
					}, function(errResponse) {
						DialogUtil.error(errResponse);
					});
			},
			
			listQuestion : function(groupTitle, searchText, callBack) {
	    	  NProgress.start(4000, 5);
				var data = {
					groupTitle : groupTitle,
					searchText : searchText
				}
				
				var config = {
					params : data,
					headers : {
						'Accept' : 'application/json'
					}
				};
				return $http.get(webApplicationUrlPrefix + '/api/simpleobject/listquestion', config).then(function(response) {
						NProgress.done(true);
						var result = isResultSuccess(response.data, true, function() {
							callBack(response.data.objectList);
						}, false);
					}, function(errResponse) {
						DialogUtil.error(errResponse);
					});
			},
			
			getTokenObject : function(operationToken, operationId, callBack) {
		    	  NProgress.start(4000, 5);
					var data = {
						operationToken : operationToken,
						operationId : operationId
					}
					
					var config = {
						params : data,
						headers : {
							'Accept' : 'application/json'
						}
					};
					
					var objectType = null;
					if (operationId == EnmOperation.CREATE_RESERVATION) {
						objectType = 'reservation';
					}
					
					return $http.get(webApplicationUrlPrefix + '/api/' + objectType + '/gettokenobject', config).then(function(response) {
							NProgress.done(true);
							var result = isResultSuccess(response.data, true, function() {
								callBack(response.data.resultValue);
							}, false);
						}, function(errResponse) {
							DialogUtil.error(errResponse);
						});
				}
	};
	
}]);
