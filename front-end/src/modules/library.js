const utils = require("./utils");
var frame = require("./frame");

class LibraryHandler {
  static getHandler(req, res) {
    var data = {};
    utils.renderPage(res, "library.html", data, frame.compiled);
  }
  static postHandler(req, res) {}
  static putHandler(req, res) {}
  static deleteHandler(req, res) {}
}

exports.Handler = LibraryHandler;
