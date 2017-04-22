var express = require('express');
var app = express();
var path = require('path');
var bodyParser = require('body-parser');
var nodemailer = require('nodemailer');
// var nsq = require('nsqjs');

app.use(bodyParser.json());

// //nsq writer
// var write = new nsq.Writer('127.0.0.1', 4150);
// write.connect();

// write.on("closed", function()
// {
// 	console.log("NSQ writer closed");
// });

// //nsq reader
// var read;

var GISObjectArray = [
						{name:"IT Building", id:1, coordinates:"1,1"}, 
						{name:"Humanities", id:2, coordinates:"2,2"}, 
						{name:"Centenery", id:3, coordinates:"3,3"}, 
						{name:"Chancellors Building", id:4, coordinates:"4,4"},
						{name:"Aula", id:5, coordinates:"5,5"},
						{name:"Engineering 3", id:6, coordinates:"6,6"},
						{name:"Sci-Enza", id:7, coordinates:"7,7"},
						{name:"Bio-Informatics", id:8, coordinates:"8,8"},
						{name:"Postgraduate Center", id:9, coordinates:"9,9"},
						{name:"Student Center", id:10, coordinates:"10,10"}
					];

var locationArray = [
						{name:"IT Building", id:1}, 
						{name:"Humanities", id:2}, 
						{name:"Centenery", id:3}, 
						{name:"Chancellors Building", id:4},
						{name:"Aula", id:5},
						{name:"Engineering 3", id:6},
						{name:"Sci-Enza", id:7},
						{name:"Bio-Informatics", id:8},
						{name:"Postgraduate Center", id:9},
						{name:"Student Center", id:10}
					];

var userArray = [{id:1, fname:"Munya", sname:"Mpofu", email:"munya@gmail.com", stud_num:"u15071830", password:"1234", phone:"1234567890", status:"admin"}];

app.get('/', function(request, response) 
{
    response.sendFile(path.join(__dirname + '/index.html'));
    app.use(express.static(__dirname));
});

/************************************************Manage GIS**********************************************/

app.get('/findGISObject', function(request, response){
	response.json(GISObjectArray);

	
});

app.get('/findCurrentCoordinates', function(request, response){
	response.json(GISObjectArray[1].coordinates);
	// Regarding the full functionality here the communication spec specifies that the device's MAC address should be passed through in the nsq message,
	// however this isn't something acheivable in JS as far as I understand as it would entail disabling nearly all security for the browser being used.
    // Furthermore while this can be done using activeX objects, the clients would all need Windows Management Instrumentation installed as a prereq.
	// Below is a code block that in theory should return the MAC addresses for the devices current connections.

	/*var macAddress = "";
    var ipAddress = "";
    var computerName = "";
    var wmi = GetObject("winmgmts:{impersonationLevel=impersonate}");
    e = new Enumerator(wmi.ExecQuery("SELECT * FROM Win32_NetworkAdapterConfiguration WHERE IPEnabled = True"));
    for(; !e.atEnd(); e.moveNext()) {
        var s = e.item(); 
        macAddress = s.MACAddress;
        ipAddress = s.IPAddress(0);
        computerName = s.DNSHostName;
    }
*/

	// var getLocation = new Object();
	// getLocation.src = "Access";
	// getLocation.dest = "Data";
	// getLocation.msgType = "request";
	// getLocation.queryType = "getLocation";
	// getLocation.content = new Object();
	// getLocation.content.mac = ipAddress;
	

	// var getLocationRequestJson = JSON.stringify(getLocation);

	// //send request
	// write.on("ready", function()
	// {
	// 	write.publish("data", getLocationRequestJson);
	// });

	// //receive respose from user module NSQ
	// read = new nsq.Reader('data', 'navup', { lookupdHTTPAddresses : '127.0.0.1:4161', nsqdTCPAddresses : 'localhost:4150' });
	// read.connect();

	// read.on("message", function(msg)
	// {
	// 	console.log("Recieved location: " + msg.id + msg.body.toString());
	// 	msg.finish();
	// });

	// read.on("closed", function()
	// {
	// 	console.log("NSQ reader closed");
	// });


});

