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
				var result = isResultSuccess(response.data, true, function() {
					callBack(response.data.resultValue);
				}, false);
			}, function(errResponse) {
				DialogUtil.error(errResponse);
			});
		},
		
		listPlaceReview : function(placeId, callBack) {
			var data = {
				placeId : placeId
			};
			
			var config = {
				params : data,
				headers : {
					'Accept' : 'application/json'
				}
			};
			
			NProgress.start(3000, 5);
			return $http.get(webApplicationUrlPrefix + '/api/review/listplacereview', config).then(function(response) {
				NProgress.done(true);
				var result = isResultSuccess(response.data, true, function() {
					callBack(response.data.resultValue);
				}, false);
			}, function(errResponse) {
				DialogUtil.error(errResponse);
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
				var result = isResultSuccess(response.data, true, function() {
					callBack(true);
				}, false);
			}, function(errResponse) {
				DialogUtil.error(errResponse);
			});
		}
	}

}]);
