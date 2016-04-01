//planner list controller
awsmuApp.controller('plannersListCtrl', function($scope, $location, $window, awsmuServices) {
	
	//to fetch site constant
	awsmuServices.getConstant($scope);
	
	var op = ":All;1:Yes;0:No";
	var arr = ['0','1'];
	var opreal = ":All;0:Yes;1:No";
	var sym;
	var userName = '';
	$scope.patientdata = [];
	$scope.gridapi = {};
	$scope.config = {
				url: baseUrl+'plannersList',
				datatype: "json",
				colNames: ['Planner Id', 'Area', 'Gender', 'Min Age', 'Max Age', 'Min Follow Time', 'Max Follow Time', 'Is Active', 'Created Date', 'Action'],
				colModel: [
                           {name: '_id', index: '_id', align: "center", search:true, sortable:true, width:200, formatter: function (cellvalue) {  userId = cellvalue; return cellvalue;}
    		               },
					       {name: 'area', index: 'area', align: "left", search:true, sortable:true
				           },
					       {name: 'gender', index: 'gender', align: "left", search:true, sortable:true
				           },
				           {name: 'minAge', index: 'minAge', align: "center", search:true, sortable:true
				           },
				           {name: 'maxAge', index: 'maxAge', align: "center", search:true, sortable:true
				           },
				           {name: 'minFollowTime', index: 'minFollowTime', align: "center", search:true, sortable:true
				           },
				           {name: 'maxFollowTime', index: 'maxFollowTime', align: "center", search:true, sortable:true
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
                            	return '<a href ="'+baseUrl+'#/plannerEdit/'+cellvalue+'">Edit</a> | <a href ="'+baseUrl+'#/plannerView/'+cellvalue+'">View</a>';
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
		}
	};
	
	$scope.loadrecord=function(){
		$scope.gridapi.insert($scope.modeldata);
	}
});





//User Planner edit Controller 
awsmuApp.controller('plannerEditCtrl', function($http, $scope, $routeParams, awsmuServices) {
	
	//to fetch site constant
	awsmuServices.getConstant($scope);
	
	var plannerEditResponse = awsmuServices.postData("getPlannerDetail/"+$routeParams.plannerId, '');

	plannerEditResponse.success(function(response, status, headers, config) {
			    			    
		var	response = awsmuServices.responseCheckLogin(response);
		
		if(response && response.status){
			
			content =  angular.fromJson(response.content);
			$scope.dataaaa = content;
			
			$scope.plannerDetails = content.plannerDetail;
   		   	$scope.plannerGeneralList = content.plannerGeneralList;
   		   	$scope.plannerTipsList = content.plannerTipsList;
   		   	$scope.plannerTimelineList = content.plannerTimelineList;  
   		   	$scope.plannerInfoList = content.plannerInfoList;
   		   	
   		   	$scope.timelineListLength = content.plannerTimelineList.length; 	
   		   	$scope.generalInstructions = content.generalInstructions;
   		   	$scope.tips = content.tips; 
   		   	$scope.problemDetail = content.problemDetail; 
	   		
		
			
			$scope.gender = $scope.plannerInfoList['gender'];
			$scope.isActive = $scope.plannerInfoList['isActive'];
			
			$scope.area = $scope.plannerInfoList['area'];
			$scope.nationality = $scope.plannerInfoList['nationality'];
			
			$scope.maxAge = $scope.plannerInfoList['maxAge'];
			$scope.minAge = $scope.plannerInfoList['minAge'];
			
			$scope.maxFollowTime = $scope.plannerInfoList['maxFollowTime'];
			$scope.minFollowTime = $scope.plannerInfoList['minFollowTime'];
			$scope.problemId = $scope.plannerInfoList['problemId'];
			
			$scope.natioanlityList = content.natioanlityList;
			
			
			$scope.addAny = {"iso":"","name":"Any","nicename":"Any","iso3":"","numcode":"","phonecode":"","nationality":"Any"};
			$scope.natioanlityList.value.splice(0,0,$scope.addAny);
			
			
			$scope.problemList = content.problemList;
			
		}
	});
	
	plannerEditResponse.error(function(data, status, headers, config) {  		        	
		awsmuServices.showError();
	});
	
	
	
	//Remove General Instruction
	$scope.removeGeneralInstruction = function(removeItem){
		var index = $scope.generalInstructions.indexOf(removeItem);
	    if (index != -1) 
	        $scope.generalInstructions.splice(index, 1);
	}
	
	// Add General Instruction
	$scope.addGeneralInstruction = function(){ 
		 $scope.generalInstructions.unshift('');		
	}
	
	
	//Remove Tip 
	$scope.removeTip = function(removeItem){
		var index = $scope.tips.indexOf(removeItem);
	    if (index != -1) 
	        $scope.tips.splice(index, 1);
	}
	
	// Add Tip Instruction
	$scope.addTip = function(){
		$scope.tips.unshift('');		
	}
	
	
	
	
	// Edit planner
	$scope.editPlannerSubmit= function(){
		$scope.plannerTimeLineSubmitted = true;
		if(! $scope.submitPlanner.$invalid) {
			// enable loading on button
			$scope.loading= true;
			$scope.plannerDetails.plannerActivities = $scope.plannerTimelineList;
			$scope.plannerDetails.generalInstructions = $scope.generalInstructions;
			$scope.plannerDetails.tips = $scope.tips;
			var data = {planner: JSON.stringify($scope.plannerDetails)};
			var response = awsmuServices.postData("editPlanner",data);
			
			
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
		
	
	
	// Function to submit planner information	
	$scope.editPlannerInfoSubmit= function(){
		$scope.plannerSubmitted = true;
		if( ! $scope.plannerForm.$invalid) {	
    		//disable button
			$scope.loading = true;
			//set error false
			$scope.plannerSubmitted = false;
			var config = {
					plannerId:$routeParams.plannerId,
					gender: $scope.gender,
					isActive: $scope.isActive,
					area: $scope.area,						
					nationality: $scope.nationality,
					maxAge: $scope.maxAge,					
					minAge: $scope.minAge,
					maxFollowTime: $scope.maxFollowTime,
					minFollowTime: $scope.minFollowTime,
					problemId: $scope.problemId
			};
			
			
			var plannerResponse =	$http({
			    method: 'POST',
			    url: baseUrl+"plannerInfoEdit/",
			    data: decodeURIComponent($.param(config)),
			    headers: {'Content-Type': 'application/x-www-form-urlencoded'}
			})
						
			plannerResponse.success(function(response, status, headers, config) {
				
				var	response = awsmuServices.responseCheckLogin(response);
				
				if(response && response.status){
					awsmuServices.showSuccess(response.message); 
					
				}else{
					if(response)
						awsmuServices.showError(response.message);
					else
						awsmuServices.showError();
				}
				
				//enable button
				$scope.loading = false;
			});
			
			plannerResponse.error(function(data, status, headers, config) {  		        	
				awsmuServices.showError();
				
				//enable button
				$scope.loading = false;
			});
			
			
			
		}	
		
	}
	
	
	
	
	// Function to store user edit planner 
    $scope.plannerSubmit  = function(){
    	    	
    	if( ! $scope.submitPlanner.$invalid) {	
    		
    		//disable button
			$scope.loading = true;
			
			//set error false
			$scope.plannerSubmitted = false;
			
			var plannerData = $("#submitPlanner").serialize()+'&userPlannerId=' + $routeParams.userPlannerId;
			
			var plannerResponse =	$http({
			    method: 'POST',
			    url: baseUrl+"userPlannerEdit/",
			    data: plannerData,
			    headers: {'Content-Type': 'application/x-www-form-urlencoded'}
			})
						
			plannerResponse.success(function(response, status, headers, config) {
				
				var	response = awsmuServices.responseCheckLogin(response);
				
				if(response && response.status){
					awsmuServices.showSuccess(response.message); 
					
				}else{
					if(response)
						awsmuServices.showError(response.message);
					else
						awsmuServices.showError();
				}
				
				//enable button
				$scope.loading = false;
			});
			
			plannerResponse.error(function(data, status, headers, config) {  		        	
				awsmuServices.showError();
				
				//enable button
				$scope.loading = false;
			});	
		}	
    }
	 
});

//User's planner controller 
awsmuApp.controller('userPlannersListCtrl', function($scope, $routeParams, awsmuServices) {
	
	//to fetch site constant
	awsmuServices.getConstant($scope);
	
	var op = ":All;1:Yes;0:No";
	var arr = ['0','1'];
	var opreal = ":All;0:Yes;1:No";
	var sym;
	var userName = '';
	$scope.patientdata = [];
	$scope.gridapi = {};
	$scope.config = {
				url: baseUrl+'usersPlannersList/'+$routeParams.userId,
				datatype: "json",
				colNames: ['User Planner Id', 'Problem Id', 'Name', 'Age', 'Area', 'Gender', 'Created Date', 'End Date', 'Is Active', 'Is Deleted', 'Action'],
				colModel: [
                           {name: '_id', index: '_id', align: "center", search:true, sortable:true, width:200, formatter: function (cellvalue) {  userId = cellvalue; return cellvalue;}
    		               },
    		               {name: 'problemId', index: 'problemId', align: "center", search:true, sortable:false
				           },
    		               {name: 'plannerName', index: 'plannerName', align: "left", search:true, sortable:true
				           },
				           {name: 'age', index: 'age', align: "center", search:true, sortable:true
				           },
					       {name: 'area', index: 'area', align: "left", search:true, sortable:true
				           },
					       {name: 'gender', index: 'gender', align: "left", search:true, sortable:true
				           },
				          
				           {name: 'createdDate', index: 'createdDate',search:false,  align: "center",searchoptions:{sopt: ['dt']}
				           },
				           {name: 'endDate', index: 'createdDate',search:false,  align: "center",searchoptions:{sopt: ['dt']}
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
                         {name: 'deletedByUser', index: 'deletedByUser', align: "center",search:true, stype:"select",searchoptions:{
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
                        
                        {name: '_id', index: '_id', width:"150", align: "center",search:false, sortable:false,
                            formatter: function (cellvalue) {
                            	return '<a href ="'+baseUrl+'#/userPlannerEdit/'+cellvalue+'">Edit</a> | <a href ="'+baseUrl+'#/userPlannerView/'+cellvalue+'">View</a>';
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
		}
	};
	
	$scope.loadrecord=function(){
		$scope.gridapi.insert($scope.modeldata);
	}
	 
});


//all User's planner controller 
awsmuApp.controller('allUserPlannersListCtrl', function($scope, awsmuServices) {
	
	//to fetch site constant
	awsmuServices.getConstant($scope);
	
	var op = ":All;1:Yes;0:No";
	var arr = ['0','1'];
	var opreal = ":All;0:Yes;1:No";
	var sym;
	var userName = '';
	$scope.patientdata = [];
	$scope.gridapi = {};
	$scope.config = {
				url: baseUrl+'allUsersPlannersList',
				datatype: "json",
				colNames: ['User Planner Id', 'User Id', 'Problem Id', 'Name', 'Age', 'Area', 'Gender', 'Created Date', 'End Date', 'Is Active', 'Is Deleted', 'Action'],
				colModel: [
                           {name: '_id', index: '_id', align: "center", search:true, sortable:true, width:200, formatter: function (cellvalue) {  userId = cellvalue; return cellvalue;}
    		               },
    		               {name: 'userId', index: 'userId', align: "center", search:true, sortable:false
				           },
    		               {name: 'problemId', index: 'problemId', align: "center", search:true, sortable:false
				           },
    		               {name: 'plannerName', index: 'plannerName', align: "left", search:true, sortable:true
				           },
				           {name: 'age', index: 'age', align: "center", search:true, sortable:true
				           },
					       {name: 'area', index: 'area', align: "left", search:true, sortable:true
				           },
					       {name: 'gender', index: 'gender', align: "left", search:true, sortable:true
				           },
				          
				           {name: 'createdDate', index: 'createdDate',search:false,  align: "center",searchoptions:{sopt: ['dt']}
				           },
				           {name: 'endDate', index: 'createdDate',search:false,  align: "center",searchoptions:{sopt: ['dt']}
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
                         {name: 'deletedByUser', index: 'deletedByUser', align: "center",search:true, stype:"select",searchoptions:{
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
                        
                        {name: '_id', index: '_id', width:"150", align: "center",search:false, sortable:false,
                            formatter: function (cellvalue) {
                            	return '<a href ="'+baseUrl+'#/userPlannerEdit/'+cellvalue+'">Edit</a> | <a href ="'+baseUrl+'#/userPlannerView/'+cellvalue+'">View</a>';
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
		}
	};
	
	$scope.loadrecord=function(){
		$scope.gridapi.insert($scope.modeldata);
	}
	 
});

//Planner View Controller 
awsmuApp.controller('plannerViewCtrl', function($scope, $routeParams, awsmuServices) {
	//to fetch site constant
	awsmuServices.getConstant($scope);
	
	//get planner detail
	var plannerResponse = awsmuServices.postData("getPlannerDetail/"+$routeParams.plannerId, '');
	
	plannerResponse.success(function(response, status, headers, config) {
	
		var	response = awsmuServices.responseCheckLogin(response);
		
		if(response && response.status){
   		   content =  angular.fromJson(response.content);
   		   
   		   $scope.plannerGeneralList = content.plannerGeneralList;
   		   $scope.plannerTipsList = content.plannerTipsList;
   		   $scope.plannerTimelineList = content.plannerTimelineList;  
   		   $scope.plannerInfoList = content.plannerInfoList;
   		   $scope.timelineListLength = content.plannerTimelineList.length; 	
   		   $scope.generalInstructions = content.generalInstructions;
   		   $scope.tips = content.tips; 
   		   $scope.problemDetail = content.problemDetail;
		}
	});
	
	plannerResponse.error(function(data, status, headers, config) {  		        	
		awsmuServices.showError();
	});
	 
});


//user Planner View Controller 
awsmuApp.controller('userPlannerViewCtrl', function($scope, $routeParams, awsmuServices) {
	//to fetch site constant
	awsmuServices.getConstant($scope);
	
	//get planner detail
	var plannerResponse = awsmuServices.postData("getUserPlannerDetail/"+$routeParams.userPlannerId, '');
	
	plannerResponse.success(function(response, status, headers, config) {
	
		var	response = awsmuServices.responseCheckLogin(response);
		
		if(response && response.status){
   		   content =  angular.fromJson(response.content);
   		   
   		   $scope.plannerGeneralList = content.plannerGeneralList;
   		   $scope.plannerTipsList = content.plannerTipsList;
   		   $scope.plannerTimelineList = content.plannerTimelineList;  
   		   $scope.plannerInfoList = content.plannerInfoList;
   		   $scope.timelineListLength = content.plannerTimelineList.length; 	
   		   $scope.generalInstructions = content.generalInstructions;
   		   $scope.tips = content.tips; 
   		   $scope.problemDetail = content.problemDetail; 
		}
	});
	
	plannerResponse.error(function(data, status, headers, config) {  		        	
		awsmuServices.showError();
	});
});


//Planner History Controller 
awsmuApp.controller('userPlannerHistoryCtrl', function($scope, $routeParams, awsmuServices) {
	//to fetch site constant
	awsmuServices.getConstant($scope);
	
	
	//get planner action detail
	$scope.setPageContent = function(actionDate){
		
		var actionData = { actionDate:actionDate };
		var plannerActionsResponse = awsmuServices.postData("getUserPlannerActions/"+$routeParams.userPlannerId, actionData);
	
		plannerActionsResponse.success(function(response, status, headers, config) {
				    			    
			var	response = awsmuServices.responseCheckLogin(response);
			
			if(response && response.status){
				
				 content =  angular.fromJson(response.content);
		   		   
		   		   $scope.plannerGeneralList = content.plannerGeneralList;
		   		   $scope.plannerTipsList = content.plannerTipsList;
		   		   $scope.plannerTimelineList = content.plannerTimelineList;  
		   		   $scope.plannerInfoList = content.plannerInfoList;
		   		   $scope.timelineListLength = content.plannerTimelineList.length; 	
		   		   $scope.generalInstructions = content.generalInstructions;
		   		   $scope.tips = content.tips; 
		   		   $scope.problemDetail = content.problemDetail; 
		   		   $scope.plannerActionList = content.plannerActionList;
				
				//get created date and end date
				
				var createdDateObj = new Date($scope.plannerInfoList["createdDate"]); 
				$scope.createdDate = (createdDateObj.getMonth()+1)+"/"+ createdDateObj.getDate() +"/"+createdDateObj.getFullYear();
				var endDateObj = new Date($scope.plannerInfoList["endDate"]); 
				$scope.endDate = (endDateObj.getMonth()+1)+"/"+ endDateObj.getDate() +"/"+endDateObj.getFullYear();
	
				//check if action date is not null the set created date as satrt date
				if(actionDate != null)
					$scope.dt = actionDate;
				else
					$scope.dt = $scope.createdDate;
				
				//get action day
				var actionDay =  new Date($scope.dt);
				var weekday = new Array(7);
				weekday[0]=  "Sunday";
				weekday[1] = "Monday";
				weekday[2] = "Tuesday";
				weekday[3] = "Wednesday";
				weekday[4] = "Thursday";
				weekday[5] = "Friday";
				weekday[6] = "Saturday";
				actionDay =  weekday[actionDay.getDay()];
				
				$scope.minDate = createdDateObj.getFullYear()+"-"+  ((createdDateObj.getMonth()+1).toString().length==1?+"0"+(createdDateObj.getMonth()+1).toString():(createdDateObj.getMonth()+1)) +"-"+((createdDateObj.getDate().toString().length==1)?"0"+createdDateObj.getDate().toString():createdDateObj.getDate().toString());
				$scope.maxDate =  endDateObj.getFullYear()+"-"+  ((endDateObj.getMonth()+1).toString().length==1?+"0"+(endDateObj.getMonth()+1).toString():(endDateObj.getMonth()+1)) +"-"+((endDateObj.getDate().toString().length==1)?"0"+endDateObj.getDate().toString():endDateObj.getDate().toString());
				
				var currDateObj = new Date();
				
				// get number of days
				var oneDay = 24 * 60 * 60 * 1000; // hours*minutes*seconds*milliseconds
				$scope.dayCount = Math.round(Math.abs((createdDateObj.getTime() - currDateObj.getTime())/ (oneDay)));
				
				//prepare isPerformed for general
				if($scope.plannerGeneralList.length !=0){
					for(generalList in $scope.plannerGeneralList[0].userActivities){
					
						//get the previous answer
						$scope.plannerGeneralList[0].userActivities[generalList]['isPerformed'] = ""; 	
						for(actionList in $scope.plannerActionList){					
							if($scope.plannerActionList[actionList].activityId == $scope.plannerGeneralList[0].userActivities[generalList].activityId){
								$scope.plannerGeneralList[0].userActivities[generalList]['isPerformed'] = $scope.plannerActionList[actionList].isPerformed;
							}
						}
						//check if activity belong to a specific day
						if($scope.plannerGeneralList[0].userActivities[generalList].frequency == "Weekly"){
							
							if($scope.plannerGeneralList[0].userActivities[generalList].frequencyValues.indexOf(actionDay) > -1){
								$scope.plannerGeneralList[0].userActivities[generalList]['isShow'] = true;
							}else
								$scope.plannerGeneralList[0].userActivities[generalList]['isShow'] = false;
						}
						else
							$scope.plannerGeneralList[0].userActivities[generalList]['isShow'] = true;
					}
				}
				
				//prepare isPerformed for tip
				if($scope.plannerTipsList.length !=0){
					for(tipsList in $scope.plannerTipsList[0].userActivities){
						
						//get the previous answer
						$scope.plannerTipsList[0].userActivities[tipsList]['isPerformed'] = ""; 	
						for(actionList in $scope.plannerActionList){
							if($scope.plannerActionList[actionList].activityId == $scope.plannerTipsList[0].userActivities[tipsList].activityId){
								$scope.plannerTipsList[0].userActivities[tipsList]['isPerformed'] = $scope.plannerActionList[actionList].isPerformed;
							}
						}
						//check if activity belong to a specific day
						if($scope.plannerTipsList[0].userActivities[tipsList].frequency == "Weekly"){
							
							if($scope.plannerTipsList[0].userActivities[tipsList].frequencyValues.indexOf(actionDay) > -1){
								$scope.plannerTipsList[0].userActivities[tipsList]['isShow'] = true;
							}else
								$scope.plannerTipsList[0].userActivities[tipsList]['isShow'] = false;
						}
						else
							$scope.plannerTipsList[0].userActivities[tipsList]['isShow'] = true;
					}
				}	
				
				//prepare isPerformed for timeline
				for(timelineList in $scope.plannerTimelineList){
					for(userActivity in $scope.plannerTimelineList[timelineList].userActivities){
						
						//get the previous answer
						$scope.plannerTimelineList[timelineList].userActivities[userActivity]['isPerformed'] = ""; 	
						for(actionList in $scope.plannerActionList){ 
							if($scope.plannerActionList[actionList].activityId == $scope.plannerTimelineList[timelineList].userActivities[userActivity].activityId){
								$scope.plannerTimelineList[timelineList].userActivities[userActivity]['isPerformed'] = $scope.plannerActionList[actionList].isPerformed;
							}
						}
						//check if activity belong to a specific day
						if($scope.plannerTimelineList[timelineList].userActivities[userActivity].frequency == "Weekly"){
							
							if($scope.plannerTimelineList[timelineList].userActivities[userActivity].frequencyValues.indexOf(actionDay) > -1){
								$scope.plannerTimelineList[timelineList].userActivities[userActivity]['isShow'] = true;
							}else
								$scope.plannerTimelineList[timelineList].userActivities[userActivity]['isShow'] = false;
						}
						else
							$scope.plannerTimelineList[timelineList].userActivities[userActivity]['isShow'] = true;
					}
					
				}
	    	}
	    	
	    	//generate chart after 1 sec delay to load html
			$scope.createDailyActivitiesChart($scope.plannerInfoList.totalActivity, $scope.plannerInfoList.totalYesCount, $scope.plannerInfoList.totalNoCount);	    			
	    	
		});
		
	    plannerActionsResponse.error(function(data, status, headers, config) {  		        	
			awsmuServices.showError();
		});
	}
	
	//call function on page load
	$scope.setPageContent(null);
	
	var tempDate = "";
	//call function on date change
	$scope.getPlannerAction = function(){
		//check if date are same then do call function 
		var selectedDateObj = new Date($scope.dt);
		var selectedDate = (selectedDateObj.getMonth()+1)+"/"+ selectedDateObj.getDate() +"/"+selectedDateObj.getFullYear();
		if(tempDate != selectedDate){
			
			tempDate = selectedDate;
			$scope.setPageContent(selectedDate);
			
		}
	}
	
	//submit action
    $scope.actionsSubmit = function(activityId, isPerformed, actionDate){
    	
    	//change class to active
    	$("#"+activityId+"Yes").removeClass("active");
    	$("#"+activityId+"No").removeClass("active");
    	$("#"+activityId+"Yes").attr('disabled',false);
    	$("#"+activityId+"No").attr('disabled',false);
    	$("#"+activityId+isPerformed).addClass("active");
    	$("#"+activityId+isPerformed).attr('disabled',true);
    	//total yes
    	var yesCount = parseInt($('.yes-btn.active').length);
    	//total no
    	var noCount = parseInt($('.no-btn.active').length);
    	//update daily activities chart
    	$scope.createDailyActivitiesChart($scope.plannerInfoList.totalActivity, yesCount, noCount);
    			
		var actionData = { activityId:activityId, actionDate:actionDate, isPerformed:isPerformed, yesCount:yesCount, noCount:noCount, userId: $scope.plannerInfoList.userId, userPlannerId: $scope.plannerInfoList._id};
		var actionsSubmitResponse = awsmuServices.postData("actionsSubmit/", actionData);
	
		actionsSubmitResponse.success(function(response, status, headers, config) {
			
			var	response = awsmuServices.responseCheckLogin(response);
			
			if(response ){
				if(response.status)
					awsmuServices.showSuccess(response.message);
				else
					awsmuServices.showError(response.message);
			}else{
				awsmuServices.showError();	
			}
	    	
    	});
		 
		actionsSubmitResponse.error(function(data, status, headers, config) {  		        	
			 awsmuServices.showError();
	    });	
    }
    
    
	//chart code start
   $scope.createDailyActivitiesChart = function(totalActivitiesCount, yesCount, noCount){
	  	  
	  var remainingActivitiesCount = totalActivitiesCount-yesCount-noCount;
      var chart = c3.generate({
    	    data: {
    	        columns: [
    	           
    	        ],
    	        type : 'donut',
    	        //onclick: function (d, i) { console.log("onclick", d, i); },
    	        //onmouseover: function (d, i) { console.log("onmouseover", d, i); },
    	        //onmouseout: function (d, i) { console.log("onmouseout", d, i); }
    	    },
    	    donut: {
    	        title: "Daily Activities Meter"
    	    },
    	    size: {
  	    	  width:250
  	    	}
    	});

    	setTimeout(function () { 
    	    chart.load({
    	        columns: [
    	            ["Remaining Activities:"+ remainingActivitiesCount, remainingActivitiesCount],
    	            ["Not Performed Activities:"+ noCount, noCount],
    	            ["Performed Activities:"+ yesCount, yesCount]
    	        ]
    	    });
    	}, 500);
  	}
  	//chart code end 
	 
});



//User Planner edit Controller 
awsmuApp.controller('userPlannerEditCtrl', function($http, $scope, $routeParams, awsmuServices) {
	
	//to fetch site constant
	awsmuServices.getConstant($scope);
	
	var plannerEditResponse = awsmuServices.postData("getUserPlannerDetail/"+$routeParams.userPlannerId, '');

	plannerEditResponse.success(function(response, status, headers, config) {
			    			    
		var	response = awsmuServices.responseCheckLogin(response);
		
		if(response && response.status){
			
			content =  angular.fromJson(response.content);
	   		   
   		   	$scope.plannerGeneralList = content.plannerGeneralList;
   		   	$scope.plannerTipsList = content.plannerTipsList;
   		   	$scope.plannerTimelineList = content.plannerTimelineList;  
   		   	$scope.plannerInfoList = content.plannerInfoList;
   		   	$scope.timelineListLength = content.plannerTimelineList.length; 	
   		   	$scope.generalInstructions = content.generalInstructions;
   		   	$scope.tips = content.tips; 
   		   	$scope.problemDetail = content.problemDetail; 
	   		
			$scope.plannerName = $scope.plannerInfoList['plannerName'];
			$scope.plannerDescription = $scope.plannerInfoList['plannerDescription'];
			$scope.isActive = $scope.plannerInfoList['isActive'];
			
			$scope.formats = ['dd-MMMM-yyyy', 'yyyy/MM/dd','MM/dd/yyyy', 'dd.MM.yyyy', 'shortDate'];
			$scope.format = $scope.formats[2];
			  
			var curEndDateObj = new Date($scope.plannerInfoList['endDate']);
			
			$scope.curEndDate =  curEndDateObj;curEndDateObj.getFullYear()+"-"+  ((curEndDateObj.getMonth()+1).toString().length==1?+"0"+(curEndDateObj.getMonth()+1).toString():(curEndDateObj.getMonth()+1)) +"-"+((curEndDateObj.getDate().toString().length==1)?"0"+curEndDateObj.getDate().toString():curEndDateObj.getDate().toString());
			   
			var maxDateObj = new Date($scope.plannerInfoList['plannerMaxDate']);
			$scope.maxDate =  maxDateObj.getFullYear()+"-"+  ((maxDateObj.getMonth()+1).toString().length==1?+"0"+(maxDateObj.getMonth()+1).toString():(maxDateObj.getMonth()+1)) +"-"+((maxDateObj.getDate().toString().length==1)?"0"+maxDateObj.getDate().toString():maxDateObj.getDate().toString());
			   
			var currDateObj = new Date();
			var minDateObj = new Date($scope.plannerInfoList['plannerMinDate']);
			   
			if(currDateObj > minDateObj){
				$scope.minDate = new Date();
			}else
				$scope.minDate =  minDateObj.getFullYear()+"-"+  ((minDateObj.getMonth()+1).toString().length==1?+"0"+(minDateObj.getMonth()+1).toString():(minDateObj.getMonth()+1)) +"-"+((minDateObj.getDate().toString().length==1)?"0"+minDateObj.getDate().toString():minDateObj.getDate().toString());
			
			/*Start Edit code 15-07-2015*/
			$scope.open = function($event) {
				    $event.preventDefault();
				    $event.stopPropagation();
				    if (typeof($scope.mydp) === 'undefined'){
				        $scope.mydp = {};
				    }
				    $scope.mydp.opened = true;
			 };
			 /*End Edit code 15-07-2015*/
		}
	});
	
	plannerEditResponse.error(function(data, status, headers, config) {  		        	
		awsmuServices.showError();
	});
	
	
	// Function to store user edit planner 
    $scope.plannerSubmit  = function(){
    	    	
    	if( ! $scope.submitPlanner.$invalid) {	
    		
    		//disable button
			$scope.loading = true;
			
			//set error false
			$scope.plannerSubmitted = false;
			
			var plannerData = $("#submitPlanner").serialize()+'&userPlannerId=' + $routeParams.userPlannerId;
			
			var plannerResponse =	$http({
			    method: 'POST',
			    url: baseUrl+"userPlannerEdit/",
			    data: plannerData,
			    headers: {'Content-Type': 'application/x-www-form-urlencoded'}
			})
						
			plannerResponse.success(function(response, status, headers, config) {
				
				var	response = awsmuServices.responseCheckLogin(response);
				
				if(response && response.status){
					awsmuServices.showSuccess(response.message); 
					
				}else{
					if(response)
						awsmuServices.showError(response.message);
					else
						awsmuServices.showError();
				}
				
				//enable button
				$scope.loading = false;
			});
			
			plannerResponse.error(function(data, status, headers, config) {  		        	
				awsmuServices.showError();
				
				//enable button
				$scope.loading = false;
			});	
		}	
    }
	 
});

