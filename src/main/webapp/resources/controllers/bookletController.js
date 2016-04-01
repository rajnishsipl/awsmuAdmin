//booklet list controller
awsmuApp.controller('bookletsListCtrl', function($scope, $location, $window, $compile, awsmuServices) {
	
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
				url: baseUrl+'bookletsList',
				datatype: "json",
				colNames: ['Booklet Id', 'User Id', 'Title', 'Description', 'Is Active', 'Created Date', 'Action'],
				colModel: [
				           {name: '_id', index: '_id', align: "center", search:true, sortable:true, formatter: function (cellvalue) {  userId = cellvalue; return cellvalue;}
				           },
                           {name: 'userId', index: 'userId', align: "center", search:true, sortable:true, width:200, formatter: function (cellvalue) {  userId = cellvalue; return cellvalue;}
    		               },
					       {name: 'title', index: 'title', align: "left", search:true, sortable:true
				           },
					       {name: 'description', index: 'description', align: "left", search:true, sortable:true
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
                            	return '<a href ="'+baseUrl+'#/bookletEdit/'+cellvalue+'">Edit</a> | <a href ="'+baseUrl+'#/bookletView/'+cellvalue+'">View</a> | <a href="javascript:void(0)" ng-click="deleteBooklet(\''+cellvalue+'\')" class="deleteLink">Delete</a>';
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
	
	//delete booklet
	$scope.deleteBooklet = function(bookletId){	
		bootbox.confirm("Are you sure you want to delete this booklet?", function(result) {
			
			if(result){
				var data = {bookletId:bookletId};		
					
				var deleteResponse = awsmuServices.postData("deleteBooklet", data);
		
				deleteResponse.success(function(response, status, headers, config) {
					
					var	response = awsmuServices.responseCheckLogin(response);
					
					if(response && response.status){
						awsmuServices.showSuccess(response.message);
						$window.location.href= "#/";
						$window.location.href= "#/bookletsList";
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


//User's booklet controller 
awsmuApp.controller('userBookletsListCtrl', function($scope, $routeParams, $window, $compile, awsmuServices) {
	
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
			url: baseUrl+'userBookletsList/'+$routeParams.userId,
				datatype: "json",
				colNames: ['Booklet Id', 'User Id', 'Title', 'Description', 'Is Active', 'Created Date', 'Action'],
				colModel: [
				           {name: '_id', index: '_id', align: "center", search:true, sortable:true, formatter: function (cellvalue) {  userId = cellvalue; return cellvalue;}
},
                           {name: 'userId', index: 'userId', align: "center", search:true, sortable:true, width:200, formatter: function (cellvalue) {  userId = cellvalue; return cellvalue;}
    		               },
					       {name: 'title', index: 'title', align: "left", search:true, sortable:true
				           },
					       {name: 'description', index: 'description', align: "left", search:true, sortable:true
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
                            	return '<a href ="'+baseUrl+'#/bookletEdit/'+cellvalue+'">Edit</a> | <a href ="'+baseUrl+'#/bookletView/'+cellvalue+'">View</a> | <a href="javascript:void(0)" ng-click="deleteBooklet(\''+cellvalue+'\')" class="deleteLink">Delete</a>';
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
	
	//delete booklet
	$scope.deleteBooklet = function(bookletId){	
		bootbox.confirm("Are you sure you want to delete this booklet?", function(result) {
			
			if(result){
				var data = {bookletId:bookletId};		
					
				var deleteResponse = awsmuServices.postData("deleteBooklet", data);
		
				deleteResponse.success(function(response, status, headers, config) {
					
					var	response = awsmuServices.responseCheckLogin(response);
					
					if(response && response.status){
						awsmuServices.showSuccess(response.message);
						$window.location.href= "#/";
						$window.location.href= "#/userBookletsList/"+$routeParams.userId;
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

//Booklet View Controller 
awsmuApp.controller('bookletViewCtrl', function($scope, $routeParams, awsmuServices) {
	
	//to fetch site constant
	awsmuServices.getConstant($scope);
	
	//get booklet detail
	var bookletResponse = awsmuServices.postData("getBookletDetail/"+$routeParams.bookletId, '');
	
	bookletResponse.success(function(response, status, headers, config) {
	
		var	response = awsmuServices.responseCheckLogin(response);
		
		if(response && response.status){
   		   content =  angular.fromJson(response.content);
   		   
   		   $scope.bookletDetail = content.bookletDetail;
   		   
		}
	});
	
	bookletResponse.error(function(data, status, headers, config) {  		        	
		awsmuServices.showError();
	});
	 
});

//Booklet edit Controller 
awsmuApp.controller('bookletEditCtrl', function($scope, $routeParams, $window, Upload, awsmuServices) {
	
	//to fetch site constant
	awsmuServices.getConstant($scope);
	
	//get booklet detail
	var bookletResponse = awsmuServices.postData("getBookletDetail/"+$routeParams.bookletId, '');
	
	bookletResponse.success(function(response, status, headers, config) {
	
		var	response = awsmuServices.responseCheckLogin(response);
		
		if(response && response.status){
   		   content =  angular.fromJson(response.content);
   		   
   		   $scope.booklet = content.bookletDetail;
   		   
		}
	});
	
	bookletResponse.error(function(data, status, headers, config) {  		        	
		awsmuServices.showError();
	});
	
	//edit booklet detail
	$scope.bookletEditSubmit = function(){
		
		$scope.bookletSubmitted = true;
		
		if( ! $scope.bookletForm.$invalid){		
			
			//check file format 
			var file = $("#file").val();
		
   			if(file != ''){
   				var fileSize = $("#file")[0].files[0].size;
   				var checkfile = file.toLowerCase();
   				if ( ! checkfile.match(/(\.jpg|\.png|\.JPG|\.PNG|\.jpeg|\.JPEG|\.doc|\.DOC|\.docx|\.DOCX|\.xls|\.XLS|\.pdf|\.PDF|\.ppt|\.PPT)$/)){
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
			$scope.bookletSubmitted = false;
			
			//get uploaded file
			var file = $scope.file;
			
			//if file is selected
			if(file != undefined){
			
				var fieldsData = {bookletDetail: JSON.stringify($scope.booklet), fileUpload: 1};
				var file = $scope.file;
				
				file.upload = Upload.upload({
		       		url: baseUrl+'saveUploadBooklet',
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
							$window.location.href= "#/bookletsList";
						}else{
							awsmuServices.showError(response.message);
						}
					}
					
					//enable button
					$scope.loading = false;
					
		       });
	
		       file.upload.progress(function (evt) {
		           // Math.min is to fix IE which reports 200% sometimes
		           file.progress = Math.min(100, parseInt(100.0 * evt.loaded / evt.total));
		       });
			}else{ //if file is not seleted
				var data = {bookletDetail: JSON.stringify($scope.booklet), fileUpload: 0};
				var bookletResponse = awsmuServices.postData("saveBooklet", data);
				
				bookletResponse.success(function(response, status, headers, config) {
					
					var	response = awsmuServices.responseCheckLogin(response);
					
					if(response && response.status){
						awsmuServices.showSuccess(response.message);
						$window.location.href= "#/bookletsList";
					}else{
						awsmuServices.showError(response.message);
					}
					
					//enable button
					$scope.loading = false;
				});
				
				bookletResponse.error(function(data, status, headers, config) {  		        	
					awsmuServices.showError();
					
					//enable button
					$scope.loading = false;
				});	
			}  
		}
	} 
});



//Booklet add Controller 
awsmuApp.controller('bookletAddCtrl', function($scope, $routeParams, $window, Upload, awsmuServices) {
	
	//to fetch site constant
	awsmuServices.getConstant($scope);
	
	//edit booklet detail
	$scope.bookletAddSubmit = function(){
		
		$scope.bookletSubmitted = true;
		
		if( ! $scope.bookletForm.$invalid){		
			
			//check file format 
			var file = $("#file").val();
		
 			if(file != ''){
 				var fileSize = $("#file")[0].files[0].size;
 				var checkfile = file.toLowerCase();
 				if ( ! checkfile.match(/(\.jpg|\.png|\.JPG|\.PNG|\.jpeg|\.JPEG|\.doc|\.DOC|\.docx|\.DOCX|\.xls|\.XLS|\.pdf|\.PDF|\.ppt|\.PPT)$/)){
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
			$scope.bookletSubmitted = false;
			
			//get uploaded file
			var file = $scope.file;
			
			//set userId
			$scope.booklet.userId =  $routeParams.userId;
			
			//if file is selected
			if(file != undefined){
			
				var fieldsData = {bookletDetail: JSON.stringify($scope.booklet), fileUpload: 1};
				var file = $scope.file;
				
				file.upload = Upload.upload({
		       		url: baseUrl+'saveUploadBooklet',
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
							$window.location.href= "#/bookletsList";
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
				var data = {bookletDetail: JSON.stringify($scope.booklet), fileUpload: 0};
				var bookletResponse = awsmuServices.postData("saveBooklet", data);
				
				bookletResponse.success(function(response, status, headers, config) {
					
					var	response = awsmuServices.responseCheckLogin(response);
					
					if(response && response.status){
						awsmuServices.showSuccess(response.message); 
						$window.location.href= "#/bookletsList";
					}else{
						awsmuServices.showError(response.message);
					}
					
					//enable button
					$scope.loading = false;
				});
				
				bookletResponse.error(function(data, status, headers, config) {  		        	
					awsmuServices.showError();
					
					//enable button
					$scope.loading = false;
				});	
			}  
		}
	} 
});