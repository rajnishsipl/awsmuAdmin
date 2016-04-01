/*activities list controller*/
awsmuApp.controller('activitiesListCtrl', function($scope, $window, awsmuServices,$compile) {
	//to fetch site constant
	awsmuServices.getConstant($scope);

	var op = ":All;1:Yes;0:No";
	var arr = ['0','1'];
	var opreal = ":All;0:Yes;1:No";
	var sym;
	
	 $scope.patientdata = [];
	 $scope.gridapi = {};
	 $scope.config = {
				url: baseUrl+'getActivities',
				datatype: "json",
				colNames: ['Activity Id', 'Category', 'Problems Name', 'Action', 'Amount', 'Nationality', 'isActive', 'createdDate','Action'],
				colModel: [
                           {name: '_id', index: '_id', align: "center", search:true, sortable:true, formatter: function (cellvalue) {  userId = cellvalue; return cellvalue;}
    		               },
					       {name: 'category', index: 'category', align: "left", search:true, sortable:true
				           },
					       {name: 'problems', index: 'problems', align: "left", search:true, sortable:true
				           },
				           {name: 'action', index: 'action', align: "left",search:true, sortable:true
				           },
				           {name: 'amount', index: 'amount', align: "left",search:true, sortable:true 
				           },
					       {name: 'nationality', index: 'nationality', align: "left",search:true, sortable:true 
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
                                             return '<a href ="'+baseUrl+'#/activitiesEdit/'+cellvalue+'">Edit</a> | <a href ="'+baseUrl+'#/activitiesView/'+cellvalue+'">View</a> | <a href="javascript:void(0)" ng-click="deleteRecord(\''+cellvalue+'\')" class="deleteLink">Delete</a>';
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
	$scope.deleteRecord = function(activityId){	
		bootbox.confirm("Are you sure you want to delete this record?", function(result) {
			
			if(result){
				var data = {activityId:activityId};		
					
				var deleteResponse = awsmuServices.postData("activityDelete", data);
		
				deleteResponse.success(function(response, status, headers, config) {
					
					var	response = awsmuServices.responseCheckLogin(response);
					
					if(response && response.status){
						awsmuServices.showSuccess(response.message);
						$window.location.href= "#/";
						$window.location.href= "#/activities";
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


/*activities add controller*/
awsmuApp.controller('activitiesAddCtrl', function($scope, awsmuServices) {
	awsmuServices.getConstant($scope);
	var data = {};
	var response = awsmuServices.postData("getActivitiesFormData", data);
	$scope.activitiesCategoriesList='';
	
	$scope.errors ="";
	
	response.success(function(response, status, headers, config) {
		var	serviceResponse = awsmuServices.responseCheck(response)
		if(serviceResponse && serviceResponse.status){
			 content = angular.fromJson(serviceResponse.content);
			 //create country and nationality list
			 var nationalityList = [];
			 nationalityList.push("Any");
			 for(country in content.nationalityList.value){
					nationalityList.push(content.nationalityList.value[country].nationality);
			 }
			 $scope.nationalityList = nationalityList;
			 
			//create problem  list
			
			 var problemLists = [];
			 for(problem in content.problemList){ 
				 problemLists.push(content.problemList[problem].name);
			 }
			 $scope.problemList = problemLists;
			
			 
			//create activity category  list
			 
			 var activityList = [];
			 for(activity in content.activityCategoriesList){ 
				 activityList.push(content.activityCategoriesList[activity].category);
			 }
			 $scope.activitiesCategoriesList = activityList;
			 
		}else{			
				awsmuServices.showError(response.message);
			}
	});
	response.error(function(data, status, headers, config) {  		        	
		awsmuServices.showError("An error occurred please try again later.");
    });
	
	
	$scope.activity={};
	
	// Add activity Form submit
	$scope.AddActivitySubmit  = function(){
		$scope.activitySubmitted = true;
		
		if(! $scope.activityForm.$invalid) {
			// enable loading on button
			$scope.loading= true;
			 var data = {activity: JSON.stringify($scope.activity)};
			
			var response = awsmuServices.postData("addActivities",data);
			response.success(function(response, status, headers, config) {
		
				
				var	serviceResponse = awsmuServices.responseCheck(response)
					
				if(serviceResponse && serviceResponse.status){
					
		       			$scope.activitySubmitted = false;
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

/*activities edit controller*/
awsmuApp.controller('activitiesEditCtrl', function($http, $scope, $routeParams, $location, awsmuServices) {
	awsmuServices.getConstant($scope);
	
	$scope.activitiesCategoriesList='';
	$scope.errors ="";
	
	
	//get activities detail
	var activityResponse = awsmuServices.postData("getActivityDetail/"+$routeParams.activityId, '');
	
	
	activityResponse.success(function(response, status, headers, config) {
		var	serviceResponse = awsmuServices.responseCheck(response)
		if(serviceResponse && serviceResponse.status){
			 content = angular.fromJson(serviceResponse.content);
			
			// get activity data
			 $scope.activity = content.activityDetails;
			
			 
			 //create  nationality list
			 var nationalityList = [];
			 nationalityList.push("Any");
			 for(country in content.nationalityList.value){
					nationalityList.push(content.nationalityList.value[country].nationality);
			 }
			 $scope.nationalityList = nationalityList;
			 
			//create problem  list
			
			 var problemLists = [];
			 for(problem in content.problemList){ 
				 problemLists.push(content.problemList[problem].name);
			 }
			 $scope.problemList = problemLists;
			
			 
			//create activity category  list
			 
			 var activityList = [];
			 for(activityCat in content.activityCategoriesList){ 
				 activityList.push(content.activityCategoriesList[activityCat].category);
			 }
			 $scope.activitiesCategoriesList = activityList;
			 
		}else{			
				awsmuServices.showError(response.message);
				$location.path('/activities');
		}
		
	});
	
	
	
	activityResponse.error(function(data, status, headers, config) {  		        	
		awsmuServices.showError("An error occurred please try again later.");
    });
		
	
	// Update activities 
	
	$scope.updateActivitySubmit  = function(){
		$scope.activitySubmitted = true;
		
		if(! $scope.activityForm.$invalid) {
			// enable loading on button
			$scope.loading= true;
			 var data = {activity: JSON.stringify($scope.activity)};
			
			var response = awsmuServices.postData("updateActivities",data);
			response.success(function(response, status, headers, config) {
		
				
				var	serviceResponse = awsmuServices.responseCheck(response)
					
				if(serviceResponse && serviceResponse.status){
					
		       			$scope.activitySubmitted = false;
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

/*activities view controller*/
awsmuApp.controller('activitiesViweCtrl', function($http, $scope, $routeParams, awsmuServices) {
	awsmuServices.getConstant($scope);
	//get activities detail
	var activityResponse = awsmuServices.postData("getActivityDetail/"+$routeParams.activityId, '');
	
	activityResponse.success(function(response, status, headers, config) {
		var	serviceResponse = awsmuServices.responseCheck(response)
		if(serviceResponse && serviceResponse.status){
			 content = angular.fromJson(serviceResponse.content);
			 // get activity data
			 $scope.activity = content.activityDetails;
			 
		}else{			
				awsmuServices.showError(response.message);
				$location.path('/activities');
		}
	});
	
	activityResponse.error(function(data, status, headers, config) {  		        	
		awsmuServices.showError("An error occurred please try again later.");
    });
		//Do Your Code here
});