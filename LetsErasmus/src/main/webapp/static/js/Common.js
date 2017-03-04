﻿
var EnmReservationStatus = {
	INQUIRY : 0,
	PENDING : 1,
	CONFIRMED : 2,
	DECLINED : 3,
	EXPIRED : 4,
	RECALLED : 5,
	HOST_CANCELLED : 6,
	CLIENT_CANCELLED : 7,
	WAITING_PAYMENT : 8,
	CLOSED : 9
};

function getReservationStatusDesc(reservationStatus) {
	if (reservationStatus == EnmReservationStatus.PENDING) {
		return 'Pending';
	} else if (reservationStatus == EnmReservationStatus.CONFIRMED) {
		return 'Confirmed';
	} else if (reservationStatus == EnmReservationStatus.DECLINED) {
		return 'Rejected';
	} else if (reservationStatus == EnmReservationStatus.EXPIRED) {
		return 'Expired';
	} else if (reservationStatus == EnmReservationStatus.RECALLED) {
		return 'Recalled by Guest';
	} else if (reservationStatus == EnmReservationStatus.HOST_CANCELLED) {
		return 'Cancelled by Host';
	} else if (reservationStatus == EnmReservationStatus.CLIENT_CANCELLED) {
		return 'Cancelled by Guest';
	} else if (reservationStatus == EnmReservationStatus.WAITING_PAYMENT) {
		return 'Waiting For Payment';
	} else if (reservationStatus == EnmReservationStatus.CLOSED) {
		return 'Closed';
	}
}

var EnmUriParam = {
	CHECKIN_DATE : 'checkinDate',
	CHECKOUT_DATE : 'checkoutDate',
	LATITUDE : 'lat',
	LONGITUDE : 'lng',
	LOCATION : 'loc',
	PLACE_ID : 'placeId',
	USER_ID : 'userId'
};

var EnmOperation = {
	CONFIRM_EMAIL : 1,
	LOGIN : 2
};

var EnmPageMode = {
	CREATE : 1,
	UPDATE : 2
};

var EnmPlaceStatus = {
	ACTIVE : 1,
	DEACTIVE : 2,
	DELETED : 3
};

var EnmOperationResultCode = {
    SUCCESS: 0,
    WARNING : 1,
    ERROR: 2,
    EXCEPTION : 3
};

var EnmEntityType = {
    USER  : 1,
	PLACE : 2,
    EVENT : 3,
    STAFF : 4
};

var OperationResult = {
    resultCode: 'resultCode',
    resultDesc: 'resultDesc',
    resultObj: 'resultObj',
    resultValue: 'resultValue',
    errorCode: 'errorCode'
};

var EnmErrorCode = {
		SYSTEM_ERROR : -2,
		UNDEFINED_ERROR : -1,
		UNAUTHORIZED_OPERATION : 1,
        USER_NOT_FOUND : 2,
        MSISDN_VERIFICATION_CODE_INCORRECT : 3,
        USER_NOT_LOGGED_IN : 4
};

var EnmLoginType = {
		LOCAL_ACCOUNT : 1,
		GOOGLE : 2,
		FACEBOOK : 3
};

var tests = {
      	  	filereader : typeof FileReader != 'undefined',
      	  	dnd : 'draggable' in document.createElement('span'),
      	  	formdata : !!window.FormData,
      	  	progress : "upload" in new XMLHttpRequest
      	  };

var currentUrlParamArr = null;
var getUriParam = function (paramName) {
    var url = window.location.href;
    var indexOfParamStart = url.indexOf('?');
    if (indexOfParamStart > -1 && (indexOfParamStart + 1) < url.length) {

        var paramPart = url.substring(indexOfParamStart + 1);
        var paramList = paramPart.split('&');
        
        for (i = 0; i < paramList.length; i++) {
            var paramValuePair = paramList[i];
            if (paramValuePair.indexOf('=') > -1) {
                var paramValueArr = paramValuePair.split('=');
                if (paramValueArr[0].toLowerCase() == paramName.toLowerCase()) {
                    return decodeURI(paramValueArr[1]);
                }
            } else {
                if (paramValuePair.toLowerCase() == paramName.toLowerCase()) {
                    return null;
                }
            }
        }
    }
    return null;
};

