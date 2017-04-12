App.controller('paymentMethodCtrl', ['$scope', '$controller', 'paymentService', function($scope, $controller, paymentService) {
      var self = this;
      self.paymentMethodList = [];
      
      self.initialize = function() {
    	  paymentService.listPaymentMethod(function(userPaymentMethodList) {
    		  if (userPaymentMethodList) {
    			  self.paymentMethodList = userPaymentMethodList;
    		  }
    	  });
	 };
	 
	 self.onNewPaymentBtnClick = function() {
		 $('#divNewPayment').removeClass('hidden-force');
		 $('#sectionPaymentMethodList').addClass('hidden-force');
	 };
	 
	 self.onCancelNewPaymentBtnClick = function() {
		 $('#divNewPayment').addClass('hidden-force');
		 $('#sectionPaymentMethodList').removeClass('hidden-force');
	 };
	 
	 self.btnCreatePaymentMethodClick = function(paymentMethod) {
		 paymentService.createPaymentMethod(paymentMethod, function(newPaymentMethod) {
	   		  if (newPaymentMethod) {
	   			  self.paymentMethodList.push(newPaymentMethod);
	   		  }
	   		  $('#divNewPayment').addClass('hidden-force');
	   		  $('#sectionPaymentMethodList').removeClass('hidden-force');
	   	  });
		 
	 };
	 
	 self.getPaymentMethodImage = function(cardType) {
		 return getPaymentMethodImage(cardType);
	};
	 
    self.initialize();
      
  }]);

function onCancelNewPaymentBtnClick() {
	 $('#divNewPayment').addClass('hidden-force');
	 $('#sectionPaymentMethodList').removeClass('hidden-force');
};

function btnCreatePaymentMethodClick(){                         
	bluesnap.submitCredentials( function(cardData){
        console.log('the card type is ' + cardData.ccType + ' and last 4 digits are ' + cardData.last4Digits + ' and exp is ' + cardData.exp + ' after that I can call final submit');
    });

	/*
	var cardNumber = cardData.last4Digits;
	var cardType = cardData.ccType;
	var expDate = cardData.exp;
	var blueSnapId = cardData.bluesSnapId;
	*/
	/*
	var frameDocument = null;
	for(var i = 0; i < window.frames.length; i++) {
		if (window.frames[i].document.getElementById('divNewPayment') != null) {
			frameDocument = window.frames[i].document;
			break;
		}
	}
	var cardNumber = '11111111111';
	var cardType = 'Visa';
	var expDate = '12/2019';
	var blueSnapId = 1234;
		
	var paymentMethod = {
			cardNumber : cardNumber.substring(cardNumber.length - 4),
			cardType   : cardType,
			expDate	   : expDate,
			blueSnapId : blueSnapId
	 };
	var scope = angular.element( $('#divBody') ).scope();
	scope.ctrl.btnCreatePaymentMethodClick(paymentMethod);
	*/
}