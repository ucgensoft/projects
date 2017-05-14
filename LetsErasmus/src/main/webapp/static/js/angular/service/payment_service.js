'use strict';

App.factory('paymentService', ['$http', '$q', function($http, $q){

	return {
		listPaymentMethod : function(callBack) {
			var config = {
				headers : {
					'Accept' : 'application/json'
				}
			};
			
			NProgress.start(3000, 5);
			return $http.get(webApplicationUrlPrefix + '/api/paymentmethod/list', config).then(function(response) {
				NProgress.done(true);
				var result = isResultSuccess(response.data, true, function() {
					callBack(response.data.objectList);
				}, false);
			}, function(errResponse) {
				DialogUtil.error(errResponse);
			});
		},
		
		createPaymentMethod : function(paymentMethod, callBack) {
			var config = {
				headers : {
					'Accept' : 'application/json'
				}
			};
			
			NProgress.start(3000, 5);
			return $http.post(webApplicationUrlPrefix + '/api/paymentmethod/create', paymentMethod, config).then(function(response) {
				NProgress.done(true);
				var result = isResultSuccess(response.data, true, function() {
					callBack(response.data.resultValue);
				}, false);
			}, function(errResponse) {
				DialogUtil.error(errResponse);
			});
		},
		
		getPaymentToken : function(operationToken, operationId, callBack) {
			var data = {
				operationToken : operationToken,
				operationId : operationId
			}
			
			var config = {
				params : data,
				headers : {
					'Accept' : 'application/json'
				}
			};
			
			NProgress.start(3000, 5);
			return $http.get(webApplicationUrlPrefix + '/api/payment/gettoken', config).then(function(response) {
				NProgress.done(true);
				var operationResult = response.data;
				if (operationResult.resultCode == EnmOperationResultCode.SUCCESS) {
					callBack(true, operationResult.resultValue);
				} else {
					callBack(false, null);
				}				
			}, function(errResponse) {
				DialogUtil.error(errResponse);
			});
		},
		
		hasPayoutMethod : function(callBack) {
			var data = {
			}
			var config = {
				params : data,
				headers : {
					'Accept' : 'application/json'
				}
			};
			
			NProgress.start(3000, 5);
			return $http.get(webApplicationUrlPrefix + '/api/payout/haspayout', config).then(function(response) {
				NProgress.done(true);
				var result = isResultSuccess(response.data, true, function() {
					callBack(response.data.resultValue);
				}, false);		
			}, function(errResponse) {
				DialogUtil.error(errResponse);
			});
		},
		
		createPayoutMethod : function(payoutMethod, callBack) {
			var config = {
				headers : {
					'Accept' : 'application/json'
				}
			};
			
			NProgress.start(3000, 5, true);
			return $http.post(webApplicationUrlPrefix + '/api/payout/createdraft', payoutMethod, config).then(function(response) {
				NProgress.done(true, true);
				var result = isResultSuccess(response.data, true, function() {
					callBack();
				}, false);	
			}, function(errResponse) {
				DialogUtil.error(errResponse);
			});
		},
		
		updatePayoutMethod : function(payoutMethod, callBack) {
			var config = {
				headers : {
					'Accept' : 'application/json'
				}
			};
			
			NProgress.start(3000, 5);
			return $http.post(webApplicationUrlPrefix + '/api/payout/update', payoutMethod, config).then(function(response) {
				NProgress.done(true);
				var result = isResultSuccess(response.data, true, function() {
					callBack(true);
				}, false);	
			}, function(errResponse) {
				DialogUtil.error(errResponse);
			});
		},
		
		getPayoutMethod : function(callBack) {
			var config = {
				headers : {
					'Accept' : 'application/json'
				}
			};
			
			NProgress.start(3000, 5);
			return $http.get(webApplicationUrlPrefix + '/api/payout/get', config).then(function(response) {
				NProgress.done(true);
				var result = isResultSuccess(response.data, true, function() {
					callBack(response.data.resultValue);
				}, false);	
			}, function(errResponse) {
				DialogUtil.error(errResponse);
			});
		},
	}

}]);
