'use strict';

App.factory('userService', ['$http', '$q', function($http, $q){

	return {
		createUser : function(user, callBack) {
			var config = {
				headers : {
					'Accept' : 'application/json'
				}
			};
			
			NProgress.start(3000, 5);
			self = this;
			return $http.post(webApplicationUrlPrefix + '/api/user/signup', user, config).then(function(response) {
				NProgress.done(true);
				var result = isResultSuccess(response.data, true);
				if (result) {
					if (response.data != null && response.data.errorCode == EnmErrorCode.USER_DEACTIVE) {
						DialogUtil.confirm('Confirm', response.data.resultDesc, function(isAccepted) {
							if (isAccepted) {
								self.reactivateUser(callBack);	
							}
						});
					} else {
						if (callBack) {
							callBack(result);
						}
					}
				}
			}, function(errResponse) {
				DialogUtil.error('Error', errResponse, 'OK');
				if (callBack) {
					callBack(false);
				}
			});
		},
		
		updateUser : function(user, profilePhoto, callBack) {
			NProgress.start(4000, 10);
			this.savePhoto(profilePhoto).then(
				function(operationResult) {
					if (isResultSuccess(operationResult, true)) {
						
						var config = {
							headers : {
								'Accept' : 'application/json'
							}
						};
						
						$http.post(webApplicationUrlPrefix + '/api/user/update', user, config).then(
							function(response) {
								NProgress.done(true);
								var isSuccess = false;
								if (isResultSuccess(response.data, true)) {
									isSuccess = true;
								}
								if (callBack) {
									callBack(isSuccess);
								}
							}, function(errResponse) {
								NProgress.done(true);
								console.error('Error while updating place');
								if (callBack) {
									callBack(false);
								}
							}
						);
					} else {
						NProgress.done(true);
						if (callBack) {
							callBack(false);
						}
					}
				}, function(errResponse) {
					NProgress.done(true);
					DialogUtil.inform('Error', 'Operation could not be completed. Please try again later!', 'OK', null);
					if (callBack) {
						callBack(false);
					}
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
			self = this;
			return $http.post(webApplicationUrlPrefix + '/api/user/login', user, config).then(function(response) {
				NProgress.done(true);
				var result = isResultSuccess(response.data, true);
				if (result) {
					if (response.data != null && response.data.errorCode == EnmErrorCode.USER_DEACTIVE) {
						DialogUtil.confirm('Confirm', response.data.resultDesc, function(isAccepted) {
							if (isAccepted) {
								self.reactivateUser(callBack);	
							}
						});
					} else {
						if (callBack) {
							callBack(result);
						}
					}
				}	
			}, function(errResponse) {
				DialogUtil.error('Error', errResponse, 'OK');
				if (callBack) {
					callBack(false);
				}
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
						return $q.reject(errResponse);
					});
		},
		
		confirmEmail : function(id, code) {
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
						isResultSuccess(response.data, true);
						return response.data;
					}, function(errResponse) {
						return $q.reject(errResponse);
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
						var result = isResultSuccess(response.data, true);
						if (callBack) {
							callBack(result);
						}
					}, function(errResponse) {
						DialogUtil.error('Error', errResponse, 'OK');
						if (callBack) {
							callBack(false);
						}
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
						var result = isResultSuccess(response.data, true);
						if (callBack) {
							callBack(result);
						}
					}, function(errResponse) {
						DialogUtil.error('Error', errResponse, 'OK');
						if (callBack) {
							callBack(false);
						}
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
						var result = isResultSuccess(response.data, true);
						if (callBack) {
							callBack(result);
						}
					}, function(errResponse) {
						DialogUtil.error('Error', errResponse, 'OK');
						if (callBack) {
							callBack(false);
						}
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
						var result = isResultSuccess(response.data, true);
						if (callBack) {
							callBack(result);
						}
					}, function(errResponse) {
						DialogUtil.error('Error', errResponse, 'OK');
						if (callBack) {
							callBack(false);
						}
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
						var operationSuccess = isResultSuccess(response.data, false);
						if (operationSuccess) {
							var isVerified = response.data.resultValue;
							if (callBack) {
								callBack(isVerified);
							}
						}
					}, function(errResponse) {
						/*
						DialogUtil.error('Error', errResponse, 'OK');
						if (callBack) {
							callBack(false);
						}
						*/
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
					var result = isResultSuccess(response.data, true);
					if (callBack) {
						callBack(result);
					}
				}, function(errResponse) {
					DialogUtil.error('Error', errResponse, 'OK');
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
				var result = isResultSuccess(response.data, true);
				if (callBack) {
					callBack(result);
				}
			}, function(errResponse) {
				DialogUtil.error('Error', errResponse, 'OK');
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
					var result = isResultSuccess(response.data, true);
					if (callBack) {
						callBack(result);
					}
				}, function(errResponse) {
					DialogUtil.error('Error', errResponse, 'OK');
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
				var result = isResultSuccess(response.data, true);
				if (callBack) {
					callBack(result);
				}
			}, function(errResponse) {
				DialogUtil.error('Error', errResponse, 'OK');
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
				var result = isResultSuccess(response.data, true);
				if (callBack) {
					callBack(result);
				}
			}, function(errResponse) {
				DialogUtil.error('Error', errResponse, 'OK');
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
				var result = isResultSuccess(response.data, true);
				if (callBack) {
					callBack(result);
				}
			}, function(errResponse) {
				DialogUtil.error('Error', errResponse, 'OK');
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
				var result = isResultSuccess(response.data, true);
				if (result && callBack) {
					callBack(response.data.resultValue);
				}
			}, function(errResponse) {
				DialogUtil.error('Error', errResponse, 'OK');
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
				var result = isResultSuccess(response.data, true);
				if (result && callBack) {
					callBack(result);
				}
			}, function(errResponse) {
				DialogUtil.error('Error', errResponse, 'OK');
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
				var result = isResultSuccess(response.data, true);
				if (result && callBack) {
					callBack(result);
				}
			}, function(errResponse) {
				DialogUtil.error('Error', errResponse, 'OK');
			});
			
		}
	};

}]);
