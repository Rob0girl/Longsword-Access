angular.module('navUP').controller('manageUsersController', 
['$scope', '$http', '$routeParams', '$location',function ($scope, $http, $routeParams, $location){

	var user = $routeParams.user.split(",");
	var stud_num = user[0];
	var status = user[1];

	$scope.navigateNav = true;

    if(status == "user" || status == "admin")
    {
        $scope.profileNav = true;
        $scope.poiNav = true;
        $scope.eventsNav= true;
    }

    if(status == "admin")
    {
        $scope.manageGisNav = true;
        $scope.manageUsersNav = true;
        $scope.manageLocationsNav = true;
        $scope.manageEventsNav = true;
    }

    $scope.home = function()
    {
        $location.path("/home" + user);
    }
    
    $scope.profile = function()
    {
        $location.path("/profile" + user);
    }

    $scope.navigate = function()
    {
        $location.path("/navigate" + user);
    }

    $scope.poi = function()
    {
        $location.path("/poi" + user);
    }

    $scope.manageUsers = function()
    {
        $location.path("/manageUsers" + user);
    }

    $scope.manageGis = function()
    {
        $location.path("/manageGIS" + user);
    }

    $scope.manageLocations = function()
    {
        $location.path("/manageLocations" + user);
    }

	$scope.getUser = function()
	{
		$http.post("/getUser",
		{
			studentNumber: $scope.studentNumber
		})
		.then(function(response)
		{
			$scope.name = response.data.name;
			$scope.surname = response.data.surname;
		});
	}

	$scope.addAdminRights = function()
	{
		$http.post("/addAdminRights",
		{
			studentNumber: $scope.studentNumber
		})
		.then(function(response)
		{
			alert(response.data);
		});
	}

	$scope.removeAdminRights = function()
	{
		$http.post("/removeAdminRights",
		{
			studentNumber: $scope.studentNumber
		})
		.then(function(response)
		{
			alert(response.data);
		});
	}

	$scope.deleteProfile = function()
	{
		$http.post("/deleteProfile",
		{
			studentNumber: $scope.studentNumber
		})
		.then(function(response)
		{
			alert(response.data);
		});
	}

}]);