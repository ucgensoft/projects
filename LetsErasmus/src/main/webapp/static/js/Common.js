
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

var newOperationResult = function (code, description) {
    return {
        code: code,
        description: description
    };
};

function openModal(url, elementId) {
	ajaxHtml(url, elementId, function() { modal.style.display='block'; });
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