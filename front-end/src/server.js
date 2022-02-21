const http = require("http");
const url = require("url");
const path = require("path");

const ws = require("./websocket");
const express = require("express");

const handlers = require("./handlers/handlers");
const managementHandlers = require("./handlers/managementHandler");

const server = express();
const port = 6969;

// const httpServer = http.createServer(handleRequest);
// const wsServer = ws.initWebSocket(httpServer);

const handlerMap = new Map();

exports.init = () => {
	handlerMap.set("/", handlers.IndexHandler);
	// handlerMap.set("/assets", handlers.AssetsHandler);
	handlerMap.set("/login", handlers.LoginHandler);
	handlerMap.set("/logout", handlers.LogoutHandler);
	handlerMap.set("/signup", handlers.RegisterHandler);
	handlerMap.set("/users", handlers.UserHandler);

	handlerMap.set("/artists", handlers.ArtistHandler);
	handlerMap.set("/artists/:id(\\d+)", handlers.ArtistHandler);

	handlerMap.set("/albums", handlers.AlbumHandler);
	handlerMap.set("/albums/:id(\\d+)", handlers.AlbumHandler);

	handlerMap.set("/tracks", handlers.TrackHandler);

	handlerMap.set("/library", handlers.LibraryHandler);
	handlerMap.set("/search", handlers.SearchHandler);

	// handlerMap.set("/management/:entity(users|albums)", handlers.ManagementHandler);
	handlerMap.set("/management/albums", managementHandlers.AlbumManHandler);
	handlerMap.set("/management/albums/:id(\\d+)", managementHandlers.AlbumManHandler);
	handlerMap.set("/management/albums/:id(\\d+)/:type(tracklist|comments)", managementHandlers.AlbumManHandler);
	// handlerMap.set("/management/albums/:id(\\d+)", handlers.ManagementHandler);
	handlerMap.set("/management/:entity(users|albums)/:id(\\d+)", handlers.ManagementHandler);

	handlerMap.set("/albums/:id(\\d+)/cover", handlers.APIForwarderHandler);
	handlerMap.set("/users/:id(\\d+)/avatar", handlers.APIForwarderHandler);
	handlerMap.set("/tracks/:id(\\d+)/audio", handlers.APIForwarderHandler);

	handlerMap.set("/profile", handlers.ProfileHandler)

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
};

exports.start = () => {
	server.listen("6969", "0.0.0.0", () => {
		console.log(`[Log][Server]Server is running.`);
	});
};
