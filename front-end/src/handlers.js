const utils = require("./utils")
const url = require("url")
const fs = require("fs")
const path = require("path")
const entities = require("./entities")

const testToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI0IiwiaWF0IjoxNjQxNTg3NzA0fQ.zraT71sfAcTaxGoY8Xs00bUo3w6ikY1Tu-hnCjIJQJQ"

class ArtistHandler {
  static async getHandler(req, res) {
    let artist = await entities.Artist.getArtistById(utils.parseId(req.url), testToken);
    await artist.getAlbumList(testToken)

    utils.renderPage(res, "artist.ejs", {artist: artist}, 1);
  }
  static async postHandler(req, res) {}
  static async putHandler(req, res) {}
  static async deleteHandler(req, res) {}
}


class AlbumHandler {
  static async getHandler(req, res) {
    let album = await entities.Album.getAlbumById(utils.parseId(req.url), testToken);
    await album.getTrackList(testToken)

    utils.renderPage(res, "album.ejs", {album: album}, 1);
  }
  static async postHandler(req, res) {}
  static async putHandler(req, res) {}
  static async deleteHandler(req, res) {}
}


class IndexHandler {
  static async getHandler(req, res) {
    utils.renderPage(res, "index.html", null);
  }
  static async postHandler(req, res) {}
  static async putHandler(req, res) {}
  static async deleteHandler(req, res) {}
}


class LibraryHandler {
  static async getHandler(req, res) {
    var data = {};
    utils.renderPage(res, "library.html", data, frame.compiled);
  }
  static async postHandler(req, res) {}
  static async putHandler(req, res) {}
  static async deleteHandler(req, res) {}
}


class LoginHandler {
  static async getHandler(req, res, status = 0) {
    var data = { status: status };
    utils.renderPage(res, "login.html", data);
  }
  static async postHandler(req, res, body) {
    var post = qs.parse(body);
    if (post.username == "admin" && post.password == "admin") {
      utils.redirect(res, "/management/user");
    } else {
      res.setHeader(
        "Set-Cookie",
        cookie.serialize("id", post.username, {
          httpOnly: true,
          maxAge: 60 * 60 * 24 * 30, // 1 month
        })
      );
    }
    console.log(
      `[Log][Login]Login attempt with email=${post.username}, password=${post.password}`
    );
    utils.redirect(res, "/library");
    //login(post.username, post.password);
  }
  static async putHandler(req, res) {}
  static async deleteHandler(req, res) {}
}

class AssetsHandler {
  static async getHandler(req, res) {
    var filePath = url.parse(req.url).pathname;
    var file = fs.readFileSync("src/wwwroot" + filePath, "UTF-8");
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
  static async postHandler(req, res) {}
}

exports.AlbumHandler = AlbumHandler
exports.ArtistHandler = ArtistHandler
exports.AssetsHandler = AssetsHandler
exports.IndexHandler = IndexHandler
exports.LibraryHandler = LibraryHandler
exports.LoginHandler = LoginHandler
