'use strict';

App.factory('complaintService', ['$http', '$q', function($http, $q){

	return {
		listComplaint : function(entityType, callBack) {
			var config = {
				params : {entityType : entityType},
				headers : {
					'Accept' : 'application/json'
				}
			};
			
			return $http.get(webApplicationUrlPrefix + '/api/complaint/list', config).then(function(response) {
				var result = isResultSuccess(response.data, true, function() {
					callBack(response.data.resultValue);
				}, false);
			}, function(errResponse) {
				DialogUtil.error(errResponse);
			});
		},
		
		createComplaint : function(complaint, callBack) {
			var config = {
				headers : {
					'Accept' : 'application/json'
				}
			};
			
			NProgress.start(3000, 5);
			return $http.post(webApplicationUrlPrefix + '/api/complaint/create', complaint, config).then(function(response) {
				NProgress.done(true);
				var result = isResultSuccess(response.data, true, function() {
					callBack(response.data.resultValue);
				}, false);
			}, function(errResponse) {
				DialogUtil.error(errResponse);
			});
		},
		
		deleteComplaint : function(complaint, callBack) {
			var config = {
				headers : {
					'Accept' : 'application/json'
				}
			};
			
			NProgress.start(3000, 5);
			return $http.post(webApplicationUrlPrefix + '/api/complaint/delete', complaint, config).then(function(response) {
				NProgress.done(true);
				var result = isResultSuccess(response.data, true, function() {
					callBack(response.data.resultValue);
				}, false);
			}, function(errResponse) {
				DialogUtil.error(errResponse);
			});
		}
	}

}]);
