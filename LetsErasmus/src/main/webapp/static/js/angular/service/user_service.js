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
		
		updateUser : function(user, profilePhoto) {
			var profilePhotoFile = (profilePhoto != null ? profilePhoto.file : null);
			if (profilePhotoFile == null) {
				var parts = [
		            new Blob([''], {type: 'text/plain'}), '', new Uint16Array([33])
		          ];
	
				profilePhotoFile = new File(parts, 'dummy', {});
			}
			
			var formData = new FormData();
			formData.append('user', JSON.stringify(user));
			formData.append('profilePhoto', profilePhotoFile);					
			
			var config = {
				headers : {
					'Accept' : 'application/json',
					'Content-Type' : undefined
				}
			};
			return $http.post(
					webApplicationUrlPrefix + '/api/user/update',
					formData, config).then(function(response) {
				return response.data;
			}, function(errResponse) {
				console.error('Error while updating user');
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
