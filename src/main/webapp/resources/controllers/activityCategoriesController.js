/*activity categories list controller*/
awsmuApp.controller('activityCategoriesListCtrl', function($scope,$compile,$window, awsmuServices) {
	//to fetch site constant
	awsmuServices.getConstant($scope);

	var op = ":All;1:Yes;0:No";
	var arr = ['0','1'];
	var opreal = ":All;0:Yes;1:No";
	var sym;
	
	 $scope.patientdata = [];
	 $scope.gridapi = {};
	 $scope.config = {
				url: baseUrl+'activityCategories/getGrid',
				datatype: "json",
				colNames: ['Category Id', 'Name', 'isActive', 'createdDate','Action'],
				colModel: [
                           {name: '_id', index: '_id', align: "center", search:true, sortable:true, formatter: function (cellvalue) {  userId = cellvalue; return cellvalue;}
    		               },
					       {name: 'category', index: 'category', align: "left", search:true, sortable:true
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
                                             return '<a href ="'+baseUrl+'#/activityCategoriesEdit/'+cellvalue+'">Edit</a> | <a href ="'+baseUrl+'#/activityCategoriesView/'+cellvalue+'">View</a> | <a href="javascript:void(0)" ng-click="deleteRecord(\''+cellvalue+'\')" class="deleteLink">Delete</a>';
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
	$scope.deleteRecord = function(activityCategoryId){	
		bootbox.confirm("Are you sure you want to delete this record?", function(result) {
			
			if(result){
				var data = {activityCategoryId:activityCategoryId};		
					
				var deleteResponse = awsmuServices.postData("activityCategories/delete", data);
		
				deleteResponse.success(function(response, status, headers, config) {
					
					var	response = awsmuServices.responseCheckLogin(response);
					
					if(response && response.status){
						awsmuServices.showSuccess(response.message);
						//$window.location.href= "#/";
						$window.location.href= "#/activityCategories";
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


/*activity categories  add controller*/
awsmuApp.controller('activityCategoriesAddCtrl', function($scope, awsmuServices,Upload) {
	awsmuServices.getConstant($scope);
	
$scope.activityCategory={};
$scope.errors ="";	
	// Add activityCategory Form submit
	$scope.AddActivityCategorySubmit  = function(){
		$scope.activityCategoriesSubmitted = true;
		if(! $scope.activityCategoryForm.$invalid) {
			// enable loading on button
			$scope.loading= true;
				
			var data = {activityCategory: JSON.stringify($scope.activityCategory)};
		
			var response = awsmuServices.postData("activityCategories/add",data);
			
			
			response.success(function(response, status, headers, config) {
		
				
				var	serviceResponse = awsmuServices.responseCheck(response)
					
				if(serviceResponse && serviceResponse.status){
					
		       			$scope.activityCategoriesSubmitted = false;
		       			awsmuServices.showSuccess(response.message);
			   	}else{    	   	    
			   		if(response.message !="" || response.message !=null )
						awsmuServices.showError(response.message);
					if( response.errorMap !=null)
						$scope.errors = angular.fromJson(response.errorMap);
		       	}
			       	//enable button
				$scope.loading= false;
				$scope.activityCategory={};
		   });
			
			response.error(function(data, status, headers, config) {  		        	
				awsmuServices.showError("An error occurred please try again later.");
		    });
	 }
		
	}
});



/*activity categories  edit controller*/
awsmuApp.controller('activityCategoriesEditCtrl', function($scope, awsmuServices,$routeParams,Upload) {
	awsmuServices.getConstant($scope);
	
	$scope.errors ="";
	//get category detail
	var response = awsmuServices.postData("activityCategories/getDetail/"+$routeParams.activityCategoryId, '');
	
	response.success(function(response, status, headers, config) {
		var	serviceResponse = awsmuServices.responseCheck(response)
		if(serviceResponse && serviceResponse.status){
			 content = angular.fromJson(serviceResponse.content);
			 $scope.activityCategory = content.activityCategoryDetails;
			 
		}else{			
				awsmuServices.showError(response.message);
		}
	});
	
	response.error(function(data, status, headers, config) {  		        	
		awsmuServices.showError("An error occurred please try again later.");
    });
	
	
	// update activityCategory Form submit
	$scope.updateActivityCategorySubmit  = function(){
		
		$scope.activityCategoriesSubmitted = true;
		if(! $scope.activityCategoryForm.$invalid) {
			// enable loading on button
			$scope.loading= true;
				
			var data = {activityCategory: JSON.stringify($scope.activityCategory)};
		
			var response = awsmuServices.postData("activityCategories/update",data);
			
			
			response.success(function(response, status, headers, config) {
		
				
				var	serviceResponse = awsmuServices.responseCheck(response)
					
				if(serviceResponse && serviceResponse.status){
					
		       			$scope.activityCategoriesSubmitted = false;
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


/*activity categories  view controller*/
awsmuApp.controller('activityCategoriesViweCtrl', function($scope,$routeParams, awsmuServices,Upload) {
	awsmuServices.getConstant($scope);
	
	//get category detail
	var response = awsmuServices.postData("activityCategories/getDetail/"+$routeParams.activityCategoryId, '');
	
	response.success(function(response, status, headers, config) {
		var	serviceResponse = awsmuServices.responseCheck(response)
		if(serviceResponse && serviceResponse.status){
			 content = angular.fromJson(serviceResponse.content);
			 $scope.activityCategory = content.activityCategoryDetails;
			 
		}else{			
				awsmuServices.showError(response.message);
		}
	});
	
	response.error(function(data, status, headers, config) {  		        	
		awsmuServices.showError("An error occurred please try again later.");
    });
});