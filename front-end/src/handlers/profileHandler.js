const utils = require("./utils")
const entities = require("../entities")
class ProfileHandler {
	static async getHandler(req, res) {
		let user = await entities.User.getUserById(4, utils.getToken())
        utils.renderPage(res, "profile.ejs", {user: user}, 1)
	}
    static async postHandler(req, res) {

    }
}
exports.Handler = ProfileHandler;
