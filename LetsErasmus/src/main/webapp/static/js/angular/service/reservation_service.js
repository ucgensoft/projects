'use strict';

App.factory('reservationService', ['$http', '$q', function($http, $q){

	return {
		
		startReservation : function(reservation, callBack) {
			var config = {
				headers : {
					'Accept' : 'application/json'
				}
			};
			
			NProgress.start(3000, 5);
			return $http.post(webApplicationUrlPrefix + '/api/reservation/start', reservation, config).then(function(response) {
				NProgress.done(true);
				var result = isResultSuccess(response.data, true);
				if (callBack) {
					callBack(response.data.resultValue);
				}
			}, function(errResponse) {
				DialogUtil.error('Error', errResponse, 'OK');
				if (callBack) {
					callBack(false);
				}
			});
		},
		
		finishReservation : function(reservation, callBack) {
			var config = {
				headers : {
					'Accept' : 'application/json'
				}
			};
			
			NProgress.start(3000, 5);
			return $http.post(webApplicationUrlPrefix + '/api/reservation/finish', reservation, config).then(function(response) {
				NProgress.done(true);
				var result = isResultSuccess(response.data, true);
				if (callBack) {
					callBack(result);
				}
			}, function(errResponse) {
				DialogUtil.error('Error', errResponse, 'OK');
				if (callBack) {
					callBack(false);
				}
			});
		},
		
		updateReservation : function(reservationId, messageText, status, callBack) {
			var config = {
				headers : {
					'Accept' : 'application/json'
				}
			};
			
			var reservation = {
				id : reservationId,
				messageText : messageText,
				status : status
			};
			
			NProgress.start(3000, 5);
			return $http.post(webApplicationUrlPrefix + '/api/reservation/update', reservation, config).then(function(response) {
				NProgress.done(true);
				var result = isResultSuccess(response.data, true);
				if (callBack) {
					callBack(result);
				}
			}, function(errResponse) {
				DialogUtil.error('Error', errResponse, 'OK');
				if (callBack) {
					callBack(false);
				}
			});
		},
		
		listReservation : function(callBack) {
			var data = {
					
			};
			
			var config = {
				params : data,
				headers : {
					'Accept' : 'application/json'
				}
			};
			
			NProgress.start(3000, 5);
			return $http.get(webApplicationUrlPrefix + '/api/reservation/list', config).then(function(response) {
				NProgress.done(true);
				var result = isResultSuccess(response.data, true);
				if (result && callBack) {
					callBack(response.data.resultValue);
				}
			}, function(errResponse) {
				DialogUtil.error('Error', errResponse, 'OK');
			});
		},
		
		listTrips : function(callBack) {
			var data = {
					
			};
			
			var config = {
				params : data,
				headers : {
					'Accept' : 'application/json'
				}
			};
			
			NProgress.start(3000, 5);
			return $http.get(webApplicationUrlPrefix + '/api/reservation/listtrips', config).then(function(response) {
				NProgress.done(true);
				var result = isResultSuccess(response.data, true);
				if (result && callBack) {
					callBack(response.data.resultValue);
				}
			}, function(errResponse) {
				DialogUtil.error('Error', errResponse, 'OK');
			});
		}
	}

}]);
