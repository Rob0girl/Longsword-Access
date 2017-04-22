angular.module('navUP').controller('profileController', 
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

    $scope.profileForm = true;

    $scope.fname = null;
    $scope.sname = null;
    $scope.email = null;
    $scope.stud_num = null;
    $scope.password = null;
    $scope.phone = null;

    $http.post("/getUser",
    {
        studentNumber:stud_num
    })
    .then(function(response)
    {
        if(response.status == 200)
        {
            if(response.data == "user not found")
            {
                alert("The profile could not be retreived!");
            }
            else
            {
                var userObj = response.data;

                $scope.fname = userObj.name;
                $scope.sname = userObj.surname;
                $scope.email = userObj.email;
                $scope.stud_num = userObj.studentNumber;
                $scope.password = userObj.password;
            }
        }
    });

    $scope.saveProfile = function()
    {
        var userObj = new Object();
        userObj.fname = $scope.fname;
        userObj.sname = $scope.sname;
        userObj.email = $scope.email;
        userObj.stud_num = $scope.stud_num;
        userObj.password = $scope.password;
        userObj.phone = $scope.phone;

        //send rewuest
        $http.post("/saveProfile",
        {
            user:userObj
        })
        .then(function(response)
        {
            alert(response.data);

            $location.path("/home" + user);
        });
    }

    $scope.deleteProfile = function()
    {
        $http.post("/deleteProfile",
        {
            studentNumber: $scope.stud_num
        })
        .then(function(response)
        {
            alert(response.data);
            $location.path("/");
        });
    }
}]);