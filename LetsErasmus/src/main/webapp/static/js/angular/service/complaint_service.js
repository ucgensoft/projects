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
				var result = isResultSuccess(response.data, true);
				if (result && callBack) {
					callBack(response.data.resultValue);
				}
			}, function(errResponse) {
				DialogUtil.error('Error', errResponse, 'OK');
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
				var result = isResultSuccess(response.data, true);
				if (result && callBack) {
					callBack(response.data.resultValue);
				}
			}, function(errResponse) {
				DialogUtil.error('Error', errResponse, 'OK');
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
				var result = isResultSuccess(response.data, true);
				if (result && callBack) {
					callBack(result);
				}
			}, function(errResponse) {
				DialogUtil.error('Error', errResponse, 'OK');
			});
		}
	}

}]);
