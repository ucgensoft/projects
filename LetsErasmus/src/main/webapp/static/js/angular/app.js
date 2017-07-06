'use strict';

var App = angular.module('myApp', []).config(function($sceDelegateProvider) {
	 $sceDelegateProvider.resourceUrlWhitelist([
	                                     	   'self',
	                                     	   'https://www.bluesnap.com/**']);
	                                     	 });

angular.module('myApp').directive('dynamic', function ($compile) {
  return {
    restrict: 'A',
    replace: true,
    scope: { dynamic: '=dynamic'},
    link: function postLink(scope, element, attrs) {
      scope.$watch( 'dynamic' , function(html){
        element.html(html);
        $compile(element.contents())(scope);
      });
    }
  };
});

App.filter('html', ['$sce', function ($sce) { 
    return function (text) {
        return $sce.trustAsHtml(text);
    };    
}])

/*
var App = angular.module('myApp', ['ngMockE2E']);
App.run(function($httpBackend) {
	  	  
	  $httpBackend.whenGET('/angular/fakerequest').respond(function(method,url,data) {
	    return [200, phones, {}];
	  });
	});

*/