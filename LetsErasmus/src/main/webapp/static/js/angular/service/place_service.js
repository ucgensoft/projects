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
				
				listPlace : function(startDate, endDate, pageNumber, locSearchCriteria, callBack) {
					var data = {
						pageNumber : pageNumber,
						startDate : startDate,
						endDate : endDate,
						lat1 : locSearchCriteria.lat1,
						lat2 : locSearchCriteria.lat2,
						lng1 : locSearchCriteria.lng1,
						lng2 : locSearchCriteria.lng2
					};
					var config = {
						params : data,
						headers : {
							'Accept' : 'application/json'
						}
					};
					
					NProgress.start(3000, 5);
					return $http.get(webApplicationUrlPrefix + '/api/place/list', config).then(function(response) {
						NProgress.done(true);
						var result = isResultSuccess(response.data, true);
						if (result && callBack) {
							callBack(response.data.objectList);
						}
					}, function(errResponse) {
						DialogUtil.error('Error', errResponse, 'OK');
					});
					
				},
				
				listUserPlaces : function(callBack) {
					var data = {
							
					};
					var config = {
						params : data,
						headers : {
							'Accept' : 'application/json'
						}
					};
					NProgress.start(3000, 5);
					return $http.get(webApplicationUrlPrefix + '/api/place/listuserplace', config).then(
						function(response) {
							NProgress.done(true);
							var result = isResultSuccess(response.data, true);
							if (result && callBack) {
								callBack(response.data);
							}
						}, function(errResponse) {
							DialogUtil.error('Error', errResponse, 'OK');
						});
				},

				savePlace : function(place, photoList, callBack) {
					NProgress.start(4000, 10);
					this.savePhoto(photoList).then(
    					function(operationResult) {
    						if (isResultSuccess(operationResult, true)) {
    							
    							var config = {
    								headers : {
    									'Accept' : 'application/json'
    								}
    							};
    							
    							$http.post(webApplicationUrlPrefix + '/api/place/create', place, config).then(
    								function(response) {
	    								NProgress.done(true);
	    								var isSuccess = false;
	    								if (isResultSuccess(operationResult, true)) {
	    									isSuccess = true;
	    								}
	    								if (callBack) {
	    									callBack(isSuccess);
	    								}
	    							}, function(errResponse) {
	    								NProgress.done(true);
	    								console.error('Error while creating place');
	    								if (callBack) {
	    									callBack(false);
	    								}
	    							}
	    						);
    						} else {
    							NProgress.done(true);
    							if (callBack) {
									callBack(false);
								}
    						}
    					}, function(errResponse) {
    						NProgress.done(true);
    						alert('Operation could not be completed. Please try again later!');
    						if (callBack) {
								callBack(false);
							}
    					});
				},
				
				savePhoto : function(photoList) {
					var formData = new FormData();
					for (var i = 0; i < photoList.length; i++) {
						var placePhoto = photoList[i];
						if (!placePhoto.file) {
							var parts = [
					            new Blob([''], {type: 'text/plain'}), '', new Uint16Array([33])
					          ];
				
							placePhoto.file = new File(parts, 'dummy_' + placePhoto.photoId, {});
						}
						
						formData.append('photoList', placePhoto.file);
					}					
					
					var config = {
						headers : {
							'Accept' : 'application/json',
							'Content-Type' : undefined
						}
					};
					return $http.post(webApplicationUrlPrefix + '/api/place/savephoto', formData, config).then(function(response) {
						return response.data;
					}, function(errResponse) {
						console.error('Error while creating place');
						return $q.reject(errResponse);
					});
				},
				
				updatePlace : function(place, photoList, callBack) {
					NProgress.start(4000, 10);
					this.savePhoto(photoList).then(
    					function(operationResult) {
    						if (isResultSuccess(operationResult, true)) {
    							
    							var config = {
    								headers : {
    									'Accept' : 'application/json'
    								}
    							};
    							
    							$http.post(webApplicationUrlPrefix + '/api/place/update', place, config).then(
    								function(response) {
    									NProgress.done(true);
	    								var isSuccess = false;
	    								if (isResultSuccess(response.data, true)) {
	    									isSuccess = true;
	    								}
	    								if (callBack) {
	    									callBack(isSuccess);
	    								}
	    							}, function(errResponse) {
	    								NProgress.done(true);
	    								console.error('Error while creating place');
	    								if (callBack) {
	    									callBack(false);
	    								}
	    							}
	    						);
    						} else {
    							NProgress.done(true);
    							if (callBack) {
									callBack(false);
								}
    						}
    					}, function(errResponse) {
    						NProgress.done(true);
    						alert('Operation could not be completed. Please try again later!');
    						if (callBack) {
								callBack(false);
							}
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
