App.controller('listingsCtrl', ['$scope', '$controller', 'placeService', 'commonService', 
                                   function($scope, $controller, placeService, commonService) {
      var self = this;
      self.ACTIVE = EnmPlaceStatus.ACTIVE;
      self.DEACTIVE = EnmPlaceStatus.DEACTIVE;
      self.DELETED = EnmPlaceStatus.DELETED;
      
      self.activePlaceList = [];
      self.deactivePlaceList = [];
                  
      self.initialize = function() {
    	  placeService.listUserPlaces(function(operationResult) {
    		  		if (operationResult[OperationResult.resultCode] == EnmOperationResultCode.SUCCESS) {
    		  			self.activePlaceList = operationResult.resultValue.active;
    		  			self.deactivePlaceList = operationResult.resultValue.deactive;
    		  		}
          		}
          );
      };
      
      self.updatePlaceStatus = function(placeId, status) {
    	  var message = null;
    	  if (status == EnmPlaceStatus.DEACTIVE) {
    		  message = 'Do you really want to deactivate this listing?';
    	  } else if (status == EnmPlaceStatus.ACTIVE) {
    		  message = 'Do you really want to activate this listing?';
    	  } else if (status == EnmPlaceStatus.DELETED) {
    		  message = 'Do you really want to delete this listing?';
    	  }
    	  
    	  var callBack = function() {
	    	  placeService.changePlaceStatus(placeId, status).then(function(operationResult) {
			  		if (operationResult[OperationResult.resultCode] == EnmOperationResultCode.SUCCESS) {
				  		  var resultMessage = null;
				      	  if (status == EnmPlaceStatus.DEACTIVE) {
				      		resultMessage = 'Your place definition is deactivated successfully!';
				      	  } else if (status == EnmPlaceStatus.ACTIVE) {
				      		resultMessage = 'Your place definition is activated successfully!';
				      	  } else if (status == EnmPlaceStatus.DELETED) {
				      		resultMessage = 'Your place definition is deleted successfully!';
				      	  }
				      	  DialogUtil.success(resultMessage, function() {
				      		self.initialize();
				      		  //reloadPage();
				      	  });
			  		} else {
			  			 DialogUtil.error(resultMessage);
			  		}
	    		},
				function(errResponse){
					console.error('Error in calling server');
				}
	    	  );
  		};
    	DialogUtil.confirm(message, callBack);
      };
      
      self.generatePlacePhotoUrl = function(placeId, photoId, size) {
    		return generatePlacePhotoUrl(placeId, photoId, size);
    	};
      	
      self.initialize();
      
  }]);