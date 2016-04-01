/*tips list controller*/
awsmuApp.controller('tipsListCtrl', function($scope, $window, $compile, awsmuServices) {
	//to fetch site constant
	awsmuServices.getConstant($scope);

	var op = ":All;1:Yes;0:No";
	var arr = ['0','1'];
	var opreal = ":All;0:Yes;1:No";
	var sym;
	
	 $scope.patientdata = [];
	 $scope.gridapi = {};
	 $scope.config = {
				url: baseUrl+'tips/getGrid',
				datatype: "json",
				colNames: ['Tip Id', 'Tip Title', 'Tip Text', 'Problem Names', 'Gender', 'Nationality', 'Profession' ,'isActive', 'createdDate','Action'],
				colModel: [
                           {name: '_id', index: '_id', align: "center", search:true, sortable:true, formatter: function (cellvalue) {  userId = cellvalue; return cellvalue;}
    		               },
					       {name: 'tipTitle', index: 'tipTitle', align: "left", search:true, sortable:true
				           },
				           {name: 'tipText', index: 'tipText', align: "left", search:true, sortable:true
				           },
				           {name: 'problemNames', index: 'problemNames', align: "left", search:true, sortable:true
				           },
				           {name: 'gender', index: 'gender', align: "left", search:true, sortable:true
				           },
				           {name: 'nationality', index: 'nationality', align: "left", search:true, sortable:true
				           },
				           {name: 'profession', index: 'profession', align: "left", search:true, sortable:true
				           },
					       {name: 'isActive', index: 'isActive', align: "center",search:true, stype:"select",searchoptions:{
					    	   value: op,
					    	   sopt:['bw'],
					    	   dataEvents :[
								{type: 'change', fn: function(e) {
									var thisval = $(this).find('option:selected').text();
									if(thisval=='1'){sym= arr[1];}
									else{sym = arr[0];}
						       }
						 }]
						 },sortable:true,formatter: function (cellvalue) {
				                if(cellvalue==1)
                                   return 'Yes';
					             else
                                   return 'No';  
                         }},
                        {name: 'createdDate', index: 'createdDate',search:false,  align: "center",searchoptions:{sopt: ['dt']}},
                        
                        {name: '_id', index: '_id', width:"150", align: "center",search:false, sortable:false,
                            formatter: function (cellvalue) {
                                             return '<a href ="'+baseUrl+'#/tipsEdit/'+cellvalue+'">Edit</a> | <a href ="'+baseUrl+'#/tipsView/'+cellvalue+'">View</a> | <a href="javascript:void(0)" ng-click="deleteRecord(\''+cellvalue+'\')" class="deleteLink">Delete</a>';
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
	
	
	//delete records
	$scope.deleteRecord = function(tipId){	
		bootbox.confirm("Are you sure you want to delete this record?", function(result) {
			
			if(result){
				var data = {tipId:tipId};		
					
				var deleteResponse = awsmuServices.postData("tips/delete", data);
		
				deleteResponse.success(function(response, status, headers, config) {
					
					var	response = awsmuServices.responseCheckLogin(response);
					
					if(response && response.status){
						awsmuServices.showSuccess(response.message);
						$window.location.href= "#/";
						$window.location.href= "#/tips";
					}else{
						awsmuServices.showError(response.message);	
					}
				});
				
				deleteResponse.error(function(data, status, headers, config) {  		        	
					awsmuServices.showError();
				});	
			}
		});
	}
	
});





/*problem add controller*/
awsmuApp.controller('tipsAddCtrl', function($scope, awsmuServices,Upload) {
	// Get constant
	awsmuServices.getConstant($scope);
	
	$scope.errors ="";
	
	// Get form data for tip
	var response = awsmuServices.postData("tips/FormData",'');
	response.success(function(response, status, headers, config) {
		var	serviceResponse = awsmuServices.responseCheck(response)
		if(serviceResponse && serviceResponse.status){
			 content = angular.fromJson(serviceResponse.content);
			 $scope.nationalityList = content.nationalityList.value;
			 $scope.problemList =content.problemList;
			 $scope.professionList =content.professionList.value;
		}else{			
				awsmuServices.showError(response.message);
		}
	});
	response.error(function(data, status, headers, config) {  		        	
		awsmuServices.showError("An error occurred please try again later.");
    });
	
	
	
	
	
	
	
	
	$scope.tip={};
	
	// Add tip Form submit
	$scope.addTipSubmit  = function(){
		
		$scope.tipSubmitted = true;
		
		if(! $scope.tipForm.$invalid) {
			// enable loading on button
			$scope.loading= true;
			var data = {tip: JSON.stringify($scope.tip)};
			var response = awsmuServices.postData("tips/addTip",data);
			
			response.success(function(response, status, headers, config) {
				
				var	serviceResponse = awsmuServices.responseCheck(response)
				if(serviceResponse && serviceResponse.status){
		       			$scope.tipSubmitted = false;
		       			awsmuServices.showSuccess(response.message);
		       			$scope.tip={};
			   	}else{    	   	    
			   		if(response.message !="" || response.message !=null )
						awsmuServices.showError(response.message);
					if( response.errorMap !=null)
						$scope.errors = angular.fromJson(response.errorMap);
		       	}
			       	//enable button
				$scope.loading= false;
				
		   });
			
			response.error(function(data, status, headers, config) {  		        	
				awsmuServices.showError("An error occurred please try again later.");
		    });
	 }
		
	}
});





/*tip edit controller*/
awsmuApp.controller('tipsEditCtrl', function($scope, awsmuServices,Upload,$routeParams, $location) {
	awsmuServices.getConstant($scope);
	$scope.errors ="";
	
	
	//get tip detail
	var response = awsmuServices.postData("tips/getDetail/"+$routeParams.tipId, '');
	$scope.errors ="";
	
	response.success(function(response, status, headers, config) {
		var	serviceResponse = awsmuServices.responseCheck(response)
		
		if(serviceResponse && serviceResponse.status){
			 content = angular.fromJson(serviceResponse.content);
			 $scope.tip = content.tipDetails;
			 $scope.nationalityList = content.nationalityList.value;
			 $scope.problemList =content.problemList;
			 $scope.professionList =content.professionList.value;
			 
		}else{			
				awsmuServices.showError(response.message);
		}
	});
	
	response.error(function(data, status, headers, config) {  		        	
		awsmuServices.showError("An error occurred please try again later.");
    });
	
	
	
	// Edit tip Form submit
	$scope.editTipSubmit  = function(){
		$scope.tipSubmitted = true;
		if(! $scope.tipForm.$invalid) {
			// enable loading on button
			$scope.loading= true;
			
			var data = {tip: JSON.stringify($scope.tip)};
			var response = awsmuServices.postData("tips/editTip",data);
			
			
			response.success(function(response, status, headers, config) {
				var	serviceResponse = awsmuServices.responseCheck(response)
					
				if(serviceResponse && serviceResponse.status){
		       			$scope.tipSubmitted = false;
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
			
			response.error(function(data, status, headers, config) {  		        	
				awsmuServices.showError("An error occurred please try again later.");
		    });
	 }
		
	}
});



/*tip view  controller*/
awsmuApp.controller('tipsViweCtrl', function($scope, awsmuServices,$routeParams, $location) {
	awsmuServices.getConstant($scope);

	//get tip detail
	var response = awsmuServices.postData("tips/getDetail/"+$routeParams.tipId, '');
	$scope.errors ="";
	
	response.success(function(response, status, headers, config) {
		var	serviceResponse = awsmuServices.responseCheck(response)
		
		if(serviceResponse && serviceResponse.status){
			 content = angular.fromJson(serviceResponse.content);
			 $scope.tip = content.tipDetails;		
			 
		}else{			
				awsmuServices.showError(response.message);
		}
	});
	
	response.error(function(data, status, headers, config) {  		        	
		awsmuServices.showError("An error occurred please try again later.");
    });
});