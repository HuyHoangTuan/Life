var ws = require('ws');

var wsServer;

exports.initWebSocket = (httpServer) => {
    wsServer = new ws.WebSocketServer({ noServer: true });
    wsServer.on('connection', onConnection);

    httpServer.on("upgrade", (req, socket, head) => {
        // This function is not defined on purpose. Implement it with your own logic.
        // authenticate(request, function next(err, client) {
        //   if (err || !client) {
        //     socket.write("HTTP/1.1 401 Unauthorized\r\n\r\n");
        //     socket.destroy();
        //     return;
        //   }
          wsServer.handleUpgrade(req, socket, head, function done(ss) {
            console.log(req.headers.cookie);
            wsServer.emit("connection", ss, req, "client");
          });
        });
      // });

    return wsServer;
};


function onConnection(socket, req, client){
    console.log(socket + "::" + req + "::" + client);
    socket.on('message', (data)=>{
        console.log(`${client} sent a message: ${data}`);
    });
}
