'use strict';

App.factory('CommentService', ['$http', '$q', function($http, $q){

	return {
		listComment : function() {
				var data = {
				};
				var config = {
						 params: data,
						 headers : {'Accept' : 'application/json'}
						};
				return $http.get('/LetsErasmus/comment/list', config)
						.then(
								function(response) {
									return response.data;
								}, 
								function(errResponse){
									console.error('Error while fetching comment');
									return $q.reject(errResponse);
								}
						);
		}
	};

}]);
