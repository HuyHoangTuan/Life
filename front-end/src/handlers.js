const utils = require("./utils");
const cookie = require("cookie");
const qs = require("querystring");
const entities = require("./entities");
const api = require("./api");

function getMode(req) {
	// return utils.getURLQuery(req.url, "raw") == "true" ? true : false;
	return req.query.raw != undefined;
}

function getCookie(key) {
	var cookies = cookie.parse(req.headers.cookie || "");
	return cookies[key];
}

function getToken() {
	return "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNjQyMzYwODU3fQ.MEfen90dvOGM4zuzNm-BLJTdQgC-ZCo_1dMqcTzboYw";
}

async function getBody(req) {
	return new Promise((resolve) => {
		let body = "";
		req.on("data", (data) => {
			body += data;
			// Too much data, kill the connection!
			if (body.length > 1e6) request.connection.destroy();
		});
		req.on("end", (data) => {
			resolve(body);
		});
	});
}

class APIForwarderHandler {
	static allHandler(req, res) {
		api.forward(req, res, req.path, { token: getToken() });
	}
}

class UserHandler {
	static async getHandler(req, res, man = 0) {
		let mode = getMode(req, 1);
		if (man == 1) {
			let id = utils.getPath(req.url, 3);
			if (id) {
				let user = await entities.User.getUserById(id, getToken());

				utils.renderPage(
					res,
					"man_user_detail.ejs",
					{ user: user },
					mode ? 3 : 2
				);
			} else {
				let userList = await entities.User.getUserList(getToken());
				utils.renderPage(
					res,
					"man_user.ejs",
					{ userList: userList },
					mode ? 3 : 2
				);
			}
		} else {
			let id = utils.getPath(req.url, 2);
			if (utils.getPath(req.url, 3) == "avatar") {
				api.forward(req, res, `/users/${id}/avatar`, {
					token: getToken(),
				});
			}
		}
	}
	static async postHandler(req, res) {
		let id = utils.getPath(req.url, 2);
		//upload avatar
		if (id && utils.getPath(req.url, 3) == "avatar") {
			api.forward(req, res, `/users/${id}/avatar`, { token: getToken() });
		} else if (!id) {
			let newUser = JSON.parse(await getBody(req));
			let result = JSON.parse(
				await api.doPost(
					"/users",
					{ token: getToken() },
					JSON.stringify(newUser)
				)
			);
			console.log(result);
			if (result.status == "success") {
				console.log("[Log][POST_user][status]done");
				res.statusCode = 200;
				res.write("sucess");
				res.end();
			} else {
			}
		}
	}
	static async putHandler(req, res) {
		let body = await getBody(req);

		console.log("[BODY]" + body);

		let id = utils.getPath(req.url, 2);
		console.log("[Log][PUT_User][BODY]" + body);
		let result = JSON.parse(
			await api.doPut(`/users/${id}`, { token: getToken() }, body)
		);
		console.log("[Log][PUT_User][RES]" + result);
		if (result.status == "success") {
			console.log("[Log][PUT_User][status]done");
			res.statusCode = 200;
			res.write("sucess");
			res.end();
		}
	}
	static async deleteHandler(req, res) {}
}

class ArtistHandler {
	static async getHandler(req, res) {
		let mode = getMode(req, 1);
		let id = req.params["id"];
		let artist = await entities.Artist.getArtistById(id, getToken());
		await artist.getAlbumList(getToken());

		utils.renderPage(res, "artist.ejs", { artist: artist }, mode ? 3 : 1);
	}
	static async postHandler(req, res) {}
	static async putHandler(req, res) {}
	static async deleteHandler(req, res) {}
}

