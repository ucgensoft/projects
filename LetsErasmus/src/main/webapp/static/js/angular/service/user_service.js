'use strict';

App.factory('userService', ['$http', '$q', function($http, $q){

	return {
		createUser : function(user) {
			var config = {
				headers : {
					'Accept' : 'application/json'
				}
			};
			return $http
					.post(webApplicationUrlPrefix + '/api/user/signup', user, config).then(function(response) {
						return response.data;
					}, function(errResponse) {
						console.error('Error while fetching enumerations');
						return $q.reject(errResponse);
					});
		},
		
		login : function(user) {
			var config = {
				headers : {
					'Accept' : 'application/json'
				}
			};
			return $http
					.post(webApplicationUrlPrefix + '/api/user/login', user, config).then(function(response) {
						return response.data;
					}, function(errResponse) {
						console.error('Error while fetching enumerations');
						return $q.reject(errResponse);
					});
		},
		
		logout : function() {
			var config = {
				headers : {
					'Accept' : 'application/json'
				}
			};
			return $http
					.post(webApplicationUrlPrefix + '/api/user/logout', null, config).then(function(response) {
						return response.data;
					}, function(errResponse) {
						console.error('Error while fetching enumerations');
						return $q.reject(errResponse);
					});
		}
	};

}]);
