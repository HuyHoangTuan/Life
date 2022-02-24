const utils = require("./utils");
const entities = require("../entities");
const api = require("../api");
// const AlbumHandler = require("./albumHandler").Handler;
// const UserHandler = require("./userHandler").UserHandler;
// const PlaylistHandler = require("./albumHandler").Handler;

const DIR = "management";

exports.Handler = class {
	static async getHandler(req, res) {
		let totalUser = JSON.parse(await api.doGet("/users/total"), {token: req.token});
		let totalAlbum = JSON.parse(await api.doGet("/albums/total"), {token: req.token});
		let totalSong = JSON.parse(await api.doGet("/tracks/total"), {token: req.token});
		utils.renderPage(res,`${DIR}/management.ejs`, {
			totalUser: totalUser.total,
			totalAlbum: totalAlbum.total,
			totalSong: totalSong.total
		}, req.raw ? utils.FORMAT_RAW : utils.FORMAT_MAN)
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
					data = { album: album, commentList: commentList, title: `${fname} - Comments`  };
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

exports.PlaylistManHandler = class {
	static async getHandler(req, res) {
		let id = req.params.id;
		let data, page;

		if (id) {
			let type = req.params.type;
			let playlist = await entities.Playlist.getPlaylistById(id, req.token);
			let fname = `${playlist.name} by ${playlist.display_name}`;
			switch (type) {
				case undefined:
					let userList = await entities.User.getUserList(req.token);

					page = `${DIR}/playlist/playlist_detail.ejs`;
					data = { playlist: playlist, userList: userList, title: `${fname} - Detail` };
					break;

				case "tracks":
					await album.getTrackList(req.token)

					page = `${DIR}/playlist/playlist_tracklist.ejs`;
					data = { playlist: playlist, title: `${fname} - Tracklist`  };
					break;

				case "comments":
					let commentList = await entities.Comment.getCommentsByPlaylistId(id, req.token);

					page = `${DIR}/playlist/playlist_comments.ejs`;
					data = { playlist: playlist, commentList: commentList, title: `${fname} - Comments`  };
					break;
			}
		} else {
			let playlists = await entities.Playlist.getPlaylistList(req.token);
			let userList = await entities.User.getUserList(req.token);

			page = `${DIR}/man_playlist.ejs`;
			data = { playlists: playlists, userList: userList, title: "Playlist Management" };
		}

		utils.renderPage(res, page, data, req.raw ? utils.FORMAT_RAW : utils.FORMAT_MAN);
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
			data = { userList: userList, title: "User Managament" };
		}
		utils.renderPage(res, page, data, raw ? utils.FORMAT_RAW : utils.FORMAT_MAN);
	}
};
