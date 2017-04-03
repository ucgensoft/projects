App.controller('paymentMethodCtrl', ['$scope', '$controller', function($scope, $controller) {
      var self = this;
      
      self.initialize = function() {
    	  $('#txtExpDate').mask('00/0000');
    	  $('#txtCardNumber').mask('0000 0000 0000 0000');
    	  $('#txtCvv').mask('00000');
    	  
    	  
    	  var bsObj = {
                  hostedPaymentFields: {
                      ccn: "ccn",
                      cvv: "cvv", 
                      exp: "exp"  
                  },
                  onFieldEventHandler: {

		                      onError: function(tagId, errorCode) { 
		                          // Handle a change in validation
		                          /* error codes:
		                              "001" --> "Please enter a valid credit card number";            
		                              "002" --> "Please enter the CVV/CVC of your card";          
		                              "003" --> "Please enter your credit card's expiration date";            
		                              "004" --> "Session expired please refresh page to continue";            
		                              "005" --> "Internal server error please try again later";           
		                          */
		
		              if (tagId == "ccn" && errorCode == "001") {
		                $( "#card-number" ).removeClass( "hosted-field-focus hosted-field-valid" ).addClass( "hosted-field-invalid" );
		                $( "#card-help" ).text('Please enter a valid credit card number');
		              } else if (tagId == "exp" && errorCode == "003") {
		                $( "#exp-date" ).removeClass( "hosted-field-focus hosted-field-valid" ).addClass( "hosted-field-invalid" ).next('span').text('Please enter the expiration date (MM/YYYY)'); 
		              } else if (tagId == "cvv" && errorCode == "002" ) {
		                $( "#cvv" ).removeClass( "hosted-field-focus hosted-field-valid" ).addClass( "hosted-field-invalid" ).next('span').text('Please enter the CVV/CVC of your card');
		              }
		                      },
		                      onEmpty: function(tagId, errorCode) { 
		                          // Handle a change in validation  
		              if (tagId == "ccn" && errorCode == "001") {
		                $( "#card-number" ).removeClass( "hosted-field-focus hosted-field-valid hosted-field-invalid" );
		                $( "#card-help" ).text('');
		                $('#card-logo img').attr("src", "https://files.readme.io/d1a25b4-generic-card.png");
		              } else if (tagId == "exp" && errorCode == "003") {
		                $( "#exp-date" ).removeClass( "hosted-field-focus hosted-field-valid hosted-field-invalid" ).next('span').text('');
		              } else if (tagId == "cvv" && errorCode == "002" ) {
		                $( "#cvv" ).removeClass( "hosted-field-focus hosted-field-valid hosted-field-invalid" ).next('span').text('');
		              }
		                      },
		                      onType: function(tagId, cardType) {
		                          // get card type from cardType and display the img accordingly
		                          
		              if (cardType == "AmericanExpress") { $('#card-logo img').attr("src", "https://files.readme.io/97e7acc-Amex.png");
		              } else if (cardType == "CarteBleue") { $('#card-logo img').attr("src", "https://files.readme.io/5da1081-cb.png");
		              } else if (cardType == "DinersClub") { $('#card-logo img').attr("src", "https://files.readme.io/8c73810-Diners_Club.png");
		              } else if (cardType == "Discover") { $('#card-logo img').attr("src", "https://files.readme.io/caea86d-Discover.png");
		              } else if (cardType == "JCB") { $('#card-logo img').attr("src", "https://files.readme.io/e076aed-JCB.png");
		              } else if (cardType == "MaestroUK") { $('#card-logo img').attr("src", "https://files.readme.io/daeabbd-Maestro.png");
		              } else if (cardType == "MasterCard") { $('#card-logo img').attr("src", "https://files.readme.io/5b7b3de-Mastercard.png");
		              } else if (cardType == "Solo") { $('#card-logo img').attr("src", "https://sandbox.bluesnap.com/services/hosted-payment-fields/cc-types/solo.png");
		              } else if (cardType == "Visa") { $('#card-logo img').attr("src", "https://files.readme.io/9018c4f-Visa.png");
		              }
		                      },
		                      onValid: function(tagId) {
		                          // Handle a change in validation
		              if (tagId == "ccn") {
		                $( "#card-number" ).removeClass( "hosted-field-focus hosted-field-invalid" ).addClass( "hosted-field-valid" )
		                $( "#card-help" ).text('');
		              } else if (tagId == "exp") {
		                $( "#exp-date" ).removeClass( "hosted-field-focus hosted-field-invalid" ).addClass( "hosted-field-valid" ).next('span').text(''); 
		              } else if (tagId == "cvv") {
		                $( "#cvv" ).removeClass( "hosted-field-focus hosted-field-invalid" ).addClass( "hosted-field-valid" ).next('span').text('');
		              }
		                      }
		                  },                                    
		                  //styling is optional
		                  style: {
		                      // Styling all Hosted Payment Field inputs
		                      "input": {
		                          "font-size": "14px",
		              "font-family": "Helvetica Neue,Helvetica,Arial,sans-serif",
		              "line-height": "1.42857143",
		              "color": "#555"
		                      },
		                      
		                      // Styling a specific field
		                      /*"#ccn": {
		                          
		                      },*/
		                      
		                      // Styling Hosted Payment Field input state
		                      ":focus": {
		                          "color": "#555"
		                      }
		                  },
		          ccnPlaceHolder: "4111222233334444", 
		          cvvPlaceHolder: "123", 
		          expPlaceHolder: "MM/YYYY"
		              };
		              // Use jQuery ready() Method to run bluesnap.hostedPaymentFieldsCreation command after DOM is fully loaded
		              $( document ).ready(function() {
		                  bluesnap.hostedPaymentFieldsCreation ("672d02b47ae64498c30ec3312ce57976bf171d86a1b75c805c212b715352639d_", bsObj); //insert your Hosted Payment Fields token
		              });
		
		          
          // Submits the credit card, expiration date and CVV data to BlueSnap, where it will be associated with the Hosted Payment Fields token. On success, a card data object containing the card type and last four digits will be be passed to the function (cardData). You should add a function to do the final submit of the form after the card type and last four digits are received.
          

              
              function do_when_clicking_submit_button(){                         
                  bluesnap.submitCredentials( function(cardData){
                      console.log('the card type is ' + cardData.ccType + ' and last 4 digits are ' + cardData.last4Digits + ' and exp is ' + cardData.exp + ' after that I can call final submit');
                  });
                  /* submit the form
                  return true; */
                  return false; // don't submit the form
              }           
	 };
  	
	 self.onNewPaymentBtnClick = function() {
		 $('#divNewPaymentMethod').removeClass('hidden-force');
		 $('#sectionPaymentMethodList').addClass('hidden-force');
	 };
	 
	 self.onCancelNewPaymentBtnClick = function() {
		 $('#divNewPaymentMethod').addClass('hidden-force');
		 $('#sectionPaymentMethodList').removeClass('hidden-force');
	 };
	 
    self.initialize();
      
  }]);