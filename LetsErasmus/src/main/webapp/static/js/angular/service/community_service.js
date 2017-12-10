'use strict';

App.factory('communityService', ['$http', '$q', function($http, $q){

	return {
		
		createUpdateTopic : function(id, communityGroupId, title, description, callBack) {
			var config = {
				headers : {
					'Accept' : 'application/json'
				}
			};
			
			var communityTopic = {
				id : id,
				communityGroupId : communityGroupId,
				title : title,
				description : description
			};
			
			var methodName = null;
			
			if (id != null && id != '') {
				methodName = 'update';
			} else {
				methodName = 'create';
			}
			
			NProgress.start(3000, 5);
			return $http.post(webApplicationUrlPrefix + '/api/community/topic/' + methodName, communityTopic, config).then(function(response) {
				NProgress.done(true);
				var result = isResultSuccess(response.data, true, function() {
					callBack(response.data);
				}, true);
			}, function(errResponse) {
				DialogUtil.error(errResponse);
			});
		},
		
		listCommunityGroup : function(countryId, callBack) {
			var data = {
					countryId : countryId
			};
			
			var config = {
				params : data,
				headers : {
					'Accept' : 'application/json'
				}
			};
			
			NProgress.start(3000, 5);
			return $http.get(webApplicationUrlPrefix + '/api/community/group/list', config).then(function(response) {
				NProgress.done(true);
				var result = isResultSuccess(response.data, true, function() {
					callBack(response.data.resultValue);
				}, false);
			}, function(errResponse) {
				DialogUtil.error(errResponse);
			});
		},
		
		listTopicMessage : function(message, callBack) {
			var config = {
				headers : {
					'Accept' : 'application/json'
				}
			};
			
			NProgress.start(3000, 5);
			return $http.post(webApplicationUrlPrefix + '/api/community/listtopicmessage', reservation, config).then(function(response) {
				NProgress.done(true);
				var result = isResultSuccess(response.data, true, function() {
					callBack(response.data.resultValue);
				}, false);
			}, function(errResponse) {
				DialogUtil.error(errResponse);
			});
		},
		
		sendTopicMessage : function(communityTopicId, description, callBack) {
			var config = {
				headers : {
					'Accept' : 'application/json'
				}
			};
			
			var message = {
				communityTopicId : communityTopicId,
				description : description
			};
			
			NProgress.start(3000, 5);
			return $http.post(webApplicationUrlPrefix + '/api/community/message/create', message, config).then(function(response) {
				NProgress.done(true);
				var result = isResultSuccess(response.data, true, function() {
					callBack(response.data);
				}, false);
			}, function(errResponse) {
				DialogUtil.error(errResponse);
			});
		}
	}

}]);
