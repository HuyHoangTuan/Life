var qs = require("querystring");
var utils = require("./utils");
var api = require("../api");
var jwt = require("jsonwebtoken");
const cookie = require("cookie");

class LoginHandler {
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
			console.log(`[DECODED] ${decoded}`)
			if (result.role == 0) {
				res.redirect(301, "/management/users");
			} else if (result.role == 2) {
				res.redirect(301, "/library");
			}
		}
	}
}

exports.Handler = LoginHandler;
