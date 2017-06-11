'use strict';

App.factory('userService', ['$http', '$q', function($http, $q){

	return {
		createUser : function(user, callBack) {
			var config = {
				headers : {
					'Accept' : 'application/json'
				}
			};
			
			NProgress.start(3000, 5, true);
			self = this;
			return $http.post(webApplicationUrlPrefix + '/api/user/signup', user, config).then(function(response) {
				NProgress.done(true, true);
				var result = isResultSuccess(response.data, true, function() {
					if (response.data != null && response.data.errorCode == EnmErrorCode.USER_DEACTIVE) {
						DialogUtil.confirm(response.data.resultDesc, function() {
							self.reactivateUser(callBack);
						});
					} else {
						if (callBack) {
							callBack(true);
						}
					}
				}, false);
			}, function(errResponse) {
				NProgress.done(true, true);
				DialogUtil.error(errResponse);
			});
		},
		
		updateUser : function(user, profilePhoto, callBack) {
			NProgress.start(4000, 10);
			this.savePhoto(profilePhoto).then(
				function(operationResult) {
					if (isResultSuccess(operationResult, true)) {
						
						var config = {
							headers : {
								'Accept' : 'application/json',
								'Accept-Charset': 'UTF-8'
							}
						};
						
						$http.post(webApplicationUrlPrefix + '/api/user/update', user, config).then(
							function(response) {
								NProgress.done(true);
								var result = isResultSuccess(response.data, true, function() {
									callBack(true);
								}, false);
							}, function(errResponse) {
								NProgress.done(true);
								console.error(errResponse);
							}
						);
					} else {
						NProgress.done(true);
					}
				}, function(errResponse) {
					NProgress.done(true);
					DialogUtil.error('Operation could not be completed. Please try again later!');
				});
		},
		
		savePhoto : function(profilePhoto) {
			var formData = new FormData();
			var file = profilePhoto.file;
			/*
			if (profilePhoto == null || profilePhoto.file == null) {
				var parts = [
		            new Blob([''], {type: 'text/plain'}), '', new Uint16Array([33])
		          ];
	
				file = new File(parts, 'dummy', {});
			} else {
				file = profilePhoto.file;
			}
			*/
			
			formData.append('profilePhoto', file);					
			
			var config = {
				headers : {
					'Accept' : 'application/json',
					'Content-Type' : undefined
				}
			};
			return $http.post(webApplicationUrlPrefix + '/api/user/savephoto', formData, config).then(function(response) {
				return response.data;
			}, function(errResponse) {
				console.error('Error while creating place');
				return $q.reject(errResponse);
			});
		},
		
		login : function(user, callBack) {
			var config = {
				headers : {
					'Accept' : 'application/json'
				}
			};
			NProgress.start(3000, 5);
			var self = this;
			return $http.post(webApplicationUrlPrefix + '/api/user/login', user, config).then(function(response) {
				NProgress.done(true);
				
				var result = isResultSuccess(response.data, true, function() {
					if (response.data != null && response.data.errorCode == EnmErrorCode.USER_DEACTIVE) {
						DialogUtil.confirm(response.data.resultDesc, function() {
							self.reactivateUser(callBack);
						});
					} else {
						if (callBack) {
							callBack(true);
						}
					}
				}, false);
			}, function(errResponse) {
				DialogUtil.error(errResponse);
			});
		},
		
		logout : function() {
			var config = {
				headers : {
					'Accept' : 'application/json'
				}
			};
			return $http
					.post(webApplicationUrlPrefix + '/api/user/logout', null, config).then(function(response) {
						return response.data;
					}, function(errResponse) {
						DialogUtil.error(errResponse);
					});
		},
		
		confirmEmail : function(id, code, callBack) {
			NProgress.start(4000, 5);
			var data = {
					id : id,
					code : code
			}
			
			var config = {
				params : data,
				headers : {
					'Accept' : 'application/json'
				}
			};
			return $http.post(webApplicationUrlPrefix + '/api/user/confirmemail', data, config).then(function(response) {
						NProgress.done(true);
						var result = isResultSuccess(response.data, true, function() {
							callBack(true);
						}, false);
					}, function(errResponse) {
						DialogUtil.error(errResponse);
					});
		},
		
		sendMsisdnVerificationCode : function(countryCode, msisdn, callBack) {
			NProgress.start(4000, 5);
			var data = {
				msisdn : msisdn,
				msisdnCountryCode : countryCode
			}
			
			var config = {
				params : data,
				headers : {
					'Accept' : 'application/json'
				}
			};
			return $http.post(webApplicationUrlPrefix + '/api/user/msisdn/sendcode', data, config).then(function(response) {
						NProgress.done(true);
						var result = isResultSuccess(response.data, true, function() {
							callBack(true);
						}, false);
					}, function(errResponse) {
						DialogUtil.error(errResponse);
					});
		},
		
		sendEmailVerificationCode : function(callBack) {
			NProgress.start(4000, 5);
			var data = {
			}
			
			var config = {
				params : data,
				headers : {
					'Accept' : 'application/json'
				}
			};
			return $http.post(webApplicationUrlPrefix + '/api/user/email/sendcode', data, config).then(function(response) {
						NProgress.done(true);
						var result = isResultSuccess(response.data, true, function() {
							callBack(true);
						}, false);
					}, function(errResponse) {
						DialogUtil.error(errResponse);
					});
		},
		
		verifyMsisdnCode : function(code, callBack) {
			NProgress.start(4000, 5);
			var data = {
				code : code
			}
			
			var config = {
				params : data,
				headers : {
					'Accept' : 'application/json'
				}
			};
			return $http.post(webApplicationUrlPrefix + '/api/user/msisdn/verify', data, config).then(function(response) {
						NProgress.done(true);
						var result = isResultSuccess(response.data, true, function() {
							callBack(true);
						}, false);
					}, function(errResponse) {
						DialogUtil.error(errResponse);
					});
		},
		
		removeMsisdn : function(callBack) {
			NProgress.start(4000, 5);
			var data = {
				
			}
			
			var config = {
				params : data,
				headers : {
					'Accept' : 'application/json'
				}
			};
			return $http.post(webApplicationUrlPrefix + '/api/user/msisdn/remove', data, config).then(function(response) {
						NProgress.done(true);
						var result = isResultSuccess(response.data, true, function() {
							callBack(true);
						}, false);
					}, function(errResponse) {
						DialogUtil.error(errResponse);
					});
		},
		
		isEmailVerified : function(callBack) {
			var data = {
			}
			
			var config = {
				params : data,
				headers : {
					'Accept' : 'application/json'
				}
			};
			return $http.post(webApplicationUrlPrefix + '/api/user/email/isverified', data, config).then(function(response) {
						var result = isResultSuccess(response.data, true, function() {
							var isVerified = response.data.resultValue;
							if (callBack) {
								callBack(isVerified);
							}
						}, false);		
					}, function(errResponse) {
						DialogUtil.error(errResponse);
					});
		},
		
		connectGoogleAccount : function(user, callBack) {
			var config = {
					headers : {
						'Accept' : 'application/json'
					}
				};
				
				NProgress.start(3000, 5);
				return $http.post(webApplicationUrlPrefix + '/api/user/connectgoogle', user, config).then(function(response) {
					NProgress.done(true);
					var result = isResultSuccess(response.data, true, function() {
						callBack(true);
					}, false);
				}, function(errResponse) {
					DialogUtil.error(errResponse);
				});
		},
		
		disconnectGoogleAccount : function(callBack) {
			var config = {
				headers : {
					'Accept' : 'application/json'
				}
			};
			
			NProgress.start(3000, 5);
			return $http.post(webApplicationUrlPrefix + '/api/user/disconnectgoogle', config).then(function(response) {
				NProgress.done(true);
				var result = isResultSuccess(response.data, true, function() {
					callBack(true);
				}, false);
			}, function(errResponse) {
				DialogUtil.error(errResponse);
			});
		},
		
		connectFacebookAccount : function(user, callBack) {
			var config = {
					headers : {
						'Accept' : 'application/json'
					}
				};
				
				NProgress.start(3000, 5);
				return $http.post(webApplicationUrlPrefix + '/api/user/connectfacebook', user, config).then(function(response) {
					NProgress.done(true);
					var result = isResultSuccess(response.data, true, function() {
						callBack(true);
					}, false);
				}, function(errResponse) {
					DialogUtil.error(errResponse);
				});
		},
		
		disconnectFacebookAccount : function(callBack) {
			var config = {
				headers : {
					'Accept' : 'application/json'
				}
			};
			
			NProgress.start(3000, 5);
			return $http.post(webApplicationUrlPrefix + '/api/user/disconnectfacebook', config).then(function(response) {
				NProgress.done(true);
				var result = isResultSuccess(response.data, true, function() {
					callBack(true);
				}, false);
			}, function(errResponse) {
				DialogUtil.error(errResponse);
			});
		},
		
		deactivateUser : function(callBack) {
			var config = {
				headers : {
					'Accept' : 'application/json'
				}
			};
			
			NProgress.start(2000, 5);
			return $http.post(webApplicationUrlPrefix + '/api/user/deactivate', config).then(function(response) {
				NProgress.done(true);
				var result = isResultSuccess(response.data, true, function() {
					callBack(true);
				}, false);
			}, function(errResponse) {
				DialogUtil.error(errResponse);
			});
		},
		
		reactivateUser : function(callBack) {
			var config = {
				headers : {
					'Accept' : 'application/json'
				}
			};
			
			NProgress.start(2000, 5);
			return $http.post(webApplicationUrlPrefix + '/api/user/reactivate', config).then(function(response) {
				NProgress.done(true);
				var result = isResultSuccess(response.data, true, function() {
					callBack(true);
				}, false);
			}, function(errResponse) {
				DialogUtil.error(errResponse);
			});
		},
		
		getUser : function(userId, callBack) {
			var config = {
				params : {userId: userId},
				headers : {
					'Accept' : 'application/json'
				}
			};
			
			NProgress.start(2000, 5);
			return $http.get(webApplicationUrlPrefix + '/api/user/get', config).then(function(response) {
				NProgress.done(true);
				var result = isResultSuccess(response.data, true, function() {
					callBack(response.data.resultValue);
				}, false);
			}, function(errResponse) {
				DialogUtil.error(errResponse);
			});
		},
		
		sendResetPasswordEmail : function(email, callBack) {
			NProgress.start(4000, 5);
			var data = {
				email : email
			}
			
			var config = {
				params : data,
				headers : {
					'Accept' : 'application/json'
				}
			};
			
			NProgress.start(2000, 5);
			return $http.get(webApplicationUrlPrefix + '/api/user/sendresetpasswordemail', config).then(function(response) {
				NProgress.done(true);
				var result = isResultSuccess(response.data, true, function() {
					callBack(true);
				}, false);
			}, function(errResponse) {
				DialogUtil.error(errResponse);
			});
			
		},
		
		resetPassword : function(userId, code, callBack) {
			NProgress.start(4000, 5);
			var data = {
					userId : userId,
					code : code
			}
			
			var config = {
				params : data,
				headers : {
					'Accept' : 'application/json'
				}
			};
			
			NProgress.start(2000, 5);
			return $http.get(webApplicationUrlPrefix + '/api/user/resetpassword', config).then(function(response) {
				NProgress.done(true);
				var result = isResultSuccess(response.data, true, function() {
					callBack(true);
				}, false);
			}, function(errResponse) {
				DialogUtil.error(errResponse);
			});
			
		}
	};

}]);
