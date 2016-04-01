/*message conversation  grid listing */
awsmuApp.controller('messageListCtrl', function($scope,$window,awsmuServices) {
	//to fetch site constant
	awsmuServices.getConstant($scope);
	var op = ":All;1:Yes;0:No";
	var arr = ['0','1'];
	var opreal = ":All;0:Yes;1:No";
	var sym;
	var messageId = '';
	var fromUserUserId = '';
	var toUserId = '';
	var toUserRole = '';
	var fromUserRole = '';
	 $scope.patientdata = [];
	 $scope.gridapi = {};
	 $scope.config = {
				url: baseUrl+'getMessageConversaction', 
				datatype: "json",
				colNames: ['From User Role','To User Role','From User Id','To User Id','Conversaction Id', 'Last Message', 'From User', 'To User',"Is Read",'Active' ,'createdDate','Action'],
				colModel: [
				           {name: 'fromUserRole', hidden:true,index: 'fromUserRole', align: "center", search:true, sortable:true, formatter: function (cellvalue) {  fromUserRole = cellvalue; return cellvalue;}
				           },
				           {name: 'toUserRole', hidden:true,index: 'toUserRole', align: "center", search:true, sortable:true, formatter: function (cellvalue) {  toUserRole = cellvalue; return cellvalue;}
				           },
				           {name: 'toUserId', hidden:true,index: 'toUserId', align: "center", search:true, sortable:true, formatter: function (cellvalue) {  toUserId = cellvalue; return cellvalue;}
				           },
                           {name: 'fromUserUserId', hidden:true, index: 'fromUserUserId', align: "center", search:true, sortable:true, formatter: function (cellvalue) {  fromUserUserId = cellvalue; return cellvalue;}
    		               },
    		               {name: 'chainId', index: 'chainId',width:110, align: "center", search:true, sortable:true, formatter: function (cellvalue) {  chainId = cellvalue; return cellvalue;}
    		               },
					       {name: 'message',width:250, index: 'message', align: "left", search:true, sortable:true,formatter: function (cellvalue) { return awsmuServices.addToTrusted(cellvalue) }
				           },				           
					       {name: 'fromUserDisplayName',width:110, index: 'fromUserDisplayName', align: "left", search:true, sortable:true,formatter: function (cellvalue) { return '<a target="_blank" href ="#/userView/'+fromUserUserId+'">'+cellvalue+'('+fromUserRole+')'+'</a>'; }
				           },				           
				           {name: 'toUserDisplayName',width:110, index: 'toUserDisplayName', align: "left",search:true, sortable:true,formatter: function (cellvalue) { return '<a target="_blank" href ="#/userView/'+toUserId+'">'+cellvalue+'('+toUserRole+')</a>'; }
					       },
					       {name: 'isRead',width:60, index: 'isRead', align: "center",search:true, stype:"select",searchoptions:{
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
					      {name: 'isActive',width:60, index: 'isActive', align: "center",search:true, stype:"select",searchoptions:{
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
                        {name: 'createdDate',width:105, index: 'createdDate',search:false,  align: "center",searchoptions:{sopt: ['dt']}},
                        
                        {name: 'chainId', index: 'chainId', width:100, align: "center",search:false, sortable:false,
                            formatter: function (cellvalue) {
                            	return '<a href ="'+baseUrl+'#/messageThread/'+cellvalue+'">Message Thread</a>';
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
		}
		};
	$scope.loadrecord=function(){
	 $scope.gridapi.insert($scope.modeldata);
	}
});

/* message listing controller */
awsmuApp.controller('messageThreadCtrl', function($scope,$window,$location,$routeParams,awsmuServices) {
	//to fetch site constant
	awsmuServices.getConstant($scope);
	
	//set menu selected
	//$scope.sideNav = 'profileInfo';
	$scope.chainId = $routeParams.chainId;
	$scope.messagesThread = [];
	$scope.previousAvl = true;
	$scope.messageDetail = {};
	// loading thread
	$scope.formData = {};
	$scope.formData.skipRecord = 0;
	$scope.formData.skipFreq = 10;
	var checkAdmin = true;
	$scope.getMessageThread = function(){	
		var userResponse = awsmuServices.postData("getMessageThread/"+$scope.chainId, $scope.formData);		
		userResponse.success(function(response, status, headers, config) {	
			var	response = awsmuServices.responseCheckLogin(response);		
			if(response && response.status){
		   		   content =  angular.fromJson(response.contentList);
		   		   usersData = angular.fromJson(response.content);
		   		   $scope.role = 'user';		   		   
		   		   if(checkAdmin && usersData.fromUser.userRole=='admin') {
		   			   $scope.role = 'admin';
		   			   $scope.messageDetail.fromUser = usersData.fromUser;
		   			   $scope.messageDetail.toUser = usersData.toUser;
		   		   } else if(checkAdmin && usersData.toUser.userRole=='admin'){
		   			   $scope.role = 'admin';
		   			   $scope.messageDetail.fromUser = usersData.toUser;
		   			   $scope.messageDetail.toUser = usersData.fromUser;
		   		   } else if(checkAdmin) {
		   			   $scope.messageDetail.fromUser = usersData.formUser;
		   			   $scope.messageDetail.toUser = usersData.toUser;
		   		   }		   		   
		   		   /* call emogies if not called already */ 
		   		   if(checkAdmin && $scope.role=='admin') {
		   			   awsmuServices.addEmojis('#newMessage',5);		   			   
		   		   }
		   		   checkAdmin = false;
		   		   
		   		   $scope.formData.skipRecord += $scope.formData.skipFreq;
		   		   
		   		   var loadMessages = [];
		   		   var objIndex = 0;
		   		   for ( var rowObj in content) {
		   			   content[rowObj].message = awsmuServices.addToTrusted(content[rowObj].message);
		   			   loadMessages.push(content[rowObj]);
		   			   objIndex++;
		   		   }
		   		   if (objIndex < $scope.formData.skipFreq) {
		   			   $scope.previousAvl = false;
		   		   }
		   		   for ( var rowObj in $scope.messagesThread) {
		   			   loadMessages.push($scope.messagesThread[rowObj]);
		   			   objIndex++;
		   		   }
		   		   $scope.messagesThread = [];
		   		   $scope.messagesThread = loadMessages;		   		   
			}else  
				$location.path('/messages');
		});	
		userResponse.error(function(data, status, headers, config) {  		        	
			awsmuServices.showError();
			//$location.path('/messages');
		});
	}
	$scope.errorMap = {};
	// get first thread of values
	$scope.getMessageThread();
	$scope.messageDetail.message = '';
	$scope.messageDetail.chainId = $scope.chainId;
	$scope.messageSent = false;
	$scope.messageSend = function() {
		$scope.messageSent = true;
		$scope.errorMap = {};
		if( ! $scope.messageForm.$invalid){				
			$('#messageSendButton').val("Sending.....");
			$('#messageSendButton').attr("disabled", true);
			//prepare data 
			var data = {messageDetail: JSON.stringify($scope.messageDetail)};
			var sendMsgResponse = awsmuServices.postData("sendMessage/", data);
			sendMsgResponse.success(function(response, status,headers, config) {
				var	response = awsmuServices.responseCheckLogin(response,status);
				if(!response)
					return false;
				$scope.messageSent = false;
				if ($scope.messageForm.message.$dirty != undefined) {
					$scope.messageForm.message.$dirty = false;
				}
				if(response && response.status){
					awsmuServices.showSuccess(response.message); 					
					$(".emoji-wysiwyg-editor").html("");
					var contentData = angular.fromJson(response.content);							
					//awsmuServices.addToTrusted(
					contentData.message =awsmuServices.addToTrusted(contentData.message); 
					$scope.messagesThread.push(contentData);
					$scope.formData.skipRecord++;
					$scope.messageDetail.message = '';					
				}else if(response.errorMap){					
					$scope.errorMap = {};
					$scope.errorMap=response.errorMap;
					awsmuServices.showError(response.message);
				} else {
					awsmuServices.showError(response.message);
				}
				// enable button
				$('#messageSendButton').val("Send Message");
				$('#messageSendButton').attr("disabled", false);
			});
			sendMsgResponse.error(function(data, status, headers,config) {
					$scope.messageSent = false;	
					awsmuServices.showError();
			});
			return false;
		}
			return false;
	}
});


awsmuApp.controller('messageEditCtrl', function($scope,$location,$window,$routeParams,awsmuServices) {
	//to fetch site constant
	awsmuServices.getConstant($scope);
	
	//set menu selected
	//$scope.sideNav = 'profileInfo';
	
	//get post detail
	var userResponse = awsmuServices.postData("getPostDetail/"+$routeParams.postId, '');
	
	userResponse.success(function(response, status, headers, config) {
	
		var	response = awsmuServices.responseCheckLogin(response);		
		if(response && response.status){
	   		   content =  angular.fromJson(response.content);
	   		   $scope.postDetail = content.postDetail;
	   		   $scope.trendsList = content.trendsList;
	   		   console.log($scope.postDetail);
	   		console.log($scope.trendsList);
		}else
			$location.path('/posts');	
	});
	
	$scope.submitPostEdit=function(){
		console.log(content.postDetail);
	}
	
	userResponse.error(function(data, status, headers, config) {  		        	
		awsmuServices.showError();
		$location.path('/posts');
	});	
	
});
