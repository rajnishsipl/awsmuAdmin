awsmuApp.controller('plannerGroupPostCtrl', function($scope,$window,$location,$routeParams,awsmuServices) {
	$scope.problemId = $routeParams.problemId;
		//to fetch site constant
		awsmuServices.getConstant($scope);
		var op = ":All;1:Yes;0:No";
		var arr = ['0','1'];
		var opreal = ":All;0:Yes;1:No";
		var sym;
		 $scope.patientdata = [];
		 $scope.gridapi = {};
		 $scope.config = {
					url: baseUrl+'getPlannerGroupPosts/'+$routeParams.problemId,
					datatype: "json",
					colNames: ['User Id', 'User Name', 'Post Content','Trend','Comment Count','Like Count','isActive', 'createdDate','Action'],
					colModel: [	                          
					           {name: 'user.userId', index: 'user.userId',width:"100", align: "left",search:true, sortable:true
					           },
					           {name: 'user', index: 'user.displayName', width:"150", align: "center",search:true, sortable:true,
		                            formatter: function (cellvalue) {		                            	
		                                	return '<a href ="'+baseUrl+'#/userView/'+cellvalue.userId+'"><img src="'+cellvalue.image+'" height="50" width="50"/>'+cellvalue.displayName+'</a>';
		                             }
		                        } ,
					           {name: 'postContent',width:200, index: 'postContent', align: "left",search:true, sortable:true ,
		                            formatter: function (cellvalue) {return awsmuServices.addToTrusted(cellvalue)}
					           },
					           {name: 'trends', index: 'trends.title', width:"150", align: "center",search:true, sortable:true,
		                            formatter: function (cellvalue) {
		                                return '<img src="resources/images/icon/'+cellvalue[0].icon+'" height="50" width="50"/>'+cellvalue[0].title+'';
		                             }
		                        },
		                        {name: 'commentCount',width:200, index: 'commentCount', align: "right",search:true, sortable:true},
		                        {name: 'likeCount',width:200, index: 'likeCount', align: "right",search:true, sortable:true},
		                        
						       {name: 'isActive',width:80, index: 'isActive', align: "center",search:true, stype:"select",searchoptions:{
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
	                        {name: 'createdDate', width:"100", index: 'createdDate',search:false,  align: "center",searchoptions:{sopt: ['dt']}},
	                        {name: '_id', index: '_id', width:"150", align: "center",search:false, sortable:false,
	                            formatter: function (cellvalue) {
	                            	/*<a href ="'+baseUrl+'#/problemsEdit/'+cellvalue+'">Edit</a> | | <a href="javascript:void(0)" ng-click="deleteRecord(\''+cellvalue+'\')" class="deleteLink">Delete</a>*/
	                                	return '<a target="_blank" href ="'+baseUrl+'#/postView/'+cellvalue+'">View</a>';
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
			pager: '#groupPostPager',
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
				}
			}
			};
		$scope.loadrecord=function(){
		 $scope.gridapi.insert($scope.modeldata);
		}	
});

/*Post grid listing */
awsmuApp.controller('plannerGroupListCtrl', function($scope,$window,$compile,awsmuServices) {
	//to fetch site constant
	awsmuServices.getConstant($scope);
	var op = ":All;1:Yes;0:No";
	var arr = ['0','1'];
	var opreal = ":All;0:Yes;1:No";
	var sym;
	 $scope.patientdata = [];
	 $scope.gridapi = {};
	 $scope.config = {
				url: baseUrl+'problem/getProblems',
				datatype: "json",
				colNames: ['Planner Group Title', 'Description', 'isActive', 'createdDate','Action'],
				colModel: [
                          
				           {name: 'title',width:100, index: 'title', align: "left",search:true, sortable:true
				           },
				           {name: 'description',width:200, index: 'description', align: "left",search:true, sortable:true 
				           },					    
					       {name: 'isActive',width:80, index: 'isActive', align: "center",search:true, stype:"select",searchoptions:{
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
                        {name: 'createdDate', width:"100", index: 'createdDate',search:false,  align: "center",searchoptions:{sopt: ['dt']}},
                        
                        {name: '_id', index: '_id', width:"150", align: "center",search:false, sortable:false,
                            formatter: function (cellvalue) {
                            	/*<a href ="'+baseUrl+'#/problemsEdit/'+cellvalue+'">Edit</a> | | <a href="javascript:void(0)" ng-click="deleteRecord(\''+cellvalue+'\')" class="deleteLink">Delete</a>*/
                                	return '<a href ="'+baseUrl+'#/plannerGroupView/'+cellvalue+'">View</a>';
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
			}
		}
		};
	$scope.loadrecord=function(){
	 $scope.gridapi.insert($scope.modeldata);
	}
	
	$scope.makeDatabaseChanges=function() {
		var userResponse = awsmuServices.postData("makeDatabaseChanges/", '');
		alert("Hello");
	}
	
});

awsmuApp.controller('plannerGroupMemberCtrl', function($scope,$window,$location,$routeParams,awsmuServices) {
	$scope.problemId = $routeParams.problemId;
		//to fetch site constant
		awsmuServices.getConstant($scope);
		var op = ":All;1:Yes;0:No";
		var arr = ['0','1'];
		var opreal = ":All;0:Yes;1:No";
		var sym;
		 $scope.patientdata = [];
		 $scope.gridapi = {};
		 $scope.config = {
					url: baseUrl+'getPlannerGroupMember/'+$routeParams.problemId,
					datatype: "json",
					colNames: ['User Id', 'User Name', 'Activity Points','isActive', 'createdDate','Action'],
					colModel: [	                          
					           {name: 'user.userId', index: 'user.userId',width:"100", align: "left",search:true, sortable:true
					           },
					           {name: 'user', index: 'user.displayName', width:"150", align: "center",search:true, sortable:true,
		                            formatter: function (cellvalue) {		                            	
		                                	return '<a href ="'+baseUrl+'#/userView/'+cellvalue.userId+'"><img src="'+cellvalue.image+'" height="50" width="50"/>'+cellvalue.displayName+'</a>';
		                             }
		                        } ,
					           {name: 'pointsEarned',width:200, index: 'pointsEarned', align: "left",search:true, sortable:true 
					           },					    
						       {name: 'isActive',width:80, index: 'isActive', align: "center",search:true, stype:"select",searchoptions:{
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
	                        {name: 'createdDate', width:"100", index: 'createdDate',search:false,  align: "center",searchoptions:{sopt: ['dt']}},
	                        
	                        {name: '_id', index: '_id', width:"150", align: "center",search:false, sortable:false,
	                            formatter: function (cellvalue) {
	                            	/*<a href ="'+baseUrl+'#/problemsEdit/'+cellvalue+'">Edit</a> | | <a href="javascript:void(0)" ng-click="deleteRecord(\''+cellvalue+'\')" class="deleteLink">Delete</a>*/
	                                	return '<a href ="'+baseUrl+'#/plannerGroupView/'+cellvalue+'">View</a>';
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
			pager: '#groupMemberPager',
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
				}
			}
			};
		$scope.loadrecord=function(){
		 $scope.gridapi.insert($scope.modeldata);
		}
	
});
/*one planner group view according to problem id  */
awsmuApp.controller('plannerGroupViewCtrl', function($scope,$window,$location,$routeParams,awsmuServices) {
	//to fetch site constant
	awsmuServices.getConstant($scope);	
	//set menu selected
	$scope.sideNav = 'plannerGroup';
	
	$scope.problemId = $routeParams.problemId;
	//get planner group detail 
	var userResponse = awsmuServices.postData("getplannerGroupView/"+$routeParams.problemId, '');
	//var userResponse = awsmuServices.postData("problem/getDetail/"+$routeParams.problemId, '');
	userResponse.success(function(response, status, headers, config) {
		//return false;
		var	response = awsmuServices.responseCheckLogin(response);		
		if(response && response.status){
	   		   content =  angular.fromJson(response.content);
	   		   $scope.plannerGroupDetail = content.plannerGroupDetail;
	   		   $scope.plannerGroupDetail.description = awsmuServices.addToTrusted($scope.plannerGroupDetail.description);
	   		//getPlannerGroupMembers();
		}else
			$location.path('/plannerGroup');	
	});	
	userResponse.error(function(data, status, headers, config) {  		        	
		awsmuServices.showError();
		$location.path('/plannerGroup');
	});
});



