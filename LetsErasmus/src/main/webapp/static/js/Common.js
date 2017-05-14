
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
	LOCATION_ID : 'locationId',
	PLACE_ID : 'placeId',
	USER_ID : 'userId',
	OPERATION : 'op',
	OPERATION_TOKEN : 'opToken'
};

var EnmOperation = {
	CREATE_PLACE : 5,
	CREATE_RESERVATION : 8,
	CONFIRM_EMAIL : 10,
	LOGIN : 11,
	RESET_PASSWORD : 12 
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
    STAFF : 4,
    RESERVATION : 5,
    MESSAGE : 6
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
        USER_NOT_LOGGED_IN : 4,
        ALREADY_CONTACTED : 5,
        USER_DEACTIVE : 6
};

var EnmLoginType = {
		LOCAL_ACCOUNT : 1,
		GOOGLE : 2,
		FACEBOOK : 3
};

var EnmImageSize = {
		SMALL : 'small',
		MEDIUM : 'medium'
}

var tests = {
      	  	filereader : typeof FileReader != 'undefined',
      	  	dnd : 'draggable' in document.createElement('span'),
      	  	formdata : !!window.FormData,
      	  	progress : "upload" in new XMLHttpRequest
      	  };

var currentUrlParamArr = null;
var getUriParam = function (paramName) {
    var url = window.location.href;
    if (url.substring(url.length - 1) == '#') {
    	url = url.substring(0, url.length - 1);
    }
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
                if (urlParamName.toLowerCase() != paramName.toLowerCase()) {
                	if (newUrl.indexOf('?') < 0) {
    	            	newUrl += '?';
    	            }
                	newUrl += paramValuePair;
                }	            
	        }
	    }
	}
	return newUrl;
};

var newOperationResult = function (resultCode, resultDesc, resultObj) {
    return {
        resultCode: resultCode,
        resultDesc: resultDesc,
        resultObj: resultObj,
    };
};

function openModal(url, elementId) {
	ajaxHtml(url, elementId, 
			function() { 
				modal.style.display='block'; 
			});
}

function openWindow (url, isSelf) {
	if (isSelf) {
		if (document.location.href == url) {
			location.reload();
		} else {
			document.location.href = url;
		}
	} else {
		window.open(url, '_blank');
	}
}

function closeWindow() {
	 window.open('','_parent','');
     window.close();
}

