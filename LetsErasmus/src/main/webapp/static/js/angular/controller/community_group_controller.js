App.controller('communityGroupCtrl', [ '$scope', '$controller', 'communityService',
		function($scope, $controller, communityService) {
			var self = this;
			self.countryId = $('#cmbCountry').val();
			self.communityGroupSubUrl = $('#cmbCommunityGroup').val();
			self.topicList = [];

			$( function() {
			    $.widget( "custom.combobox", {
			      _create: function() {
			        this.wrapper = $( "<span>" )
			          .addClass( "custom-combobox" )
			          .insertAfter( this.element );
			 
			        this.element.hide();
			        this._createAutocomplete();
			        this._createShowAllButton();
			      },
			 
			      _createAutocomplete: function() {
			        var selected = this.element.children( ":selected" ),
			          value = selected.val() ? selected.text() : "";
			 
			        this.input = $( "<input>" )
			          .appendTo( this.wrapper )
			          .val( value )
			          .attr( "title", "" )
			          .addClass( "custom-combobox-input ui-widget ui-widget-content ui-state-default ui-corner-left" )
			          .autocomplete({
			            delay: 0,
			            minLength: 0,
			            source: $.proxy( this, "_source" )
			          })
			          .tooltip({
			            classes: {
			              "ui-tooltip": "ui-state-highlight"
			            }
			          });
			 
			        this._on( this.input, {
			          autocompleteselect: function( event, ui ) {
			            ui.item.option.selected = true;
			            this._trigger( "select", event, {
			              item: ui.item.option
			            });
			          },
			 
			          autocompletechange: "_removeIfInvalid"
			        });
			      },
			 
			      _createShowAllButton: function() {
			        var input = this.input,
			          wasOpen = false;
			 
			        $( "<a>" )
			          .attr( "tabIndex", -1 )
			          .attr( "title", "Show All Items" )
			          .tooltip()
			          .appendTo( this.wrapper )
			          .button({
			            icons: {
			              primary: "ui-icon-triangle-1-s"
			            },
			            text: false
			          })
			          .removeClass( "ui-corner-all" )
			          .addClass( "custom-combobox-toggle ui-corner-right" )
			          .on( "mousedown", function() {
			            wasOpen = input.autocomplete( "widget" ).is( ":visible" );
			          })
			          .on( "click", function() {
			            input.trigger( "focus" );
			 
			            // Close if already visible
			            if ( wasOpen ) {
			              return;
			            }
			 
			            // Pass empty string as value to search for, displaying all results
			            input.autocomplete( "search", "" );
			          });
			      },
			 
			      _source: function( request, response ) {
			        var matcher = new RegExp( $.ui.autocomplete.escapeRegex(request.term), "i" );
			        response( this.element.children( "option" ).map(function() {
			          var text = $( this ).text();
			          if ( this.value && ( !request.term || matcher.test(text) ) )
			            return {
			              label: text,
			              value: text,
			              option: this
			            };
			        }) );
			      },
			 
			      _removeIfInvalid: function( event, ui ) {
			 
			        // Selected an item, nothing to do
			        if ( ui.item ) {
			          return;
			        }
			 
			        // Search for a match (case-insensitive)
			        var value = this.input.val(),
			          valueLowerCase = value.toLowerCase(),
			          valid = false;
			        this.element.children( "option" ).each(function() {
			          if ( $( this ).text().toLowerCase() === valueLowerCase ) {
			            this.selected = valid = true;
			            return false;
			          }
			        });
			 
			        // Found a match, nothing to do
			        if ( valid ) {
			          return;
			        }
			 
			        // Remove invalid value
			        this.input
			          .val( "" )
			          .attr( "title", value + " didn't match any item" )
			          .tooltip( "open" );
			        this.element.val( "" );
			        this._delay(function() {
			          this.input.tooltip( "close" ).attr( "title", "" );
			        }, 2500 );
			        this.input.autocomplete( "instance" ).term = "";
			      },
			 
			      _destroy: function() {
			        this.wrapper.remove();
			        this.element.show();
			      }
			    });
			 
			    $( "#combobox" ).combobox();
			    $( "#toggle" ).on( "click", function() {
			      $( "#combobox" ).toggle();
			    });
			  } );
			
			self.initialize = function() {
				self.topicList = [{id: 1, title: 'First topic of Erasmus', createdDate: new Date()}, 
				                  {id: 2, title: 'Second topic of Erasmus', createdDate: new Date()}];
				//self.listTopic;
			};

			self.listTopic = function() {
				var listCallBack = function(receivedTopicList) {
					self.topicList = receivedTopicList;
				};
				communityService.listTopic(self.selectedCityId, listCallBack);
			};
			
			self.btnCreateTopicClick = function() {
				if (loginUserId != null && loginUserId != '') {
					ajaxHtml(webApplicationUrlPrefix + '/static/html/CommunityTopic.htm', 'divCommonModal', function() {
							$('#hiddenDialogTopicGroupId').val(globalCommunityGroupId);
							$('#btnDialogSubmitTopic').click(self.submitTopicCallBack);
						}
					);
				} else {
					openLoginWindow();
				}
			};
			
			self.btnEditTopicClick = function(id, communityGroupId) {
				if (loginUserId != null && loginUserId != '') {
					ajaxHtml(webApplicationUrlPrefix + '/static/html/CommunityTopic.htm', 'divCommonModal', function() {
							$('#hiddenDialogTopicId').val(id);
							$('#hiddenDialogTopicGroupId').val(communityGroupId);
							if (id != null && id != '') {
								var title = $('#hiddenTopicTitle_' + id).val()
								var description = $('#hiddenTopicDescription_' + id).val()
								$("#txtDialogTopicTitle").val(title);
								$("#txtDialogTopicDescription").val(description);
								$("#txtDialogTopicTitle").attr('disabled','disabled');
							}
							$('#btnDialogSubmitTopic').click(self.submitTopicCallBack);
						}
					);
				} else {
					openLoginWindow();
				}
			};
			
			self.submitTopicCallBack = function() {
				var callBack = function(operationResult) {
					if (isResultSuccess(operationResult)) {
						DialogUtil.success(operationResult.resultDesc, function() {
							 reloadPage()
						 });
					} else {
						hideModal('divCommonModal');
					}
				};
				var topicId = $('#hiddenDialogTopicId').val();
				var communityGroupId = $('#hiddenDialogTopicGroupId').val();
				var title = $('#txtDialogTopicTitle').val();
				var description = $('#txtDialogTopicDescription').val();
				
				communityService.createUpdateTopic(topicId, communityGroupId, title, description, callBack);
				
			};
			
			self.onCountryChange = function() {
				communityService.listCommunityGroup(self.countryId, function(groupList) {
		  			$('#cmbCommunityGroup').find('option:not(:first)').remove();
		  			if (groupList != null && groupList.length > 0) {
		  				for(var i = 0; i < groupList.length; i++) {
		  	  				$('#cmbCommunityGroup').append('<option value="' + groupList[i].subUrl + '">' + groupList[i].name + '</option>');
		  	  			}
		  			}
		  			self.communityGroupSubUrl = '-1';
		  		});
		  	};
		  	
		  	self.onCommunityGroupChange = function(option) {
		  		var url = WebUtil.concatUrl([webApplicationUrlPrefix, '/community/', self.communityGroupSubUrl]);
		  		openWindow(url, true);
		  	};
		  	
			self.initialize();

		} ]);