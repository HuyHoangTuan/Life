const utils = require("./utils");
const url = require("url");
const cookie = require("cookie");
const fs = require("fs");
const qs = require("querystring");
const path = require("path");
const entities = require("./entities");
const api = require("./api");

const testToken =
	"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNjQyMzYwODU3fQ.MEfen90dvOGM4zuzNm-BLJTdQgC-ZCo_1dMqcTzboYw";

function getMode(req) {
	return utils.getURLQuery(req.url, "raw") == "true" ? true : false;
}

function getCookie(key) {
	var cookies = cookie.parse(req.headers.cookie || "");
	return cookies[key];
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

function writeError() {}

class UserHandler {
	static async getHandler(req, res, man = 0) {
		let mode = getMode(req, 1);
		if (man == 1) {
			let id = utils.getPath(req.url, 3);
			if (id) {
				let user = await entities.User.getUserById(id, testToken);

				utils.renderPage(
					res,
					"man_user_detail.ejs",
					{ user: user },
					mode ? 3 : 2
				);
			} else {
				let userList = await entities.User.getUserList(testToken);
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
					token: testToken,
				});
			}
		}
	}
	static async postHandler(req, res) {
		let id = utils.getPath(req.url, 2);
		//upload avatar
		if (id && utils.getPath(req.url, 3) == "avatar") {
			api.forward(req, res, `/users/${id}/avatar`, { token: testToken });
		} else if (!id) {
			let newUser = JSON.parse(await getBody(req));
			let result = JSON.parse(
				await api.doPost(
					"/users",
					{ token: testToken },
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
			await api.doPut(`/users/${id}`, { token: testToken }, body)
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
		let id = utils.getPath(req.url, 2);
		let artist = await entities.Artist.getArtistById(id, testToken);
		await artist.getAlbumList(testToken);

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
				let album = await entities.Album.getAlbumById(id, testToken);
				let artistList = await entities.Artist.getArtistList(testToken);
				await album.getTrackList(testToken);

				utils.renderPage(
					res,
					"man_album_detail.ejs",
					{ album: album, artistList: artistList },
					mode ? 3 : 2
				);
			} else {
				let albumList = await entities.Album.getAlbumList(testToken);
				let artistList = await entities.Artist.getArtistList(testToken);
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
					token: testToken,
				});
			} else if (utils.getPath(req.url, 3) == undefined) {
				let album = await entities.Album.getAlbumById(id, testToken);
				await album.getTrackList(testToken);

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
			api.forward(req, res, `/albums/${id}/cover`, { token: testToken });
		} else if (id == undefined) {
			let body = JSON.parse(await getBody(req));
			let result = JSON.parse(
				await api.doPost(
					"/albums",
					{ token: testToken },
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
			await api.doPut(`/albums/${id}`, { token: testToken }, body)
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
			let trackList = await entities.Track.getTrackList(testToken);
			// let artistList = await entities.Artist.getArtistList(testToken);
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
					token: testToken,
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
	static async postHandler(req, res) {}
	static async putHandler(req, res) {}
	static async deleteHandler(req, res) {}
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
		utils.renderPage(res, "login.ejs", data);
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
			this.getHandler(req, res, 1)
		} else {
			res.setHeader(
				"Set-Cookie",
				cookie.serialize("token", result.token, {
					httpOnly: true,
					maxAge: 60 * 60 * 24 * 30,
				})
			);
			if (result.role == 0) {
				utils.redirect(res, "/management/users");
			} else if (result.role == 2) {
				utils.redirect(res, "/library");
			}
		}
	}
	static async putHandler(req, res) {}
	static async deleteHandler(req, res) {}
};

exports.LogoutHandler = class {
	static async getHandler(req, res) {
		console.log("???????")
		res.setHeader(
			"Set-Cookie",
			cookie.serialize("token", "", {
				httpOnly: true,
				maxAge: 60 * 60 * 24 * 30,
			})
		);
		utils.redirect(res, "/login");
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
		let result = JSON.parse (await api.doPost(
			"/register",
			null,
			JSON.stringify({
				email: post.email,
				"display_name": post.name,
				password: post.password,
				role: 3
			})
		));
		if(result.status == "success"){
			utils.redirect(res, "/login");
		}else{
			this.getHandler(req, res, 1)
		}
		//login(post.username, post.password);
	}
	static async putHandler(req, res) {}
	static async deleteHandler(req, res) {}
};

class AssetsHandler {
	static async getHandler(req, res) {
		var filePath = url.parse(req.url).pathname;
		var file = fs.readFileSync("src/wwwroot" + filePath, "UTF-8");
		res.statusCode = 200;
		switch (path.extname(filePath)) {
			case ".js":
				res.setHeader("Content-Type", "	application/javascript");
				break;
			case ".css":
				res.setHeader("Content-Type", "	text/css");
				break;
			default:
				res.setHeader("Content-Type", "text");
				break;
		}
		res.write(file);
		res.end();
	}
	static async postHandler(req, res) {}
}

class ManagementHandler {
	static async getHandler(req, res) {
		switch (utils.getPath(req.url, 2)) {
			case "albums":
				AlbumHandler.getHandler(req, res, 1);
				break;
			case "users":
				UserHandler.getHandler(req, res, 1);
				break;
			/*case "tracks":
				TrackHandler.getHandler(req, res, 1);*/
			case undefined:
				utils.redirect(res, "/management/users");
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
		switch (utils.getPath(req.url, 2)) {
			case "albums": {
				let albumList = entities.Album.searchAlbum(keyword, testToken);
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
					testToken
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
				let trackList = entities.Track.searchTrack(keyword, testToken);
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
					testToken
				);
				let artistList = await entities.Artist.searchArtist(
					keyword,
					testToken
				);
				let trackList = await entities.Track.searchTrack(
					keyword,
					testToken
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
exports.AssetsHandler = AssetsHandler;
exports.IndexHandler = IndexHandler;
exports.LibraryHandler = LibraryHandler;
exports.ManagementHandler = ManagementHandler;
exports.UserHandler = UserHandler;
