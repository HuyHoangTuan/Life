const ejs = require("ejs");
const utils = require("./utils");
var frame = require("./frame");

/*
    /api/account
    URLs
    /management/user
    /management/album
    /management/song
    /management/playlist
    /management/content
*/

class MgrHandler {
  static getHandler(req, res) {
    var type = utils.parseMgr(req.url);
    var data = {};
    utils.renderPage(res, `management/${type}.html`,data, frame.compiledMgr);
  }
  static postHandler(req, res) {}
  static putHandler(req, res) {}
  static deleteHandler(req, res) {}
}

exports.Handler = MgrHandler;
