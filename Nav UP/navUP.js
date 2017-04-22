var navUP = angular.module('navUP', ['ngRoute']);

navUP.config(['$routeProvider', '$locationProvider', function ($routeProvider, $locationProvider)
{
    $routeProvider
    .when("/", 
    {
        templateUrl : "login.html",
        controller: "authenticateController"
    })
    .when("/register",
    {
        templateUrl: "register.html",
        controller: "authenticateController"
    })
    .when("/navigate:user",
    {
        templateUrl: "navigate.html",
        controller: "navigateController"
    })
    .when("/home:user",
    {
        templateUrl: "home.html",
        controller: "homeController"
    })
    .when("/profile:user", 
    {
        templateUrl: "profile.html",
        controller: "profileController"
    })
    .when("/manageGIS:user",
    {
        templateUrl: "manageGIS.html",
        controller: "manageGisController"
    })
    .when("/manageLocations:user",
    {
        templateUrl: "manage-locations.html",
        controller: "manageLocationsController"
    })
    .when("/manageUsers:user", 
    {
        templateUrl: "manageUsers.html",
        controller: "manageUsersController"
    })
    .when("/poi:user",
    {
        templateUrl: "poiSection.html",
        controller: "poiController"
    })
    .otherwise(
    {
        redirectTo: '/'
    });

}]);
