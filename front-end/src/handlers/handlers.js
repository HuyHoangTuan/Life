const utils = require("./utils");
const cookie = require("cookie");
const qs = require("querystring");
const entities = require("../entities");
const api = require("../api");

const AlbumHandler = require("./albumHandler")
const UserHandler = require("./userHandler")
const ProfileHandler = require("./profileHandler")
const PlaylistHandler = require("./playlistHandler")
const ManagementHandler = require("./managementHandler")
var jwt = require("jsonwebtoken");


class APIForwarderHandler {
	static allHandler(req, res) {
		api.forward(req, res, req.path, { token: utils.getToken(req) });
	}
}

class TrackHandler {
	static async getHandler(req, res, man = 0) {
		let mode = getMode(req);
		if (man == 1) {
			let trackList = await entities.Track.getTrackList(getToken());
			// let artistList = await entities.Artist.getArtistList(getToken());
			utils.renderPage(
				res,
				"man_track.ejs",
				{ trackList: trackList },
				mode ? 3 : 2
			);
		} else {
			let id = utils.getPath(req.url, 2);
			if (utils.getPath(req.url, 3) == "audio") {
				api.forward(req, res, `/tracks/${id}/audio`, {
					token: getToken(),
				});
			} else {
			}
		}
	}
	static async postHandler(req, res) {}
	static async putHandler(req, res) {}
	static async deleteHandler(req, res) {}
}

class IndexHandler {
	static async getHandler(req, res) {
		utils.renderPage(res, "index.html", null);
	}
}

class LibraryHandler {
	static async getHandler(req, res) {
		let favAlbumList = await entities.User.getFavAlbums(res.uid, req.token);
		let favArtistList = await entities.User.getFavArtists(res.uid, req.token);
		let playlist = await entities.User.getPlaylists(res.uid, req.token);
		var data = {favAlbumList: favAlbumList, favArtistList: favArtistList, playlist: playlist};
		utils.renderPage(res, "library.ejs", data, req.raw ? utils.FORMAT_RAW : utils.FORMAT_USER);
	}
}

exports.LoginHandler = class {
	static async getHandler(req, res, status = 0) {
		var data = { status: status };
		// utils.renderPage(res, "login.ejs", data);
		res.render("login", data);
	}
	static async postHandler(req, res) {
		var post = qs.parse(await utils.getBody(req));

		let result = JSON.parse(await api.doPost("/authenticate", null, JSON.stringify({ email: post.email, password: post.password })));

		console.log(`[Log][Login]Login attempt with email=${post.username}, password=${post.password}`);

		if (result.status != undefined) {
			LoginHandler.getHandler(req, res, 1);
		} else {
			res.setHeader("Set-Cookie", cookie.serialize("token", result.token, { httpOnly: true, maxAge: 60 * 60 * 24 * 30 }));
			let decoded = jwt.verify(result.token, "L0Zhbmt5Y2hvcDEyMz9sb2dpbj1GYW5reWNob3AmcGFzc3dvcmQ9S3ViaW4xMjM/");
			console.log(`[DECODED] ${decoded.sub}`)
			if (result.role == 0) {
				res.redirect(301, "/management/users");
			} else if (result.role == 2) {
				res.redirect(301, "/library");
			}
		}
	}
};

exports.LogoutHandler = class {
	static async getHandler(req, res) {
		res.setHeader(
			"Set-Cookie",
			cookie.serialize("token", "", {
				httpOnly: true,
				maxAge: 60 * 60 * 24 * 30,
			})
		);
		res.redirect(301, "/login");
	}
};

exports.RegisterHandler = class {
	static async getHandler(req, res, status = 0) {
		var data = { status: status };
		utils.renderPage(res, "register.ejs", data);
	}
	static async postHandler(req, res) {
		var post = qs.parse(await getBody(req));
		console.log(post);
		let result = JSON.parse(
			await api.doPost(
				"/register",
				null,
				JSON.stringify({
					email: post.email,
					display_name: post.name,
					password: post.password,
					role: 3,
				})
			)
		);
		if (result.status == "success") {
			res.redirect(301, "/login");
		} else {
			this.getHandler(req, res, 1);
		}
		//login(post.username, post.password);
	}
};
/*
/search/tracks?content={keyword}&index
*/
exports.SearchHandler = class {
	static async getHandler(req, res) {
		let mode = getMode(req);
		let keyword = utils.getURLQuery(req.url, "keyword");
		switch (req.params["entity"]) {
			case "albums": {
				let albumList = entities.Album.searchAlbum(keyword, getToken());
				utils.renderPage(
					res,
					"search.ejs",
					{ result: { albums: albumList } },
					mode ? 3 : 1
				);
				break;
			}
			case "artists": {
				let artistList = entities.Artist.searchArtist(
					keyword,
					getToken()
				);
				utils.renderPage(
					res,
					"search.ejs",
					{ result: { artists: artistList } },
					mode ? 3 : 1
				);
				break;
			}
			case "tracks": {
				let trackList = entities.Track.searchTrack(keyword, getToken());
				utils.renderPage(
					res,
					"search.ejs",
					{ result: { tracks: trackList } },
					mode ? 3 : 1
				);
				break;
			}
			case undefined:
				let albumList = await entities.Album.searchAlbum(
					keyword,
					getToken()
				);
				let artistList = await entities.Artist.searchArtist(
					keyword,
					getToken()
				);
				let trackList = await entities.Track.searchTrack(
					keyword,
					getToken()
				);
				utils.renderPage(
					res,
					"search.ejs",
					{
						result: {
							tracks: trackList,
							artists: artistList,
							albums: albumList,
						},
					},
					mode ? 3 : 1
				);
				break;
			default:
				console.log(utils.getPath(req.url, 2));
		}
	}
};

exports.AlbumHandler = AlbumHandler.Handler;

exports.UserHandler = UserHandler.UserHandler;
exports.ArtistHandler = UserHandler.ArtistHandler;
exports.ProfileHandler = ProfileHandler.Handler;

exports.TrackHandler = TrackHandler;

exports.IndexHandler = IndexHandler;
exports.LibraryHandler = LibraryHandler;
exports.ManagementHandler = ManagementHandler.Handler;

exports.APIForwarderHandler = APIForwarderHandler;
