const api = require("./api");

class User {
	id;
	display_name;
	email;
	active;
	role;
	constructor(obj) {
		Object.assign(this, obj);
	}
	get name() {
		return this.display_name;
	}
	get roleName() {
		switch (this.role) {
			case 0:
				return "Admin";
			case 1:
				return "Artist";
			case 2:
				return "User";
		}
	}
	static async getUserById(id, token) {
		return new User(
			JSON.parse(await api.doGet(`/users/${id}`, { token: token }))
		);
	}

	static async getUserList(token) {
		let rawUserList = JSON.parse(
			await api.doGet("/users", { token: token })
		);
		// console.log("RAWWWW" + rawAlbumList)
		let userList = [];
		rawUserList.forEach((rawUser) => {
			userList.push(new User(rawUser));
		});
		return userList;
	}
}

class Artist {
	id;
	display_name;
	email;
	active;
	_albumList;
	constructor(obj) {
		Object.assign(this, obj);
	}
	get name() {
		return this.display_name;
	}
	static async getArtistById(id, token) {
		return new Artist(
			JSON.parse(await api.doGet(`/artists/${id}`, { token: token }))
		);
	}

	static async getArtistList(token) {
		let rawArtistList = JSON.parse(
			await api.doGet("/artists", { token: token })
		);
		// console.log("RAWWWW" + rawAlbumList)
		let artistList = [];
		rawArtistList.forEach((rawArtist) => {
			artistList.push(new Artist(rawArtist));
		});
		return artistList;
	}

	static async searchArtist(keyword, token) {
		let rawArtistList = JSON.parse(
			await api.doGet("/search/artists", {
				content: keyword,
				token: token,
			})
		);
		// console.log("RAWWWW" + rawAlbumList)
		let artistList = [];
		rawArtistList.forEach((rawArtist) => {
			artistList.push(new Artist(rawArtist));
		});
		return artistList;
	}

	async getAlbumList(token) {
		let albumList = await Album.getAlbumList(token, this.id);
		this.albums = [];
		this.singles = [];
		this.compilations = [];
		albumList.forEach((album) => {
			switch (album.type) {
				case 1:
					this.albums.push(album);
					break;
				case 2:
					this.singles.push(album);
					break;
				case 3:
					this.compilations.push(album);
					break;
			}
		});
	}
}

class Album {
	album_id;
	title;
	artist_id;
	artist_name;
	release_date;
	type;
	constructor(obj) {
		Object.assign(this, obj);
	}
	get id() {
		return this.album_id;
	}
	get name() {
		return this.title;
	}
	get typeName() {
		switch (this.type) {
			case 1:
				return "Album";
			case 2:
				return "Single/EP";
			case 3:
				return "Compilation";
		}
	}
	static async getAlbumById(id, token) {
		return new Album(
			JSON.parse(await api.doGet(`/albums/${id}`, { token: token }))
		);
	}

	static async getAlbumList(token, artistId = -1) {
		var path = "/albums";
		if (artistId != -1) {
			path = `/artists/${artistId}/albums`;
		}
		let rawAlbumList = JSON.parse(await api.doGet(path, { token: token }));
		let albumList = [];
		rawAlbumList.forEach((rawAlbum) => {
			albumList.push(new Album(rawAlbum));
		});
		return albumList;
	}

	static async searchAlbum(keyword, token) {
		let rawAlbumList = JSON.parse(
			await api.doGet("/search/albums", {
				content: keyword,
				token: token,
			})
		);
		let albumList = [];
		rawAlbumList.forEach((rawAlbum) => {
			albumList.push(new Album(rawAlbum));
		});
		return albumList;
	}

	async getTrackList(token) {
		this.tracks = await Track.getTrackList(token, this.id);
	}
}

class Track {
	track_id;
	album_id;
	album_name;
	artist_id;
	artist_name;
	track_num;
	track_name;
	duration;
	constructor(obj) {
		Object.assign(this, obj);
		this.duration = Math.round(this.duration);
	}
	get id() {
		return this.track_id;
	}
	get title() {
		return this.track_name;
	}
	set title(value) {
		this.track_name = value;
	}
	static async getTrackById(id, token) {
		return new Track(
			JSON.parse(await api.doGet(`/tracks/${id}`, { token: token }))[0]
		);
	}
	static async getTrackList(token, albumId = -1) {
		var path = "/tracks";
		if (albumId != -1) {
			path = `/albums/${albumId}/tracks`;
		}
		let rawTrackList = JSON.parse(await api.doGet(path, { token: token }));
		// console.log("RAWWWW" + rawAlbumList)
		let trackList = [];
		rawTrackList.forEach((rawTrack) => {
			trackList.push(new Track(rawTrack));
		});
		trackList.sort((a, b) => {
			return a.track_num - b.track_num;
		});
		return trackList;
	}

	static async searchTrack(keyword, token) {
		let rawTrackList = JSON.parse(
			await api.doGet("/search/tracks", {
				content: keyword,
				token: token,
			})
		);

		let trackList = [];
		rawTrackList.forEach((rawTrack) => {
			trackList.push(new Track(rawTrack));
		});
		// console.log(trackList)
		return trackList;
	}
}

exports.Artist = Artist;
exports.Album = Album;
exports.Track = Track;
exports.User = User;
