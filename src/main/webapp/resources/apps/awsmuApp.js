//define app 
var awsmuApp = angular.module('awsmuApp', [
  'ngRoute',
  'ngStorage',
  'ui.bootstrap',
  'nya.bootstrap.select',
  'ngImgCrop',
  'ngFileUpload',
  'ngSanitize',
  'dndLists'
]);
//define interceptor to handle http request
function routeInterceptor($q, $location, $localStorage) {
  return {
    request: function(config) {
    	
    	//check if we want to allow interceptor to check request if ("?i" not exits in url)
    	if (  config.url.indexOf('?i') === -1) {
    		
    		//check if current user is active
    		if($localStorage.loggedIn == false)
    			$location.path('/login');
    		else if($localStorage.loggedIn == true && $location.$$path == '/login')
    			$location.path('/dashboard');
    		else if($localStorage.loggedIn == true){
    			
    		}
    		else
    			$location.path('/login');
        }       
    	return config;
    },

    //if error occure on sending request to server
    requestError: function(config) {
    	
    	$location.path('/login');
    	return config;
    },

    //check response 
    response: function(res) {
    	
      return res;
    },

    //if error occur on receiving response for server
    responseError: function(res) {
    
      return res;
    }
  }
  
}

//add interceptor
awsmuApp.factory('routeInterceptor', routeInterceptor);

