const utils = require("./utils");
const entities = require("../entities");
const AlbumHandler = require("./albumHandler").Handler;
const UserHandler = require("./userHandler").UserHandler;
const PlaylistHandler = require("./albumHandler").Handler;

const DIR = "management";

exports.Handler = class {
	static async getHandler(req, res) {
		// if (!res.params) {
		// 	res.redirect(301, "/management/users");
		// 	return;
		// }
		console.log(req.params);
		switch (req.params["entity"]) {
			case "albums":
				AlbumHandler.getHandler(req, res, 1);
				break;
			case "users":
				UserHandler.getHandler(req, res, 1);
				break;
			case "playlists":
				PlaylistHandler.getHandler(req, res, 1);
			case undefined:
				break;
			default:
				console.log(utils.getPath(req.url, 2));
		}
	}
};

exports.AlbumManHandler = class {
	static async getHandler(req, res) {
		let id = req.params.id;
		const raw = utils.formatIsRaw(req);
		const token = utils.getToken(req);
		let data, page;

		if (id) {
			let type = req.params.type;
			let album = await entities.Album.getAlbumById(id, token);
			let fname = `${album.name} by ${album.artist_name}`;
			switch (type) {
				case undefined:
					let artistList = await entities.Artist.getArtistList(token);

					page = `${DIR}/album/album_detail.ejs`;
					data = { album: album, artistList: artistList, title: `${fname} - Detail` };
					break;

				case "tracks":
					await album.getTrackList(token)

					page = `${DIR}/album/album_tracklist.ejs`;
					data = { album: album, title: `${fname} - Tracklist`  };
					break;

				case "comments":
					let commentList = await entities.Comment.getCommentsByAlbumId(id, token);

					page = `${DIR}/album/album_comments.ejs`;
					data = { commentList: commentList, title: `${fname} - Comments`  };
					break;
			}
		} else {
			let albumList = await entities.Album.getAlbumList(token);
			let artistList = await entities.Artist.getArtistList(token);

			page = `${DIR}/man_album.ejs`;
			data = { albumList: albumList, artistList: artistList, title: "Album Management" };
		}

		utils.renderPage(res, page, data, raw ? utils.FORMAT_RAW : utils.FORMAT_MAN);
	}
};

exports.UserManHandler = class {
	static async getHandler(req, res) {
		let id = req.params.id;
		const raw = utils.formatIsRaw(req);
		let page, data;
		if (id) {
			let type = req.params.type;
			switch (type) {
				case undefined:
					let user = await entities.User.getUserById(id, utils.getToken(req));
					page = `${DIR}/user/user_detail.ejs`;
					data = { user: user };
					break;
				case "playlists":
					let playlists = await entities.Playlist.getPlaylistList(token, id);
					page = `${DIR}/user/user_playlist.ejs`;
					break;
			}
		} else {
			let userList = await entities.User.getUserList(utils.getToken(req));
			page = `${DIR}/man_user.ejs`;
			data = { userList: userList, title: user.name };
		}
		utils.renderPage(res, page, data, raw ? utils.FORMAT_RAW : utils.FORMAT_MAN);
	}
};
