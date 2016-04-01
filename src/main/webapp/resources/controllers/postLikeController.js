/*Post grid listing */
awsmuApp.controller('postLikeCtrl', function($scope,$window,$location,$routeParams,$compile,awsmuServices) {
	//to fetch site constant
	awsmuServices.getConstant($scope);
	var op = ":All;1:Yes;0:No";
	var arr = ['0','1'];
	var opreal = ":All;0:Yes;1:No";
	var sym;
	var id = '';
	var activeInactiveStatus;
	$scope.postId = $routeParams.postId;
	$scope.patientdata = [];
	$scope.gridapi = {};
	$scope.config = {
				url: baseUrl+'getPostLikes/'+$scope.postId,
				datatype: "json",
				colNames: ['Like Id', 'Liked By','Active' ,'createdDate','Action'],
				colModel: [
                           {name: '_id', index: '_id',width:100, align: "center", search:true, sortable:true, formatter: function (cellvalue) {  id = cellvalue; return cellvalue;}
    		               },
    		               {name: 'user',width:100, index: 'user.displayName', align: "left", search:true, sortable:true, formatter: function (cellvalue) {  return '<a href ="#/userView/'+cellvalue.userId+'">'+cellvalue.displayName+'</a>'; }
				           },					     
				        {name: 'isActive',width:70, index: 'isActive', align: "center",search:true, stype:"select",searchoptions:{
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
							 activeInactiveStatus =  cellvalue;
				                if(cellvalue==1)
                                   return 'Yes';
					             else
                                   return 'No';  
                         }},
                        {name: 'createdDate',width:100, index: 'createdDate',search:false,  align: "center",searchoptions:{sopt: ['dt']}},
                        
                        {name: '_id', index: '_id', width:150, align: "center",search:false, sortable:false,
                            formatter: function (cellvalue) {
                            	var activeInactiveText = "InActive";
                            	if(!activeInactiveStatus==1) {
                            		activeInactiveText = "Active";
                            	} 
                                return '<a href="javascript:void(0)" ng-click="activeInactiveLike(\''+cellvalue+'\','+activeInactiveStatus+',\''+activeInactiveText+'\')" class="activeInactiveLink">'+activeInactiveText+'</a>';
                             }
                        } 
				],
		rowNum: 5,
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
			$compile(angular.element('.activeInactiveLink'))($scope);
		}
	
		};
	$scope.loadrecord=function(){
	 $scope.gridapi.insert($scope.modeldata);
	}
	$scope.activeInactiveLike = function(likeId,activeInactiveStatus,activeInactiveText) {
		bootbox.confirm("Are you sure you want to "+activeInactiveText+" this like?", function(result) {			
			if(result){
				var data = {likeId:likeId,active:activeInactiveStatus==1?0:1,postId:$scope.postId};							
				var deleteResponse = awsmuServices.postData("activeInactiveLike", data);		
				deleteResponse.success(function(response, status, headers, config) {
					var	response = awsmuServices.responseCheckLogin(response);					
					if(response && response.status){
						awsmuServices.showSuccess(response.message);
						$window.location.href= "#/";
						$window.location.href= "#/postView/"+$scope.postId;
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