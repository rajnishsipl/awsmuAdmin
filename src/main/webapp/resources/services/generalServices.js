//perform common http functions
awsmuApp.factory('awsmuServices', function ($http, $location, $localStorage,$compile) {
	
	return {
		//get constant
		getConstant: function($scope){
			
			//set constant 
			$scope.baseUrl = baseUrl;
			$scope.imageUrl = imageUrl;
			$scope.viewPath = viewPath;
	    	
			//set session
			$scope.loggedIn =  $localStorage.loggedIn;
			$scope.userId = $localStorage.userId;
			$scope.displayName = $localStorage.displayName;
			$scope.profilePic = $localStorage.profilePic;
			
        },
		//ajax to fetch data from server through GET method
		getData: function(methodName){
			
	    	return $http.get(baseUrl+methodName);	
        },
        //ajax to post data to server through POST method
        postData: function(methodName, data){
        	return response =	$http({
    		    method: 'POST',
    		    url: baseUrl+methodName,
    		    //data:  decodeURIComponent($.param(data)),
    		    data:  $.param(data),
    		    headers: {'Content-Type': 'application/x-www-form-urlencoded'}
    		});
        }, 
        //check ajax response for login
        responseCheckLogin: function(response,status){
        	if(status==404)
        		return this.showError();
			//check if logged in
			if( ! response.isLoggedIn){	
				   this.localStorageFalse();
			}else if(response.code==200){
				return response;			
			}else {
				return this.showError(response.message);	
				
			}
        },
        //check ajax response without login
        responseCheck: function(response){
			if(response.code==200){
				//set user to active
				return response;
			}else
				return this.showError(response.message);
		},
        //Show error alert
        showError: function(message){  
        	
        	if(message == '' || message == undefined)
        		message = 'An error occurred please try again later.';
        	
        	$('.top-right').notify({
				message : {
					text : message
				},
				type : "danger",
				fadeOut : {
					enabled : true,
					delay : 60000
				}
			}).show();
        	return false;
        },
        //set local storage to false
        localStorageFalse: function(){
        	
      
           $localStorage.loggedIn = false;
  		   $localStorage.userId = false;
      	   $localStorage.displayName = false;
      	   $localStorage.profilePic = false;
      	   
      	   $location.path('/login');
        },
        //show success message
        showSuccess: function(message){
        	if(message == '')
        		message = 'Success.';
        	
        	$('.top-right').notify({
				message : {
					text : message
				},
				fadeOut : {
					enabled : true,
					delay : 60000
				}
			}).show();
        	return false;
        }, 
        //validate html
        addToTrusted : function(html_code) {
        	//convert to emojis if any
        	var emojis = $.emojiarea.icons;
    		for (var key in emojis) {
    			if (emojis.hasOwnProperty(key)) {
    				html_code = html_code.replace(new RegExp((key + '').replace(/([.?*+^$[\]\\(){}|-])/g, '\\$1'), 'g'),this.createIcon(key));
    			}
    		}						
    		return html_code;
    		//return html_code;
    	},
    	//convert to image from key
        createIcon : function(emoji) {
    		var filename = $.emojiarea.icons[emoji];
    		var path = $.emojiarea.path || '';
    		if (path.length && path.charAt(path.length - 1) !== '/') {
    			path += '/';
    		}
    		return '<img src="' + path + filename + '" alt="'+emoji+'" class="icon-emojis">';
    	},
    	//add emojis
	    addEmojis : function(selectionField,waitTime) {
	    	waitTime = waitTime==undefined?1000:waitTime;
	    	setTimeout(function(){
	    		$(selectionField).emojiarea({wysiwyg: true});
	    	},waitTime) ;
		}
        
	}
        
});

//add ng directives 
awsmuApp.directive('ngJqgrid', function ($window) {
    return {
        restrict: 'E',
        replace: true,
        scope: {
            config: '=',
            modeldata: '=',
            insert: '=?',
            api: '=?',
            addnew:"="
        },
        link: function (scope, element, attrs) {
        	//console.log(scope);
        	//console.log(element);
        	//console.log(attrs);
            var table, div;
            scope.$watch('config', function (value) {
                element.children().empty();
                table = angular.element('<table id="' + attrs.gridid + '"></table>');
                element.append(table);
                if (attrs.pagerid) {
                    value.pager = '#' + attrs.pagerid;
                    
                    var pager = angular.element(value.pager);
                    if (pager.length == 0) {
                        div = angular.element('<div id="' + attrs.pagerid + '"></div>');
                        element.append(div);
                    }
                }
                table.jqGrid(value);
                //if (scope.addnew == true)
                //{
                	
                    table.jqGrid("inlineNav", '#'+attrs.pagerid, { addParams: { position: "last" } });
                //}
                // Variadic API – usage:
                //   view:  <ng-jqgrid … vapi="apicall">
                //   ctrl:  $scope.apicall('method', 'arg1', …);
                scope.vapi = function () {
                    var args = Array.prototype.slice.call(arguments, 0);
                    return table.jqGrid.apply(table, args);
                };
                // allow to insert(), clear(), refresh() the grid from 
                // outside (e.g. from a controller). Usage:
                //   view:  <ng-jqgrid … api="gridapi">
                //   ctrl:  $scope.gridapi.clear();
                scope.api = {
                    insert: function (rows) {                    	
                        if (rows) {                        	
                            for (var i = 0; i < rows.length; i++) {
                                scope.modeldata.push(rows[i]);
                            }
                            table.jqGrid('setGridParam', { data: scope.modeldata })
                                 .trigger('reloadGrid');
                        }
                    },
                    clear: function () {
                        scope.modeldata.length = 0;
                        table.jqGrid('clearGridData', { data: scope.modeldata })
                            .trigger('reloadGrid');
                    },
                    refresh: function () {
                        table
                            .jqGrid('clearGridData')
                            .jqGrid('setGridParam', { data: scope.modeldata })
                            .trigger('reloadGrid');
                    }
                };
            });
            scope.$watch('data', function (value) {
                table.jqGrid('setGridParam', { data: value })
                     .trigger('reloadGrid');
            });
            scope.$watch('options', function (value) {            	
                table.jqGrid('filterToolbar', {stringResult: true,searchOnEnter : false});                    
            });
        }
    };
});


