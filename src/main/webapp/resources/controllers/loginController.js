
//check login
awsmuApp.controller('LoginCtrl', function($scope, $compile, $localStorage, $location,  awsmuServices){
	
	//to fetch site constant
	awsmuServices.getConstant($scope);
	
	//Set Alert Message 
	$scope.invaalidLogin = "";
	
	// Login Form submit
	$scope.login  = function(){
		//set form submitted true to enable validation
		$scope.loginSubmitted = true;
		$('#loginForm .errorMessage').hide();
			
		if(! $scope.loginForm.$invalid) {
			//disable button
			$('#loginSubmit').val("Please wait...");
			$('#loginSubmit').attr("disabled", true);	
			
        var data = {												
        			email: $scope.loginEmail,
					password: $scope.loginPassword
			};
		var loginResponse = awsmuServices.postData("loginAction", data);
		
		loginResponse.success(function(response, status, headers, config) {
		
			var	serviceResponse = awsmuServices.responseCheckLogin(response)
			if(serviceResponse && serviceResponse.status){
				if(serviceResponse.backUrl==null){
	       		   content = angular.fromJson(serviceResponse.content);
				   // Set local storage 
	       		   $localStorage.loggedIn = true;
	       		   $localStorage.userId = content._id;
		       	   $localStorage.displayName = content.displayName;
		       	   $localStorage.profilePic = content.image;
		       	   $location.path('/dashboard');
		       	   
				}else
					$location.path(serviceResponse.backUrl);
       		              		
	   	   	}else{    	   	    
	   	   	   $('#loginForm .errorMessage p').html($compile("<span>"+serviceResponse.message+"</span>")($scope));
	   	   	   $('#loginForm .errorMessage').show();
       	   	}
	       	
	   		//enable button
			$('#loginSubmit').val("Login");
			$('#loginSubmit').attr("disabled", false);
	   	   });
	    loginResponse.error(function(data, status, headers, config) {  		        	
	            $('#login .errodashboardrMessage').show();
   	   	        $('#login .errorMessage p').html("An error occurred please try again later.");  
	      	 	//enable button
			$('#loginSubmit').val("Login");
			$('#loginSubmit').attr("disabled", false);
            });
		}   	
	}	
});


//logout
awsmuApp.controller('LogoutCtrl', function($scope, $localStorage, awsmuServices){
	var data = {};
	awsmuServices.responseCheckLogin(awsmuServices.postData("logout?i", data))
	
});
