angular.module('navUP').controller('poiController',
    ['$scope', '$location', '$routeParams',
        function ($scope, $location, $routeParams)
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

            // poi stuff
            
            $scope.searchLocation = function()
            {
                $scope.searchedLocations = true;

                function hasTerm(itemText) {
                    if (itemText.indexOf($scope.locationString) >= 0) {
                        return true;
                    }
                }

                $scope.searchedItems = $scope.items.filter(hasTerm);


                //$location.path("/searchLocation" + $scope.locationString);
            }

            $scope.saveLocation = function()
            {
                $scope.locationSaved = true;
                //$location.path("/saveLocation");
            }

            $scope.getCurrentDeviceLocation = function()
            {
                $scope.locationReceived = true;
                $scope.locationReceivedString = "IT 4-66, University of Pretoria"
                //$location.path("/getCurrentDeviceLocation");
            }

            if(status === "admin")
            {
                $scope.addLocation = function()
                {
                    $scope.items.push($scope.addLocationString);
                    //$location.path("/addLocation" + locationString);
                }

                $scope.modifyLocation = function()
                {
                    $location.path("/modifyLocation" + locationString);
                }

                $scope.removeLocation = function()
                {
                    $location.path("/removeLocation" + locationString);
                }
            }
        }]);