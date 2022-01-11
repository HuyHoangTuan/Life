const utils = require("./utils");

class IndexHandler {
  static getHandler(req, res) {
    utils.renderPage(res, "index.html", null);
  }
  static postHandler(req, res) {}
  static putHandler(req, res) {}
  static deleteHandler(req, res) {}
}

exports.Handler = IndexHandler;
