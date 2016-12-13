
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
                    return paramValueArr[1];
                } else {
                    return null;
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
	$.ajax({
        url: url,
        data: {
        },
        type: "GET",
        dataType: "html",
        success: function (data) {
            $('#' + elementId).html(data);
            if (callbackFunc != null) {
            	callbackFunc();
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

function login() {
	var email = $('#txtEmail')[0].value;
	var password = $('#txtPassword')[0].value;
	var url = webApplicationUrlPrefix + "/api/place/login";
	var callBackFunc = function(data) {
		alert(data.resultDesc);
		openWindow(webApplicationUrlPrefix + "/pages/Main.xhtml", true);
	}
	ajaxJson(url, {email: email, password: password}, callBackFunc);
}