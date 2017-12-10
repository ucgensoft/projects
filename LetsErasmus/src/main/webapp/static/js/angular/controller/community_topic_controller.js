App.controller('communityTopicCtrl', [ '$scope', '$controller', 'communityService',
		function($scope, $controller, communityService) {
			var self = this;
			
			self.initialize = function() {
			};
						
			self.btnReplyClick = function() {
				if (loginUserId != null && loginUserId != '') {
					var replyText = StringUtil.trim($('#txtReply').val());
					if (replyText != '') {
						var callBack = function(operationResult) {
							if (isResultSuccess(operationResult)) {
								DialogUtil.success(operationResult.resultDesc, function() {
									 reloadPage()
								 });
							} else {
								$('#txtReply').focus();
							}
						};
						
						communityService.sendTopicMessage(globalCommunityTopicId, replyText, callBack);
					} else {
						DialogUtil.warn("Please type a reply text!", function() {
							$('#txtReply').focus();
						 });
					}
				} else {
					openLoginWindow();
				}
			};
					  	
			self.initialize();

		} ]);