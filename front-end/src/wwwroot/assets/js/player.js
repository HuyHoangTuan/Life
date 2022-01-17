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

// function initPlayer(type = 0) {
// 	p = document.createElement("AUDIO");
// 	p.volume = 0.5;
// 	p.onended = () => {
// 		if ((type = 1)) {
// 			preload(sPlayer);
// 		} else {
// 			preload(player);
// 		}
// 	}
// 	p.onpause = () => {
// 		// stopSlider(songSlider);
// 		buttonPlay.classList.toggle("paused");
// 		console.log("Player paused");
// 	};
// 	p.onplay = () => {
// 		buttonPlay.classList.toggle("paused");
// 		console.log("Player is trying to play");
// 	};
// 	p.ontimeupdate = () => {
// 		sliderTime.value = (p.currentTime * 100) / p.duration;
// 		timeElapsed.innerHTML = toTime(p.currentTime);

// 		if (p.currentTime - p.duration < 30) {
// 			if ((type = 1)) {
// 				preload(sPlayer);
// 			} else {
// 				preload(player);
// 			}
// 		}
// 		// console.log(player.currentTime)
// 	};
// 	p.onplaying = () => {
// 		// sliderTime.setAutoSlideTime(player.duration - player.currentTime);
// 		// sliderTime.startAutoSlide();
// 		console.log("Player is playing");
// 	};
// 	p.onseeked = () => {
// 		// if (player.paused) {
// 		// 	stopSlider(songSlider);
// 		// } else {
// 		// 	updateSongSlider();
// 		// }
// 		// console.log("Seek");
// 		console.log("Player is seekd");
// 	};
// 	return p;
// }

function preload(p) {
	p.src = `/tracks/${queue.pop()}/audio`;
	p.load();
}

function init() {
	player = document.createElement("AUDIO");
	player.volume = 0.5;
	player.onpause = () => {
		// stopSlider(songSlider);
		buttonPlay.classList.toggle("paused");
		console.log("Player paused");
	};
	player.onplay = () => {
		buttonPlay.classList.toggle("paused");
		console.log("Player is trying to play");
	};
	player.ontimeupdate = () => {
		sliderTime.value = (player.currentTime * 100) / player.duration;
		timeElapsed.innerHTML = toTime(player.currentTime);

		if (player.currentTime - player.duration < 30) {
			if ((type = 1)) {
				preload(sPlayer);
			} else {
				preload(player);
			}
		}
		// console.log(player.currentTime)
	};
	player.onplaying = () => {
		// sliderTime.setAutoSlideTime(player.duration - player.currentTime);
		// sliderTime.startAutoSlide();
		console.log("Player is playing");
	};
	player.onseeked = () => {
		// if (player.paused) {
		// 	stopSlider(songSlider);
		// } else {
		// 	updateSongSlider();
		// }
		// console.log("Seek");
		console.log("Player is seekd");
	};
	sliderTime = document.getElementById("slider-time");
	currentSong = document.getElementById("current-song");
	currentArtist = document.getElementById("current-artist");
	timeElapsed = document.getElementById("time-elapsed");
	timeLength = document.getElementById("time-length");
	buttonPlay = document.getElementById("button-play");
	// player = initPlayer(1);
	// sPlayer = initPlayer(0);
}

function buildPlaylist() {
	let tracks = document.querySelectorAll("[track-id]");
	tracks.forEach((track) => {
		let id = track.getAttribute("track-id");
		if (id == playingTrackId) {
			queue.push(id);
		}
	});
	queue.reverse();
	queue.pop();
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
			player.play();
		} else {
			console.log("Failed to load song");
		}
	});
	player.load();
	sliderTime.value = 0;
	updateFields(track);
}

function toTime(second) {
	return new Date(null, null, null, null, null, second)
		.toTimeString()
		.substr(3, 5);
}

function updateFields(track) {
	currentArtist.innerHTML = track.getAttribute("track-artist-name");
	currentSong.innerHTML = track.getAttribute("track-title");
	timeLength.innerHTML = toTime(track.getAttribute("length"));
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
