const utils = require("./utils");
const entities = require("../entities");
const { APIForwarderHandler } = require("./handlers");

exports.UserHandler = class {
	static async getHandler(req, res, man = 0) {
		let mode = utils.formatIsRaw(req, 1);
		if (man == 1) {
			let id = req.params.id;
			if (id) {
				let user = await entities.User.getUserById(id, utils.getToken(req));

				utils.renderPage(
					res,
					"man_user_detail.ejs",
					{ user: user },
					mode ? 3 : 2
				);
			} else {
				let userList = await entities.User.getUserList(utils.getToken(req));
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
					token: utils.getToken(req),
				});
			}
		}
	}
	static async postHandler(req, res) {
		// let id = utils.params.id;
		// if (!id) {
		// 	let newUser = JSON.parse(await getBody(req));
		// 	let result = JSON.parse(
		// 		await api.doPost(
		// 			"/users",
		// 			{ token: utils.getToken(req) },
		// 			JSON.stringify(newUser)
		// 		)
		// 	);
		// 	console.log(result);
		// 	if (result.status == "success") {
		// 		console.log("[Log][POST_user][status]done");
		// 		res.statusCode = 200;
		// 		res.write("sucess");
		// 		res.end();
		// 	} else {
		// 	}
		// }
		APIForwarderHandler.allHandler(req, res);
	}
	static async putHandler(req, res) {
		// let body = await getBody(req);

		// console.log("[BODY]" + body);

		// let id = utils.getPath(req.url, 2);
		// console.log("[Log][PUT_User][BODY]" + body);
		// let result = JSON.parse(
		// 	await api.doPut(`/users/${id}`, { token: utils.getToken(req) }, body)
		// );
		// console.log("[Log][PUT_User][RES]" + result);
		// if (result.status == "success") {
		// 	console.log("[Log][PUT_User][status]done");
		// 	res.statusCode = 200;
		// 	res.write("sucess");
		// 	res.end();
		// }
		APIForwarderHandler.allHandler(req, res);
	}
	static async deleteHandler(req, res) {
		APIForwarderHandler.allHandler(req, res);
	}
};
exports.ArtistHandler = class {
	static async getHandler(req, res) {
		let id = req.params.id;
		let artist = await entities.Artist.getArtistById(id, req.token);
		await artist.getAlbumList(req.token);
		let isFav = await artist.checkFavArtist(res.uid, req.token);
		utils.renderPage(res, "artist.ejs", { artist: artist, isFav: isFav }, req.raw ? utils.FORMAT_RAW : utils.FORMAT_USER);
	}
	static async postHandler(req, res) {}
	static async putHandler(req, res) {}
	static async deleteHandler(req, res) {}
};
