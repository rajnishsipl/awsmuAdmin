/*celebrities list controller*/
awsmuApp.controller('celebritiesListCtrl', function($scope, awsmuServices,$compile,$window) {
	
	//to fetch site constant
	awsmuServices.getConstant($scope);

	var op = ":All;1:Yes;0:No";
	var arr = ['0','1'];
	var opreal = ":All;0:Yes;1:No";
	var sym;
	
	 $scope.patientdata = [];
	 $scope.gridapi = {};
	 $scope.config = {
				url: baseUrl+'celebrities/getGrid',
				datatype: "json",
				colNames: ['Calebrities Id', 'Name', 'Gender', 'Nationality', 'Profession', 'isActive', 'Created Date','Action'],
				colModel: [
                           {name: '_id', index: '_id', align: "center", search:true, sortable:true, formatter: function (cellvalue) {  userId = cellvalue; return cellvalue;}
    		               },
					       {name: 'name', index: 'name', align: "left", search:true, sortable:true
				           },
					       {name: 'gender', index: 'gender', align: "left", search:true, sortable:true
				           },
				           {name: 'nationality', index: 'nationality', align: "left",search:true, sortable:true
				           },
				           {name: 'profession', index: 'profession', align: "left",search:true, sortable:true
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
                                             return '<a href ="'+baseUrl+'#/celebritiesEdit/'+cellvalue+'">Edit</a> | <a href ="'+baseUrl+'#/celebritiesView/'+cellvalue+'">View</a> | <a href="javascript:void(0)" ng-click="deleteRecord(\''+cellvalue+'\')" class="deleteLink">Delete</a>';
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
	$scope.deleteRecord = function(celebrityId){	
		bootbox.confirm("Are you sure you want to delete this record?", function(result) {
			
			if(result){
				var data = {celebrityId:celebrityId};		
					
				var deleteResponse = awsmuServices.postData("celebrities/delete", data);
		
				deleteResponse.success(function(response, status, headers, config) {
					
					var	response = awsmuServices.responseCheckLogin(response);
					
					if(response && response.status){
						awsmuServices.showSuccess(response.message);
						//$window.location.href= "#/";
						$window.location.href= "#/celebrities";
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

/*celebrities add controller*/
awsmuApp.controller('celebritiesAddCtrl', function($scope, awsmuServices,Upload) {
	awsmuServices.getConstant($scope);
	
	$scope.celebrity={};
	//$scope.celebrity.images = [{'name':'','attribute':'','attributeTitle':''},{'name':'','attribute':'','attributeTitle':''}];
	
	
	//get question detail
	var response = awsmuServices.postData("celebrities/FormData/", '');
	
	
	response.success(function(response, status, headers, config) {
		var	serviceResponse = awsmuServices.responseCheck(response)
		if(serviceResponse && serviceResponse.status){
			 content = angular.fromJson(serviceResponse.content);
			 $scope.problemList =content.problemList;
			 
		}else{			
				awsmuServices.showError(response.message);
		}
	});
	
	response.error(function(data, status, headers, config) {  		        	
		awsmuServices.showError("An error occurred please try again later.");
    });
	
		
	// Add Celebrity Form submit
	$scope.addCelebritySubmit  = function(){
		
		$scope.celebritySubmitted = true;
		$scope.celebrity.images =[];
		
		
		angular.forEach($scope.problemNames, function(obj){
		   $scope.celebrity.images.push({'name':'','attribute':obj["name"],'attributeTitle':obj["title"]})
		});	
	
		
		
		if(! $scope.celebrityForm.$invalid) {
			
			//check file format of icon image
			var mainImage = $("#mainImage").val();
			if(mainImage != ''){
				var fileSize = $("#mainImage")[0].files[0].size;
				var checkfile = mainImage.toLowerCase();
				if ( ! checkfile.match(/(\.jpg|\.png|\.JPG|\.PNG|\.jpeg|\.JPEG)$/)){
					$scope.mainImageError = true;
					return false;
				}else if ((parseInt(fileSize) / 1024) > 32000) {
					$scope.mainImageError = true;
					return false;
	            }
			}else{
 				$scope.mainImageError = false;	
 			    return false;
 			}
 			
			
			
			var fieldsData = {celebrity: JSON.stringify($scope.celebrity)};
			var file = $scope.mainImage;
			
			//if file is selected
			if(file != undefined){
				//enable button
				$scope.loading = true;
				var file = $scope.mainImage;
				file.upload = Upload.upload({
		       		url: baseUrl+'celebrities/add',
		       		method: 'POST',
		       		headers: {
		       			'Content-Type': file.type
		       		},
		       		file: file,
		       		fields: fieldsData,
		       		fileFormDataName: "mainImage"
		       });
			
				 file.upload.then(function (response) {
				    	
			    	    var	response = awsmuServices.responseCheck(response.data);
						
						if(response){
							if(response.status) {
								awsmuServices.showSuccess(response.message); 
								$scope.mainImageError = false; 	
								
							}else{
								if(response.message !="" || response.message !=null )
									awsmuServices.showError(response.message);
								if( response.errorMap !=null)
									$scope.errors = angular.fromJson(response.errorMap);
								
							}
						}else{
							awsmuServices.showError();
						}
						
						//enable button
						$scope.loading = false;
						
			       });
		
			       file.upload.progress(function (evt) {
			           // Math.min is to fix IE which reports 200% sometimes
			           file.progress = Math.min(100, parseInt(100.0 * evt.loaded / evt.total));
			       });	
			
			}else{
				awsmuServices.showError("There is some problem in given input.");
			}
			
	 }
		
	}
	


});



/*celebrities edit controller*/

awsmuApp.controller('celebritiesEditCtrl', function($scope, awsmuServices, Upload,$routeParams) {
	
awsmuServices.getConstant($scope);
	
	$scope.celebrity={};
	
	//get problems list
	var response = awsmuServices.postData("celebrities/getDetail/"+$routeParams.celebId, '');
	
	
	response.success(function(response, status, headers, config) {
		var	serviceResponse = awsmuServices.responseCheck(response)
		if(serviceResponse && serviceResponse.status){
			 content = angular.fromJson(serviceResponse.content);
			 $scope.problemList =content.problemList;
			 $scope.celebrity = content.celebrityDetails;
			 $scope.problemNames = [];
			
			 angular.forEach($scope.celebrity.images, function(obj){
				 $scope.problemNames.push(obj["attributeTitle"]);
			 });
			   
		}else{			
				awsmuServices.showError(response.message);
		}
	});
	
	response.error(function(data, status, headers, config) {  		        	
		awsmuServices.showError("An error occurred please try again later.");
    });
	

	
	$scope.updateCelebritySubmit  = function(){
	
		$scope.celebritySubmitted = true;
		$scope.images =  $scope.celebrity.images;
		$scope.celebrity.images = [];
		
		angular.forEach($scope.problemList, function(obj){			
			if($scope.problemNames.indexOf(obj["title"]) != -1){
				$scope.celebrity.images.push({'name':'','attribute':obj["name"],'attributeTitle':obj["title"]})
			}			
		});
		
		
		if(! $scope.celebrityForm.$invalid) {
			
			//check file format of icon image
			var mainImage = $("#mainImage").val();
			
			if(mainImage != ''){
				var fileSize = $("#mainImage")[0].files[0].size;
				var checkfile = mainImage.toLowerCase();
				if ( ! checkfile.match(/(\.jpg|\.png|\.JPG|\.PNG|\.jpeg|\.JPEG)$/)){
					$scope.mainImageError = true;
					return false;
				}else if ((parseInt(fileSize) / 1024) > 32000) {
					$scope.mainImageError = true;
					return false;
	            }
			}
 			
			var fieldsData = {celebrity: JSON.stringify($scope.celebrity)};
			
			var file = $scope.mainImage;
			
			//if file is selected
			if(file != undefined){
				//enable button
				$scope.loading = true;
				var file = $scope.mainImage;
				file.upload = Upload.upload({
		       		url: baseUrl+'celebrities/editCelebrityImage',
		       		method: 'POST',
		       		headers: {
		       			'Content-Type': file.type
		       		},
		       		file: file,
		       		fields: fieldsData,
		       		fileFormDataName: "mainImage"
		       });
			
				 file.upload.then(function (response) {
				    	
			    	    var	response = awsmuServices.responseCheck(response.data);
						
						if(response){
							if(response.status) {
								awsmuServices.showSuccess(response.message); 
								$scope.mainImageError = false; 	
								
							}else{
								if(response.message !="" || response.message !=null )
									awsmuServices.showError(response.message);
								if( response.errorMap !=null)
									$scope.errors = angular.fromJson(response.errorMap);
								
							}
						}else{
							awsmuServices.showError();
						}
						
						//enable button
						$scope.loading = false;
						
			       });
		
			       file.upload.progress(function (evt) {
			           // Math.min is to fix IE which reports 200% sometimes
			           file.progress = Math.min(100, parseInt(100.0 * evt.loaded / evt.total));
			       });	
			
			}else{
				var data = {celebrity: JSON.stringify($scope.celebrity)};
				var response = awsmuServices.postData("celebrities/editCelebrity",data);
				
				
				response.success(function(response, status, headers, config) {
					var	serviceResponse = awsmuServices.responseCheck(response)
						
					if(serviceResponse && serviceResponse.status){
			       			$scope.celebritySubmitted = false;
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

	}
	
});

/*celebrities view controller*/
awsmuApp.controller('celebritiesViweCtrl', function($scope, $routeParams,awsmuServices) {
	//to fetch site constant
	awsmuServices.getConstant($scope);
	
	//get question detail
	var response = awsmuServices.postData("celebrities/getDetail/"+$routeParams.celebId, '');
	
	
	response.success(function(response, status, headers, config) {
		var	serviceResponse = awsmuServices.responseCheck(response)
		if(serviceResponse && serviceResponse.status){
			 content = angular.fromJson(serviceResponse.content);
			 $scope.celebrity = content.celebrityDetails;
			 
		}else{			
				awsmuServices.showError(response.message);
		}
	});
	
	response.error(function(data, status, headers, config) {  		        	
		awsmuServices.showError("An error occurred please try again later.");
    });
	
});