class AlbumHandler {
	static async getHandler(req, res, man = 0) {
		let mode = getMode(req);
		if (man == 1) {
			let id = utils.getPath(req.url, 3);
			if (id) {
				let album = await entities.Album.getAlbumById(id, getToken());
				let artistList = await entities.Artist.getArtistList(
					getToken()
				);
				await album.getTrackList(getToken());

				utils.renderPage(
					res,
					"man_album_detail.ejs",
					{ album: album, artistList: artistList },
					mode ? 3 : 2
				);
			} else {
				let albumList = await entities.Album.getAlbumList(getToken());
				let artistList = await entities.Artist.getArtistList(
					getToken()
				);
				utils.renderPage(
					res,
					"man_album.ejs",
					{ albumList: albumList, artistList: artistList },
					mode ? 3 : 2
				);
			}
		} else {
			let id = utils.getPath(req.url, 2);
			if (utils.getPath(req.url, 3) == "cover") {
				api.forward(req, res, `/albums/${id}/cover`, {
					token: getToken(),
				});
			} else if (utils.getPath(req.url, 3) == undefined) {
				let album = await entities.Album.getAlbumById(id, getToken());
				await album.getTrackList(getToken());

				utils.renderPage(
					res,
					"album.ejs",
					{ album: album },
					mode ? 3 : 1
				);
			}
		}
	}
	static async postHandler(req, res) {
		let id = utils.getPath(req.url, 2);
		//upload cover
		if (id && utils.getPath(req.url, 3) == "cover") {
			api.forward(req, res, `/albums/${id}/cover`, { token: getToken() });
		} else if (id == undefined) {
			let body = JSON.parse(await getBody(req));
			let result = JSON.parse(
				await api.doPost(
					"/albums",
					{ token: getToken() },
					JSON.stringify(body)
				)
			);
			console.log(result);
			if (result.status == "success") {
				console.log("[Log][POST_albums][status]done");
				res.statusCode = 200;
				res.write("sucess");
				res.end();
			}
		}
	}
	static async putHandler(req, res) {
		let body = await getBody(req);

		console.log("[BODY]" + body);

		let id = utils.getPath(req.url, 2);
		console.log("[Log][PUT_Album][BODY]" + body);
		let result = JSON.parse(
			await api.doPut(`/albums/${id}`, { token: getToken() }, body)
		);
		console.log("[Log][PUT_Album][RES]" + result);
		if (result.status == "success") {
			console.log("[Log][PUT_Album][status]done");
			res.statusCode = 200;
			res.write("sucess");
			res.end();
		}
	}
	static async deleteHandler(req, res) {}
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
		var data = {};
		utils.renderPage(res, "library.ejs", data, 1);
	}
	static async postHandler(req, res) {}
	static async putHandler(req, res) {}
	static async deleteHandler(req, res) {}
}

exports.LoginHandler = class {
	static async getHandler(req, res, status = 0) {
		var data = { status: status };
		// utils.renderPage(res, "login.ejs", data);
		res.render("login", data);
	}
	static async postHandler(req, res) {
		var post = qs.parse(await getBody(req));

		let result = JSON.parse(
			await api.doPost(
				"/authenticate",
				null,
				JSON.stringify({
					email: post.email,
					password: post.password,
				})
			)
		);
		console.log(
			`[Log][Login]Login attempt with email=${post.username}, password=${post.password}`
		);
		if (result.status != undefined) {
			this.getHandler(req, res, 1);
		} else {
			res.setHeader(
				"Set-Cookie",
				cookie.serialize("token", result.token, {
					httpOnly: true,
					maxAge: 60 * 60 * 24 * 30,
				})
			);
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

class ManagementHandler {
	static async getHandler(req, res) {
		// if (!res.params) {
		// 	res.redirect(301, "/management/users");
		// 	return;
		// }
		console.log(req.params)
		switch (req.params["entity"]) {
			case "albums":
				AlbumHandler.getHandler(req, res, 1);
				break;
			case "users":
				UserHandler.getHandler(req, res, 1);
				break;
			/*case "tracks":
				TrackHandler.getHandler(req, res, 1);*/
			case undefined:
				break;
			default:
				console.log(utils.getPath(req.url, 2));
		}
	}
}
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

exports.AlbumHandler = AlbumHandler;
exports.ArtistHandler = ArtistHandler;
exports.TrackHandler = TrackHandler;
exports.IndexHandler = IndexHandler;
exports.LibraryHandler = LibraryHandler;
exports.ManagementHandler = ManagementHandler;
exports.UserHandler = UserHandler;
exports.APIForwarderHandler = APIForwarderHandler;
