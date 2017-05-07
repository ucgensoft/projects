App.controller('wishListCtrl', ['$scope', '$controller', 'favoriteService',
                                function($scope, $controller, favoriteService) {
      var self = this;
      self.favoriteMap = {};
      self.placeFavoriteList = [];
      
      self.initialize = function() {
    	  favoriteService.listFavorite(
    			  function(userFavoriteMap) {
    				  self.favoriteMap = userFavoriteMap;
    				  if (userFavoriteMap && userFavoriteMap[EnmEntityType.PLACE.toString()]) {
    					  var placeFavoriteMap = userFavoriteMap[EnmEntityType.PLACE.toString()];
    					  self.placeFavoriteList = [];
    					  for (placeId in placeFavoriteMap) {
    						  self.placeFavoriteList.push(placeFavoriteMap[placeId]);
      					  }
    				  }
    	  		  }
    	  	  );
	 };
  	
  	self.getCurrencySymbol = function(currencyId) {
  		return getCurrencySymbol(currencyId);
  	};
  	
  	self.generateUserProfilePhotoUrl = function(userId, photoId, size) {
  		return generateUserProfilePhotoUrl(userId, photoId, size);
  	};
  	
  	self.generatePlacePhotoUrl = function(placeId, photoId, size) {
  		return generatePlacePhotoUrl(placeId, photoId, size);
  	};
  	
  	self.removeFavorite = function(entityType, placeId) {
  		removeFavorite(entityType, placeId, function(result) {
 			 if (result) {
 				 reloadPage();
 			 } 
 		  });
    };
  	
    self.initialize();
      
  }]);