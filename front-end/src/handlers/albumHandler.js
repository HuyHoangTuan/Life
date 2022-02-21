const utils = require("./utils");
const entities = require("../entities");
const { APIForwarderHandler } = require("./handlers");

exports.Handler = class {
	static async getHandler(req, res) {
		let raw = utils.formatIsRaw(req);
		let id = req.params.id;
		let album = await entities.Album.getAlbumById(id, utils.getToken(req));
		await album.getTrackList(utils.getToken(req));
		utils.renderPage(res, "album.ejs", { album: album }, raw ? utils.FORMAT_RAW : utils.FORMAT_USER);
	}
	static async postHandler(req, res) {
		let id = req.params.id;
		if (id == undefined) {
			let body = JSON.parse(await getBody(req));
			let result = JSON.parse(await api.doPost("/albums", { token: utils.getToken(req) }, JSON.stringify(body)));
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
		// let body = await utils.getBody(req);

		// console.log("[BODY]" + body);

		// let id = req.param.id;
		// let result = JSON.parse(await api.doPut(`/albums/${id}`, { token: utils.getToken(req) }, body));
		// if (result.status == "success") {
		// 	console.log("[Log][PUT_Album][status]done");
		// 	res.statusCode = 200;
		// 	res.write("success");
		// 	res.end();
		// }
		APIForwarderHandler.allHandler(req, res)
	}
	static async deleteHandler(req, res) {}
};
