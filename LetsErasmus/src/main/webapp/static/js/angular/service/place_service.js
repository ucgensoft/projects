'use strict';

App.factory('placeService', ['$http', '$q', function($http, $q) {
			return {
				getPlace : function(placeId) {
					var data = {
						placeId : placeId
					};
					var config = {
						params : data,
						headers : {
							'Accept' : 'application/json'
						}
					};
					return $http.get(webApplicationUrlPrefix + '/api/place/get', config).then(function(response) {
								return response.data;
							}, function(errResponse) {
								console.error('Error while getting places');
								return $q.reject(errResponse);
							});
				},
				listPlace : function() {
					var data = {

					};
					var config = {
						params : data,
						headers : {
							'Accept' : 'application/json'
						}
					};
					return $http.get(webApplicationUrlPrefix + '/api/place/list',
									config).then(function(response) {
								return response.data;
							}, function(errResponse) {
								console.error('Error while fetching places');
								return $q.reject(errResponse);
							});
				},
				listUserPlaces : function() {
					var data = {
							
					};
					var config = {
						params : data,
						headers : {
							'Accept' : 'application/json'
						}
					};
					return $http.get(webApplicationUrlPrefix + '/api/place/listuserplace', config).then(
							function(response) {
								return response.data;
							}, function(errResponse) {
								console.error('Error while fetching places');
								return $q.reject(errResponse);
							});
				},

				savePlace : function(place, photoList) {
					var formData = new FormData();
					formData.append('place', angular.toJson(place));
					for (var i = 0; i < photoList.length; i++) {
						formData.append('photoList', photoList[i].file);
					}					
					
					var config = {
						headers : {
							'Accept' : 'application/json',
							'Content-Type' : undefined
						}
					};
					return $http.post(
							webApplicationUrlPrefix + '/api/place/create',
							formData, config).then(function(response) {
						return response.data;
					}, function(errResponse) {
						console.error('Error while creating place');
						return $q.reject(errResponse);
					});
				},

				listPhoto : function(placeId) {
					var data = {
						placeId : placeId
					};
					var config = {
						params : data,
						headers : {
							'Accept' : 'application/json'
						}
					};
					return $http
							.get(webApplicationUrlPrefix + '/api/place/listphoto',
									config).then(function(response) {
								return response.data;
							}, function(errResponse) {
								console.error('Error while fetching places');
								return $q.reject(errResponse);
							});
				},
				
				deletePlace : function(place) {
					var data = {
						id : place.id
					};
					var config = {
						params : data,
						headers : {
							'Accept' : 'application/json'
						}
					};
					return $http.get(
							webApplicationUrlPrefix + '/api/place/create',
							config).then(function(response) {
						return response.data;
					}, function(errResponse) {
						console.error('Error while fetching QuestionAnswer');
						return $q.reject(errResponse);
					});
				},
				
				changePlaceStatus : function(placeId, status) {
					
					var config = {
						headers : {
							'Accept' : 'application/json',
						}
					};
					
					var data = {
						id : placeId,
						status : status
					};
					
					return $http.post(webApplicationUrlPrefix + '/api/place/updateplacestatus', data, config).then(function(response) {
								return response.data;
							}, function(errResponse) {
								console.error('Error while updating place status');
								return $q.reject(errResponse);
							});
				}
				
			};

		} ]);
