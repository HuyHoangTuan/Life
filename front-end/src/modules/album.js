const ejs = require("ejs");
const frame = require("./frame");
const utils = require("./utils");

class Album {
  constructor(id, title, artist, coverUrl) {
    this.id = id;
    this.title = title;
    this.artist = artist;
    this.coverUrl = coverUrl;
  }
}

const testAlbum1 = new Album(
  1,
  "The E.N.D. (The Energy Never Dies)",
  "Black Eyed Peas",
  "https://is5-ssl.mzstatic.com/image/thumb/Music115/v4/ee/78/af/ee78afcc-ad30-9296-515c-9d16507ece8c/source/600x600bb.jpg"
);
const testAlbum2 = new Album(
  2,
  "The Beginning",
  "Black Eyed Peas",
  "https://is4-ssl.mzstatic.com/image/thumb/Music118/v4/bb/c3/43/bbc34315-dbbe-f63f-00b5-833f195a3b88/source/600x600bb.jpg"
);
const testAlbum = new Album(2, "Test", "Test", "");

class AlbumHandler {
  static getHandler(req, res) {
    var id = utils.parseId(req.url);
    if (id == undefined) {
      var album = testAlbum;
    } else if (id == 1) {
      var album = testAlbum1;
    } else {
      var album = testAlbum2;
    }
    utils.renderPage(res, "album.html", album, frame.compiled);
  }
  static postHandler(req, res) {}
  static putHandler(req, res) {}
  static deleteHandler(req, res) {}
}

exports.Handler = AlbumHandler;