function ajaxHtml(url, elementId, callbackFunc) {
	getHtml(url, function(html) {
		$('#' + elementId).html(html);
		$('#' + elementId).css('display', '');
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
        	DialogUtil.error("Sorry, there was a problem!");
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
        	DialogUtil.error("Sorry, there was a problem!");
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

function getCurrencyAbbr(currencyId) {
	if (currencyId == 1) {
		return "TRY";
	} else if (currencyId == 2) {
		return "USD";
	} else if (currencyId == 3) {
		return "EUR";
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
	
	success : function (message, callback, title, okText) {
		if (!title) {
			title = 'Success';
		}
		if (!okText) {
			okText = 'OK';
		}
		swal({title: title, text: message, type: "success"}, callback);
	},
	
	info : function (message, callback, title, okText) {
		if (!title) {
			title = 'Information';
		}
		if (!okText) {
			okText = 'OK';
		}
		swal({title: title, text: message, type: "info"}, callback);
	},
	
	warn : function (message, callback, title, okText) {
		if (!title) {
			title = 'Warning';
		}
		if (!okText) {
			okText = 'OK';
		}
		swal({title: title, text: message, type: "warning"}, callback);
	},
	
	error : function (message, callback, title, okText) {
		if (!title) {
			title = 'Error';
		}
		if (!okText) {
			okText = 'OK';
		}
		swal({title: title, text: message, type: "error"}, callback);
	},
	
	confirm : function (message, callback, title, confirmText, cancelText) {
		if (!title) {
			title = 'Confirmation';
		}
		if (!confirmText) {
			confirmText = 'Yes';
		}
		if (!cancelText) {
			cancelText = 'No';
		}
		swal({
	      	  title: title,
	      	  text: message,
	      	  //type: "warning",
	      	  showCancelButton: true,
	      	  confirmButtonColor: "#DD6B55",
	      	  confirmButtonText: confirmText,
	      	  cancelButtonText : cancelText,
	      	  closeOnConfirm: false,
	      	  imageUrl : '/static/images/confirm.png'
	      	},
	      	callback);
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

function handleAjaxError(operationResult, callBack) {
	if (operationResult.resultCode == EnmOperationResultCode.WARNING) {
		DialogUtil.warn(operationResult.resultDesc, function() {
			if (operationResult.errorCode == EnmErrorCode.ALREADY_CONTACTED) {
				openWindow(operationResult.resultValue, true);
			} else {
				callBack();
			}
		});
	} else {
		if (operationResult.errorCode == EnmErrorCode.USER_NOT_LOGGED_IN) {
			DialogUtil.warn(operationResult.resultDesc, function() {
				openLoginWindow();
			});
		} else {
			var messageText = operationResult.resultDesc;
			//  || messageText.length > 150
			if (messageText.indexOf('Exception') > - 1) {
				messageText = 'Operation failed with an unknown reason. Please try again later.';
			}
			DialogUtil.error(operationResult.resultDesc, function() {
				if (operationResult.errorCode == EnmErrorCode.UNAUTHORIZED_OPERATION) {
					location.href = webApplicationUrlPrefix + '/pages/Unauthorized.xhtml';
				} else {
					callBack();
				}
			});
		}
	}
}

function isResultSuccess(operationResult, handleError, callBack, callBackIfError) {
	if (operationResult.resultCode == EnmOperationResultCode.SUCCESS) {
		if (callBack) {
			callBack();
		}
		return true;
	} else {
		if (handleError) {
			handleAjaxError(operationResult, function() {
				if (callBackIfError) {
					callBack();
				}
			});
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

function generatePlacePhotoUrl(placeId, photoId, size) {
	if (placeId && photoId) {
		var photoUrl = StringUtil.replaceAll(placePhotoUrlTemplate, '#placeId#', placeId);
		photoUrl = StringUtil.replaceAll(photoUrl, '#photoId#', photoId);
		photoUrl = StringUtil.replaceAll(photoUrl, '#size#', size);
		return photoUrl;
	} else {
		return StringUtil.replaceAll(defaultPlaceImageUrlTemplate, '#size#', size);
	}
}

function createEmptyFile(fileName) {
	var parts = [
        new Blob([''], {type: 'text/plain'}), '', new Uint16Array([33])
      ];

	return new File(parts, fileName, {});
}

MapUtil = {
		degreeToMeterLat : function(degreeValue) {
			return degreeValue * 111000;
		},
		
		meterToDegreeLat : function(distance) {
			return (distance / 111000);
		},
		
		meterToDegreeLng : function(distance, lat) {
			//return (distance / 111000) * ( 90 / (90 - Math.abs(lat)));
			return (distance / 111000);
		},
		
		degreeToMeterLng : function(degreeValue, lat) {
			return (degreeValue * 111000) * ( (90 - Math.abs(lat)) / 90) ;
		},
		
		adjustLat : function(lat, adjustAmount) {
			sign = (lat >= 0 ? 1 : -1);
			
			if (lat >= 0) {
				lat = lat + adjustAmount;
			} else {
				lat = sign * (Math.abs(lat) + adjustAmount);
			}
			
			if (lat > 90) {
				lat = 90;
			} else if (lat < -90) {
				lat = -90;
			}
			return lat;
		},
		
		adjustLng : function(lng, adjustAmount) {
			sign = (lat >= 0 ? 1 : -1);
			
			if (lng >= 0) {
				lng = lng + adjustAmount;
			} else {
				lng = sign * (Math.abs(lng) + adjustAmount);
			}
			
			if (lng > 180) {
				lng = -1 * (lng - 180);
			} else if (lng < -180) {
				lng = 180 - (Math.abs(lng) - 180);
			}
			return lng;
		},
		
		getDistanceLat : function(lat1, lat2) {
			var latDiff = 0;
			if ((lat1 > 0 && lat2 > 0) || (lat1 < 0 && lat2 < 0)) {
    		  latDiff = (lat1 > lat2 ? (Math.abs(lat1) - Math.abs(lat2)) : (Math.abs(lat2) - Math.abs(lat1)));
			} else {
    		  latDiff = Math.abs(lat1) + Math.abs(lat2);
			}
			return MapUtil.degreeToMeterLat(latDiff);
		},
		
		getDistanceLng : function(lng1, lng2, lat) {
			var lngDiff = 0;
			if ((lng1 > 0 && lng2 > 0) || lng1 < 0 && lng2 < 0) {
	    		  lngDiff = (lng1 > lng2 ? (Math.abs(lng1) - Math.abs(lng2)) : (Math.abs(lng2) - Math.abs(lng1)));
	    	} else {
	    		var lngDiff1 = Math.abs(lat1) + Math.abs(lat2);
	    		var lngDiff2 = (180 - Math.abs(lat1)) + (180 - Math.abs(lat2));
	    		if (lngDiff1 < lngDiff2) {
	    			lngDiff = lngDiff1;
	    		} else {
	    			lngDiff = lngDiff2;
	    		}
	    	}
			return MapUtil.degreeToMeterLng(lngDiff, lat);
		}
		
}

function reloadPage() {
	var url = location.href;
	if (url.indexOf('#') > -1) {
		url = url.substring(0, url.indexOf('#'));
	}
	openWindow(url, true);
}