const utils = require("./utils");
var frame = require("./frame");

class ArtistHandler {
  static getHandler(req, res) {
    var data = {};
    utils.renderPage(res, "artist.html", data, frame.compiled);
  }
  static postHandler(req, res) {}
  static putHandler(req, res) {}
  static deleteHandler(req, res) {}
}

exports.Handler = ArtistHandler;
