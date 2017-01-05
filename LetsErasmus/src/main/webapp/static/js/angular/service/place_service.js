'use strict';

App.factory('placeService', ['$http', '$q', function($http, $q){

	return {
			listPlace : function() {
					var data = {
						
					};
					var config = {
							 params: data,
							 headers : {'Accept' : 'application/json'}
							};
					return $http.get(webApplicationUrlPrefix + '/api/place/list', config)
							.then(
									function(response) {
										return response.data;
									}, 
									function(errResponse){
										console.error('Error while fetching places');
										return $q.reject(errResponse);
									}
							);
			},
	
			createPlace : function(place) {
				var data = {
					place : place
				};
				var config = {
						 params: data,
						 headers : {'Accept' : 'application/json'}
						};
				return $http.post(webApplicationUrlPrefix + '/api/place/create', place)
						.then(
								function(response) {
									return response.data;
								}, 
								function(errResponse){
									console.error('Error while creating place');
									return $q.reject(errResponse);
								}
						);
		},
			
			deletePlace : function(place) {
				var data = {
					id				: place.id
				};
				var config = {
						 params: data,
						 headers : {'Accept' : 'application/json'}
						};
				return $http.get(webApplicationUrlPrefix + '/api/place/create', config)
						.then(
								function(response) {
									return response.data;
								}, 
								function(errResponse){
									console.error('Error while fetching QuestionAnswer');
									return $q.reject(errResponse);
								}
						);
		},
		addPhoto : function(formData) {
			var xhr = new XMLHttpRequest();
			xhr.open('POST', webApplicationUrlPrefix + '/api/place/addphoto');
			xhr.onload = function() {
				//progress.value = progress.innerHTML = 100;
			};

			/*
			if (tests.progress) {
				xhr.upload.onprogress = function(event) {
					if (event.lengthComputable) {
						var complete = (event.loaded / event.total
								* 100 | 0);
						progress.value = progress.innerHTML = complete;
					}
				}
			}
			*/
			xhr.send(formData);
		}
	};

}]);