//add routing
awsmuApp.config(function($routeProvider, $httpProvider) {
	
	$routeProvider.
	when('/login', {
	  templateUrl: viewPath+'login.html',
	  controller: 'LoginCtrl'
	  
	}).
	when('/logout', {
		  templateUrl: viewPath+'login.html?i',
		  controller: 'LogoutCtrl'
		  
	}).
	when('/dashboard', {
		  templateUrl: viewPath+'dashboard.html',
		  controller: 'dashboardCtrl'
	}).
	when('/users', {
		  templateUrl: viewPath+'usersList.html',
		  controller: 'usersListCtrl'
	}).
	when('/userView/:userId', {
		  templateUrl: viewPath+'userView.html',
		  controller: 'userViewCtrl'
	}).
	when('/userEdit/:userId', {
		  templateUrl: viewPath+'userEdit.html',
		  controller: 'userEditCtrl'
	}).
	when('/planners', {
		  templateUrl: viewPath+'plannersList.html',
		  controller: 'plannersListCtrl'
	}).
	when('/plannerEdit/:plannerId', {
		  templateUrl: viewPath+'plannerEdit.html',
		  controller: 'plannerEditCtrl'
	}).
	when('/userPlannersList/:userId', {
		  templateUrl: viewPath+'userPlannersList.html',
		  controller: 'userPlannersListCtrl'
	}).
	when('/allUserPlannersList', {
		  templateUrl: viewPath+'allUserPlannersList.html',
		  controller: 'allUserPlannersListCtrl'
	}).
	when('/plannerView/:plannerId', {
		  templateUrl: viewPath+'plannerView.html',
		  controller: 'plannerViewCtrl'
	}).
	when('/userPlannerView/:userPlannerId', {
		  templateUrl: viewPath+'userPlannerView.html',
		  controller: 'userPlannerViewCtrl'
	}).
	when('/userPlannerHistory/:userPlannerId', {
		  templateUrl: viewPath+'userPlannerHistory.html',
		  controller: 'userPlannerHistoryCtrl'
	}).
	when('/userPlannerEdit/:userPlannerId', {
		  templateUrl: viewPath+'userPlannerEdit.html',
		  controller: 'userPlannerEditCtrl'
	}).
	when('/bookletsList', {
		  templateUrl: viewPath+'bookletsList.html',
		  controller: 'bookletsListCtrl'
	}).
	when('/userBookletsList/:userId', {
		  templateUrl: viewPath+'userBookletsList.html',
		  controller: 'userBookletsListCtrl'
	}).
	when('/bookletView/:bookletId', {
		  templateUrl: viewPath+'bookletView.html',
		  controller: 'bookletViewCtrl'
	}).
	when('/bookletEdit/:bookletId', {
		  templateUrl: viewPath+'bookletEdit.html',
		  controller: 'bookletEditCtrl'
	}).
	when('/bookletAdd/:userId', {
		  templateUrl: viewPath+'bookletAdd.html',
		  controller: 'bookletAddCtrl'
	}).
	when('/doctorsList', {
		  templateUrl: viewPath+'doctorsList.html', 
		  controller: 'doctorsListCtrl'
	}).
	when('/doctorView/:doctorId', {
		  templateUrl: viewPath+'doctorView.html',
		  controller: 'doctorViewCtrl'
	}).
	when('/doctorEdit/:doctorId', {
		  templateUrl: viewPath+'doctorEdit.html',
		  controller: 'DoctorEditCtrl'
	}).
	when('/doctorAdd', {
		  templateUrl: viewPath+'doctorAdd.html',
		  controller: 'DoctorAddCtrl'
	}).
	when('/userDoctorsList', {
		  templateUrl: viewPath+'userDoctorsList.html',
		  controller: 'UserDoctorsListCtrl'
	}).
	when('/userDoctorAdd/:userId', {
		  templateUrl: viewPath+'userDoctorAdd.html',
		  controller: 'UserDoctorAddCtrl'
	}).
	when('/userDoctorEdit/:doctorId', {
		  templateUrl: viewPath+'userDoctorEdit.html',
		  controller: 'UserDoctorEditCtrl'
	}).
	when('/userDoctorView/:doctorId', {
		  templateUrl: viewPath+'userDoctorView.html',
		  controller: 'UserDoctorViewCtrl'
	}).
	when('/celebrities', {
		  templateUrl: viewPath+'celebritiesList.html?i',
		  controller: 'celebritiesListCtrl'
	}).
	when('/emergency', {
		  templateUrl: viewPath+'emergency.html?i',
		  controller: 'emergencyCtrl'
	}).
	
	when('/celebritiesAdd', {
		  templateUrl: viewPath+'celebritiesAdd.html?i',
		  controller: 'celebritiesAddCtrl'
	}).
	when('/celebritiesEdit/:celebId', {
		  templateUrl: viewPath+'celebritiesEdit.html?i',
		  controller: 'celebritiesEditCtrl'
	}).
	when('/celebritiesView/:celebId', {
		  templateUrl: viewPath+'celebritiesView.html?i',
		  controller: 'celebritiesViweCtrl'
	}).
	when('/activities', {
		  templateUrl: viewPath+'activitiesList.html?i',
		  controller: 'activitiesListCtrl'
	}).
	when('/activitiesAdd', {
		  templateUrl: viewPath+'activitiesAdd.html?i',
		  controller: 'activitiesAddCtrl'
	}).
	when('/activitiesEdit/:activityId', { 
		  templateUrl: viewPath+'activitiesEdit.html?i',
		  controller: 'activitiesEditCtrl'
	}).
	when('/activitiesView/:activityId', {
		  templateUrl: viewPath+'activitiesView.html?i',
		  controller: 'activitiesViweCtrl'
	}).
	when('/trends', {
		  templateUrl: viewPath+'trendsList.html?i',
		  controller: 'trendsListCtrl'
	}).
	when('/trendsAdd', {
		  templateUrl: viewPath+'trendsAdd.html?i',
		  controller: 'trendsAddCtrl'
	}).
	when('/trendsEdit/:trendId', { 
		  templateUrl: viewPath+'trendsEdit.html?i',
		  controller: 'trendsEditCtrl'
	}).
	when('/trendsView/:trendId', {
		  templateUrl: viewPath+'trendsView.html?i',
		  controller: 'trendsViweCtrl'
	}).
	when('/tips', {
		  templateUrl: viewPath+'tipsList.html?i',
		  controller: 'tipsListCtrl'
	}).
	when('/tipsAdd', {
		  templateUrl: viewPath+'tipsAdd.html?i',
		  controller: 'tipsAddCtrl'
	}).
	when('/tipsEdit/:tipId', { 
		  templateUrl: viewPath+'tipsEdit.html?i',
		  controller: 'tipsEditCtrl'
	}).
	when('/tipsView/:tipId', {
		  templateUrl: viewPath+'tipsView.html?i',
		  controller: 'tipsViweCtrl'
	}).
	when('/activityCategories', {
		  templateUrl: viewPath+'activityCategoriesList.html?i',
		  controller: 'activityCategoriesListCtrl'
	}).
	when('/activityCategoriesAdd', {
		  templateUrl: viewPath+'activityCategoriesAdd.html?i',
		  controller: 'activityCategoriesAddCtrl'
	}).
	when('/activityCategoriesEdit/:activityCategoryId', { 
		  templateUrl: viewPath+'activityCategoriesEdit.html?i',
		  controller: 'activityCategoriesEditCtrl'
	}).
	when('/activityCategoriesView/:activityCategoryId', {
		  templateUrl: viewPath+'activityCategoriesView.html?i',
		  controller: 'activityCategoriesViweCtrl'
	}).
	when('/problems', {
		  templateUrl: viewPath+'problemsList.html?i',
		  controller: 'problemsListCtrl'
	}).
	when('/problemsAdd', {
		  templateUrl: viewPath+'problemsAdd.html?i',
		  controller: 'problemsAddCtrl'
	}).
	when('/problemsEdit/:problemId', { 
		  templateUrl: viewPath+'problemsEdit.html?i',
		  controller: 'problemsEditCtrl'
	}).
	when('/problemsView/:problemId', {
		  templateUrl: viewPath+'problemsView.html?i',
		  controller: 'problemsViweCtrl'
	}).
	when('/posts', {
		  templateUrl: viewPath+'postsList.html?i',
		  controller: 'postsListCtrl'
	}).
	when('/postView/:postId', {
		  templateUrl: viewPath+'postView.html',
		  controller: 'postsViewCtrl'
	}).
	when('/postEdit/:postId', {
		  templateUrl: viewPath+'postEdit.html',
		  controller: 'postsEditCtrl'
	}).
	when('/postComments/:postId', {
		  templateUrl: viewPath+'postCommentList.html',
		  controller: 'postCommentCtrl'
	}).
	when('/postCommentEdit/:postId/:commentId', {
		  templateUrl: viewPath+'postCommentEdit.html',
		  controller: 'postCommentEditCtrl'
	}).
	
	
	when('/attributes', {
		  templateUrl: viewPath+'attributesList.html?i',
		  controller: 'attributesListCtrl'
	}).
	when('/attributesCountryView/:attributeId', {
		  templateUrl: viewPath+'attributesCountryView.html?i',
		  controller: 'attributesCountryView'
	}).
	when('/attributesCountryEdit/:attributeId', {
		  templateUrl: viewPath+'attributesCountryEdit.html?i',
		  controller: 'attributesCountryEdit'
	}).
	when('/attributesCountryAdd/:attributeId', {
		  templateUrl: viewPath+'attributesCountryAdd.html?i',
		  controller: 'attributesCountryAdd'
	}).
	when('/attributesdegreeCoursesView/:attributeId', {
		  templateUrl: viewPath+'attributesDegreeCoursesView.html?i',
		  controller: 'attributesDegreeCoursesView'
	}).
	when('/attributesdegreeCoursesEdit/:attributeId', {
		  templateUrl: viewPath+'attributesDegreeCoursesEdit.html?i',
		  controller: 'attributesDegreeCoursesEdit'
	}).
	when('/attributesdegreeCoursesAdd/:attributeId', {
		  templateUrl: viewPath+'attributesDegreeCoursesAdd.html?i',
		  controller: 'attributesDegreeCoursesAdd'
	}).
	when('/attributesProfessionView/:attributeId', {
		  templateUrl: viewPath+'attributesProfessionView.html?i',
		  controller: 'attributesProfessionView'
	}).
	when('/attributesProfessionEdit/:attributeId', {
		  templateUrl: viewPath+'attributesProfessionEdit.html?i',
		  controller: 'attributesProfessionEdit'
	}).
	when('/attributesProfessionAdd/:attributeId', {
		  templateUrl: viewPath+'attributesProfessionAdd.html?i',
		  controller: 'attributesProfessionsAdd'
	}).
	when('/attributesSpecialityView/:attributeId', {
		  templateUrl: viewPath+'attributesSpecialityView.html?i',
		  controller: 'attributesSpecialityView'
	}).
	when('/attributesSpecialityEdit/:attributeId', {
		  templateUrl: viewPath+'attributesSpecialityEdit.html?i',
		  controller: 'attributesSpecialityEdit'
	}).
	when('/attributesSpecialityAdd/:attributeId', {
		  templateUrl: viewPath+'attributesSpecialityAdd.html?i',
		  controller: 'attributesSpecialityAdd'
	}).
	when('/questions', {
		  templateUrl: viewPath+'questionsList.html?i',
		  controller: 'questionsListCtrl'
	}).
	when('/questionsView/:questionId', {
		  templateUrl: viewPath+'questionsView.html?i',
		  controller: 'questionsViewCtrl'
	}).
	when('/questionsEdit/:questionId', {
		  templateUrl: viewPath+'questionsEdit.html?i',
		  controller: 'questionsEditCtrl'
	}).
	when('/questionsAdd', {
		  templateUrl: viewPath+'questionsAdd.html?i',
		  controller: 'questionsAddCtrl'
	}).
	when('/messages', {
		  templateUrl: viewPath+'messageList.html?i',
		  controller: 'messageListCtrl'
	}).
	when('/messageThread/:chainId', {
		  templateUrl: viewPath+'messageThread.html?i',
		  controller: 'messageThreadCtrl'
	}).
	when('/plannerGroup', {
		  templateUrl: viewPath+'plannerGroupList.html?i',
		  controller: 'plannerGroupListCtrl'
	}).
	when('/plannerGroupView/:problemId', {
		  templateUrl: viewPath+'plannerGroupView.html?i',
		  controller: 'plannerGroupViewCtrl'
	}).
	when('/connections/', {
		  templateUrl: viewPath+'connectionsList.html?i',
		  controller: 'connectionsListCtrl'
	}).
	when('/connectionView/:connectionId', {
		  templateUrl: viewPath+'connectionsView.html?i',
		  controller: 'connectionsViewCtrl'
	}).
	when('/connectionEdit/:connectionId', {
		  templateUrl: viewPath+'connectionsEdit.html?i',
		  controller: 'connectionsEditCtrl'
	}).
	when('/userConnectionList/:userId', {
		  templateUrl: viewPath+'userConnectionsList.html?i',
		  controller: 'userConnectionsListCtrl'
	}).
	otherwise({
	  redirectTo: '/login'
	});
	  
	$httpProvider.interceptors.push('routeInterceptor');
})
                    
