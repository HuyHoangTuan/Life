const http = require("http");
const url = require("url");
const path = require("path");

const hostname = "0.0.0.0";
const port = "6969";

const ws = require("./modules/websocket");

const utils = require("./modules/utils");

const assets = require("./modules/assets");
const login = require("./modules/login");
const index = require("./modules/index");
const album = require("./modules/album");
const artist = require("./modules/artist");
const library = require("./modules/library");
const manager = require("./modules/manager");

const handlerMap = new Map();

const server = http.createServer(handleRequest);

server.listen(port, hostname, () => {
  console.log(`[Log][Server]Server is running at ${hostname}:${port}`);
});

const wsServer = ws.initWebSocket(server);

function init() {
  handlerMap.set("", index.Handler);
  handlerMap.set("assets", assets.Handler);
  handlerMap.set("login", login.Handler);
  handlerMap.set("album", album.Handler);
  handlerMap.set("artist", artist.Handler);
  handlerMap.set("library", library.Handler);
  // handlerMap.set('/playlist', playlistHandler);

  handlerMap.set("management", manager.Handler);
}

init();
//URL : /[handler]/[ID]?[param]=[value]&[param]=[value]

function handleRequest(req, res) {
  const handler = utils.parseHandler(req.url);
  if (handlerMap.has(handler)) {
    //console.log("Handling " + handler);
    var handlerObj = handlerMap.get(handler);

    switch (req.method) {
      case "GET":
        handlerObj.getHandler(req, res);
        break;
      case "POST":
        var body = "";
        req.on("data", (data) => {
          body += data;
          // Too much POST data, kill the connection!
          if (body.length > 1e6) request.connection.destroy();
        });
        req.on("end", () => {
          handlerObj.postHandler(req, res, body);
        });

        break;
      case "PUT":
        handlerObj.putHandler(req, res);
        break;
      case "DELETE":
        handlerObj.deleteHandler(req, res);
      default:
        break;
    }
  }
}
