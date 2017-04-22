angular.module('navUP').controller('navigateController', 
['$scope', '$location', '$routeParams', '$http',
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

    //dummy call to get location

    // $http.post("/getLocation").then(function(response)
    // {
    //     alert(response.status);
    // });

    var map;
    
    function initMap() {

    //up campus coordinates -25.752163658, 28.224499102
    //it building coordinates: -25.755627, 28.232553
    //emb building coordinates: -25.7552303, 28.2324397

    map = new google.maps.Map(document.getElementById('map'), {
          center: {lat: -25.755033333333333, lng: 28.23083333333333},
          zoom: 16
        });
    }

    $scope.getLocation = function() {
        var marker = new google.maps.Marker({
        position: {lat: -25.755627, lng: 28.232553},
        map: map,
        title: "My Location",
        });  

        map.setZoom(20);
        map.panTo(marker.position); 
    } 

    $scope.calculateRoute = function(locationFrom, locationTo) {
        navigator.geolocation.getCurrentPosition(function(position) {

            //get location
            var directionsService = new google.maps.DirectionsService();
            var directionsDisplay = new google.maps.DirectionsRenderer();

            // var pos = {
            //   lat: position.coords.latitude,
            //   lng: position.coords.longitude
            // };

            var from = locationFrom.split(",");
            var to = locationTo.split(",");

            var start = new google.maps.LatLng(from[0], from[1]);
            var end = new google.maps.LatLng(to[0], to[1]);
            
            var bounds = new google.maps.LatLngBounds();
            bounds.extend(start);
            bounds.extend(end);
            map.fitBounds(bounds);
            var request = {
                origin: start,
                destination: end,
                travelMode: google.maps.TravelMode.WALKING
            };

            //create route
            directionsDisplay.setMap(null);
            directionsService.route(request, function (response, status) 
            {
                if (status == google.maps.DirectionsStatus.OK) 
                {
                    directionsDisplay.setDirections(response);
                    directionsDisplay.setMap(map);
                } 
                else 
                {
                    alert("Directions Request from " + start.toUrlValue(6) + " to " + end.toUrlValue(6) + " failed: " + status);
                }
            });
        });
    }

    initMap();

}]);