app.post("/editGISObject", function(request, response){
	var GISObject = request.body;
	console.log(GISObject);
	var edit = false;
	for(var key in GISObjectArray){
		if(GISObjectArray[key].id == GISObject.id){
			GISObjectArray[key] = GISObject;
			edit = true;
		}
	}
	if(edit == false){
		response.send("GIS Object edit failed!");
	}
	else{
		response.send("GIS Object has been edited");
    	}

    // var updateLocation = new Object();
	// updateLocation.src = "Access";
	// updateLocation.dest = "GIS";
	// updateLocation.msgType = "request";
	// updateLocation.queryType = "updateLocation";
	// updateLocation.content = new Object();
	// updateLocation.content.from = request.body.from;
	// updateLocation.content.to = request.body.to;
	

	// var updateLocationRequestJson = JSON.stringify(updateLocation);

	// //send request
	// write.on("ready", function()
	// {
	// 	write.publish("gis", updateLocationRequestJson);
	// });

	// //receive respose from user module NSQ
	// read = new nsq.Reader('gis', 'navup', { lookupdHTTPAddresses : '127.0.0.1:4161', nsqdTCPAddresses : 'localhost:4150' });
	// read.connect();

	// read.on("message", function(msg)
	// {
	// 	console.log("Location edited: " + msg.id + msg.body.toString());
	// 	msg.finish();
	// });

	// read.on("closed", function()
	// {
	// 	console.log("NSQ reader closed");
	// });

});



app.post("/addGISObject", function(request, response){
	var GISObject = new Object();
	try{
		GISObject.name = request.body.name;
		GISObject.id = GISObjectArray[GISObjectArray.length - 1].id + 1;
		GISObjectArray.push(GISObject);
		response.send("The GIS Object has been added successfully");
	}
	catch(error){
		response.send("The GIS Object could not be added");
	}
	// INTENDED FUNCTIONALITY

	// var addLocation = new Object();
	// addLocation.src = "Access";
	// addLocation.dest = "GIS";
	// addLocation.msgType = "request";
	// addLocation.queryType = "addLocation";
	// addLocation.content = new Object();
	// addLocation.content.location = request.body.name;
	

	// var addLocationRequestJson = JSON.stringify(addLocation);

	// //send request
	// write.on("ready", function()
	// {
	// 	write.publish("gis", addLocationRequestJson);
	// });

	// //receive respose from user module NSQ
	// read = new nsq.Reader('gis', 'navup', { lookupdHTTPAddresses : '127.0.0.1:4161', nsqdTCPAddresses : 'localhost:4150' });
	// read.connect();

	// read.on("message", function(msg)
	// {
	// 	console.log("GIS Object added: " + msg.id + msg.body.toString());
	// 	msg.finish();
	// });

	// read.on("closed", function()
	// {
	// 	console.log("NSQ reader closed");
	// });
});

app.post("/deleteGISObject", function(request, response){
	var deleted = false;
	var id = request.body.id;
	console.log(id);
	for(var key in GISObjectArray){
		if(GISObjectArray[key].id == id){
			GISObjectArray.splice(key, 1);
			deleted = true;
		}
	}
	if(deleted == false){
		response.send("The GIS Object could not be deleted");
	}
	else{
		response.send("The GIS Object was successfully deleted");
    	}

    // var deleteLocation = new Object();
	// deleteLocation.src = "Access";
	// deleteLocation.dest = "GIS";
	// deleteLocation.msgType = "request";
	// deleteLocation.queryType = "deleteLocation";
	// deleteLocation.content = new Object();
	// deleteLocation.content.id = id;
	

	// var deleteLocationRequestJson = JSON.stringify(deleteLocation);

	// //send request
	// write.on("ready", function()
	// {
	// 	write.publish("gis", deleteLocationRequestJson);
	// });

	// //receive respose from user module NSQ
	// read = new nsq.Reader('gis', 'navup', { lookupdHTTPAddresses : '127.0.0.1:4161', nsqdTCPAddresses : 'localhost:4150' });
	// read.connect();

	// read.on("message", function(msg)
	// {
	// 	console.log("GIS Object deleted: " + msg.id + msg.body.toString());
	// 	msg.finish();
	// });

	// read.on("closed", function()
	// {
	// 	console.log("NSQ reader closed");
	// });

});