var clearUrlParameter = function (url, paramName) {
	var newUrl = url.substring(0, url.indexOf('?'));
	if (paramName) {
		var indexOfParamStart = url.indexOf('?');
	    if (indexOfParamStart > -1 && (indexOfParamStart + 1) < url.length) {

	        var paramPart = url.substring(indexOfParamStart + 1);
	        var paramList = paramPart.split('&');
	        
	        for (i = 0; i < paramList.length; i++) {
	            var paramValuePair = paramList[i];
	            var urlParamName = null
	            if (paramValuePair.indexOf('=') > -1) {
	                var paramValueArr = paramValuePair.split('=');
	                urlParamName = paramValueArr[0];
	            } else {
	            	urlParamName = paramValuePair;
	            }
	            if (newUrl.indexOf('?') < 0) {
	            	newUrl += '?';
	            }
                if (urlParamName.toLowerCase() != paramName.toLowerCase()) {
                	newUrl += paramValuePair;
                }	            
	        }
	    }
	} else {
		return newUrl;
	}
};

var newOperationResult = function (resultCode, resultDesc, resultObj) {
    return {
        resultCode: resultCode,
        resultDesc: resultDesc,
        resultObj: resultObj,
    };
};

function openModal(url, elementId) {
	ajaxHtml(url, elementId, function() { modal.style.display='block'; });
}

function openWindow (url, isSelf) {
	if (isSelf) {
		document.location.href = url;
	} else {
		window.open(url, '_blank');
	}
}

function ajaxHtml(url, elementId, callbackFunc) {
	getHtml(url, function(html) {
		$('#' + elementId).html(html);
        if (callbackFunc != null) {
        	callbackFunc();
        }
	});	
}

function getHtml(url, callbackFunc) {
	$.ajax({
        url: url,
        data: {
        },
        type: "GET",
        dataType: "html",
        success: function (data) {
            if (callbackFunc != null) {
            	callbackFunc(data);
            }
        },
        error: function (xhr, status) {
            alert("Sorry, there was a problem!");
        },
        complete: function (xhr, status) {
            
        }
    });
}

function ajaxJson(url, data, callbackFunc) {
	$.ajax({
        url: url,
        data: data,
        type: "POST",
        dataType: "json",
        success: function (data) {
            if (callbackFunc != null) {
            	callbackFunc(data);
            }
        },
        error: function (xhr, status) {
            alert("Sorry, there was a problem!");
        },
        complete: function (xhr, status) {
            
        }
    });
}

function getCurrencySymbol(currencyId) {
	if (currencyId == 1) {
		return "₺";
	} else if (currencyId == 2) {
		return "$";
	} else if (currencyId == 3) {
		return "€";
	}
}

function clone(obj){
    if(obj == null || typeof(obj) != 'object') return obj;
    var temp = new obj.constructor(); 
    for(var key in obj) temp[key] = clone(obj[key]);
    return temp;
}

function increaseCounterElement(elementId) {
	var currentText = $('#' + elementId).text();
	var currentValue = currentText.substring(0, currentText.indexOf(' '));
	var newValue = parseInt(currentValue) + 1;
	var newText = newValue + currentText.substring(currentText.indexOf(' '))
	$('#' + elementId).text(newText);
}

function decreaseCounterElement(elementId, minValue) {
	var currentText = $('#' + elementId).text();
	var currentValue = currentText.substring(0, currentText.indexOf(' '));
	currentValue = parseInt(currentValue);
	if (minValue == null || currentValue != minValue) {
		var newValue = parseInt(currentValue) - 1;
		var newText = newValue + currentText.substring(currentText.indexOf(' '))
		$('#' + elementId).text(newText);
	}
}

function getCounterElementValue(elementId) {
	var currentText = $('#' + elementId).text();
	var currentValue = currentText.substring(0, currentText.indexOf(' '));
	return parseInt(currentValue);
}

function setCounterElementValue(elementId, value) {
	var currentText = $('#' + elementId).text();
	var newValue = value;
	var newText = value + currentText.substring(currentText.indexOf(' '))
	$('#' + elementId).text(newText);
}

function generateRandomValue(minValue, maxValue) {
	var range = maxValue - minValue + 1;
	return Math.floor(Math.random() * range) + minValue;
}

