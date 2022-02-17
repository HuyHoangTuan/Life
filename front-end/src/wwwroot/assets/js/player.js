var player;
var sPlayer;

var sliderTime;
var sliderVol;
var currentSong;
var currentArtist;
var timeElapsed;
var timeLength;
var buttonPlay;

var playingTrackId;
var queue = [];

var preloaded = false;

function initPlayer(type = 0) {
	let p = document.createElement("AUDIO");
	p.volume = 0.5;
	p.onended = () => {
		let temp = player;
		player = sPlayer;
		sPlayer = temp;
		console.log("ended");

		player.play();

		preloaded = false;
	};
	p.onpause = () => {
		// stopSlider(songSlider);
		buttonPlay.classList.toggle("paused");
		console.log("Player paused");
	};
	p.onplay = () => {
		buttonPlay.classList.toggle("paused");
		console.log("Player is trying to play");
	};
	p.ontimeupdate = () => {
		sliderTime.value = (p.currentTime * 100) / p.duration;
		timeElapsed.innerHTML = toTime(p.currentTime);

		if (p.duration - p.currentTime < 30 && !preloaded) {
			if ((type = 1)) {
				preload(sPlayer);
			} else {
				preload(player);
			}
		}
		// console.log(player.currentTime)
	};
	p.onplaying = () => {
		// sliderTime.setAutoSlideTime(player.duration - player.currentTime);
		// sliderTime.startAutoSlide();
		console.log("Player is playing");
	};
	p.onseeked = () => {
		// if (player.paused) {
		// 	stopSlider(songSlider);
		// } else {
		// 	updateSongSlider();
		// }
		// console.log("Seek");
		console.log("Player is seekd");
	};
	return p;
}

function preload(p) {
	preloaded = true;
	console.log("preloaded");
	p.src = `/tracks/${queue.pop()}/audio`;
	p.load();
}

function init() {
	sliderTime = document.getElementById("slider-time");
	currentSong = document.getElementById("current-song");
	currentArtist = document.getElementById("current-artist");
	timeElapsed = document.getElementById("time-elapsed");
	timeLength = document.getElementById("time-length");
	buttonPlay = document.getElementById("button-play");
	player = initPlayer(1);
	sPlayer = initPlayer(0);
}

function buildPlaylist() {
	let tracks = document.querySelectorAll("[track-id]");
	let flag = false;
	tracks.forEach((track) => {
		let id = track.getAttribute("track-id");
		if (flag) {
			queue.push({
				id: id,
				albumId: track.getAttribute("album-id"),
				artistId: track.getAttribute("artist-id"),
				artistName: track.getAttribute("artist-name"),
			});
		}
		if (id == playingTrackId) {
			flag = true;
		}
	});
	queue.reverse();
}

function playTrack(track) {
	let id = track.getAttribute("track-id");
	playingTrackId = id;
	buildPlaylist();
	var url = `/tracks/${id}/audio`;
	if (player == undefined) {
		init();
	}
	player.src = url;
	player.addEventListener("loadeddata", () => {
		if (player.readyState >= 2) {
			// player.play();
		} else {
			console.log("Failed to load song");
		}
	});
	player.load();
	sliderTime.value = 0;
	player.play();
	updateFields({
		id: id,
		title: track.getAttribute("track-title"),
		albumId: track.getAttribute("album-id"),
		artistId: track.getAttribute("artist-id"),
		artistName: track.getAttribute("artist-name"),
		artistName: track.getAttribute("artist-name")
	});
}

function toTime(second) {
	return new Date(null, null, null, null, null, second)
		.toTimeString()
		.substr(3, 5);
}

function updateFields(track) {
	currentArtist.innerHTML = track.artistName
	currentSong.innerHTML = track.title
	timeLength.innerHTML = track.length
}

function toggleMusic() {
	if (player != undefined) {
		if (player.paused) {
			player.play();
		} else {
			player.pause();
		}
	}
}

function updateSongSlider() {
	setSliderTime(songSlider, player.duration - player.currentTime);
}

function seek(value) {
	player.currentTime = (value * player.duration) / 100;
	//updateSongSlider();
}
