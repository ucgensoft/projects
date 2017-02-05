
var EnmPlaceStatus = {
	ACTIVE : 1,
	DEACTIVE : 2,
	DELETED : 3
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

var EnmOperationResultCode = {
    SUCCESS: 0,
    WARNING : 1,
    ERROR: 2,
    EXCEPTION : 3
};

var OperationResult = {
        resultCode: 'resultCode',
        resultDesc: 'resultDesc',
        resultObj: 'resultObj',
};

var EnmLoginType = {
		LOCAL_ACCOUNT : 1,
		GOOGLE : 2,
		FACEBOOK : 3
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