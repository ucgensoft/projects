'use strict';

var App = angular.module('myApp', []);

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

/*
var App = angular.module('myApp', ['ngMockE2E']);
App.run(function($httpBackend) {
	  	  
	  $httpBackend.whenGET('/angular/fakerequest').respond(function(method,url,data) {
	    return [200, phones, {}];
	  });
	});

*/