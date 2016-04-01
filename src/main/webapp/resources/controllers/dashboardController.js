//dashboard
awsmuApp.controller('dashboardCtrl', function($scope, $localStorage, awsmuServices){
	//to fetch site constant
	awsmuServices.getConstant($scope);
	var dashboardResponse = awsmuServices.getData("dashboard");
	dashboardResponse.success(function(response, status, headers, config) {			
		var	response = awsmuServices.responseCheckLogin(response);		
		if(response && response.status){
			var	response = awsmuServices.responseCheckLogin(response);			
			if(response && response.status){				
				 content =  angular.fromJson(response.content);
			   	 $scope.totalUsersCount = content.totalUsersCount;
				 $scope.latestUserList = content.latestUserList;
			   	 $scope.unreadMessagesCount = content.unreadMessagesCount;
			   	 $scope.messagesList = content.messagesList;
			   	 for(var indexVal in $scope.messagesList) {
			   		$scope.messagesList[indexVal].message = awsmuServices.addToTrusted($scope.messagesList[indexVal].message); 
			   	 }
			   	 $scope.totalPlannersCount = content.totalPlannersCount;
			   	 $scope.plannersList = content.plannersList;
			   	 $scope.totalUserPlannersCount = content.totalUserPlannersCount;
			   	 $scope.userPlannersList = content.userPlannersList;
			   	 $scope.totalExpertRequestsCount = content.totalExpertRequestsCount;
			   	 $scope.expertRequestsList = content.expertRequestsList;
			}		
		}else{
			if(response)
				awsmuServices.showError(response.message);
			else
				awsmuServices.showError();
		}
	});	
	dashboardResponse.error(function(data, status, headers, config) {  		        	
		awsmuServices.showError();		
	});				
});
