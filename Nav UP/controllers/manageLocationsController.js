angular.module('navUP').controller('manageLocationsController', 
['$scope', '$location', '$routeParams', "$http",
function ($scope, $location, $routeParams, $http) 
{
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
    
	$scope.ViewLocations = true;

	$http.get("/viewLocations").then(function(response)
	{
		if(response.status == 200)
		{
			$scope.Locations = response.data;
		}
		else
		{
			console.log("something went wrong");
		}
	});

	$scope.editLocation = function(id)
	{
		$scope.ViewLocations = false;
		$scope.EditLocation = true;

		for(var key in $scope.Locations)
		{
			if($scope.Locations[key].id == id)
			{
				$scope.locationName = $scope.Locations[key].name;
				$scope.locationID = $scope.Locations[key].id;
			}
		}
	}

	$scope.saveEditLocation = function()
	{				
		var name = $scope.locationName;
		var id = $scope.locationID;

		$http.post("/editLocation",
		{
			name:name, id:id
		})
		.then(function(response)
		{
			alert(response.data);

			$http.get("/viewLocations").then(function(response)
			{
				if(response.status == 200)
				{
					$scope.Locations = response.data;
				}
				else
				{
					console.log("something went wrong");
				}
			});
			
			$scope.EditLocation = false;
			$scope.ViewLocations = true;
		});
	}
	

	$scope.back = function()
	{
		$scope.ViewLocations = true;
		$scope.EditLocation = false;
		$scope.AddLocation = false;
		$scope.prompt = false;
	}

	$scope.addLocation = function()
	{
		$scope.ViewLocations = false;
		$scope.AddLocation = true;
	}

	$scope.saveLocation = function()
	{
		$http.post("/addLocation",
		{
			name: $scope.name
		})
		.then(function(response)
		{
			alert(response.data);

			$http.get("/viewLocations").then(function(response)
			{
				$scope.Locations = response.data;
			});

			$scope.AddLocation = false;
			$scope.ViewLocations = true;
		});
	}

	$scope.toDelete;

	$scope.deleteLocationPrompt = function(id)
	{
		$scope.toDelete = id;

		$scope.promptBody = "Are you sure you want to delete this location?"
		$scope.ViewLocations = false;
		$scope.prompt = true;
	}

	$scope.deleteLocation = function()
	{
		$http.post("/deleteLocation",
		{
			id: $scope.toDelete
		})
		.then(function(response)
		{
			alert(response.data);

			$http.get("/viewLocations").then(function(response)
			{
				$scope.Locations = response.data;
			});

			$scope.prompt = false;
			$scope.ViewLocations = true;
		});
	}
}]);