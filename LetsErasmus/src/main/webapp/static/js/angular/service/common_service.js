'use strict';

App.factory('CommonService', ['$http', '$q', function($http, $q){

	return {
		listPortfolio : function() {
				var data = {
				};
				var config = {
						 params: data,
						 headers : {'Accept' : 'application/json'}
						};
				return $http.get('/BillingSolutionsPortal/portfolio/list', config)
						.then(
								function(response) {
									return response.data;
								}, 
								function(errResponse){
									console.error('Error while fetching portfolio');
									return $q.reject(errResponse);
								}
						);
		},
	
		listService : function(portfolioId) {
			var data = {
				portfolioId 	: portfolioId
			};
			var config = {
					 params: data,
					 headers : {'Accept' : 'application/json'}
					};
			return $http.get('/BillingSolutionsPortal/service/list', config)
					.then(
							function(response) {
								return response.data;
							}, 
							function(errResponse){
								console.error('Error while fetching service');
								return $q.reject(errResponse);
							}
					);
		},
		
		listApplication : function(serviceId) {
			var data = {
					serviceId 	: serviceId
			};
			var config = {
					 params: data,
					 headers : {'Accept' : 'application/json'}
					};
			return $http.get('/BillingSolutionsPortal/application/list', config)
					.then(
							function(response) {
								return response.data;
							}, 
							function(errResponse){
								console.error('Error while fetching application');
								return $q.reject(errResponse);
							}
					);
		},
		
		listTeam : function() {
			var data = {
			};
			var config = {
					 params: data,
					 headers : {'Accept' : 'application/json'}
					};
			return $http.get('/BillingSolutionsPortal/team/list', config)
					.then(
							function(response) {
								return response.data;
							}, 
							function(errResponse){
								console.error('Error while fetching team');
								return $q.reject(errResponse);
							}
					);
		}
	};

}]);
