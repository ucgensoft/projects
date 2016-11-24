'use strict';

App.controller('MainController', ['$scope', 'CommentService', function($scope, CommentService) {
          var self = this;
          
          self.commentList = [];
              
          self.listComment = function() {
        	  CommentService.listComment()
                  .then(
      					       function(response) {
      						        self.commentList = response;
      					       },
            					function(errResponse){
            						console.error('Error while fetching Comments');
            					}
      			       );
          };
          
          self.listComment();
                        
      }]);
