'use strict';

App.factory('reviewService', ['$http', '$q', function($http, $q){

	return {
		listUserReview : function(userId, callBack) {
			var data = {
					userId : userId
			};
			
			var config = {
				params : data,
				headers : {
					'Accept' : 'application/json'
				}
			};
			
			NProgress.start(3000, 5);
			return $http.get(webApplicationUrlPrefix + '/api/review/listuserreview', config).then(function(response) {
				NProgress.done(true);
				var result = isResultSuccess(response.data, true);
				if (result && callBack) {
					callBack(response.data.resultValue);
				}
			}, function(errResponse) {
				DialogUtil.error('Error', errResponse, 'OK');
			});
		},
		
		createReview : function(review, callBack) {
			var config = {
				headers : {
					'Accept' : 'application/json'
				}
			};
			
			NProgress.start(3000, 5);
			return $http.post(webApplicationUrlPrefix + '/api/review/createreview', review, config).then(function(response) {
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