var DialogUtil = {
		
	MESSAGE_TYPE : {
		INFO : 1,
		WARNING : 2,
		ERROR : 3
	},
	
	showInfo : function (title, message, callBackFunc) {
		// TODO : jquery dialog framework will be used
		alert(message);
	},
	
	info : function (title, message, okText, callback) {
	    $("<div style='z-index: 10000'></div>").dialog( {
	        buttons: [{
	            text: okText,
	            click: function() {
	                if (callback) {
	                	callback();
	                }
	                $( this ).remove();
	            }
	        }],
	        close: function (event, ui) { $(this).remove(); },
	        resizable: false,
	        title: title,
	        modal: true
	    }).text(message);
	},
	
	warn : function (title, message, okText, callback) {
	    $("<div style='z-index: 10000'></div>").dialog( {
	        buttons: [{
	            text: okText,
	            click: function() {
	                if (callback) {
	                	callback();
	                }
	                $( this ).remove();
	            }
	        }],
	        close: function (event, ui) { $(this).remove(); },
	        resizable: false,
	        title: title,
	        modal: true
	    }).text(message);
	},
	
	error : function (title, message, okText, callback) {
	    $("<div></div>").dialog( {
	        buttons: [{
	            text: okText,
	            click: function() {
	                if (callback) {
	                	callback();
	                }
	                $( this ).remove();
	            }
	        }],
	        close: function (event, ui) { $(this).remove(); },
	        resizable: false,
	        title: title,
	        modal: true
	    }).text(message);
	},
	
	confirm : function (title, message, callback) {
	    $("<div></div>").dialog( {
	        buttons: [{
	            text: 'Yes',
	            click: function() {
	                if (callback) {
	                	callback(true);
	                }
	                $( this ).remove();
	            }
	        },
	        {
	            text: 'No',
	            click: function() {
	                if (callback) {
	                	callback(false);
	                }
	                $( this ).remove();
	            }
	        }
	        ],
	        close: function (event, ui) { $(this).remove(); },
	        resizable: false,
	        title: title,
	        modal: true
	    }).text(message);
	},
	
	showMessage : function(type, title, message, callBackFunc) {
		$.msgbox({
			  title: title,
			  content: message,
			  type: type,
			  buttons: [{ value: "Ok" }],
			  success : function (result) {
				  if (result == 'Ok') {
					  if (callBackFunc) {
						  callBackFunc();
					  }
				  }
			  }
			  });
	
	},

	showConfirm : function(title, message, callBackFunc) {
		$.msgBox({
			title : title,
			content : message,
			type : "confirm",
			buttons : [ {
				value : "Yes"
			}, {
				value : "No"
			}, {
				value : "Cancel"
			} ],
			success : function(result) {
				if (result == "Yes") {
					callBackFunc(true);
				} else if (result == "No") {
					callBackFunc(false);
				}
			}
		});
	}
};

var StringUtil = {
	trim: function(value) {
		if (value != null) {
			return value.trim();
		} else {
			return null;
		}
	},
	
	replaceAll : function(text, oldValue, newValue) {
		return eval('text.replace(/' + oldValue + '/g, newValue)');
	}
};


$.extend({ confirm: function (title, message, yesText, noText, yesCallback) {
    $("<div></div>").dialog( {
        buttons: [{
            text: yesText,
            click: function() {
                if (yesCallback) {
                	yesCallback();
                }
                $( this ).remove();
            }
        },
        {
            text: noText,
            click: function() {
                $( this ).remove();
            }
        }
        ],
        close: function (event, ui) { $(this).remove(); },
        resizable: false,
        title: title,
        modal: true
    }).text(message);
}
});

function handleAjaxError(operationResult) {
	if (operationResult.resultCode == EnmOperationResultCode.WARNING) {
		DialogUtil.warn('Warning', operationResult.resultDesc, 'OK', null);
	} else {
		DialogUtil.error('Error', operationResult.resultDesc, 'OK', function() {
			if (operationResult.errorCode == EnmErrorCode.UNAUTHORIZED_OPERATION) {
				location.href = webApplicationUrlPrefix + '/pages/Unauthorized.xhtml';
			} else if(operationResult.errorCode == EnmErrorCode.USER_NOT_LOGGED_IN) {
				openLoginWindow();
			}
		});
	}
}

function isResultSuccess(operationResult, handleError) {
	if (operationResult.resultCode == EnmOperationResultCode.SUCCESS) {
		return true;
	} else {
		if (handleError) {
			handleAjaxError(operationResult);
		}
		return false;
	}
}

function refreshAngularScope(scope) {
	scope.$apply(function() {
		
	});
}

function generateUserProfilePhotoUrl(userId, photoId, size) {
	if (userId && photoId) {
		var photoUrl = StringUtil.replaceAll(profilePhotoUrlTemplate, '#userId#', userId);
		photoUrl = StringUtil.replaceAll(photoUrl, '#photoId#', photoId);
		photoUrl = StringUtil.replaceAll(photoUrl, '#size#', size);
		return photoUrl;
	} else {
		return StringUtil.replaceAll(defaultUserProfileImageUrlTemplate, '#size#', size);
	}
}

function createEmptyFile(fileName) {
	var parts = [
        new Blob([''], {type: 'text/plain'}), '', new Uint16Array([33])
      ];

	return new File(parts, fileName, {});
}