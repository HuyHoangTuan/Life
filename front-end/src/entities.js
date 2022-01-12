const api = require("./api");

class Artist {
	artist_id;
	display_name;
	email;
	active;
	_albumList;
	constructor(obj) {
		Object.assign(this, obj);
	}
	get id() {
		return this.artist_id;
	}
	get name() {
		return this.display_name;
	}
	static async getArtistById(id, token) {
		return new Artist(
				JSON.parse(await api.doGet(`/artists/${id}`, { token: token }))
		);
	}
	async getAlbumList(token) {
		let rawAlbumList = JSON.parse(
			await api.doGet(`/artists/${this.id}/albums`, { token: token })
		);
		// console.log("RAWWWW" + rawAlbumList)
		this.albums = [];
		this.singles = [];
		this.compilations = [];
		rawAlbumList.forEach((rawAlbum) => {
			switch (rawAlbum.type) {
				case 1:
					this.albums.push(new Album(rawAlbum))
					break;
				case 2:
					this.singles.push(new Album(rawAlbum))
					break;
				case 3:
					this.compilations.push(new Album(rawAlbum))
					break;
			}
		});
	}
	async getAlbumsByType(type) {
		if (this._albumList == null) {
			console.log("");
		}
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
	static async getAlbumById(id, token) {
		return new Album(
			JSON.parse(await api.doGet(`/albums/${id}`, { token: token }))[0]
		);
	}
	async getTrackList(token) {
		let rawTrackList = JSON.parse(
			await api.doGet(`/albums/${this.id}/tracks`, { token: token })
		);
		// console.log("RAWWWW" + rawAlbumList)
		this.tracks = [];
		rawTrackList.forEach((rawTrack) => {
			this.tracks.push(new Track(rawTrack))
		});
		this.tracks.sort((a, b)  => {return a.track_num - b.track_num})
	}
}

class Track {
	track_id
	album_id
	album_name
	track_num
	track_name
	duration
	constructor(obj) {
		Object.assign(this, obj);
	}
	get id() {
		return this.track_id;
	}
	get title() {
		return this.track_name;
	}
	static async getTrackById(id, token) {
		return new Track(
			JSON.parse(await api.doGet(`/tracks/${id}`, { token: token }))[0]
		);
	}
}

exports.Artist = Artist
exports.Album = Album
exports.Track = Track