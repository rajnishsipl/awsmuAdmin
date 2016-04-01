/* Attributes categories list controller*/
awsmuApp.controller('attributesListCtrl', function($scope, $window, awsmuServices) {
	//to fetch site constant
	awsmuServices.getConstant($scope);
	 $scope.patientdata = [];
	 $scope.gridapi = {};
	 var attrName="";
	 $scope.config = {
				url: baseUrl+'getAttributesGrid',
				datatype: "json",
				colNames: ['Attribute Id', 'Name', 'created Date','Action'],
				colModel: [
                           {name: '_id', index: '_id', align: "center", search:true, sortable:true
    		               },
					       {name: 'attr', index: 'attr', align: "left", search:true, sortable:true, formatter: function (cellvalue) {  attrName = cellvalue; return cellvalue;}
				           },
				           {name: 'created_date', index: 'created_date',search:false,  align: "center",searchoptions:{sopt: ['dt']}},
				           {name: '_id', index: '_id', width:"150", align: "center",search:false, sortable:false,
				        	   formatter: function (cellvalue) {
                                             return '<a href ="'+baseUrl+'#/attributes'+attrName+'Add/'+cellvalue+'">Add</a> |<a href ="'+baseUrl+'#/attributes'+attrName+'Edit/'+cellvalue+'">Edit</a> | <a href ="'+baseUrl+'#/attributes'+attrName+'View/'+cellvalue+'">View</a>';
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
		}
		};
	$scope.loadrecord=function(){
	 $scope.gridapi.insert($scope.modeldata);
	}	
});

/* Attributes country view controller*/
awsmuApp.controller('attributesCountryView', function($scope, $window,$routeParams, awsmuServices) {
	//to fetch site constant
	awsmuServices.getConstant($scope);
	//get country detail
	var response = awsmuServices.postData("getCounntryList/"+$routeParams.attributeId, '');
	
	response.success(function(response, status, headers, config) {
		var	serviceResponse = awsmuServices.responseCheck(response)
		if(serviceResponse && serviceResponse.status){
			 content = angular.fromJson(serviceResponse.content);
			 $scope.countries = content.countryList;
		}else{			
				awsmuServices.showError(response.message);
		}
	});
	
	response.error(function(data, status, headers, config) {  		        	
		awsmuServices.showError("An error occurred please try again later.");
    });
	
});

/* Attributes country edit controller*/
awsmuApp.controller('attributesCountryEdit', function($scope, $window,$routeParams, awsmuServices) {
	//to fetch site constant
	awsmuServices.getConstant($scope);
	//get country detail
	var response = awsmuServices.postData("getCounntryList/"+$routeParams.attributeId, '');
	
	
	response.success(function(response, status, headers, config) {
		var	serviceResponse = awsmuServices.responseCheck(response)
		if(serviceResponse && serviceResponse.status){
			 content = angular.fromJson(serviceResponse.content);
			 $scope.countries = content.countryList;
			 
		}else{			
				awsmuServices.showError(response.message);
		}
	});
	
	response.error(function(data, status, headers, config) {  		        	
		awsmuServices.showError("An error occurred please try again later.");
    });
	
	//remove Data
	$scope.removeData = function(removeItem){
		console.log($scope.countries)
		var index = $scope.countries.value.indexOf(removeItem);
	    if (index != -1) 
	        $scope.countries.value.splice(index, 1);
	}
	
	// Edit country Form submit
	$scope.updateAttributeCountrySubmit  = function(){
		
		$scope.attributeCountrySubmitted = true;
		
		$scope.loading= true;
		var data = {countries: JSON.stringify($scope.countries)};
		/*update country*/
		var response = awsmuServices.postData("updateCountry",data);
			
			response.success(function(response, status, headers, config) {
				var	serviceResponse = awsmuServices.responseCheck(response)
				if(serviceResponse && serviceResponse.status){
		       			$scope.attributeCountrySubmitted = false;
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
	
	
	
});

/* Attributes country add controller*/
awsmuApp.controller('attributesCountryAdd', function($scope, $window,$routeParams, awsmuServices) {
	//to fetch site constant
	awsmuServices.getConstant($scope);
	//get country detail
	var response = awsmuServices.postData("getCounntryList/"+$routeParams.attributeId, '');
	
	$scope.countries ={};
	$scope.country={};
		
	response.success(function(response, status, headers, config) {
		var	serviceResponse = awsmuServices.responseCheck(response)
		if(serviceResponse && serviceResponse.status){
			 content = angular.fromJson(serviceResponse.content);
			 $scope.countries = content.countryList;
		}else{			
				awsmuServices.showError(response.message);
		}
	});
	
	response.error(function(data, status, headers, config) {  		        	
		awsmuServices.showError("An error occurred please try again later.");
    });
	
		
	/*Add country Form submit*/
	$scope.addAttributeCountrySubmit  = function(){
		/*Add new added item to list*/
		$scope.countries.value.push($scope.country);
		$scope.attributeCountrySubmitted = true;
		
		if(! $scope.attributeCountryForm.$invalid) {
			/* enable loading on button */
			$scope.loading= true;
			var data = {countries: JSON.stringify($scope.countries)};
			var response = awsmuServices.postData("updateCountry",data);
			
			response.success(function(response, status, headers, config) {
				var	serviceResponse = awsmuServices.responseCheck(response)
				if(serviceResponse && serviceResponse.status){
		       			$scope.attributeCountrySubmitted = false;
		       			awsmuServices.showSuccess(response.message);
		       			$scope.country={};
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
});




/* Attributes degree courses view controller*/
awsmuApp.controller('attributesDegreeCoursesView', function($scope, $window,$routeParams, awsmuServices) {
	//to fetch site constant
	awsmuServices.getConstant($scope);
	//get degree courses detail
	var response = awsmuServices.postData("getDegreeCoursesList/"+$routeParams.attributeId, '');
	
	
	response.success(function(response, status, headers, config) {
		var	serviceResponse = awsmuServices.responseCheck(response)
		if(serviceResponse && serviceResponse.status){
			 content = angular.fromJson(serviceResponse.content);
			 $scope.degreeCourses = content.degreeCoursesList;
		}else{			
				awsmuServices.showError(response.message);
		}
	});
	
	response.error(function(data, status, headers, config) {  		        	
		awsmuServices.showError("An error occurred please try again later.");
    });
	
});


/* Attributes degree courses edit controller*/
awsmuApp.controller('attributesDegreeCoursesEdit', function($scope, $window,$routeParams, awsmuServices) {
	//to fetch site constant
	awsmuServices.getConstant($scope);
	//get degree courses detail
	var response = awsmuServices.postData("getDegreeCoursesList/"+$routeParams.attributeId, '');
	
	
	response.success(function(response, status, headers, config) {
		var	serviceResponse = awsmuServices.responseCheck(response)
		if(serviceResponse && serviceResponse.status){
			 content = angular.fromJson(serviceResponse.content);
			 $scope.degreeCourses = content.degreeCoursesList;
		}else{			
				awsmuServices.showError(response.message);
		}
	});
	
	response.error(function(data, status, headers, config) {  		        	
		awsmuServices.showError("An error occurred please try again later.");
    });
	
	
	//remove Data
	$scope.removeData = function(removeItem){
		
		var index = $scope.degreeCourses.value.indexOf(removeItem);
	    if (index != -1) 
	        $scope.degreeCourses.value.splice(index, 1);
	}
	
	// Edit degree courses Form submit
	$scope.updateAttributeDegreeCoursesSubmit  = function(){
		
		$scope.attributeDegreeCoursesSubmitted = true;
			$scope.loading= true;
			
			var data = {degreeCourses: JSON.stringify($scope.degreeCourses)};
			var response = awsmuServices.postData("updateDegreeCourses",data);
			
			response.success(function(response, status, headers, config) {
				var	serviceResponse = awsmuServices.responseCheck(response)
				if(serviceResponse && serviceResponse.status){
		       			$scope.attributeDegreeCoursesSubmitted = false;
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
	
});


/* Attributes degree courses add controller*/
awsmuApp.controller('attributesDegreeCoursesAdd', function($scope, $window,$routeParams, awsmuServices) {
	//to fetch site constant
	awsmuServices.getConstant($scope);
	//get degree courses detail
	var response = awsmuServices.postData("getDegreeCoursesList/"+$routeParams.attributeId, '');
	
	$scope.degreeCourses ={};
	response.success(function(response, status, headers, config) {
		var	serviceResponse = awsmuServices.responseCheck(response)
		if(serviceResponse && serviceResponse.status){
			 content = angular.fromJson(serviceResponse.content);
			 $scope.degreeCourses = content.degreeCoursesList;
		}else{			
				awsmuServices.showError(response.message);
		}
	});
	
	response.error(function(data, status, headers, config) {  		        	
		awsmuServices.showError("An error occurred please try again later.");
    });
	
	
	// Add attribute Degree Course Form submit
	$scope.addAttributeDegreeCoursesSubmit  = function(){
		/*add new added degree course  to list*/
		$scope.degreeCourses.value.push($scope.degreeCoursesName);
		$scope.attributeDegreeCoursesSubmitted = true;
		
		
		if($scope.attributeDegreeCourseForm.$valid) {
			// enable loading on button
			$scope.loading= true;
			
			var data = {degreeCourses: JSON.stringify($scope.degreeCourses)};
			var response = awsmuServices.postData("updateDegreeCourses",data);
			
			response.success(function(response, status, headers, config) {
				var	serviceResponse = awsmuServices.responseCheck(response)
				if(serviceResponse && serviceResponse.status){
		       			$scope.attributeDegreeCoursesSubmitted = false;
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
	
});




/* Attributes profession view controller*/
awsmuApp.controller('attributesProfessionView', function($scope, $window,$routeParams, awsmuServices) {
	//to fetch site constant
	awsmuServices.getConstant($scope);
	//get profession detail
	var response = awsmuServices.postData("getProfessionList/"+$routeParams.attributeId, '');
	
	
	response.success(function(response, status, headers, config) {
		var	serviceResponse = awsmuServices.responseCheck(response)
		if(serviceResponse && serviceResponse.status){
			 content = angular.fromJson(serviceResponse.content);
			 $scope.professions = content.professionList;
		}else{			
				awsmuServices.showError(response.message);
		}
	});
	
	response.error(function(data, status, headers, config) {  		        	
		awsmuServices.showError("An error occurred please try again later.");
    });
	
});


/* Attributes profession edit controller*/
awsmuApp.controller('attributesProfessionEdit', function($scope, $window,$routeParams, awsmuServices) {
	//to fetch site constant
	awsmuServices.getConstant($scope);
	//get profession detail
	var response = awsmuServices.postData("getProfessionList/"+$routeParams.attributeId, '');
	
	
	response.success(function(response, status, headers, config) {
		var	serviceResponse = awsmuServices.responseCheck(response)
		if(serviceResponse && serviceResponse.status){
			 content = angular.fromJson(serviceResponse.content);
			 $scope.professions = content.professionList;
		}else{			
				awsmuServices.showError(response.message);
		}
	});
	
	response.error(function(data, status, headers, config) {  		        	
		awsmuServices.showError("An error occurred please try again later.");
    });
	
	
	//remove Data
	$scope.removeData = function(removeItem){
		
		var index = $scope.professions.value.indexOf(removeItem);
	    if (index != -1) 
	        $scope.professions.value.splice(index, 1);
	}
	
	
	// Edit professions Form submit
	$scope.updateAttributeProfessionsSubmit  = function(){
		
		$scope.attributeProfessionsSubmitted = true;
		$scope.loading= true;
			
		var data = {professions: JSON.stringify($scope.professions)};
		var response = awsmuServices.postData("updateProfessions",data);
		
		response.success(function(response, status, headers, config) {
			var	serviceResponse = awsmuServices.responseCheck(response)
			if(serviceResponse && serviceResponse.status){
	       			$scope.attributeProfessionsSubmitted = false;
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
	
});


/* Attributes professions add controller*/
awsmuApp.controller('attributesProfessionsAdd', function($scope, $window,$routeParams, awsmuServices) {
	//to fetch site constant
	awsmuServices.getConstant($scope);
	$scope.professions={};
	$scope.professionsDetails={};
	
	//get profession detail
	var response = awsmuServices.postData("getProfessionList/"+$routeParams.attributeId, '');
	response.success(function(response, status, headers, config) {
		var	serviceResponse = awsmuServices.responseCheck(response)
		if(serviceResponse && serviceResponse.status){
			 content = angular.fromJson(serviceResponse.content);
			 $scope.professions = content.professionList;
		}else{			
				awsmuServices.showError(response.message);
		}
	});
	
	response.error(function(data, status, headers, config) {  		        	
		awsmuServices.showError("An error occurred please try again later.");
    });
	
	
	// Add professions Form submit
	$scope.addAttributeProfessionsSubmit  = function(){
		
		$scope.professions.value.push($scope.professionsDetails);
		$scope.attributeProfessionsSubmitted = true;
		
		
		if($scope.attributeProfessionsForm.$valid) {
			// enable loading on button
			$scope.loading= true;
			console.log($scope.degreeCourses);
			
			var data = {professions: JSON.stringify($scope.professions)};
			var response = awsmuServices.postData("updateProfessions",data);
			
			response.success(function(response, status, headers, config) {
				var	serviceResponse = awsmuServices.responseCheck(response)
				if(serviceResponse && serviceResponse.status){
		       			$scope.attributeProfessionsSubmitted = false;
		       			awsmuServices.showSuccess(response.message);
		       			
			   	}else{    	   	    
			   		if(response.message !="" || response.message !=null )
						awsmuServices.showError(response.message);
					if( response.errorMap !=null)
						$scope.errors = angular.fromJson(response.errorMap);
		       	}
			       	//enable button
				$scope.loading= false;
				$scope.professionsDetails={};
		   });
			
			response.error(function(data, status, headers, config) {  		        	
				awsmuServices.showError("An error occurred please try again later.");
		    });
		}	
	 }
	
});



/* Attributes speciality view controller*/
awsmuApp.controller('attributesSpecialityView', function($scope, $window,$routeParams, awsmuServices) {
	//to fetch site constant
	awsmuServices.getConstant($scope);
	//get speciality detail
	var response = awsmuServices.postData("getSpecialityList/"+$routeParams.attributeId, '');
	
	
	response.success(function(response, status, headers, config) {
		var	serviceResponse = awsmuServices.responseCheck(response)
		if(serviceResponse && serviceResponse.status){
			 content = angular.fromJson(serviceResponse.content);
			 $scope.specialities = content.specialtyList;
		}else{			
				awsmuServices.showError(response.message);
		}
	});
	
	response.error(function(data, status, headers, config) {  		        	
		awsmuServices.showError("An error occurred please try again later.");
    });
	
});


/* Attributes degree courses edit controller*/
awsmuApp.controller('attributesSpecialityEdit', function($scope, $window,$routeParams, awsmuServices) {
	//to fetch site constant
	awsmuServices.getConstant($scope);
	//get speciality detail
	var response = awsmuServices.postData("getSpecialityList/"+$routeParams.attributeId, '');
	
	
	response.success(function(response, status, headers, config) {
		var	serviceResponse = awsmuServices.responseCheck(response)
		if(serviceResponse && serviceResponse.status){
			 content = angular.fromJson(serviceResponse.content);
			 $scope.specialities = content.specialtyList;
		}else{			
				awsmuServices.showError(response.message);
		}
	});
	//remove Data
	$scope.removeData = function(removeItem){
		
		var index = $scope.specialities.value.indexOf(removeItem);
	    if (index != -1) 
	        $scope.specialities.value.splice(index, 1);
	}
	
	// Edit speciality Form submit
	$scope.updateAttributeSpecialitiesSubmit  = function(){
		
			$scope.attributeSpecialitySubmitted = true;
		
			$scope.loading= true;
			var data = {specialities: escape(JSON.stringify($scope.specialities))};
			var response = awsmuServices.postData("updateSpecialities",data);
			
			response.success(function(response, status, headers, config) {
				var	serviceResponse = awsmuServices.responseCheck(response)
				if(serviceResponse && serviceResponse.status){
		       			$scope.attributeSpecialitySubmitted = false;
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
	
});


/* Attributes speciality add controller*/
awsmuApp.controller('attributesSpecialityAdd', function($scope, $window,$routeParams, awsmuServices) {
	
	//to fetch site constant
	awsmuServices.getConstant($scope);
	$scope.specialities={};
	
	//get speciality detail
	var response = awsmuServices.postData("getSpecialityList/"+$routeParams.attributeId, '');
	
	
	response.success(function(response, status, headers, config) {
		var	serviceResponse = awsmuServices.responseCheck(response)
		if(serviceResponse && serviceResponse.status){
			 content = angular.fromJson(serviceResponse.content);
			 $scope.specialities = content.specialtyList;
		}else{			
				awsmuServices.showError(response.message);
		}
	});
	
	// Add specialities Form submit
	$scope.addAttributeSpecialitiesSubmit  = function(){
		
		$scope.specialities.value.push($scope.speciality);
		$scope.attributeSpecialitySubmitted = true;
		
		
		if($scope.attributeSpecialityForm.$valid) {
			// enable loading on button
			$scope.loading= true;
			
			
			var data = {specialities: escape(JSON.stringify($scope.specialities))};
			var response = awsmuServices.postData("updateSpecialities",data);
			
			response.success(function(response, status, headers, config) {
				var	serviceResponse = awsmuServices.responseCheck(response)
				if(serviceResponse && serviceResponse.status){
		       			$scope.attributeSpecialitySubmitted = false;
		       			awsmuServices.showSuccess(response.message);
		       			
			   	}else{    	   	    
			   		if(response.message !="" || response.message !=null )
						awsmuServices.showError(response.message);
					if( response.errorMap !=null)
						$scope.errors = angular.fromJson(response.errorMap);
		       	}
			       	//enable button
				$scope.loading= false;
				$scope.speciality="";
		   });
			
			response.error(function(data, status, headers, config) {  		        	
				awsmuServices.showError("An error occurred please try again later.");
		    });
		}	
	 }
	
});

