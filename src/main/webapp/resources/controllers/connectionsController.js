/*activities list controller*/
awsmuApp.controller('connectionsListCtrl', function($scope, $window, awsmuServices,$compile) {
	//to fetch site constant
	awsmuServices.getConstant($scope);

	var op = ":All;1:Yes;0:No";
	var arr = ['0','1'];
	var opreal = ":All;0:Yes;1:No";
	var sym;
	
	 $scope.patientdata = [];
	 $scope.gridapi = {};
	 $scope.config = {
				url: baseUrl+'connections/getGrid',
				datatype: "json",
				colNames: ['Connection Id', 'To User', 'From User', 'Status', 'createdDate','Action'],
				colModel: [
                           {name: '_id', index: '_id', align: "center", search:true, sortable:true, formatter: function (cellvalue) {  userId = cellvalue; return cellvalue;}
    		               },
					       {name: 'toUser.displayName', index: 'toUser.displayName', align: "left", search:true, sortable:true
				           },
					       {name: 'fromUser.displayName', index: 'fromUser.displayName', align: "left", search:true, sortable:true
				           },
				           {name: 'status', index: 'status', align: "left",search:true, sortable:true
				           },
				           {name: 'createdDate', index: 'createdDate',search:false,  align: "center",searchoptions:{sopt: ['dt']}},
                        
				           {name: '_id', index: '_id', width:"150", align: "center",search:false, sortable:false,
				        	   formatter: function (cellvalue) {
                                       return '<a href ="'+baseUrl+'#/connectionEdit/'+cellvalue+'">Edit</a> | <a href ="'+baseUrl+'#/connectionView/'+cellvalue+'">View</a> | <a href="javascript:void(0)" ng-click="deleteRecord(\''+cellvalue+'\')" class="deleteLink">Delete</a>';
				        	   }
				           } 
				],
		rowNum: 11,
		rowList: [10, 20,30,40],
		viewrecords: true,
		sortorder: 'desc',
		height: '100%',
		loadonce:false,
		autoencode: false,
		mtype: "POST",
		rownumbers: true,
		rownumWidth: 30,
		gridview: true,
		pager: '#pager',
		sortname: 'createdDate',
		multiselect:false,
		filterToolbar: true,
		caption: "",
		ignoreCase: true,
		autowidth: true,
		loadComplete:function(data,status){			
			if(!data.isLoggedIn) {
				awsmuServices.localStorageFalse();
				awsmuServices.showError(data.message);
				$window.location.href= "#/login";		        
		       // return false;
			}
			
			//compile ng-click code
			$compile(angular.element('.deleteLink'))($scope);
		}
		};
	$scope.loadrecord=function(){
	 $scope.gridapi.insert($scope.modeldata);
	}
});






