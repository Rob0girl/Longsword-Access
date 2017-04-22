angular.module('navUP').controller('manageGisController', 
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

	//manage gis stuff

	$scope.findGISObject = true;

	$http.get("/findGisObject").then(function(response)
	{
		if(response.status == 200)
		{
			$scope.GISObjects = response.data;
		}
		else
		{
			console.log("Error Occurred");
		}
	});

	$scope.addGISObject = function()
	{
		$scope.findGISObject = false;
		$scope.AddGISObject = true;
	}

	$scope.saveGISObject = function()
	{
		$http.post("/addGISObject",
		{
			name: $scope.name,
			coordinates: $scope.coordinates
		})
		.then(function(response)
		{
			alert(response.data);

			$http.get("/findGISObject").then(function(response)
			{
				$scope.GISObjects = response.data;
			});

			$scope.addGISObject = false;
			$scope.findGISObject = true;
		});
	}

	$scope.back = function()
	{
		$scope.findGISObject = true;
		$scope.EditGISObj = false;
		$scope.AddGISObject = false;
		$scope.GISDeletePrompt = false;
	}

	$scope.editGISObject = function(id)
	{
		$scope.findGISObject = false;
		$scope.EditGISObj = true;

		for(var key in $scope.GISObjects)
		{
			if($scope.GISObjects[key].id == id)
			{
				$scope.GISObjectID = $scope.GISObjects[key].id;
				$scope.GISObjectName = $scope.GISObjects[key].name;
				$scope.GISObjectCoordinates = $scope.GISObjects[key].coordinates;
			}
		}
	}

	$scope.saveEditLocation = function()
	{
		var id = $scope.GISObjectID;				
		var name = $scope.GISObjectName;
		var coordinates = $scope.GISObjectCoordinates;

		$http.post("/editGISObject",
		{
			id:id, name:name, coordinates:coordinates 
		})
		.then(function(response)
		{
			alert(response.data);

			$http.get("/findGISObject").then(function(response)
			{
				if(response.status == 200)
				{
					$scope.GISObjects = response.data;
				}
				else
				{
					console.log("Error Occurred");
				}
			});
			
			$scope.EditLocation = false;
			$scope.ViewLocations = true;
		});
	}

	$scope.toDelete;

	$scope.deleteGISObjectPrompt = function(id)
	{
		$scope.deletThis = id;

		$scope.deleteGISBody = "Are you sure you want to delete this location?"
		$scope.findGISObject = false;
		$scope.GISDeletePrompt = true;
	}

	$scope.deleteGISObject = function()
	{
		$http.post("/deleteGISObject",
		{
			id: $scope.deletThis
		})
		.then(function(response)
		{
			alert(response.data);

			$http.get("/findGISObject").then(function(response)
			{
				$scope.GISObjects = response.data;
			});

			$scope.GISDeletePrompt = false;
			$scope.findGISObject = true;
		});
	}


}]);