/************************************************Manage Locations**********************************************/

app.get('/viewLocations', function(request, response)
{
	response.json(locationArray);
});

app.post("/editLocation", function(request, response)
{
	var location = request.body;

	console.log(location);
	var edit = false;

	for(var key in locationArray)
	{
		if(locationArray[key].id === location.id)
		{
			locationArray[key] = location;
			edit = true;
		}
	}

	if(edit == false)
	{
		response.send("Location edit failed!");
	}
	else
	{
		response.send("Location has been edited");
	}

});

app.post("/addLocation", function(request, response)
{
	var location = new Object();

	try
	{
		location.name = request.body.name;
		location.id = locationArray[locationArray.length - 1].id + 1;

		locationArray.push(location);

		response.send("The location has been added successfully");
	}
	catch(error)
	{
		response.send("The location could not be added");
	}
});

app.post("/deleteLocation", function(request, response)
{

	var deleted = false;

	var id = request.body.id;

	console.log(id);
	for(var key in locationArray)
	{
		if(locationArray[key].id === id)
		{
			locationArray.splice(key, 1);
			deleted = true;
		}
	}

	if(deleted === false)
	{
		response.send("The location could not be deleted");
	}
	else
	{
		response.send("The location was successfully deleted");
	}

});

/************************************************Users**********************************************/

app.post("/registerUser", function(request, response)
{
	//mock functionality 
	var user = request.body.user;
	var exists = false;

	for (var i = 0; i < userArray.length; i++) 
	{
		if(userArray[i].email === user.email)
		{
			exists = true;
			response.send("email");
		}	
	}

	if(!exists)
	{
		userArray.push(user);
		userArray[userArray.length - 1].id = userArray.length;
		console.log(userArray[userArray.length - 1]);
		response.send("registered");
	}

	//intended functionality

	// //send user object to user module via NSQ
	// var addUserRequest = new Object();
	// addUserRequest.src = "Access";
	// addUserRequest.dest = "Users";
	// addUserRequest.msgType = "request";
	// addUserRequest.queryType = "insert";
	// addUserRequest.content = new Object();
	// addUserRequest.content.fname = user.fname;
	// addUserRequest.content.sname = user.sname;
	// addUserRequest.content.email = user.email;
	// addUserRequest.content.password = user.password;
	// addUserRequest.content.stud_num = user.stud_num;
	// addUserRequest.content.phone = user.phone;

	// var addUserRequestJson = JSON.stringify(addUserRequest);

	// //send request
	// write.on("ready", function()
	// {
	// 	write.publish("users", addUserRequestJson);
	// });

	// //receive respose from user module NSQ
	// read = new nsq.Reader('user', 'navup', { lookupdHTTPAddresses : '127.0.0.1:4161', nsqdTCPAddresses : 'localhost:4150' });
	// read.connect();

	// read.on("message", function(msg)
	// {
	// 	console.log("Recieved user registration confirmation: " + msg.id + msg.body.toString());
	// 	msg.finish();
	// });

	// read.on("closed", function()
	// {
	// 	console.log("NSQ reader closed");
	// });

	// //send confirmation email
	// var notificationsRequest = new Object()
	// notificationsRequest.src = "Access";
	// notificationsRequest.dest = "Users";
	// notificationsRequest.msgType = "request";
	// notificationsRequest.queryType = "addUser";
	// notificationsRequest.content = new Object();
	// notificationsRequest.content.email = user.email;

	// //send email address to notfications module
	// var notificationsRequestJson = JSON.stringify(notificationsRequest);

	// write.on("ready", function()
	// {
	// 	write.publish("notifications", notificationsRequestJson);
	// });

	// //receive response from notifications module
	// read = new nsq.Reader('notifications', 'navup', { lookupdHTTPAddresses : '127.0.0.1:4161', nsqdTCPAddresses : 'localhost:4150' });
	// read.connect();

	// read.on("message", function(msg)
	// {
	// 	console.log("Recieved regitration notification confirmation: " + msg.id + msg.body.toString());
	// 	msg.finish();
	// });

	// read.on("closed", function()
	// {
	// 	console.log("NSQ reader closed");
	// });
});

