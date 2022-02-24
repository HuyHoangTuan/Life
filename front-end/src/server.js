const ws = require("./websocket");
const utils = require("./handlers/utils");
const express = require("express");
const router = express.Router();

const entities = require("./entities");
const handlers = require("./handlers/handlers");
const socketHandlers = require("./handlers/socketHandler");
const managementHandlers = require("./handlers/managementHandler");

const server = express();
const socket = require("express-ws")(server)
const port = 6969;

// const httpServer = http.createServer(handleRequest);
var wsServer;
const handlerMap = new Map();

exports.init = () => {
	server.use(async (req, res, next) => {
		req.raw = utils.formatIsRaw(req);
		req.token = utils.getToken(req);
		if (!req.token) {
			next();
			return;
		}
		let user = await entities.User.getUserById(utils.getUID(req.token), req.token);
		res.uid = user.id;
		req.uid = user.id;
		res.uname = user.name;
		next();
	});


	// wsServer = ws.initWebSocket(server);

	handlerMap.set("/", handlers.IndexHandler);
	// handlerMap.set("/assets", handlers.AssetsHandler);
	handlerMap.set("/login", handlers.LoginHandler);
	handlerMap.set("/logout", handlers.LogoutHandler);
	handlerMap.set("/signup", handlers.RegisterHandler);
	handlerMap.set("/users", handlers.UserHandler);
	handlerMap.set("/users/:id(\\d+)", handlers.UserHandler);

	handlerMap.set("/artists", handlers.ArtistHandler);
	handlerMap.set("/artists/:id(\\d+)", handlers.ArtistHandler);

	handlerMap.set("/albums", handlers.AlbumHandler);
	handlerMap.set("/albums/:id(\\d+)", handlers.AlbumHandler);

	handlerMap.set("/playlists", handlers.PlaylistHandler);
	handlerMap.set("/playlists/:id(\\d+)", handlers.PlaylistHandler);
	handlerMap.set("/playlists/liked_song", handlers.FavSongHandler);

	handlerMap.set("/tracks", handlers.APIForwarderHandler);
	handlerMap.set("/tracks/:id(\\d+)", handlers.APIForwarderHandler);

	handlerMap.set("/library", handlers.LibraryHandler);
	handlerMap.set("/search", handlers.SearchHandler);

	handlerMap.set("/management", handlers.ManagementHandler);
	handlerMap.set("/management/albums", managementHandlers.AlbumManHandler);
	handlerMap.set("/management/albums/:id(\\d+)", managementHandlers.AlbumManHandler);
	handlerMap.set("/management/albums/:id(\\d+)/:type(tracks|comments)", managementHandlers.AlbumManHandler);

	handlerMap.set("/management/playlists", managementHandlers.PlaylistManHandler);
	handlerMap.set("/management/playlists/:id(\\d+)", managementHandlers.PlaylistManHandler);
	handlerMap.set("/management/playlists/:id(\\d+)/:type(tracks|comments)", managementHandlers.PlaylistManHandler);

	handlerMap.set("/management/users", managementHandlers.UserManHandler);
	handlerMap.set("/management/users/:id(\\d+)", managementHandlers.UserManHandler);
	handlerMap.set("/management/users/:id(\\d+)/:type(playlists|comments)", managementHandlers.UserManHandler);

	handlerMap.set("/management/:entity(users|albums)/:id(\\d+)", handlers.ManagementHandler);

	handlerMap.set("/albums/:id(\\d+)/cover", handlers.APIForwarderHandler);
	handlerMap.set("/users/:id(\\d+)/avatar", handlers.APIForwarderHandler);
	handlerMap.set("/users/:id(\\d+)/playlists", handlers.APIForwarderHandler);
	handlerMap.set("/users/:id(\\d+)/playlists/:pid(\\d+)/tracks/:tid(\\d+)", handlers.APIForwarderHandler);
	handlerMap.set("/tracks/:id(\\d+)/audio", handlers.APIForwarderHandler);

	handlerMap.set("/profile", handlers.ProfileHandler);

	handlerMap.set("/comment/:entity(albums|playlists)/:id", socketHandlers.CommentHandler);

	// handlerMap.set("/users/:id(\\d+)/albums/favorite", handlers.APIForwarderHandler);
	handlerMap.set("/users/:id(\\d+)/:entity(albums|artists|track)/favorite", handlers.APIForwarderHandler);

	server.set("view engine", "ejs");
	server.set("views", "src/wwwroot");
	server.use("/assets", express.static("src/wwwroot/assets"));

	for (const [key, value] of handlerMap.entries()) {
		let route = server.route(key);
		if (value.allHandler != undefined) {
			console.log("[Log][Init]Forwarding " + key);
			route.all(value.allHandler);
			continue;
		}
		if (value.getHandler != undefined) {
			console.log("[Log][Init]Mapped [GET] " + key);
			route.get(value.getHandler);
		}
		if (value.postHandler != undefined) {
			console.log("[Log][Init]Mapped [POST] " + key);
			route.post(value.postHandler);
		}
		if (value.putHandler != undefined) {
			console.log("[Log][Init]Mapped [PUT] " + key);
			route.put(value.putHandler);
		}
		if (value.deleteHandler != undefined) {
			console.log("[Log][Init]Mapped [DELETE] " + key);
			route.delete(value.deleteHandler);
		}
	}

	router.ws("/", socketHandlers.handler);

	server.use("/ws", router)
};

exports.start = () => {
	server.listen("6969", "0.0.0.0", () => {
		console.log(`[Log][Server]Server is running.`);
	});
};
