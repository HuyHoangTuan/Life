const wsAddr = "ws://192.168.192.40:6969";
var socket;

function initWebSocket() {
  socket = new WebSocket(wsAddr);
  //
  socket.onopen = function (mesg) {
    wsOpen(mesg);
  };
  socket.onmessage = function (mesg) {
    wsGetMesg(mesg);
  };
  socket.onclose = function (mesg) {
    wsClose(mesg);
  };
  socket.onerror = function (mesg) {
    wsError(mesg);
  };
  //
}

function wsOpen() {
  console.log(`Connected to WebSocket at ${wsAddr}`);
}
function wsClose() {
  console.log(`Closed connection to ${wsAddr}`);
}
function wsError(mesg) {
  console.error(mesg);
}
function wsSendMesg(data) {
  console.log(`Sent message: "${JSON.stringify(data)}"`);
  socket.send(JSON.stringify(data));
}
function wsGetMesg(mesg) {}
