const utils = require("./utils")
const entities = require("../entities")
class ProfileHandler {
	static async getHandler(req, res) 
    {
		let user = await entities.User.getUserById(res.uid, req.token);
        utils.renderPage(res, "profile.ejs", {user: user}, req.raw ? utils.FORMAT_RAW : utils.FORMAT_USER);
	}
    static async postHandler(req, res) {

    }
}
exports.Handler = ProfileHandler;
