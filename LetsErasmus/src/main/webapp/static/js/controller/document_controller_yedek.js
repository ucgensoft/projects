'use strict';

App.controller('DocumentController', ['$scope', 'DocumentService', function($scope, DocumentService) {
          var self = this;
          self.portfolios = [
                             {id:1, name:'SOL'},
                             {id:2, name:'TURKCELL'}
                             ];
          self.services = [
                             {id:1, name:'TESLA'},
                             {id:2, name:'TBAS'},
                             {id:3, name:'BSCS'}
                             ];
         
          self.selectedDocument={id:-1, name:'', htmlContent:'<h3>Sol men�den dok�man se�iniz</h3>', url:''};
          self.selectedPortfolio = 1;
          self.selectedService = 1;
          self.documents=[];
          self.dutyList=[];
          self.searchText = '';
              
          self.listDocuments = function() {
        	  DocumentService.listDocuments(self.searchText)
                  .then(
      					       function(response) {
      						        self.documents = response;
      					       },
            					function(errResponse){
            						console.error('Error while fetching Currencies');
            					}
      			       );
          };
           
          self.getDocument = function(id){
        	  DocumentService.getDocument(id)
		              .then(
		            		  function(response){
		            			  self.selectedDocument = response;
				              },
				              function(errResponse){
					               console.error('Error while creating User.');
				              }	
                  );
          };
          
          self.reset = function(id){
        	  self.searchText = '';
        	  self.listDocuments();
          };

          self.openWindow = function (url) {
        	  if (url != null && url != "") {
        		  window.open(url, '_blank');
        	  } else {
        		  alert("Dok�man sistemde bulunamad�! Haz�land�tan sonra sisteme aktar�lacakt�r.");
        	  }
          };
          
          self.listSupprtDuty = function() {
        	  DocumentService.listSupportDuty()
                  .then(
      					       function(response) {
      						        self.dutyList = response;
      					       },
            					function(errResponse){
            						console.error('Error while fetching Duty List');
            					}
      			       );
          };
          
          self.listSupprtDuty();
          self.listDocuments();
                        
      }]);
