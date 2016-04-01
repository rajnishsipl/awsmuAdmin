//list controller
awsmuApp.controller('usersListCtrl', function($http, $scope, $location, $window, $compile, awsmuServices) {
	
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
				url: baseUrl+'getUsers',
				datatype: "json",
				colNames: ['User Id', 'Name', 'Display Name', 'Username', 'Email', 'Is Active', 'Created Date','Action'],
				colModel: [
	                       {name: '_id', index: '_id', align: "center", search:true, sortable:true, formatter: function (cellvalue) {  userId = cellvalue; return cellvalue;}
			               },
					       {name: 'name', index: 'name', align: "left", search:true, sortable:true
				           },
					       {name: 'displayName', index: 'displayName', align: "left", search:true, sortable:true
				           },
				           {name: 'username', index: 'username', align: "left",search:true, sortable:true,
						      formatter: function (cellvalue) {
						    	  return cellvalue;					      
						      }
				           },
				           {name: 'email', index: 'email', align: "left",search:true, sortable:true,formatter: function (cellvalue) { return '<a href ="mailto:'+cellvalue+'">'+cellvalue+'</a>'; }
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
	                        	return '<a href ="'+baseUrl+'#/userEdit/'+cellvalue+'">Edit</a> | <a href ="'+baseUrl+'#/userView/'+cellvalue+'">View</a> | <a href="javascript:void(0)" ng-click="deleteUser(\''+cellvalue+'\')" class="deleteLink">Delete</a>';
	                        	
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
		loadComplete:function(data, status){			
			if(!data.isLoggedIn) {
				awsmuServices.localStorageFalse();
				awsmuServices.showError(data.message);
				$window.location.href= "#/login";		        
			}
			//compile ng-click code
			$compile(angular.element('.deleteLink'))($scope);
		}
	};
	
	$scope.loadrecord=function(){
	 $scope.gridapi.insert($scope.modeldata);
	}
	
	//delete user
	$scope.deleteUser = function(userId){	
		bootbox.confirm("Are you sure you want to delete this user?", function(result) {
			
			if(result){
				var data = {userId:userId};		
					
				var deleteResponse = awsmuServices.postData("deleteUser", data);
		
				deleteResponse.success(function(response, status, headers, config) {
					
					var	response = awsmuServices.responseCheckLogin(response);
					
					if(response && response.status){
						awsmuServices.showSuccess(response.message);
						$window.location.href= "#/";
						$window.location.href= "#/users";
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

//edit controller
awsmuApp.controller('userEditCtrl', function($http, $scope, $routeParams, Upload, awsmuServices) {
	
	//to fetch site constant
	awsmuServices.getConstant($scope);
	
	//visiblity drop down
    /*Data for dropdown*/
	$scope.visibilityOptions = ['Only me', 'Public', 'Friends', 'Planner group', 'Friends & Planner group' ];
	$scope.visibilityIcons = ['','fa-lock', 'fa-globe', 'fa-user-plus', 'fa-user-times', 'fa-group' ];
	
	
	//get user detail
	var userResponse = awsmuServices.postData("getUserDetail/"+$routeParams.userId, '');
	
	userResponse.success(function(response, status, headers, config) {
	
		var	response = awsmuServices.responseCheckLogin(response);
		
		if(response && response.status){
   		   content =  angular.fromJson(response.content);
   		   $scope.userDetail = content.userDetail;
   		   $scope.nationalities = content.nationalityList.value;
   		   $scope.professionList = content.professionList.value;
   		   $scope.degreeCoursesList = content.degreeCoursesList.value;
	   	   $scope.specialtieslist = content.specialtyList.value;
	   		   
	   		   //check if image is exits image  to show remove button
		   var picUrl = $scope.userImageUrl = $scope.userDetail.image;
	   	   if(picUrl.split('/').pop() != 'profile_no_pic.png'){
				$scope.imageExists = true;
			}
   					
			//set username
			if($scope.username !=""){
				$scope.publicLink =  $scope.userDetail.username;
				
			}else{
				$scope.publicLink =  $scope.userDetail._id;
			}
			
			//set cities list
			$scope.getCities($scope.userDetail.country);
			
			//set user attributes
			$scope.attributesList = content.userAttributesList;
			$scope.questionsList =  content.attrQuestionsList;
			var attrCount = 0; 
			for(attr in $scope.attributesList){
				attrCount = attrCount+1;
				
				$scope.attributesList[attr]['options'] = $scope.questionsList[attr].options;	
				$scope.attributesList[attr]['optionsName'] = "optionsName"+attrCount;
				$scope.attributesList[attr]['attrId'] = "attrId"+attrCount;
			}
			$scope.attrCount = attrCount;
		}
	});
	
	userResponse.error(function(data, status, headers, config) {  		        	
		awsmuServices.showError();
	});	
	
	//edit detail
	$scope.profileSubmit = function(){
		
		$scope.profileSubmitted = true;
		
		if( ! $scope.profileForm.$invalid){		
			
			//disable button
			$scope.loading = true;
			//set error false
			$scope.profileSubmitted = false;
			
			//this fields are setting through js so need to add here
			$scope.userDetail.address = $("#address").val();
			$scope.userDetail.longitude = $("#longitude").val();
			$scope.userDetail.latitude = $("#latitude").val();
			
			//prepare data
			var data = {userDetail: JSON.stringify($scope.userDetail)};
			var userResponse = awsmuServices.postData("editUser", data);
			
			userResponse.success(function(response, status, headers, config) {
				
				var	response = awsmuServices.responseCheckLogin(response);
				
				if(response && response.status){
					awsmuServices.showSuccess(response.message); 
					//ActivitiesModel activityModel = new Gson().fromJson(activity, ActivitiesModel.class);
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
	
	//edit attributes
	$scope.attrSubmit = function(){
		
		//disable button
		$scope.attrloading = true;
						
		//prepare data
		var data = $("#attrForm").serialize();
		
		var attrResponse =	$http({
		    method: 'POST',
		    url: baseUrl+"saveUserAttributes/",
		    data: data,
		    headers: {'Content-Type': 'application/x-www-form-urlencoded'}
		})
		
		attrResponse.success(function(response, status, headers, config) {
			
			var	response = awsmuServices.responseCheckLogin(response);
			
			if(response && response.status){
				awsmuServices.showSuccess(response.message); 
			}else{
				awsmuServices.showError(response.message);
			}
			
			//enable button
			$scope.attrloading = false;
		});
		
		attrResponse.error(function(data, status, headers, config) {  		        	
			awsmuServices.showError();
			
			//enable button
			$scope.attrloading = false;
		});			
	}
	
	//get cities drop down
	$scope.getCities = function(){	
		if(content != undefined){
			var countryData = { country:$scope.userDetail.country };
			
			var cityListResponse = awsmuServices.postData("getCitiesList/", countryData);
			
			cityListResponse.success(function(response, status, headers, config) { 
				var cityList = [];
				if(response.status){				
					content = angular.fromJson(response.content);   			
    		
					if(content.citiesList != null){
						for(city in content.citiesList.cities){
							cityList.push(content.citiesList.cities[city]);
						}
    			
						$scope.cityList = cityList;
					}else $scope.cityList = cityList;
    			
				}else{
					$scope.cityList = cityList;   						
				}
			});
		}
	}
	
	//upload image parameter
	$scope.myImage='';
    $scope.myCroppedImage='';
    
    //create cropped image
    var handleFileSelect = function(evt) {
   	
    	//validate file
    	var file = evt.currentTarget.files[0];
   	
       //check image dimension writing code inside it 
    	var _URL = window.URL || window.webkitURL;
    	var image;
    	image = new Image();  
    	image.onload = function() {  
    		
    		this.width;
    		this.height;
       
    		if(file.name != ''){
   			
    			if ( ! file.name.match(/(\.jpg|\.png|\.JPG|\.PNG|\.jpeg|\.JPEG)$/)){
    				awsmuServices.showError('Please select image in .jpg, .png, .jpeg format.');
	   				return false;
   				
    			}else if ((parseInt(file.size) / 1024) > 4816) {
   				 
    			 awsmuServices.showError('Image size must be 4 MB or below.');
   				 return false;
               }else if (parseInt(this.width) < 500 || parseInt(this.height) < 350) {
               	
               	 awsmuServices.showError('Image dimension must be 500 X 350.');
               	 return false;
               }
   		}else{
   				awsmuServices.showError('Please select an image.');
   				return false;
   		}
   		
   		//show image to crop      	
       	var reader = new FileReader();
       	reader.onload = function (evt) {
       		 $scope.$apply(function($scope){
       			 $scope.myImage=evt.target.result;
       		 });
       	};
       	reader.readAsDataURL(file);
       	
       	$scope.cropSection = true;
       	
       };
       image.src = _URL.createObjectURL(file);
    };
    
    angular.element(document.querySelector('#file')).on('change', handleFileSelect);
	
    // Converts data uri to Blob. Necessary for uploading.
    var dataURItoBlob = function(dataURI) {
   	 
		   var binary = atob(dataURI.split(',')[1]);
		   var mimeString = dataURI.split(',')[0].split(':')[1].split(';')[0];
		   var array = [];
		   for(var i = 0; i < binary.length; i++) {
		     array.push(binary.charCodeAt(i));
		   }
		   return new Blob([new Uint8Array(array)], {type: mimeString});
    };
    
   //upload cropped file
	$scope.uploadProfilePic  = function(){
		
		//disable  button
		$scope.cropSection = false;
		$scope.uploading = true;
		$scope.uploadAction = true;
				
		var file = dataURItoBlob($scope.myCroppedImage);
	
       file.upload = Upload.upload({
       		url: baseUrl+'uploadProfilePic',
       		method: 'POST',
       		headers: {
       			'Content-Type': file.type
       		},
       		file: file,
       		fields: {userId: $scope.userDetail._id},
       		fileFormDataName: 'file'
       });

       file.upload.then(function (response) {
    	
    	    var	response = awsmuServices.responseCheckLogin(response.data);
			
			if(response){
				if(response.status) {
					
					content = angular.fromJson(response.content);  
					
					$scope.userDetail.image = content.userImage;
					
					$scope.userImageUrl = content.userImageUrl;

					awsmuServices.showSuccess(response.message); 
				}else{
					awsmuServices.showError(response.message);
				}
			}else{
				awsmuServices.showError();
			}
			
			//enable button
			$scope.uploading = false;
			$scope.uploadAction = false;
			$scope.imageExists = true;
       });

       file.upload.progress(function (evt) {
           // Math.min is to fix IE which reports 200% sometimes
           file.progress = Math.min(100, parseInt(100.0 * evt.loaded / evt.total));
       });

       file.upload.xhr(function (xhr) {
           // xhr.upload.addEventListener('abort', function(){console.log('abort complete')}, false);
       });        
	}
	
	//delete uploaded image
	$scope.removeProfilePic  = function(){
		
		bootbox.confirm("Are you sure you want to remove this image?", function(result) {
			if(result){
				
				//disable button
				$scope.removing = true;
				$scope.uploadAction = true;
				
				
				var picData = { userId:$scope.userDetail._id };
				
				var picResponse = awsmuServices.postData("removeProfilePic/", picData);
				
				picResponse.success(function(response, status, headers, config) {
					
					var	response = awsmuServices.responseCheckLogin(response);
					
					if(response && response.status){
						
						content = angular.fromJson(response.content);  
						$scope.userDetail.image = content.userImage;
						$scope.userImageUrl = content.userImageUrl;
						awsmuServices.showSuccess(response.message); 
						//"remove buton" hide
						$scope.imageExists = false;
						
					}else{
						awsmuServices.showError(response.message);
					}
					
					///enable button
					$scope.removing = false;
					$scope.uploadAction = false;
					
				});
				
				userResponse.error(function(data, status, headers, config) {  		        	
					awsmuServices.showError();
					
					//enable button
					$scope.removing = false;
					$scope.uploadAction = false;
				});	
			}
		});     
	}
	
	//cancel crop
	$scope.cancelCrop  = function(){
		$scope.cropSection = false;
	}
	
	//change pasword
	$scope.passwordSubmit  = function(){
		
		$scope.passwordSubmitted = true;
		
		if( ! $scope.passwordForm.$invalid){		
			
			//disable button
			$scope.passwordLoading = true;
			//set error false
			$scope.passwordSubmitted = false;
		
			var passwordData = { userId:$scope.userDetail._id, password:$scope.password };
		
			var passwordResponse = awsmuServices.postData("changeUserPassword/", passwordData);
		
			passwordResponse.success(function(response, status, headers, config) {
			
				var	response = awsmuServices.responseCheckLogin(response);
			
				if(response && response.status){
					awsmuServices.showSuccess(response.message); 
				}else{
					awsmuServices.showError(response.message);
				}
				//enable button
				$scope.passwordLoading = false;
			});
		
			passwordResponse.error(function(data, status, headers, config) {  		        	
				awsmuServices.showError();
			
				//enable button
				$scope.passwordLoading = false;
			});	
		}
	}

});

//user view controller
awsmuApp.controller('userViewCtrl', function($scope, $routeParams, $location, awsmuServices) {

	//to fetch site constant
	awsmuServices.getConstant($scope);
	
	//set menu selected
	$scope.sideNav = 'profileInfo';
	
	//get user detail
	var userResponse = awsmuServices.postData("getUserDetail/"+$routeParams.userId, '');
	
	userResponse.success(function(response, status, headers, config) {
	
		var	response = awsmuServices.responseCheckLogin(response);
		
		if(response && response.status){
	   		   content =  angular.fromJson(response.content);
	   		   $scope.userDetail = content.userDetail;
	   		   $scope.userAttributesList = content.userAttributesList;
		}else
			$location.path('/users');	
	});
	
	userResponse.error(function(data, status, headers, config) {  		        	
		awsmuServices.showError();
		$location.path('/users');
	});	
	
});
