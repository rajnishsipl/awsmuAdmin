/*one post view according to postid */
awsmuApp.controller('postsViewCtrl', function($scope,$window,$location,$routeParams,awsmuServices) {
	//to fetch site constant
	awsmuServices.getConstant($scope);
	
	//set menu selected
	//$scope.sideNav = 'profileInfo';
	$scope.postId = $routeParams.postId;
	
	//get post detail
	var userResponse = awsmuServices.postData("getPostDetail/"+$routeParams.postId, '');	
	userResponse.success(function(response, status, headers, config) {	
		var	response = awsmuServices.responseCheckLogin(response);		
		if(response && response.status){
	   		   content =  angular.fromJson(response.content);
	   		   $scope.postDetail = content.postDetail;
	   		   $scope.postDetail.postContent= awsmuServices.addToTrusted($scope.postDetail.postContent);	   		
	   		   $scope.trendsList = content.trendsList;
	   		   //awsmuServices.addToTrusted(cellvalue)
	   		   //console.log($scope.postDetail);
		}else
			$location.path('/posts');	
	});	
	userResponse.error(function(data, status, headers, config) {  		        	
		awsmuServices.showError();
		$location.path('/posts');
	});		
});


awsmuApp.controller('postsEditCtrl', function($scope,$location,$window,$routeParams,awsmuServices) {
	//to fetch site constant
	awsmuServices.getConstant($scope);
	
	//get post detail
	var userResponse = awsmuServices.postData("getPostDetail/"+$routeParams.postId, '');
	
	userResponse.success(function(response, status, headers, config) {
	
		var	response = awsmuServices.responseCheckLogin(response);		
		if(response && response.status){
	   		   var content =  angular.fromJson(response.content);
	   		   $scope.postDetail = content.postDetail;
	   		   $scope.trendTitle = $scope.postDetail.trends[0]!=undefined?$scope.postDetail.trends[0].title:'';
	   		   //alert($scope.trendTitle);
	   		   $scope.trendsList = content.trendsList;
	   		   awsmuServices.addEmojis('#postContent',50);
	   		   //console.log($scope.trendsList);
		}else
			$location.path('/posts');	
	});
	
	$scope.submitPostEdit=function(){
		$scope.postSubmitted = true;		
		if( ! $scope.postForm.$invalid){
			//alert($scope.trendTitle);			
			for(var row in $scope.trendsList) {
				if($scope.trendsList[row].title==$scope.trendTitle){
					var trendData = {title:$scope.trendsList[row].title,trendId:$scope.trendsList[row]._id,icon:$scope.trendsList[row].icon};
					$scope.postDetail.trends = [];
					$scope.postDetail.trends.push(trendData);					
					break;
				}
			}
			//disable button
			$scope.loading = true;
			//set error false
			$scope.postSubmitted = false;
			//prepare data
			var data = {postDetail: JSON.stringify($scope.postDetail)};
			var userResponse = awsmuServices.postData("submitPostEdit/"+$routeParams.postId, data);
			userResponse.success(function(response, status, headers, config) {				
				var	response = awsmuServices.responseCheckLogin(response);				
				if(response && response.status){
					awsmuServices.showSuccess(response.message); 
				}else{
					awsmuServices.showError(response.message);
				}
				
				//enable button
				$scope.loading = false;
			});
			
			userResponse.error(function(data, status, headers, config) {  		        	
				awsmuServices.showError();
				
				//enable button
				$scope.loading = false;
			});	
			
		}

	}
	
	userResponse.error(function(data, status, headers, config) {  		        	
		awsmuServices.showError();
		$location.path('/posts');
	});
});


/*Post grid listing */
awsmuApp.controller('postsListCtrl', function($scope,$window,$compile,awsmuServices) {
	//to fetch site constant
	awsmuServices.getConstant($scope);
	var op = ":All;1:Yes;0:No";
	var arr = ['0','1'];
	var opreal = ":All;0:Yes;1:No";
	var sym;
	var postId = '';
	var activeInactiveStatus;
	 $scope.patientdata = [];
	 $scope.gridapi = {};
	 $scope.config = {
				url: baseUrl+'getPosts',
				datatype: "json",
				colNames: ['Post Id', 'User Name', 'Post', 'Total Comment', 'Total Likes','Active' ,'createdDate','Action'],
				colModel: [
                           {name: '_id', index: '_id',width:100, align: "center", search:true, sortable:true, formatter: function (cellvalue) {  postId = cellvalue; return cellvalue;}
    		               },
					       {name: 'user.displayName',width:100, index: 'user.displayName', align: "left", search:true, sortable:true
				           },
				           
					       {name: 'postContent',width:250, index: 'postContent', align: "left", search:true, sortable:true, formatter: function (cellvalue) { return awsmuServices.addToTrusted(cellvalue)}
				           },
				           
				           {name: 'commentCount',width:85, index: 'commentCount', align: "left",search:true, sortable:true,formatter: function (cellvalue) { return '<a href ="#/postComments/'+postId+'">'+cellvalue+'</a>'; }
					       },
					       {name: 'likeCount',width:65, index: 'likeCount', align: "left",search:true, sortable:true,formatter: function (cellvalue) { return '<a href ="#/likes:'+postId+'">'+cellvalue+'</a>'; }
					       },
				        {name: 'isActive',width:70, index: 'isActive', align: "center",search:true, stype:"select",searchoptions:{
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
							 activeInactiveStatus =  cellvalue;
				                if(cellvalue==1)
                                   return 'Yes';
					             else
                                   return 'No';  
                         }},
                        {name: 'createdDate',width:100, index: 'createdDate',search:false,  align: "center",searchoptions:{sopt: ['dt']}},
                        
                        {name: '_id', index: '_id', width:150, align: "center",search:false, sortable:false,
                            formatter: function (cellvalue) {
                            	var activeInactiveText = "InActive";
                            	if(!activeInactiveStatus==1) {
                            		activeInactiveText = "Active";
                            	} 
                            	return '<a href ="'+baseUrl+'#/postEdit/'+cellvalue+'">Edit</a> | <a href ="'+baseUrl+'#/postView/'+cellvalue+'">View</a> | <a href="javascript:void(0)" ng-click="activeInactivePost(\''+cellvalue+'\','+activeInactiveStatus+',\''+activeInactiveText+'\')" class="activeInactiveLink">'+activeInactiveText+'</a>';
                             }
                        } 
				],
		rowNum: 10,
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
			//compile ng-click code
			$compile(angular.element('.activeInactiveLink'))($scope);
		}
		};
	$scope.loadrecord=function(){
	 $scope.gridapi.insert($scope.modeldata);
	}
		
	$scope.activeInactivePost = function(postId,activeInactiveStatus,activeInactiveText) {
		bootbox.confirm("Are you sure you want to "+activeInactiveText+" this post?", function(result) {			
			if(result){
				var data = {postId:postId,active:activeInactiveStatus==1?0:1};							
				var deleteResponse = awsmuServices.postData("activeInactivePost", data);		
				deleteResponse.success(function(response, status, headers, config) {
					var	response = awsmuServices.responseCheckLogin(response);					
					if(response && response.status){
						awsmuServices.showSuccess(response.message);
						$window.location.href= "#/";
						$window.location.href= "#/posts";
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