/*activities list controller*/
awsmuApp.controller('userConnectionsListCtrl', function($scope, $window, $routeParams,awsmuServices,$compile) {
	//to fetch site constant
	awsmuServices.getConstant($scope);

	var op = ":All;1:Yes;0:No";
	var arr = ['0','1'];
	var opreal = ":All;0:Yes;1:No";
	var sym;
	
	 $scope.patientdata = []; //userId
	 $scope.gridapi = {};
	 $scope.config = {
				url: baseUrl+'connections/getUserConnectionGrid'+$routeParams.userId,
				datatype: "json",
				colNames: ['Connection Id', 'To User', 'From User', 'Status', 'createdDate','Action'],
				colModel: [
                           {name: '_id', index: '_id', align: "center", search:true, sortable:true, formatter: function (cellvalue) {  userId = cellvalue; return cellvalue;}
    		               },
					       {name: 'toUser.displayName', index: 'toUser.displayName', align: "left", search:true, sortable:true
				           },
					       {name: 'fromUser.displayName', index: 'fromUser.displayName', align: "left", search:true, sortable:true
				           },
				           {name: 'status', index: 'status', align: "left",search:true, sortable:true
				           },
				           {name: 'createdDate', index: 'createdDate',search:false,  align: "center",searchoptions:{sopt: ['dt']}},
                        
				           {name: '_id', index: '_id', width:"150", align: "center",search:false, sortable:false,
				        	   formatter: function (cellvalue) {
                                       return '<a href ="'+baseUrl+'#/connectionEdit/'+cellvalue+'">Edit</a> | <a href ="'+baseUrl+'#/connectionView/'+cellvalue+'">View</a> | <a href="javascript:void(0)" ng-click="deleteRecord(\''+cellvalue+'\')" class="deleteLink">Delete</a>';
				        	   }
				           } 
				],
		rowNum: 11,
		rowList: [10, 20,30,40],
		viewrecords: true,
		sortorder: 'desc',
		height: '100%',
		loadonce:false,
		autoencode: false,
		mtype: "POST",
		rownumbers: true,
		rownumWidth: 30,
		gridview: true,
		pager: '#pager',
		sortname: 'createdDate',
		multiselect:false,
		filterToolbar: true,
		caption: "",
		ignoreCase: true,
		autowidth: true,
		loadComplete:function(data,status){			
			if(!data.isLoggedIn) {
				awsmuServices.localStorageFalse();
				awsmuServices.showError(data.message);
				$window.location.href= "#/login";		        
		       // return false;
			}
			
			//compile ng-click code
			$compile(angular.element('.deleteLink'))($scope);
		}
		};
	$scope.loadrecord=function(){
	 $scope.gridapi.insert($scope.modeldata);
	}
});















/*connection view  controller*/
awsmuApp.controller('connectionsViewCtrl', function($scope, awsmuServices,$routeParams, $location) {
	awsmuServices.getConstant($scope);
	//get connection detail
	var response = awsmuServices.postData("connections/getDetail/"+$routeParams.connectionId, '');
	
	
	response.success(function(response, status, headers, config) {
		var	serviceResponse = awsmuServices.responseCheck(response)
		if(serviceResponse && serviceResponse.status){
			 content = angular.fromJson(serviceResponse.content);
			 $scope.connection = content.connectionDetails;
		}else{			
				awsmuServices.showError(response.message);
		}
	});
	
	response.error(function(data, status, headers, config) {  		        	
		awsmuServices.showError("An error occurred please try again later.");
    });
});





/*connections edit controller*/
awsmuApp.controller('connectionsEditCtrl', function($http, $scope, $routeParams, $location, awsmuServices) {
	awsmuServices.getConstant($scope);
	
	//get connection detail
	var response = awsmuServices.postData("connections/getDetail/"+$routeParams.connectionId, '');
	
	
	response.success(function(response, status, headers, config) {
		var	serviceResponse = awsmuServices.responseCheck(response)
		if(serviceResponse && serviceResponse.status){
			 content = angular.fromJson(serviceResponse.content);
			 $scope.connection = content.connectionDetails;
		}else{			
				awsmuServices.showError(response.message);
		}
	});
	
	response.error(function(data, status, headers, config) {  		        	
		awsmuServices.showError("An error occurred please try again later.");
    });
		
	
	// Update connections 
	
	$scope.editConnectionSubmit  = function(){
		$scope.connectionSubmitted = true;
		
		if(! $scope.connectionForm.$invalid) {
			// enable loading on button
			$scope.loading= true;
			 var data = {connection: JSON.stringify($scope.connection)};
			
			var response = awsmuServices.postData("connections/update",data);
			response.success(function(response, status, headers, config) {
				var	serviceResponse = awsmuServices.responseCheck(response)
		
				if(serviceResponse && serviceResponse.status){
		       			$scope.connectionSubmitted = false;
		       			awsmuServices.showSuccess(response.message);
			   	}else{    	   	    
			   		if(response.message !="" || response.message !=null )
						awsmuServices.showError(response.message);
					if( response.errorMap !=null)
						$scope.errors = angular.fromJson(response.errorMap);
		       	}
			       	//enable button
				$scope.loading= false;
				
		   });
	 }
		
	}
	
});