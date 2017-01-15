'use strict';

App.factory('enumerationService', ['$http', '$q', function($http, $q) {
			return {
				listEnumeration : function() {
					var data = {

					};
					var config = {
						params : data,
						headers : {
							'Accept' : 'application/json'
						}
					};
					return $http
							.get(webApplicationUrlPrefix + '/api/enumeration/list', config).then(function(response) {
								return response.data;
							}, function(errResponse) {
								console.error('Error while fetching enumerations');
								return $q.reject(errResponse);
							});
				}
			};

		} ]);
