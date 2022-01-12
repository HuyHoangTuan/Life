const http = require("http");
const url = require("url");
const path = require("path");

const ws = require("./websocket");
const utils = require("./utils");
const handlers = require("./handlers");
const mux = require("./mux")

const httpServer = http.createServer(handleRequest);
const wsServer = ws.initWebSocket(httpServer);

const handlerMap = new Map();

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

exports.init = () => {
	handlerMap.set("", handlers.IndexHandler);
	handlerMap.set("assets", handlers.AssetsHandler);
	handlerMap.set("login", handlers.LoginHandler);
	handlerMap.set("albums", handlers.AlbumHandler);
	handlerMap.set("artists", handlers.ArtistHandler);
	handlerMap.set("library", handlers.LibraryHandler);
	// handlerMap.set('/playlist', playlistHandler);

	// handlerMap.set("management", manager.Handler);

    router = new mux.Router()
    // router.map("/some", a)
};
exports.start = () => {
	httpServer.listen("6969", "0.0.0.0", () => {
		console.log(`[Log][Server]Server is running.`);
	});
};
