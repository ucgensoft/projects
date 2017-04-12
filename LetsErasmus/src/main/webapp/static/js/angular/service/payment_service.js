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
				var result = isResultSuccess(response.data, true);
				if (result && callBack) {
					callBack(response.data.objectList);
				}
			}, function(errResponse) {
				DialogUtil.error('Error', errResponse, 'OK');
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
				var result = isResultSuccess(response.data, true);
				if (result && callBack) {
					callBack(response.data.resultValue);
				}
			}, function(errResponse) {
				DialogUtil.error('Error', errResponse, 'OK');
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
				DialogUtil.error('Error', errResponse, 'OK');
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
				var result = isResultSuccess(response.data, true);
				if (result && callBack) {
					callBack(response.data.resultValue);
				}		
			}, function(errResponse) {
				DialogUtil.error('Error', errResponse, 'OK');
			});
		},
		
		createPayoutMethod : function(countryCode, callBack) {
			var data = {
					countryCode : countryCode
			}
			var config = {
				params : data,
				headers : {
					'Accept' : 'application/json'
				}
			};
			
			NProgress.start(3000, 5);
			return $http.get(webApplicationUrlPrefix + '/api/payout/createdraft', config).then(function(response) {
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
