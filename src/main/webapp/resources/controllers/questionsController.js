/*questions list controller*/
awsmuApp.controller('questionsListCtrl', function($scope, $window, awsmuServices,$compile) {
	//to fetch site constant
	awsmuServices.getConstant($scope);

	var op = ":All;1:Yes;0:No";
	var arr = ['0','1'];
	var opreal = ":All;0:Yes;1:No";
	var sym;
	
	 $scope.patientdata = [];
	 $scope.gridapi = {};
	 $scope.config = {
				url: baseUrl+'question/getGrid',
				datatype: "json",
				colNames: ['Question Id', 'Question', 'Question Tag', 'Attribute Text', 'isActive', 'Created Date','Action'],
				colModel: [
                           {name: '_id', index: '_id', align: "center", search:true, sortable:true, formatter: function (cellvalue) {  userId = cellvalue; return cellvalue;}
    		               },
					       {name: 'question', index: 'question', align: "left", search:true, sortable:true
				           },
					       {name: 'questionTag', index: 'questionTag', align: "left", search:true, sortable:true
				           },
				           {name: 'attributeText', index: 'attributeText', align: "left",search:true, sortable:true
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
                                             return '<a href ="'+baseUrl+'#/questionsEdit/'+cellvalue+'">Edit</a> | <a href ="'+baseUrl+'#/questionsView/'+cellvalue+'">View</a> | <a href="javascript:void(0)" ng-click="deleteRecord(\''+cellvalue+'\')" class="deleteLink">Delete</a>';
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
	$scope.deleteRecord = function(questionId){	
		bootbox.confirm("Are you sure you want to delete this record?", function(result) {
			
			if(result){
				var data = {questionId:questionId};		
					
				var deleteResponse = awsmuServices.postData("question/delete", data);
		
				deleteResponse.success(function(response, status, headers, config) {
					
					var	response = awsmuServices.responseCheckLogin(response);
					
					if(response && response.status){
						awsmuServices.showSuccess(response.message);
						//$window.location.href= "#/";
						$window.location.href= "#/questions";
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


/*questions view controller*/
awsmuApp.controller('questionsViewCtrl', function($scope, $routeParams,awsmuServices) {
	//to fetch site constant
	awsmuServices.getConstant($scope);
	
	//get question detail
	var response = awsmuServices.postData("question/getDetail/"+$routeParams.questionId, '');
	
	
	response.success(function(response, status, headers, config) {
		var	serviceResponse = awsmuServices.responseCheck(response)
		if(serviceResponse && serviceResponse.status){
			 content = angular.fromJson(serviceResponse.content);
			 $scope.question = content.questionDetails;
			 $scope.problemList = content.problemList;
		}else{			
				awsmuServices.showError(response.message);
		}
	});
	
	response.error(function(data, status, headers, config) {  		        	
		awsmuServices.showError("An error occurred please try again later.");
    });
});



/*questions edit controller*/
awsmuApp.controller('questionsEditCtrl', function($http, $scope, $routeParams, $location, awsmuServices,Upload) {
	awsmuServices.getConstant($scope);
	
	$scope.questionsList='';
	$scope.errors ="";
	
	
	$scope.getQuestionsDetail = function(){
	
			//get questions detail
			var questionsResponse = awsmuServices.postData("question/getDetail/"+$routeParams.questionId, '');
			
			
			questionsResponse.success(function(response, status, headers, config) {
				var	serviceResponse = awsmuServices.responseCheck(response)
				if(serviceResponse && serviceResponse.status){
					 content = angular.fromJson(serviceResponse.content);
					// get questions data
					 $scope.question = content.questionDetails;
					 $scope.options =  $scope.question.options;
					 $scope.question.options =[];
					//get problem  list
					 $scope.problemList = content.problemList;
					
				}else{			
						awsmuServices.showError(response.message);
						$location.path('/activities');
				}
				
			});
			
			questionsResponse.error(function(data, status, headers, config) {  		        	
				awsmuServices.showError("An error occurred please try again later.");
		    });
	
	}
	
	// Call function on load 
	$scope.getQuestionsDetail();
	
	
	//Remove options
	$scope.removeOptions = function(removeItem){
		var index = $scope.options.indexOf(removeItem);
	    if (index != -1) 
	        $scope.options.splice(index, 1);
	}
	
	// Add options
	$scope.addOptions = function(){
		 $scope.options.push({'text':'','image':''});
		
	}
	
	
	$scope.fileChanged = function(element) {
		var index = parseInt(element.id);
		$scope.options[index].isUpdate = 1;
		
	};
	
	
	$scope.updateQuestionSubmit  = function(){
		
		
		$scope.questionSubmitted = true;
		$scope.imageError = [];
		$scope.index = 1;
		$scope.imageFlag = true;
		$scope.isImageError = false;
		
		var mainFile='';
	
		var images = []
		var isFileUploaded = false;
		
		if($scope.question.isImage == 1){
			angular.forEach($scope.options, function(value, key) {			
				
				if(value.isUpdate == 1){
					var file=	value.image;
					
					if($scope.imageFlag == true)
						mainFile = value.image;
					
					images.push(value.image);
					
					if(file != ''){
						isFileUploaded = true;
		 				var fileSize = file.size;
		 				var checkfile = file.name.toLowerCase();
		 				if ( ! checkfile.match(/(\.jpg|\.png|\.JPG|\.PNG|\.jpeg|\.JPEG)$/)){
		 					$scope.imageError[$scope.index] = true;
		 					$scope.isImageError = true;
		 				}else if ((parseInt(fileSize) / 1024) > 32000) {
		 					$scope.isImageError = true;
		 					$scope.imageError[$scope.index] = true;
		 					
		 	            }
					}
					$scope.imageFlag = false;
				}
				
				$scope.index = $scope.index+1;			
			});
		
		
			if($scope.isImageError)	
				return false;		
		}
		
		//if file is updated 
		if($scope.question.isImage!=0 && mainFile != ''){
			
			angular.forEach($scope.options, function(obj){				   
				   if(obj["isUpdate"] == 1){
					   obj["image"] = "";   
				   }
			});	
			$scope.question.options = $scope.options;
			var fieldsData = {question: JSON.stringify($scope.question)};
			
			
			mainFile.upload = Upload.upload({
	       		url: baseUrl+'question/updateQuestionImage',
	       		method: 'POST',
	       		headers: {
	       			'Content-Type': mainFile.type
	       		},
	       		file: images,
	       		fields: fieldsData,
	       		fileFormDataName: 'optionsIcon'
	       });

			mainFile.upload.then(function (response) {
	    	
	    	    var	response = awsmuServices.responseCheck(response.data);
				
				if(response){
					if(response.status) {
						awsmuServices.showSuccess(response.message); 
						$scope.getQuestionsDetail();
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

			mainFile.upload.progress(function (evt) {
	           // Math.min is to fix IE which reports 200% sometimes
				mainFile.progress = Math.min(100, parseInt(100.0 * evt.loaded / evt.total));
	       });
		}else{
			
			$scope.question.options = $scope.options; 
			
			var data = {question: JSON.stringify($scope.question)};
			
			var response = awsmuServices.postData("question/update",data);
			response.success(function(response, status, headers, config) {
		
				
			var	serviceResponse = awsmuServices.responseCheck(response)
			if(serviceResponse && serviceResponse.status){
					$scope.questionSubmitted = false;
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
		
		}
		
		
	}
	

	
});


/*questions add controller*/
awsmuApp.controller('questionsAddCtrl', function($http, $scope, $location, awsmuServices,Upload) {
	awsmuServices.getConstant($scope);
	

	//get questions form data
	var questionsResponse = awsmuServices.postData("question/getFormData/", '');
	
	
	questionsResponse.success(function(response, status, headers, config) {
		var	serviceResponse = awsmuServices.responseCheck(response)
		if(serviceResponse && serviceResponse.status){
			 content = angular.fromJson(serviceResponse.content);
			//get problem  list
			 $scope.problemList = content.problemList;
			
		}else{			
				awsmuServices.showError(response.message);
				$location.path('/questions');
		}
		
	});
	
	questionsResponse.error(function(data, status, headers, config) {  		        	
		awsmuServices.showError("An error occurred please try again later.");
    });
	
	
	
	
	$scope.questionsList='';
	$scope.errors ="";
	
	
	$scope.question={};
	
	// Add default option
	$scope.question.options = [{'text':'','image':''},{'text':'','image':''}];
	
	//Remove options
	$scope.removeOptions = function(removeItem){
		var index = $scope.question.options.indexOf(removeItem);
	    if (index != -1) 
	        $scope.question.options.splice(index, 1);
	}
	
	// Add options
	$scope.addOptions = function(){
		 $scope.question.options.push({'text':'','image':''});
	}
	
	// Add question 
	$scope.addQuestionSubmit  = function(){ 
		$scope.questionSubmitted = true;
		$scope.imageError = [];
		$scope.index = 1;
		$scope.isImageError = false;
		
		var mainFile='';
		var names = [];
		var images = []
		if($scope.question.isImage == 1){
			angular.forEach($scope.question.options, function(value, key) {
				
				var file=	value.image;
				
				if($scope.index == 1)
					mainFile = value.image;
				
				names.push('optionsIcon'+$scope.index);
				images.push(value.image);
				if(file != ''){
		 				var fileSize = file.size;
		 				var checkfile = file.name.toLowerCase();
		 				if ( ! checkfile.match(/(\.jpg|\.png|\.JPG|\.PNG|\.jpeg|\.JPEG)$/)){
		 					$scope.imageError[$scope.index] = true;
		 					$scope.isImageError = true;
		 				}else if ((parseInt(fileSize) / 1024) > 32000) {
		 					$scope.isImageError = true;
		 					$scope.imageError[$scope.index] = true;
		 					
		 	            }
		 		}else{
		 			$scope.isImageError = true;
					$scope.imageError[$scope.index] = true;
		 		}
				$scope.index = $scope.index+1;
				
				//$scope.question.options[key.image]="";
			});
			
			if($scope.isImageError)	
				return false;	
		}
		
		
		if(! $scope.questionForm.$invalid) {
			//var files = 	$scope.question.options;
			//mainFile
			
			// enable loading on button
			$scope.loading= true;
			
			
			
			//if file is selected
			if($scope.question.isImage!=0 && mainFile != ''){
				angular.forEach($scope.question.options, function(obj){
					   obj["image"] = "";
					});	
				var fieldsData = {question: JSON.stringify($scope.question),noOfimage: $scope.question.options.length};
				
				
				mainFile.upload = Upload.upload({
		       		url: baseUrl+'question/addQuestionImage',
		       		method: 'POST',
		       		headers: {
		       			'Content-Type': mainFile.type
		       		},
		       		file: images,
		       		fields: fieldsData,
		       		fileFormDataName: 'optionsIcon'
		       });
	
				mainFile.upload.then(function (response) {
		    	
		    	    var	response = awsmuServices.responseCheck(response.data);
					
					if(response){
						if(response.status) {
							awsmuServices.showSuccess(response.message); 
							$scope.question={};
							 	
							
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
	
				mainFile.upload.progress(function (evt) {
		           // Math.min is to fix IE which reports 200% sometimes
					mainFile.progress = Math.min(100, parseInt(100.0 * evt.loaded / evt.total));
		       });
			}else{
				var data = {question: JSON.stringify($scope.question)};
				
				var response = awsmuServices.postData("question/add",data);
				response.success(function(response, status, headers, config) {
			
					
				var	serviceResponse = awsmuServices.responseCheck(response)
				if(serviceResponse && serviceResponse.status){
						$scope.questionSubmitted = false;
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
			
			}
			
				
			
	 }
		
	}
	
});
