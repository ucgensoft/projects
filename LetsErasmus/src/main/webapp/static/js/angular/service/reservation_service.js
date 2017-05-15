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
				var result = isResultSuccess(response.data, true, function() {
					callBack(response.data.resultValue);
				}, false);
			}, function(errResponse) {
				DialogUtil.error(errResponse);
			});
		},
		
		finishReservation : function(reservation, callBack) {
			var config = {
				headers : {
					'Accept' : 'application/json'
				}
			};
			
			NProgress.start(3000, 5, true);
			return $http.post(webApplicationUrlPrefix + '/api/reservation/finish', reservation, config).then(function(response) {
				NProgress.done(true, true);
				var result = isResultSuccess(response.data, true, function() {
					callBack(true);
				}, false);
			}, function(errResponse) {
				NProgress.done(true, true);
				DialogUtil.error(errResponse);
			});
		},
		
		createInquiry : function(reservation, callBack) {
			var config = {
				headers : {
					'Accept' : 'application/json'
				}
			};
			
			NProgress.start(3000, 5);
			return $http.post(webApplicationUrlPrefix + '/api/reservation/createinquiry', reservation, config).then(function(response) {
				NProgress.done(true);
				var result = isResultSuccess(response.data, true, function() {
					callBack();
				}, false);
			}, function(errResponse) {
				DialogUtil.error(errResponse);
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
			
			NProgress.start(3000, 5, true);
			return $http.post(webApplicationUrlPrefix + '/api/reservation/update', reservation, config).then(function(response) {
				NProgress.done(true, true);
				var result = isResultSuccess(response.data, true, function() {
					callBack(true);
				}, false);
			}, function(errResponse) {
				NProgress.done(true, true);
				DialogUtil.error(errResponse);
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
				var result = isResultSuccess(response.data, true, function() {
					callBack(response.data.resultValue);
				}, false);
			}, function(errResponse) {
				DialogUtil.error(errResponse);
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
				var result = isResultSuccess(response.data, true, function() {
					callBack(response.data.resultValue);
				}, false);
			}, function(errResponse) {
				DialogUtil.error(errResponse);
			});
		}
	}

}]);
