/*trend list controller*/
awsmuApp.controller('trendsListCtrl', function($scope, $window,$compile, awsmuServices) {
	//to fetch site constant
	awsmuServices.getConstant($scope);

	var op = ":All;1:Yes;0:No";
	var arr = ['0','1'];
	var opreal = ":All;0:Yes;1:No";
	var sym;
	
	 $scope.patientdata = [];
	 $scope.gridapi = {};
	 $scope.config = {
				url: baseUrl+'trends/getGrid',
				datatype: "json",
				colNames: ['Trend Id', 'Title', 'isActive', 'createdDate','Action'],
				colModel: [
                           {name: '_id', index: '_id', align: "center", search:true, sortable:true, formatter: function (cellvalue) {  userId = cellvalue; return cellvalue;}
    		               },
					       {name: 'title', index: 'title', align: "left", search:true, sortable:true
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
                                             return '<a href ="'+baseUrl+'#/trendsEdit/'+cellvalue+'">Edit</a> | <a href ="'+baseUrl+'#/trendsView/'+cellvalue+'">View</a> | <a href="javascript:void(0)" ng-click="deleteRecord(\''+cellvalue+'\')" class="deleteLink">Delete</a>';
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
		$scope.deleteRecord = function(trendId){	
			bootbox.confirm("Are you sure you want to delete this record?", function(result) {
				
				if(result){
					var data = {trendId:trendId};		
						
					var deleteResponse = awsmuServices.postData("trends/delete", data);
			
					deleteResponse.success(function(response, status, headers, config) {
						
						var	response = awsmuServices.responseCheckLogin(response);
						
						if(response && response.status){
							awsmuServices.showSuccess(response.message);
							//$window.location.href= "#/";
							$window.location.href= "#/trends";
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





/*trend add controller*/
awsmuApp.controller('trendsAddCtrl', function($scope, awsmuServices,Upload) {
	// Get constant
	awsmuServices.getConstant($scope);
	
	$scope.errors ="";
	$scope.trend={};
	
	// Add trend Form submit
	$scope.addTrendSubmit  = function(){
		
		$scope.trendSubmitted = true;
		
		if(! $scope.trendForm.$invalid) {
			
			//check file format of icon image
			var trendIcon = $("#trendIcon").val();
			if(trendIcon != ''){
				var fileSize = $("#trendIcon")[0].files[0].size;
				var checkfile = trendIcon.toLowerCase();
				if ( ! checkfile.match(/(\.jpg|\.png|\.JPG|\.PNG|\.jpeg|\.JPEG)$/)){
					$scope.trendIconError = true;
					return false;
				}else if ((parseInt(fileSize) / 1024) > 32000) {
					$scope.trendIconError = true;
					return false;
	            }
			}else{
 				$scope.trendIconError = false;	
 			    return false;
 			}
 			
			
			
			var fieldsData = {trend: JSON.stringify($scope.trend)};
			var file = $scope.trendIcon;
			
			//if file is selected
			if(file != undefined){
				//enable button
				$scope.loading = true;
				var file = $scope.trendIcon;
				file.upload = Upload.upload({
		       		url: baseUrl+'trends/addTrend',
		       		method: 'POST',
		       		headers: {
		       			'Content-Type': file.type
		       		},
		       		file: file,
		       		fields: fieldsData,
		       		fileFormDataName: "trendIcon"
		       });
			
				 file.upload.then(function (response) {
				    	
			    	    var	response = awsmuServices.responseCheck(response.data);
						
						if(response){
							if(response.status) {
								awsmuServices.showSuccess(response.message); 
								$scope.trendIconError = false; 	
								
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





/*trend edit controller*/
awsmuApp.controller('trendsEditCtrl', function($scope, awsmuServices,Upload,$routeParams, $location) {
	awsmuServices.getConstant($scope);
	$scope.errors ="";
	
	
	//get trend detail
	$scope.getTrendDetail  = function(){
		var response = awsmuServices.postData("trends/getDetail/"+$routeParams.trendId, '');
		$scope.errors ="";
		
		response.success(function(response, status, headers, config) {
			var	serviceResponse = awsmuServices.responseCheck(response)
			
			if(serviceResponse && serviceResponse.status){
				 content = angular.fromJson(serviceResponse.content);
				 $scope.trend = content.trendDetails;		
				 
			}else{			
					awsmuServices.showError(response.message);
			}
		});
		
		response.error(function(data, status, headers, config) {  		        	
			awsmuServices.showError("An error occurred please try again later.");
	    });
	
	}
	
	
	// Get trend detail on page load
	$scope.getTrendDetail(); 
	// Edit trend Form submit
	$scope.updateTrendSubmit  = function(){
		$scope.trendSubmitted = true;
		
		if(! $scope.trendForm.$invalid) {			
			//check file format of icon image
			var trendIcon = $("#trendIcon").val();
			if(trendIcon != ''){
				var fileSize = $("#trendIcon")[0].files[0].size;
				var checkfile = trendIcon.toLowerCase();
				if ( ! checkfile.match(/(\.jpg|\.png|\.JPG|\.PNG|\.jpeg|\.JPEG)$/)){
					$scope.trendIconError = true;
					return false;
				}else if ((parseInt(fileSize) / 1024) > 32000) {
					$scope.trendIconError = true;
					return false;
	            }
			}
			
			
			
			// enable loading on button
			$scope.loading= true;
			var fieldsData = {trend: JSON.stringify($scope.trend)};
			var file = $scope.trendIcon;
			
			//if file is selected
			if(file != undefined){
				//enable button
				$scope.loading = true;
				var file = $scope.trendIcon;
				file.upload = Upload.upload({
		       		url: baseUrl+'trends/editTrendImage',
		       		method: 'POST',
		       		headers: {
		       			'Content-Type': file.type
		       		},
		       		file: file,
		       		fields: fieldsData,
		       		fileFormDataName: "trendIcon"
		       });
			
				 file.upload.then(function (response) {
				    	
			    	    var	response = awsmuServices.responseCheck(response.data);
						
						if(response){
							if(response.status) {
								awsmuServices.showSuccess(response.message); 
								$scope.trendIconError = false;
								$scope.trendIcon="";
								//get updated trend details
								$scope.getTrendDetail();
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
			//if icon file is not selected
		}else{
			
			var data = {trend: JSON.stringify($scope.trend)};
			var response = awsmuServices.postData("trends/editTrend",data);
			
			
			response.success(function(response, status, headers, config) {
				var	serviceResponse = awsmuServices.responseCheck(response)
					
				if(serviceResponse && serviceResponse.status){
		       			$scope.trendSubmitted = false;
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



/*trends view  controller*/
awsmuApp.controller('trendsViweCtrl', function($scope, awsmuServices,$routeParams, $location) {
	awsmuServices.getConstant($scope);

	//get trend detail
	var response = awsmuServices.postData("trends/getDetail/"+$routeParams.trendId, '');
	$scope.errors ="";
	
	response.success(function(response, status, headers, config) {
		var	serviceResponse = awsmuServices.responseCheck(response)
		
		if(serviceResponse && serviceResponse.status){
			 content = angular.fromJson(serviceResponse.content);
			 $scope.trend = content.trendDetails;		
			 
		}else{			
				awsmuServices.showError(response.message);
		}
	});
	
	response.error(function(data, status, headers, config) {  		        	
		awsmuServices.showError("An error occurred please try again later.");
    });
});