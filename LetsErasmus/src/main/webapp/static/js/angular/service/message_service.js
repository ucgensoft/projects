'use strict';

App.factory('messageService', ['$http', '$q', function($http, $q){

	return {
		listMessageThread : function(entityType, hostFlag, clientFlag, callBack) {
			var data = {
					entityType : entityType,
					hostFlag : hostFlag,
					clientFlag : clientFlag
			};
			
			var config = {
				params : data,
				headers : {
					'Accept' : 'application/json'
				}
			};
			
			NProgress.start(3000, 5);
			return $http.get(webApplicationUrlPrefix + '/api/message/listthread', config).then(function(response) {
				NProgress.done(true);
				var result = isResultSuccess(response.data, true, function() {
					if (response.data.resultValue['hostThreadList'] == null) {
						response.data.resultValue['hostThreadList'] = [];
					}
					if (response.data.resultValue['clientThreadList'] == null) {
						response.data.resultValue['clientThreadList'] = [];
					}
					callBack(response.data.resultValue);
				}, false);
			}, function(errResponse) {
				DialogUtil.error(errResponse);
			});
		},
		
		getMessageThread : function(threadId, callBack) {
			var data = {
					threadId : threadId
			};
			
			var config = {
				params : data,
				headers : {
					'Accept' : 'application/json'
				}
			};
			
			NProgress.start(3000, 5);
			return $http.get(webApplicationUrlPrefix + '/api/message/getthread', config).then(function(response) {
				NProgress.done(true);
				var result = isResultSuccess(response.data, true, function() {
					callBack(response.data.resultValue);
				}, false);
			}, function(errResponse) {
				DialogUtil.error(errResponse);
			});
		},
		
		listMessage : function(message, callBack) {
			var config = {
				headers : {
					'Accept' : 'application/json'
				}
			};
			
			NProgress.start(3000, 5);
			return $http.post(webApplicationUrlPrefix + '/api/message/list', reservation, config).then(function(response) {
				NProgress.done(true);
				var result = isResultSuccess(response.data, true, function() {
					callBack(response.data.resultValue);
				}, false);
			}, function(errResponse) {
				DialogUtil.error(errResponse);
			});
		},
		
		sendMessage : function(threadId, messageText, callBack) {
			var config = {
				headers : {
					'Accept' : 'application/json'
				}
			};
			
			var message = {
				messageThreadId : threadId,
				messageText : messageText
			};
			
			NProgress.start(3000, 5);
			return $http.post(webApplicationUrlPrefix + '/api/message/send', message, config).then(function(response) {
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
