var http = require("http");
var fs = require('fs');

http.createServer(function (request, response) {

   // Send the HTTP header 
   // HTTP Status: 200 : OK
   // Content Type: text/plain
   response.writeHead(200, {'Content-Type': 'text/html'});
   
   // Send the response body as "Hello World"
   fs.readFile('./index.html', null, function(error, data) {
   		if (error)
   			{
   				response.writeHead(404);
   				response.write('lol');
   			} else
   			{
   				response.write(data);
   			}
   			response.end();
   });

}).listen(8081);

// Console will print the message
console.log('Server running at http://127.0.0.1:8081/');