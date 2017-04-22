var nsq = require('nsqjs');

//nsq writer
var write = new nsq.Writer('127.0.0.1', 4150);
write.connect();

write.on("closed", function()
{
	console.log("NSQ writer closed");
});

//nsq reader
var readUser = new nsq.Reader('access', 'navup', { lookupdHTTPAddresses : '127.0.0.1:4161', nsqdTCPAddresses : 'localhost:4150' });
readUser.connect();

readUser.on("message", function()
{
	console.log("Recieved : " + msg.id + msg.body.toString());

	var messageObject = msg.body;
	var content = messageObject.content;
	var response = new Object();
	var responseJson;

	if(messageObject.queryType === "addUser") {
		response.success = true;
		responseJson = JSON.stringify(response);

		write.on("ready", function()
		{
			write.publish("access", responseJson);
		});
	}
	else if(messageObject.queryType === "getUser") {
		response.fname = "Munya";
		response.sname = "Mpofu";
		response.email = "munya@gmail.com";
		response.stud_num = "stud_num";
		response.password = "1234";
		response.phone = "0123456789";

		responseJson = JSON.stringify(response);

		write.on("ready", function()
		{
			write.publish("access", responseJson);
		});
	}
	else if(messageObject.queryType === "getRoute")
	{
		response.from = new Object();
		response.from.latitude = -25.7552303;
		response.from.longitude = 28.2324397;

		response.to = new Object();
		response.to.latitude = -25.755846;
		response.to.longitude = 28.233151;

		responseJson = JSON.stringify(response);

		write.on("ready", function()
		{
			write.publish("access", responseJson);
		});
	}
});



