/*problems list controller*/
awsmuApp.controller('problemsListCtrl', function($scope, $window, awsmuServices,$compile) {
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
				colNames: ['Problem Id', 'Name', 'Parent Id', 'Title', 'Description', 'isActive', 'createdDate','Action'],
				colModel: [
                           {name: '_id', index: '_id', align: "center", search:true, sortable:true, formatter: function (cellvalue) {  userId = cellvalue; return cellvalue;}
    		               },
					       {name: 'name', index: 'name', align: "left", search:true, sortable:true
				           },
					       {name: 'parent_id', index: 'parent_id', align: "left", search:true, sortable:true
				           },
				           {name: 'title', index: 'title', align: "left",search:true, sortable:true
				           },
				           {name: 'description', index: 'description', align: "left",search:true, sortable:true 
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
                                             return '<a href ="'+baseUrl+'#/problemsEdit/'+cellvalue+'">Edit</a> | <a href ="'+baseUrl+'#/problemsView/'+cellvalue+'">View</a> | <a href="javascript:void(0)" ng-click="deleteRecord(\''+cellvalue+'\')" class="deleteLink">Delete</a>';
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
	$scope.deleteRecord = function(problemId){	
		bootbox.confirm("Are you sure you want to delete this record?", function(result) {
			
			if(result){
				var data = {problemId:problemId};		
					
				var deleteResponse = awsmuServices.postData("problem/delete", data);
		
				deleteResponse.success(function(response, status, headers, config) {
					
					var	response = awsmuServices.responseCheckLogin(response);
					
					if(response && response.status){
						awsmuServices.showSuccess(response.message);
						//$window.location.href= "#/";
						$window.location.href= "#/problems";
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





/*problem add controller*/
awsmuApp.controller('problemsAddCtrl', function($scope, awsmuServices,Upload) {
	awsmuServices.getConstant($scope);
	
	var response = awsmuServices.postData("problem/getFormData",'');
	$scope.trendsList='';
	$scope.errors ="";
	$scope.prList = "";
	response.success(function(response, status, headers, config) {
		var	serviceResponse = awsmuServices.responseCheck(response)
		if(serviceResponse && serviceResponse.status){
			 content = angular.fromJson(serviceResponse.content);
			
			 $scope.trendsList = content.trendList;
			 $scope.prList  =content.problemList;
			 
		}else{			
				awsmuServices.showError(response.message);
		}
	});
	response.error(function(data, status, headers, config) {  		        	
		awsmuServices.showError("An error occurred please try again later.");
    });
	
	
	
	$scope.problem={};
	
	// Add activity Form submit
	$scope.AddProblemSubmit  = function(){
		
		$scope.problemSubmitted = true;
		
		if(! $scope.problemForm.$invalid) {
			// enable loading on button
			
			//check file format  of problem Icon
			var file = $("#problemIcon").val();
		
 			if(file != ''){
 				var fileSize = $("#problemIcon")[0].files[0].size;
 				var checkfile = file.toLowerCase();
 				if ( ! checkfile.match(/(\.jpg|\.png|\.JPG|\.PNG|\.jpeg|\.JPEG)$/)){
 					$scope.problemIconError = true;
 					return false;
 				}else if ((parseInt(fileSize) / 1024) > 32000) {
 					$scope.problemIconError = true;
 					return false;
 	            }
 			}else{
 				$scope.problemIconError = false; 			   
 			}
 			
 			
 			//check file format of banner image
			var file = $("#problemBanner").val();
		
 			if(file != ''){
 				var fileSize = $("#problemBanner")[0].files[0].size;
 				var checkfile = file.toLowerCase();
 				if ( ! checkfile.match(/(\.jpg|\.png|\.JPG|\.PNG|\.jpeg|\.JPEG)$/)){
 					$scope.problemBannerError = true;
 					return false;
 				}else if ((parseInt(fileSize) / 1024) > 32000) {
 					$scope.problemBannerError = true;
 					return false;
 	            }
 			}else{
 				$scope.problemBannerError = false;	
 			    return false;
 			}
 			
 			
			
			$scope.loading= true;
			$scope.problemSubmitted = false;
			
			//get uploaded file
			var file = $scope.problemIcon;
			//Set the names of uploaded file
			var names = [];
			names.push('problemIcon');
			names.push('problemBanner');
			
			//if file is selected
			if(file != undefined){
							
				var fieldsData = {problem: JSON.stringify($scope.problem), imageUpload: 1};
				var file = $scope.problemIcon;
				var file2 = $scope.problemBanner;
				
				file.upload = Upload.upload({
		       		url: baseUrl+'problem/addProblem',
		       		method: 'POST',
		       		headers: {
		       			'Content-Type': file.type
		       		},
		       		file: [file, file2],
		       		fields: fieldsData,
		       		fileFormDataName: names
		       });
	
		       file.upload.then(function (response) {
		    	
		    	    var	response = awsmuServices.responseCheck(response.data);
					
					if(response){
						if(response.status) {
							awsmuServices.showSuccess(response.message); 
							$scope.problem={};
							$scope.problemIconError = false; 	
							$scope.problemBannerError = false; 	
							
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





/*problem edit controller*/
awsmuApp.controller('problemsEditCtrl', function($scope, awsmuServices,Upload,$routeParams, $location) {
	awsmuServices.getConstant($scope);
	$scope.errors ="";
	
	
	//get problem detail
	
	$scope.getProblemDetail  = function(){
		var response = awsmuServices.postData("problem/getDetail/"+$routeParams.problemId, '');
		$scope.trendsList='';
		$scope.errors ="";
		$scope.prList = "";
		
		
		response.success(function(response, status, headers, config) {
			var	serviceResponse = awsmuServices.responseCheck(response)
			if(serviceResponse && serviceResponse.status){
				 content = angular.fromJson(serviceResponse.content);
				
				 $scope.trendsList = content.trendList;
				 $scope.prList  =content.problemList;
				 $scope.problem = content.problemDetails;
				 
				//create trend list
					var trendlist = []; 
					for(trend in $scope.problem.trends){
						trendlist.push($scope.problem.trends[trend]._id);
					}
					$scope.problemTrendList = trendlist;
					$scope.problem.trendsIdList = $scope.problemTrendList;
				 
			}else{			
					awsmuServices.showError(response.message);
			}
		});
		
		response.error(function(data, status, headers, config) {  		        	
			awsmuServices.showError("An error occurred please try again later.");
	    });
	}
	
	// Get problem details on page load
	$scope.getProblemDetail();
	
	
	
	// Edit problem Form submit
	$scope.editProblemSubmit  = function(){
		$scope.problemSubmitted = true;
		
		if(! $scope.problemForm.$invalid) {
			//check file format  of problem Icon
			var problemIcon = $("#problemIcon").val();
			if(problemIcon != ''){
 				
 				var fileSize = $("#problemIcon")[0].files[0].size;
 				var checkfile = problemIcon.toLowerCase();
 				if ( ! checkfile.match(/(\.jpg|\.png|\.JPG|\.PNG|\.jpeg|\.JPEG)$/)){
 					$scope.problemIconError = true;
 					return false;
 				}else if ((parseInt(fileSize) / 1024) > 32000) {
 					$scope.problemIconError = true;
 					return false;
 	            }
 		}
 			
 			
 		//check file format of banner image
		var problemBanner = $("#problemBanner").val();
		
		if(problemBanner != ''){
			var fileSize = $("#problemBanner")[0].files[0].size;
			var checkfile = problemBanner.toLowerCase();
			if ( ! checkfile.match(/(\.jpg|\.png|\.JPG|\.PNG|\.jpeg|\.JPEG)$/)){
				$scope.problemBannerError = true;
				return false;
			}else if ((parseInt(fileSize) / 1024) > 32000) {
				$scope.problemBannerError = true;
				return false;
            }
		}
 			
 			
		//get uploaded file
		var problemIcon = $scope.problemIcon;
		//Set the names of uploaded file
		var problemBanner = $scope.problemBanner;
		//enable button
		$scope.loading = true;
		//if both file is selected
		if(problemBanner != undefined && problemIcon != undefined ){
							
				var fieldsData = {problem: JSON.stringify($scope.problem),isIcon:1,isBanner:1};
				var file = $scope.problemIcon;
				var file2 = $scope.problemBanner;
				var names = [];
				names.push('problemIcon');
				names.push('problemBanner');
				
				file.upload = Upload.upload({
		       		url: baseUrl+'problem/editProblemImage',
		       		method: 'POST',
		       		headers: {
		       			'Content-Type': file.type
		       		},
		       		file: [file, file2],
		       		fields: fieldsData,
		       		fileFormDataName: names
		       });
			 
				 file.upload.then(function (response) {
				    	
			    	    var	response = awsmuServices.responseCheck(response.data);
						
						if(response){
							if(response.status) {
								awsmuServices.showSuccess(response.message); 
								$scope.getProblemDetail();
								$scope.problemIconError = false; 	
								$scope.problemBannerError = false; 	
								
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
				
				
			//if icon file is selected
		}else if(problemBanner == undefined && problemIcon != undefined){
				
				var fieldsData = {problem: JSON.stringify($scope.problem),isIcon:1,isBanner:0};
				var file = $scope.problemIcon;
				
			
				
				file.upload = Upload.upload({
		       		url: baseUrl+'problem/editProblemImage',
		       		method: 'POST',
		       		headers: {
		       			'Content-Type': file.type
		       		},
		       		file: file,
		       		fields: fieldsData,
		       		fileFormDataName: "problemIcon"
		       });
			
				 file.upload.then(function (response) {
				    	
			    	    var	response = awsmuServices.responseCheck(response.data);
						
						if(response){
							if(response.status) {
								awsmuServices.showSuccess(response.message); 
								$scope.getProblemDetail();
								$scope.problemIconError = false; 	
								$scope.problemBannerError = false; 	
								
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
				
				
			//if banner file is selected	
		}else if(problemBanner != undefined && problemIcon == undefined){
			
				var fieldsData = {problem: JSON.stringify($scope.problem),isIcon:0,isBanner:1};
				var file = $scope.problemBanner;
				
				file.upload = Upload.upload({
		       		url: baseUrl+'problem/editProblemImage',
		       		method: 'POST',
		       		headers: {
		       			'Content-Type': file.type
		       		},
		       		file: file,
		       		fields: fieldsData,
		       		fileFormDataName: "problemBanner"
		       });
				
				
				 file.upload.then(function (response) {
				    	
			    	    var	response = awsmuServices.responseCheck(response.data);
						
						if(response){
							if(response.status) {
								awsmuServices.showSuccess(response.message); 
								$scope.getProblemDetail();
								$scope.problemIconError = false; 	
								$scope.problemBannerError = false; 	
								
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
			
		//if both file is not selected	
		}else{
				var data = {problem: JSON.stringify($scope.problem)};
				var response = awsmuServices.postData("problem/editProblem",data);
				
				
				response.success(function(response, status, headers, config) {
			
					
					var	serviceResponse = awsmuServices.responseCheck(response)
						
					if(serviceResponse && serviceResponse.status){
						
			       			$scope.problemSubmitted = false;
			       			awsmuServices.showSuccess(response.message);
			       			$scope.getProblemDetail();
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
			
			/*
			var data = {problem: JSON.stringify($scope.problem)};
			var response = awsmuServices.postData("problem/editProblem",data);
			
			
			response.success(function(response, status, headers, config) {
		
				
				var	serviceResponse = awsmuServices.responseCheck(response)
					
				if(serviceResponse && serviceResponse.status){
					
		       			$scope.problemSubmitted = false;
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
		    });*/
	 }
		
	}
});
/*problem view  controller*/
awsmuApp.controller('problemsViweCtrl', function($scope, awsmuServices,$routeParams, $location) {
	awsmuServices.getConstant($scope);
	//get problem detail
	var response = awsmuServices.postData("problem/getDetail/"+$routeParams.problemId, '');
	
	
	response.success(function(response, status, headers, config) {
		var	serviceResponse = awsmuServices.responseCheck(response)
		if(serviceResponse && serviceResponse.status){
			 content = angular.fromJson(serviceResponse.content);
			 $scope.problem = content.problemDetails;
		}else{			
				awsmuServices.showError(response.message);
		}
	});
	
	response.error(function(data, status, headers, config) {  		        	
		awsmuServices.showError("An error occurred please try again later.");
    });
});