app.post("/login", function(request, response)
{

	//mock functionality
	var stud_num = request.body.stud_num;
	var password = request.body.password;
	var loginObj = new Object();
	var exists = false;

	for (var i = 0; i < userArray.length; i++) 
	{
		if(userArray[i].stud_num == stud_num && userArray[i].password == password)
		{
			exists = true;
			var user = userArray[i].id + "-" + userArray[i].status;

			response.send(user);
		}
	}

	if(exists == false)
	{
		response.send("login failed");
	}

	//intended functionality

	// //send user object to user module via NSQ
	// var getUserRequest = new Object();
	// getUserRequest.src = "Access";
	// getUserRequest.dest = "Users";
	// getUserRequest.msgType = "request";
	// getUserRequest.queryType = "getUser";
	// getUserRequest.content = new Object();
	// getUserRequest.content.stud_num = stud_num;

	// var getUserRequestJson = JSON.stringify(getUserRequest);

	// //send request
	// write.on("ready", function()
	// {
	// 	write.publish("users", getUserRequestJson);
	// });

	// //receive respose from user module NSQ
	// read = new nsq.Reader('user', 'navup', { lookupdHTTPAddresses : '127.0.0.1:4161', nsqdTCPAddresses : 'localhost:4150' });
	// read.connect();

	// read.on("message", function(msg)
	// {
	// 	console.log("Recieved login confirmation: " + msg.id + msg.body.toString());
	// 	msg.finish();
	// });

	// read.on("closed", function()
	// {
	// 	console.log("NSQ reader closed");
	// });
});

app.post("/viewProfile", function(request, response)
{
	var id = request.body.id;
	console.log(id);
	var exists = false;

	for (var i = 0; i < userArray.length; i++) 
	{
		if(userArray[i].id == id)
		{
			exists = true;
			var user = new Object();
			user.name = userArray[i].fname;
			user.surname = userArray[i].sname;
			user.email = userArray[i].email;
			user.password = userArray[i].password;
			user.status = userArray[i].status;

			response.send(user);
		}
	}
	if(exists == false)
	{
		response.send("user not found");
	}
});

/************************************************GIS**********************************************/
app.post("navigateToLocation", function()
{
	var from = request.body.from;
	var to = request.body.to;

	// var getRouteRequest = new Object();
	// getRouteRequest.src = "Access";
	// getRouteRequest.dest = "Users";
	// getRouteRequest.msgType = "request";
	// getRouteRequest.queryType = "getRoute";
	// getRouteRequest.content = new Object();
	// getRouteRequest.content.from = from;
	// getRouteRequest.content.to = to;

	// var getRouteRequestJson = JSON.stringify(getRouteRequest);

	// //send request
	// write.on("ready", function()
	// {
	// 	write.publish("gis", getRouteRequestJson);
	// });

	// //receive respose from user module NSQ
	// read = new nsq.Reader('gis', 'navup', { lookupdHTTPAddresses : '127.0.0.1:4161', nsqdTCPAddresses : 'localhost:4150' });
	// read.connect();

	// read.on("message", function(msg)
	// {
	// 	console.log("Recieved route: " + msg.id + msg.body.toString());
	// 	msg.finish();
	// });

	// read.on("closed", function()
	// {
	// 	console.log("NSQ reader closed");
	// });
});

var port = process.argv[process.argv.length - 1];

app.listen(port, function () {
  console.log('NavUP listening on port ' + port);
});