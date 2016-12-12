'use strict';

App.factory('QuestAnswService', ['$http', '$q', function($http, $q){

	return {
			listQuestAnsw : function(portfolioId, serviceId, applicationId, teamId, searchText) {
					var data = {
						portfolioId 	: portfolioId,
						serviceId 		: serviceId,
						applicationId 	: applicationId,
						teamId			: teamId,
						searchText : searchText
					};
					var config = {
							 params: data,
							 headers : {'Accept' : 'application/json'}
							};
					return $http.get('/BillingSolutionsPortal/questionanswer/list', config)
							.then(
									function(response) {
										return response.data;
									}, 
									function(errResponse){
										console.error('Error while fetching documents');
										return $q.reject(errResponse);
									}
							);
			},
	
			saveQuestAnsw : function(qa) {
				var data = {
					id				: qa.id,
					portfolioId 	: qa.portfolioId,
					serviceId 		: qa.serviceId,
					applicationId 	: qa.applicationId,
					teamId 			: qa.teamId,
					question		: qa.question,
					answer			: qa.answer
				};
				var config = {
						 params: data,
						 headers : {'Accept' : 'application/json'}
						};
				return $http.get('/BillingSolutionsPortal/questionanswer/save', config)
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
			
			deleteQuestAnsw : function(id) {
				var data = {
					id				: id
				};
				var config = {
						 params: data,
						 headers : {'Accept' : 'application/json'}
						};
				return $http.get('/BillingSolutionsPortal/questionanswer/delete', config)
						.then(
								function(response) {
									return response.data;
								}, 
								function(errResponse){
									console.error('Error while fetching QuestionAnswer');
									return $q.reject(errResponse);
								}
						);
		}
	};

}]);
