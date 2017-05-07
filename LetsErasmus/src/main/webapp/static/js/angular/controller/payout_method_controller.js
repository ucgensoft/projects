App.controller('payoutMethodCtrl', ['$scope', '$controller', 'paymentService', 'commonService', 
                                    function($scope, $controller, paymentService, commonService) {
      var self = this;
      self.payoutMethod = null;
      self.countryList = [];
      self.dummyModel = null;
      self.dummyModelBankAcount = null;
      
      self.initialize = function() {
    	  self.displayTab('divPersonalInfo', 'lnkPersonalInfo');
    	  $("#txtBirthDatePicker").datepicker(
  			{
  				changeMonth: true,
  	            changeYear: true,
  	            yearRange: '-100:-10',
  				maxDate : new Date(),
  				dateFormat : "dd.mm.yy"
  			});
    	  //$("#txtBirthDatePicker").datepicker().val(birthDate);
    	  /*
    	  paymentService.listPaymentMethod(function(userPaymentMethodList) {
    		  if (userPaymentMethodList) {
    			  self.paymentMethodList = userPaymentMethodList;
    		  }
    	  });
    	  */
    	  commonService.listCountry( function(countryList) {
				if (countryList) {
					self.countryList = countryList;
					paymentService.getPayoutMethod(function(payoutMethod) {
						self.displayPayoutMethod(payoutMethod);
					});
				}
			}
		  );
	 };
	 
	 self.displayPayoutMethod = function(payoutMethod) {
		 $('#txtFirstName').val(payoutMethod.vendorFirstName);
		 $('#txtLastName').val(payoutMethod.vendorLastName);
		 if (payoutMethod.vendorBirthDate) {
			 $("#txtBirthDatePicker").datepicker('setDate', new Date(payoutMethod.vendorBirthDate));
		 }
		 $('#txtNationalId').val(payoutMethod.vendorNationalId);
		 $('#txtTaxVatId').val(payoutMethod.vendorTaxId);
		 if (payoutMethod.vendorCountry) {
			 $('#cmbCountry').val(payoutMethod.vendorCountry);
		 }
		 
		 $('#txtCity').val(payoutMethod.vendorCity);
		 $('#txtAddress').val(payoutMethod.vendorAddress);
		 $('#txtAddress2').val(payoutMethod.vendorAddress2);
		 $('#txtPostalCode').val(payoutMethod.vendorZip);
		 
		 if (payoutMethod.bankAccountClass) {
			 if (payoutMethod.bankAccountClass == 'PERSONAL') {
				 $("#rdAccountClassPersonal")[0].checked = true;
			 } else if (payoutMethod.bankAccountClass == 'BUSINESS') {
				 $("#rdAccountClassBusiness")[0].checked = true;
			 }
		 }
		 
		 $('#txtIban').val(payoutMethod.bankAccountIban);
		 $('#txtBankAccountHolderName').val(payoutMethod.bankAccountHolderName);
		 if (payoutMethod.bankCountry) {
			 $('#cmbBankAccountCountry').val(payoutMethod.bankCountry);
			 self.onBankCountryChange();
		 }
		 $('#txtSwiftCode').val(payoutMethod.bankSwiftBic);
		 
		 self.onBankAccountTypeChange();
	 };
	 
	 self.displayTab = function(tabId, linkId) {
		 $('#lnkPersonalInfo').attr('aria-selected', 'false');
		 $('#lnkAddressInfo').attr('aria-selected', 'false');
		 $('#lnkBankAccountInfo').attr('aria-selected', 'false');
		 
		 $('#divPersonalInfo').addClass('hidden-force');
		 $('#divAddressInfo').addClass('hidden-force');
		 $('#divBankAccountInfo').addClass('hidden-force');
		 
		 $('#' + linkId).attr('aria-selected', 'true');
		 $('#' + tabId).removeClass('hidden-force');
		 
	 };
	 
	 self.onBankCountryChange = function() {
		 $('#txtIbanCountryCode').val($('#cmbBankAccountCountry').val().toUpperCase());
		 
	 };
	 
	 self.onSaveBtnClick = function() {
		 var vendorFirstName = StringUtil.trim($('#txtFirstName').val());
		 var vendorLastName = StringUtil.trim($('#txtLastName').val());
		 var vendorBirthDate = $("#txtBirthDatePicker").datepicker("getDate");
		 var vendorNationalId = StringUtil.trim($("#txtNationalId").val());
		 var vendorTaxId = null;
		 if (!$('#divTaxId').hasClass('hidden-force')) {
			 vendorTaxId = StringUtil.trim($("#txtTaxVatId").val());
		 }
		 
		 var vendorCountry = null;
		 if ($("#cmbCountry")[0].selectedIndex > 0) {
			 vendorCountry = $("#cmbCountry").val();
		 }
		 var vendorCity = StringUtil.trim($("#txtCity").val());
		 var vendorAddress = StringUtil.trim($("#txtAddress").val());
		 var vendorAddress2 = StringUtil.trim($("#txtAddress2").val());
		 var vendorZip = StringUtil.trim($("#txtPostalCode").val());
		 
		 var bankAccountClass = null;
		 if ($("#rdAccountClassPersonal")[0].checked) {
			 bankAccountClass = 'PERSONAL';
	   	 } else if ($("#rdAccountClassBusiness")[0].checked) {
	   		bankAccountClass = 'BUSINESS';
	   	 }
		 var bankAccountIban = StringUtil.trim($('#txtIban').val());
		 var bankAccountHolderName = StringUtil.trim($('#txtBankAccountHolderName').val());
		 var bankCountry = null;
		 if ($("#cmbBankAccountCountry")[0].selectedIndex > 0) {
			 bankCountry = $('#cmbBankAccountCountry').val();
		 }
		 var bankSwiftBic = StringUtil.trim($('#txtSwiftCode').val());
		 
		 var payoutMethod = {
			 vendorFirstName : vendorFirstName,
			 vendorLastName : vendorLastName,
			 vendorBirthDate : vendorBirthDate,
			 vendorNationalId : vendorNationalId,
			 vendorTaxId : vendorTaxId,
			 vendorCountry : vendorCountry,
			 vendorCity : vendorCity,
			 vendorAddress : vendorAddress,
			 vendorAddress2 : vendorAddress2,
			 vendorZip : vendorZip,
			 
			 bankAccountClass : bankAccountClass,
			 bankAccountIban : bankAccountIban,
			 bankAccountHolderName : bankAccountHolderName,
			 bankCountry : bankCountry,
			 bankSwiftBic : bankSwiftBic
		 };
		 
		 paymentService.updatePayoutMethod(payoutMethod, function(result) {
			  if (result) {
				  DialogUtil.success( 'Your payout information is saved!', function() {
					  reloadPage();
				  });
			  }
		  });
		 
	 };
	 
	 self.onBankAccountTypeChange = function() {
		 if ($("#rdAccountClassBusiness")[0].checked) {
			 $('#divTaxId').removeClass('hidden-force');
	   	 } else {
	   		$('#divTaxId').addClass('hidden-force');
	   	 }
	 };
	  
    self.initialize();
      
  }]);