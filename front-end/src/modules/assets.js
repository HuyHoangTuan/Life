var url = require("url");
var fs = require("fs");
var path = require("path");

class AssetsHandler {
  static getHandler(req, res) {
    var filePath = url.parse(req.url).pathname;
    var file = fs.readFileSync("./wwwroot" + filePath, "UTF-8");
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
  static postHandler(req, res) {}
}

exports.Handler = AssetsHandler;
