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
		 $('#frameNewPayment').removeClass('hidden-force');
		 $('#sectionPaymentMethodList').addClass('hidden-force');
	 };
	 
	 self.onCancelNewPaymentBtnClick = function() {
		 $('#frameNewPayment').addClass('hidden-force');
		 $('#sectionPaymentMethodList').removeClass('hidden-force');
	 };
	 
	 self.btnCreatePaymentMethodClick = function(paymentMethod) {
		 paymentService.createPaymentMethod(paymentMethod, function(newPaymentMethod) {
	   		  if (newPaymentMethod) {
	   			  self.paymentMethodList.push(newPaymentMethod);
	   		  }
	   		  $('#frameNewPayment').addClass('hidden-force');
	   		  $('#sectionPaymentMethodList').removeClass('hidden-force');
	   	  });
		 
	 };
	 
	 self.getPaymentMethodImage = function(cardType) {
		 return getPaymentMethodImage(cardType);
	};
	 
    self.initialize();
      
  }]);

function onCancelNewPaymentBtnClick() {
	 $('#frameNewPayment').addClass('hidden-force');
	 $('#sectionPaymentMethodList').removeClass('hidden-force');
};

function btnCreatePaymentMethodClick(){                         
    /*
	bluesnap.submitCredentials( function(cardData){
        console.log('the card type is ' + cardData.ccType + ' and last 4 digits are ' + cardData.last4Digits + ' and exp is ' + cardData.exp + ' after that I can call final submit');
    });
    */
	/*
	var cardNumber = cardData.last4Digits;
	var cardType = cardData.ccType;
	var expDate = cardData.exp;
	var blueSnapId = cardData.bluesSnapId;
	*/
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
}

function getPaymentMethodImage(cardType) {
	var imageUrl = null;
	if (cardType == "AmericanExpress") {
		imageUrl = "https://files.readme.io/97e7acc-Amex.png";
	} else if (cardType == "CarteBleue") {
		imageUrl = "https://files.readme.io/5da1081-cb.png";
	} else if (cardType == "DinersClub") {
		imageUrl = "https://files.readme.io/8c73810-Diners_Club.png";
	} else if (cardType == "Discover") {
		imageUrl = "https://files.readme.io/caea86d-Discover.png";
	} else if (cardType == "JCB") {
		imageUrl = "https://files.readme.io/e076aed-JCB.png";
	} else if (cardType == "MaestroUK") {
		imageUrl = "https://files.readme.io/daeabbd-Maestro.png";
	} else if (cardType == "MasterCard") {
		imageUrl = "https://files.readme.io/5b7b3de-Mastercard.png";
	} else if (cardType == "Solo") {
		imageUrl = "https://sandbox.bluesnap.com/services/hosted-payment-fields/cc-types/solo.png";
	} else if (cardType == "Visa") {
		imageUrl = "https://files.readme.io/9018c4f-Visa.png";
	}
	return imageUrl;
}