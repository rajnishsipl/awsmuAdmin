//doctor list controller
awsmuApp.controller('doctorsListCtrl', function($scope, $location, $window, $compile, awsmuServices) {
	
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
				url: baseUrl+'doctorsList',
				datatype: "json",
				colNames: ['Doctor Id', 'Name', 'Personal', 'Office', 'Country', 'Is Active', 'Created Date', 'Action'],
				colModel: [
				           {name: '_id', index: '_id', align: "center", search:true, sortable:true, formatter: function (cellvalue) {  userId = cellvalue; return cellvalue;}
				           },
                           {name: 'name', index: 'name', align: "center", search:true, sortable:true, width:200, formatter: function (cellvalue) {  userId = cellvalue; return cellvalue;}
    		               },
					       {name: 'personalContactNo', index: 'personalContactNo', align: "left", search:true, sortable:true
				           },
					       {name: 'officeContactNo', index: 'officeContactNo', align: "left", search:true, sortable:true
				           },
				           {name: 'country', index: 'country', align: "left", search:true, sortable:true
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
                            	return '<a href ="'+baseUrl+'#/doctorEdit/'+cellvalue+'">Edit</a> | <a href ="'+baseUrl+'#/doctorView/'+cellvalue+'">View</a> | <a href="javascript:void(0)" ng-click="deleteDoctor(\''+cellvalue+'\')" class="deleteLink">Delete</a>';
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
			//compile ng-click code
			$compile(angular.element('.deleteLink'))($scope);
		}
	};
	
	$scope.loadrecord=function(){
		$scope.gridapi.insert($scope.modeldata);
	}
	
	//delete doctor
	$scope.deleteDoctor = function(doctorId){	
		bootbox.confirm("Are you sure you want to delete this doctor?", function(result) {
			
			if(result){
				var data = {doctorId:doctorId};		
					
				var deleteResponse = awsmuServices.postData("deleteDoctor", data);
		
				deleteResponse.success(function(response, status, headers, config) {
					
					var	response = awsmuServices.responseCheckLogin(response);
					
					if(response && response.status){
						awsmuServices.showSuccess(response.message);
						$window.location.href= "#/";
						$window.location.href= "#/doctorsList";
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

//Doctor View Controller 
awsmuApp.controller('doctorViewCtrl', function($scope, $routeParams, awsmuServices) {
	
	//to fetch site constant
	awsmuServices.getConstant($scope);
	
	//get doctor detail
	var doctorResponse = awsmuServices.postData("getDoctorDetail/"+$routeParams.doctorId, '');
	
	doctorResponse.success(function(response, status, headers, config) {
	
		var	response = awsmuServices.responseCheckLogin(response);
		
		if(response && response.status){
   		   content =  angular.fromJson(response.content);
   		   
   		   $scope.doctorDetail = content.doctorDetail;
   		   
		}
	});
	
	doctorResponse.error(function(data, status, headers, config) {  		        	
		awsmuServices.showError();
	});
	 
});

//Doctor edit Controller 
awsmuApp.controller('DoctorEditCtrl', function($scope, $routeParams, $window, Upload, awsmuServices) {
	
	//to fetch site constant
	awsmuServices.getConstant($scope);
	
	//get doctor detail
	var doctorResponse = awsmuServices.postData("getDoctorDetail/"+$routeParams.doctorId, '');
	
	doctorResponse.success(function(response, status, headers, config) {
	
		var	response = awsmuServices.responseCheckLogin(response);
		
		if(response && response.status){
   		   content =  angular.fromJson(response.content);
   		   
   		   $scope.doctor = content.doctorDetail;
   		   $scope.nationalities = content.nationalityList.value;
   		   $scope.degreeCoursesList = content.degreeCoursesList.value;
   		   $scope.specialtieslist = content.specialtyList.value;
   		   //set cities list
		   $scope.getCities($scope.doctor.country);
		   if( $scope.doctor.isActive == 1)
			   $scope.doctor.isActive =true;
   		   
		}
	});
	
	doctorResponse.error(function(data, status, headers, config) {  		        	
		awsmuServices.showError();
	});
	
	//edit doctor detail
	$scope.doctorEditSubmit = function(){
		
		$scope.doctorSubmitted = true;
		
		
		if( ! $scope.doctorForm.$invalid){		
			
			//check file format 
			var file = $("#file").val();
			
			//set check box
			if($scope.doctor.isActive)
				$scope.doctor.isActive = 1;
			else
				$scope.doctor.isActive = 0;
			
			//this fields are setting through js so need to add here
			
			$scope.doctor.address = $("#address").val();
			$scope.doctor.longitude = $("#longitude").val();
			$scope.doctor.latitude = $("#latitude").val();
			
   			if(file != ''){
   				var fileSize = $("#file")[0].files[0].size;
   				var checkfile = file.toLowerCase();
   				if ( ! checkfile.match(/(\.jpg|\.png|\.JPG|\.PNG|\.jpeg|\.JPEG)$/)){
   					$scope.fileError = true;
   					return false;
   				}else if ((parseInt(fileSize) / 1024) > 32000) {
   					$scope.fileError = true;
   					return false;
   	            }
   			}else{
   				$scope.fileError = false;	
   			}
   			   			
			//disable button
			$scope.loading = true;
			
			//set error false
			$scope.doctorSubmitted = false;
			
			//get uploaded file
			var file = $scope.file;
			
			//if file is selected
			if(file != undefined){
			
				var fieldsData = {doctorDetail: JSON.stringify($scope.doctor), imageUpload: 1};
				var file = $scope.file;
				
				file.upload = Upload.upload({
		       		url: baseUrl+'saveImageDoctor',
		       		method: 'POST',
		       		headers: {
		       			'Content-Type': file.type
		       		},
		       		file: file,
		       		fields: fieldsData,
		       		fileFormDataName: 'file'
		       });
	
		       file.upload.then(function (response) {
		    	
		    	    var	response = awsmuServices.responseCheckLogin(response.data);
					
					if(response){
						if(response.status) {
							awsmuServices.showSuccess(response.message); 
						
							$window.location.href= "#/doctorsList";
						}else{
							awsmuServices.showError(response.message);
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
			}else{ //if file is not seleted
				var data = {doctorDetail: JSON.stringify($scope.doctor), imageUpload: 0};
				var doctorResponse = awsmuServices.postData("saveDoctor", data);
				
				doctorResponse.success(function(response, status, headers, config) {
					
					var	response = awsmuServices.responseCheckLogin(response);
					
					if(response && response.status){
						awsmuServices.showSuccess(response.message);
						$window.location.href= "#/doctorsList";
					}else{
						awsmuServices.showError(response.message);
					}
					
					//enable button
					$scope.loading = false;
				});
				
				doctorResponse.error(function(data, status, headers, config) {  		        	
					awsmuServices.showError();
					
					//enable button
					$scope.loading = false;
				});	
			}  
		}
	} 
	
	//get cities drop down
	$scope.getCities = function(){	
		if(content != undefined){
			var countryData = { country:$scope.doctor.country };
			
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
});

//Doctor add Controller 
awsmuApp.controller('DoctorAddCtrl', function($scope, $window, Upload, awsmuServices) {
	
	//to fetch site constant
	awsmuServices.getConstant($scope);
	
	//get doctor detail
	var addResponse = awsmuServices.getData("doctorsAttributesList");
	
	addResponse.success(function(response, status, headers, config) {
	
		var	response = awsmuServices.responseCheckLogin(response);
		
		if(response && response.status){
			
			 content =  angular.fromJson(response.content);
	   		 $scope.nationalities = content.nationalityList.value;
	   		 $scope.degreeCoursesList = content.degreeCoursesList.value;
		   	 $scope.specialtieslist = content.specialtyList.value;
		}
	});
	
	addResponse.error(function(data, status, headers, config) {  		        	
		awsmuServices.showError();
	});
	
	//add doctor detail
	$scope.doctorAddSubmit = function(){
		
		$scope.doctorSubmitted = true;
		
		if( ! $scope.doctorForm.$invalid){		
			
			//check file format 
			var file = $("#file").val();
		
 			if(file != ''){
 				var fileSize = $("#file")[0].files[0].size;
 				var checkfile = file.toLowerCase();
 				if ( ! checkfile.match(/(\.jpg|\.png|\.JPG|\.PNG|\.jpeg|\.JPEG)$/)){
 					$scope.fileError = true;
 					return false;
 				}else if ((parseInt(fileSize) / 1024) > 32000) {
 					$scope.fileError = true;
 					return false;
 	            }
 			}else{
 				$scope.fileError = false;	
 			}
 			   			
			//disable button
			$scope.loading = true;
			
			//set error false
			$scope.doctorSubmitted = false;
			
			//get uploaded file
			var file = $scope.file;
			
			//set check box
			if($scope.doctor.isActive)
				$scope.doctor.isActive = 1;
			else
				$scope.doctor.isActive = 0;
			
			//this fields are setting through js so need to add here
			$scope.doctor.address = $("#address").val();
			$scope.doctor.longitude = $("#longitude").val();
			$scope.doctor.latitude = $("#latitude").val();
			
			//if file is selected
			if(file != undefined){
								
				var fieldsData = {doctorDetail: JSON.stringify($scope.doctor), imageUpload: 1};
				var file = $scope.file;
				
				file.upload = Upload.upload({
		       		url: baseUrl+'saveImageDoctor',
		       		method: 'POST',
		       		headers: {
		       			'Content-Type': file.type
		       		},
		       		file: file,
		       		fields: fieldsData,
		       		fileFormDataName: 'file'
		       });
	
		       file.upload.then(function (response) {
		    	
		    	    var	response = awsmuServices.responseCheckLogin(response.data);
					
					if(response){
						if(response.status) {
							awsmuServices.showSuccess(response.message); 
							$window.location.href= "#/doctorsList";
						}else{
							awsmuServices.showError(response.message);
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
			}else{ //if file is not seleted
				var data = {doctorDetail: JSON.stringify($scope.doctor), imageUpload: 0};
				var doctorResponse = awsmuServices.postData("saveDoctor", data);
				
				doctorResponse.success(function(response, status, headers, config) {
					
					var	response = awsmuServices.responseCheckLogin(response);
					
					if(response && response.status){
						awsmuServices.showSuccess(response.message); 
						$window.location.href= "#/doctorsList";
					}else{
						awsmuServices.showError(response.message);
					}
					
					//enable button
					$scope.loading = false;
				});
				
				doctorResponse.error(function(data, status, headers, config) {  		        	
					awsmuServices.showError();
					
					//enable button
					$scope.loading = false;
				});	
			}  
		}
	} 
	
	//get cities drop down
	$scope.getCities = function(){	
		if(content != undefined){
			var countryData = { country:$scope.doctor.country };
			
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
});
